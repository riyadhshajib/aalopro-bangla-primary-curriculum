# Play Console declaration pack — 2026-10-27

App: AaloPro Bangla Primary. Package: com.aalopro.bd.primary. This pack describes the current native release candidate, not the separate Flutter AaloApp. Do not submit until the release evidence and remaining gates below are resolved.

## Suggested entries supported by source
- Ads: No advertising UI/SDK identified. AD_ID and AdServices identifier permissions are removed. Confirm merged release manifest.
- App access: Core local curriculum and quiz do not require paid membership. Guardian onboarding is required. Firebase anonymous auth/profile sync and cloud tutor depend on backend configuration; provide reviewers with steps, not invented credentials.
- Audience: Primary-school children and their guardians/teachers. Review 6–8 and 9–12 selections against actual intended ages; do not select adult-only to evade Families requirements. Final audience choices belong to publisher.
- Payments: No paid access/subscription flow identified in this review.
- Government affiliation: Educational app; do not imply NCTB/government endorsement. Publisher must confirm affiliation answer and textbook distribution rights.
- Content rating: Complete IARC truthfully. No numeric rating is pre-certified. Online general tutor is dynamic AI content; safety/reporting must be verified.
- AI: Quiz adaptive estimation and authored explanations are local. Open-question fallback calls Firebase AI Logic/Gemini and sends the question plus up to four prior exchanges. The entire app must NOT be described as completely offline or as generating all explanations locally.
- Analytics: firebase_analytics_collection_enabled=false in manifest. Do not describe analytics as active without a runtime override. SDK presence alone does not establish exact collection; final network/SDK disclosure review remains required.
- Version: source currently versionCode 6/versionName 1.4.1. Confirm Play Console has not already consumed versionCode 6 before upload; installed or uploaded app identity/upload certificate must match.

## Data safety inventory
| Data | Current handling | Proposed purpose / submission check |
|---|---|---|
| Guardian name and phone | Local, then authenticated Firestore users document | App functionality/account management; required by onboarding |
| Child display name, class, avatar, language | Local and Firestore child documents | Personalisation/app functionality |
| Religion-subject preference | Local and Firestore | Treat as potentially Religious or philosophical beliefs; do not omit merely because it selects a school subject. Confirm optionality and prominent disclosure |
| Questions and recent chat text | Sent to Firebase AI Logic for online fallback | User-generated content/app functionality; cloud retention and service-provider terms must be checked |
| Notes/chat history | Local Room database; selected recent chat also enters online prompt | Do not label all chat local-only |
| Adaptive answers, estimates, completed quiz count | Local SharedPreferences per child/class | No new cloud upload added by adaptive feature |
| Auth/installation/integrity identifiers and network metadata | Firebase Auth, Firestore, App Check/Play Integrity SDKs | Account management/security; reconcile with current SDK disclosure guidance |

HTTPS is used by the Firebase services and cleartext is disabled. This is not evidence of encryption at rest on every service. Do not mark all collection ephemeral. Do not answer 'No data collected'. Sharing exemptions for service-provider processing need validation against provider terms; they do not erase the collection disclosure.

## Concrete remaining publication gates
1. Public policy exists at https://aalopro.com/privacy/ (verified 27 October 2026), with publisher AALO PRO PTE LTD and help@aalopro.com. Reconcile this build-specific inventory with that policy and add visible in-app access; no in-app link was found in this bounded source check.
2. Online generative responses need an in-app reporting path that reaches the developer. No such path was found in this check. Emailing outside the app alone is insufficient.
3. Anonymous cloud profile/account lifecycle and deletion must be resolved. Child-document removal exists, but this is not a proven full account/data deletion flow or public deletion-request route.
4. Confirm cloud rules deployed, Firebase App Check release certificate/provider configured, AI service availability, and child data/consent handling against the exact production service.
5. Confirm Play signing identity, unused version code, listing assets, truthful age groups, IARC result and account-specific testing requirement in authenticated Play Console.
6. Physical phone test and Play internal-testing/pre-launch report. Passing local checks is not production approval.

## Official references checked
- https://support.google.com/googleplay/android-developer/answer/9867159
- https://support.google.com/googleplay/android-developer/answer/10144311
- https://support.google.com/googleplay/android-developer/answer/13327111
- https://support.google.com/googleplay/android-developer/answer/13985936
- https://developer.android.com/guide/practices/page-sizes

Target API reference: https://support.google.com/googleplay/android-developer/answer/11926878 (current mobile submission requirement API 36 from 31 August 2026). The source targets API 36; final manifest verification is separate.


## Submission order
1. Resolve the publication gates above and verify the release APK on the physical phone.
2. In the existing Play Console app, confirm package, upload certificate and unused version code. Do not create a second listing accidentally.
3. Upload the exact verified signed AAB to internal testing. The APK is for local testing, not the Play upload artifact.
4. Complete App content: privacy policy, ads, app access, target audience/Families, content rating and Data safety from this inventory. Do not copy unverified suggested entries blindly.
5. Provide current screenshots, app icon, feature graphic, short/full description and working support contact. Avoid claims of complete textbook coverage or fully offline generative answers unless evidenced.
6. Install through the internal-test Play link and check Play signing/App Check, download, cold start, offline use and profile isolation. Review the pre-launch report.
7. Complete any account-specific closed-testing/production-access requirements shown by Play Console.
8. Submit for production review only after these gates pass. Google review outcome and timing are not guaranteed.
