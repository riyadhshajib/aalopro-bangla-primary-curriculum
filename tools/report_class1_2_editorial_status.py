#!/usr/bin/env python3
"""Write concise, evidence-backed Class 1 and 2 editorial status reports."""

from __future__ import annotations

import json
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
REPORTS = ROOT / "build/reports/curriculum"
AUDIT_OUT = ROOT.parents[1] / "outputs/curriculum-audit-2026-09-26"


def main() -> None:
    ledger = json.loads((AUDIT_OUT / "class-1-2-exercise-ledger.json").read_text(encoding="utf-8"))
    evidence = {
        grade: json.loads((REPORTS / f"agent-class{grade}/source-evidence-validation.json").read_text(encoding="utf-8"))
        for grade in (1, 2)
    }
    for grade in (1, 2):
        summaries = [row for row in ledger["summary"] if row["grade"] == grade]
        payload = {
            "grade": grade,
            "verificationRule": "Only visually reviewed source pages and second-pass complete editorial records count as verified. OCR-only text is excluded.",
            "sourceEvidence": evidence[grade],
            "subjects": summaries,
            "totals": {
                "pages": sum(row["pages"] for row in summaries),
                "verifiedExercisePages": sum(row["verified_exercise_pages"] for row in summaries),
                "verifiedExercises": sum(row["verified_exercises"] for row in summaries),
                "trueExerciseGapPages": sum(row["true_exercise_gap_pages"] for row in summaries),
                "qualifiedVisualReviewPages": sum(row["qualified_visual_review_pages"] for row in summaries),
                "nonExercisePages": sum(row["non_exercise_pages"] for row in summaries),
            },
        }
        target = REPORTS / f"agent-class{grade}/editorial-status-current.json"
        target.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")

        lines = [
            f"# Class {grade} editorial status",
            "",
            "Only visually reviewed source pages and second-pass complete editorial records count as verified. OCR-only text is excluded.",
            "",
            f"- Source page evidence: {evidence[grade]['sourceEvidencePresent']}/{evidence[grade]['expectedPageRows']} present",
            f"- Verified exercise pages: {payload['totals']['verifiedExercisePages']}",
            f"- Verified exercises: {payload['totals']['verifiedExercises']}",
            f"- Known exercise pages still requiring editorial capture: {payload['totals']['trueExerciseGapPages']}",
            f"- Pages still requiring qualified visual classification: {payload['totals']['qualifiedVisualReviewPages']}",
            f"- Reviewed non-exercise/front-matter pages: {payload['totals']['nonExercisePages']}",
            "",
            "## Subject breakdown",
            "",
        ]
        for row in summaries:
            lines.extend([
                f"### {row['subject'].title()}",
                "",
                f"- Book pages: {row['pages']}",
                f"- Verified exercise pages: {row['verified_exercise_pages']}",
                f"- Verified exercises: {row['verified_exercises']}",
                f"- Known exercise gaps: {row['true_exercise_gap_pages']}",
                f"- Qualified visual review pending: {row['qualified_visual_review_pages']}",
                "",
            ])
        lines.extend([
            "## Completion gate",
            "",
            "A remaining page can close only after visual source review, exact exercise capture, source-grounded answer, age-appropriate explanation, Bangla/Banglish/English follow-up, correct mapping, and deterministic validation.",
            "",
        ])
        (REPORTS / f"agent-class{grade}/EDITORIAL-STATUS-CURRENT.md").write_text("\n".join(lines), encoding="utf-8")
        print(json.dumps(payload, ensure_ascii=False))


if __name__ == "__main__":
    main()
