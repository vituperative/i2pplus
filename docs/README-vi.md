[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

Đây là mã nguồn cho bản fork mềm (soft-fork) của phiên bản Java implementation của I2P.

Phiên bản mới nhất: https://i2pplus.github.io/

## Cài đặt

Xem [INSTALL.md](docs/INSTALL.md) hoặc https://i2pplus.github.io/ để biết hướng dẫn cài đặt.

## Tài liệu

https://geti2p.net/how

Câu hỏi thường gặp: https://geti2p.net/faq

API: http://docs.i2p-projekt.de/javadoc/
hoặc chạy 'ant javadoc' rồi bắt đầu tại build/javadoc/index.html

## Cách đóng góp / Hack trên I2P

Vui lòng xem [HACKING.md](docs/HACKING.md) và các tài liệu khác trong thư mục docs.

## Xây dựng gói cài đặt từ mã nguồn

Để lấy nhánh phát triển từ source control: https://gitlab.com/i2p.plus/I2P.Plus/

### Yêu cầu trước khi cài đặt

- Java SDK (ưu tiên Oracle/Sun hoặc OpenJDK) 1.8.0 hoặc cao hơn
  - Hệ điều hành không phải Linux và JVM: Xem https://trac.i2p2.de/wiki/java
  - Một số subsystem cho embedded (core, router, mstreaming, streaming, i2ptunnel)
    chỉ cần Java 1.6
- Apache Ant 1.9.8 hoặc cao hơn
- Cài đặt các công cụ xgettext, msgfmt, và msgmerge từ GNU gettext package
  http://www.gnu.org/software/gettext/
- Môi trường build phải sử dụng UTF-8 locale.

### Quá trình build Ant

Trên hệ x86, chạy lệnh sau (sẽ build sử dụng IzPack4):

    ant pkg

Trên hệ không phải x86, sử dụng một trong các lệnh sau:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

Nếu bạn muốn build với IzPack5, tải về từ: http://izpack.org/downloads/ rồi cài đặt, sau đó chạy các lệnh sau:

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

Để build một bản cập nhật chưa được ký cho cài đặt hiện tại, chạy:

    ant updater

Nếu bạn gặp vấn đề khi build full installer (Java14 trở lên có thể gây ra lỗi build cho izpack liên quan đến pack200),
bạn có thể build file nén cài đặt đầy đủ có thể giải nén và chạy tại chỗ:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Chạy 'ant' không có đối số để xem các tùy chọn build khác.

### Docker

Để biết thêm thông tin về cách chạy I2P trong Docker, xem [docker/README.md](docker/README.md)

## Thông tin liên hệ

Cần giúp đỡ? Truy cập kênh IRC #saltR trên mạng I2P IRC

Báo lỗi: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues hoặc https://github.com/I2PPlus/i2pplus/issues

## Giấy phép

I2P+ được cấp phép theo AGPL v.3.

Để xem giấy phép của các thành phần phụ, xem: [LICENSE.txt](docs/licenses/LICENSE.txt)

## Xem thêm

### Tài liệu

- [docs/README.md](docs/README.md) - Chỉ mục tài liệu đầy đủ
- [docs/INSTALL.md](docs/INSTALL.md) - Hướng dẫn cài đặt
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Cài đặt headless (console mode)
- [docs/HACKING.md](docs/HACKING.md) - Hướng dẫn phát triển và hệ thống build
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Bố trí cây mã nguồn và nơi tìm thứ
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Quản lý lệnh cấm session I2P với nftables
- [docs/history.txt](docs/history.txt) - Lịch sử thay đổi đầy đủ

### README của các sub-project

- [apps/addressbook/README.md](apps/addressbook/README.md) - Ứng dụng addressbook
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Ứng dụng Desktop GUI
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - I2P Control API
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - I2PSnark BitTorrent client
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - Ứng dụng I2P Tunnel
- [apps/imagegen/README.md](apps/imagegen/README.md) - Công cụ tạo hình ảnh
- [apps/jetty/README.md](apps/jetty/README.md) - Jetty HTTP server
- [apps/jrobin/README.md](apps/jrobin/README.md) - Thư viện giám sát JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Thư viện streaming tối thiểu
- [apps/pack200/README.md](apps/pack200/README.md) - Nén Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Proxy scripts
- [apps/README.md](apps/README.md) - Tổng quan ứng dụng
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Router console
- [apps/sam/README.md](apps/sam/README.md) - Simple Anonymous Messaging
- [apps/streaming/README.md](apps/streaming/README.md) - Thư viện streaming
- [apps/susidns/README.md](apps/susidns/README.md) - DNS server
- [apps/susimail/README.md](apps/susimail/README.md) - I2P email client
- [apps/systray/README.md](apps/systray/README.md) - Ứng dụng system tray
- [core/c/jbigi/docs/README.md](core/c/jbigi/docs/README.md) - Thư viện BigInteger native (GMP)
- [core/c/jcpuid/README.md](core/c/jcpuid/README.md) - Thư viện native phát hiện CPU
- [core/README.md](core/README.md) - Tài liệu thư viện core
- [docker/README.md](docker/README.md) - Chạy I2P+ trong Docker
- [docs/licenses/README.md](docs/licenses/README.md) - Giấy phép third-party
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Thư viện JNI native cho mật mã học (GMP)
- [installer/resources/README.md](installer/resources/README.md) - Tài nguyên installer bundled
- [scripts/README.md](scripts/README.md) - Script tiện ích cho phát triển và quản trị
- [scripts/tests/README.md](scripts/tests/README.md) - Script xác thực và kiểm tra