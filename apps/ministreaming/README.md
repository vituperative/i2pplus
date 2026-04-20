# Ministreaming (`ministreaming/`)

API, interfaces, and factory for TCP-like sockets over I2P's unreliable data messages. The implementation lives in `streaming/`.

## Overview

Provides familiar socket semantics over I2P:

```java
I2PSocketManager sm = I2PSocketManagerFactory.getManager();
I2PSocket socket = sm.connect(destination);

// Standard Java socket API
OutputStream out = socket.getOutputStream();
out.write("Hello, I2P!".getBytes());
out.close();

// Clean close
socket.close();
sm.destroy();
```

## Key Classes

| Class              | Purpose                              |
| ------------------ | ------------------------------------ |
| `I2PSocketManager` | Creates sockets, manages connections |
| `I2PSocket`        | Connected socket (like `Socket`)     |
| `I2PServerSocket`  | Accepts incoming connections         |
| `I2PStreamSession` | Session state                        |

## Differences from TCP

- **Destinations** are the identity, not IP:port
- **Bidirectional** from start (no listen socket needed)
- **Explicit close** - no half-close (FIN)
- **Built-in encryption** - no TLS needed
- **Congestion sliding window** limits buffer bloat

## Usage

```java
// Server side
I2PServerSocket ss = sm.getServerSocket(6666);
I2PClientSocket client = ss.accept();

// Client side  
Destination serverDest = ...;
I2PSocket socket = sm.connect(serverDest);
```

## Packages

- `net.i2p.client.streaming` - `I2PSocketManager`, `I2PSocket`, `I2PServerSocket`

## Building

```bash
# Gradle
./gradlew :apps:ministreaming:jar

# Ant (from repo root)
ant buildMinistreaming
```