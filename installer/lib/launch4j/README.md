# Launch4j (`lib/launch4j/`)

Third-party tool for wrapping Java JARs into Windows native executables. Used to create `i2pinstall.exe` and `i2p.exe` (standalone router launcher).

**Version: 3.50** (released 2022-11-15)

Source: https://sourceforge.net/projects/launch4j/

## What's Included

- `launch4j.jar` - Launch4j compiler (v3.50)
- `launch4j` - Shell wrapper script
- `bin/` - MinGW cross-compilation tools (`ld`, `windres`)
- `lib/` - Launch4j dependencies
- `w32api/` - Windows API static libraries
- `head/` - EXE header stubs (console, GUI)
- `i2plustoexe.xml` - I2P installer EXE configuration
- `manifest/` - Windows manifest files
- `i2pstandalone.xml` - Standalone router EXE configuration (in `installer/`)

## Usage

Launch4j is invoked by Ant targets in the root `build.xml`:

- `installerexe` - Wraps the IzPack 4 `install.jar` into `i2pinstall.exe`
- `installer5exe` - Wraps the IzPack 5 `install.jar` into `i2pinstall.exe`

```bash
# Direct usage
java -jar lib/launch4j/launch4j.jar config.xml
```

## License

BSD 3-Clause. See `LICENSE.txt`.
