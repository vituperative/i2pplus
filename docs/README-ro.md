[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

Acesta este codul sursă pentru fork-ul soft al implementării Java a I2P.

Ultima versiune: https://i2pplus.github.io/

## Instalare

Consultați [INSTALL.md](docs/INSTALL.md) sau https://i2pplus.github.io/ pentru instrucțiuni de instalare.

## Documentație

https://geti2p.net/how

Întrebări frecvente: https://geti2p.net/faq

API: https://i2pplus.github.io/javadoc/
sau rulați 'ant javadoc' și apoi începeți de la build/javadoc/index.html

## Cum să contribuiți / Hacker pe I2P

Vă rugăm să consultați [HACKING.md](docs/HACKING.md) și alte documente din directorul docs.

## Construirea pachetelor din codul sursă

Pentru a obține branch-ul de dezvoltare din controlul sursă: https://github.com/I2PPlus/i2pplus/

### Cerințe prealabile

- Java SDK (de preferință Oracle/Sun sau OpenJDK) 1.8.0 sau mai mare
  - Anumite sub sisteme pentru embedded (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 sau mai mare
- Instrumente xgettext, msgfmt, și msgmerge instalate din pachetul GNU gettext
  http://www.gnu.org/software/gettext/
- Mediul de build trebuie să folosească o locație UTF-8.

### Procesul de build Ant

Pe sistemele x86 rulați următorul (va construi folosind IzPack4):

    ant pkg

Pe sistemele non-x86, folosiți una dintre următoarele în schimb:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

Dacă doriți să construiți cu IzPack5, descărcați de la: http://izpack.org/downloads/ și apoi
instalați-l, apoi rulați următoarea(e) comandă(e):

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

Pentru a construi o actualizare nesemnată pentru o instalare existentă, rulați:

    ant updater

Dacă aveți probleme la construirea unui installer complet (Java14 și versiunile ulterioare pot genera erori de build pentru izpack legate de pack200),
puteți construi un zip de instalare complet care poate fi extras și executat pe loc:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Rulați 'ant' fără argumente pentru a vedea alte opțiuni de build.

### Docker

Pentru mai multe informații despre cum să rulați I2P în Docker, consultați [docker/README.md](docker/README.md)

## Informații de contact

Aveți nevoie de ajutor? Vizitați canalul IRC #saltR pe rețeaua I2P IRC

Rapoarte de erori: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues sau https://github.com/I2PPlus/i2pplus/issues

## Licențe

I2P+ este licențiat sub AGPL v.3.

Pentru licențele diferitelor sub-componente, consultați: [LICENSE.txt](docs/licenses/LICENSE.txt)

## Vezi și

### Documentație

- [docs/README.md](docs/README.md) - Index complet documentație
- [docs/INSTALL.md](docs/INSTALL.md) - Ghid de instalare
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Instalare headless (mod consolă)
- [docs/HACKING.md](docs/HACKING.md) - Ghid pentru dezvoltatori și sisteme de build
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Structura arborelui sursă și unde să găsiți lucrurile
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Gestionarea interdicțiilor de sesiune I2P cu nftables
- [docs/history.txt](docs/history.txt) - Jurnal complet de modificări

### README-uri sub-proiecte

- [apps/addressbook/README.md](apps/addressbook/README.md) - Aplicație agendă
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Aplicație GUI desktop
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - I2P Control API
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - Client I2PSnark BitTorrent
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - Aplicație I2P Tunnel
- [apps/imagegen/README.md](apps/imagegen/README.md) - Instrumente de generare imagini
- [apps/jetty/README.md](apps/jetty/README.md) - Server HTTP Jetty
- [apps/jrobin/README.md](apps/jrobin/README.md) - Biblioteca de monitorizare JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Biblioteca minimală de streaming
- [apps/pack200/README.md](apps/pack200/README.md) - Compresie Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Script-uri proxy
- [apps/README.md](apps/README.md) - Prezentare generală aplicații
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Consolă router
- [apps/sam/README.md](apps/sam/README.md) - Mesagerie anonimă simplă
- [apps/streaming/README.md](apps/streaming/README.md) - Biblioteca de streaming
- [apps/susidns/README.md](apps/susidns/README.md) - Server DNS
- [apps/susimail/README.md](apps/susimail/README.md) - Client email I2P
- [apps/systray/README.md](apps/systray/README.md) - Aplicație system tray
- [core/c/jbigi/docs/README.md](core/c/jbigi/docs/README.md) - Biblioteca nativă BigInteger (GMP)
- [core/c/jcpuid/README.md](core/c/jcpuid/README.md) - Biblioteca nativă detectare CPU
- [core/README.md](core/README.md) - Documentație bibliotecă core
- [docker/README.md](docker/README.md) - Rulare I2P+ în Docker
- [docs/licenses/README.md](docs/licenses/README.md) - Licențe terțe
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Biblioteca JNI nativă pentru criptografie (GMP)
- [installer/resources/README.md](installer/resources/README.md) - Resurse installer incluse
- [scripts/README.md](scripts/README.md) - Script-uri utilitare pentru dezvoltare și administrare
- [scripts/tests/README.md](scripts/tests/README.md) - Script-uri de validare și testare