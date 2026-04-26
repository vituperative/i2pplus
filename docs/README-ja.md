[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Português](README-pt.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

これはI2PのJava実装のソフトフォークのソースコードです。

最新リリース：https://i2pplus.github.io/

## インストール

[INSTALL.txt](INSTALL.txt)を参照するか、https://i2pplus.github.io/でインストール手順を確認してください。

## ドキュメント

https://geti2p.net/how

FAQ：https://geti2p.net/faq

API：https://i2pplus.github.io/javadoc/
または 'ant javadoc' を実行し、build/javadoc/index.htmlから開始してください。

## 貢献方法 / I2Pの改造

[HACKING.md](HACKING.md)とdocsディレクトリの他のドキュメントをご確認ください。

## ソースコードからパッケージをビルドする方法

ソースコントロールから開発ブランチを取得するには：https://github.com/I2PPlus/i2pplus/

### 必要条件

- Java SDK（可能な限りOracle/SunまたはOpenJDK）1.8.0以上
- Apache Ant 1.9.8以上
- GNU gettextパッケージからインストールされたxgettext、msgfmt、およびmsgmergeツール
  http://www.gnu.org/software/gettext/
- ビルド環境はUTF-8ロケールを使用する必要があります。

### Antビルドプロセス

x86システムでは、次のコマンドを実行します（これによりIzPack4を使用してビルドされます）：

    ant pkg

x86以外の場合は、次のいずれかを使用してください：

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

IzPack5でビルドする場合は、http://izpack.org/downloads/ からダウンロードし、
インストールしてから次のコマンドを実行してください：

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

既存のインストールに対して署名されていない更新をビルドするには、次のコマンドを実行します：

    ant updater

他のビルドオプションを確認するには、引数なしで 'ant' を実行してください。

## See also

### ドキュメント

- [docs/README.md](docs/README.md) - 完全的ドキュメント索引
- [docs/INSTALL.md](docs/INSTALL.md) - インストールガイド
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - ヘッドレス（コンソールモード）インストール
- [docs/HACKING.md](docs/HACKING.md) - 開発者ガイドとビルドシステム
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - ソースツリー構造
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - nftablesによるI2Pセッション禁止の管理
- [docs/LICENSE.md](docs/LICENSE.md) - サードパーティライセンス
- [docs/history.txt](docs/history.txt) - 完全な変更履歴

### サブプロジェクト

- [apps/README.md](apps/README.md) - アプリケーション概要
- [apps/addressbook/README.md](apps/addressbook/README.md) - アドレス帳アプリケーション
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - デスクトップGUIアプリケーション
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - I2PコントロールAPI
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - I2PSnark BitTorrentクライアント
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - I2Pトンネルアプリケーション
- [apps/imagegen/README.md](apps/imagegen/README.md) - 画像生成ツール
- [apps/jetty/README.md](apps/jetty/README.md) - Jetty HTTPサーバー
- [apps/jrobin/README.md](apps/jrobin/README.md) - JRobin監視ライブラリ
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - 最小ストリーミングライブラリ
- [apps/pack200/README.md](apps/pack200/README.md) - Pack200圧縮
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - プロキシスクリプト
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - ルーターコンソール
- [apps/sam/README.md](apps/sam/README.md) - シンプル匿名メッセージング
- [apps/streaming/README.md](apps/streaming/README.md) - ストリーミングライブラリ
- [apps/susidns/README.md](apps/susidns/README.md) - DNSサーバー
- [apps/susimail/README.md](apps/susimail/README.md) - I2Pメールクライアント
- [apps/systray/README.md](apps/systray/README.md) - システムトレイアプリケーション
- [core/README.md](core/README.md) - コアライブラリのドキュメント
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - 暗号化用ネイティブJNIライブラリ(GMP)

### その他

- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - nftablesによるI2Pセッション禁止の管理
- [installer/resources/README.md](installer/resources/README.md) - バンドルされたインストーラーリソース
- [tools/scripts/README.md](tools/scripts/README.md) - 開発・管理用ユーティリティスクリプト
- [tools/scripts/tests/README.md](tools/scripts/tests/README.md) - 検証・テストスクリプト



- [docker/README.md](docker/README.md) - DockerでのI2P+の実行





## 連絡先情報
ヘルプが必要ですか？I2P IRCネットワークのIRCチャンネル＃saltRをご覧ください。

## 連絡先情報


## 連絡先情報
バグ報告：https://github.com/I2PPlus/i2pplus/issues

## 連絡先情報


## 連絡先情報


## 連絡先情報
## ライセンス
I2P+はAGPL v.3の下でライセンスされています。

## 連絡先情報
## ライセンス


## 連絡先情報
## ライセンス
さまざまなサブコンポーネントのライセンスについては、[README.md](docs/LICENSE.md)を参照してください。

## 連絡先情報
## ライセンス
