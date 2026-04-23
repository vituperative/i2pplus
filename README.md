[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[Русский](docs/README-ru.md) | [日本語](docs/README-ja.md) | [中文](docs/README-zh.md) | [हिन्दी](docs/README-hi.md) | [བོད་ཡིག](docs/README-bo.md) | [فارسی](docs/README-fa.md) | [العربية](docs/README-ar.md) | [Español](docs/README-es.md) | [Français](docs/README-fr.md) | [Deutsch](docs/README-de.md) | [Türkçe](docs/README-tr.md) | [Bahasa Indonesia](docs/README-id.md) | [Українська](docs/README-uk.md) | [Português](docs/README-pt.md) | [Polski](docs/README-pl.md) | [한국어](docs/README-ko.md) | [Tiếng Việt](docs/README-vi.md) | [ภาษาไทย](docs/README-th.md) | [اردو](docs/README-ur.md) | [עברית](docs/README-he.md) | [Italiano](docs/README-it.md) | [Nederlands](docs/README-nl.md) | [Română](docs/README-ro.md) | [Čeština](docs/README-cs.md) | [Magyar](docs/README-hu.md) | [Ελληνικά](docs/README-el.md)

This is the source code for the soft-fork of the Java implementation of I2P.

Latest release: https://i2pplus.github.io/

## Installing

See [INSTALL.md](docs/INSTALL.md) or https://i2pplus.github.io/ for installation instructions.

## Documentation

https://geti2p.net/how

FAQ: https://geti2p.net/faq

API: http://docs.i2p-projekt.de/javadoc/
or run 'ant javadoc' then start at build/javadoc/index.html

## How to contribute / Hack on I2P

Please check out [HACKING.md](docs/HACKING.md) and other documents in the docs directory.

## Building packages from source

To get development branch from source control: https://gitlab.com/i2p.plus/I2P.Plus/

### Prerequisites

- Java SDK (preferably Oracle/Sun or OpenJDK) 1.8.0 or higher
  - Non-linux operating systems and JVMs: See https://trac.i2p2.de/wiki/java
  - Certain subsystems for embedded (core, router, mstreaming, streaming, i2ptunnel)
    require only Java 1.6
- Apache Ant 1.9.8 or higher
- The xgettext, msgfmt, and msgmerge tools installed from the GNU gettext package
  http://www.gnu.org/software/gettext/
- Build environment must use a UTF-8 locale.

### Ant build process

On x86 systems run the following (this will build using IzPack4):

    ant pkg

On non-x86, use one of the following instead:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

If you want to build with IzPack5, download from: http://izpack.org/downloads/ and then
install it, and then run the following command(s):

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

To build an unsigned update for an existing installation, run:

    ant updater

If you have issues building a full installer (Java14 and later may generate build errors for izpack relating to pack200),
you can build a full installation zip which can be extracted and run in situ:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Run 'ant' with no arguments to see other build options.

### Docker

For more information how to run I2P in Docker, see [docker/README.md](docker/README.md)

## Contact info

Need help? Visit the IRC channel #saltR on the I2P IRC network

Bug reports: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues or https://github.com/I2PPlus/i2pplus/issues

## Licenses

I2P+ is licensed under the AGPL v.3.

For the various sub-component licenses, see: [LICENSE.txt](docs/licenses/LICENSE.txt)

## See also

### Documentation

- [docs/README.md](docs/README.md) - Full documentation index
- [docs/INSTALL.md](docs/INSTALL.md) - Installation guide
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Headless (console mode) installation
- [docs/HACKING.md](docs/HACKING.md) - Developer guide and build systems
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Source tree layout and where to find things
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Managing I2P session bans with nftables
- [docs/history.txt](docs/history.txt) - Full changelog

### Sub-project READMEs

- [apps/addressbook/README.md](apps/addressbook/README.md) - Addressbook application
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Desktop GUI application
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - I2P Control API
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - I2PSnark BitTorrent client
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - I2P Tunnel application
- [apps/imagegen/README.md](apps/imagegen/README.md) - Image generation tools
- [apps/jetty/README.md](apps/jetty/README.md) - Jetty HTTP server
- [apps/jrobin/README.md](apps/jrobin/README.md) - JRobin monitoring library
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Minimal streaming library
- [apps/pack200/README.md](apps/pack200/README.md) - Pack200 compression
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Proxy scripts
- [apps/README.md](apps/README.md) - Application overview
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Router console
- [apps/sam/README.md](apps/sam/README.md) - Simple Anonymous Messaging
- [apps/streaming/README.md](apps/streaming/README.md) - Streaming library
- [apps/susidns/README.md](apps/susidns/README.md) - DNS server
- [apps/susimail/README.md](apps/susimail/README.md) - I2P email client
- [apps/systray/README.md](apps/systray/README.md) - System tray application
- [core/c/jbigi/docs/README.md](core/c/jbigi/docs/README.md) - Native BigInteger library (GMP)
- [core/c/jcpuid/README.md](core/c/jcpuid/README.md) - CPU detection native library
- [core/README.md](core/README.md) - Core library documentation
- [docker/README.md](docker/README.md) - Running I2P+ in Docker
- [docs/licenses/README.md](docs/licenses/README.md) - Third-party licenses
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Native JNI library for cryptography (GMP)
- [installer/resources/README.md](installer/resources/README.md) - Bundled installer resources
- [scripts/README.md](scripts/README.md) - Utility scripts for development and administration
- [scripts/tests/README.md](scripts/tests/README.md) - Validation and testing scripts
