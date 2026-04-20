# SAM Bridge (`sam/`)

SAM (Simple Anonymous Messaging) bridge for integrating external applications with I2P. Applications connect via a simple TCP-based protocol, eliminating the need to bundle the full I2P router.

## Overview

SAM bridges applications to I2P without requiring them to run embedded. The app connects to SAM over `localhost:7656` (default) and gets:
- **Destinations** (I2P identities) with `.b32.i2p` base32 addresses
- **Streams** for reliable, in-order TCP-like communication
- **Datagrams** for fire-and-forget messages

## Usage

```python
# Python 3 example
import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(('127.0.0.1', 7656))
s.sendall(b"HELLO VERSION MIN=3.1\n")
# ... handle SAM protocol responses
```

## Session Types

| Type       | Description                       | Use Case                  |
| ---------- | --------------------------------- | ------------------------- |
| `STREAM`   | Reliable, in-order, bidirectional | File transfer, chat       |
| `DATAGRAM` | Fire-and-forget, fixed size       | Status updates, signaling |
| `RAW`      | Low-level I2P messages            | Custom protocols          |

## Protocol

```
HELLO VERSION MIN=3.1\n           # Handshake
SESSION CREATE STYLE=STREAM ...\n   # Create destination
STREAM CONNECT DESTINATION=<hash> # Connect to peer
```

See `SAMBridge.java` for the full protocol specification.

## Packages

- `net.i2p.sam` - SAM bridge, v3 handler, stream and datagram sessions

## Building

```bash
# Gradle
./gradlew :apps:sam:jar

# Ant (from repo root)
ant buildSAM
```