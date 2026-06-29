# Privacy Policy

**Globber** is built to protect your privacy. It runs entirely on your device.

## Summary

- **No data leaves your device.** Globber has no network/internet permission.
- **No analytics, no tracking, no ads, no third-party SDKs.**
- **No accounts.** Nothing to sign up for.

## Data Globber accesses

| Data | Why | Where it goes |
|------|-----|---------------|
| **Contacts** (`READ_CONTACTS`) | Optionally allow calls from people already in your contacts | Read in-memory at call time; never stored, never transmitted |
| **Incoming call numbers** | Matched against your block rules via the system `CallScreeningService` | Processed on-device; a local block log is stored only on your device |
| **Notifications** (`POST_NOTIFICATIONS`) | Tell you when a call was blocked | Stays on-device |

## Data storage

Block rules and the block log are stored in a local database on your device
only. Uninstalling the app removes this data. Globber does not back this data
up to any server.

## Permissions

Globber requests only:

- `READ_CONTACTS` — optional contacts allow-list
- `POST_NOTIFICATIONS` — block notifications

It does **not** request internet, location, microphone, camera, or storage
permissions.

## Changes

This policy may be updated alongside releases. Material changes will be noted in
[CHANGELOG.md](CHANGELOG.md).

## Contact

Questions? Email **cvsalahu12@gmail.com** or open an issue.
