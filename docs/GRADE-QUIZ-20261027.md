# Class-aware adaptive practice, 2026-10-27

Native Bangla Primary now filters quiz items by active child's class. The subject menu is derived from that class. Switching child/class clears current selection, score, help and queue. Invalid option indexes and advancing before submission are ignored. Completed quiz counts and last scores are saved under child/class keys, not the shared legacy quiz statistics.

The selected practice import contains 19 existing AaloApp exercises: class 1: 2, class 2: 2, class 3: 3, class 4: 6, class 5: 6. This is selected practice, NOT all subjects/chapters or exhaustive textbook quiz coverage. Original legacy questions remain in QuizRepository but are not assigned guessed class labels. Six imported items lack precise textbook references and are explicitly labeled as authored AaloApp practice, not verbatim textbook questions. Remaining source references are retained as provided; this change does not establish a new page-by-page educator review.

Import sources: AaloApp lib/content/demo_content.dart, demo_content_extension.dart and practice_content.dart. One apple question was made self-contained using the supplied saved explanation (6 sold, 8 remaining). No new answers or language model were generated.

Verification includes learner estimates, hints/repeats, five child profiles, restart persistence, class boundaries, actual ViewModel profile switching and the help screen. Final execution results and APK integrity are recorded with the delivered artifact. Physical phone testing remains a separate gate.

Executed 2026-10-27: 16 tests passed, zero failures/errors across GradeQuizFlowTest (3), AdaptivePracticeTest (4), AdaptivePracticeScreenTest (1), ProfileRepositoryTest (5), ProfileAcceptanceRegressionTest (3). Test XML and results.json are retained under build/reports/grade-quiz. No physical-device result is claimed.
