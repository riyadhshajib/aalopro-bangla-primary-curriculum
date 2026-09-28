#!/usr/bin/env python3
"""Deterministically separate proven exercise gaps from non-exercise pages.

This classifier is intentionally conservative. It never promotes OCR or a visual
guess to verified curriculum content. Class 1 uses the completed visual
classification matrix. Class 2 treats reviewed batches as exercises, known
front matter as non-exercise, nearly blank rendered pages as non-exercise, and
keeps every other page in qualified visual review.
"""

from __future__ import annotations

import csv
import json
from pathlib import Path

import fitz
import numpy as np


ROOT = Path(__file__).resolve().parents[1]
REPORTS = ROOT / "build/reports/curriculum"
OUT = ROOT.parents[1] / "outputs/curriculum-audit-2026-09-26"
PDFS = {
    (2, "bangla"): Path(r"C:\Users\riyad\Downloads\Class-2, Bangla_compressed.pdf"),
    (2, "english"): Path(r"C:\Users\riyad\Downloads\english_compressed.pdf"),
    (2, "math"): Path(r"C:\Users\riyad\Downloads\Class-2, Math_compressed.pdf"),
}


def ink_ratio(doc: fitz.Document, page_number: int) -> float:
    pix = doc[page_number - 1].get_pixmap(matrix=fitz.Matrix(0.35, 0.35), colorspace=fitz.csGRAY, alpha=False)
    arr = np.frombuffer(pix.samples, dtype=np.uint8)
    return float(np.mean(arr < 242))


def complete_flags(exercises: list[dict]) -> dict[str, bool]:
    if not exercises:
        return {"captured": False, "answered": False, "explained": False, "followUpTested": False}
    return {
        "captured": all(str(x.get("printedQuestion", "")).strip() for x in exercises),
        "answered": all(str(x.get("directAnswer", "")).strip() for x in exercises),
        "explained": all(str(x.get("explanation", "")).strip() for x in exercises),
        "followUpTested": all(
            str(x.get(field, "")).strip()
            for x in exercises
            for field in ("followUpBangla", "followUpBanglish", "followUpEnglish")
        ),
    }


def main() -> None:
    inventory = json.loads((OUT / "class-1-2-page-inventory.json").read_text(encoding="utf-8"))
    class1 = {
        (r["subject"], int(r["sourcePage"])): r
        for r in json.loads((REPORTS / "agent-class1/class1-page-by-page-zero-gap-matrix.json").read_text(encoding="utf-8"))
    }
    docs = {key: fitz.open(path) for key, path in PDFS.items()}
    rows = []
    for page in inventory["pages"]:
        grade, subject, number = page["grade"], page["subject"], page["pdfPage"]
        exercises = page["exercises"]
        flags = complete_flags(exercises)
        ratio = None
        if grade == 1:
            evidence = class1[(subject, number)]
            if evidence["classification"] == "non_exercise":
                classification, state, confidence = "non_exercise", "not_applicable", "source_reviewed"
            elif page["auditStatus"] == "verified":
                classification, state, confidence = "exercise", "verified_complete", "source_reviewed"
            else:
                classification, state, confidence = "exercise", "true_exercise_gap", "source_reviewed"
        elif page["auditStatus"] == "verified":
            classification, state, confidence = "exercise", "verified_complete", "source_reviewed"
        elif number <= 7:
            classification, state, confidence = "non_exercise", "front_matter", "deterministic_page_range"
        else:
            ratio = ink_ratio(docs[(grade, subject)], number)
            if ratio < 0.004:
                classification, state, confidence = "non_exercise", "near_blank", "deterministic_pixel_threshold"
            else:
                classification, state, confidence = "unresolved", "qualified_visual_review", "conservative"
        rows.append({
            "grade": grade,
            "subject": subject,
            "pdfPage": number,
            "classification": classification,
            "verificationState": state,
            "confidenceBasis": confidence,
            "inkRatio": None if ratio is None else round(ratio, 6),
            "exerciseCount": len(exercises),
            **flags,
            "exerciseIds": [x.get("id", "") for x in exercises],
        })
    for doc in docs.values():
        doc.close()

    summary = []
    for grade in (1, 2):
        for subject in ("bangla", "english", "math"):
            subset = [r for r in rows if r["grade"] == grade and r["subject"] == subject]
            summary.append({
                "grade": grade,
                "subject": subject,
                "pages": len(subset),
                "verified_exercise_pages": sum(r["verificationState"] == "verified_complete" for r in subset),
                "verified_exercises": sum(r["exerciseCount"] for r in subset),
                "true_exercise_gap_pages": sum(r["verificationState"] == "true_exercise_gap" for r in subset),
                "non_exercise_pages": sum(r["classification"] == "non_exercise" for r in subset),
                "qualified_visual_review_pages": sum(r["verificationState"] == "qualified_visual_review" for r in subset),
            })
    payload = {
        "method": "Conservative deterministic classification. No OCR result is promoted to verified content.",
        "summary": summary,
        "pages": rows,
    }
    (OUT / "class-1-2-exercise-ledger.json").write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
    with (OUT / "class-1-2-exercise-summary.csv").open("w", encoding="utf-8-sig", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=summary[0].keys())
        writer.writeheader()
        writer.writerows(summary)
    validation = {
        "pageRows": len(rows),
        "expectedPageRows": 627,
        "verifiedPagesComplete": sum(
            r["verificationState"] == "verified_complete" and all(r[k] for k in ("captured", "answered", "explained", "followUpTested"))
            for r in rows
        ),
        "verifiedPagesWithMissingStage": [
            {"grade": r["grade"], "subject": r["subject"], "page": r["pdfPage"]}
            for r in rows
            if r["verificationState"] == "verified_complete" and not all(r[k] for k in ("captured", "answered", "explained", "followUpTested"))
        ],
    }
    validation["status"] = "pass" if validation["pageRows"] == 627 and not validation["verifiedPagesWithMissingStage"] else "fail"
    (OUT / "class-1-2-exercise-ledger-validation.json").write_text(json.dumps(validation, ensure_ascii=False, indent=2), encoding="utf-8")
    print(json.dumps({"summary": summary, "validation": validation}, ensure_ascii=False))


if __name__ == "__main__":
    main()
