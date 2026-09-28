#!/usr/bin/env python3
"""Validate local visual evidence and editorial records for Classes 1 and 2.

This check deliberately separates rendered source evidence from editorial
verification.  A rendered page proves that a reviewer can inspect the source;
it does not promote page text or OCR output to verified curriculum content.
"""

from __future__ import annotations

import json
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
REPORTS = ROOT / "build/reports/curriculum"


def class1_result() -> dict:
    matrix_path = REPORTS / "agent-class1/class1-page-by-page-zero-gap-matrix.json"
    rows = json.loads(matrix_path.read_text(encoding="utf-8"))
    missing = []
    for row in rows:
        evidence = ROOT / row["sourceImage"]
        if not evidence.is_file():
            missing.append({"subject": row["subject"], "page": row["sourcePage"], "path": row["sourceImage"]})
    return {
        "grade": 1,
        "pageRows": len(rows),
        "expectedPageRows": 310,
        "sourceEvidencePresent": len(rows) - len(missing),
        "missingSourceEvidence": missing,
        "status": "pass" if len(rows) == 310 and not missing else "fail",
    }


def class2_result() -> dict:
    totals = {"bangla": 74, "english": 106, "math": 137}
    missing = []
    present = 0
    for subject, total in totals.items():
        for page in range(1, total + 1):
            relative = Path(f"build/reports/curriculum/agent-class2/pages/{subject}/page-{page:03d}.jpg")
            if (ROOT / relative).is_file():
                present += 1
            else:
                missing.append({"subject": subject, "page": page, "path": relative.as_posix()})
    expected = sum(totals.values())
    return {
        "grade": 2,
        "pageRows": expected,
        "expectedPageRows": expected,
        "sourceEvidencePresent": present,
        "missingSourceEvidence": missing,
        "status": "pass" if present == expected and not missing else "fail",
    }


def main() -> None:
    for grade, result in ((1, class1_result()), (2, class2_result())):
        output = REPORTS / f"agent-class{grade}/source-evidence-validation.json"
        output.write_text(json.dumps(result, ensure_ascii=False, indent=2), encoding="utf-8")
        print(json.dumps(result, ensure_ascii=False))


if __name__ == "__main__":
    main()
