[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

Este es el código fuente del fork blando (soft-fork) de la implementación en Java de I2P.

Última versión: https://i2pplus.github.io/

## Instalación

Consulta [INSTALL.md](docs/INSTALL.md) o https://i2pplus.github.io/ para instrucciones de instalación.

## Documentación

https://geti2p.net/how

Preguntas frecuentes: https://geti2p.net/faq

API: https://i2pplus.github.io/javadoc/
o ejecuta 'ant javadoc' y luego inicia en build/javadoc/index.html

## Cómo contribuir / Programar en I2P

Por favor revisa [HACKING.md](docs/HACKING.md) y otros documentos en el directorio docs.

## Construir paquetes desde el código fuente

Para obtener la rama de desarrollo del control de código fuente: https://github.com/I2PPlus/i2pplus/

### Requisitos previos

- Java SDK (preferiblemente Oracle/Sun u OpenJDK) 1.8.0 o superior
  - Ciertos subsistemas para embebido (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 o superior
- Las herramientas xgettext, msgfmt y msgmerge instaladas del paquete GNU gettext
  http://www.gnu.org/software/gettext/
- El entorno de construcción debe usar una configuración regional UTF-8.

### Proceso de construcción con Ant

En sistemas x86 ejecuta lo siguiente (esto construirá usando IzPack4):

    ant pkg

En sistemas no x86, usa uno de los siguientes en su lugar:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

Si quieres construir con IzPack5, descarga desde: http://izpack.org/downloads/
y luego instálalo, y luego ejecuta el siguiente comando(s):

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

Para construir una actualización sin firmar para una instalación existente, ejecuta:

    ant updater

Si tienes problemas al construir un instalador completo (Java14 y posteriores pueden generar errores de construcción para izpack relacionados con pack200),
puedes construir un zip de instalación completa que puede ser extraído y ejecutado in situ:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Ejecuta 'ant' sin argumentos para ver otras opciones de construcción.

### Docker

Para más información sobre cómo ejecutar I2P en Docker, consulta [docker/README.md](docker/README.md)

## Información de contacto

¿Necesitas ayuda? Visita el canal IRC #saltR en la red IRC de I2P

Informes de errores: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues o https://github.com/I2PPlus/i2pplus/issues

## Licencias

I2P+ está licenciado bajo AGPL v.3.

Para las licencias de los subcomponentes, ver: [LICENSE.txt](docs/licenses/LICENSE.txt)

## Ver también

### Documentación

- [docs/README.md](docs/README.md) - Índice de documentación completo
- [docs/INSTALL.md](docs/INSTALL.md) - Guía de instalación
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Instalación sin GUI (modo consola)
- [docs/HACKING.md](docs/HACKING.md) - Guía para desarrolladores y sistemas de construcción
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Diseño del árbol de código fuente y dónde encontrar cosas
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Gestión de prohibiciones de sesiones I2P con nftables
- [docs/history.txt](docs/history.txt) - Registro de cambios completo

### READMEs de subproyectos

- [apps/addressbook/README.md](apps/addressbook/README.md) - Aplicación de libreta de direcciones
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Aplicación de GUI de escritorio
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - API de control de I2P
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - Cliente BitTorrent I2PSnark
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - Aplicación de túnel I2P
- [apps/imagegen/README.md](apps/imagegen/README.md) - Herramientas de generación de imágenes
- [apps/jetty/README.md](apps/jetty/README.md) - Servidor HTTP Jetty
- [apps/jrobin/README.md](apps/jrobin/README.md) - Biblioteca de monitoreo JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Biblioteca de streaming mínima
- [apps/pack200/README.md](apps/pack200/README.md) - Compresión Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Scripts de proxy
- [apps/README.md](apps/README.md) - Resumen de aplicaciones
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Consola del router
- [apps/sam/README.md](apps/sam/README.md) - Mensajería anónima simple
- [apps/streaming/README.md](apps/streaming/README.md) - Biblioteca de streaming
- [apps/susidns/README.md](apps/susidns/README.md) - Servidor DNS
- [apps/susimail/README.md](apps/susimail/README.md) - Cliente de correo I2P
- [apps/systray/README.md](apps/systray/README.md) - Aplicación de bandeja del sistema
- [core/c/jbigi/docs/README.md](core/c/jbigi/docs/README.md) - Biblioteca nativa BigInteger (GMP)
- [core/c/jcpuid/README.md](core/c/jcpuid/README.md) - Biblioteca nativa de detección de CPU
- [core/README.md](core/README.md) - Documentación de la biblioteca central
- [docker/README.md](docker/README.md) - Ejecutando I2P+ en Docker
- [docs/licenses/README.md](docs/licenses/README.md) - Licencias de terceros
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Biblioteca JNI nativa para criptografía (GMP)
- [installer/resources/README.md](installer/resources/README.md) - Recursos del instalador incluidos
- [scripts/README.md](scripts/README.md) - Scripts de utilidad para desarrollo y administración
- [scripts/tests/README.md](scripts/tests/README.md) - Scripts de validación y prueba