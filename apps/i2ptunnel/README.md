# I2PTunnel (`i2ptunnel/`)

Tunnel manager for I2P - bridges local services to the I2P network and vice versa.

## What It Does

I2PTunnel creates two-way bridges between I2P and the regular internet:

- **Client tunnels** - Connect to external services via I2P (HTTP proxy, SOCKS)
- **Server tunnels** - Expose local services to I2P (websites, IRC, etc.)

## Tunnel Types

| Type        | Direction      | Use Case                   |
| ----------- | -------------- | -------------------------- |
| HTTP Client | I2P → External | Browse .i2p sites          |
| SOCKS       | I2P → External | I2P-aware apps             |
| IRC Client  | I2P → IRC      | Connect to IRC through I2P |
| HTTP Server | Local → I2P    | Host a website on .i2p     |
| IRC Server  | Local → I2P    | Host an IRC server         |
| Streamr     | UDP bridge     | UDP over I2P               |

## Key Classes

| Class                 | Purpose                    |
| --------------------- | -------------------------- |
| `I2PTunnel`           | Base class, configuration  |
| `TunnelController`    | Per-tunnel lifecycle       |
| `I2PTunnelHTTPServer` | Serve local HTTP to I2P    |
| `I2PTunnelHTTPClient` | Proxy external through I2P |
| `I2PTunnelClientBase` | Base for client tunnels    |
| `I2PTunnelServerBase` | Base for server tunnels    |

## Packages

- `net.i2p.i2ptunnel` - Core tunnel framework, controller, and configuration
- `net.i2p.i2ptunnel.sockets` - Protocol-specific tunnel implementations (HTTP, SOCKS, IRC)

## Building

```bash
# Gradle
./gradlew :apps:i2ptunnel:jar

# Ant (from repo root)
ant buildI2PTunnel
```