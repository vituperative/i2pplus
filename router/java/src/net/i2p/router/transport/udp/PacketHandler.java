package net.i2p.router.transport.udp;

import java.util.concurrent.BlockingQueue;

import net.i2p.router.RouterContext;
import net.i2p.router.util.CoDelBlockingQueue;
import net.i2p.util.I2PThread;
import net.i2p.util.Log;
import net.i2p.util.SystemVersion;

/**
 * Pull inbound packets from the inbound receiver's queue, figure out what
 * peer session they belong to (if any), authenticate and decrypt them
 * with the appropriate keys, and push them to the appropriate handler.
 * Data and ACK packets go to the InboundMessageFragments, the various
 * establishment packets go to the EstablishmentManager, and, once implemented,
 * relay packets will go to the relay manager.  At the moment, this is
 * an actual pool of packet handler threads, each pulling off the inbound
 * receiver's queue and pushing them as necessary.
 *
 */
class PacketHandler {
    private final RouterContext _context;
    private final Log _log;
    private final UDPTransport _transport;
    private final EstablishmentManager _establisher;
    private final PeerTestManager _testManager;
    private volatile boolean _keepReading;
    private final Handler[] _handlers;
    private final BlockingQueue<UDPPacket> _inboundQueue;
    private final int _networkID;

    private static final int TYPE_POISON = -99999;
    private static final int MIN_QUEUE_SIZE = SystemVersion.isSlow() ? 32 : 64;
    private static final int MAX_QUEUE_SIZE = SystemVersion.isSlow() ? 192 : 384;
    private static final int MIN_NUM_HANDLERS = 1;  // if < 128MB
    private static final int MAX_NUM_HANDLERS = SystemVersion.isSlow() ? 2 : 4;

    PacketHandler(RouterContext ctx, UDPTransport transport, EstablishmentManager establisher,
                  InboundMessageFragments inbound, PeerTestManager testManager, IntroductionManager introManager) {
        _context = ctx;
        _log = ctx.logManager().getLog(PacketHandler.class);
        _transport = transport;
        _establisher = establisher;
        _testManager = testManager;
        _networkID = ctx.router().getNetworkID();

        long maxMemory = SystemVersion.getMaxMemory();
        int cores = SystemVersion.getCores();
        boolean isSlow = SystemVersion.isSlow();
        int qsize = (int) Math.max(MIN_QUEUE_SIZE, Math.min(MAX_QUEUE_SIZE, maxMemory / (2*1024*1024)));
        _inboundQueue = new CoDelBlockingQueue<UDPPacket>(ctx, "UDP-Receiver", qsize);
        int num_handlers;
        if (maxMemory < 128*1024*1024) {num_handlers = 1;}
        else {num_handlers = MAX_NUM_HANDLERS;}
        _handlers = new Handler[num_handlers];
        for (int i = 0; i < num_handlers; i++) {_handlers[i] = new Handler();}

        _context.statManager().createRateStat("udp.destroyedInvalidSkew", "Session destroyed (bad skew)", "Transport [UDP]", UDPTransport.RATES);
    }

    public synchronized void startup() {
        _keepReading = true;
        for (int i = 0; i < _handlers.length; i++) {
            I2PThread t = new I2PThread(_handlers[i], "UDPPktHandler " + (i+1) + '/' + _handlers.length, true);
            t.setPriority(I2PThread.MAX_PRIORITY);
            t.start();
        }
    }

    public synchronized void shutdown() {
        _keepReading = false;
        stopQueue();
    }

    String getHandlerStatus() {
        StringBuilder rv = new StringBuilder();
        rv.append("Handlers: ").append(_handlers.length);
        for (int i = 0; i < _handlers.length; i++) {
            Handler handler = _handlers[i];
            rv.append(" handler ").append(i);
        }
        return rv.toString();
    }

    /**
     * Blocking call to retrieve the next inbound packet, or null if we have
     * shut down.
     *
     * @since IPv6 moved from UDPReceiver
     */
    public void queueReceived(UDPPacket packet) throws InterruptedException {
        if (_log.shouldDebug()) {_log.debug("Adding packet to queue: " + packet);}
        _inboundQueue.put(packet);
    }

    /**
     * Blocking for a while
     *
     * @since IPv6 moved from UDPReceiver
     */
    private void stopQueue() {
        _inboundQueue.clear();
        for (int i = 0; i < _handlers.length; i++) {
            UDPPacket poison = UDPPacket.acquire(_context, false);
            poison.setMessageType(TYPE_POISON);
            _inboundQueue.offer(poison);
        }
        for (int i = 1; i <= 5 && !_inboundQueue.isEmpty(); i++) {
            try {Thread.sleep(i * 50);}
            catch (InterruptedException ie) {}
        }
        _inboundQueue.clear();
    }

    /**
     * Blocking call to retrieve the next inbound packet, or null if we have
     * shut down.
     *
     * @since IPv6 moved from UDPReceiver
     */
    public UDPPacket receiveNext() {
        UDPPacket rv = null;
        //int remaining = 0;
        while (_keepReading && rv == null) {
            try {rv = _inboundQueue.take();}
            catch (InterruptedException ie) {}
            if (rv != null && rv.getMessageType() == TYPE_POISON) {return null;}
        }
        //_context.statManager().addRateData("udp.receiveRemaining", remaining, 0);
        return rv;
    }

    private class Handler implements Runnable {

        public void run() {
            while (_keepReading) {
                UDPPacket packet = receiveNext();
                if (packet == null) {break;} // keepReading is probably false, or bind failed...

                packet.received();
                if (_log.shouldDebug()) {_log.debug("Received packet from " + packet);}
                try {handlePacket(packet);}
                catch (RuntimeException e) {
                    if (_log.shouldError()) {
                        _log.error("Internal error handling a UDP packet from " + packet, e);
                    }
                }
                // back to the cache with thee!
                packet.release();
            }
        }

        /**
         * Initial handling, called for every packet.
         * Find the state and call the correct receivePacket() variant.
         *
         * Classify the packet by source IP/port, into 4 groups:
         *<ol>
         *<li>Established session
         *<li>Pending inbound establishment
         *<li>Pending outbound establishment
         *<li>No established or pending session found
         *</ol>
         */
        private void handlePacket(UDPPacket packet) {
            RemoteHostId rem = packet.getRemoteHost();
            PeerState state = _transport.getPeerState(rem);
            if (state == null) {
                //if (_log.shouldDebug())
                //    _log.debug("Packet received is not for a connected peer");
                InboundEstablishState est = _establisher.getInboundState(rem);
                if (est != null) {
                    // Group 2: Inbound Establishment
                    if (_log.shouldDebug()) {
                        _log.debug("Packet received IS for an Inbound establishment");
                    }
                    receiveSSU2Packet(rem, packet, (InboundEstablishState2) est);
                } else {
                    //if (_log.shouldDebug())
                    //    _log.debug("Packet received is not for an inbound establishment");
                    OutboundEstablishState oest = _establisher.getOutboundState(rem);
                    if (oest != null) {
                        // Group 3: Outbound Establishment
                        if (_log.shouldDebug()) {
                            _log.debug("Packet received IS for an Outbound establishment");
                        }
                        receiveSSU2Packet(packet, (OutboundEstablishState2) oest);
                    } else {
                        // Group 4: New conn or needs fallback
                        if (_log.shouldDebug()) {
                            _log.debug("Packet received is not for an Inbound or Outbound establishment");
                        }
                        // ok, not already known establishment, try as a new one
                        // Last chance for success, using our intro key
                        receiveSSU2Packet(rem, packet, (InboundEstablishState2) null);
                    }
                }
            } else {
                // Group 1: Established
                //if (_log.shouldDebug())
                //    _log.debug("Packet received IS for an existing peer");
                ((PeerState2) state).receivePacket(rem, packet);
            }
        }
    }

    /**
     *  Decrypt the header and hand off to the state for processing.
     *  Packet is trial-decrypted, so fallback
     *  processing is possible if this returns false.
     *
     *  Possible messages here are Session Request, Token Request, Session Confirmed, or Peer Test.
     *  Data messages out-of-order from Session Confirmed, or following a
     *  Session Confirmed that was lost, or in-order but before the Session Confirmed was processed,
     *  will handed to the state to be queued for deferred handling.
     *
     *  Min packet data size: 56 (token request) if state is null; 40 (data) if state is non-null
     *
     *  @param state must be version 2, but will be null for session request unless retransmitted
     *  @return true if the header was validated as a SSU2 packet, cannot fallback to SSU 1
     *  @since 0.9.54
     */
    private boolean receiveSSU2Packet(RemoteHostId from, UDPPacket packet, InboundEstablishState2 state) {
        // decrypt header
        byte[] k1 = _transport.getSSU2StaticIntroKey();
        byte[] k2;
        SSU2Header.Header header;
        int type;
        boolean shouldBan = false;
        if (state == null) {
            // Session Request, Token Request, Peer Test 5-7, or Hole Punch
            k2 = k1;
            header = SSU2Header.trialDecryptHandshakeHeader(packet, k1, k2);
            if (header == null ||
                header.getType() != SSU2Util.SESSION_REQUEST_FLAG_BYTE ||
                header.getVersion() != 2 ||
                header.getNetID() != _networkID) {
                if (header != null && _log.shouldInfo()) {
                    _log.info("Packet does not decrypt as Session Request, attempting to decrypt as Token Request / PeerTest / HolePunch \n* " +
                              header + " from " + from);
                }
                // The first 32 bytes were fine, but it corrupted the next 32 bytes
                // TODO make this more efficient, just take the first 32 bytes
                header = SSU2Header.trialDecryptLongHeader(packet, k1, k2);
                if (header == null || header.getVersion() != 2 || header.getNetID() != _networkID) {
                    // typical case, SSU 1 that didn't validate, will be logged at WARN level above
                    // in group 4 receive packet
                    //if (_log.shouldDebug())
                    //    _log.debug("Packet does not decrypt as Session Request, Token Request, or Peer Test: " + header);
                    if (header != null) {
                        // conn ID decryption is the same for short and long header, with k1
                        // presumably a data packet, either ip/port changed, or a race during establishment?
                        // lookup peer state by conn ID, pass over for decryption with the proper k2
                        long id = header.getDestConnID();
                        PeerState2 ps2 = _transport.getPeerState(id);
                        if (ps2 != null) {
                            if (_log.shouldInfo()) {
                                _log.info("Migrated " + packet.getPacket().getLength() + " byte packet from " + from + ps2);
                            }
                            ps2.receivePacket(from, packet);
                            return true;
                        }
                        PeerStateDestroyed dead = _transport.getRecentlyClosed(id);
                        if (dead != null) {
                            // Probably termination ack.
                            // Prevent attempted SSU1 fallback processing and adding to fail cache
                            if (_log.shouldDebug()) {
                                _log.debug("Handling " + packet.getPacket().getLength() + " byte packet from " + from +
                                           " for recently closed ID " + id);
                            }
                            dead.receivePacket(from, packet);
                            return true;
                        }
                    }
                    return false;
                }
                type = header.getType();

                /* One in a million decrypt of SSU 1 packet with type/version/netid all correct?
                   can't have session confirmed with null state, avoid NPE below */
                if (type == SSU2Util.SESSION_CONFIRMED_FLAG_BYTE) {return false;}
                if (type == SSU2Util.SESSION_REQUEST_FLAG_BYTE &&
                    packet.getPacket().getLength() == SSU2Util.MIN_HANDSHAKE_DATA_LEN - 1) {
                    // i2pd short 87 byte session request thru 0.9.56, drop packet
                    if (_log.shouldWarn()) {
                        _log.warn("Received short Session Request (87 bytes) from " + from);
                    }
                    shouldBan = true;
                    return true;
                }
            } else {type = SSU2Util.SESSION_REQUEST_FLAG_BYTE;}
        } else {
            // Session Request (after Retry) or Session Confirmed
            // or retransmitted Session Request or Token Request
            k2 = state.getRcvHeaderEncryptKey2();
            if (k2 == null) {
                // Session Request after Retry
                k2 = k1;
                header = SSU2Header.trialDecryptHandshakeHeader(packet, k1, k2);
                if (header == null ||
                    header.getType() != SSU2Util.SESSION_REQUEST_FLAG_BYTE ||
                    header.getVersion() != 2 ||
                    header.getNetID() != _networkID) {
                    // possibly token-request-after-retry? let's see...
                    header = SSU2Header.trialDecryptLongHeader(packet, k1, k2);
                    if (header != null && header.getType() == SSU2Util.SESSION_REQUEST_FLAG_BYTE &&
                        header.getVersion() == 2 && header.getNetID() == _networkID &&
                        packet.getPacket().getLength() == 87) {
                        // i2pd short 87 byte session request thru 0.9.56, drop packet
                        if (_log.shouldWarn()) {
                            _log.warn("Received short Session Request (87 bytes) after Retry on " + state);
                        }
                        shouldBan = true;
                        return true;
                    }
                    if (header == null ||
                        header.getType() != SSU2Util.TOKEN_REQUEST_FLAG_BYTE ||
                        header.getVersion() != 2 ||
                        header.getNetID() != _networkID) {
                        if (_log.shouldWarn()) {
                            _log.warn("Failed to decrypt Session or Token Request after Retry \n* " + header +
                                      " (" + packet.getPacket().getLength() + " bytes) on " + state);
                        }
                        shouldBan = true;
                        return false;
                    }
                    // yes, retransmitted token request
                }
                if (header.getSrcConnID() != state.getSendConnID()) {
                    if (_log.shouldWarn()) {
                        _log.warn("Received BAD Source Connection ID \n* " + header +
                                  " (" + packet.getPacket().getLength() + " bytes) on " + state);
                    }
                    // TODO could be a retransmitted Session Request,
                    // tell establisher?
                    shouldBan = true;
                    return false;
                }
                if (header.getDestConnID() != state.getRcvConnID()) {
                    // i2pd bug changing after retry, thru 0.9.56, drop packet
                    if (_log.shouldWarn()) {
                        _log.warn("Received BAD Destination Connection ID \n* " + header +
                                  " (" + packet.getPacket().getLength() + " bytes) on " + state);
                    }
                    shouldBan = true;
                    return true;
                }
                type = header.getType();
            } else {
                // Session Confirmed or retransmitted Session Request or Token Request
                header = SSU2Header.trialDecryptShortHeader(packet, k1, k2);
                if (header == null) {
                    // Java I2P thru 0.9.56 retransmits session confirmed with 1-2 byte packets
                    if (_log.shouldWarn()) {
                        _log.warn("Received SessionConfirmed packet was too short (" +
                                  + packet.getPacket().getLength() + " bytes) on " + state);
                    }
                    shouldBan = true;
                    return false;
                }
                if (header.getDestConnID() != state.getRcvConnID()) {
                    if (_log.shouldWarn()) {
                        _log.warn("Received BAD Destination Connection ID \n* " + header + " on " + state);
                    }
                    shouldBan = true;
                    return false;
                }
                if (header.getPacketNumber() != 0 ||
                    header.getType() != SSU2Util.SESSION_CONFIRMED_FLAG_BYTE) {
                    if (_log.shouldInfo()) {
                        _log.info("Queueing possible data packet (" + packet.getPacket().getLength() + " bytes) on: " + state);
                    }
                    // TODO either attempt to decrypt as a retransmitted
                    // Session Request or Token Request,
                    // or just tell establisher so it can retransmit Session Created or Retry

                    // Possible ordering issues and races:
                    // Case 1: Data packets before (possibly lost or out-of-order) Session Confirmed
                    // Case 2: Data packets after Session Confirmed but it wasn't processed yet
                    // Queue the packet with the state for processing
                    state.queuePossibleDataPacket(packet);
                    return true;
                }
                type = SSU2Util.SESSION_CONFIRMED_FLAG_BYTE;
            }
        }

        // all good
        SSU2Header.acceptTrialDecrypt(packet, header);
        switch (type) {
          case SSU2Util.SESSION_REQUEST_FLAG_BYTE:
            if (_log.shouldDebug()) {_log.debug("Received a SessionRequest on " + state);}
            _establisher.receiveSessionOrTokenRequest(from, state, packet);
            break;

          case SSU2Util.TOKEN_REQUEST_FLAG_BYTE:
            if (_log.shouldDebug()) {_log.debug("Received a TokenRequest on " + state);}
            _establisher.receiveSessionOrTokenRequest(from, state, packet);
            break;

          case SSU2Util.SESSION_CONFIRMED_FLAG_BYTE:
            if (_log.shouldDebug()) {_log.debug("Received a SessionConfirmed on " + state);}
            _establisher.receiveSessionConfirmed(state, packet);
            break;

          case SSU2Util.PEER_TEST_FLAG_BYTE:
            if (_log.shouldDebug()) {_log.debug("Received a PeerTest from " + from);}
            _testManager.receiveTest(from, packet);
            break;

          case SSU2Util.HOLE_PUNCH_FLAG_BYTE:
            if (_log.shouldDebug()) {_log.debug("Received a HolePunch from " + from);}
            _establisher.receiveHolePunch(from, packet);
            break;

          default:
            if (_log.shouldWarn()) {_log.warn("Received UNKNOWN SSU2 message \n* " + header + " from " + from);}
            break;
        }
        return true;
    }

    /**
     *  Decrypt the header and hand off to the state for processing.
     *  Packet is trial-decrypted, so fallback
     *  processing is possible if this returns false.
     *  But that's probably not necessary.
     *
     *  Possible messages here are Session Created or Retry
     *
     *  Min packet data size: 56 (retry)
     *
     *  @param state must be version 2, non-null
     *  @return true if the header was validated as a SSU2 packet, cannot fallback to SSU 1
     *  @since 0.9.54
     */
    private boolean receiveSSU2Packet(UDPPacket packet, OutboundEstablishState2 state) {
        // decrypt header
        byte[] k1 = state.getRcvHeaderEncryptKey1();
        byte[] k2 = state.getRcvHeaderEncryptKey2();
        SSU2Header.Header header;
        if (k2 != null) {
            header = SSU2Header.trialDecryptHandshakeHeader(packet, k1, k2);
            if (header != null) {
                // dest conn ID decrypts the same for both Session Created
                // and Retry, so we can bail out now if it doesn't match
                if (header.getDestConnID() != state.getRcvConnID()) {
                    if (_log.shouldWarn()) {_log.warn("Received BAD Destination Connection ID \n* " + header);}
                    return false;
                }
            }
        } else {header = null;} // we have only sent a Token Request
        int type;
        if (header == null ||
            header.getType() != SSU2Util.SESSION_CREATED_FLAG_BYTE ||
            header.getVersion() != 2 ||
            header.getNetID() != _networkID) {
            if (_log.shouldInfo()) {
                _log.info("Packet does not decrypt as SessionCreated, attempting to decrypt as Retry" + (header != null ? "\n* " + header : ""));
            }
            k2 = state.getRcvRetryHeaderEncryptKey2();
            header = SSU2Header.trialDecryptLongHeader(packet, k1, k2);
            if (header == null || header.getType() != SSU2Util.RETRY_FLAG_BYTE ||
                header.getVersion() != 2 || header.getNetID() != _networkID) {
                if (_log.shouldInfo()) {
                    _log.info("Packet does not decrypt as SessionCreated or Retry \n* " + header + " on " + state);
                }
                return false;
            }
            type = SSU2Util.RETRY_FLAG_BYTE;
        } else {type = SSU2Util.SESSION_CREATED_FLAG_BYTE;}
        if (header.getDestConnID() != state.getRcvConnID()) {
            if (_log.shouldWarn()) {_log.warn("Received BAD Destination Connection ID \n* " + header);}
            return false;
        }
        if (header.getSrcConnID() != state.getSendConnID()) {
            if (_log.shouldWarn()) {_log.warn("Received BAD Source Connection ID \n* " + header);}
            return false;
        }

        // all good
        SSU2Header.acceptTrialDecrypt(packet, header);
        if (type == SSU2Util.SESSION_CREATED_FLAG_BYTE) {
            if (_log.shouldDebug()) {_log.debug("Received a SessionCreated on " + state);}
            _establisher.receiveSessionCreated(state, packet);
        } else {
            if (_log.shouldDebug()) {_log.debug("Received a Retry on " + state);}
            _establisher.receiveRetry(state, packet);
        }
        return true;
    }

}
