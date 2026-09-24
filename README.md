# AaloPro Bangla Android app

Android Studio project for the Class 1–5 learning app.

## Current answer modes

- Offline lessons, calculations, quizzes, and a limited local fact reference work without a connection.
- The local reference is rule based. Despite the legacy `AloQwenEngine` class name, this project does **not** package or run Qwen model weights.
- Open-ended cloud answers currently require Gemini API configuration. The repository contains no live key or Firebase project configuration, so a downloaded APK cannot currently provide open-ended online answers.
- Do not put a Gemini API key in a public release APK. Google recommends a server proxy or Firebase AI Logic with App Check for mobile apps.

## Build locally

Open this directory in Android Studio and run the `app` configuration. The checked-in `.env.example` is a placeholder for development only. A public release needs a secure managed AI connection and release signing.

## Curriculum audit

The official NCTB 2026 primary textbook PDFs were downloaded into the workspace under `tmp/pdfs/nctb2026`. The app's `Nctb2026Catalog` contains verified chapter names for a growing subset of books; `catalogOnly` entries are navigation records, not a claim that every exercise has an offline verified answer. See the test suite before treating an APK as ready for learners.
