package net.i2p.client;

import net.i2p.data.TunnelId;

/**
 * Event fired when a tunnel is removed from a client tunnel pool.
 * Allows streaming library to gracefully handle connection migration.
 *
 * @since 0.9.69
 */
public class TunnelRemovalEvent {
    private final String _poolName;
    private final TunnelId _tunnelId;
    private final boolean _isInbound;
    private final RemovalReason _reason;
    private final long _timestamp;

    public enum RemovalReason {
        /** Tunnel was explicitly removed or failed */
        EXPLICIT_REMOVAL,
        /** Tunnel was pruned to maintain pool size */
        POOL_PRUNING,
        /** Tunnel lease expired */
        LEASE_EXPIRED,
        /** Pool is being shut down */
        POOL_SHUTDOWN,
        /** Tunnel failed and was removed */
        FAILURE
    }

    public TunnelRemovalEvent(String poolName, TunnelId tunnelId, boolean isInbound, RemovalReason reason) {
        _poolName = poolName;
        _tunnelId = tunnelId;
        _isInbound = isInbound;
        _reason = reason;
        _timestamp = System.currentTimeMillis();
    }

    public String getPoolName() {
        return _poolName;
    }

    public TunnelId getTunnelId() {
        return _tunnelId;
    }

    public boolean isInbound() {
        return _isInbound;
    }

    public RemovalReason getReason() {
        return _reason;
    }

    public long getTimestamp() {
        return _timestamp;
    }

    @Override
    public String toString() {
        return "TunnelRemovalEvent [" + _poolName + " id=" + _tunnelId +
               " direction=" + (_isInbound ? "inbound" : "outbound") +
               " reason=" + _reason + "]";
    }
}
