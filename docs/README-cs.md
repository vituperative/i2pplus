[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

Toto je zdrojový kód soft-forku Java implementace I2P.

Nejnovější vydání: https://i2pplus.github.io/

## Instalace

Pokyny k instalaci naleznete v [INSTALL.md](docs/INSTALL.md) nebo na https://i2pplus.github.io/

## Dokumentace

https://geti2p.net/how

FAQ: https://geti2p.net/faq

API: https://i2pplus.github.io/javadoc/
nebo spusťte 'ant javadoc' a poté začněte v build/javadoc/index.html

## Jak přispět / Hackovat na I2P

Prohlédněte si prosím [HACKING.md](docs/HACKING.md) a další dokumenty v adresáři docs.

## Sestavování balíčků ze zdrojového kódu

Chcete-li získat vývojovou větev ze správy zdrojového kódu: https://github.com/I2PPlus/i2pplus/

### Požadavky

- Java SDK (nejlépe Oracle/Sun nebo OpenJDK) 1.8.0 nebo vyšší
  - Některé subsystémy pro vestavěné systémy (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 nebo vyšší
- Nástroje xgettext, msgfmt a msgmerge nainstalované z balíčku GNU gettext
  http://www.gnu.org/software/gettext/
- Buildovací prostředí musí používat UTF-8 locale.

### Buildovací proces Ant

Na systémech x86 spusťte následující (bude sestaveno pomocí IzPack4):

    ant pkg

Na non-x86 systémech použijte místo toho jednu z následujících možností:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

Chcete-li sestavovat pomocí IzPack5, stáhněte z: http://izpack.org/downloads/ a poté
nainstalujte a poté spusťte následující příkaz(y):

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

Chcete-li sestavit nepodepsanou aktualizaci pro stávající instalaci, spusťte:

    ant updater

Pokud máte problémy se sestavením úplného instalátoru (Java14 a novější mohou generovat build chyby pro izpack týkající se pack200),
můžete sestavit úplný instalační zip, který lze rozbalit a spustit na místě:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Spusťte 'ant' bez argumentů pro zobrazení dalších možností sestavení.

### Docker

Další informace o spouštění I2P v Docker naleznete v [docker/README.md](docker/README.md)

## Kontaktní informace

Potřebujete pomoc? Navštivte IRC kanál #saltR v síti I2P IRC

Nahlašování chyb: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues nebo https://github.com/I2PPlus/i2pplus/issues

## Licence

I2P+ je licencováno pod AGPL v.3.

Pro licence různých subkomponent viz: [README.md](docs/licenses/README.md)

## Viz také

### Dokumentace

- [docs/README.md](docs/README.md) - Úplný rejstřík dokumentace
- [docs/INSTALL.md](docs/INSTALL.md) - Průvodce instalací
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Instalace bez grafického rozhraní (konzolový režim)
- [docs/HACKING.md](docs/HACKING.md) - Průvodce pro vývojáře a build systémy
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Rozložení zdrojového stromu a kde co najít
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Správa zákazů relací I2P pomocí nftables
- [docs/history.txt](docs/history.txt) - Úplný seznam změn

### README sub-projektů

- [apps/addressbook/README.md](apps/addressbook/README.md) - Aplikace adresář
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Desktopová GUI aplikace
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - I2P Control API
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - I2PSnark BitTorrent klient
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - Aplikace I2P Tunnel
- [apps/imagegen/README.md](apps/imagegen/README.md) - Nástroje pro generování obrázků
- [apps/jetty/README.md](apps/jetty/README.md) - Jetty HTTP server
- [apps/jrobin/README.md](apps/jrobin/README.md) - Monitorovací knihovna JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Minimální streaming knihovna
- [apps/pack200/README.md](apps/pack200/README.md) - Komprese Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Proxy skripty
- [apps/README.md](apps/README.md) - Přehled aplikací
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Router konzole
- [apps/sam/README.md](apps/sam/README.md) - Simple Anonymous Messaging
- [apps/streaming/README.md](apps/streaming/README.md) - Streaming knihovna
- [apps/susidns/README.md](apps/susidns/README.md) - DNS server
- [apps/susimail/README.md](apps/susimail/README.md) - I2P e-mail klient
- [apps/systray/README.md](apps/systray/README.md) - System tray aplikace
- [core/c/jbigi/docs/README.md](core/c/jbigi/docs/README.md) - Nativní BigInteger knihovna (GMP)
- [core/c/jcpuid/README.md](core/c/jcpuid/README.md) - Nativní CPU detekční knihovna
- [core/README.md](core/README.md) - Dokumentace core knihovny
- [docker/README.md](docker/README.md) - Spouštění I2P+ v Docker
- [docs/licenses/README.md](docs/licenses/README.md) - Licence třetích stran
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Nativní JNI knihovna pro kryptografii (GMP)
- [installer/resources/README.md](installer/resources/README.md) - Zahrnuté installer prostředky
- [scripts/README.md](scripts/README.md) - Utilitní skripty pro vývoj a správu
- [scripts/tests/README.md](scripts/tests/README.md) - Validační a testovací skripty