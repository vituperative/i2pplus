# I2P Installer (`installer/`)

Builds the I2P+ installer JAR and Windows EXE packages using IzPack (v4 and v5). Also builds the `jbigi.jar` containing native crypto libraries for all platforms.

## Structure

| Directory       | Description                                         |
| --------------- | --------------------------------------------------- |
| `lib/izpack/`   | IzPack installer toolkit (v4, v5, shared resources) |
| `lib/jbigi/`    | Native JBIGI/JCPUID binaries for all platforms      |
| `lib/wrapper/`  | Tanuki Java Service Wrapper binaries                |
| `lib/launch4j/` | Launch4j Windows EXE wrapper                        |
| `resources/`    | Files bundled into the installed I2P package        |
| `java/`         | Installer Java source                               |
| `tools/`        | Build-time utilities                                |

## Building

```bash
# Ant (from repo root)
ant build
```

## Installer Builds

```bash
# IzPack 4 installer
ant installer

# IzPack 5 installer
ant installer5
```
