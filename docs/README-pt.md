[![CodeQL](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/codeql-analysis.yml)
[![Java CI](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml/badge.svg)](https://github.com/I2PPlus/i2pplus/actions/workflows/ant.yml)
[![I2P+ Update zip](https://i2pplus.github.io/download.svg)](https://i2pplus.github.io/i2pupdate.zip)
[![I2P+ I2PSnark standalone](https://i2pplus.github.io/i2psnarkdownload.svg)](https://i2pplus.github.io/installers/i2psnark-standalone.zip)
[![I2P+ Javadocs](https://i2pplus.github.io/javadocsdownload.svg)](https://i2pplus.github.io/javadoc.zip)

# I2P+

[English](../README.md) | [Русский](README-ru.md) | [日本語](README-ja.md) | [中文](README-zh.md) | [हिन्दी](README-hi.md) | [བོད་ཡིག](README-bo.md) | [فارسی](README-fa.md) | [العربية](README-ar.md) | [Español](README-es.md) | [Français](README-fr.md) | [Deutsch](README-de.md) | [Türkçe](README-tr.md) | [Bahasa Indonesia](README-id.md) | [Українська](README-uk.md) | [Polski](README-pl.md) | [한국어](README-ko.md) | [Tiếng Việt](README-vi.md) | [ภาษาไทย](README-th.md) | [اردو](README-ur.md) | [עברית](README-he.md) | [Italiano](README-it.md) | [Nederlands](README-nl.md) | [Română](README-ro.md) | [Čeština](README-cs.md) | [Magyar](README-hu.md) | [Ελληνικά](README-el.md)

Este é o código fonte do fork suave (soft-fork) da implementação Java do I2P.

Último lançamento: https://i2pplus.github.io/

## Instalação

Consulte [INSTALL.md](docs/INSTALL.md) ou https://i2pplus.github.io/ para instruções de instalação.

## Documentação

https://geti2p.net/how

FAQ: https://geti2p.net/faq

API: https://i2pplus.github.io/javadoc/
ou execute 'ant javadoc' e depois inicie em build/javadoc/index.html

## Como contribuir / Programar no I2P

Por favor, verifique [HACKING.md](docs/HACKING.md) e outros documentos no diretório docs.

## Compilando pacotes a partir do código fonte

Para obter o branch de desenvolvimento do controle de código fonte: https://github.com/I2PPlus/i2pplus/

### Pré-requisitos

- Java SDK (preferencialmente Oracle/Sun ou OpenJDK) 1.8.0 ou superior
  - Sistemas operacionais não-linux e JVMs: Veja 
  - Certos subsistemas para embarcado (core, router, mstreaming, streaming, i2ptunnel)
- Apache Ant 1.9.8 ou superior
- As ferramentas xgettext, msgfmt e msgmerge instaladas do pacote GNU gettext
  http://www.gnu.org/software/gettext/
- O ambiente de compilação deve usar uma localização UTF-8.

### Processo de compilação com Ant

Em sistemas x86 execute o seguinte (isso irá compilar usando IzPack4):

    ant pkg

Em sistemas não-x86, use uma das seguintes alternativas:

    ant installer-linux
    ant installer-freebsd
    ant installer-osx
    ant installer-windows

Se você quiser compilar com IzPack5, baixe de: http://izpack.org/downloads/
e então instale, e então execute o(s) seguinte(s) comando(s):

    ant installer5-linux
    ant installer5-freebsd
    ant installer5-osx
    ant installer5-windows

Para compilar uma atualização não assinada para uma instalação existente, execute:

    ant updater

Se você tiver problemas ao compilar um instalador completo (Java14 e versões posteriores podem gerar erros de compilação para izpack relacionados a pack200),
você pode compilar um zip de instalação completo que pode ser extraído e executado no local:

     ant zip-linux
     ant zip-freebsd
     ant zip-macos
     ant zip-windows

Execute 'ant' sem argumentos para ver outras opções de compilação.

### Docker

Para mais informações sobre como executar o I2P no Docker, veja [docker/README.md](docker/README.md)

## Informações de contato

Precisa de ajuda? Visite o canal IRC #saltR na rede IRC do I2P

Relatórios de bugs: https://i2pgit.org/i2p-hackers/i2p.i2p/-/issues ou https://github.com/I2PPlus/i2pplus/issues

## Licenças

I2P+ é licenciado sob a AGPL v.3.

Para as licenças dos vários subcomponentes, veja: [README.md](docs/licenses/README.md)

## Veja também

### Documentação

- [docs/README.md](docs/README.md) - Índice completo de documentação
- [docs/INSTALL.md](docs/INSTALL.md) - Guia de instalação
- [docs/INSTALL-headless.md](docs/INSTALL-headless.md) - Instalação sem GUI (modo console)
- [docs/HACKING.md](docs/HACKING.md) - Guia do desenvolvedor e sistemas de compilação
- [docs/DIRECTORIES.md](docs/DIRECTORIES.md) - Layout da árvore de código fonte e onde encontrar coisas
- [docs/i2p-sessionban-nftables.md](docs/i2p-sessionban-nftables.md) - Gerenciando bans de sessão I2P com nftables
- [docs/history.txt](docs/history.txt) - Registro completo de alterações

### READMEs de subprojetos

- [apps/addressbook/README.md](apps/addressbook/README.md) - Aplicativo de agenda de endereços
- [apps/desktopgui/README.md](apps/desktopgui/README.md) - Aplicativo GUI de desktop
- [apps/i2pcontrol/README.md](apps/i2pcontrol/README.md) - API de Controle I2P
- [apps/i2psnark/README.md](apps/i2psnark/README.md) - Cliente BitTorrent I2PSnark
- [apps/i2ptunnel/README.md](apps/i2ptunnel/README.md) - Aplicativo de Tunnel I2P
- [apps/imagegen/README.md](apps/imagegen/README.md) - Ferramentas de geração de imagens
- [apps/jetty/README.md](apps/jetty/README.md) - Servidor HTTP Jetty
- [apps/jrobin/README.md](apps/jrobin/README.md) - Biblioteca de monitoramento JRobin
- [apps/ministreaming/README.md](apps/ministreaming/README.md) - Biblioteca de streaming mínima
- [apps/pack200/README.md](apps/pack200/README.md) - Compressão Pack200
- [apps/proxyscript/README.md](apps/proxyscript/README.md) - Scripts de proxy
- [apps/README.md](apps/README.md) - Visão geral dos aplicativos
- [apps/routerconsole/README.md](apps/routerconsole/README.md) - Console do roteador
- [apps/sam/README.md](apps/sam/README.md) - Mensagens anônimas simples
- [apps/streaming/README.md](apps/streaming/README.md) - Biblioteca de streaming
- [apps/susidns/README.md](apps/susidns/README.md) - Servidor DNS
- [apps/susimail/README.md](apps/susimail/README.md) - Cliente de e-mail I2P
- [apps/systray/README.md](apps/systray/README.md) - Aplicativo de bandeja do sistema
- [core/c/jbigi/docs/README.md](core/c/jbigi/docs/README.md) - Biblioteca nativa BigInteger (GMP)
- [core/c/jcpuid/README.md](core/c/jcpuid/README.md) - Biblioteca nativa de detecção de CPU
- [core/README.md](core/README.md) - Documentação da biblioteca principal
- [docker/README.md](docker/README.md) - Executando I2P+ no Docker
- [docs/licenses/README.md](docs/licenses/README.md) - Licenças de terceiros
- [installer/lib/jbigi/README.md](installer/lib/jbigi/README.md) - Biblioteca JNI nativa para criptografia (GMP)
- [installer/resources/README.md](installer/resources/README.md) - Recursos do instalador incluídos
- [scripts/README.md](scripts/README.md) - Scripts de utilidade para desenvolvimento e administração
- [scripts/tests/README.md](scripts/tests/README.md) - Scripts de validação e teste