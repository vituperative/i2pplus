package net.i2p.router.web.helpers;

import java.io.IOException;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.i2p.app.ClientAppManager;
import net.i2p.data.DataHelper;
import net.i2p.data.Destination;
import net.i2p.data.Hash;
import net.i2p.data.LeaseSet;
import net.i2p.data.router.RouterAddress;
import net.i2p.data.router.RouterInfo;
import net.i2p.router.CommSystemFacade.Status;
import net.i2p.router.Router;
import net.i2p.router.RouterContext;
import net.i2p.router.RouterVersion;
import net.i2p.router.TunnelPoolSettings;
import net.i2p.router.networkdb.kademlia.FloodfillNetworkDatabaseFacade;
import net.i2p.router.networkdb.reseed.ReseedChecker;
import net.i2p.router.transport.TransportUtil;
import net.i2p.router.web.CSSHelper;
import net.i2p.router.web.DeadlockDetector;
import net.i2p.router.web.HelperBase;
import net.i2p.router.web.NewsHelper;
import net.i2p.servlet.util.ServletUtil;
import net.i2p.stat.Rate;
import net.i2p.stat.RateStat;
import net.i2p.util.PortMapper;
import net.i2p.util.SystemVersion;

import net.i2p.router.web.ConfigUpdateHandler;

/**
 * Simple helper to query the appropriate router for data necessary to render
 * the summary sections on the router console.
 *
 * For the full summary bar use renderSummaryBar()
 */
public class SidebarHelper extends HelperBase {

    static final String THINSP = " / ";
    private static final char S = ',';
    static final String PROP_SUMMARYBAR = "routerconsole.summaryBar.";
    net.i2p.I2PAppContext ctx = net.i2p.I2PAppContext.getGlobalContext();
    String firstVersion = ctx.getProperty("router.firstVersion");
    String version = net.i2p.CoreVersion.VERSION;
    private static final String PROP_ADVANCED = "routerconsole.advanced";
    public boolean isAdvanced() {return ctx.getBooleanProperty(PROP_ADVANCED);}

    static final String DEFAULT_FULL_NEWUSER =
        "RouterInfo" + S +
        "CPUBar" + S +
        "MemoryBar" + S +
        "UpdateStatus" + S +
        "Bandwidth" + S +
        "BandwidthGraph" + S +
        "NetworkReachability" + S +
        "FirewallAndReseedStatus" + S +
        "I2PServices" + S +
        "I2PInternals" + S +
        "HelpAndFAQ" + S +
        "Peers" + S +
        "Tunnels" + S +
        "TunnelStatus" + S +
        "RestartStatus" + S +
        "Destinations" + S +
        "";

    static final String DEFAULT_FULL =
        "RouterInfo" + S +
        "CPUBar" + S +
        "MemoryBar" + S +
        "UpdateStatus" + S +
        "Bandwidth" + S +
        "BandwidthGraph" + S +
        "NetworkReachability" + S +
        "FirewallAndReseedStatus" + S +
        "I2PServices" + S +
        "I2PInternals" + S +
        "Peers" + S +
        "Tunnels" + S +
        "TunnelStatus" + S +
        "RestartStatus" + S +
        "Destinations" + S +
        "";

    static final String DEFAULT_FULL_ADVANCED =
        "AdvancedRouterInfo" + S +
        "CPUBar" + S +
        "MemoryBar" + S +
        "UpdateStatus" + S +
        "Bandwidth" + S +
        "BandwidthGraph" + S +
        "NetworkReachability" + S +
        "FirewallAndReseedStatus" + S +
        "I2PServices" + S +
        "I2PInternals" + S +
        "Advanced" + S +
        "Peers" + S +
        "Tunnels" + S +
        "TunnelStatus" + S +
        "Congestion" + S +
        "RestartStatus" + S +
        "Destinations" + S +
        "";

    static final String DEFAULT_MINIMAL =
        "ShortRouterInfo" + S +
        "CPUBar" + S +
        "MemoryBar" + S +
        "UpdateStatus" + S +
        "Bandwidth" + S +
        "BandwidthGraph" + S +
        "NetworkReachability" + S +
        "FirewallAndReseedStatus" + S +
        "Peers" + S +
        "Tunnels" + S +
        "NewsHeadings" + S +
        "RestartStatus" + S +
        "Destinations" + S +
        "";

     /** @since 0.9.32 */
    static final String DEFAULT_MINIMAL_ADVANCED =
        "AdvancedRouterInfo" + S +
        "CPUBar" + S +
        "MemoryBar" + S +
        "UpdateStatus" + S +
        "Bandwidth" + S +
        "BandwidthGraph" + S +
        "NetworkReachability" + S +
        "FirewallAndReseedStatus" + S +
        "Peers" + S +
        "Tunnels" + S +
        "Congestion" + S +
        "NewsHeadings" + S +
        "RestartStatus" + S +
        "Destinations" + S +
        "";

    /**
     * Retrieve the shortened 4 character ident for the router located within
     * the current JVM at the given context.
     *
     */
    public String getIdent() {
        if (_context == null) {return "[no router]";}
        if (_context.routerHash() != null) {return _context.routerHash().toBase64().substring(0, 4);}
        else {return "[unknown]";}
    }

    /**
     * Retrieve the version number of the router.
     *
     */
    public String getVersion() {return RouterVersion.FULL_VERSION;}

    /**
     * Retrieve a pretty printed uptime count (ala 4d or 7h or 39m)
     *
     */
    public String getUptime() {
        if (_context == null) return "[no router]";

        Router router = _context.router();
        if (router == null) {return "[not up]";}
        else {return DataHelper.formatDuration2(router.getUptime());}
    }

    /** allowReseed */
    public boolean allowReseed() {
        long uptime = _context.router().getUptime();
        return _context.netDb().isInitialized() &&
               (_context.netDb().getKnownRouters() < ReseedChecker.MINIMUM && uptime > 60*1000) ||
                _context.getBooleanProperty("i2p.alwaysAllowReseed");
    }

    /** subtract one for ourselves, so if we know no other peers it displays zero */
    public int getAllPeers() {return Math.max(_context.netDb().getKnownRouters() - 1, 0);}

    public enum NetworkState {
        HIDDEN,
        TESTING,
        FIREWALLED,
        RUNNING,
        WARN,
        ERROR,
        CLOCKSKEW,
        VMCOMM;
    }

    /**
     * State message to be displayed to the user in the summary bar.
     *
     * @since 0.9.31
     */
    public static class NetworkStateMessage {
        private NetworkState state;
        private String msg;

        NetworkStateMessage(NetworkState state, String msg) {
            setMessage(state, msg);
        }

        public void setMessage(NetworkState state, String msg) {
            this.state = state;
            this.msg = msg;
        }

        public NetworkState getState() {return state;}

        public String getMessage() {return msg;}

        @Override
        public String toString() {return "(" + state + "; " + msg + ')';}
    }

    public NetworkStateMessage getReachability() {
        return reachability(); // + timeSkew();
        // testing
        //return reachability() +
        //       " Offset: " + DataHelper.formatDuration(_context.clock().getOffset()) +
        //       " Slew: " + DataHelper.formatDuration(((RouterClock)_context.clock()).getDeltaOffset());
    }

    private NetworkStateMessage reachability() {
        if (_context.commSystem().isDummy()) {return new NetworkStateMessage(NetworkState.VMCOMM, "VM Comm System");}
/*
        if (_context.router().getUptime() > 60*1000 &&
            !_context.clientManager().isAlive() &&
            !_context.router().gracefulShutdownInProgress() &&
            !_context.router().isRestarting())
            return new NetworkStateMessage(NetworkState.ERROR, _t("Client Manager I2CP Error"));
            // TODO: display advice in .sb_warning i.e. repurposed showFirewallWarning  ->  _t("Check logs!"));  // not a router problem but the user should know
*/
        // Warn based on actual skew from peers, not update status, so if we successfully offset
        // the clock, we don't complain.
        //if (!_context.clock().getUpdatedSuccessfully())
        long skew = _context.commSystem().getFramedAveragePeerClockSkew(10);
        // Display the actual skew, not the offset
        if (Math.abs(skew) > 30*1000) {
            return new NetworkStateMessage(NetworkState.CLOCKSKEW, _t("Clock Skew of {0}", DataHelper.formatDuration2(Math.abs(skew))));
        }
        if (_context.router().isHidden()) {return new NetworkStateMessage(NetworkState.HIDDEN, _t("Hidden"));}
        RouterInfo routerInfo = _context.router().getRouterInfo();
        if (routerInfo == null) {return new NetworkStateMessage(NetworkState.TESTING, _t("Testing"));}

        // TODO: Migrate/decouple + enhance .sb_netstatus linked advice to Network Reachability section,
        // add tooltips to .sb_netstatus to explain status/nature of error (user may have Reachability section hidden),
        // color .sb_netstatus text according to status
        Status status = _context.commSystem().getStatus();
        String txstatus = _context.commSystem().getLocalizedStatusString();
        NetworkState state = NetworkState.RUNNING;
        switch (status) {
            case OK:
            case IPV4_OK_IPV6_UNKNOWN:
            case IPV4_OK_IPV6_FIREWALLED:
            case IPV4_UNKNOWN_IPV6_OK:
            case IPV4_DISABLED_IPV6_OK:
            case IPV4_SNAT_IPV6_OK:
                List<RouterAddress> ras = routerInfo.getTargetAddresses("NTCP", "NTCP2");
                if (ras.isEmpty()) {return new NetworkStateMessage(NetworkState.RUNNING, txstatus);}
                byte[] ip = null;
                for (RouterAddress ra : ras) {
                    ip = ra.getIP();
                    if (ip != null) {break;}
                }
                if (ip == null) {
                    // Usually a transient issue during state transitions, possibly with hidden mod, don't show this
                    // NTCP2 addresses may not have an IP
                    //return new NetworkStateMessage(NetworkState.ERROR, _t("Unresolved TCP Address"));
                    return new NetworkStateMessage(NetworkState.RUNNING, txstatus);
                }
                if (TransportUtil.isPubliclyRoutable(ip, true)) { // TODO set IPv6 arg based on configuration?
                    return new NetworkStateMessage(NetworkState.RUNNING, txstatus);
                }
                return new NetworkStateMessage(NetworkState.ERROR, _t("Private TCP Address"));

            case IPV4_SNAT_IPV6_UNKNOWN:
            case DIFFERENT:
                return new NetworkStateMessage(NetworkState.ERROR, _t("SymmetricNAT"));

            case REJECT_UNSOLICITED:
                state = NetworkState.FIREWALLED;

            case IPV4_DISABLED_IPV6_FIREWALLED:
                if (routerInfo.getTargetAddress("NTCP") != null) {
                    return new NetworkStateMessage(NetworkState.WARN, _t("Firewalled with Inbound TCP Enabled"));
                }

            // fall through...
            case IPV4_FIREWALLED_IPV6_OK:
            case IPV4_FIREWALLED_IPV6_UNKNOWN:
                if ((_context.netDb()).floodfillEnabled()) {
                    return new NetworkStateMessage(NetworkState.WARN, _t("Firewalled &amp; Floodfill"));
                }
                //if (_context.router().getRouterInfo().getCapabilities().indexOf('O') >= 0)
                //    return new NetworkStateMessage(NetworkState.WARN, _t("WARN-Firewalled and Fast"));
                return new NetworkStateMessage(state, txstatus);

            case DISCONNECTED:
                return new NetworkStateMessage(NetworkState.ERROR, _t("Disconnected")); // .sb_warning shows -> _t("Check Network Connection"));

            case HOSED:
                return new NetworkStateMessage(NetworkState.ERROR, _t("UDP Port In Use"));
                // TODO: display advice in .sb_warning -> _t("Set i2np.udp.internalPort=xxxx in advanced config and restart"));

            case UNKNOWN:
                state = NetworkState.TESTING;

            case IPV4_UNKNOWN_IPV6_FIREWALLED:
            case IPV4_DISABLED_IPV6_UNKNOWN:
            default:
                List<RouterAddress> ra = routerInfo.getTargetAddresses("SSU", "SSU2");
                if (ra.isEmpty() && _context.router().getUptime() > 5*60*1000) {
                    if (getActivePeers() <= 0) {
                        // TODO: display advice in .sb_warning -> _t("Check Network Connection and Firewall"));
                        return new NetworkStateMessage(NetworkState.ERROR, _t("No Active Peers"));
                    } else if (_context.getProperty(ConfigNetHelper.PROP_I2NP_NTCP_HOSTNAME) == null ||
                        _context.getProperty(ConfigNetHelper.PROP_I2NP_NTCP_PORT) == null) {
                        return new NetworkStateMessage(NetworkState.ERROR, _t("UDP Disabled &amp; Inbound TCP host/port not set"));
                    } else {
                        return new NetworkStateMessage(NetworkState.WARN, _t("Firewalled with UDP Disabled"));
                    }
                }
                return new NetworkStateMessage(state, txstatus);
        }
    }

    /**
     * Retrieve amount of used memory.
     * @since 0.9.32 uncommented
     */
    public String getMemory() {
        DecimalFormat integerFormatter = new DecimalFormat("###,###,##0");
        long tot = SystemVersion.getMaxMemory();
        // This reads much higher than the graph, possibly because it's right in
        // the middle of a console refresh... so get it from the Rate instead.
        //long free = Runtime.getRuntime().freeMemory();
        long used = (long) _context.statManager().getRate("router.memoryUsed").getRate(60*1000).getAvgOrLifetimeAvg();
        if (used <= 0) {
            long free = Runtime.getRuntime().freeMemory();
            used = tot - free;
        }
        used /= 1024*1024;
        long total = tot / (1024*1024);
        if (used > total) {used = total;}
        // long free = Runtime.getRuntime().freeMemory()/1024/1024;
        // return integerFormatter.format(used) + "MB (" + usedPc + "%)";
        // return integerFormatter.format(used) + "MB / " + free + " MB";
        return integerFormatter.format(used) + " / " + total + " MiB";
    }

    /** @since 0.9.32 */
    public String getMemoryBar() {
        DecimalFormat integerFormatter = new DecimalFormat("###,###,##0");
        long tot = SystemVersion.getMaxMemory();
        // This reads much higher than the graph, possibly because it's right in
        // the middle of a console refresh... so get it from the Rate instead.
        //long free = Runtime.getRuntime().freeMemory();
        long used = (long) _context.statManager().getRate("router.memoryUsed").getRate(60*1000).getAvgOrLifetimeAvg();
        long usedPc;
        if (used <= 0) {
            long free = Runtime.getRuntime().freeMemory();
            usedPc = 100 - ((free * 100) / tot);
            used = (tot - free) / (1024*1024);
        } else {
            usedPc = used * 100 / tot;
            used /= 1024*1024;
        }
        long total = tot / (1024*1024);
        if (used > total) {used = total;}
        if (usedPc > 100) {usedPc = 100;}
        // long free = Runtime.getRuntime().freeMemory()/1024/1024;
        // return integerFormatter.format(used) + "MB (" + usedPc + "%)";
        // return integerFormatter.format(used) + "MB / " + free + " MB";
        return "<div class=\"percentBarOuter volatile\" id=sb_memoryBar><div class=percentBarText>RAM: " +
               integerFormatter.format(used) + " / " + total + " M" +
               "</div><div class=percentBarInner style=\"width: " + integerFormatter.format(usedPc) +
               "%;\"></div></div>";
    }

    /**
     * Retrieve CPU Load as a percentage.
     * @since 0.9.58+
     */
    public int getCPULoad() {
        if (_context == null) {return 0;}
        else {return SystemVersion.getCPULoad();}
    }

    /**
     * Retrieve CPU Load Average as a percentage.
     * @since 0.9.58+
     */
    public int getCPULoadAvg() {
        if (_context == null) {return 0;}
        else {return SystemVersion.getCPULoadAvg();}
    }

    /**
     * Retrieve System Load Average as a percentage.
     * @since 0.9.58+
     */
    public int getSystemLoad() {
        if (_context == null) {return 0;}
        else {return SystemVersion.getSystemLoad();}
    }

    /**
     * Render JVM CPU Load Bar
     * @since 0.9.58+
     */
    public String getCPUBar() {
        return "<div class=\"percentBarOuter volatile\" id=sb_CPUBar><div class=percentBarText>CPU: " +
               getCPULoadAvg() + "%" + (getSystemLoad() > 0 ? " | Sys Load Avg: " + getSystemLoad() + "%" : "") +
               "</div><div class=percentBarInner style=\"width:" + getCPULoadAvg() +
               "%\"></div></div>";
    }

    /**
     * Retrieve Tunnel build success as a percentage.
     * @since 0.9.58+
     */
    public int getTunnelBuildSuccess() {
        if (_context == null) {return 0;}
        Rate explSuccess = _context.statManager().getRate("tunnel.buildExploratorySuccess").getRate(10*60*1000);
        Rate explReject = _context.statManager().getRate("tunnel.buildExploratoryReject").getRate(10*60*1000);
        Rate explExpire = _context.statManager().getRate("tunnel.buildExploratoryExpire").getRate(10*60*1000);
        Rate clientSuccess = _context.statManager().getRate("tunnel.buildClientSuccess").getRate(10*60*1000);
        Rate clientReject = _context.statManager().getRate("tunnel.buildClientReject").getRate(10*60*1000);
        Rate clientExpire = _context.statManager().getRate("tunnel.buildClientExpire").getRate(10*60*1000);
        int success = (int)explSuccess.getLastEventCount() + (int)clientSuccess.getLastEventCount();
        int reject = (int)explReject.getLastEventCount() + (int)clientReject.getLastEventCount();
        int expire = (int)explExpire.getLastEventCount() + (int)clientExpire.getLastEventCount();
        int percentage;
        if (success < 1) {success = 1;}
        percentage = (100 * success) / (success + reject + expire);
        if (percentage == 100 || percentage == 0) {return 0;}
        else {return percentage;}
    }

    /**
     * How many peers we are talking to now
     *
     */
    public int getActivePeers() {
        if (_context == null) {return 0;}
        else {return _context.commSystem().countActivePeers();}
    }

    /**
     * Should we warn about a possible firewall problem?
     */
    public boolean showFirewallWarning() {
        return _context != null &&
               _context.netDb().isInitialized() &&
               _context.router().getUptime() > 2*60*1000 &&
               (!_context.commSystem().isDummy()) &&
               _context.commSystem().countActivePeers() <= 0 &&
               _context.netDb().getKnownRouters() > 5 ||
               _context.netDb().isInitialized() &&
               _context.router().getUptime() > 2*60*1000 &&
               (!_context.commSystem().isDummy()) &&
               _context.commSystem().countActivePeers() <= 0;
    }

    /**
     * How many active identities have we spoken with recently
     *
     */
    public int getActiveProfiles() {
        if (_context == null) {return 0;}
        else {return _context.profileOrganizer().countActivePeersInLastHour();}
    }
    /**
     * How many active peers the router ranks as fast.
     *
     */
    public int getFastPeers() {
        if (_context == null) {return 0;}
        else {return _context.profileOrganizer().countFastPeers();}
    }
    /**
     * How many active peers the router ranks as having a high capacity.
     *
     */
    public int getHighCapacityPeers() {
        if (_context == null) {return 0;}
        else {return _context.profileOrganizer().countHighCapacityPeers();}
    }
    /**
     * How many active peers the router ranks as well integrated.
     *
     */
    public int getWellIntegratedPeers() {
        if (_context == null) {return 0;}
        //return _context.profileOrganizer().countWellIntegratedPeers();
        return _context.peerManager().getPeersByCapability(FloodfillNetworkDatabaseFacade.CAPABILITY_FLOODFILL).size();
    }

    /**
     * How many peers are banned.
     * @since 0.9.32 uncommented
     */
    public int getBanlistedPeers() {
        if (_context == null) {return 0;}
        else {return _context.banlist().getRouterCount();}
    }

    /**
     * How many peers are unreachable.
     *
     */
    public int getUnreachablePeers() {
        if (_context == null) {return 0;}
        return _context.peerManager().getPeersByCapability(FloodfillNetworkDatabaseFacade.CAPABILITY_UNREACHABLE).size();
    }

    /**
     *    @return "x.xx / y.yy {K|M}"
     */
    public String getSecondKBps() {
        if (_context == null) {return "0 / 0";}
        else if (_context.bandwidthLimiter().getReceiveBps() < 1024 * 1024 || _context.bandwidthLimiter().getSendBps() < 1024 * 1024) {
            return formatPair(Math.round(_context.bandwidthLimiter().getReceiveBps() * 10.0 / 10.0),
                              Math.round(_context.bandwidthLimiter().getSendBps() * 10.0 / 10.0));
        } else {
            return formatPair(_context.bandwidthLimiter().getReceiveBps(), _context.bandwidthLimiter().getSendBps());
        }
    }

    /**
     *    @return "x.xx / y.yy {K|M}"
     */
    public String getFiveMinuteKBps() {
        if (_context == null) {return "0 / 0";}

        RateStat receiveRate = _context.statManager().getRate("bw.recvRate");
        double in = 0;
        if (receiveRate != null) {
            Rate r = receiveRate.getRate(5*60*1000);
            if (r != null) {in = r.getAverageValue();}
        }
        RateStat sendRate = _context.statManager().getRate("bw.sendRate");
        double out = 0;
        if (sendRate != null) {
            Rate r = sendRate.getRate(5*60*1000);
            if (r != null) {out = r.getAverageValue();}
        }
        if (in < 1024 * 1024 || out < 1024 * 1024) {
            return formatPair(Math.round(in) * 10.0 / 10.0, Math.round(out) * 10.0 / 10.0);
        } else {return formatPair(in, out);}
    }

    /**
     *    @return "x.xx / y.yy {K|M}"
     */
    public String getLifetimeKBps() {
        if (_context == null) {return "0 / 0";}

        RateStat receiveRate = _context.statManager().getRate("bw.recvRate");
        double in;
        if (receiveRate == null) {in = 0;}
        else {in = receiveRate.getLifetimeAverageValue();}
        RateStat sendRate = _context.statManager().getRate("bw.sendRate");
        double out;
        if (sendRate == null) {out = 0;}
        else {out = sendRate.getLifetimeAverageValue();}
        return formatPair(in, out);
    }

    /**
     *  Output is decimal, not binary
     *  @return "x.xx / y.yy {K|M}"
     */
    private static String formatPair(double in, double out) {
        boolean mega = in >= 1000*1000 || out >= 1000*1000;
        // scale both the same
        if (mega) {
            in /= 1000*1000;
            out /= 1000*1000;
        } else {
            in /= 1000;
            out /= 1000;
        }
        // control total width
        DecimalFormat fmt;
        if ((in >= 1000 || out >= 1000) && mega) {fmt = new DecimalFormat("#0.00");}
        else if ((in >= 10 || out >= 10) && mega) {fmt = new DecimalFormat("#0.0");}
        else {fmt = new DecimalFormat("#0.0");}
        return fmt.format(in) + THINSP + fmt.format(out) + "&nbsp;" + (mega ? 'M' : 'K');
    }

    /**
     * How much data have we received since the router started (pretty printed
     * string with 2 decimal places and the appropriate units - GB/MB/KB/bytes)
     *
     */
    public String getInboundTransferred() {
        if (_context == null) {return "0";}
        long received = _context.bandwidthLimiter().getTotalAllocatedInboundBytes();
        return DataHelper.formatSize2(received) + 'B';
    }

    /**
     * How much data have we sent since the router started (pretty printed
     * string with 2 decimal places and the appropriate units - GB/MB/KB/bytes)
     *
     */
    public String getOutboundTransferred() {
        if (_context == null) {return "0";}
        long sent = _context.bandwidthLimiter().getTotalAllocatedOutboundBytes();
        return DataHelper.formatSize2(sent) + 'B';
    }

    /**
     * Are both the webapp and TCG running?
     *
     * @since 0.9.58
     */
    public boolean isI2PTunnelRunning() {
        if (!_context.portMapper().isRegistered(PortMapper.SVC_I2PTUNNEL)) {return false;}
        ClientAppManager cmgr = _context.clientAppManager();
        return cmgr != null && cmgr.getRegisteredApp("i2ptunnel") != null;
    }

    /**
     * Client destinations connected locally.
     *
     * @return html section summary
     */
    public String getDestinations() {
        // convert the set to a list so we can sort by name and not lose duplicates
        List<Destination> clients = new ArrayList<Destination>(_context.clientManager().listClients());

        StringBuilder buf = new StringBuilder(512);
        boolean link = isI2PTunnelRunning();
        int clientCount = 0;
        if (!clients.isEmpty()) {
            DataHelper.sort(clients, new AlphaComparator());
            clientCount = clients.size();
            buf.append("<h3 id=sb_localTunnelsHeading");
            if (!link) {buf.append(" class=unregistered");}
            buf.append("><a href=\"/tunnelmanager\" target=_top title=\"")
               .append(_t("Add/remove/edit &amp; control your client and server tunnels"))
               .append("\">").append(_t("Service Tunnels"))
               .append(" <span id=tunnelCount class=\"badge volatile\" title=\"").append(_t("How many local service tunnels we're running"))
               .append("\">").append(clientCount).append("</span>").append("</a>")
               .append("<input type=checkbox id=toggle_sb_localtunnels class=\"toggleSection script\" checked hidden></h3>\n<hr class=\"b\">\n")
               .append("<table id=sb_localtunnels class=volatile>");

            for (Destination client : clients) {
                String name = getTunnelName(client);
                Hash h = client.calculateHash();
                Boolean server = _context.clientManager().shouldPublishLeaseSet(h);
                Boolean isPing = (name.startsWith("Ping") && name.contains("[")) || name.equals("I2Ping");
                Boolean isSnark = name.equals(_t("I2PSnark"));
                Boolean isI2PChat = name.equals(_t("Messenger")) || name.toLowerCase().equals(_t("i2pchat"));

                buf.append("<tr><td ");
                if (isSnark) {buf.append("class=tunnelI2PSnark ");}
                else if (isI2PChat) {buf.append("class=tunnelI2PChat ");}
                else if (server) {buf.append("class=tunnelServer ");}
                else if (isPing) {buf.append("class=ping ");}
                buf.append("><img src=/themes/console/images/");
                if (isSnark) {buf.append("snark.svg alt=I2PSnark title=\"").append(_t("Torrents"));}
                else if (isI2PChat) {buf.append("i2pchat.svg alt=I2PChat title=\"").append(_t("I2PChat"));}
                else if (server) {
                    buf.append("server.svg alt=Server title=\"").append(_t("Server"));
                    if (!isAdvanced()) {buf.append(" (").append(_t("service may be available to peers")).append(")");}
                } else {
                    if (isPing) {buf.append("ping.svg alt=Client title=\"").append(_t("Client"));}
                    else {buf.append("client.svg alt=Client title=\"").append(_t("Client"));}
                    if (!isAdvanced()) {buf.append(" (").append(_t("service is only available locally")).append(")");}
                }
                buf.append("\" width=16 height=16>");
                buf.append("</td><td><b><a href=\"/tunnels#").append(h.toBase64().substring(0,4));
                buf.append("\" target=_top title=\"").append(_t("Show tunnels"));
                if (isAdvanced()) {
                    buf.append(" [").append(h.toBase32().substring(0,8)).append("&hellip;b32.i2p]");
                }
                buf.append("\">");
                // Increase permitted max length of tunnel name & handle overflow with css
                if (name.length() <= 32) {buf.append(DataHelper.escapeHTML(name));}
                else {buf.append(DataHelper.escapeHTML(ServletUtil.truncate(name, 29))).append("&hellip;");}
                buf.append("</a></b></td>\n");
                LeaseSet ls = _context.clientNetDb(client.calculateHash()).lookupLeaseSetLocally(h);
                if (ls != null && _context.tunnelManager().getOutboundClientTunnelCount(h) > 0) {
                    long timeToExpire = ls.getEarliestLeaseDate() - _context.clock().now();
                    if ((timeToExpire < 0) || !ls.isCurrent(0)) {
                        // red light
                        buf.append("<td class=tunnelRebuilding><img src=/themes/console/images/local_down.svg alt=\"")
                           .append(_t("Rebuilding")).append("&hellip;\" title=\"").append(_t("Leases expired")).append(" ")
                           .append(DataHelper.formatDuration2(0 - timeToExpire))
                           .append(" ").append(_t("ago")).append(". ").append(_t("Rebuilding"))
                           .append("&hellip;\" width=16 height=16></td></tr>\n");
                    } else {
                        // green light
                        buf.append("<td class=tunnelReady><img src=/themes/console/images/local_up.svg alt=\"")
                           .append(_t("Ready")).append("\" title=\"").append(_t("Ready"))
                           .append("\" width=16 height=16></td></tr>\n");
                    }
                } else {
                    // yellow light
                    buf.append("<td class=tunnelBuilding><img src=/themes/console/images/local_inprogress.svg alt=\"")
                       .append(_t("Building")).append("&hellip;\" title=\"").append(_t("Building tunnels"))
                       .append("&hellip;\" width=16 height=16></td></tr>\n");
                }
            }
            buf.append("</table>");
        } else {
            buf.append("<h3 id=sb_localTunnelsHeading");
            if (!link) {buf.append(" class=unregistered");}
            buf.append("><a href=\"/tunnelmanager\" target=_top title=\"")
               .append(_t("Add/remove/edit &amp; control your client and server tunnels"))
               .append("\">").append(_t("Service Tunnels"))
               .append(" <span id=tunnelCount class=\"badge volatile\" title=\"").append(_t("How many local service tunnels we're running"))
               .append("\">").append(clientCount).append("</span>").append("</a>")
               .append("<input type=checkbox id=toggle_sb_localtunnels class=\"toggleSection script\" checked hidden></h3>\n<hr class=\"b\">\n")
               .append("<table id=sb_localtunnels class=\"volatile notunnels\">\n<tr><td colspan=3 class=center><i>")
               .append(_t("none")).append("</i></td></tr>\n</table>\n");
        }
        buf.append("<table id=localtunnelSummary hidden>\n<tr id=localtunnelsActive><td>")
           .append("<span id=snarkCount class=\"count_0\">0 x <img src=/themes/console/images/snark.svg></span>")
           .append("<span id=serverCount class=\"count_0\">0 x <img src=/themes/console/images/server.svg></span>")
           .append("<span id=clientCount class=\"count_0\">0 x <img src=/themes/console/images/client.svg></span>")
           .append("<span id=pingCount class=\"count_0\">0 x <img src=/themes/console/images/ping.svg></span>")
           .append("</td></tr>\n</table>\n");
        return buf.toString();
    }

    /**
     *  Compare translated nicknames - put "shared clients" first in the sort
     *  Inner class, can't be Serializable
     */
    private class AlphaComparator implements Comparator<Destination> {
        private final String xsc = _t("Shared Clients");
        private final String snark = _t("I2PSnark");

        public int compare(Destination lhs, Destination rhs) {
            String lname = getTunnelName(lhs);
            String rname = getTunnelName(rhs);

            boolean lSnark = lname.startsWith("I2PSnark") || lname.equals(snark);
            boolean rSnark = rname.startsWith("I2PSnark") || rname.equals(snark);
            boolean lServer = _context.clientManager().shouldPublishLeaseSet(lhs.calculateHash()) && !lSnark;
            boolean rServer = _context.clientManager().shouldPublishLeaseSet(rhs.calculateHash()) && !rSnark;
            boolean lClient = !lServer && !lSnark;
            boolean rClient = !rServer && !rSnark;
            boolean lI2PChat = lname.equals("Messenger") || lname.equals("I2PChat");
            boolean rI2PChat = lname.equals("Messenger") || lname.equals("I2PChat");
            boolean lPing = lname.startsWith("Ping") || lname.equals("I2Ping");
            boolean rPing = rname.startsWith("Ping") || rname.equals("I2Ping");

            if (lI2PChat && !rI2PChat) return -1;
            if (!lI2PChat && rI2PChat) return 1;
            if (lSnark && !rSnark) return -1;
            if (!lSnark && rSnark) return 1;
            if (lServer && !rServer) return -1;
            if (!lServer && rServer) return 1;
            if (lClient && !rClient) return -1;
            if (!lClient && rClient) return 1;
            if (lPing && !rPing) return -1;
            if (!lPing && rPing) return 1;

            return Collator.getInstance().compare(lname.toLowerCase(), rname.toLowerCase());
        }
    }

    /** translate here so collation works above */
    private String getTunnelName(Destination d) {
        TunnelPoolSettings in = _context.tunnelManager().getInboundSettings(d.calculateHash());
        String name = (in != null ? in.getDestinationNickname() : null);
        if (name == null) {
            TunnelPoolSettings out = _context.tunnelManager().getOutboundSettings(d.calculateHash());
            name = (out != null ? out.getDestinationNickname() : null);
            if (name == null) {name = d.toBase32();}
            else {name = _t(name);}
        } else {name = _t(name);}
        return name;
    }

    /**
     * How many free inbound tunnels we have.
     *
     */
    public int getInboundTunnels() {
        if (_context == null) {return 0;}
        else {return _context.tunnelManager().getFreeTunnelCount();}
    }

    /**
     * How many active outbound tunnels we have.
     *
     */
    public int getOutboundTunnels() {
        if (_context == null) {return 0;}
        else {return _context.tunnelManager().getOutboundTunnelCount();}
    }

    /**
     * How many inbound client tunnels we have.
     *
     */
    public int getInboundClientTunnels() {
        if (_context == null) {return 0;}
        else {return _context.tunnelManager().getInboundClientTunnelCount();}
    }

    /**
     * How many active outbound client tunnels we have.
     *
     */
    public int getOutboundClientTunnels() {
        if (_context == null) {return 0;}
        else {return _context.tunnelManager().getOutboundClientTunnelCount();}
    }

    /**
     * How many tunnels we are participating in.
     *
     */
    public int getParticipatingTunnels() {
        if (_context == null) {return 0;}
        else {return _context.tunnelManager().getParticipatingCount();}
    }

    public String getMaxParticipatingTunnels() {
        int defaultMax = SystemVersion.isSlow() ? 2*1000 :
                         SystemVersion.getMaxMemory() < 512*1024*1024 ? 5*1000 :
                         SystemVersion.getCores() >= 8 ? 12*1000 : 8*1000;
        if (_context.getProperty("router.maxParticipatingTunnels") != null) {
            return _context.getProperty("router.maxParticipatingTunnels");
        } else {
            return Integer.toString(defaultMax);
        }
    }

    /** @since 0.7.10 */
    public String getShareRatio() {
        if (_context == null) {return "0";}
        double sr = _context.tunnelManager().getShareRatio();
        DecimalFormat fmt = new DecimalFormat("##0");
        if (sr < 1) {fmt = new DecimalFormat("##0.00");}
        else if (sr < 10) {fmt = new DecimalFormat("##0.0");}
        return fmt.format(sr).replace("0.00", "0");
    }

    /**
     * How lagged our job queue is over the last minute (pretty printed with
     * the units attached)
     *
     */
    public String getJobLag() {
        if (_context == null) {return "0";}
        RateStat rs = _context.statManager().getRate("jobQueue.jobLag");
        if (rs == null) {return "0";}
        Rate lagRate = rs.getRate(60*1000);
        long maxLag = _context.jobQueue().getMaxLag();
        if (!isAdvanced() || maxLag < (double)30) {
            if (lagRate.getAverageValue() < 1) {
                return DataHelper.formatDuration2((double)lagRate.getAverageValue());
            } else {
                return DataHelper.formatDuration2((long)lagRate.getAverageValue());
            }
        } else {
            if (lagRate.getAverageValue() < 1 && (double)maxLag < 1) {
                return DataHelper.formatDuration2((double)lagRate.getAverageValue()) + THINSP + (double)maxLag + "&nbsp;µs";
            } else if (lagRate.getAverageValue() < 1) {
                return DataHelper.formatDuration2((double)lagRate.getAverageValue()) + THINSP + maxLag + "&nbsp;ms";
            } else {
                return (long)lagRate.getAverageValue() + THINSP + maxLag + "&nbsp;ms";
            }
        }
    }

    /**
     * How long it takes us to pump out a message, averaged over the last minute
     * (pretty printed with the units attached)
     *
     */
    public String getMessageDelay() {
        if (_context == null) {return "0";}
        return DataHelper.formatDuration2(_context.throttle().getMessageDelay());
    }

    /**
     * How long it takes us to test our tunnels, averaged over the last 10 minutes
     * (pretty printed with the units attached)
     *
     */
    public String getTunnelLag() {
        if (_context == null) {return "0";}
        return DataHelper.formatDuration2(_context.throttle().getTunnelLag());
    }

    public String getTunnelStatus() {
        if (_context == null) {return "";}
        return _context.throttle().getLocalizedTunnelStatus();
    }

    public String getConcurrency() {
        if (_context == null) {return "0 / 0";}
        RateStat cb = _context.statManager().getRate("tunnel.concurrentBuilds");
        RateStat brt = _context.statManager().getRate("tunnel.buildRequestTime");
        Rate concurrentBuilds = cb.getRate(60*1000);
        Rate buildRequestTime = brt.getRate(60*1000);
        double cbavg = concurrentBuilds.getAvgOrLifetimeAvg();
        String brtavg = DataHelper.formatDuration2((long)buildRequestTime.getAvgOrLifetimeAvg());
        Router router = _context.router();
        if (router.getUptime() < 15 * 1000) {return "0 / 0";}
        else {
            DecimalFormat fmt = new DecimalFormat("##0.0");
            if (cbavg < 0.1 || cbavg > 10) {
                if (cbavg < 0.1) {fmt = new DecimalFormat("##0.00");}
                if (cbavg > 10) {fmt = new DecimalFormat("##0");}
                return String.valueOf(fmt.format(cbavg).replace(".00", "")) + " / " + brtavg;
            } else {return String.valueOf(fmt.format(cbavg).replace(".0", "")) + " / " + brtavg;}
        }
    }

    public String getInboundBacklog() {
        if (_context == null) {return "0";}
        return String.valueOf(_context.tunnelManager().getInboundBuildQueueSize());
    }

    /** @since 0.9.49+ */
    public int getAvgPeerTestTime() {
        if (_context == null) {return 0;}
        RateStat ok = _context.statManager().getRate("peer.testOK");
        Rate rok = ok.getRate(60*60*1000);
        RateStat tooslow = _context.statManager().getRate("peer.testTooSlow");
        Rate rtooslow = tooslow.getRate(60*60*1000);
        int avgTestTime = (int) rok.getLifetimeAverageValue() + (int) rtooslow.getLifetimeAverageValue();
        return avgTestTime;
    }

    /** @since 0.9.50+ */
    public int getAvgPeerTestTimeGood() {
        if (_context == null) {return 0;}
        RateStat ok = _context.statManager().getRate("peer.testOK");
        Rate rok = ok.getRate(60*60*1000);
        int avgTestTimeGood = (int) rok.getLifetimeAverageValue();
        return avgTestTimeGood;
    }

    private static boolean updateAvailable() {
        return NewsHelper.isUpdateAvailable();
    }

    private boolean unsignedUpdateAvailable() {
        return NewsHelper.isUnsignedUpdateAvailable(_context);
    }

    /** @since 0.9.20 */
    private boolean devSU3UpdateAvailable() {
        return NewsHelper.isDevSU3UpdateAvailable(_context);
    }

    private static String getUpdateVersion() {
        return DataHelper.escapeHTML(NewsHelper.updateVersion());
    }

    private static String getUnsignedUpdateVersion() {
        // value is a formatted date, does not need escaping
        return NewsHelper.unsignedUpdateVersion();
    }

    /** @since 0.9.20 */
    private static String getDevSU3UpdateVersion() {
        return DataHelper.escapeHTML(NewsHelper.devSU3UpdateVersion());
    }

    /**
     *  The update status and buttons
     *  @since 0.8.13 moved from SidebarRenderer
     */
    public String getUpdateStatus() {
        StringBuilder buf = new StringBuilder(512);
        // display all the time so we display the final failure message, and plugin update messages too
        String status = NewsHelper.getUpdateStatus();
        String source = _context.getProperty(ConfigUpdateHandler.PROP_ZIP_URL);
        boolean needSpace = false;
        if (status.length() > 0) {
            buf.append("<h4 class=\"sb_info sb_update volatile");
            if (NewsHelper.isUpdateInProgress()) {buf.append(" inProgress");}
            buf.append("\">").append(status).append("</h4>\n");
            needSpace = true;
        }
        String dver = NewsHelper.updateVersionDownloaded();
        if (dver == null) {
            dver = NewsHelper.devSU3VersionDownloaded();
            if (dver == null) {dver = NewsHelper.unsignedVersionDownloaded();}
        }
        if (dver != null && !NewsHelper.isUpdateInProgress() && !_context.router().gracefulShutdownInProgress()) {
            if (needSpace) {buf.append("<hr>");}
            else {needSpace = true;}
            if (!_context.router().gracefulShutdownInProgress() && !NewsHelper.isUpdateInProgress()) {
                buf.append("<h4 id=restartRequired class=\"sb_info sb_update volatile\" title=\"");
                if (_context.hasWrapper() || NewsHelper.isExternalRestartPending()) {
                    buf.append(_t("Click Restart to install").replace("Click ", ""));
                } else {
                    buf.append(_t("Click Shutdown and restart to install").replace("Click ", ""));
                }
                buf.append("\"><b>");
                if (source != null && source.contains("skank")) {buf.append("I2P+ ");}
                buf.append(_t("Update downloaded")).append("<br>")
                   .append("[").append(_t("{0}", DataHelper.escapeHTML(dver))).append("]")
                   .append("</b></h4>");
            } else {buf.append("<h4 id=restartRequired class=\"sb_info sb_update volatile inactive\" hidden></h4>");}
        } else if (dver != null && _context.router().gracefulShutdownInProgress() &&
                   !NewsHelper.isUpdateInProgress() &&
                   (_context.getProperty("router.updatePolicy") != null &&
                   !_context.getProperty("router.updatePolicy").equals("install"))) {
            buf.append("<h4 id=shutdownInProgress class=\"sb_info sb_update volatile\"><b>")
               .append(_t("Updating after restart")).append("&hellip;</b></h4>");
        }
        boolean avail = updateAvailable();
        boolean unsignedAvail = unsignedUpdateAvailable();
        boolean devSU3Avail = devSU3UpdateAvailable();
        String constraint = avail ? NewsHelper.updateConstraint() : null;
        String unsignedConstraint = unsignedAvail ? NewsHelper.unsignedUpdateConstraint() : null;
        String devSU3Constraint = devSU3Avail ? NewsHelper.devSU3UpdateConstraint() : null;
/*
        if (avail && constraint != null &&
            !NewsHelper.isUpdateInProgress() &&
            !_context.router().gracefulShutdownInProgress()) {
            if (needSpace)
                buf.append("<hr>");
            else
                needSpace = true;
            buf.append("<h4 class=\"sb_info sb_update volatile\"><b>").append(_t("Update available")).append(":<br>");
            buf.append(_t("Version {0}", getUpdateVersion())).append("<br>");
            buf.append(constraint).append("</b></h4>");
            avail = false;
        }
*/
        if (unsignedAvail && unsignedConstraint != null &&
            !NewsHelper.isUpdateInProgress() &&
            !_context.router().gracefulShutdownInProgress()) {
            if (needSpace) {buf.append("<hr>");}
            else {needSpace = true;}
            buf.append("<h4 class=\"sb_info sb_update volatile\"><b>").append(_t("Update available")).append(":<br>")
               .append(_t("Version {0}", getUnsignedUpdateVersion())).append("<br>")
               .append(unsignedConstraint).append("</b></h4>");
            unsignedAvail = false;
        }
        if (devSU3Avail && devSU3Constraint != null && !NewsHelper.isUpdateInProgress() && !_context.router().gracefulShutdownInProgress()) {
            if (needSpace) {buf.append("<hr>");}
            else {needSpace = true;}
            buf.append("<h4 class=\"sb_info sb_update volatile\"><b>").append(_t("Update available")).append(":<br>")
               .append(_t("Version {0}", getDevSU3UpdateVersion())).append("<br>")
               .append(devSU3Constraint).append("</b></h4>");
            devSU3Avail = false;
        }
        if ((avail || unsignedAvail || devSU3Avail) && !NewsHelper.isUpdateInProgress() &&
            !_context.router().gracefulShutdownInProgress() && !_context.commSystem().isDummy() &&
            _context.portMapper().isRegistered(PortMapper.SVC_HTTP_PROXY) &&  // assume using proxy for now
            getAction() == null && getUpdateNonce() == null) {
                if (needSpace) {buf.append("<hr>");}
                // else {needSpace = true;} // needed ?
                long nonce = _context.random().nextLong();
                String prev = System.getProperty("net.i2p.router.web.UpdateHandler.nonce");
                if (prev != null) {
                    System.setProperty("net.i2p.router.web.UpdateHandler.noncePrev", prev);
                }
                System.setProperty("net.i2p.router.web.UpdateHandler.nonce", nonce + "");
                String uri = getRequestURI();
                buf.append("<form id=sb_updateform action=\"").append(uri).append("\" method=POST class=volatile target=processSidebarForm>\n")
                   .append("<input type=hidden name=\"updateNonce\" value=\"").append(nonce).append("\">\n");
/*
                if (avail) {
                    buf.append("<span id=updateAvailable class=volatile>").append(_t("Release update available")).append("<br><i>")
                       .append(_t("Version")).append(": ").append(getUpdateVersion())
                       .append("</i></span><br><button type=submit id=sb_downloadReleaseUpdate class=download name=updateAction value=\"signed\" >")
                       // Note to translators: parameter is a version, e.g. "0.8.4"
                       .append(_t("Download {0} Update", getUpdateVersion()))
                       .append(_t("Download I2P Update"))
                       .append("</button><br>\n");
                }
*/
                if (devSU3Avail) {
                    buf.append("<span id=updateAvailable class=volatile>").append(_t("Signed development update available")).append("<br><i>")
                       .append(_t("Version")).append(": ").append(getDevSU3UpdateVersion())
                       .append("</i></span><br><button type=submit id=sb_downloadSignedDevUpdate class=download name=updateAction value=\"DevSU3\" >")
                       .append(_t("Download I2P Update"))
                       .append("</button><br>\n");
                }

                if (unsignedAvail) {
                    buf.append("<span id=updateAvailable class=volatile>");
                    if (source.contains("skank")) {buf.append(_t("Unsigned update available").replace("update", "I2P+ update"));}
                    else {buf.append(_t("Unsigned update available").replace("update", "I2P update"));}
                    buf.append("<br><i>").append(getUnsignedUpdateVersion())
                       .append("</i></span><br><button type=submit id=sb_downloadUnsignedDevUpdate class=download name=updateAction value=\"Unsigned\" >");
                    if (source != null && source.contains("skank")) {buf.append(_t("Download I2P Update").replace("I2P", "I2P+"));}
                    else {buf.append(_t("Download I2P Update"));}
                    buf.append("</button><br>\n");
                }
                buf.append("</form>\n");
        }
        return buf.toString();
    }

    /**
     *  The restart status and buttons
     *  @since 0.8.13 moved from SidebarRenderer
     */
    public String getRestartStatus() {
        return ConfigRestartBean.renderStatus(getRequestURI(), getAction(), getConsoleNonce());
    }

    /**
     *  The firewall status and reseed status/buttons
     *  @since 0.9 moved from SidebarRenderer
     */
    public String getFirewallAndReseedStatus() {
        StringBuilder buf = new StringBuilder(256);
        if (showFirewallWarning()) {
            buf.append("<h4 id=sb_warning class=volatile><span><a href=\"/help#configurationhelp\" target=_top title=\"")
               .append(_t("Help with firewall configuration"))
               .append("\">")
               .append(_t("Check network connection and NAT/firewall!"))
               .append("</a></span></h4>");
        } else {buf.append("<h4 id=sb_warning class=\"volatile hide\" hidden></h4>");} // Hide warn but retain h4 so ajax refresh picks it up

        if (DeadlockDetector.isDeadlocked()) {
            buf.append("<div class=sb_notice><b>")
               .append(_t("Deadlock detected"))
               .append(" - <a href=\"/logs\">")
               .append(_t("Please report"))
               .append("</a> - ").append(_t("After reporting, please restart your router"))
               .append("</b></div>");
        }

        // checker will be null for DummyNetworkDatabaseFacade
        ReseedChecker checker = _context.netDb().reseedChecker();
        String status = checker != null ? checker.getStatus() : "";
        if (status.length() > 0) {
            // Show status message even if not running, timer in ReseedChecker should remove after 20 minutes
            buf.append("<div class=\"sb_notice volatile\" id=sb_notice><i>").append(status).append("</i></div>");
        } else {
            // Hide status message but retain div so ajax refresh picks it up
            buf.append("<div class=\"sb_notice volatile hide\" id=sb_notice hidden></div>");
        }
        if (checker != null && !checker.inProgress()) {
            // If a new reseed isn't running, and the last reseed had errors, show error message
            String reseedErrorMessage = checker.getError();
            if (reseedErrorMessage.length() > 0) {
                buf.append("<div class=\"sb_notice volatile\" id=sb_notice><i>").append(reseedErrorMessage).append("</i></div>");
            }
            // If showing the reseed link is allowed
            if (allowReseed()) {
                // While no reseed occurring, show reseed link
                long nonce = _context.random().nextLong();
                String prev = System.getProperty("net.i2p.router.web.ReseedHandler.nonce");
                if (prev != null) System.setProperty("net.i2p.router.web.ReseedHandler.noncePrev", prev);
                System.setProperty("net.i2p.router.web.ReseedHandler.nonce", nonce+"");
                String uri = getRequestURI();
                buf.append("<p class=volatile><form action=\"").append(uri).append("\" method=POST>\n");
                buf.append("<input type=hidden name=\"reseedNonce\" value=\"").append(nonce).append("\">\n");
                buf.append("<button type=submit title=\"").append(_t("Attempt to download router reference files (if automatic reseed has failed)"));
                buf.append("\" id=sb_manualReseed class=reload value=\"Reseed\" >").append(_t("Reseed")).append("</button></form></p>\n");
            }
        }
        if (buf.length() <= 0) {return "";}
        return buf.toString();
    }

    private NewsHelper _newshelper;
    public void storeNewsHelper(NewsHelper n) {_newshelper = n;}
    public NewsHelper getNewsHelper() {return _newshelper;}

    private static final String SS = Character.toString(S);

    public List<String> getSummaryBarSections(String page) {
        String config;
        if ("home".equals(page)) {
            config = _context.getProperty(PROP_SUMMARYBAR + page, isAdvanced() ? DEFAULT_MINIMAL_ADVANCED : DEFAULT_MINIMAL);
        } else {
            config = _context.getProperty(PROP_SUMMARYBAR + page);
            if (config == null) {
                if (version.equals(firstVersion)) {
                    config = _context.getProperty(PROP_SUMMARYBAR + "default", isAdvanced() ? DEFAULT_FULL_ADVANCED : DEFAULT_FULL_NEWUSER);
                } else {
                    config = _context.getProperty(PROP_SUMMARYBAR + "default", isAdvanced() ? DEFAULT_FULL_ADVANCED : DEFAULT_FULL);
                }
            }
        }
        if (config.length() <= 0) {return Collections.emptyList();}
        return Arrays.asList(DataHelper.split(config, SS));
    }

    static void saveSummaryBarSections(RouterContext ctx, String page, Map<Integer, String> sections) {
        StringBuilder buf = new StringBuilder(512);
        for(String section : sections.values()) {buf.append(section).append(S);}
        ctx.router().saveConfig(PROP_SUMMARYBAR + page, buf.toString());
    }

    /** output the summary bar to _out */
    public void renderSummaryBar() throws IOException {
        SidebarRenderer renderer = new SidebarRenderer(_context, this);
        renderer.renderSummaryHTML(_out);
    }

    /* below here is stuff we need to get from sidebar_noframe.jsp to SidebarRenderer */

    private String _action;
    public void setAction(String s) {_action = s == null ? null : DataHelper.stripHTML(s);}
    public String getAction() {return _action;}

    private String _consoleNonce;
    public void setConsoleNonce(String s) {_consoleNonce = s == null ? null : DataHelper.stripHTML(s);}
    public String getConsoleNonce() {return _consoleNonce;}

    private String _updateNonce;
    public void setUpdateNonce(String s) {_updateNonce = s == null ? null : DataHelper.stripHTML(s);}
    public String getUpdateNonce() {return _updateNonce;}

    private String _requestURI;
    public void setRequestURI(String s) {_requestURI = s == null ? null : DataHelper.stripHTML(s);}

    /**
     * @return non-null; "/home" if (strangely) not set by jsp
     */
    public String getRequestURI() {return _requestURI != null ? _requestURI : "/home";}

    public String getConfigTable() {
        String[] allSections = SidebarRenderer.ALL_SECTIONS;
        Map<String, String> sectionNames = SidebarRenderer.SECTION_NAMES;
        List<String> sections = getSummaryBarSections("default");
        // translated section name to section id
        TreeMap<String, String> sortedSections = new TreeMap<String, String>(Collator.getInstance());

        // Forward-convert old section names
        int pos = sections.indexOf("General");
        if (pos >= 0) {sections.set(pos, "RouterInfo");}
        pos = sections.indexOf("ShortGeneral");
        if (pos >= 0) {sections.set(pos, "ShortRouterInfo");}

        for (int i = 0; i < allSections.length; i++) {
            String section = allSections[i];
            if (!sections.contains(section)) {
                String name = sectionNames.get(section);
                if (name != null) {sortedSections.put(_t(name), section);}
            }
        }

        String theme = _context.getProperty(CSSHelper.PROP_THEME_NAME, CSSHelper.DEFAULT_THEME);
        String imgPath = CSSHelper.BASE_THEME_PATH + "/images/";

        StringBuilder buf = new StringBuilder(2048);
        buf.append("<table id=sidebarconf><tr><th title=\"Mark section for removal from the sidebar\">")
           .append(_t("Remove"))
           .append("</th><th>")
           .append(_t("Name"))
           .append("</th><th colspan=2>")
           .append(_t("Order"))
           .append("</th></tr>\n");
        for (String section : sections) {
            int i = sections.indexOf(section);
            String name = sectionNames.get(section);
            if (name == null) {continue;}
            buf.append("<tr><td><input type=checkbox class=optbox id=\"")
               .append(name.replace(" ", "_").replace("\'", "").replace("(", "").replace(")", "").replace("&amp;", ""))
               .append("\" name=\"delete_")
               .append(i)
               .append("\"></td><td><label for=\"")
               .append(name.replace(" ", "_").replace("\'", "").replace("(", "").replace(")", "").replace("&amp;", ""))
               .append("\">")
               .append(_t(name))
               .append("</label></td><td><input type=hidden name=\"order_")
               .append(i).append('_').append(section)
               .append("\" value=\"")
               .append(i)
               .append("\">");
            if (i > 0) {
                buf.append("<button type=submit class=buttonTop name=action value=\"move_")
                   .append(i)
                   .append("_top\"><img alt=\"")
                   .append(_t("Top"))
                   .append("\" src=\"")
                   .append(imgPath)
                   .append("move_top.svg")
                   .append("\" title=\"")
                   .append(_t("Move to top"))
                   .append("\"/></button>")
                   .append("<button type=submit class=buttonUp name=action value=\"move_")
                   .append(i)
                   .append("_up\"><img alt=\"")
                   .append(_t("Up"))
                   .append("\" src=\"")
                   .append(imgPath)
                   .append("move_up.svg")
                   .append("\" title=\"")
                   .append(_t("Move up"))
                   .append("\"/></button>");
            }
            buf.append("</td><td>");
            if (i < sections.size() - 1) {
                buf.append("<button type=submit class=buttonDown name=action value=\"move_")
                   .append(i)
                   .append("_down\"><img alt=\"")
                   .append(_t("Down"))
                   .append("\" src=\"")
                   .append(imgPath)
                   .append("move_down.svg")
                   .append("\" title=\"")
                   .append(_t("Move down"))
                   .append("\"/></button>")
                   .append("<button type=submit class=buttonBottom name=action value=\"move_")
                   .append(i)
                   .append("_bottom\"><img alt=\"")
                   .append(_t("Bottom"))
                   .append("\" src=\"")
                   .append(imgPath)
                   .append("move_bottom.svg")
                   .append("\" title=\"")
                   .append(_t("Move to bottom"))
                   .append("\"/></button>");
            }
            buf.append("</td></tr>\n");
        }
        buf.append("<tr><td><input type=submit name=action class=delete value=\"")
           .append(_t("Delete selected")).append("\"></td><td>")
           .append("<select name=\"name\">\n<option value=\"\" selected=selected>")
           .append(_t("Select a section to add"))
           .append("</option>\n");

        for (Map.Entry<String, String> e : sortedSections.entrySet()) {
            String name = e.getKey();
            String s = e.getValue();
            buf.append("<option value=\"").append(s).append("\">").append(name).append("</option>\n");
        }

        buf.append("</select>\n<input type=hidden name=\"order\" value=\"")
           .append(sections.size()).append("\"></td><td colspan=2>")
           .append("<input type=submit name=action class=\"add\" value=\"")
           .append(_t("Add item")).append("\"></td></tr>\n").append("</table>\n");
        return buf.toString();
    }
}
