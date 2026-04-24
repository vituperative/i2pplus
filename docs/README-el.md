[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

Αυτός είναι ο πηγαίος κώδικας του soft-fork της Java υλοποίησης του I2P.

Τελευταία έκδοση: https://i2pplus.github.io/

## Εγκατάσταση

Δείτε τις οδηγίες εγκατάστασης στο [INSTALL.md](docs/INSTALL.md) ή στο https://i2pplus.github.io/

## Τεκμηρίωση

https://geti2p.net/how

Συχνές ερωτήσεις: https://geti2p.net/faq

API: https://i2pplus.github.io/javadoc/
ή εκτελέστε 'ant javadoc' και μετά ξεκινήστε από build/javadoc/index.html

## Πώς να συνεισφέρετε / Να χακάρετε το I2P

Παρακαλώ ελέγξτε το [HACKING.md](docs/HACKING.md) και άλλα έγγραφα στον κατάλογο docs.

## Δημιουργία πακέτων από τον πηγαίο κώδικα

Για να αποκτήσετε το branch ανάπτυξης από τον έλεγχο πηγαίου κώδικα: https://github.com/I2PPlus/i2pplus/

### Προϋποθέσεις

- Java SDK (κατά προτίμηση Oracle/Sun ή OpenJDK) 1.8.0 ή νεότερο
  - Ορισμένα υποσυστήματα για embedded (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 ή νεότερο
- Εγκατεστημένα εργαλεία xgettext, msgfmt, και msgmerge από το πακέτο GNU gettext
  http://www.gnu.org/software/gettext/
- Το περιβάλλον build πρέπει να χρησιμοποιεί UTF-8 locale.

### Διαδικασία build με Ant

Σε συστήματα x86 εκτελέστε το ακόλουθο (θα γίνει build χρησιμοποιώντας IzPack4):

    ant pkg

Σε μη-x86, χρησιμοποιήστε ένα από τα ακόλουθα αντί:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

Αν θέλετε να κάνετε build με IzPack5, κατεβάστε από: http://izpack.org/downloads/ και μετά
εγκαταστήστε το, και μετά εκτελέστε την ακόλουθα εντολή(ες):

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

Για να δημιουργήσετε μια μη υπογεγραμμένη ενημέρωση για μια υπάρχουσα εγκατάσταση, εκτελέστε:

    ant updater

Αν αντιμετωπίζετε προβλήματα με τη δημιουργία ενός πλήρους installer (Java14 και νεότερα μπορεί να δημιουργήσουν σφάλματα build για izpack σχετικά με το pack200),
μπορείτε να δημιουργήσετε ένα πλήρες εγκατάστασης zip που μπορεί να εξαχθεί και να εκτελεστεί επιτόπου:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Εκτελέστε 'ant' χωρίς ορίσματα για να δείτε άλλες επιλογές build.

### Docker

Για περισσότερες πληροφορίες σχετικά με την εκτέλεση του I2P σε Docker, δείτε το [docker/README.md](docker/README.md)

## Στοιχεία επικοινωνίας

Χρειάζεστε βοήθεια; Επισκεφθείτε το κανάλι IRC #saltR στο δίκτυο I2P IRC

Αναφορές σφαλμάτων: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues ή https://github.com/I2PPlus/i2pplus/issues

## Άδειες

Το I2P+ έχει άδεια χρήσης υπό την AGPL v.3.

Για τις άδειες των διάφορων υποσυστατικών, δείτε: [README.md](docs/licenses/README.md)

## Δείτε επίσης

### Τεκμηρίωση

- [docs/README.md](docs/README.md) - Πλήρες ευρετήριο τεκμηρίωσης
- [docs/INSTALL.md](docs/INSTALL.md) - Οδηγός εγκατάστασης
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Εγκατάσταση χωρίς γραφικό περιβάλλον (λειτουργία κονσόλας)
- [docs/HACKING.md](docs/HACKING.md) - Οδηγός προγραμματιστή και συστήματα build
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Δομή δέντρου πηγαίου κώδικα και πού να βρείτε πράγματα
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Διαχείριση απαγορεύσεων συνεδριών I2P με nftables
- [docs/history.txt](docs/history.txt) - Πλήρες ιστορικό αλλαγών

### README υπο-έργων

- [apps/addressbook/README.md](apps/addressbook/README.md) - Εφαρμογή βιβλίου διευθύνσεων
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Εφαρμογή Desktop GUI
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - I2P Control API
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - I2PSnark BitTorrent πελάτης
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - Εφαρμογή I2P Tunnel
- [apps/imagegen/README.md](apps/imagegen/README.md) - Εργαλεία δημιουργίας εικόνων
- [apps/jetty/README.md](apps/jetty/README.md) - Jetty HTTP server
- [apps/jrobin/README.md](apps/jrobin/README.md) - Βιβλιοθήκη παρακολούθησης JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Ελάχιστη βιβλιοθήκη streaming
- [apps/pack200/README.md](apps/pack200/README.md) - Συμπίεση Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Proxy scripts
- [apps/README.md](apps/README.md) - Επισκόπηση εφαρμογών
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Κονσόλα router
- [apps/sam/README.md](apps/sam/README.md) - Απλή ανώνυμη ανταλλαγή μηνυμάτων
- [apps/streaming/README.md](apps/streaming/README.md) - Βιβλιοθήκη streaming
- [apps/susidns/README.md](apps/susidns/README.md) - DNS server
- [apps/susimail/README.md](apps/susimail/README.md) - I2P πελάτης email
- [apps/systray/README.md](apps/systray/README.md) - Εφαρμογή system tray
- [core/c/jbigi/docs/README.md](core/c/jbigi/docs/README.md) - Native BigInteger βιβλιοθήκη (GMP)
- [core/c/jcpuid/README.md](core/c/jcpuid/README.md) - Native βιβλιοθήκη ανίχνευσης CPU
- [core/README.md](core/README.md) - Τεκμηρίωση βιβλιοθήκης core
- [docker/README.md](docker/README.md) - Εκτέλεση I2P+ σε Docker
- [docs/licenses/README.md](docs/licenses/README.md) - Άδειες τρίτων μερών
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Native JNI βιβλιοθήκη για κρυπτογράφηση (GMP)
- [installer/resources/README.md](installer/resources/README.md) - Πόροι εγκατάστασης
- [scripts/README.md](scripts/README.md) - Βοηθητικά scripts για ανάπτυξη και διαχείριση
- [scripts/tests/README.md](scripts/tests/README.md) - Scripts επικύρωσης και δοκιμής