# Jetty Integration (`jetty/`)

Embedded Jetty web server for I2P web applications.

## What It Does

- Starts Jetty servlet container
- Configures HTTP listeners on I2P ports
- Routes requests to webapps (console, i2psnark, etc.)
- Manages SSL for I2P HTTPS

## Structure

| Layer             | Purpose              |
| ----------------- | -------------------- |
| `I2PJettyStartup` | Initializes Jetty    |
| `I2PHttpServer`   | Main server instance |
| `I2PHandler`      | Request routing      |
| Connectors        | Network listeners    |

## Default Ports

| Port   | Application          |
| ------ | -------------------- |
| 7657   | Router console       |
| 7658   | HTTP proxy (clients) |
| 7659   | I2PSnark             |
| 7660   | I2PTunnel            |

## Packages

- `net.i2p.jetty` - Server bootstrap, I2P request logging, logger integration

## Building

```bash
# Gradle
./gradlew :apps:jetty:jar

# Ant (from repo root)
ant buildJetty
```