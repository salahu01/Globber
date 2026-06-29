# Globber

A pattern-matching call blocker for Android. Block incoming calls by number
patterns — wildcards, prefixes, and rules — using the system
`CallScreeningService`. No ads, no tracking, fully offline.

> The name comes from **glob** patterns: you describe the numbers you want
> gone, Globber screens every incoming call against your rules.

## Features

- **Pattern-based rules** — match numbers by prefix, suffix, or wildcard
  patterns, not just exact blacklists.
- **System call screening** — uses Android's `CallScreeningService`, so blocked
  calls are rejected silently before they ring.
- **Contacts-aware** — optionally allow known contacts through regardless of
  rules.
- **Block log** — review every screened call.
- **Dark neon-lime bento UI** with a custom icon set.
- **Private** — runs entirely on-device. No network permission, no analytics.

## Requirements

- Android 10 (API 29) or newer
- App must be set as the device's call-screening app

## Permissions

| Permission | Why |
|------------|-----|
| `READ_CONTACTS` | Allow calls from known contacts (optional) |
| `POST_NOTIFICATIONS` | Notify when a call is blocked |

## Build

```bash
git clone https://github.com/salahu01/call-blocker.git
cd call-blocker
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release-unsigned.apk`

> Release APK is unsigned. Sign with your own keystore before installing:
> ```bash
> apksigner sign --ks <your-keystore> app-release-unsigned.apk
> ```

## Tech

- Kotlin · Jetpack Compose
- Room (block rules + log persistence)
- `CallScreeningService` for call interception
- `minSdk 29` · `targetSdk 35`

## License

[MIT](LICENSE) © 2026 salahu01
