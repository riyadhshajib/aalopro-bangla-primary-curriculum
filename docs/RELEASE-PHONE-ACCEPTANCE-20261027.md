# Release candidate phone acceptance

Use the APK set generated from the exact signed AAB, not the debug APK. Upload key signing can differ from Play App Signing; Play internal testing is needed for the final delivered signing identity and App Check.

1. Cold start and guardian onboarding; verify disclosure and consent before cloud sync.
2. Create children in classes 1, 3 and 5. Confirm class/subject quiz filtering and profile switching.
3. Give one wrong answer, request help on another, repeat one. Check independent progress is not inflated.
4. Complete a quiz; switch child; force-stop/reopen. Check isolation and persistence.
5. Airplane mode: books, saved answers, quiz, notes and profile must remain usable. Online tutor must fail honestly without an endless spinner.
6. Online: test auth/profile sync and App Check through Play internal testing; verify child deletion behavior and cloud data separately.
7. Check small-screen layout, Bengali text, keyboard, TTS availability, back navigation, rotation policy and accessibility.
8. Check cold start/memory on low-memory Android and 16 KB Android environment. Static alignment is not runtime proof.
9. Check content report and privacy/deletion paths after those publication blockers are implemented.

Do not uninstall an existing app to solve a signature conflict without explicit permission, because local learning data would be lost.
