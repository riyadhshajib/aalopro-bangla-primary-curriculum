# AaloPro Master Product Map

This is the authoritative product context for every AaloPro implementation task. Conversation history is supporting context, not the source of truth.

## Product identity

- Product: AaloPro Bangla Primary
- Entry audience: Bangladesh primary learners in Classes 1–5 and their families, especially users of low-cost Android devices and unreliable internet
- Core promise: a curriculum-aligned, offline AI-assisted tutoring experience grounded in verified bundled learning content
- Supported baseline: Android 7+ and a practical target of 4 GB RAM
- Primary language: plain, conversational Bangla; Banglish and English commands are accepted where supported

## Core experience

1. Select class, then subject, then chapter/topic.
2. Learn from bundled curriculum content without internet.
3. Ask curriculum questions and follow-ups in Bangla, Banglish, or English.
4. Receive source-grounded answers, explanations, quizzes, notes, and fixed parent/teacher guidance offline.
5. With internet, ask general or new questions through the managed online AI path.

## Non-negotiable rules

- A curriculum answer must not be invented by a general language model.
- Direct textbook questions must map to traceable source content.
- Generated or creative follow-ups must be clearly separated from directly verified answers.
- Missing verified curriculum content must be reported as missing, not presented as complete.
- General online AI is not described as offline.
- No personal question text, child name, phone, payment data, or private note content is sent as analytics.
- Signing keys, secrets, tokens, and credentials never enter source control or public APK resources.
- Portrait is the default app orientation unless a documented screen has a strong educational reason to differ.
- No existing functionality may disappear during a visual redesign without an explicit recorded decision.

## Design system

- Visual character: clear, premium, warm, playful, child-friendly without becoming visually noisy
- Palette: cream/ivory base with teal, warm yellow, soft green, and carefully varied supporting card colors
- Depth: restrained soft 3D shadows and layered surfaces; no heavy black outlines or cheap-looking nested boxes
- Typography: bold rounded geometric `AaloPro` wordmark; readable Bangla body; a distinct, legible numeric font where Bangla numerals become ambiguous
- Information density: progressive disclosure—class → subject → chapter/topic; avoid filling the full page with all choices
- Accessibility: responsive text, sufficient contrast, tap targets, portrait layouts, and small-device validation
- Illustration: relevant classroom, quiz, note, tutor, teacher, and parent cartoons; decorations must support comprehension

## Offline and online boundary

### Offline

- bundled class/subject/topic navigation;
- verified curriculum answers and explanations;
- supported follow-up patterns;
- quizzes and formula/reference content;
- personal notes stored locally;
- fixed parent/teacher guides;
- deterministic Bangla/Banglish command routing.

### Online

- general, random, or new questions outside bundled verified scope;
- Firebase AI Logic/Gemini path protected by the release security configuration;
- privacy-safe Firebase Analytics and reliability telemetry.

When internet is unavailable for an online-only question, the app must explain in simple Bangla that internet is required. It must not fail silently.

## Needle/on-device model boundary

Needle is treated as a small intent and tool-routing model, not as the source of curriculum truth. It may route a command to a lesson, quiz, note, or guide only after the benchmark and device gates pass. Deterministic routing remains authoritative until then. Needle cannot overwrite verified curriculum content.

Current recorded benchmark: Needle 3 did not meet the production routing gate and therefore remains disabled as an automatic production controller.

## Content model

Every verified curriculum unit should be able to carry:

- source class, subject, book, chapter/topic, page or source locator;
- lesson explanation in simple language;
- every direct exercise/question available in the defined source scope;
- verified answer;
- age-appropriate explanation steps;
- anticipated creative or related questions, clearly labeled;
- Bangla/Banglish/English follow-up intents;
- quiz explanation and remediation;
- parent/teacher support where relevant;
- image reference and image-based activity instructions when educationally necessary.

Image-led mathematical and geometry learning remains primarily in Mathematics. In other subjects, textbook images may be included when they are necessary to understand or answer the textbook task.

## Success gates

- Defined curriculum scope coverage: 100%
- Human-verified direct-answer accuracy: at least 99%
- Follow-up relevance: at least 95%
- Release checklist: 100%
- Critical/high-severity open defects: 0
- Crash-free target during pilot: at least 99.5%
- Real-device matrix: Android 7+ and agreed low-cost device profiles

These are release gates for the defined scope, not a claim of universal perfection.

## Product truth today

- The UI and main product flow are substantially implemented.
- Offline curriculum structure exists but complete book-by-book verification is unfinished.
- Quiz, notes, parent/teacher guide, online AI, and Firebase metrics exist in varying degrees of maturity.
- Legacy APK artifacts exist, but a newly registered release must carry commit, hash, tests, content gaps, and known limitations.
- Needle fine-tuning data and benchmark tooling exist; production activation remains gated.

## Change control

Every accepted change updates at least one of:

- this Master Product Map for product truth;
- `VARIANT_MATRIX.md` for configuration differences;
- `RELEASE_REGISTRY.md` for a distributed artifact;
- an audit report for evidence and gaps.

