# I2PControl (`i2pcontrol/`)

JSON-RPC 2.0 API for external applications to control and monitor the I2P router.
Enables home automation dashboards, mobile apps, and scripts.

## Overview

I2PControl exposes router functionality over HTTP with JSON-RPC 2.0:

```
POST http://localhost:7657/i2pcontrol/jsonrpc
{
  "method": "RouterInfo",
  "params": [],
  "id": 1
}
```

## Available Methods

| Category   | Methods                                       |
| ---------- | --------------------------------------------- |
| Router     | `RouterInfo`, `RouterManager`, `RouterUpdate` |
| Network    | `NetworkStatus`, `NetworkClocks`, `Peers`     |
| Tunnels    | `TunnelManager`, `TunnelList`, `TunnelConfig` |
| I2PSnark   | `SnarkManager`, `SnarkList`                   |
| Config     | `ConfigReseed`, `ConfigAll`                   |

## Authentication

Required in `i2cp.props`:
```
i2pcontrol.username=admin
i2pcontrol.password=changeme
i2pcontrol.port=7657
```

## Use Cases

- **Monitoring dashboards** - Grafana, Home Assistant
- **Mobile apps** - I2P Android controllers
- **Automation** - Tunnel on/off based on schedule
- **Scripts** - Check router health, restart if needed

## Packages

- `net.i2p.i2pcontrol` - Controller, servlets, security, JSON-RPC handlers

## Building

```bash
# Gradle
./gradlew :apps:i2pcontrol:jar

# Ant (from repo root)
ant buildI2PControl
```