# Network Switcher — Moto Edge 50 Pro

A minimal Android app that lets you switch between 5G and LTE in **2 taps**:

1. Tap the app icon
2. (Dual SIM) Pick your SIM → lands directly on the **Preferred network type** screen

---

## Build options

### Option A — GitHub Actions (easiest, no installs)

1. Create a free account at [github.com](https://github.com)
2. Create a new **public** repository called `NetworkSwitcher`
3. Upload all files from this zip (keep folder structure)
4. GitHub automatically runs the build workflow
5. Go to **Actions → Build APK → Artifacts** → download `NetworkSwitcher-APK.zip`
6. Unzip, install the APK on your phone (enable *Install unknown apps* for your browser)

### Option B — Android Studio (offline)

1. Download [Android Studio](https://developer.android.com/studio) (free)
2. Open this folder as an existing project
3. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
4. APK appears in `app/build/outputs/apk/debug/`
5. Transfer to phone and install

### Option C — Online build service

Upload the project zip to [Appetize.io](https://appetize.io) or
[BuildStore](https://buildstore.io) if you don't want to install anything locally.

---

## Install on phone

1. Transfer the APK to your Moto Edge 50 Pro
2. Tap it from Files app
3. If prompted, allow *Install from unknown sources* for Files app
4. Done — **"Net Switch"** appears on your home screen

---

## How it works

- On launch, reads active SIM subscriptions
- If you have 2 SIMs → shows a one-tap picker dialog
- Then fires the `android.settings.NETWORK_OPERATOR_SETTINGS` intent with the SIM's
  subscription ID — this lands you directly on the *Preferred network type* screen
- Falls back through 3 alternative intents if the primary one isn't available
- App finishes itself after launching settings (no background process, zero battery use)

---

## Permissions

| Permission | Why |
|---|---|
| `READ_PHONE_STATE` | To list your SIMs and show the picker |

No internet, no tracking, no analytics.
