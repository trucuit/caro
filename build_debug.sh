#!/bin/bash
# ============================================================
# Caro — Debug Build + Install + Launch
# Workarounds for macOS SIP blocking ~/.gradle, app/build, etc.
# ============================================================
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
GRADLE_HOME="$HOME/.gradle/wrapper/dists/gradle-8.11.1-bin/bpt9gzteqjrbo1mjrsomdt32c/gradle-8.11.1"
PACKAGE="com.tructt.caro"
ACTIVITY="com.tructt.caro.MainActivity"

# --- Tools ---
ADB="$HOME/Library/Android/sdk/platform-tools/adb"
EMULATOR="$HOME/Library/Android/sdk/emulator/emulator"
AVD_MANAGER="$HOME/Library/Android/sdk/cmdline-tools/latest/bin/avdmanager"

# Fallback to PATH if not at default location
command -v adb    &>/dev/null && ADB=$(command -v adb)
command -v emulator &>/dev/null && EMULATOR=$(command -v emulator)

# --- 1. Start emulator if no device connected ---
echo "📱 Checking for connected devices..."
DEVICE=$("$ADB" devices 2>/dev/null | grep -w "device" | head -1 | awk '{print $1}')

if [ -z "$DEVICE" ]; then
    echo "⏳ No device found. Starting emulator..."

    # Pick the first available AVD
    AVD_NAME=$("$EMULATOR" -list-avds 2>/dev/null | head -1)

    if [ -z "$AVD_NAME" ]; then
        echo "❌ No AVD found. Create one in Android Studio → Device Manager first."
        exit 1
    fi

    echo "   Booting AVD: $AVD_NAME"
    "$EMULATOR" -avd "$AVD_NAME" -no-snapshot-load &>/dev/null &
    EMULATOR_PID=$!

    # Wait for emulator to boot (max 120s)
    echo -n "   Waiting for boot"
    for i in $(seq 1 120); do
        BOOT=$("$ADB" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')
        if [ "$BOOT" = "1" ]; then
            echo " ✅"
            break
        fi
        echo -n "."
        sleep 1
    done

    DEVICE=$("$ADB" devices | grep -w "device" | head -1 | awk '{print $1}')
    if [ -z "$DEVICE" ]; then
        echo ""
        echo "❌ Emulator failed to boot in 120s."
        exit 1
    fi
else
    echo "   Found: $DEVICE"
fi

# --- 2. Build ---
export GRADLE_USER_HOME=/tmp/gradle-home
export ANDROID_USER_HOME=/tmp/android-home
export TMPDIR=/tmp/gradle-tmp
export GRADLE_OPTS="-Djava.io.tmpdir=/tmp/gradle-tmp"
mkdir -p "$GRADLE_USER_HOME" "$ANDROID_USER_HOME" "$TMPDIR"

SETTINGS="$PROJECT_DIR/settings.gradle.kts"
REDIRECT='gradle.lifecycle.beforeProject { layout.buildDirectory = file("/tmp/caro-build/${project.name}") }'

if ! grep -q "caro-build" "$SETTINGS" 2>/dev/null; then
    ORIGINAL=$(cat "$SETTINGS")
    echo "$REDIRECT" > "$SETTINGS"
    echo "" >> "$SETTINGS"
    echo "$ORIGINAL" >> "$SETTINGS"
    INJECTED=true
fi

echo ""
echo "🔨 Building debug APK..."
"$GRADLE_HOME/bin/gradle" \
    --no-daemon \
    --project-cache-dir=/tmp/gradle-project-cache \
    -Djava.io.tmpdir=/tmp/gradle-tmp \
    -p "$PROJECT_DIR" \
    assembleDebug

if [ "$INJECTED" = true ]; then
    echo "$ORIGINAL" > "$SETTINGS"
fi

APK_PATH="/tmp/caro-build/app/outputs/apk/debug/app-debug.apk"
if [ ! -f "$APK_PATH" ]; then
    echo "❌ APK not found at $APK_PATH"
    exit 1
fi
echo "✅ Build done: $APK_PATH"

# --- 3. Install ---
echo ""
echo "📲 Installing on $DEVICE..."
"$ADB" -s "$DEVICE" uninstall "$PACKAGE" &>/dev/null || true
"$ADB" -s "$DEVICE" install -r "$APK_PATH"

# --- 4. Launch ---
echo ""
echo "🚀 Launching $PACKAGE..."
"$ADB" -s "$DEVICE" shell am start -n "$PACKAGE/$ACTIVITY"

echo ""
echo "✅ Done! App is running on $DEVICE"
