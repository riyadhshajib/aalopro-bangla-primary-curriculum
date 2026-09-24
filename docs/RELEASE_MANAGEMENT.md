# AaloPro release management

This file is the single operating rule for tracking AaloPro builds.

## One product, one source of truth

- `main` contains the latest accepted AaloPro source.
- A short-lived branch is used for an experiment or a large independent change.
- A release is identified by a Git tag, an APK/AAB, release notes, test evidence, and its SHA-256 hash.
- Tester, demo, and production are build configurations of this project. They must not become independent copied source folders.
- Never commit signing keys, credentials, `.env`, Firebase secrets, APKs, AABs, build output, or personal test data.

## When to use a separate Codex task

Keep product decisions and release coordination in the main AaloPro task. Use a separate task only for a bounded workstream:

1. Curriculum ingestion and textbook verification
2. Needle/on-device routing research
3. Firebase, analytics, and online AI
4. Play Store, security, and release engineering

Every workstream must write its accepted outcome back to `docs/MASTER_PRODUCT_MAP.md` or `docs/RELEASE_REGISTRY.md`. A chat is working context; Git and these documents are the record.

## Version convention

- Product version: `MAJOR.MINOR.PATCH`, for example `1.2.0`
- Android `versionCode`: always increases for every distributed build
- Git tag: `v1.2.0-test.1`, `v1.2.0-demo.1`, or `v1.2.0`
- Artifact name: `AaloPro-<version>-<channel>-<YYYYMMDD>-<short-sha>.apk`

Channels:

- `dev`: internal engineering build
- `test`: external tester build with test-safe services
- `demo`: controlled stakeholder demonstration
- `production`: Play Store release candidate

## Quality gates

A build may be registered but must not be called release-ready unless its registry row links to evidence for:

- successful compile and unit tests;
- defined curriculum-scope coverage;
- direct-answer source traceability and human verification;
- follow-up relevance evaluation;
- quiz, notes, parent/teacher guide, navigation, and orientation flows;
- privacy-safe Firebase events and online-AI behavior;
- Android 7+, supported ABI, low-memory, and responsive UI checks;
- dependency, secret, signing, and release-configuration security checks;
- no unresolved critical or high-severity defect.

Target numbers are not substitutes for evidence: defined scope 100%, direct-answer accuracy at least 99%, follow-up relevance at least 95%, and release checklist 100%.

## Release workflow

1. Consolidate the feedback batch.
2. Record the intended scope before editing.
3. Implement on a branch when the change is risky or experimental.
4. Run the smallest relevant tests first, then the full quality gate once.
5. Build only after tests pass.
6. Run `scripts/register-release.ps1` to copy, rename, hash, and register the artifact.
7. Tag the exact accepted commit.
8. Keep known limitations in the registry; do not hide them.

