[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

هذا هو الكود المصدري للفرع الناعم (soft-fork) للتنفيذ بلغة Java لـ I2P.

أحدث إصدار: https://i2pplus.github.io/

## التثبيت

راجع [INSTALL.md](docs/INSTALL.md) أو https://i2pplus.github.io/ للحصول على تعليمات التثبيت.

## التوثيق

https://geti2p.net/how

الأسئلة الشائعة: https://geti2p.net/faq

واجهة برمجة التطبيقات: https://i2pplus.github.io/javadoc/
أو قم بتشغيل 'ant javadoc' ثم ابدأ بـ build/javadoc/index.html

## كيفية المساهمة / البرمجة على I2P

يرجى مراجعة [HACKING.md](docs/HACKING.md) والمستندات الأخرى في دليل المستندات.

## بناء الحزم من المصدر

للحصول على فرع التطوير من نظام التحكم في الكود المصدري: https://github.com/I2PPlus/i2pplus/

### المتطلبات الأساسية

- مجموعة تطوير Java (يفضل Oracle/Sun أو OpenJDK) 1.8.0 أو أعلى
  - أنظمة التشغيل غير Linux وآلات Java الافتراضية
  - بعض الأنظمة الفرعية المدمجة (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 أو أعلى
- يجب تثبيت أدوات xgettext و msgfmt و msgmerge من حزمة GNU gettext
  http://www.gnu.org/software/gettext/
- يجب أن يستخدم بيئة البناء موقع UTF-8.

### عملية البناء باستخدام Ant

على أنظمة x86 قم بتشغيل ما يلي (سيتم البناء باستخدام IzPack4):

    ant pkg

على الأنظمة غير x86، استخدم أحد البدائل التالية:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

إذا كنت تريد البناء باستخدام IzPack5، قم بتنزيله من: http://izpack.org/downloads/
ثم قم بتثبيته، ثم قم بتشغيل الأمر (الأوامر) التالية:

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

لبناء تحديث غير موقع لتثبيت موجود، قم بتشغيل:

    ant updater

إذا واجهت مشاكل في بناء المثبت الكامل (Java14 والإصدارات الأحدث قد تُنشئ أخطاء في البناء لـ izpack المتعلقة بـ pack200)،
يمكنك بناء ملف zip للتثبيت الكامل والذي يمكن استخراجه وتشغيله في مكانه:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

قم بتشغيل 'ant' بدون وسيطات لرؤية خيارات البناء الأخرى.

### Docker

لمزيد من المعلومات حول كيفية تشغيل I2P في Docker، انظر [docker/README.md](docker/README.md)

## معلومات الاتصال

تحتاج إلى مساعدة؟ قم بزيارة قناة IRC #saltR على شبكة I2P IRC

تقارير الأخطاء: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues أو https://github.com/I2PPlus/i2pplus/issues

## التراخيص

مرخص I2P+ تحت AGPL v.3.

للتراخيص الفرعية للمكونات المختلفة، انظر: [LICENSE.txt](docs/licenses/LICENSE.txt)

## انظر أيضًا

### التوثيق

- [docs/README.md](docs/README.md) - فهرس التوثيق الكامل
- [docs/INSTALL.md](docs/INSTALL.md) - دليل التثبيت
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - التثبيت بدون واجهة رسومية (وضع وحدة التحكم)
- [docs/HACKING.md](docs/HACKING.md) - دليل المطور وأنظمة البناء
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - تخطيط شجرة المصدر وأين تجد الأشياء
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - إدارة حظر جلسات I2P باستخدام nftables
- [docs/history.txt](docs/history.txt) - سجل التغييرات الكامل

### READMEs للمشاريع الفرعية

- [apps/addressbook/README.md](apps/addressbook/README.md) - تطبيق دفتر العناوين
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - تطبيق واجهة المستخدم الرسومية للسطح المكتب
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - واجهة برمجة تطبيقات تحكم I2P
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - عميل BitTorrent I2PSnark
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - تطبيق نفق I2P
- [apps/imagegen/README.md](apps/imagegen/README.md) - أدوات إنشاء الصور
- [apps/jetty/README.md](apps/jetty/README.md) - خادم HTTP Jetty
- [apps/jrobin/README.md](apps/jrobin/README.md) - مكتبة المراقبة JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - مكتبة البث المصغرة
- [apps/pack200/README.md](apps/pack200/README.md) - ضغط Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - نصوص الوكيل
- [apps/README.md](apps/README.md) - نظرة عامة على التطبيقات
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - وحدة تحكم الموجه
- [apps/sam/README.md](apps/sam/README.md) - المراسلة المجهولة البسيطة
- [apps/streaming/README.md](apps/streaming/README.md) - مكتبة البث
- [apps/susidns/README.md](apps/susidns/README.md) - خادم DNS
- [apps/susimail/README.md](apps/susimail/README.md) - عميل البريد الإلكتروني I2P
- [apps/systray/README.md](apps/systray/README.md) - تطبيق علبة النظام
- [core/c/jbigi/docs/README.md](core/c/jbigi/docs/README.md) - مكتبة BigInteger الأصلية (GMP)
- [core/c/jcpuid/README.md](core/c/jcpuid/README.md) - مكتبة اكتشاف CPU الأصلية
- [core/README.md](core/README.md) - توثيق المكتبة الأساسية
- [docker/README.md](docker/README.md) - تشغيل I2P+ في Docker
- [docs/licenses/README.md](docs/licenses/README.md) - تراخيص الأطراف الثالثة
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - مكتبة JNI الأصلية للتشفير (GMP)
- [installer/resources/README.md](installer/resources/README.md) - موارد المثبت المجمعة
- [scripts/README.md](scripts/README.md) - نصوص مساعدة للتطوير والإدارة
- [scripts/tests/README.md](scripts/tests/README.md) - نصوص التحقق والاختبار