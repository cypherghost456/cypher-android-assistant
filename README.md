# Cypher — Android AI Assistant System

**Cypher** is a fully self-contained, high-performance Android AI assistant app that integrates a local Node.js backend gateway, advanced voice synthesis/recognition, a Jarvis-grade UI, and rootless device control via Accessibility Service and Shizuku.

---

## 🚀 Key Features

1. **Self-Contained Backend Gateway**:
   - Embeds a local Node.js runtime and HTTP gateway (`127.0.0.1:3000`), removing reliance on external servers.
2. **Modern Jetpack Compose UI**:
   - Dark futuristic theme with neon cyan accents, glassmorphism, smooth animations, and real-time chat interface.
3. **Advanced Voice System**:
   - Wake word detection (`"Cypher"` / `"Hey Cypher"`), Speech-to-Text (STT), AI reasoning processing, and Text-to-Speech (TTS) audio output.
4. **Device Control & Automation**:
   - **Accessibility Service**: Full screen content reading, UI element clicking, gesture dispatching, and app navigation.
   - **Shizuku Integration**: Rootless ADB shell execution for package management, app install/uninstall, and force-stopping background services.
5. **Secure Authentication**:
   - AndroidX `BiometricPrompt` supporting Face Unlock, fingerprint, and device credential PIN fallback.
   - Encrypted token storage via Android Keystore (`EncryptedSharedPreferences`).

---

## 📱 Project Directory Structure

```text
openclaw-android-assistant-main/
├── android/
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/com/codex/mobile/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── CypherCommandEngine.kt
│   │   │   │   ├── CypherAccessibilityService.kt
│   │   │   │   ├── CypherShizukuManager.kt
│   │   │   │   ├── CypherVoiceEngine.kt
│   │   │   │   ├── CypherFailsafeManager.kt
│   │   │   │   ├── CodexServerManager.kt
│   │   │   │   └── BootstrapInstaller.kt
│   │   │   ├── res/
│   │   │   └── assets/
│   │   ├── build.gradle.kts
│   │   └── build.gradle.kts
├── backend/                  # Cleaned Node.js backend runtime & server.js
├── README.md                 # Complete documentation
└── package.json
```

---

## 🛠️ How to Build & Run

### Prerequisites
- **Android Studio Ladybug+** (or Gradle 8.11+)
- **JDK 17**
- **Android SDK (Compile SDK 35, Min SDK 26)**

### Building the APK
1. Open the project folder in Android Studio.
2. Sync Gradle files.
3. Run the following command in the terminal to build the release APK:
   ```bash
   cd android
   ./gradlew assembleRelease
   ```
4. The generated APK will be available at:
   `android/app/build/outputs/apk/release/app-release.apk`

---

## 🔐 Required Permissions Explanation

- **`INTERNET`**: Required for communicating with local backend gateway and AI model providers.
- **`FOREGROUND_SERVICE` & `WAKE_LOCK`**: Keeps the local backend server running reliably in the background.
- **`BIND_ACCESSIBILITY_SERVICE`**: Enables Cypher to read screen content and perform automated UI taps/clicks upon request.
- **`USE_BIOMETRIC`**: Secures app launch and sensitive commands via Face Unlock / Fingerprint / PIN.
- **`QUERY_ALL_PACKAGES`**: Allows Cypher to inspect installed apps for voice-activated app launching and management.
