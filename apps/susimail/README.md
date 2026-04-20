# SusiMail (`susimail/`)

Webmail interface for I2P. Provides POP3/SMTP access to email through I2P, with local caching for privacy and offline access.

## Overview

SusiMail bridges traditional email to I2P:

```
Internet Email --> [SMTP into I2P] --> I2P Router --> [POP3 out] --> Local client
```

## Privacy Features

- **Local caching** - Messages stored locally, not fetched on-demand
- **Header scrubbing** - Removes Internet email headers identifying your router
- **No external MX** - All traffic stays within I2P
- **Outbound encryption** - All emails encrypted to I2P destinations

## Email Flow

1. **Inbound (POP3)** - External POP3 server fetches → local cache → I2P-deliverable
2. **Outbound (SMTP)** - Composes in web UI → encrypts to recipient destination → sends over I2P

## Configuration

Requires an external POP3/SMTP server for Internet email. Configure in console:
- POP3 server, username, password
- SMTP server for outbound
- Reply-to address mapping

## Use Cases

- **Hidden email** - Use `.i2p` destinations as email addresses
- **Anonymous leaks** - Send to secure drop points
- **Mail2web** - Internet email to `.i2p` destinations

## Packages

- `i2p.susi.webmail` - Mail cache, message handling, web UI
- `i2p.susi.webmail.pop3` - POP3 client
- `i2p.susi.webmail.smtp` - SMTP client

## Building

```bash
# Gradle
./gradlew :apps:susimail:jar

# Ant (from repo root)
ant buildSusiMail
```