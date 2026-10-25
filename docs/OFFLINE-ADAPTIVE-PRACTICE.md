# Offline adaptive practice port

The existing native quiz now uses a small Bayesian learner estimator. State is stored only in `aalo_adaptive_local`, keyed by active child ID, class and subject. No additional model or library was added. Existing tutor/model features outside quiz were not removed by this port.

First unassisted answers to distinct quiz IDs update a provisional estimate (prior .2, slip .1, random-choice guess, learning transition .08). Repeated and assisted answers do not inflate independent evidence. New questions are preferred, then subjects with lower estimated knowledge. The remaining queue is re-ranked only between questions so displayed choices cannot change under the learner. Profile changes reset selection, score, help and queue.

The quiz has an explicit help action that shows the original saved explanation before submission. It never generates an explanation. Subject-level estimates are not calibrated assessments or precise misconception diagnoses. The legacy quiz bank does not have class metadata, so this port does not claim class-filtered quiz coverage. Learning estimates remain class-isolated. Existing global legacy statistics and other tutor features are outside this change.

Validation evidence is retained under build/reports/adaptive. Physical-device testing and new APK packaging have not been performed.

Verified 2026-10-25: production/test compilation and five focused Robolectric tests passed (four learner-state tests and one Compose help-screen test), zero failures/skips. Evidence: final-tests.log and TEST-com.example.AdaptivePractice*.xml. This validates the current working tree; unrelated pre-existing edits are excluded from the scoped source commit.
