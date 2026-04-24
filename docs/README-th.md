[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

นี่คือซอร์สโค้ดของ soft-fork ของ Java implementation ของ I2P

รุ่นล่าสุด: https://i2pplus.github.io/

## การติดตั้ง

ดู [INSTALL.md](docs/INSTALL.md) หรือ https://i2pplus.github.io/ สำหรับคำแนะนำการติดตั้ง

## เอกสาร

https://geti2p.net/how

FAQ: https://geti2p.net/faq

API: https://i2pplus.github.io/javadoc/
หรือรัน 'ant javadoc' แล้วเริ่มต้นที่ build/javadoc/index.html

## วิธีมีส่วนร่วม / Hack บน I2P

กรุณาดู [HACKING.md](docs/HACKING.md) และเอกสารอื่นๆ ในไดเรกเทอรี docs

## การสร้างแพ็กเกจจากซอร์สโค้ด

เพื่อรับ development branch จาก source control: https://github.com/I2PPlus/i2pplus/

### ข้อกำหนดเบื้องต้น

- Java SDK (preferably Oracle/Sun or OpenJDK) 1.8.0 หรือสูงกว่า
  - บาง subsystem สำหรับ embedded (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 หรือสูงกว่า
- ติดตั้งเครื่องมือ xgettext, msgfmt, และ msgmerge จาก GNU gettext package
  http://www.gnu.org/software/gettext/
- สภาพแวดล้อมการ build ต้องใช้ UTF-8 locale

### กระบวนการ build ด้วย Ant

บนระบบ x86 ให้รันคำสั่งต่อไปนี้ (จะ build โดยใช้ IzPack4):

    ant pkg

บนระบบที่ไม่ใช่ x86 ให้ใช้คำสั่งใดคำสั่งหนึ่งต่อไปนี้แทน:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

ถ้าต้องการ build ด้วย IzPack5 ให้ดาวน์โหลดจาก: http://izpack.org/downloads/ แล้��ติดตั้ง จากนั้นรันคำสั่งต่อไปนี้:

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

เพื่อสร้าง unsigned update สำหรับการติดตั้งที่มีอยู่ ให้รัน:

    ant updater

ถ้ามีปัญหาในการ build installer แบบเต็ม (Java14 ขึ้นไปอาจสร้าง build errors สำหรับ izpack เกี่ยวกับ pack200)
สามารถ build การติดตั้งแบบ zip ที่สามารถแตกไฟล์และรันในสถานที่ได้:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

รัน 'ant' โดยไม่มีอาร์กิวเมนต์เพื่อดูตัวเลือกการ build อื่นๆ

### Docker

สำหรับข้อมูลเพิ่มเติมเกี่ยวกับวิธีรัน I2P ใน Docker ให้ดู [docker/README.md](docker/README.md)

## ข้อมูลติดต่อ

ต้องการความช่วยเหลือ? เยี่ยมชมช่อง IRC #saltR บนเครือข่าย I2P IRC

รายงานข้อผิดพลาด: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues หรือ https://github.com/I2PPlus/i2pplus/issues

## สิทธิ์อนุญาต

I2P+ ได้รับสิทธิ์อนุญาตภายใต้ AGPL v.3

สำหรับสิทธิ์อนุญาตของ sub-component ต่างๆ ดู: [LICENSE.txt](docs/licenses/LICENSE.txt)

## ดูเพิ่มเติม

### เอกสาร

- [docs/README.md](docs/README.md) - ดัชนีเอกสารฉบับเต็ม
- [docs/INSTALL.md](docs/INSTALL.md) - คู่มือการติดตั้ง
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - การติดตั้งแบบ headless (console mode)
- [docs/HACKING.md](docs/HACKING.md) - คู่มือนักพัฒนาและระบบ build
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - โครงสร้างต้นไม้ซอร์สโค้ดและที่ที่จะหาสิ่งต่างๆ
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - การจัดการ I2P session bans ด้วย nftables
- [docs/history.txt](docs/history.txt) - บันทึกการเปลี่ยนแปลงฉบับเต็ม

### README ของ sub-project

- [apps/addressbook/README.md](apps/addressbook/README.md) - แอปพลิเคชัน addressbook
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - แอปพลิเคชัน Desktop GUI
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - I2P Control API
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - I2PSnark BitTorrent client
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - แอปพลิเคชัน I2P Tunnel
- [apps/imagegen/README.md](apps/imagegen/README.md) - เครื่องมือสร้างภาพ
- [apps/jetty/README.md](apps/jetty/README.md) - Jetty HTTP server
- [apps/jrobin/README.md](apps/jrobin/README.md) - คลังการตรวจสอบ JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - คลัง streaming แบบ minimal
- [apps/pack200/README.md](apps/pack200/README.md) - การบีบอัด Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Proxy scripts
- [apps/README.md](apps/README.md) - ภาพรวมแอปพลิเคชัน
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Router console
- [apps/sam/README.md](apps/sam/README.md) - Simple Anonymous Messaging
- [apps/streaming/README.md](apps/streaming/README.md) - คลัง streaming
- [apps/susidns/README.md](apps/susidns/README.md) - DNS server
- [apps/susimail/README.md](apps/susimail/README.md) - I2P email client
- [apps/systray/README.md](apps/systray/README.md) - แอปพลิเคชัน system tray
- [core/c/jbigi/docs/README.md](core/c/jbigi/docs/README.md) - คลัง BigInteger native (GMP)
- [core/c/jcpuid/README.md](core/c/jcpuid/README.md) - คลัง native ตรวจจับ CPU
- [core/README.md](core/README.md) - เอกสารคลัง core
- [docker/README.md](docker/README.md) - รัน I2P+ ใน Docker
- [docs/licenses/README.md](docs/licenses/README.md) - สิทธิ์อนุญาต third-party
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - คลัง JNI native สำหรับ cryptography (GMP)
- [installer/resources/README.md](installer/resources/README.md) - ทรัพยากร installer ที่รวมอยู่
- [scripts/README.md](scripts/README.md) - scripts ยูทิลิตี้สำหรับการพัฒนาและการดูแลระบบ
- [scripts/tests/README.md](scripts/tests/README.md) - scripts การตรวจสอบและการทดสอบ