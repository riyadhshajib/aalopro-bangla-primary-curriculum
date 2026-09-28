import csv
import json
from pathlib import Path

ROOT = Path("build/reports/curriculum/agent-class2")
TOTALS = {"bangla": 74, "english": 106, "math": 137}

covered = {subject: set() for subject in TOTALS}
for path in sorted(ROOT.glob("class2-consolidated-batch-*.json")):
    data = json.loads(path.read_text(encoding="utf-8"))
    for book in data["books"]:
        covered[book["subject"]].update(book["sourcePages"])

rows = []
for subject, total in TOTALS.items():
    for page in range(1, total + 1):
        status = "verified_exercise_captured" if page in covered[subject] else "pending_visual_classification"
        rows.append({"subject": subject, "pdf_page": page, "status": status})

with (ROOT / "CLASS2-PAGE-LEDGER.csv").open("w", newline="", encoding="utf-8-sig") as handle:
    writer = csv.DictWriter(handle, fieldnames=["subject", "pdf_page", "status"])
    writer.writeheader()
    writer.writerows(rows)

lines = ["# Class 2 page-by-page ledger", ""]
for subject, total in TOTALS.items():
    done = len(covered[subject])
    pending = [str(p) for p in range(1, total + 1) if p not in covered[subject]]
    lines += [
        f"## {subject.title()}",
        "",
        f"- Total PDF pages: **{total}**",
        f"- Exercise-source pages already visually verified and captured: **{done}**",
        f"- Pages still requiring visual classification: **{total - done}**",
        f"- Pending PDF pages: {', '.join(pending)}",
        "",
    ]
lines += [
    "The PDFs are image-only and local OCR lacks Bengali support. A pending page is not claimed to be an exercise page; it remains explicitly unclassified until visual editorial review. This prevents a false zero-gap claim.",
]
(ROOT / "CLASS2-PAGE-LEDGER.md").write_text("\n".join(lines) + "\n", encoding="utf-8")
