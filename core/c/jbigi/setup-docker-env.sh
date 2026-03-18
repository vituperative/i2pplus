#!/bin/bash
#
# Setup script for jbigi cross-compilation environment using Docker
# Run this script from the directory where you want to set up the build environment
#
# Usage: 
#   sudo ./setup-docker-env.sh           # Set up ARM32 (armhf)
#   sudo ./setup-docker-env.sh armhf    # Set up ARM32
#   sudo ./setup-docker-env.sh aarch64  # Set up ARM64
#
# Prerequisites:
#   sudo apt-get install docker.io qemu-user-static
#   # Enable Docker multi-arch:
#   docker run --rm --privileged multiarch/qemu-user-static --setup -p yes
#

set -e

ARCH="${1:-armhf}"

echo "Setting up $ARCH cross-compilation environment using Docker..."

# Check for Docker
if ! command -v docker >/dev/null 2>&1; then
    echo "Error: Docker not found. Install with: sudo apt-get install docker.io"
    exit 1
fi

# Check for root
if [ "$EUID" -ne 0 ]; then
    echo "Error: This script must be run as root (use sudo)"
    exit 1
fi

# Set up variables based on architecture
case "$ARCH" in
    armhf)
        DOCKER_IMAGE=arm32v7/ubuntu:plucky
        QEMU_ARCH=arm
        ;;
    aarch64|arm64)
        DOCKER_IMAGE=ubuntu:plucky
        QEMU_ARCH=aarch64
        ;;
    *)
        echo "Error: Unsupported architecture: $ARCH"
        echo "Supported: armhf, aarch64"
        exit 1
        ;;
esac

WORK_DIR="$(pwd)/jbigi-$ARCH"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "Creating work directory: $WORK_DIR"
mkdir -p "$WORK_DIR"

# Pull Docker image
echo "Pulling Docker image: $DOCKER_IMAGE"
docker pull $DOCKER_IMAGE || {
    echo "Error: Failed to pull Docker image"
    echo "Try enabling multi-arch support with:"
    echo "  docker run --rm --privileged multiarch/qemu-user-static --setup -p yes"
    exit 1
}

# Copy jbigi source
echo "Copying jbigi source..."
mkdir -p "$WORK_DIR/build"
cp -r "$SCRIPT_DIR" "$WORK_DIR/jbigi-src"

# Create build script inside the container
cat > "$WORK_DIR/build.sh" << 'BUILDSCRIPT'
#!/bin/bash
set -e
export DEBIAN_FRONTEND=noninteractive

# Install build dependencies
apt-get update
apt-get install -y --no-install-recommends \
    build-essential \
    autoconf \
    automake \
    libtool \
    m4 \
    wget \
    openjdk-17-jdk

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-armhf

# Build jbigi
cd /build/jbigi-src
./build.sh notest

# Copy output
cp lib/*.so /build/output/ 2>/dev/null || true
ls -la /build/output/
BUILDSCRIPT

chmod +x "$WORK_DIR/build.sh"

echo ""
echo "=============================================="
echo "Setup complete!"
echo "=============================================="
echo ""
echo "To build jbigi for $ARCH:"
echo ""
echo "  docker run --rm -it \\"
echo "    -v $WORK_DIR/jbigi-src:/build/jbigi-src \\"
echo "    -v $WORK_DIR/build:/build/output \\"
echo "    $DOCKER_IMAGE \\"
echo "    bash /build/build.sh"
echo ""
echo "Work directory: $WORK_DIR"
echo ""
echo "Or simpler - run the build script:"
echo "  sudo bash $WORK_DIR/build.sh"
