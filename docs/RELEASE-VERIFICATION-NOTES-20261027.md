# Release verification notes, 27 October 2026

The release lint tool initially failed while parsing ReviewedBrowserAdditions.kt (about 7 MB). No baseline, disabled check or abortOnError=false was introduced. The fresh run passed with idea.max.intellisense.filesize=65536 and idea.max.content.load.filesize=65536 supplied to the build JVM through JAVA_TOOL_OPTIONS. The lintAnalyzeRelease task was rerun explicitly because the previous failure was cached.

Fresh lint result: zero errors, 230 warnings. Retained XML and grouped warning counts are in the release verification evidence. The warnings include drawable placement/duplicates, available dependency updates, unused resources, portrait/tablet behaviour, missing ChromeOS x86_64 support and synchronous preferences. These are not a clean accessibility/device audit. API 36 is the configured submission target; an OldTargetApi lint warning does not by itself establish a rejected upload.

The signing credential was loaded only for the local build and was not included in release documents or source delivery. Final artifact signature, manifest, alignment and bundle checks must be read alongside this note, not inferred from lint success.
