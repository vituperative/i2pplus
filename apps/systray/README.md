# System Tray (`systray/`)

Tray-based URL launcher. Opens the router console in the browser with auto-detection of router readiness.

## What It Does

- System tray icon when running without full desktopgui
- Click to open router console
- Wait for router to be ready before opening URL
- Configurable default URL

## When To Use

- Headless servers
- Minimal installations
- Alternative to desktopgui

## Key Classes

| Class         | Purpose                          |
| ------------- | -------------------------------- |
| `Main`        | Entry point                      |
| `I2PLauncher` | URL opening with readiness check |

## Packages

- `net.i2p.apps.systray` - URL launcher, config

## Building

```bash
# Gradle
./gradlew :apps:systray:jar

# Ant (from repo root)
ant buildSystray
```