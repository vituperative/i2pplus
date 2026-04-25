#!/bin/bash
# Update all Tanuki Wrapper binaries
# Runs both build-wrapper.sh and update-wrapper.sh
#
# Usage: update-all.sh [--version X.X.X]

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "=== Building Windows binaries ==="
bash "${SCRIPT_DIR}/build-wrapper.sh" "$@"

echo ""
echo "=== Updating Unix binaries ==="
bash "${SCRIPT_DIR}/update-wrapper.sh" "$@"

echo ""
echo "=== Complete ==="