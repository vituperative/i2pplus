[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

Ini adalah kode sumber untuk soft-fork dari implementasi Java I2P.

Rilis terbaru: https://i2pplus.github.io/

## Instalasi

Lihat [INSTALL.md](docs/INSTALL.md) atau https://i2pplus.github.io/ untuk petunjuk instalasi.

## Dokumentasi

https://geti2p.net/how

FAQ: https://geti2p.net/faq

API: https://i2pplus.github.io/javadoc/
atau jalankan 'ant javadoc' kemudian mulai di build/javadoc/index.html

## Cara Berkontribusi / Mengembangkan I2P

Silakan periksa [HACKING.md](docs/HACKING.md) dan dokumen lain di direktori docs.

## Membangun Paket dari Sumber

Untuk mendapatkan cabang pengembangan dari kontrol sumber: https://github.com/I2PPlus/i2pplus/

### Prasyarat

- Java SDK (lebih disukai Oracle/Sun atau OpenJDK) 1.8.0 atau lebih tinggi
  - Subsistem tertentu untuk embedded (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 atau lebih tinggi
- Alat xgettext, msgfmt, dan msgmerge terinstal dari paket GNU gettext
  http://www.gnu.org/software/gettext/
- Lingkungan build harus menggunakan locale UTF-8.

### Proses Build dengan Ant

Pada sistem x86 jalankan berikut ini (akan membangun menggunakan IzPack4):

    ant pkg

Pada non-x86, gunakan salah satu alternatif berikut:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

Jika Anda ingin membangun dengan IzPack5, unduh dari: http://izpack.org/downloads/
lalu instal, lalu jalankan perintah berikut:

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

Untuk membangun update unsigned untuk instalasi yang sudah ada, jalankan:

    ant updater

Jika Anda mengalami masalah dalam membangun installer lengkap (Java14 dan yang lebih baru mungkin menghasilkan kesalahan build untuk izpack terkait pack200),
Anda dapat membuat zip instalasi lengkap yang dapat diekstrak dan dijalankan di tempat:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Jalankan 'ant' tanpa argumen untuk melihat opsi build lainnya.

### Docker

Untuk informasi lebih lanjut tentang cara menjalankan I2P di Docker, lihat [docker/README.md](docker/README.md)

## Info Kontak

Butuh bantuan? Kunjungi channel IRC #saltR di jaringan IRC I2P

Laporan bug: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues atau https://github.com/I2PPlus/i2pplus/issues

## Lisensi

I2P+ dilisensikan di bawah AGPL v.3.

Untuk lisensi sub-komponen lainnya, lihat: [README.md](docs/LICENSE.md)

## Lihat Juga

### Dokumentasi

- [docs/README.md](docs/README.md) - Indeks dokumentasi lengkap
- [docs/INSTALL.md](docs/INSTALL.md) - Panduan instalasi
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Instalasi headless (mode konsol)
- [docs/HACKING.md](docs/HACKING.md) - Panduan pengembang dan sistem build
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Tata letak pohon sumber
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Mengelola pelarangan sesi I2P dengan nftables
- [docs/LICENSE.md](docs/LICENSE.md) - Lisensi pihak ketiga
- [docs/history.txt](docs/history.txt) - Log perubahan lengkap

### Sub-proyek

- [apps/README.md](apps/README.md) - Ikhtisar aplikasi
- [apps/addressbook/README.md](apps/addressbook/README.md) - Aplikasi buku alamat
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Aplikasi GUI desktop
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - API kontrol I2P
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - Klien BitTorrent I2PSnark
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - Aplikasi terowongan I2P
- [apps/imagegen/README.md](apps/imagegen/README.md) - Alat pembuatan gambar
- [apps/jetty/README.md](apps/jetty/README.md) - Server HTTP Jetty
- [apps/jrobin/README.md](apps/jrobin/README.md) - Pustaka pemantauan JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Pustaka streaming minimal
- [apps/pack200/README.md](apps/pack200/README.md) - Kompresi Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Skrip proxy
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Konsol router
- [apps/sam/README.md](apps/sam/README.md) - Simple Anonymous Messaging
- [apps/streaming/README.md](apps/streaming/README.md) - Pustaka streaming
- [apps/susidns/README.md](apps/susidns/README.md) - Server DNS
- [apps/susimail/README.md](apps/susimail/README.md) - Klien email I2P
- [apps/systray/README.md](apps/systray/README.md) - Aplikasi baki sistem
- [core/README.md](core/README.md) - Dokumentasi pustaka core
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Pustaka JNI native untuk kriptografi (GMP)

### Lainnya

- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Mengelola pelarangan sesi I2P dengan nftables
- [installer/resources/README.md](installer/resources/README.md) - Sumber daya installer
- [tools/scripts/README.md](tools/scripts/README.md) - Skrip utilitas untuk pengembangan dan administrasi
- [tools/scripts/tests/README.md](tools/scripts/tests/README.md) - Skrip validasi dan pengujian

### Docker

- [docker/README.md](docker/README.md) - Menjalankan I2P+ di Docker