# I2PSnark (`i2psnark/`)

I2P-enhanced BitTorrent client with web UI, DHT, magnet links, PEX, and swarm management for anonymous file sharing over I2P. Fork of Snark.

## Overview

I2PSnark brings BitTorrent to I2P with privacy built-in:
- All peers are I2P destinations (anonymous)
- No tracker needed (DHT or local)
- Integrated in router console

## How It Differs from Regular BitTorrent

| Aspect   | Regular BitTorrent | I2PSnark             |
| -------- | ------------------ | -------------------- |
| Peers    | IP addresses       | I2P destinations     |
| Trackers | Centralized        | DHT or local         |
| Announce | Public tracker     | I2P router (private) |
| Metadata | Centralized        | DHT/magnet           |

## Key Features

- **DHT** - Distributed hash table for peer discovery without trackers
- **PEX** - Peer exchange from connected peers  
- **Magnet links** - Share `.i2p` torrent links
- **Auto-scaling tunnels** - Tunnel count adjusts based on activity

## Packages

- `org.klomp.snark` - Torrent engine, peer coordination, storage, tracker, DHT
- `org.klomp.snark.web` - Web UI servlets

## Building

```bash
# Gradle
./gradlew :apps:i2psnark:jar

# Ant (from repo root)
ant buildI2PSnark
```
