#!/usr/bin/env sh
# Delegate root Gradle invocations to the Android project so `./gradlew assembleDebug`
# works from the repository root and in CI.
set -eu
SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
exec "$SCRIPT_DIR/apps/android/gradlew" "$@"
