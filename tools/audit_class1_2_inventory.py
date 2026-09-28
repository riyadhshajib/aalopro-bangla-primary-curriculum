#!/usr/bin/env python3
"""Build an evidence-first Class 1-2 curriculum inventory.

Only second-pass reviewed editorial artifacts count as verified exercises.
Every PDF page remains visible in the output, including explicit gaps.
"""

from __future__ import annotations

import csv
import json
from collections import defaultdict
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
REPORTS = ROOT / "build/reports/curriculum"
OUT = ROOT.parents[1] / "outputs/curriculum-audit-2026-09-26"
TOTALS = {
    1: {"bangla": 90, "english": 98, "math": 122},
    2: {"bangla": 74, "english": 106, "math": 137},
}


def load_verified() -> tuple[dict[tuple[int, str, int], list[dict]], list[str]]:
    by_page: dict[tuple[int, str, int], list[dict]] = defaultdict(list)
    sources: list[str] = []
    for path in sorted((REPORTS / "agent-class1").glob("class1-consolidated-batch-*-verified.json")):
        sources.append(str(path.relative_to(ROOT)))
        for group in json.loads(path.read_text(encoding="utf-8")):
            if group.get("verificationStatus") != "second_pass_complete":
                continue
            for page in group.get("sourcePages", []):
                for item in group.get("entries", []):
                    by_page[(1, group["subject"], int(page))].append(item)
    for path in sorted((REPORTS / "agent-class2").glob("class2-consolidated-batch-*.json")):
        sources.append(str(path.relative_to(ROOT)))
        data = json.loads(path.read_text(encoding="utf-8"))
        if not str(data.get("verificationStatus", "")).startswith("second_pass_complete"):
            continue
        for group in data.get("books", []):
            for page in group.get("sourcePages", []):
                for item in group.get("entries", []):
                    by_page[(2, group["subject"], int(page))].append(item)
    return by_page, sources


def main() -> None:
    OUT.mkdir(parents=True, exist_ok=True)
    verified, sources = load_verified()
    class1_matrix = {
        (row["subject"], int(row["sourcePage"])): row
        for row in json.loads(
            (REPORTS / "agent-class1/class1-page-by-page-zero-gap-matrix.json").read_text(encoding="utf-8")
        )
    }
    class2_ledger: dict[tuple[str, int], str] = {}
    with (REPORTS / "agent-class2/CLASS2-PAGE-LEDGER.csv").open(encoding="utf-8-sig", newline="") as handle:
        for row in csv.DictReader(handle):
            class2_ledger[(row["subject"], int(row["pdf_page"]))] = row["status"]

    pages = []
    for grade, books in TOTALS.items():
        for subject, total in books.items():
            for page in range(1, total + 1):
                items = verified.get((grade, subject, page), [])
                if grade == 1:
                    prior = class1_matrix[(subject, page)]
                    classification = prior["classification"]
                    status = "verified" if prior["status"] == "complete" else "gap_editorial_capture"
                    evidence = prior.get("sourceImage", "")
                else:
                    prior_status = class2_ledger[(subject, page)]
                    classification = "exercise_verified" if prior_status == "verified_exercise_captured" else "unclassified"
                    status = "verified" if items else "gap_visual_classification"
                    evidence = f"build/reports/curriculum/agent-class2/pages/{subject}/page-{page:03d}.jpg"
                pages.append({
                    "grade": grade,
                    "subject": subject,
                    "pdfPage": page,
                    "classification": classification,
                    "auditStatus": status,
                    "sourceEvidence": evidence,
                    "printedExerciseCount": len(items),
                    "exercises": [{
                        "id": item.get("id", ""),
                        "type": item.get("type", ""),
                        "printedQuestion": item.get("question", ""),
                        "appQuestion": item.get("question", ""),
                        "directAnswer": item.get("answer", ""),
                        "explanation": item.get("explanation", ""),
                        "followUpBangla": item.get("followUps", {}).get("bn", ""),
                        "followUpBanglish": item.get("followUps", {}).get("banglish", ""),
                        "followUpEnglish": item.get("followUps", {}).get("en", ""),
                    } for item in items],
                })

    payload = {
        "scope": "AaloPro Classes 1-2 official supplied PDFs",
        "verificationRule": "Only second-pass reviewed artifacts count as verified; generic lesson guidance and OCR candidates do not.",
        "sourceArtifacts": sources,
        "pages": pages,
    }
    (OUT / "class-1-2-page-inventory.json").write_text(
        json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8"
    )

    rows = []
    for grade, books in TOTALS.items():
        for subject, total in books.items():
            subset = [p for p in pages if p["grade"] == grade and p["subject"] == subject]
            rows.append({
                "grade": grade,
                "subject": subject,
                "pdf_pages": total,
                "verified_pages": sum(p["auditStatus"] == "verified" for p in subset),
                "verified_exercises": sum(p["printedExerciseCount"] for p in subset),
                "gap_pages": sum(p["auditStatus"].startswith("gap_") for p in subset),
                "gap_page_numbers": ",".join(str(p["pdfPage"]) for p in subset if p["auditStatus"].startswith("gap_")),
            })
    with (OUT / "class-1-2-summary.csv").open("w", encoding="utf-8-sig", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=rows[0].keys())
        writer.writeheader()
        writer.writerows(rows)

    errors = []
    ids = []
    for page in pages:
        for item in page["exercises"]:
            ids.append(item["id"])
            for field in ("appQuestion", "directAnswer", "explanation", "followUpBangla", "followUpBanglish", "followUpEnglish"):
                if not str(item[field]).strip():
                    errors.append({"grade": page["grade"], "subject": page["subject"], "page": page["pdfPage"], "id": item["id"], "missing": field})
    validation = {
        "pageRows": len(pages),
        "expectedPageRows": sum(sum(books.values()) for books in TOTALS.values()),
        "verifiedExerciseOccurrences": len(ids),
        "missingRequiredFields": errors,
        "status": "pass" if len(pages) == sum(sum(books.values()) for books in TOTALS.values()) and not errors else "fail",
    }
    (OUT / "class-1-2-validation.json").write_text(
        json.dumps(validation, ensure_ascii=False, indent=2), encoding="utf-8"
    )
    print(json.dumps({"rows": rows, "validation": validation}, ensure_ascii=False))


if __name__ == "__main__":
    main()
