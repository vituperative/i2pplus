[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

Это исходный код альтернативной реализации I2P на Java

Последний релиз: https://i2pplus.github.io/

## Установка

Смотрите [INSTALL.txt](INSTALL.txt) или https://i2pplus.github.io/ для инструкций по установке.

## Документация

https://geti2p.net/how

Часто задаваемые вопросы: https://geti2p.net/faq

API: https://i2pplus.github.io/javadoc/
или запустите «ant javadoc», затем откройте файл index.html в папке build/javadoc.

## Как внести вклад / Взломать I2P

Пожалуйста, ознакомьтесь с [HACKING.md](HACKING.md) и другими документами в каталоге docs.

## Сборка пакетов из исходного кода

Чтобы скачать I2P+ через Git: https://github.com/I2PPlus/i2pplus/

### Предварительные требования

- Java SDK (желательно Oracle/Sun или OpenJDK) 1.8.0 или выше
  - Некоторые подсистемы для встроенных (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 или выше
- Установлены инструменты xgettext, msgfmt и msgmerge из пакета GNU gettext
  http://www.gnu.org/software/gettext/
- Среда сборки должна использовать локаль UTF-8.

### Процесс сборки Ant

На системах x86 выполните следующее (это создаст сборку, используя IzPack4):

     ant pkg

на прочих системах используйте вместо этого одну из следующих команд:

     ant installer-linux
     ant installer-freebsd
     ant installer-osx
     ant installer-windows

Если вы хотите собрать с использованием IzPack5, загрузите его с сайта: http://izpack.org/downloads/, а затем
установите его, а затем выполните следующую команду (ы):

     ant installer5-linux
     ant installer5-freebsd
     ant installer5-osx
     ant installer5-windows

Чтобы создать не подписанное обновление для существующей установки, выполните:

     ant updater

Запустите 'ant' без аргументов, чтобы увидеть другие варианты сборки.

### Docker
Дополнительную информацию о запуске I2P в Docker см. [Docker.md](../docker/Docker.md).
## Контактная информация

Нужна помощь? Смотрите канал IRC #saltR в сети I2P IRC.

Сообщения об ошибках: https://github.com/I2PPlus/i2pplus/issues

## Лицензии

I2P+ лицензирована по AGPL v.3.

Для различных подкомпонентов лицензии см.: [README.md](docs/LICENSE.md)

## См. также

### Документация

- [docs/README.md](docs/README.md) - Полный индекс документации
- [docs/INSTALL.md](docs/INSTALL.md) - Руководство по установке
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Установка без GUI (консольный режим)
- [docs/HACKING.md](docs/HACKING.md) - Руководство разработчика и системы сборки
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Структура исходного кода
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Управление bans сессий I2P через nftables
- [docs/LICENSE.md](docs/LICENSE.md) - Лицензии третьих сторон
- [docs/history.txt](docs/history.txt) - Полный список изменений

### Sub-projects

- [apps/README.md](apps/README.md) - Обзор приложений
- [apps/addressbook/README.md](apps/addressbook/README.md) - Приложение адресной книги
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Приложение десктопного GUI
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - API управления I2P
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - BitTorrent клиент I2PSnark
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - Приложение туннеля I2P
- [apps/imagegen/README.md](apps/imagegen/README.md) - Инструменты генерации изображений
- [apps/jetty/README.md](apps/jetty/README.md) - HTTP-сервер Jetty
- [apps/jrobin/README.md](apps/jrobin/README.md) - Библиотека мониторинга JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Минимальная библиотека стриминга
- [apps/pack200/README.md](apps/pack200/README.md) - Сжатие Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Прокси-скрипты
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Консоль роутера
- [apps/sam/README.md](apps/sam/README.md) - Simple Anonymous Messaging
- [apps/streaming/README.md](apps/streaming/README.md) - Библиотека стриминга
- [apps/susidns/README.md](apps/susidns/README.md) - DNS-сервер
- [apps/susimail/README.md](apps/susimail/README.md) - Почтовый клиент I2P
- [apps/systray/README.md](apps/systray/README.md) - Приложение системного лотка
- [core/README.md](core/README.md) - Документация библиотеки core
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Нативная JNI-библиотека для криптографии (GMP)

### MISC

- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Управление bans сессий I2P через nftables
- [installer/resources/README.md](installer/resources/README.md) - Ресурсы установщика
- [tools/scripts/README.md](tools/scripts/README.md) - Утилитные скрипты для разработки и администрирования
- [tools/scripts/tests/README.md](tools/scripts/tests/README.md) - Скрипты проверки и тестирования

### Docker

- [docker/README.md](docker/README.md) - Запуск I2P+ в Docker