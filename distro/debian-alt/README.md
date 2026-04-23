# Debian Package Files

Alternative debian packaging for obsolete and non-standard releases.

## Overview

The `debian/` directory contains files for the current Debian stable.
The `debian-alt/` subdirectories contain variants for old releases.

## Current Release (debian/)

For Debian 12 (bookworm) and later.

## Supported Alternatives

| Release   | Status     | Notes              |
| --------- | ---------- | ------------------ |
| jessie    | Deprecated | Use trusty instead |
| trusty    | Supported  | Ubuntu 14.04 LTS   |
| xenial    | Supported  | Ubuntu 16.04 LTS   |
| bionic    | Supported  | Ubuntu 18.04 LTS   |
| focal     | Supported  | Ubuntu 20.04 LTS   |
| wheezy    | Deprecated | Unmaintained       |
| stretch   | Deprecated | Use bionic         |

## Usage

To use an alternate:
```bash
cp -r debian-alt/<release>/* debian/
```

Or build with:
```bash
cp -r debian-alt/<release>/* debian/
dpkg-buildpackage -i
```

## Notes

- All use `systemd` for service management (except wheezy/jessie)
- Jetty 9 is required (not available in old Debian)
- See `debian-alt/README.txt` in each subdirectory for details

## Building

```bash
# Install build deps
# See debian/control for Build-Depends

# Build
dpkg-buildpackage -i

# Install
dpkg -i i2p_*_all.deb
```

## Packages Built

| Package      | Purpose       |
| ------------ | ------------- |
| i2p          | Main router   |
| i2p-doc      | Documentation |
| libjbigi-jni | Native crypto |