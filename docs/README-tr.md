[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

Bu, I2P'nin Java uygulamasının yazılım çatallamasının (soft-fork) kaynak kodudur.

En son sürüm: https://i2pplus.github.io/

## Kurulum

Kurulum talimatları için [INSTALL.md](docs/INSTALL.md) veya https://i2pplus.github.io/ adresine bakın.

## Belgeler

https://geti2p.net/how

SSS: https://geti2p.net/faq

API: https://i2pplus.github.io/javadoc/
veya 'ant javadoc' komutunu çalıştırın ve ardından build/javadoc/index.html adresinden başlayın

## Nasıl Katkıda Bulunulur / I2P Üzerinde Çalışılır

Lütfen [HACKING.md](docs/HACKING.md) dosyasını ve docs dizinindeki diğer belgeleri inceleyin.

## Kaynak Koddan Paket Oluşturma

Geliştirme dalını kaynak kontrolünden almak için: https://github.com/I2PPlus/i2pplus/

### Ön Koşullar

- Java SDK (tercihen Oracle/Sun veya OpenJDK) 1.8.0 veya üstü
- Apache Ant 1.9.8 veya üstü
- GNU gettext paketinden yüklenen xgettext, msgfmt ve msgmerge araçları
  http://www.gnu.org/software/gettext/
- Derleme ortamı UTF-8 yerel ayarını kullanmalıdır.
- Debian paketi derlemeleri için: `dpkg-deb` ve `fakeroot` paketleri (paket yöneticiniz aracılığıyla)

### Ant Derleme Süreci

x86 sistemlerinde aşağıdakini çalıştırın (IzPack4 kullanılarak derlenecektir):

    ant pkg

x86 dışı sistemlerde, bunun yerine aşağıdakilerden birini kullanın:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

IzPack5 ile derlemek istiyorsanız, şuradan indirin: http://izpack.org/downloads/
ardından kurun ve ardından aşağıdaki komut(lar)ı çalıştırın:

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

Mevcut bir kurulum için imzasız bir güncelleme oluşturmak için şunu çalıştırın:

    ant updater

Tam bir yükleyici oluştururken sorun yaşıyorsanız (Java14 ve sonraki sürümler pack200 ile ilgili izpack için derleme hataları oluşturabilir),
çıkarılıp yerinde çalıştırılabilecek tam bir kurulum zip'i oluşturabilirsiniz:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Diğer derleme seçeneklerini görmek için 'ant'ı argümansız çalıştırın.

Jetty/Tomcat harici bağımlılıkları olmadan Debian/Ubuntu için bağımsız bir Debian paketi oluşturmak için:
```bash
ant buildDeb
```

Bu, harici bağımlılıkları olmayan, paketlenmiş Jetty ve Tomcat kitaplıklarını içeren bağımsız bir `.deb` paketi oluşturur.


Linux için AppImage derlemek için:
```bash
ant buildAppImage
```

Ayrıntılar için [tools/appimage/README.md](tools/appimage/README.md) bölümüne bakın.


I2P'yi Docker'da çalıştırma hakkında daha fazla bilgi için [docker/README.md](docker/README.md) dosyasına bakın





## İletişim Bilgileri
Yardıma mı ihtiyacınız var? I2P IRC ağındaki #saltR IRC kanalını ziyaret edin

## İletişim Bilgileri

Hata raporları: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues veya https://github.com/I2PPlus/i2pplus/issues

## Lisanslar

I2P+, AGPL v.3 altında lisanslanmıştır.

Çeşitli alt bileşen lisansları için bkz: [README.md](docs/LICENSE.md)

## Ayrıca Bkz

### Belgeler

- [docs/README.md](docs/README.md) - Tam belge dizini
- [docs/INSTALL.md](docs/INSTALL.md) - Kurulum kılavuzu
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Headless (konsol modu) kurulum
- [docs/HACKING.md](docs/HACKING.md) - Geliştirici kılavuzu ve derleme sistemleri
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Kaynak ağacı düzeni
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - nftables ile I2P oturum yasaklarını yönetme
- [docs/LICENSE.md](docs/LICENSE.md) - Üçüncü taraf lisansları
- [docs/history.txt](docs/history.txt) - Tam değişiklik günlüğü

### Alt Projeler

- [apps/README.md](apps/README.md) - Uygulama genel bakış
- [apps/addressbook/README.md](apps/addressbook/README.md) - Adres defteri uygulaması
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Masaüstü GUI uygulaması
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - I2P Kontrol API
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - I2PSnark BitTorrent istemcisi
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - I2P Tünel uygulaması
- [apps/imagegen/README.md](apps/imagegen/README.md) - Görüntü oluşturma araçları
- [apps/jetty/README.md](apps/jetty/README.md) - Jetty HTTP sunucusu
- [apps/jrobin/README.md](apps/jrobin/README.md) - JRobin izleme kütüphanesi
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Minimal akış kütüphanesi
- [apps/pack200/README.md](apps/pack200/README.md) - Pack200 sıkıştırma
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Proxy betikleri
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Yönlendirici konsolu
- [apps/sam/README.md](apps/sam/README.md) - Basit Anonim Mesajlaşma
- [apps/streaming/README.md](apps/streaming/README.md) - Akış kütüphanesi
- [apps/susidns/README.md](apps/susidns/README.md) - DNS sunucusu
- [apps/susimail/README.md](apps/susimail/README.md) - I2P e-posta istemcisi
- [apps/systray/README.md](apps/systray/README.md) - Sistem tepsisi uygulaması
- [core/README.md](core/README.md) - Çekirdek kütüphane belgeleri
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Şifreleme için yerel JNI kütüphanesi (GMP)

### Çeşitli

- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - nftables ile I2P oturum yasaklarını yönetme
- [installer/resources/README.md](installer/resources/README.md) - Paketlenmiş yükleyici kaynakları
- [tools/scripts/README.md](tools/scripts/README.md) - Geliştirme ve yönetim için yardımcı betikler
- [tools/scripts/tests/README.md](tools/scripts/tests/README.md) - Doğrulama ve test betikleri



## İletişim Bilgileri
- [docker/README.md](docker/README.md) - Docker'da I2P+ çalıştırma

## İletişim Bilgileri
