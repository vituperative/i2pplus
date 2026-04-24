[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

Dies ist der Quellcode des Soft-Forks der Java-Implementierung von I2P.

Neueste Version: https://i2pplus.github.io/

## Installation

Siehe [INSTALL.md](docs/INSTALL.md) oder https://i2pplus.github.io/ für Installationsanweisungen.

## Dokumentation

https://geti2p.net/how

FAQ: https://geti2p.net/faq

API: https://i2pplus.github.io/javadoc/
oder führen Sie 'ant javadoc' aus und starten Sie dann bei build/javadoc/index.html

## Wie man beiträgt / Bei I2P entwickeln

Bitte überprüfen Sie [HACKING.md](docs/HACKING.md) und andere Dokumente im docs-Verzeichnis.

## Pakete aus dem Quellcode erstellen

Um den Entwicklungszweig aus der Quellcodeverwaltung zu erhalten: https://github.com/I2PPlus/i2pplus/

### Voraussetzungen

- Java SDK (vorzugsweise Oracle/Sun oder OpenJDK) 1.8.0 oder höher
  - Bestimmte Subsysteme für eingebettete Systeme (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 oder höher
- Die xgettext-, msgfmt- und msgmerge-Tools aus dem GNU gettext-Paket installiert
  http://www.gnu.org/software/gettext/
- Die Build-Umgebung muss ein UTF-8-Gebiet verwenden.

### Ant-Build-Prozess

Führen Sie auf x86-Systemen Folgendes aus (dies wird mit IzPack4 erstellt):

    ant pkg

Auf Nicht-x86-Systemen verwenden Sie stattdessen eine der folgenden Optionen:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

Wenn Sie mit IzPack5 erstellen möchten, laden Sie es herunter von: http://izpack.org/downloads/
installieren Sie es dann, und führen Sie dann den/die folgenden Befehl(e) aus:

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

Um ein unsigniertes Update für eine bestehende Installation zu erstellen, führen Sie aus:

    ant updater

Wenn Sie Probleme beim Erstellen eines vollständigen Installers haben (Java14 und später können Build-Fehler für izpack bezüglich pack200 erzeugen),
können Sie ein vollständiges Installations-zip erstellen, das extrahiert und an Ort und Stelle ausgeführt werden kann:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Führen Sie 'ant' ohne Argumente aus, um andere Build-Optionen zu sehen.

### Docker

Weitere Informationen zum Ausführen von I2P in Docker finden Sie unter [docker/README.md](docker/README.md)

## Kontaktinformationen

Brauchen Sie Hilfe? Besuchen Sie den IRC-Kanal #saltR im I2P-IRC-Netzwerk

Fehlerberichte: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues oder https://github.com/I2PPlus/i2pplus/issues

## Lizenzen

I2P+ ist unter AGPL v.3 lizenziert.

Für die verschiedenen Unterkomponentenlizenzen siehe: [README.md](docs/LICENSE.md)

## Siehe auch

### Dokumentation

- [docs/README.md](docs/README.md) - Vollständiger Dokumentationsindex
- [docs/INSTALL.md](docs/INSTALL.md) - Installationsanleitung
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Headless-Installation (Konsolenmodus)
- [docs/HACKING.md](docs/HACKING.md) - Entwicklerhandbuch und Build-Systeme
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Quellbaumlayout und wo etwas zu finden ist
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - I2P-Sitzungsverbote mit nftables verwalten
- [docs/LICENSE.md](docs/LICENSE.md) - Drittanbieterlizenzen
- [docs/history.txt](docs/history.txt) - Vollständiges Änderungsprotokoll

### Sub-projects

- [apps/README.md](apps/README.md) - Anwendungsübersicht
- [apps/addressbook/README.md](apps/addressbook/README.md) - Adressbuch-Anwendung
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Desktop-GUI-Anwendung
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - I2P Control API
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - I2PSnark BitTorrent-Client
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - I2P Tunnel-Anwendung
- [apps/imagegen/README.md](apps/imagegen/README.md) - Bildgenerierungswerkzeuge
- [apps/jetty/README.md](apps/jetty/README.md) - Jetty HTTP-Server
- [apps/jrobin/README.md](apps/jrobin/README.md) - JRobin-Überwachungsbibliothek
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Minimale Streaming-Bibliothek
- [apps/pack200/README.md](apps/pack200/README.md) - Pack200-Komprimierung
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Proxy-Skripte
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Router-Konsole
- [apps/sam/README.md](apps/sam/README.md) - Simple Anonymous Messaging
- [apps/streaming/README.md](apps/streaming/README.md) - Streaming-Bibliothek
- [apps/susidns/README.md](apps/susidns/README.md) - DNS-Server
- [apps/susimail/README.md](apps/susimail/README.md) - I2P-E-Mail-Client
- [apps/systray/README.md](apps/systray/README.md) - System-Tray-Anwendung
- [core/README.md](core/README.md) - Core-Bibliotheksdokumentation
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Native JNI-Bibliothek für Kryptographie (GMP)

### MISC

- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - I2P-Sitzungsverbote mit nftables verwalten
- [installer/resources/README.md](installer/resources/README.md) - Gebündelte Installer-Ressourcen
- [tools/scripts/README.md](tools/scripts/README.md) - Hilfsskripte für Entwicklung und Verwaltung
- [tools/scripts/tests/README.md](tools/scripts/tests/README.md) - Validierungs- und Testskripte

### Docker

- [docker/README.md](docker/README.md) - I2P+ in Docker ausführen