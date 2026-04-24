[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

זהו קוד המקור של הפריקה הרכה (soft-fork) של יישום ה-Java של I2P.

הגרסה האחרונה: https://i2pplus.github.io/

## התקנה

עיין ב-[INSTALL.md](docs/INSTALL.md) או ב-https://i2pplus.github.io/ להוראות התקנה.

## תיעוד

https://geti2p.net/how

שאלות נפוצות: https://geti2p.net/faq

API: https://i2pplus.github.io/javadoc/
או הרץ 'ant javadoc' ואז התחל ב-build/javadoc/index.html

## כיצד לתרום / לפרוץ ב-I2P

עיין ב-[HACKING.md](docs/HACKING.md) ובמסמכים הנוספים בתיקיית docs.

## בניית חבילות מקוד המקור

כדי לקבל את ענף הפיתוח מבקרת המקור: https://github.com/I2PPlus/i2pplus/

### דרישות מוקדמות

- Java SDK (רצוי Oracle/Sun או OpenJDK) 1.8.0 ומעלה
  - תת-מערכות מסוימות למשובצים (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 ומעלה
- התקן את הכלים xgettext, msgfmt, ו-msgmerge מחבילת GNU gettext
  http://www.gnu.org/software/gettext/
- סביבת הבנייה חייבת להשתמש במיקום UTF-8.

### תהליך הבנייה עם Ant

במערכות x86 הרץ את הבא (זה יבנה באמצעות IzPack4):

    ant pkg

במערכות שאינן x86, השתמש באחת מהאפשרויות הבאות במקום:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

אם ברצונך לבנות עם IzPack5, הורד מ: http://izpack.org/downloads/ ואז
התקן אותו, ואז הרץ את הפקודה(ות) הבאות:

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

כדי לבנת עדכון לא חתום להתקנה קיימת, הרץ:

    ant updater

אם יש לך בעיות בבניית מתקין מלא (Java14 ומעלה עלולים ליצור שגיאות בנייה עבור izpack הקשורות ל-pack200),
תוכל לבנות zip התקנה מלא שניתן לחלץ ולהפעיל במקום:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

הרץ 'ant' ללא ארגומנטים כדי לראות אפשרויות בנייה נוספות.

### Docker

למידע נוסף על הפעלת I2P ב-Docker, עיין ב-[docker/README.md](docker/README.md)

## פרטי קשר

צריך עזרה? בקר בערוץ IRC #saltR ברשת I2P IRC

דיווח על באגים: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues או https://github.com/I2PPlus/i2pplus/issues

## רישיונות

I2P+ מורשית תחת AGPL v.3.

עבור רישיונות תת-רכיבים שונים, עיין ב: [LICENSE.txt](docs/licenses/LICENSE.txt)

## ראה גם

### תיעוד

- [docs/README.md](docs/README.md) - אינדקס תיעוד מלא
- [docs/INSTALL.md](docs/INSTALL.md) - מדריך התקנה
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - התקנה ללא ממשק גרפי (מצב קונסולה)
- [docs/HACKING.md](docs/HACKING.md) - מדריך מפתחים ומערכות בנייה
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - פריסת עץ המקור והיכן למצוא דברים
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - ניהול איסורי הפעלות I2P עם nftables
- [docs/history.txt](docs/history.txt) - יומן שינויים מלא

### README של פרויקטים משנה

- [apps/addressbook/README.md](apps/addressbook/README.md) - יישום פנקס כתובות
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - יישום ממשק שולחן עבודה
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - I2P Control API
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - לקוח I2PSnark BitTorrent
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - יישום I2P Tunnel
- [apps/imagegen/README.md](apps/imagegen/README.md) - כלי יצירת תמונות
- [apps/jetty/README.md](apps/jetty/README.md) - שרת HTTP Jetty
- [apps/jrobin/README.md](apps/jrobin/README.md) - ספריית ניטור JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - ספריית streaming מינימלית
- [apps/pack200/README.md](apps/pack200/README.md) - דחיסת Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - סקריפטים פרוקסי
- [apps/README.md](apps/README.md) - סקירת יישומים
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - קונסולת נתב
- [apps/sam/README.md](apps/sam/README.md) - הודעות אנונימיות פשוטות
- [apps/streaming/README.md](apps/streaming/README.md) - ספריית streaming
- [apps/susidns/README.md](apps/susidns/README.md) - שרת DNS
- [apps/susimail/README.md](apps/susimail/README.md) - לקוח דואר I2P
- [apps/systray/README.md](apps/systray/README.md) - יישום מגש מערכת
- [core/c/jbigi/docs/README.md](core/c/jbigi/docs/README.md) - ספריית BigInteger מקורית (GMP)
- [core/c/jcpuid/README.md](core/c/jcpuid/README.md) - ספריית זיהוי CPU מקורית
- [core/README.md](core/README.md) - תיעוד ספריית הליבה
- [docker/README.md](docker/README.md) - הפעלת I2P+ ב-Docker
- [docs/licenses/README.md](docs/licenses/README.md) - רישיונות צד שלישי
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - ספריית JNI מקורית לקריפטוגרפיה (GMP)
- [installer/resources/README.md](installer/resources/README.md) - משאבי התקנה כוללים
- [scripts/README.md](scripts/README.md) - סקריפטים שירותים לפיתוח וניהול
- [scripts/tests/README.md](scripts/tests/README.md) - סקריפטים לאימות ובדיקה