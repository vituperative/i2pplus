/* PeerCoordinator - Coordinates which peers do what (up and downloading).
   Copyright (C) 2003 Mark J. Wielaard
   This file is part of Snark.
   Licensed under the GPL version 2 or later.
*/

package org.klomp.snark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import net.i2p.I2PAppContext;
import net.i2p.data.ByteArray;
import net.i2p.data.DataHelper;
import net.i2p.data.Destination;
import net.i2p.util.ConcurrentHashSet;
import net.i2p.util.I2PAppThread;
import net.i2p.util.Log;
import net.i2p.util.RandomSource;
import net.i2p.util.SimpleTimer2;

import org.klomp.snark.bencode.BEValue;
import org.klomp.snark.bencode.InvalidBEncodingException;
import org.klomp.snark.comments.Comment;
import org.klomp.snark.comments.CommentSet;
import org.klomp.snark.dht.DHT;

/**
 * Coordinates what peer does what.
 */
class PeerCoordinator implements PeerListener, BandwidthListener {
  private final Log _log;

    /**
     * External use by PeerMonitorTask only.
     * Will be null when in magnet mode.
     */
    MetaInfo metainfo;

    /**
     * External use by PeerMonitorTask only.
     * Will be null when in magnet mode.
     */
    Storage storage;
    private final Snark snark;

    // package local for access by CheckDownLoadersTask
    final static long CHECK_PERIOD = 5*1000; // update download speed in UI and choke/unchoke tests
    final static int MAX_UPLOADERS = 16;
    public static final long MAX_INACTIVE = 5*60*1000; // how long before we disconnect from an inactive peer
    public static final long MAX_SEED_INACTIVE = 3*60*1000;

    /**
     * Approximation of the number of current uploaders (unchoked peers),
     * whether interested or not.
     * Resynced by PeerChecker once in a while.
     */
    private final AtomicInteger uploaders = new AtomicInteger();

    /**
     * Approximation of the number of current uploaders (unchoked peers),
     * that are interested.
     * Resynced by PeerChecker once in a while.
     */
    private final AtomicInteger interestedUploaders = new AtomicInteger();

    /**
     * External use by PeerCheckerTask only.
     */
    private final AtomicInteger interestedAndChoking = new AtomicInteger();

    // final static int MAX_DOWNLOADERS = MAX_CONNECTIONS;
    // int downloaders = 0;

    private final AtomicLong uploaded = new AtomicLong();
    private final AtomicLong downloaded = new AtomicLong();
    final static int RATE_DEPTH = 3; // make following arrays RATE_DEPTH long
    private final long uploaded_old[] = {-1,-1,-1};
    private final long downloaded_old[] = {-1,-1,-1};

    /**
     * synchronize on this when changing peers or downloaders.
     * This is a Queue, not a Set, because PeerCheckerTask keeps things in order for choking/unchoking.
     * External use by PeerMonitorTask only.
     */
    final Deque<Peer> peers;

    /**
     * Peers we heard about via PEX
     */
    private final Set<PeerID> pexPeers;

    /** estimate of the peers, without requiring any synchronization */
    private volatile int peerCount;

    /** Timer to handle all periodical tasks. */
    private final CheckEvent timer;

    // RerequestEvent and related values
    private final SimpleTimer2.TimedEvent rerequestTimer;
    private final Object rerequestLock = new Object();
    private boolean wasRequestAllowed;
    private boolean isRerequestScheduled;

    private final byte[] id;
    private final byte[] infohash;

    /** The wanted pieces. We could use a TreeSet but we'd have to clear and re-add everything
     *  when priorities change.
     */
    private final List<Piece> wantedPieces;

    /** The total number of bytes in wantedPieces, or -1 if not yet known.
     *  Sync on wantedPieces.
     *  @since 0.9.1
     */
    private long wantedBytes;

    /** partial pieces - lock by synching on wantedPieces - TODO store Requests, not PartialPieces */
    private final List<PartialPiece> partialPieces;

    private volatile boolean halted;

    private final MagnetState magnetState;
    private final CoordinatorListener listener;
    private final BandwidthListener bwListener;
    private final I2PSnarkUtil _util;
    private final RandomSource _random;

    private final AtomicLong _commentsLastRequested = new AtomicLong();
    private final AtomicInteger _commentsNotRequested = new AtomicInteger();
    private static final long COMMENT_REQ_INTERVAL = 12*60*60*1000L;
    private static final long COMMENT_REQ_DELAY = 60*60*1000L;
    private static final int MAX_COMMENT_NOT_REQ = 10;

    /** hostname to expire time, sync on this */
    private Map<String, Long> _webPeerBans;
    private static final long WEBPEER_BAN_TIME = 30*60*1000L;

    /**
     *  @param metainfo null if in magnet mode
     *  @param storage null if in magnet mode
     */
    public PeerCoordinator(I2PSnarkUtil util, byte[] id, byte[] infohash, MetaInfo metainfo, Storage storage,
                           CoordinatorListener listener, Snark torrent, BandwidthListener bwl) {
        _util = util;
        _random = util.getContext().random();
        _log = util.getContext().logManager().getLog(PeerCoordinator.class);
        this.id = id;
        this.infohash = infohash;
        this.metainfo = metainfo;
        this.storage = storage;
        this.listener = listener;
        this.snark = torrent;
        bwListener = bwl;

        wantedPieces = new ArrayList<Piece>();
        setWantedPieces();
        partialPieces = new ArrayList<PartialPiece>(getMaxConnections() + 1);
        peers = new LinkedBlockingDeque<Peer>();
        magnetState = new MagnetState(infohash, metainfo);
        pexPeers = new ConcurrentHashSet<PeerID>();

        // Install a timer to check the uploaders.
        // Randomize the first start time so multiple tasks are spread out,
        // this will help the behavior with global limits
        timer = new CheckEvent(_util.getContext(), new PeerCheckerTask(_util, this));
        timer.schedule((CHECK_PERIOD * 8 / 2) + _random.nextInt((int) CHECK_PERIOD * 8));

        // NOT scheduled until needed
        rerequestTimer = new RerequestEvent();

        // we don't store the last-requested time, so just delay a random amount
        _commentsLastRequested.set(util.getContext().clock().now() - (COMMENT_REQ_INTERVAL - _random.nextLong(COMMENT_REQ_DELAY)));
    }

    /**
     *  Run the PeerCheckerTask via the SimpleTimer2 executors
     *  @since 0.8.2
     */
    private static class CheckEvent extends SimpleTimer2.TimedEvent {
        private final PeerCheckerTask _task;
        public CheckEvent(I2PAppContext ctx, PeerCheckerTask task) {
            super(ctx.simpleTimer2());
            _task = task;
        }
        public void timeReached() {
            _task.run();
            schedule(CHECK_PERIOD);
        }
    }

    /**
     *  Rerequest after unthrottled
     *  @since 0.9.62
     */
    private class RerequestEvent extends SimpleTimer2.TimedEvent {
        /** caller must schedule */
        public RerequestEvent() {super(_util.getContext().simpleTimer2());}

        public void timeReached() {
            if (bwListener.shouldRequest(null, 0)) {
                if (_log.shouldWarn()) {_log.warn("Now unthrottled -> Rerequesting timer, poking all peers...");}
                // so shouldRequest() won't fire us up again
                synchronized(rerequestLock) {wasRequestAllowed = true;}
                for (Peer p : peers) {
                    if (p.isInteresting() && !p.isChoked()) {p.request();}
                }
                synchronized(rerequestLock) {isRerequestScheduled = false;}
            } else {
                if (_log.shouldWarn()) {_log.warn("Still throttled -> Rerequesting timer reschedule...");}
                synchronized(rerequestLock) {wasRequestAllowed = false;}
                schedule(2*1000);
            }
        }
    }

    /**
     * Only called externally from Storage after the double-check fails.
     * Sets wantedBytes too.
     */
    public void setWantedPieces() {
        if (metainfo == null || storage == null) {
            wantedBytes = -1;
            return;
        }

        // Make a list of pieces
        synchronized(wantedPieces) {
            wantedPieces.clear();
            BitField bitfield = storage.getBitField();
            int[] pri = storage.getPiecePriorities();
            long count = 0;
            for (int i = 0; i < metainfo.getPieces(); i++) {
                // only add if we don't have and the priority is >= 0
                if ((!bitfield.get(i)) &&
                    (pri == null || pri[i] >= 0)) {
                    Piece p = new Piece(i);
                    if (pri != null)
                        p.setPriority(pri[i]);
                    wantedPieces.add(p);
                    count += metainfo.getPieceLength(i);
                }
            }
            wantedBytes = count;
            Collections.shuffle(wantedPieces, _random);
        }
    }

    public Storage getStorage() {return storage;}

    /** for web page detailed stats */
    public List<Peer> peerList() {return new ArrayList<Peer>(peers);}
    public byte[] getID() {return id;}
    public String getName() {return snark.getName();}

    public boolean completed() {
        // FIXME return metainfo complete status
        if (storage == null) {return false;}
        return storage.complete();
    }

    /** might be wrong */
    public int getPeerCount() {return peerCount;}

    /** should be right */
    public int getPeers() {
        int rv = peers.size();
        peerCount = rv;
        return rv;
    }

    /**
     *  Bytes not yet in storage. Does NOT account for skipped files.
     * Returns how many bytes are still needed to get the complete torrent.
     * @return -1 if in magnet mode
     */
    public long getLeft() {
        if (metainfo == null | storage == null) {return -1;}
        int psz = metainfo.getPieceLength(0);
        long rv = ((long) storage.needed()) * psz;
        int last = metainfo.getPieces() - 1;
        BitField bf = storage.getBitField();
        if (bf != null && !bf.get(last)) {rv -= psz - metainfo.getPieceLength(last);}
        return rv;
    }

    /**
     *  Bytes still wanted. DOES account for skipped files.
     *  @return exact value. or -1 if no storage yet.
     *  @since 0.9.1
     */
    public long getNeededLength() {return wantedBytes;}

    /**
     * Returns the total number of uploaded bytes of all peers.
     */
    public long getUploaded() {return uploaded.get();}

    /**
     *  Sets the initial total of uploaded bytes of all peers (from a saved status)
     *  @since 0.9.15
     */
    public void setUploaded(long up) {uploaded.set(up);}

    /**
     * Returns the total number of downloaded bytes of all peers.
     */
    public long getDownloaded() {return downloaded.get();}

    /////// begin BandwidthListener interface ///////

    /**
     * Called when a peer has uploaded some bytes of a piece.
     * @since 0.9.62 params changed
     */
    public void uploaded(int size) {
        uploaded.addAndGet(size);
        bwListener.uploaded(size);
    }

    /**
     * Called when a peer has downloaded some bytes of a piece.
     * @since 0.9.62 params changed
     */
    public void downloaded(int size) {
        downloaded.addAndGet(size);
        bwListener.downloaded(size);
    }

    /**
     * Should we send this many bytes?
     * Do NOT call uploaded() if this returns true.
     * @since 0.9.62
     */
    public boolean shouldSend(int size) {
        boolean rv = bwListener.shouldSend(size);
        if (rv) {uploaded.addAndGet(size);}
        return rv;
    }

    /**
     * Should we request this many bytes?
     * @since 0.9.62
     */
    public boolean shouldRequest(Peer peer, int size) {
        boolean rv;
        synchronized(rerequestLock) {
            rv = bwListener.shouldRequest(peer, size);
            if (!wasRequestAllowed && rv) {
                // We weren't allowed and now we are
                if (isRerequestScheduled) {
                    // Just let the timer run when scheduled, do not pull it in
                    // to prevent thrashing
                    if (_log.shouldInfo()) {
                        _log.info("Now unthrottled, but not rescheduling rerequest timer to prevent thrashing");
                    }
                } else {
                    // Schedule the timer
                    // we still have to throw it to the timer so we don't loop
                    if (_log.shouldWarn()) {_log.warn("Now unthrottled, scheduling rerequest timer...");}
                    isRerequestScheduled = true;
                    // no rush, wait at little while
                    rerequestTimer.reschedule(1000);
                }
                wasRequestAllowed = true;
            } else if (wasRequestAllowed && !rv) {
                // we were allowed and now we aren't
                if (!isRerequestScheduled) {
                    // schedule the timer
                    if (_log.shouldWarn()) {_log.warn("Now throttled, scheduling rerequest timer...");}
                    isRerequestScheduled = true;
                    rerequestTimer.schedule(3*1000);
                }
                wasRequestAllowed = false;
            }
        }
        return rv;
    }

    /**
     * Push the total uploaded/downloaded onto a RATE_DEPTH deep stack
     */
    public void setRateHistory(long up, long down) {
        setRate(up, uploaded_old);
        setRate(down, downloaded_old);
    }

    static void setRate(long val, long array[]) {
        synchronized(array) {
            for (int i = RATE_DEPTH-1; i > 0; i--) {
                array[i] = array[i-1];
                array[0] = val;
            }
        }
    }

    /**
     * Returns the average rate in Bps
     * over last RATE_DEPTH * CHECK_PERIOD seconds
     */
    public long getDownloadRate() {
        if (halted) {return 0;}
        return getRate(downloaded_old);
    }

    /**
     * Returns the average rate in Bps
     * over last RATE_DEPTH * CHECK_PERIOD seconds
     */
    public long getUploadRate() {
        if (halted) {return 0;}
        return getRate(uploaded_old);
    }

    /**
     * Returns the rate in Bps
     * over last complete CHECK_PERIOD seconds
     */
    public long getCurrentUploadRate() {
        if (halted) {return 0;}
        // no need to synchronize, only one value
        long r = uploaded_old[0];
        if (r <= 0) {return 0;}
        return (r * 1000) / CHECK_PERIOD;
    }

    static long getRate(long array[]) {
        long rate = 0;
        int i = 0;
        int factor = 0;
        synchronized(array) {
            for ( ; i < RATE_DEPTH; i++) {
                if (array[i] < 0)
                break;
                int f = RATE_DEPTH - i;
                rate += array[i] * f;
                factor += f;
            }
        }
        if (i == 0) {return 0;}
        return rate / (factor * CHECK_PERIOD / 1000);
    }

    /**
     * Current limit in Bps
     * @since 0.9.62
     */
    public long getUpBWLimit() {return bwListener.getUpBWLimit();}

    /**
     *  Is snark as a whole over its limit?
     */
    public boolean overUpBWLimit() {return bwListener.overUpBWLimit();}

    /**
     *  Is a particular peer who has downloaded this many bytes from us
     *  in the last CHECK_PERIOD over its limit?
     */
    public boolean overUpBWLimit(long total) {
        return total * 1000 / CHECK_PERIOD > getUpBWLimit();
    }

    /**
     * Current limit in Bps
     * @since 0.9.62
     */
    public long getDownBWLimit() {return bwListener.getDownBWLimit();}

    /**
     * Are we currently over the limit?
     * @since 0.9.62
     */
    public boolean overDownBWLimit() {return bwListener.overDownBWLimit();}

    /////// end BandwidthListener interface ///////

    public MetaInfo getMetaInfo() {return metainfo;}

    /** @since 0.8.4 */
    public byte[] getInfoHash() {return infohash;}

    /**
     *  Inbound.
     *  Not halted, peers &lt; max.
     *  @since 0.9.1
     */
    public boolean needPeers() {
        return !halted && peers.size() < getMaxConnections();
    }

    /**
     *  Outbound.
     *  Not halted, peers &lt; max, and need pieces.
     *  @since 0.9.1
     */
    public boolean needOutboundPeers() {
        //return wantedBytes != 0 && needPeers();
        // minus two to make it a little easier for new peers to get in on large swarms
        return (wantedBytes != 0 ||
                (_util.utCommentsEnabled() &&
                 // we should also check SnarkManager.getSavedCommentsEnabled() for this torrent,
                 // but that reads in the config file, there's no caching.
                 // TODO
                 _commentsLastRequested.get() < _util.getContext().clock().now() - COMMENT_REQ_INTERVAL)) &&
                 !halted &&
                 peers.size() < getMaxConnections() - 2 &&
                 (storage == null || !storage.isChecking());
    }

    /**
     *  Formerly used to reduce max if huge pieces to keep from ooming when leeching
     *  but now we don't
     *  @return usually I2PSnarkUtil.MAX_CONNECTIONS
     */
    private int getMaxConnections() {
        if (metainfo == null) {return 6;}

        int pieces = metainfo.getPieces();
        int max = _util.getMaxConnections();
        if (pieces <= 10) {if (max > 4) max = 4;}
        else if (pieces <= 25) {if (max > 10) max = 10;}
        else if (pieces <= 80) {if (max > 16) max = 16;}

        long bwl = getDownBWLimit();
        if (bwl < 32*1024) {
            max = Math.min(max, Math.max(6, (int) (I2PSnarkUtil.MAX_CONNECTIONS * bwl / (32*1024))));
        }
        return max;
    }

    public boolean halted() {return halted;}

    public void halt() {
        halted = true;
        List<Peer> removed = new ArrayList<Peer>();
        synchronized(peers) {
            // Stop peer checker task.
            timer.cancel();
            // Stop peers.
            removed.addAll(peers);
            peers.clear();
            peerCount = 0;
        }

        while (!removed.isEmpty()) {
            Peer peer = removed.remove(0);
            // disconnect() calls disconnected() calls removePeerFromPieces()
            peer.disconnect();
        }
        // delete any saved orphan partial piece
        synchronized (partialPieces) {
            for (PartialPiece pp : partialPieces) {pp.release();}
            partialPieces.clear();
        }
    }

    /**
     *  @since 0.9.1
     */
    public void restart() {
        halted = false;
        synchronized (uploaded_old) {Arrays.fill(uploaded_old, 0);}
        synchronized (downloaded_old) {Arrays.fill(downloaded_old, 0);}
        // failsafe
        synchronized(wantedPieces) {
            for (Piece pc : wantedPieces) {pc.clear();}
        }
        timer.schedule((CHECK_PERIOD / 2) + _random.nextInt((int) CHECK_PERIOD));
    }

    public void connected(Peer peer) {
        if (halted) {
            peer.disconnect(false);
            return;
        }

      Peer toDisconnect = null;
        synchronized(peers) {
            Peer old = peerIDInList(peer.getPeerID(), peers);
            if (old != null && old.getInactiveTime() > old.getMaxInactiveTime()) {
                // idle for 8 minutes, kill the old con (32KB/8min = 68B/sec minimum for one block)
                if (_log.shouldWarn()) {
                    _log.warn("Removing old peer [" + peer + "] - [" + old + "] inactive for " + old.getInactiveTime() + "ms");
                }
                peers.remove(old);
                toDisconnect = old;
                old = null;
            }
            if (old != null) {
                if (_log.shouldWarn()) {
                    _log.warn("Already connected to [" + peer + "] - [" + old + "] inactive for " + old.getInactiveTime() + "ms");
                }
                // toDisconnect = peer to get out of synchronized(peers)
                peer.disconnect(false); // Don't deregister this connection/peer.
            // Already checked in addPeer() but we could have gone over the limit since then
            } else if (peers.size() >= getMaxConnections()) {
                if (_log.shouldWarn()) {
                    _log.warn("Already at MAX_CONNECTIONS in connected() with peer [" + peer + "]");
                }
                // toDisconnect = peer to get out of synchronized(peers)
                peer.disconnect(false);
            } else {
                if (_log.shouldInfo()) {
                    // just for logging
                    String name;
                    if (metainfo == null) {name = "Magnet";}
                    else {name = metainfo.getName();}
                   _log.info("New connection to [" + peer + "] for " + name);
                }

                // We may have gotten the metainfo after the peer was created.
                if (metainfo != null) {peer.setMetaInfo(metainfo);}

                // Add it to the beginning of the list.
                // And try to optimistically make it a uploader.
                if (_util.getContext().random().nextInt(4) == 0) {peers.push(peer);}
                else {peers.add(peer);}
                peerCount = peers.size();
                unchokePeer();
            }
        }
        if (toDisconnect != null) {
            toDisconnect.disconnect(true); // ensure partial pieces are returned
            // disconnect() calls disconnected() but will not call removePeerFromPieces()
            // because it was removed from peers above
            removePeerFromPieces(toDisconnect);
        }
    }

    /**
     * @return peer if peer id  is in the collection, else null
     */
    private static Peer peerIDInList(PeerID pid, Collection<Peer> peers)
    {
      Iterator<Peer> it = peers.iterator();
      while (it.hasNext()) {
        Peer cur = it.next();
        if (pid.sameID(cur.getPeerID()))
          return cur;
      }
      return null;
    }

    /**
     * Add peer (inbound or outbound)
     * @return true if actual attempt to add peer occurs
     */
    public boolean addPeer(final Peer peer) {
        if (halted) {
            peer.disconnect(false);
            return false;
        }

        boolean need_more;
        int peersize = 0;
        synchronized(peers) {
            peersize = peers.size();
            // This isn't a strict limit, as we may have several pending connections;
            // thus there is an additional check in connected()
            need_more = (!peer.isConnected()) && peersize < getMaxConnections();
            // Check if we already have this peer before we build the connection
            if (need_more) {
                Peer old = peerIDInList(peer.getPeerID(), peers);
                need_more = old == null || old.getInactiveTime() > old.getMaxInactiveTime();
            }
        }

        if (need_more) {
            if (_log.shouldDebug()) {
                // just for logging
                String name;
                if (metainfo == null) {name = "Magnet";}
                else {name = metainfo.getName();}
                _log.debug("Adding peer [" + peer.getPeerID().toString() + "] for " + name, new Exception("add/run"));
            }

            // Run the peer with us as listener and the current bitfield.
            final PeerListener listener = this;
            final BitField bitfield;
            if (storage != null) {bitfield = storage.getBitField();}
            else {bitfield = null;}
            if (!peer.isIncoming() && wantedBytes == 0 && _log.shouldInfo()) {
                _log.info("Checking for new comments for: " + snark.getBaseName() + " from [" + peer + "]");
            }

            // if we aren't a seed but we don't want any more
            final boolean partialComplete = wantedBytes == 0 && bitfield != null && !bitfield.complete();
            Runnable r = new Runnable() {
                public void run() {
                    peer.runConnection(_util, listener, PeerCoordinator.this, bitfield, magnetState, partialComplete);
                }
            };

            String threadName = "Snark peer " + peer.toString();
            new I2PAppThread(r, threadName).start();
            return true;
        }

        if (_log.shouldDebug()) {
            if (peer.isConnected()) {
                _log.debug("Add peer already connected [" + peer + "]");
            } else {
                _log.debug("Not accepting extra peer [" + peer + "] (" +
                           "Connections: " + peersize + "/" + getMaxConnections() + ")");
            }
        }
        return false;
    }

    /**
     * (Optimistically) unchoke. Must be called with peers synchronized
     */
    void unchokePeer() {
        if (storage == null || storage.getBitField().size() == 0 || overUpBWLimit()) {return;}

        // linked list will contain all interested peers that we choke.
        // At the start are the peers that have us unchoked at the end the
        // other peer that are interested, but are choking us.
        List<Peer> interested = new LinkedList<Peer>();
        int count = 0;
        int unchokedCount = 0;
        int maxUploaders = allowedUploaders();
        Iterator<Peer> it = peers.iterator();
        while (it.hasNext()) {
            Peer peer = it.next();
            if (peer.isChoking() && peer.isInterested()) {
                count++;
                if (uploaders.get() < maxUploaders) {
                    if (peer.isInteresting() && !peer.isChoked()) {
                        interested.add(unchokedCount++, peer);
                    } else {interested.add(peer);}
                }
            }
        }

        int up = uploaders.get();
        while (up < maxUploaders && !interested.isEmpty()) {
            Peer peer = interested.remove(0);
            if (_log.shouldDebug()) {_log.debug("Unchoking [" + peer + "]");}
            peer.setChoking(false);
            up = uploaders.incrementAndGet();
            interestedUploaders.incrementAndGet();
            count--;
            // Put peer back at the end of the list.
            peers.remove(peer);
            peers.add(peer);
            peerCount = peers.size();
        }
        interestedAndChoking.set(count);
    }

    /**
     * @return true if we still want the given piece
     */
    public boolean gotHave(Peer peer, int piece) {
        synchronized(wantedPieces) {
            for (Piece pc : wantedPieces) {
                if (pc.getId() == piece) {
                    pc.addPeer(peer);
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Returns true if the given bitfield contains at least one piece we
     * are interested in.
     */
    public boolean gotBitField(Peer peer, BitField bitfield) {
        boolean rv = false;
        synchronized(wantedPieces) {
            for (Piece p : wantedPieces) {
                int i = p.getId();
                if (bitfield.get(i)) {
                    p.addPeer(peer);
                    rv = true;
                }
            }
        }
        return rv;
    }

    /**
     *  This should be somewhat less than the max conns per torrent,
     *  but not too much less, so a torrent doesn't get stuck near the end.
     *  https://en.wikipedia.org/wiki/Glossary_of_BitTorrent_terms#Endgame_/_Endgame_mode
     *  @since 0.7.14
     */
    private static final int END_GAME_THRESHOLD = 32;

    /**
     *  Max number of peers to get a piece from when in end game
     *  @since 0.8.1
     */
    private static final int MAX_PARALLEL_REQUESTS = 8;

    /**
     * Returns one of pieces in the given BitField that is still wanted or
     * null if none of the given pieces are wanted.
     *
     * @param record if true, actually record in our data structures that we gave the
     *               request to this peer. If false, do not update the data structures.
     * @since 0.8.2
     */
    private Piece wantPiece(Peer peer, BitField havePieces, boolean record) {
        if (halted) {
            if (_log.shouldInfo()) {
                _log.info("We don't want anything from [" + peer + "] as we are halted!");
            }
            return null;
        }

        Piece piece = null;
        List<Piece> requested = new ArrayList<Piece>();
        int wantedSize = END_GAME_THRESHOLD + 1;
        synchronized(wantedPieces) {
            if (record) {Collections.sort(wantedPieces);} // Sort in order of rarest first.
            Iterator<Piece> it = wantedPieces.iterator();
            while (piece == null && it.hasNext()) {
                Piece p = it.next();
                // sorted by priority, so when we hit a disabled piece we are done
                if (p.isDisabled()) {break;}
                if (havePieces.get(p.getId()) && !p.isRequested()) {
                    // never ever choose one that's in partialPieces, or we
                    // will create a second one and leak
                    boolean hasPartial = false;
                    for (PartialPiece pp : partialPieces) {
                        if (pp.getPiece() == p.getId()) {
                            if (_log.shouldDebug()) {
                                _log.debug("wantPiece() skipping partial for [" + peer + "] [Piece " + pp + "]");
                            }
                            hasPartial = true;
                            break;
                        }
                    }
                    if (!hasPartial) {piece = p;}
                } else if (p.isRequested()) {requested.add(p);}
            }

            if (piece == null) {wantedSize = wantedPieces.size();}

            // Only request a piece we've requested before if there's no other choice.
            if (piece == null) {
                // AND if there are almost no wanted pieces left (real end game).
                // If we do end game all the time, we generate lots of extra traffic
                // when the seeder is super-slow and all the peers are "caught up"
                if (wantedSize > END_GAME_THRESHOLD) {
                    if (_log.shouldInfo()) {
                      _log.info("Nothing to request, " + requested.size() + " being requested and " +
                                wantedSize + " still wanted");
                    }
                    return null;  // nothing to request and not in end game
                }

                // Let's not all get on the same piece
                // Even better would be to sort by number of requests
                if (record) {Collections.shuffle(requested, _random);}
                Iterator<Piece> it2 = requested.iterator();
                while (piece == null && it2.hasNext()) {
                    Piece p = it2.next();
                    if (havePieces.get(p.getId())) {
                        // limit number of parallel requests
                        int requestedCount = p.getRequestCount();
                        if (requestedCount < MAX_PARALLEL_REQUESTS && !p.isRequestedBy(peer)) {
                            piece = p;
                            break;
                        }
                    }
                }

                if (piece == null) {
                    if (_log.shouldInfo()) {
                      _log.info("Nothing to even rerequest from [" + peer + "] - requested = " + requested);
                    }
                    return null; // If we still can't find a piece we want, so be it.
                } else {
                    // Should be a lot smarter here -
                    // share blocks rather than starting from 0 with each peer.
                    // This is where the flaws of the snark data model are really exposed.
                    // Could also randomize within the duplicate set rather than strict rarest-first
                    if (_log.shouldInfo()) {
                        _log.info("Parallel request (end game?) for [" + peer + "] [Piece " + piece + "]");
                    }
                }
            }

            if (record) {
                if (_log.shouldInfo()) {
                    if (piece.getPriority() > 0) {
                        _log.info("Requesting piece [" + piece + "] from [" + peer + "] (Priority: " + piece.getPriority() + ")");
                    } else {
                        _log.info("Requesting piece [" + piece + "] from [" + peer + "]");
                    }
                }
                piece.setRequested(peer, true);
            }
            return piece;
        } // sync
    }

    /**
     *  Maps file priorities to piece priorities.
     *  Call after updating file priorities Storage.setPriority()
     *  @since 0.8.1
     */
    public void updatePiecePriorities() {
        if (storage == null) {return;}
        int[] pri = storage.getPiecePriorities();
        if (pri == null) {
            _log.debug("Updated piece priorities called but no priorities to set?");
            return;
        }
        List<Piece> toCancel = new ArrayList<Piece>();
        synchronized(wantedPieces) {
            // Add incomplete and previously unwanted pieces to the list
            // Temp to avoid O(n**2)
            BitField want = new BitField(pri.length);
            for (Piece p : wantedPieces) {want.set(p.getId());}
            BitField bitfield = storage.getBitField();
            for (int i = 0; i < pri.length; i++) {
                if (pri[i] >= 0 && !bitfield.get(i)) {
                    if (!want.get(i)) {
                        Piece piece = new Piece(i);
                        wantedPieces.add(piece);
                        wantedBytes += metainfo.getPieceLength(i);
                        // As connections are already up, new Pieces will
                        // not have their PeerID list populated, so do that.
                        for (Peer p : peers) {
                            // TODO don't access state directly
                            PeerState s = p.state;
                            if (s != null) {
                                BitField bf = s.bitfield;
                                if (bf != null && bf.get(i)) {piece.addPeer(p);}
                            }
                        }
                    }
                }
            }

            // Now set the new priorities and remove newly unwanted pieces
            for (Iterator<Piece> iter = wantedPieces.iterator(); iter.hasNext(); ) {
                 Piece p = iter.next();
                 int priority = pri[p.getId()];
                 if (priority >= 0) {p.setPriority(priority);}
                 else {
                     iter.remove();
                     toCancel.add(p);
                     wantedBytes -= metainfo.getPieceLength(p.getId());
                 }
            }
            if (_log.shouldDebug()) {
                _log.debug("Updated piece priorities, now wanted: " + wantedPieces);
            }
            // if we added pieces, they will be in-order unless we shuffle
            Collections.shuffle(wantedPieces, _random);
        }

        // cancel outside of wantedPieces lock to avoid deadlocks
        if (!toCancel.isEmpty()) {
            // cancel all peers
            for (Peer peer : peers) {
                for (Piece p : toCancel) {peer.cancel(p.getId());}
            }
        }

        // ditto, avoid deadlocks
        // update request queues, in case we added wanted pieces
        // and we were previously uninterested
        for (Peer peer : peers) {peer.request();}
    }

    /**
     * Returns a byte array containing the requested piece or null of
     * the piece is unknown.
     *
     * @return bytes or null for errors such as not having the piece yet
     * @throws RuntimeException on IOE getting the data
     */
    public ByteArray gotRequest(Peer peer, int piece, int off, int len) {
        if (halted) {return null;}
        if (metainfo == null || storage == null) {return null;}

        try {return storage.getPiece(piece, off, len);}
        catch (IOException ioe) {
            snark.stopTorrent();
            String msg = "Error reading the storage (piece " + piece + ") for " + metainfo.getName() + ": " + ioe;
            _log.error(msg, ioe);
            if (listener != null) {
                listener.addMessage(msg);
                listener.addMessage("Fatal storage error: Stopping torrent " + metainfo.getName());
            }
            throw new RuntimeException(msg, ioe);
        }
    }

    /**
     * Returns false if the piece is no good (according to the hash).
     * In that case the peer that supplied the piece should probably be
     * blacklisted.
     *
     * @throws RuntimeException on IOE saving the piece
     */
    public boolean gotPiece(Peer peer, PartialPiece pp) {
        if (metainfo == null || storage == null || storage.isChecking() || halted) {
            pp.release();
            return true;
        }
        int piece = pp.getPiece();

        // try/catch outside the sync to avoid deadlock in the catch
        try {
            synchronized(wantedPieces) {
            Piece p = new Piece(piece);
            if (!wantedPieces.contains(p)) {
                if (_log.shouldDebug()) {
                    _log.debug("Received unwanted piece [" + piece + "/" + metainfo.getPieces() + "] from [" +
                               peer + "] for " + metainfo.getName());
                }

                // No need to announce have piece to peers.
                // Assume we got a good piece, we don't really care anymore.
                // Well, this could be caused by a change in priorities, so
                // only return true if we already have it, otherwise might as well keep it.
                if (storage.getBitField().get(piece)) {
                    pp.release();
                    return true;
                }
            }

              // try/catch moved outside of sync
              // this takes forever if complete, as it rechecks
              if (storage.putPiece(pp)) {
                  if (_log.shouldDebug()) {
                      _log.debug("Received valid piece [" + piece + "/" + metainfo.getPieces() + "] from [" +
                                  peer + "] for " + metainfo.getName());
                  }
              } else {
                  // so we will try again
                  markUnrequested(peer, piece);
                  removePartialPiece(piece); // just in case
                  // Oops. We didn't actually download this then... :(
                  // Reports of counter going negative?
                  //downloaded.addAndGet(0 - metainfo.getPieceLength(piece));
                  // Mark this peer as not having the piece. PeerState will update its bitfield.
                  for (Piece pc : wantedPieces) {
                      if (pc.getId() == piece) {
                          pc.removePeer(peer);
                          break;
                      }
                  }
                  if (_log.shouldDebug()) {
                      _log.debug("Received BAD piece [" + piece + "/" + metainfo.getPieces() + "] from [" +
                                  peer + "] for " + metainfo.getName());
                  }
                  return false; // No need to announce BAD piece to peers.
                }

                wantedPieces.remove(p);
                wantedBytes -= metainfo.getPieceLength(p.getId());
            }  // synch
        } catch (IOException ioe) {
            String msg = "Error writing to storage [piece " + piece + "] for " + metainfo.getName();
            msg = msg + "\n* ";
            _log.error(msg, ioe);
            if (listener != null) {
                listener.addMessage(msg);
                listener.addMessage("Fatal storage error: Stopping torrent " + metainfo.getName());
            }
            // deadlock was here
            snark.stopTorrent();
            throw new RuntimeException(msg, ioe);
        }

        // just in case
        removePartialPiece(piece);

        boolean done = wantedBytes <= 0;

        // Announce to the world we have it!
        // Disconnect from other seeders when we get the last piece
        List<Peer> toDisconnect = done ? new ArrayList<Peer>() : null;
        for (Peer p : peers) {
            if (p.isConnected()) {
                if (done && p.isCompleted()) {toDisconnect.add(p);}
                else {p.have(piece);}
            }
        }

        if (done) {
            for (Peer p : toDisconnect) {p.disconnect(true);}
        }

        // put msg on the console if partial, since Storage won't do it
        if (!completed()) {snark.storageCompleted(storage);}

        synchronized (partialPieces) {
            for (PartialPiece ppp : partialPieces) {
                ppp.release();
                partialPieces.clear();
            }
        }

        return true;
    }

    /** this does nothing but logging */
    public void gotChoke(Peer peer, boolean choke) {
        if (_log.shouldInfo()) {
            _log.info("Received choke(" + choke + ") from [" + peer + "]");
        }
    }

    public void gotInterest(Peer peer, boolean interest) {
        if (interest) {
            if (storage == null || storage.getBitField().size() == 0) {return;} // XD bug #80
            if (uploaders.get() < allowedUploaders()) {
                if (peer.isChoking() && !overUpBWLimit()) {
                    uploaders.incrementAndGet();
                    interestedUploaders.incrementAndGet();
                    peer.setChoking(false);
                    if (_log.shouldInfo()) {_log.info("Unchoking [" + peer + "]");}
                }
            }
        }
    }

    public void disconnected(Peer peer) {
        if (_log.shouldInfo()) {_log.info("Disconnected peer [" + peer + "]");}

        synchronized(peers) {
            // Make sure it is no longer in our lists
            if (peers.remove(peer)) {
                // Unchoke some random other peer
                unchokePeer();
                removePeerFromPieces(peer);
            }
            peerCount = peers.size();
        }
    }

    /** Called when a peer is removed, to prevent it from being used in
     * rarest-first calculations.
     */
    private void removePeerFromPieces(Peer peer) {
        synchronized(wantedPieces) {
            for (Piece piece : wantedPieces) {
                piece.removePeer(peer);
                piece.setRequested(peer, false);
            }
        }
    }

    /**
     *  Save partial pieces on peer disconnection
     *  and hopefully restart it later.
     *  Replace a partial piece in the List if the new one is bigger.
     *  Storage method is private so we can expand to save multiple partials
     *  if we wish.
     *
     *  Also mark the piece unrequested if this peer was the only one.
     *
     *  @param peer partials, must include the zero-offset (empty) ones too.
     *              No dup pieces.
     *              len field in Requests is ignored.
     *  @since 0.8.2
     */
    public void savePartialPieces(Peer peer, List<Request> partials) {
        if (_log.shouldInfo()) {
            _log.info("Partials received from [" + peer + "] - " + partials);
        }
        if (halted || completed()) {
            for (Request req : partials) {
                PartialPiece pp = req.getPartialPiece();
                pp.release();
            }
            return;
        }

        synchronized(wantedPieces) {
            for (Request req : partials) {
                PartialPiece pp = req.getPartialPiece();
                if (pp.hasData()) {
                    // PartialPiece.equals() only compares piece number, which is what we want
                    int idx = partialPieces.indexOf(pp);
                    if (idx < 0) {
                        partialPieces.add(pp);
                        if (_log.shouldInfo()) {
                            _log.info("Saving orphaned partial piece (new) " + pp);
                        }
                    } else if (pp.getDownloaded() > partialPieces.get(idx).getDownloaded()) {
                        // replace what's there now
                        partialPieces.get(idx).release();
                        partialPieces.set(idx, pp);
                        if (_log.shouldInfo()) {
                            _log.info("Saving orphaned partial piece (bigger) " + pp);
                        }
                    } else {
                        pp.release();
                        if (_log.shouldInfo()) {
                            _log.info("Discarding partial piece (not bigger)" + pp);
                        }
                    }
                    int max = getMaxConnections();
                    if (partialPieces.size() > max) {
                        // sorts by preference, highest first
                        Collections.sort(partialPieces);
                        PartialPiece gone = partialPieces.remove(partialPieces.size() - 1);
                        gone.release();
                        if (_log.shouldInfo()) {
                            _log.info("Discarding orphaned partial piece (list full) " + gone);
                        }
                    }
                } else {pp.release();} // drop the empty partial piece
                // syncs on wantedPieces...
                markUnrequested(peer, pp.getPiece());
            }
            if (_log.shouldInfo()) {
                _log.info("Partial list size now: " + partialPieces.size());
            }
        }
    }

    /**
     *  Return partial piece to the PeerState if it's still wanted and peer has it.
     *  @param havePieces pieces the peer has, the rv will be one of these
     *
     *  @return PartialPiece or null
     *  @since 0.8.2
     */
    public PartialPiece getPartialPiece(Peer peer, BitField havePieces) {
        if (metainfo == null || (storage != null && storage.isChecking())) {return null;}
        synchronized(wantedPieces) {
            // sorts by preference, highest first
            Collections.sort(partialPieces);
            for (Iterator<PartialPiece> iter = partialPieces.iterator(); iter.hasNext(); ) {
                PartialPiece pp = iter.next();
                int savedPiece = pp.getPiece();
                if (havePieces.get(savedPiece)) {
                   // this is just a double-check, it should be in there
                   boolean skipped = false;
                   outer:
                   for (Piece piece : wantedPieces) {
                       if (piece.getId() == savedPiece) {
                           if (peer.isCompleted() && piece.getPeerCount() > 1 &&
                               wantedPieces.size() > 2*END_GAME_THRESHOLD &&
                               partialPieces.size() < 4 &&
                               _random.nextInt(4) != 0) {

                               // Try to preserve rarest-first
                               // by not requesting a partial piece that at least two non-seeders also have
                               // from a seeder
                               int nonSeeds = 0;
                               int seeds = 0;
                               for (Peer pr : peers) {
                                   if (pr.isCompleted()) {
                                       if (++seeds >= 4) {break;}
                                   } else {
                                       // TODO don't access state directly
                                       PeerState state = pr.state;
                                       if (state == null) {continue;}
                                       BitField bf = state.bitfield;
                                       if (bf == null) {continue;}
                                       if (bf.get(savedPiece)) {
                                           if (++nonSeeds > 1) {
                                               skipped = true;
                                               break outer;
                                           }
                                       }
                                   }
                               }
                           }
                           iter.remove();
                           piece.setRequested(peer, true);
                           if (_log.shouldInfo()) {
                               _log.info("Restoring orphaned partial piece " + pp + " to " + peer +
                                         " -> Partial list size now: " + partialPieces.size());
                           }
                           return pp;
                        }
                    }
                    if (_log.shouldDebug()) {
                        if (skipped) {
                            _log.debug("Partial piece " + pp + " with multiple peers skipped for seeder");
                        } else {
                            _log.debug("Partial piece " + pp + " NOT in wantedPieces??");
                        }
                    }
                }
            }
            if (_log.shouldDebug() && !partialPieces.isEmpty())
                _log.debug("Peer [" + peer + "] has none of our partials " + partialPieces);
        }
        // ...and this section turns this into the general move-requests-around code!
        // Temporary? So PeerState never calls wantPiece() directly for now...
        Piece piece = wantPiece(peer, havePieces, true);
        if (piece != null) {
            // TODO padding
            return new PartialPiece(piece, metainfo.getPieceLength(piece.getId()), _util.getTempDir());
        }
        if (_log.shouldDebug()) {_log.debug("We have no partial piece to return");}
        return null;
    }

    /**
     * Called when we are downloading from the peer and may need to ask for
     * a new piece. Returns true if wantPiece() or getPartialPiece() would return a piece.
     *
     * @param peer the Peer that will be asked to provide the piece.
     * @param havePieces a BitField containing the pieces that the other
     * side has.
     *
     * @return if we want any of what the peer has
     * @since 0.8.2
     */
    public boolean needPiece(Peer peer, BitField havePieces) {
        synchronized(wantedPieces) {
            for (PartialPiece pp : partialPieces) {
                int savedPiece = pp.getPiece();
                if (havePieces.get(savedPiece)) {
                   // this is just a double-check, it should be in there
                   for(Piece piece : wantedPieces) {
                       if (piece.getId() == savedPiece) {
                           if (_log.shouldInfo()) {
                               _log.info("We could restore orphaned partial piece " + pp);
                           }
                           return true;
                        }
                    }
                }
            }
        }
        return wantPiece(peer, havePieces, false) != null;
    }

    /**
     *  Remove saved state for this piece.
     *  Unless we are in the end game there shouldnt be anything in there.
     *  Do not call with wantedPieces lock held (deadlock)
     */
    private void removePartialPiece(int piece) {
        synchronized(wantedPieces) {
            for (Iterator<PartialPiece> iter = partialPieces.iterator(); iter.hasNext(); ) {
                PartialPiece pp = iter.next();
                if (pp.getPiece() == piece) {
                    iter.remove();
                    pp.release();
                    // there should be only one but keep going to be sure
                }
            }
        }
    }

    /**
     *  Clear the requested flag for a piece
     */
    private void markUnrequested(Peer peer, int piece) {
        synchronized(wantedPieces) {
            for (Piece pc : wantedPieces) {
                if (pc.getId() == piece) {
                  pc.setRequested(peer, false);
                  return;
                }
            }
        }
    }

    /**
     *  PeerListener callback
     *  @since 0.8.4
     */
    public void gotExtension(Peer peer, int id, byte[] bs) {
        if (_log.shouldDebug()) {
            _log.debug("Received extension message " + id + " from [" + peer + "]");
        }

        // Basic handling done in PeerState... here we just check if we are done
        if (metainfo == null && id == ExtensionHandler.ID_METADATA) {
            synchronized (magnetState) {
                if (magnetState.isComplete()) {
                    if (_log.shouldInfo()) {
                        _log.info("Received completed metainfo via extension");
                    }
                    metainfo = magnetState.getMetaInfo();
                    listener.gotMetaInfo(this, metainfo);
                }
            }
        } else if (id == ExtensionHandler.ID_HANDSHAKE) {
            // We may not have the bitfield yet, but if we do, don't send PEX to seeds
            if (!peer.isCompleted()) {sendPeers(peer);}
            sendDHT(peer);
            if (_util.utCommentsEnabled()) {sendCommentReq(peer);}
        }
    }

    /**
     *  Send a PEX message to the peer, if he supports PEX.
     *  This sends everybody we have connected to since the
     *  last time we sent PEX to him.
     *  @since 0.8.4
     */
    void sendPeers(Peer peer) {
        if (metainfo != null && metainfo.isPrivate()) {return;}
        Map<String, BEValue> handshake = peer.getHandshakeMap();
        if (handshake == null) {return;}
        BEValue bev = handshake.get("m");
        if (bev == null) {return;}
        try {
            if (bev.getMap().get(ExtensionHandler.TYPE_PEX) != null) {
                List<Peer> pList = new ArrayList<Peer>();
                long t = peer.getPexLastSent();
                for (Peer p : peers) {
                    if (p.equals(peer) || p.isWebPeer()) {continue;}
                    if (p.getWhenConnected() > t) {pList.add(p);}
                }
                if (!pList.isEmpty()) {
                    ExtensionHandler.sendPEX(peer, pList);
                    peer.setPexLastSent(_util.getContext().clock().now());
                }
            }
        } catch (InvalidBEncodingException ibee) {}
    }

    /**
     *  Send a DHT message to the peer, if we both support DHT.
     *  @since DHT
     */
    void sendDHT(Peer peer) {
        DHT dht = _util.getDHT();
        if (dht == null) {return;}
        Map<String, BEValue> handshake = peer.getHandshakeMap();
        if (handshake == null) {return;}
        BEValue bev = handshake.get("m");
        if (bev == null) {return;}
        try {
            if (bev.getMap().get(ExtensionHandler.TYPE_DHT) != null) {
                ExtensionHandler.sendDHT(peer, dht.getPort(), dht.getRPort());
            }
        } catch (InvalidBEncodingException ibee) {}
    }

    /**
     *  Send a commment request message to the peer, if he supports it.
     *  @since 0.9.31
     */
    void sendCommentReq(Peer peer) {
        Map<String, BEValue> handshake = peer.getHandshakeMap();
        if (handshake == null) {
            if (wantedBytes == 0 && _commentsNotRequested.incrementAndGet() >= MAX_COMMENT_NOT_REQ) {
                _commentsLastRequested.set(_util.getContext().clock().now());
            }
            return;
        }
        BEValue bev = handshake.get("m");
        if (bev == null) {
            if (wantedBytes == 0 && _commentsNotRequested.incrementAndGet() >= MAX_COMMENT_NOT_REQ) {
                _commentsLastRequested.set(_util.getContext().clock().now());
            }
            return;
        }

        // TODO if peer hasn't been connected very long, don't bother
        // unless forced at handshake time (see above)
        try {
            if (bev.getMap().get(ExtensionHandler.TYPE_COMMENT) != null) {
                int sz = 0;
                CommentSet comments = snark.getComments();
                if (comments != null) {
                    synchronized(comments) {sz = comments.size();}
                }
                _commentsNotRequested.set(0);
                _commentsLastRequested.set(_util.getContext().clock().now());
                if (sz >= CommentSet.MAX_SIZE) {return;}
                ExtensionHandler.sendCommentReq(peer, CommentSet.MAX_SIZE - sz);
            } else {
                // failsafe to prevent seed excessively connecting out to a swarm for comments
                // when nobody in the swarm supports comments
                if (wantedBytes == 0 && _commentsNotRequested.incrementAndGet() >= MAX_COMMENT_NOT_REQ) {
                    _commentsLastRequested.set(_util.getContext().clock().now());
                }
            }
        } catch (InvalidBEncodingException ibee) {}
    }

    /**
     *  Sets the storage after transition out of magnet mode
     *  Snark calls this after we call gotMetaInfo()
     *  @since 0.8.4
     */
    public void setStorage(Storage stg) {
        storage = stg;
        setWantedPieces();
        // ok we should be in business
        for (Peer p : peers) {p.setMetaInfo(metainfo);}
    }

    /**
     *  PeerListener callback
     *  Tell the DHT to ping it, this will get back the node info
     *  @param rport must be port + 1
     *  @since 0.8.4
     */
    public void gotPort(Peer peer, int port, int rport) {
        DHT dht = _util.getDHT();
        if (dht != null && port > 0 && port < 65535 && rport == port + 1) {
            dht.ping(peer.getDestination(), port);
        }
    }

    /**
     *  Get peers from PEX -
     *  PeerListener callback
     *  @since 0.8.4
     */
    public void gotPeers(Peer peer, List<PeerID> peers) {
        if (!needOutboundPeers()) {return;}
        Destination myDest = _util.getMyDestination();
        if (myDest == null) {return;}
        byte[] myHash = myDest.calculateHash().getData();
        List<Peer> pList = peerList();
        for (PeerID id : peers) {
             if (peerIDInList(id, pList) != null) {continue;}
             if (DataHelper.eq(myHash, id.getDestHash())) {continue;}
             pexPeers.add(id);
        }
        // TrackerClient will poll for pexPeers and do the add in its thread,
        // rather than running another thread here.
    }

    /**
     * Called when comments are requested via ut_comment
     *
     * @since 0.9.31
     */
    public void gotCommentReq(Peer peer, int num) {
        /* TODO cache per-torrent setting, use it instead */
        if (!_util.utCommentsEnabled()) {return;}
        CommentSet comments = snark.getComments();
        if (comments != null) {
            int lastSent = peer.getTotalCommentsSent();
            int sz;
            synchronized(comments) {
                sz = comments.size();
                // only send if we have more than last time
                if (sz <= lastSent) {return;}
                ExtensionHandler.locked_sendComments(peer, num, comments);
            }
            peer.setTotalCommentsSent(sz);
        }
    }

    /**
     * Called when comments are received via ut_comment
     *
     * @param comments non-null
     * @since 0.9.31
     */
    public void gotComments(Peer peer, List<Comment> comments) {
        /* TODO cache per-torrent setting, use it instead */
        if (!_util.utCommentsEnabled()) {return;}
        if (!comments.isEmpty()) {snark.addComments(comments);}
    }

    /**
     *  Called by TrackerClient
     *  @return the Set itself, modifiable, not a copy, caller should clear()
     *  @since 0.8.4
     */
    Set<PeerID> getPEXPeers() {return pexPeers;}

    /** Return number of allowed uploaders for this torrent.
     ** Check with Snark to see if we are over the total upload limit.
     */
    public int allowedUploaders() {
        int up = uploaders.get();
        if (listener != null && listener.overUploadLimit(interestedUploaders.get())) {
            if (_log.shouldDebug()) {_log.debug("Over limit, uploaders was: " + up);}
            return up - 1;
        } else if (up < MAX_UPLOADERS) {return up + 1;}
        else {return MAX_UPLOADERS;}
    }

    /**
     *  Uploaders whether interested or not
     *  Use this for per-torrent limits.
     *
     *  @return current
     *  @since 0.8.4
     */
    public int getUploaders() {
        int rv = uploaders.get();
        if (rv > 0) {
            int max = getPeers();
            if (rv > max) {rv = max;}
        }
        return rv;
    }

    /**
     *  Uploaders, interested only.
     *  Use this to calculate the global total, so that
     *  unchoked but uninterested peers don't count against the global limit.
     *
     *  @return current
     *  @since 0.9.28
     */
    public int getInterestedUploaders() {
        int rv = interestedUploaders.get();
        if (rv > 0) {
            int max = getPeers();
            if (rv > max) {rv = max;}
        }
        return rv;
    }

    /**
     *  Set the uploaders and interestedUploaders counts
     *
     *  @since 0.9.28
     *  @param upl whether interested or not
     *  @param inter interested only
     */
    public void setUploaders(int upl, int inter) {
        if (upl < 0) {upl = 0;}
        else if (upl > MAX_UPLOADERS) {upl = MAX_UPLOADERS;}
        uploaders.set(upl);
        if (inter < 0) {inter = 0;}
        else if (inter > MAX_UPLOADERS) {inter = MAX_UPLOADERS;}
        interestedUploaders.set(inter);
    }

    /**
     *  Decrement the uploaders and (if set) the interestedUploaders counts
     *
     *  @since 0.9.28
     */
    public void decrementUploaders(boolean isInterested) {
        int up = uploaders.decrementAndGet();
        if (up < 0) {uploaders.set(0);}
        if (isInterested) {
            up = interestedUploaders.decrementAndGet();
            if (up < 0) {interestedUploaders.set(0);}
        }
    }

    /**
     *  @return current
     *  @since 0.9.28
     */
    public int getInterestedAndChoking() {return interestedAndChoking.get();}

    /**
     *  @since 0.9.28
     */
    public void addInterestedAndChoking(int toAdd) {interestedAndChoking.addAndGet(toAdd);}

    /**
     *  Convenience
     *  @since 0.9.2
     */
    public I2PSnarkUtil getUtil() {return _util;}

    /**
     *  Ban a web peer for this torrent, for while or permanently.
     *  @param host the host name
     *  @since 0.9.49
     */
    public synchronized void banWebPeer(String host, boolean isPermanent) {
        if (_webPeerBans == null) {_webPeerBans = new HashMap<String, Long>(4);}
        Long time;
        if (isPermanent) {time = Long.valueOf(Long.MAX_VALUE);}
        else {
            long now = _util.getContext().clock().now();
            time = Long.valueOf(now + WEBPEER_BAN_TIME);
        }
        Long old = _webPeerBans.put(host, time);
        if (old != null && old.longValue() > time) {_webPeerBans.put(host, old);}
    }

    /**
     *  Is a web peer banned?
     *  @param host the host name
     *  @since 0.9.49
     */
    public synchronized boolean isWebPeerBanned(String host) {
        if (_webPeerBans == null) {return false;}
        Long time = _webPeerBans.get(host);
        if (time == null) {return false;}
        long now = _util.getContext().clock().now();
        boolean rv = time.longValue() > now;
        if (!rv) {_webPeerBans.remove(host);}
        return rv;
    }

}