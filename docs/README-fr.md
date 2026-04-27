[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

Ceci est le code source du fork logiciel de l'implémentation Java d'I2P.

Dernière version : https://i2pplus.github.io/

## Installation

Voir [INSTALL.md](docs/INSTALL.md) ou https://i2pplus.github.io/ pour les instructions d'installation.

## Documentation

https://geti2p.net/how

FAQ : https://geti2p.net/faq

API : https://i2pplus.github.io/javadoc/
ou exécutez 'ant javadoc' puis démarrez à build/javadoc/index.html

## Comment contribuer / Hacker sur I2P

Veuillez consulter [HACKING.md](docs/HACKING.md) et d'autres documents dans le répertoire docs.

## Construction des paquets à partir du源代码

Pour obtenir la branche de développement depuis le contrôle de code source : https://github.com/I2PPlus/i2pplus/

### Prérequis

- Java SDK (de préférence Oracle/Sun ou OpenJDK) 1.8.0 ou supérieur
  - Certains sous-systèmes pour embarqué (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 ou supérieur
- Les outils xgettext, msgfmt et msgmerge installés depuis le paquet GNU gettext
  http://www.gnu.org/software/gettext/
- L'environnement de construction doit utiliser une locale UTF-8.
- Pour les constructions de paquets Debian : paquets `dpkg-deb` et `fakeroot` (via votre gestionnaire de paquets)

### Processus de construction avec Ant

Sur les systèmes x86, exécutez ce qui suit (cela construira avec IzPack4) :

    ant pkg

Sur les systèmes non-x86, utilisez l'une des alternatives suivantes :

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

Si vous voulez construire avec IzPack5, téléchargez depuis : http://izpack.org/downloads/
puis installez-le, puis exécutez la ou les commandes suivantes :

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

Pour construire une mise à jour non signée pour une installation existante, exécutez :

    ant updater

Si vous avez des problèmes pour construire un installateur complet (Java14 et versions ultérieures peuvent générer des erreurs de construction pour izpack concernant pack200),
vous pouvez construire un zip d'installation complet qui peut être extrait et exécuté sur place :

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Exécutez 'ant' sans arguments pour voir les autres options de construction.

Pour créer un package Debian autonome pour Debian/Ubuntu sans dépendances externes à Jetty/Tomcat:
```bash
ant buildDeb
```

Cela crée un package `.deb` autonome qui inclut les bibliothèques Jetty et Tomcat groupées, sans aucune dépendance externe.


Pour créer un AppImage pour Linux :
```bash
ant buildAppImage
```

Voir [tools/appimage/README.md](tools/appimage/README.md) pour les détails.


Pour plus d'informations sur la façon d'exécuter I2P dans Docker, voir [docker/README.md](docker/README.md)


## Informations de contact

Besoin d'aide ? Visitez le canal IRC #saltR sur le réseau IRC I2P

Rapports de bugs : https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues ou https://github.com/I2PPlus/i2pplus/issues

## Licences

I2P+ est sous licence AGPL v.3.

Pour les licences des sous-composants, voir : [README.md](docs/LICENSE.md)

## Voir également

### Documentation

- [docs/README.md](docs/README.md) - Index complet de la documentation
- [docs/INSTALL.md](docs/INSTALL.md) - Guide d'installation
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Installation sans GUI (mode console)
- [docs/HACKING.md](docs/HACKING.md) - Guide du développeur et systèmes de build
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Structure de l'arborescence source et où trouver les choses
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Gestion des interdictions de session I2P avec nftables
- [docs/LICENSE.md](docs/LICENSE.md) - Licences tierces
- [docs/history.txt](docs/history.txt) - Journal des modifications complet

### Sub-projects

- [apps/README.md](apps/README.md) - Aperçu des applications
- [apps/addressbook/README.md](apps/addressbook/README.md) - Application carnet d'adresses
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Application GUI de bureau
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - API de contrôle I2P
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - Client BitTorrent I2PSnark
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - Application tunnel I2P
- [apps/imagegen/README.md](apps/imagegen/README.md) - Outils de génération d'images
- [apps/jetty/README.md](apps/jetty/README.md) - Serveur HTTP Jetty
- [apps/jrobin/README.md](apps/jrobin/README.md) - Bibliothèque de supervision JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Bibliothèque de streaming minimale
- [apps/pack200/README.md](apps/pack200/README.md) - Compression Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Scripts proxy
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Console routeur
- [apps/sam/README.md](apps/sam/README.md) - Simple Anonymous Messaging
- [apps/streaming/README.md](apps/streaming/README.md) - Bibliothèque de streaming
- [apps/susidns/README.md](apps/susidns/README.md) - Serveur DNS
- [apps/susimail/README.md](apps/susimail/README.md) - Client courriel I2P
- [apps/systray/README.md](apps/systray/README.md) - Application barre système
- [core/README.md](core/README.md) - Documentation de la bibliothèque core
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Bibliothèque JNI native pour la cryptographie (GMP)

### MISC

- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Gestion des interdictions de session I2P avec nftables
- [installer/resources/README.md](installer/resources/README.md) - Ressources du programme d'installation groupées
- [tools/scripts/README.md](tools/scripts/README.md) - Scripts utilitaires pour le développement et l'administration
- [tools/scripts/tests/README.md](tools/scripts/tests/README.md) - Scripts de validation et de test



- [docker/README.md](docker/README.md) - Exécuter I2P+ dans Docker

