#!/bin/bash
# Download and install appimagetool
# Only downloads if a newer version is available

set -e

APPIMAGE_TOOL_VERSION="${1:-continuous}"
TOOLS_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APPIMGETOOLBIN="${TOOLS_DIR}/appimagetool"
VERSION_FILE="${TOOLS_DIR}/version.txt"

get_remote_version() {
    local url="https://github.com/AppImage/appimagetool/releases/tag/$1"
    curl -sL "$url" 2>/dev/null | grep -oP 'appimagetool-x86_64-\K[0-9.]+(?=\.AppImage)' | head -1 || echo ""
}

download_appimagetool() {
    local version="$1"
    local tmpdir
    tmpdir=$(mktemp -d)

    echo "Downloading appimagetool ${version}..."

    local url
    if [ "$version" = "continuous" ]; then
        url="https://github.com/AppImage/appimagetool/releases/download/continuous/appimagetool-x86_64.AppImage"
    else
        url="https://github.com/AppImage/appimagetool/releases/download/${version}/appimagetool-x86_64.AppImage"
    fi

    local outfile="${tmpdir}/appimagetool.AppImage"

    curl -L -o "$outfile" "$url"
    chmod +x "$outfile"

    mv "$outfile" "$APPIMGETOOLBIN"
    echo "$version" > "$VERSION_FILE"

    rm -rf "$tmpdir"
    echo "Installed appimagetool to $APPIMGETOOLBIN"
}

# Check if already installed
if [ -x "$APPIMGETOOLBIN" ]; then
    if [ "$APPIMAGE_TOOL_VERSION" = "continuous" ]; then
        remote_ver=$(get_remote_version "continuous" 2>/dev/null || echo "")
        local_ver=""
        [ -f "$VERSION_FILE" ] && local_ver=$(cat "$VERSION_FILE")

        if [ -n "$remote_ver" ] && [ "$remote_ver" != "$local_ver" ]; then
            echo "Remote version: $remote_ver, local version: $local_ver"
            echo "Updating appimagetool..."
            download_appimagetool "$APPIMAGE_TOOL_VERSION"
        else
            echo "appimagetool already up to date (version: ${local_ver:-unknown})"
        fi
    else
        echo "appimagetool already installed"
    fi
else
    download_appimagetool "$APPIMAGE_TOOL_VERSION"
fi

echo "appimagetool: $APPIMGETOOLBIN"
