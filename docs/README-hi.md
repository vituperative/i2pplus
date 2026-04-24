[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

यह Java कार्यान्वयन के I2P के सॉफ्ट-फर्क का स्रोत कोड है।

नवीनतम रिलीज़: [https://i2pplus.github.io/](https://i2pplus.github.io/)

## स्थापना

स्थापना निर्देशों के लिए [INSTALL.txt](INSTALL.txt) या [https://i2pplus.github.io/](https://i2pplus.github.io/) देखें।

## प्रलेखन

[https://geti2p.net/how](https://geti2p.net/how)

अक्सर पूछे जाने वाले प्रश्न: [https://geti2p.net/faq](https://geti2p.net/faq)

API: [https://i2pplus.github.io/javadoc/](https://i2pplus.github.io/javadoc/) या 'ant javadoc' चलाएँ फिर build/javadoc/index.html पर शुरू करें।

## योगदान कैसे करें / I2P पर हैक करें

कृपया [HACKING.md](HACKING.md) और docs डायरेक्टरी में अन्य दस्तावेज़ों की जांच करें।

## स्रोत से पैकेज बनाना

स्रोत नियंत्रण से विकास शाखा प्राप्त करने के लिए: [https://github.com/I2PPlus/i2pplus/](https://github.com/I2PPlus/i2pplus/)

### पूर्वापेक्षाएँ

*   Java SDK (प्राथमिकता Oracle/Sun या OpenJDK) 1.8.0 या उच्चतर
*   Apache Ant 1.9.8 या उच्चतर
*   GNU gettext पैकेज से स्थापित xgettext, msgfmt, और msgmerge उपकरण [http://www.gnu.org/software/gettext/](http://www.gnu.org/software/gettext/)
*   निर्माण वातावरण को UTF-8 स्थानीय सेटिंग का उपयोग करना चाहिए।

### एंटी निर्माण प्रक्रिया

x86 सिस्टम पर निम्नलिखित चलाएँ (यह IzPack4 का उपयोग करके बनाएगा):

`ant pkg`

गैर-x86 पर, इसके बजाय निम्नलिखित में से एक का उपयोग करें:

`ant installer-linux ant installer-freebsd ant installer-osx ant installer-windows`

यदि आप IzPack5 के साथ निर्माण करना चाहते हैं, तो इस लिंक से डाउनलोड करें: [http://izpack.org/downloads/](http://izpack.org/downloads/) और फिर इसे स्थापित करें, और फिर निम्नलिखित कमांड(s) चलाएँ:

`ant installer5-linux ant installer5-freebsd ant installer5-osx ant installer5-windows`

मौजूदा स्थापना के लिए एक अस्वीकृत अपडेट बनाने के लिए, चलाएँ:

`ant updater`

अन्य निर्माण विकल्प देखने के लिए बिना तर्क के 'ant' चलाएँ।

### डॉकर

I2P को डॉकर में चलाने के अधिक जानकारी के लिए, देखें [Docker.md](../docker/Docker.md)

## संपर्क जानकारी

क्या आपको मदद चाहिए? I2P IRC नेटवर्क पर IRC चैनल #saltR देखें।

बग रिपोर्ट: [https://github.com/I2PPlus/i2pplus/issues](https://github.com/I2PPlus/i2pplus/issues)

## अनुमतियाँ

I2P+ AGPL v.3 के अंतर्गत लाइसेंसित है।

विभिन्न उप-घटक लाइसेंसों के लिए, देखें: [README.md](docs/LICENSE.md)