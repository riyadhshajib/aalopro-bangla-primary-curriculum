# Privacy policy publication draft

Status: factual working draft for AaloPro Bangla Primary (com.aalopro.bd.primary), 27 October 2026. Do not publish this as final until the public support contact, retention/deletion process, cloud provider configuration and public URL are completed.

AaloPro Bangla Primary provides primary-school learning materials, notes and practice. Local learning features can work without internet. Cloud profile synchronization and online tutor answers require internet.

## Information used
The guardian supplies a name and telephone number. Child profiles contain a display name, class, avatar, language and religion-subject preference. These fields are stored on the phone and synchronized to Firebase when the app can authenticate and connect. The phone number is not OTP-verified. Religion-subject preference is used to select relevant school content; it is not required as a declaration of personal belief.

Questions submitted to the online tutor and up to four recent exchanges are sent through Firebase AI Logic to generate a response. Do not include unnecessary private information in questions. Saved notes and chat history remain in the app's local database, except recent chat included in those online requests.

Adaptive quiz estimates, seen questions and completed quiz results are stored locally by child and class. They are provisional learning estimates, not certified assessments.

Firebase authentication, integrity and service infrastructure may process service identifiers and network metadata. Advertising identifiers and ad-personalisation signals are disabled. Analytics collection is disabled by default in this build. No ads or paid purchase flow was identified in the reviewed source.

## Protection and control
The app disables cleartext traffic and Android backup/device transfer. Firebase services use HTTPS. These controls do not replace careful guardian supervision or device security.

Guardians can edit local profiles and remove child profiles through supported profile controls. A child profile removed during synchronization is removed from the corresponding Firebase child collection. This must not be confused with complete deletion of the anonymous account, all cloud data or provider records.

## Publisher actions required before publication
- Existing public policy verified: https://aalopro.com/privacy/. Publisher: AALO PRO PTE LTD. Support/privacy contact: help@aalopro.com. Use these existing details; this draft is an app-specific reconciliation, not a replacement policy.
- Define and implement the complete cloud-data/account deletion request route, including a public web URL when required.
- Confirm retention periods and exceptions for Firebase and online AI processing; do not invent a number of days.
- Verify that consent/disclosure is shown before the actual transfer, including sensitive preferences and online prompts.
- Reconcile app-specific processing with the existing public policy and expose that policy inside the app.
- Recheck this policy whenever collection, analytics, AI providers or payment features change.
