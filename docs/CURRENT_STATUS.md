# AaloPro current engineering status

Last checked: 2026-09-25 (Asia/Singapore)

## Confirmed now

- Canonical Android source is now under local Git version control.
- Baseline commit: `dcbd3e8` (`Establish AaloPro canonical baseline and release tracking`).
- Master Product Map, Variant Matrix, release process, release registry, and artifact-registration script are present.
- Debug unit-test report contains 71 tests across seven suites with 0 failures and 0 errors.
- Gradle `assembleDebug` reached an idle successful/up-to-date state; the preserved debug APK is 197.58 MB and dated 2026-09-21.
- Existing release APK is 134.88 MB and dated 2026-09-21.
- A 608-example Needle fine-tuning dataset and benchmark outputs are present.
- The ten-day Codex automation is active for the 10th, 20th, and final day of each month.
- A reusable fast curriculum-structure audit now produces Markdown and CSV reports without consuming model tokens. Its first run found 154 literal curriculum-topic declarations, 41 generated chapter rows, 132 generated lesson rows, 161 literal `TopicQA` entries, but only 24 literal source-reference declarations. These source-level counts are prioritization signals, not completeness or accuracy proof.
- A PDF/asset manifest now confirms all 33 expected source-book slots are available. Twelve slots do not yet have bundled page assets, and 29 PDFs require OCR or visual extraction rather than reliable embedded-text extraction.
- The verified-answer audit confirms 12 human-reviewed question-answer entries, all currently in Class 2 Bangla lesson 18. Another 382 OCR candidates are quarantined from user-facing answers until editorial review.
- Generic page guidance can no longer be returned or labelled as a verified textbook answer. Only reviewed page questions or the existing manually reviewed drawable exercises pass that route.
- The refreshed debug build completed successfully on 2026-09-25. The APK is 197.73 MB. All 71 automated unit/Robolectric tests pass with 0 failures.

## Not yet proven complete

- Book-by-book, class-by-class, subject-by-subject curriculum coverage and source verification
- Human verification of every direct answer and explanation
- At least 95% follow-up relevance on an accepted evaluation set
- Complete offline quiz-remediation and parent/teacher-guide audit
- Current real-device matrix across Android 7+ and agreed 4 GB/low-cost devices
- Full online random-question test across tester installations
- Firebase event arrival and privacy audit against production dashboards
- Dependency, secret, App Check, signing, obfuscation, backup, and Play release security audit
- Production AAB and Play Console pre-launch checks
- Needle fine-tuning result meeting the production routing gate

## Immediate next execution order

1. Editorially review the quarantined OCR queue book by book, beginning with core Class 1 and Class 2 subjects, and attach a page/source reference to every accepted answer.
2. Build a source-traceable answer/follow-up evaluation set from the accepted entries and require at least 95% relevance before claiming coverage.
3. Add the still-missing religious-book assets/content for Classes 3–5 without inflating the APK with pages that are not needed in the learning flow.
4. Perform the real-device matrix pass on Android 7+ and agreed low-cost/4 GB devices.
5. Verify online random-answer delivery, Firebase event arrival/privacy, App Check, release signing/obfuscation and Play delivery.
6. Register a new test artifact only when evidence and limitations are attached.

## Usage-saving policy

- Inspect once, edit in batches, and run targeted tests before the full suite.
- Do not rebuild an APK for copy or cosmetic changes until the feedback batch closes.
- Reuse deterministic scripts and stored audit reports instead of asking the model to re-read all textbooks or repeat the same reasoning.
- Keep Needle work separate from curriculum truth so a failed experiment does not trigger a full app rebuild.
- Notify only on completion, failure, or required user/device action.
