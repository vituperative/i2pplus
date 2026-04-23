[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

To jest kod źródłowy miękkiego fork (soft-fork) implementacji Java I2P.

Najnowsze wydanie: https://i2pplus.github.io/

## Instalacja

Zobacz [INSTALL.md](docs/INSTALL.md) lub https://i2pplus.github.io/ dla instrukcji instalacji.

## Dokumentacja

https://geti2p.net/how

FAQ: https://geti2p.net/faq

API: http://docs.i2p-projekt.de/javadoc/
lub uruchom 'ant javadoc', a następnie zacznij od build/javadoc/index.html

## Jak przyczynić się / Programować na I2P

Proszę sprawdź [HACKING.md](docs/HACKING.md) i inne dokumenty w katalogu docs.

## Budowanie pakietów ze źródeł

Aby uzyskać gałąź deweloperską z kontroli źródła: https://gitlab.com/i2p.plus/I2P.Plus/

### Wymagania wstępne

- Java SDK (najlepiej Oracle/Sun lub OpenJDK) 1.8.0 lub wyższa
  - Systemy operacyjne nie-Linux i JVM: Zobacz https://trac.i2p2.de/wiki/java
  - Niektóre podsystemy dla wbudowanych (core, router, mstreaming, streaming, i2ptunnel)
    wymagają tylko Java 1.6
- Apache Ant 1.9.8 lub wyższy
- Narzędzia xgettext, msgfmt i msgmerge zainstalowane z pakietu GNU gettext
  http://www.gnu.org/software/gettext/
- Środowisko budowania musi używać lokalizacji UTF-8.

### Proces budowania z Ant

Na systemach x86 uruchom następujące (to zbuduje używając IzPack4):

    ant pkg

Na systemach nie-x86, użyj jednego z następujących zamiast tego:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

Jeśli chcesz budować z IzPack5, pobierz z: http://izpack.org/downloads/
następnie zainstaluj, a następnie uruchom następujące polecenie(a):

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

Aby zbudować niepodpisany update dla istniejącej instalacji, uruchom:

    ant updater

Jeśli masz problemy z budowaniem pełnego instalatora (Java14 i nowsze mogą generować błędy budowania dla izpack dotyczące pack200),
możesz zbudować pełny zip instalacyjny, który można rozpakować i uruchomić w miejscu:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Uruchom 'ant' bez argumentów, aby zobaczyć inne opcje budowania.

### Docker

Aby uzyskać więcej informacji o tym, jak uruchomić I2P w Docker, zobacz [docker/README.md](docker/README.md)

## Informacje kontaktowe

Potrzebujesz pomocy? Odwiedź kanał IRC #saltR w sieci IRC I2P

Raporty o błędach: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues lub https://github.com/I2PPlus/i2pplus/issues

## Licencje

I2P+ jest licencjonowany na podstawie AGPL w wersji 3.

W przypadku różnych subkomponentów zobacz: [LICENSE.txt](docs/licenses/LICENSE.txt)

## Zobacz również

### Dokumentacja

- [docs/README.md](docs/README.md) - Pełny indeks dokumentacji
- [docs/INSTALL.md](docs/INSTALL.md) - Przewodnik instalacji
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Instalacja bez GUI (tryb konsoli)
- [docs/HACKING.md](docs/HACKING.md) - Przewodnik dewelopera i systemy budowania
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Układ drzewa źródłowego i gdzie znaleźć rzeczy
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Zarządzanie blokadami sesji I2P za pomocą nftables
- [docs/history.txt](docs/history.txt) - Pełny dziennik zmian

### READMEs podprojektów

- [apps/addressbook/README.md](apps/addressbook/README.md) - Aplikacja książki adresowej
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Aplikacja GUI pulpitu
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - API sterowania I2P
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - Klient BitTorrent I2PSnark
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - Aplikacja tunelu I2P
- [apps/imagegen/README.md](apps/imagegen/README.md) - Narzędzia do generowania obrazów
- [apps/jetty/README.md](apps/jetty/README.md) - Serwer HTTP Jetty
- [apps/jrobin/README.md](apps/jrobin/README.md) - Biblioteka monitoringu JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Minimalna biblioteka strumieniowania
- [apps/pack200/README.md](apps/pack200/README.md) - Kompresja Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Skrypty proxy
- [apps/README.md](apps/README.md) - Przegląd aplikacji
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Konsola routera
- [apps/sam/README.md](apps/sam/README.md) - Proste anonimowe wiadomości
- [apps/streaming/README.md](apps/streaming/README.md) - Biblioteka strumieniowania
- [apps/susidns/README.md](apps/susidns/README.md) - Serwer DNS
- [apps/susimail/README.md](apps/susimail/README.md) - Klient poczty e-mail I2P
- [apps/systray/README.md](apps/systray/README.md) - Aplikacja zasobnika systemowego
- [core/c/jbigi/docs/README.md](core/c/jbigi/docs/README.md) - Natywna biblioteka BigInteger (GMP)
- [core/c/jcpuid/README.md](core/c/jcpuid/README.md) - Natywna biblioteka wykrywania CPU
- [core/README.md](core/README.md) - Dokumentacja biblioteki podstawowej
- [docker/README.md](docker/README.md) - Uruchamianie I2P+ w Docker
- [docs/licenses/README.md](docs/licenses/README.md) - Licencje stron trzecich
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Natywna biblioteka JNI do kryptografii (GMP)
- [installer/resources/README.md](installer/resources/README.md) - Zawarte zasoby instalatora
- [scripts/README.md](scripts/README.md) - Skrypty narzędziowe dla rozwoju i administracji
- [scripts/tests/README.md](scripts/tests/README.md) - Skrypty walidacji i testowania