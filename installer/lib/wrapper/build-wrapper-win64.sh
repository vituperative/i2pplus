#!/bin/bash
# Build Tanuki Wrapper for Windows 64-bit
# Downloads source from SourceForge and cross-compiles using mingw-w64
#
# Usage: build-wrapper.sh [--version X.X.X]
# Requirements: sudo apt install mingw-w64

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_DIR="${SCRIPT_DIR}"
CACHE_DIR="${WRAPPER_DIR}/cache"
INSTALL_DIR="${WRAPPER_DIR}"
WORK_DIR="${WRAPPER_DIR}/build-temp"

if [ -f "${WRAPPER_DIR}/version.txt" ]; then
    VERSION=$(grep "^WRAPPER_VERSION=" "${WRAPPER_DIR}/version.txt" | cut -d= -f2)
else
    VERSION="3.6.4"
fi

while [[ $# -gt 0 ]]; do
    case "$1" in
        --version) VERSION="$2"; shift 2 ;;
        *) shift ;;
    esac
done

BASE_URL="https://sourceforge.net/projects/wrapper/files/wrapper_src"
TGZ="wrapper_${VERSION}_src.tar.gz"

SRC_DATE="20251218"
case "$VERSION" in
    3.6.5) SRC_DATE="20260317" ;;
    3.6.4) SRC_DATE="20251218" ;;
    3.6.3) SRC_DATE="20250910" ;;
esac

SRC_URL="${BASE_URL}/Wrapper_${VERSION}_${SRC_DATE}/${TGZ}"
SRC_CACHE_DIR="${CACHE_DIR}/wrapper_${VERSION}_src"
SRC_DIR="${WORK_DIR}/wrapper_${VERSION}_src"

mkdir -p "${CACHE_DIR}"
mkdir -p "${WORK_DIR}"

OLD_DIRS=$(ls -d "${CACHE_DIR}"/wrapper_*_src 2>/dev/null | grep -v "${VERSION}" || true)
if [ -n "${OLD_DIRS}" ]; then
    echo "Cleaning old cached sources..."
    rm -rf ${OLD_DIRS}
fi

if [ ! -d "${SRC_CACHE_DIR}" ]; then
    echo "=== Downloading Wrapper ${VERSION} Source ==="
    curl -L -o "${CACHE_DIR}/${TGZ}" "${SRC_URL}"
    tar -xzf "${CACHE_DIR}/${TGZ}" -C "${CACHE_DIR}"
    echo "Cached at: ${SRC_CACHE_DIR}"
else
    echo "Using cached source: ${SRC_CACHE_DIR}"
fi

rm -rf "${SRC_DIR}"
cp -r "${SRC_CACHE_DIR}" "${SRC_DIR}"

echo "=== Building Wrapper ${VERSION} for Windows x64 ==="
cd "${SRC_DIR}/src/c"

HOST="x86_64-w64-mingw32"
WIN_DIR="win64"

command -v ${HOST}-gcc >/dev/null || { echo "Need ${HOST}-gcc"; exit 1; }

cp wrapper_win.c wrapper_win.c.orig 2>/dev/null || true
cp wrapper.c wrapper.c.orig 2>/dev/null || true

sed -i 's/#include <errno.h>/#include <errno.h>\ntypedef int socklen_t;/' wrapper.c wrapper_win.c
sed -i 's/const char\* inet_ntop(/const char* wrapper_inet_ntop(/g' wrapper.c
sed -i 's/const char\* inet_pton(/const char* wrapper_inet_pton(/g' wrapper.c
sed -i 's/Iphlpapi\.h/iphlpapi.h/g' wrapperjni_win.c
[ ! -f wrapperinfo.c ] && [ -f wrapperinfo.c.in ] && cp wrapperinfo.c.in wrapperinfo.c

python3 - << 'PYEND'
import re
import sys

with open('wrapper_win.c', 'r') as f:
    content = f.read()

content = re.sub(r'__try\s*\{', '{', content)
content = re.sub(r'\}?\s*__except\s*\(', '/* except removed */ (', content)
content = re.sub(r'__finally\s*\{', '{', content)
content = content.replace('__leave;', '/* leave */;')

with open('wrapper_win.c', 'w') as f:
    f.write(content)
PYEND

cat > Makefile-windows-x64.mingw << 'MAKEFILE'
CC = x86_64-w64-mingw32-gcc
WIN_FLAGS = -DWIN32 -D_UNICODE -DUNICODE -DHAVE_EADDRINUSE
LDFLAGS = -L/usr/x86_64-w64-mingw32/lib -lws2_32 -lshlwapi -ladvapi32 -luser32 -lcrypt32 -lwintrust -lpdh -lpsapi -lole32 -loleaut32 -lactiveds -ladsiid -lmpr -lshell32 -lnetapi32

WRAPPER_SRCS = wrapper.c wrapperinfo.c wrappereventloop.c wrapper_jvm_launch.c wrapper_win.c property.c logger.c logger_file.c wrapper_file.c wrapper_i18n.c wrapper_hashmap.c wrapper_ulimit.c wrapper_encoding.c wrapper_jvminfo.c wrapper_secure_file.c wrapper_cipher.c wrapper_cipher_base.c wrapper_sysinfo.c

.PHONY: all install

all:
	@mkdir -p ../src/bin
	$(CC) -o ../src/bin/wrapper.exe $(WRAPPER_SRCS) $(WIN_FLAGS) $(LDFLAGS) -nostartfiles -e wmain

install: all
	cp ../src/bin/wrapper.exe /home/rogue/i2pplus/installer/lib/wrapper/win64/I2Psvc.exe
	x86_64-w64-mingw32-strip -s /home/rogue/i2pplus/installer/lib/wrapper/win64/I2Psvc.exe

MAKEFILE

make -f Makefile-windows-x64.mingw

if [ ! -f "${SRC_DIR}/src/bin/wrapper.exe" ]; then
    echo "Build failed"
    exit 1
fi

mkdir -p "${INSTALL_DIR}/${WIN_DIR}"
cp "${SRC_DIR}/src/bin/wrapper.exe" "${INSTALL_DIR}/${WIN_DIR}/I2Psvc.exe"
${HOST}-strip -s "${INSTALL_DIR}/${WIN_DIR}/I2Psvc.exe"

echo ""
echo "=== Built ==="
ls -lh "${INSTALL_DIR}/${WIN_DIR}/"