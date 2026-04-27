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
- Για δημιουργία πακέτων Debian: πακέτα `dpkg-deb` και `fakeroot` (μέσω του διαχειριστή πακέτων σας)

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



Για περισσότερες πληροφορίες σχετικά με την εκτέλεση του I2P σε Docker, δείτε το [docker/README.md](docker/README.md)





## Στοιχεία επικοινωνίας
Χρειάζεστε βοήθεια; Επισκεφθείτε το κανάλι IRC #saltR στο δίκτυο I2P IRC

## Στοιχεία επικοινωνίας


## Στοιχεία επικοινωνίας
Αναφορές σφαλμάτων: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues ή https://github.com/I2PPlus/i2pplus/issues

## Στοιχεία επικοινωνίας


## Στοιχεία επικοινωνίας


## Στοιχεία επικοινωνίας
## Άδειες
Το I2P+ έχει άδεια χρήσης υπό την AGPL v.3.

## Στοιχεία επικοινωνίας
## Άδειες


## Στοιχεία επικοινωνίας
## Άδειες
Για τις άδειες των διάφορων υποσυστατικών, δείτε: [README.md](docs/LICENSE.md)

## Στοιχεία επικοινωνίας
## Άδειες


## Στοιχεία επικοινωνίας
## Άδειες


## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [docs/README.md](docs/README.md) - Πλήρες ευρετήριο τεκμηρίωσης

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [docs/INSTALL.md](docs/INSTALL.md) - Οδηγός εγκατάστασης

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Εγκατάσταση χωρίς GUI (λειτουργία κονσόλας)

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [docs/HACKING.md](docs/HACKING.md) - Οδηγός προγραμματιστή και συστήματα build

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Διάταξη δέντρου πηγαίου κώδικα

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Διαχείριση απαγορεύσεων συνεδρίας I2P με nftables

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [docs/LICENSE.md](docs/LICENSE.md) - Άδειες τρίτων μερών

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [docs/history.txt](docs/history.txt) - Πλήρες αρχείο αλλαγών

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση


## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
### Υπο-έργα

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση


## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/README.md](apps/README.md) - Επισκόπηση εφαρμογών

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/addressbook/README.md](apps/addressbook/README.md) - Εφαρμογή βιβλίου διευθύνσεων

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Εφαρμογή Desktop GUI

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - API ελέγχου I2P

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - Πελάτης BitTorrent I2PSnark

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - Εφαρμογή τούνελ I2P

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/imagegen/README.md](apps/imagegen/README.md) - Εργαλεία δημιουργίας εικόνων

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/jetty/README.md](apps/jetty/README.md) - HTTP διακομιστής Jetty

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/jrobin/README.md](apps/jrobin/README.md) - Βιβλιοθήκη παρακολούθησης JRobin

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Ελάχιστη βιβλιοθήκη streaming

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/pack200/README.md](apps/pack200/README.md) - Συμπίεση Pack200

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Proxy scripts

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Κονσόλα δρομολογητή

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/sam/README.md](apps/sam/README.md) - Απλή Ανώνυμη Ανταλλαγή Μηνυμάτων

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/streaming/README.md](apps/streaming/README.md) - Βιβλιοθήκη streaming

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/susidns/README.md](apps/susidns/README.md) - DNS διακομιστής

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/susimail/README.md](apps/susimail/README.md) - Πελάτης email I2P

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [apps/systray/README.md](apps/systray/README.md) - Εφαρμογή συστημικής θήκης

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [core/README.md](core/README.md) - Τεκμηρίωση βιβλιοθήκης core

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Native JNI βιβλιοθήκη για κρυπτογράφηση (GMP)

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση


## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
### Διάφορα

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση


## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Διαχείριση απαγορεύσεων συνεδρίας I2P με nftables

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [installer/resources/README.md](installer/resources/README.md) - Πόροι εγκατάστασης

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [tools/scripts/README.md](tools/scripts/README.md) - Βοηθητικά scripts για ανάπτυξη και διαχείριση

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [tools/scripts/tests/README.md](tools/scripts/tests/README.md) - Scripts επικύρωσης και δοκιμής

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση


## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση


## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
- [docker/README.md](docker/README.md) - Εκτέλεση I2P+ σε Docker

## Στοιχεία επικοινωνίας
## Άδειες
## Τεκμηρίωση
