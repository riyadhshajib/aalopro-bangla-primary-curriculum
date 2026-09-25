#!/usr/bin/env python3
"""Report what AaloPro can honestly call a verified offline answer.

The generated OCR queue is intentionally excluded. Only entries in
VerifiedOfflineQuestionBank.kt count as reviewed answers.
"""

from __future__ import annotations

import csv
import re
from collections import Counter
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
MODEL_DIR = ROOT / "app/src/main/java/com/example/data/model"
VERIFIED = MODEL_DIR / "VerifiedOfflineQuestionBank.kt"
GENERATED = MODEL_DIR / "FullOfflineQuestionBank.kt"
OUT_DIR = ROOT / "build/reports/curriculum"

ENTRY_RE = re.compile(
    r'OfflineBookQuestion\(\s*(\d+)\s*,\s*"([^"]+)"\s*,\s*(\d+)\s*,',
    re.MULTILINE,
)

BOOKS = {
    1: ("bangla", "english", "math"),
    2: ("bangla", "english", "math"),
    3: ("bangla", "english", "math", "science", "bgs", "islam", "hindu", "buddhist", "christian"),
    4: ("bangla", "english", "math", "science", "bgs", "islam", "hindu", "buddhist", "christian"),
    5: ("bangla", "english", "math", "science", "bgs", "islam", "hindu", "buddhist", "christian"),
}


def entries(path: Path) -> list[tuple[int, str, int]]:
    text = path.read_text(encoding="utf-8")
    return [(int(g), book, int(page)) for g, book, page in ENTRY_RE.findall(text)]


def main() -> None:
    reviewed = entries(VERIFIED)
    ocr_candidates = entries(GENERATED)
    counts = Counter((grade, book) for grade, book, _ in reviewed)
    page_counts = Counter((grade, book, page) for grade, book, page in reviewed)

    OUT_DIR.mkdir(parents=True, exist_ok=True)
    csv_path = OUT_DIR / "verified_answer_matrix.csv"
    md_path = OUT_DIR / "verified_answer_audit.md"

    rows: list[dict[str, object]] = []
    for grade, books in BOOKS.items():
        for book in books:
            count = counts[(grade, book)]
            pages = sorted(page for g, b, page in page_counts if g == grade and b == book)
            rows.append(
                {
                    "grade": grade,
                    "book": book,
                    "reviewed_answers": count,
                    "reviewed_pages": ",".join(map(str, pages)),
                    "status": "reviewed" if count else "missing",
                }
            )

    with csv_path.open("w", newline="", encoding="utf-8-sig") as handle:
        writer = csv.DictWriter(handle, fieldnames=rows[0].keys())
        writer.writeheader()
        writer.writerows(rows)

    reviewed_slots = sum(1 for row in rows if row["reviewed_answers"])
    missing_slots = len(rows) - reviewed_slots
    lines = [
        "# Verified offline-answer audit",
        "",
        "This report counts only human-reviewed entries in `VerifiedOfflineQuestionBank.kt`.",
        "OCR candidates and generic learning guidance are deliberately excluded.",
        "",
        f"- Expected book slots in current scope: **{len(rows)}**",
        f"- Slots with at least one reviewed answer: **{reviewed_slots}**",
        f"- Slots with no reviewed answer: **{missing_slots}**",
        f"- Total reviewed question-answer entries: **{len(reviewed)}**",
        f"- OCR candidates awaiting editorial review: **{len(ocr_candidates)}**",
        "",
        "| Class | Book | Reviewed answers | Reviewed pages | Status |",
        "|---:|---|---:|---|---|",
    ]
    for row in rows:
        lines.append(
            f"| {row['grade']} | {row['book']} | {row['reviewed_answers']} | "
            f"{row['reviewed_pages'] or '-'} | {row['status']} |"
        )
    lines += [
        "",
        "## Interpretation",
        "",
        "A bundled PDF/page asset proves that the source is available; it does not prove that its exercises have reviewed answers.",
        "The app must not label OCR-derived or generic guidance as a verified textbook answer.",
    ]
    md_path.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"reviewed_answers={len(reviewed)}")
    print(f"ocr_candidates={len(ocr_candidates)}")
    print(f"reviewed_slots={reviewed_slots}/{len(rows)}")
    print(md_path)


if __name__ == "__main__":
    main()
