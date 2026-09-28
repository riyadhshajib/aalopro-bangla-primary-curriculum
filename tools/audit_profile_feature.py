#!/usr/bin/env python3
"""Static acceptance audit for guardian and child profile functionality.

This utility is intentionally read-only. It lets the profile implementation
change independently, then reports which release contracts have source-level
evidence and which still require implementation or device testing.
"""

from __future__ import annotations

import json
import re
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
ONBOARDING = ROOT / "app/src/main/java/com/example/ui/screens/AaloOnboardingScreen.kt"
VIEW_MODEL = ROOT / "app/src/main/java/com/example/ui/AloViewModel.kt"
MAIN = ROOT / "app/src/main/java/com/example/MainActivity.kt"
ANALYTICS = ROOT / "app/src/main/java/com/example/analytics/AaloAnalytics.kt"
MANIFEST = ROOT / "app/src/main/AndroidManifest.xml"
RULES = ROOT / "firestore.rules"
PROFILE_MODELS = ROOT / "app/src/main/java/com/example/data/profile/ProfileModels.kt"
PROFILE_REPOSITORY = ROOT / "app/src/main/java/com/example/data/profile/SharedPreferencesProfileRepository.kt"
PROFILE_SYNC = ROOT / "app/src/main/java/com/example/data/profile/FirebaseProfileSyncGateway.kt"
PROFILE_SYNC_COORDINATOR = ROOT / "app/src/main/java/com/example/data/profile/ProfileSyncCoordinator.kt"
OUT_DIR = ROOT / "build/reports/profile"


def line_of(text: str, needle: str) -> int | None:
    for number, line in enumerate(text.splitlines(), 1):
        if needle in line:
            return number
    return None


def evidence(path: Path, text: str, needle: str) -> str:
    line = line_of(text, needle)
    return f"{path.relative_to(ROOT).as_posix()}:{line}" if line else "not found"


def main() -> None:
    sources = {path: path.read_text(encoding="utf-8") if path.exists() else "" for path in
               (ONBOARDING, VIEW_MODEL, MAIN, ANALYTICS, MANIFEST, RULES,
                PROFILE_MODELS, PROFILE_REPOSITORY, PROFILE_SYNC, PROFILE_SYNC_COORDINATOR)}
    onboarding, vm, main, analytics, manifest, rules = (sources[p] for p in
        (ONBOARDING, VIEW_MODEL, MAIN, ANALYTICS, MANIFEST, RULES))
    models, repository, profile_sync = (sources[p] for p in
        (PROFILE_MODELS, PROFILE_REPOSITORY, PROFILE_SYNC))
    sync_coordinator = sources[PROFILE_SYNC_COORDINATOR]
    profile_sources = onboarding + vm + models + repository + profile_sync + sync_coordinator

    checks = []
    def add(identifier, area, passed, requirement, proof, next_step=""):
        checks.append({"id": identifier, "area": area, "status": "PASS" if passed else "GAP",
                       "requirement": requirement, "evidence": proof, "next_step": next_step})

    add("PF-01", "guardian+child", "ParentProfile" in models and "ChildProfile" in models,
        "Model separate guardian and child profiles.", evidence(PROFILE_MODELS, models, "data class ParentProfile"),
        "")
    add("PF-02", "validation", "validateChild" in models and "child_name_length" in models and "class_out_of_range" in models,
        "Reject empty/short child names and unsupported class values.",
        evidence(PROFILE_MODELS, models, "fun validateChild"),
        "Keep Bangladesh phone validation in the UI or add it to the profile domain model.")
    add("PF-03", "local restore", "SharedPreferencesProfileRepository" in repository and "loadOrMigrate" in repository,
        "Restore a completed local profile after process death.", evidence(PROFILE_REPOSITORY, repository, "loadOrMigrate"),
        "Add process-recreation tests for every new profile field.")
    add("PF-04", "rotation", "rememberSaveable" in onboarding and not (
            "rememberSaveable { mutableStateOf(false) }" in onboarding and "addOnSuccessListener" in onboarding),
        "Rotation during save must not leave a permanently busy screen or duplicate a write.",
        evidence(ONBOARDING, onboarding, "var busy by rememberSaveable"),
        "Move registration state and Firebase work to a ViewModel or repository with an idempotent request state.")
    add("PF-05", "offline", "SharedPreferencesProfileRepository" in repository and any(token in profile_sources for token in ("pending_sync", "WorkManager", "enqueueProfile")),
        "A valid profile can be created offline and sync later without blocking learning.",
        evidence(ONBOARDING, onboarding, "addOnFailureListener"),
        "Persist locally first, mark pending sync and retry when connected.")
    offline_fallback = any(token in profile_sources for token in ("pending_sync", "WorkManager", "enqueueProfile", "continueOffline"))
    add("PF-06", "Firebase failure", offline_fallback,
        "Firebase failure has a safe retry or offline continuation path.",
        evidence(ONBOARDING, onboarding, "addOnFailureListener"),
        "Do not make remote write success the only path into the app.")
    add("PF-07", "privacy", 'android:allowBackup="false"' in manifest and
        "request.auth.uid == userId" in rules and "hasOnly" in rules,
        "PII is excluded from Android backup and Firestore is owner-scoped with a field allowlist.",
        f"{evidence(MANIFEST, manifest, 'android:allowBackup=')}; {evidence(RULES, rules, 'request.auth.uid == userId')}")
    add("PF-08", "consent", any(token in onboarding for token in ("Checkbox(", "consentAccepted", "privacyAccepted")),
        "Guardian gives explicit, recorded consent before a child's PII is uploaded.",
        evidence(ONBOARDING, onboarding, "Checkbox("),
        "Add an unchecked consent control and store consent version/time.")
    pii_in_analytics = bool(re.search(r'AaloAnalytics\.(?:event|screen)\([^\n]*(?:childName|guardianPhone|safeName|phone)', onboarding + vm))
    add("PF-09", "analytics", not pii_in_analytics and "Never pass names" in analytics,
        "Names and phone numbers are never sent to Analytics.", evidence(ANALYTICS, analytics, "Never pass names"))
    add("PF-10", "migration", "clearLocalStudyDataForNewProfile()" not in vm,
        "An app update or profile migration does not silently erase existing learning data.",
        evidence(VIEW_MODEL, vm, "clearLocalStudyDataForNewProfile"),
        "Use a versioned, one-time migration and ask before destructive profile replacement.")
    multiple_ready = "children: List<ChildProfile>" in models and "activeChildId" in models and "addChild" in repository
    add("PF-11", "multiple children", multiple_ready,
        "Schema supports multiple children under one guardian and an explicit active child.",
        evidence(PROFILE_MODELS, models, "children: List<ChildProfile>"),
        "")
    profile_nav = any(token in main for token in ("ProfileScreen", "AloTab.PROFILE", "onProfile"))
    add("PF-12", "navigation", profile_nav,
        "Guardian can reopen profile management from normal app navigation.",
        evidence(MAIN, main, "AloTopBar("), "Add a profile entry from the top bar and a reliable Home path back.")
    verification_claim = any(claim in onboarding for claim in
        ("OTP যাচাই হয়েছে", "নম্বর যাচাই হয়েছে", "phoneVerified\" to true"))
    phone_truthful = not verification_claim and (
        '"phoneVerified" to false' in onboarding or "guardianPhone" not in profile_sync)
    add("PF-13", "phone truthfulness", phone_truthful,
        "An unverified phone is stored as unverified and the UI does not claim OTP verification.",
        evidence(ONBOARDING, onboarding, '"phoneVerified" to false'))
    new_sync_shape = all(token in profile_sync for token in ('"profileSchemaVersion"', '"activeChildId"', '"children"'))
    rules_accept_new_shape = all(token in rules for token in ("profileSchemaVersion", "activeChildId", "children"))
    add("PF-14", "Firebase contract", not new_sync_shape or rules_accept_new_shape,
        "Firestore rules accept exactly the fields emitted by the profile sync gateway.",
        f"{evidence(PROFILE_SYNC, profile_sync, 'profileSchemaVersion')}; {evidence(RULES, rules, 'hasOnly')}",
        "" if (not new_sync_shape or rules_accept_new_shape) else
        "Update and emulator-test the owner-scoped Firestore allowlist before enabling profile sync.")

    OUT_DIR.mkdir(parents=True, exist_ok=True)
    (OUT_DIR / "profile-acceptance-results.json").write_text(
        json.dumps({"checks": checks}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    passed = sum(item["status"] == "PASS" for item in checks)
    lines = ["# Guardian and child profile acceptance audit", "",
             f"Source-level result: **{passed}/{len(checks)} passed**. Device and Firebase-emulator tests remain separate release gates.", "",
             "| ID | Area | Status | Requirement | Evidence / next action |", "|---|---|---|---|---|"]
    for item in checks:
        detail = item["evidence"] + (("; " + item["next_step"]) if item["next_step"] else "")
        lines.append(f"| {item['id']} | {item['area']} | {item['status']} | {item['requirement']} | {detail} |")
    lines += ["", "## Required dynamic release tests", "",
              "1. Rotate during every form step and during an in-flight Firebase request; verify one write and recoverable UI state.",
              "2. Kill the process after local save, before remote sync and after remote sync; reopen and verify the same active child.",
              "3. Test airplane mode, DNS failure, denied anonymous auth, denied Firestore write and retry after reconnection.",
              "4. Upgrade from the previous single-name preference fixture; verify no chats, notes, points or completed topics are erased without consent.",
              "5. Create two children under one guardian fixture; switch active child and verify progress and content context never leak across children.",
              "6. Inspect Firebase Analytics DebugView and Firestore emulator logs; assert no name, phone, note, question or answer appears in analytics.",
              "7. From curriculum, chat, quiz, notes and guardian studio, open profile and return Home without stale child or audience state."]
    (OUT_DIR / "profile-acceptance-audit.md").write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"profile_acceptance={passed}/{len(checks)}")
    print(OUT_DIR / "profile-acceptance-audit.md")


if __name__ == "__main__":
    main()
