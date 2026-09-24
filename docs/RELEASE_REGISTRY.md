# AaloPro release registry

This registry tracks distributable artifacts. “Built” does not mean “release-ready.”

| Version | Channel | Date | Commit | Artifact | SHA-256 | Verification | Known limitations |
|---|---|---|---|---|---|---|---|
| 1.2.0 (code 3) | legacy test baseline | 2026-09-21 | source was not under Git | `app-debug.apk` (197.58 MB) | not yet registered | Previous build existed; current full gate not evidenced | Needle controller disabled; curriculum completeness and device matrix not fully evidenced |
| 1.2.0 (code 3) | legacy release artifact | 2026-09-21 | source was not under Git | `app-release.apk` (134.88 MB) | not yet registered | Artifact existed; signing/play-readiness not re-verified | Do not distribute as production until security and release audit passes |

## Current baseline status

- The repository tracking structure is being established after the legacy artifacts were created.
- Existing APKs are preserved, but they are not promoted to a new release merely because they compile.
- Needle 3 remains an experimental guarded fallback and is not a production curriculum-answer controller.
- The next registered test build must include a commit ID, hash, test report, content gap report, and precise limitations.

