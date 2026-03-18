#!/bin/bash
#
# Setup script for jbigi cross-compilation environment
# Run this script from the directory where you want to set up the build environment
#
# Usage: 
#   sudo ./setup-cross-env.sh           # Set up ARM32 (armhf)
#   sudo ./setup-cross-env.sh armhf    # Set up ARM32
#   sudo ./setup-cross-env.sh aarch64  # Set up ARM64
#
# Prerequisites:
#   sudo apt-get install qemu-user-static debootstrap
#

set -e

ARCH="${1:-armhf}"
SUITE="${2:-resolute}"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
CURRENT_DIR="$(pwd)"

echo "Setting up $ARCH cross-compilation environment (suite: $SUITE)..."

# Check for root
if [ "$EUID" -ne 0 ]; then
    echo "Error: This script must be run as root (use sudo)"
    exit 1
fi

# Check prerequisites
command -v debootstrap >/dev/null 2>&1 || { echo "Error: debootstrap not found. Install with: apt-get install debootstrap"; exit 1; }
command -v qemu-arm >/dev/null 2>&1 || { echo "Error: qemu-arm not found. Install with: apt-get install qemu-user-binfmt"; exit 1; }

# Set up variables based on architecture
case "$ARCH" in
    armhf)
        DEB_ARCH=armhf
        QEMU_BIN=qemu-arm
        ;;
    aarch64|arm64)
        DEB_ARCH=arm64
        QEMU_BIN=qemu-aarch64
        ;;
    *)
        echo "Error: Unsupported architecture: $ARCH"
        echo "Supported: armhf, aarch64"
        exit 1
        ;;
esac

WORK_DIR="$CURRENT_DIR/jbigi-$ARCH"
ROOTFS="$WORK_DIR/rootfs"

echo "Creating directory: $ROOTFS"
mkdir -p "$ROOTFS"

# Bootstrap Ubuntu
echo "Bootstrapping Ubuntu $SUITE for $DEB_ARCH..."
debootstrap --arch=$DEB_ARCH $SUITE "$ROOTFS" http://archive.ubuntu.com/ubuntu/

# Copy QEMU binary
echo "Setting up QEMU..."
cp /usr/bin/$QEMU_BIN "$ROOTFS/usr/bin/"

# Mount filesystems
echo "Mounting filesystems..."
mount -t proc /proc "$ROOTFS/proc"
mount -t sysfs /sys "$ROOTFS/sys"
mount -o bind /dev "$ROOTFS/dev"

# Install build dependencies in chroot
echo "Installing build dependencies..."
chroot "$ROOTFS" /bin/bash -c "
    export DEBIAN_FRONTEND=noninteractive
    apt-get update
    apt-get install -y --no-install-recommends \
        build-essential \
        autoconf \
        automake \
        libtool \
        m4 \
        wget \
        gcc \
        g++ \
        file
"

# Copy jbigi source
echo "Copying jbigi source..."
cp -r "$SCRIPT_DIR" "$WORK_DIR/jbigi-src"

# Unmount
echo "Cleaning up mounts..."
umount "$ROOTFS/proc" || true
umount "$ROOTFS/sys" || true
umount "$ROOTFS/dev" || true

echo ""
echo "=============================================="
echo "Setup complete!"
echo "=============================================="
echo ""
echo "To build jbigi for $ARCH:"
echo ""
echo "  # Mount filesystems:"
echo "  sudo mount -t proc /proc \$ROOTFS/proc"
echo "  sudo mount -t sysfs /sys \$ROOTFS/sys"  
echo "  sudo mount -o bind /dev \$ROOTFS/dev"
echo ""
echo "  # Chroot in:"
echo "  sudo chroot \$ROOTFS /bin/bash"
echo ""
echo "  # Then inside the chroot:"
echo "  cd /jbigi-src"
echo "  export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-armhf"
echo "  # Or download and set up Java 8 for ARM"
echo "  ./build.sh"
echo ""
echo "  # Exit chroot and unmount:"
echo "  exit"
echo "  sudo umount \$ROOTFS/proc \$ROOTFS/sys \$ROOTFS/dev"
echo ""
echo "Work directory: $WORK_DIR"
echo "Rootfs: $ROOTFS"
