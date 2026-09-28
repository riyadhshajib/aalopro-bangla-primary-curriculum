#!/usr/bin/env python3
"""Produce an honest page ledger for the supplied Class 4 and 5 books.

This does not infer that a page has no exercise. A page is marked reviewed only
when at least one source-reviewed production answer cites that rendered page.
"""

from __future__ import annotations

import csv
import re
from collections import Counter, defaultdict
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
REPORTS = ROOT / "build/reports/curriculum"
ASSETS = ROOT / "app/src/main/assets/books"
OUT = REPORTS / "class-4-5-page-ledger.csv"
SUMMARY = REPORTS / "class-4-5-page-ledger.md"
BANKS = (
    ROOT / "app/src/main/java/com/example/data/model/VerifiedOfflineQuestionBank.kt",
    ROOT / "app/src/main/java/com/example/data/model/VerifiedAgentQuestionBank.kt",
)
ENTRY_RE = re.compile(r'OfflineBookQuestion\(\s*(\d+)\s*,\s*"([^"]+)"\s*,\s*(\d+)\s*,')
SUBJECTS = ("bangla", "english", "math", "science", "bgs", "islam", "hindu", "buddhist", "christian")


def main() -> None:
    reviewed: Counter[tuple[int, str, int]] = Counter()
    for bank in BANKS:
        for grade, subject, page in ENTRY_RE.findall(bank.read_text(encoding="utf-8")):
            if int(grade) in (4, 5):
                reviewed[(int(grade), subject, int(page))] += 1

    rows: list[dict[str, object]] = []
    for grade in (4, 5):
        for subject in SUBJECTS:
            asset_dir = ASSETS / f"class{grade}" / subject
            assets = sorted(asset_dir.glob("page_*.webp"))
            for asset in assets:
                page = int(asset.stem.removeprefix("page_"))
                count = reviewed[(grade, subject, page)]
                rows.append({
                    "grade": grade,
                    "subject": subject,
                    "page": page,
                    "asset": asset.relative_to(ROOT).as_posix(),
                    "reviewed_answers": count,
                    "status": "source_reviewed" if count else "page_asset_only_editorial_review_pending",
                })

    OUT.parent.mkdir(parents=True, exist_ok=True)
    with OUT.open("w", encoding="utf-8-sig", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=rows[0].keys())
        writer.writeheader()
        writer.writerows(rows)

    grouped: dict[tuple[int, str], Counter[str]] = defaultdict(Counter)
    for row in rows:
        key = (int(row["grade"]), str(row["subject"]))
        grouped[key]["pages"] += 1
        if int(row["reviewed_answers"]):
            grouped[key]["reviewed_pages"] += 1
            grouped[key]["answers"] += int(row["reviewed_answers"])

    lines = [
        "# Classes 4 and 5 page coverage ledger",
        "",
        "Only source-reviewed production answers count as reviewed. A bundled page image is evidence that the page is available, not that every exercise on it has an answer.",
        "",
        "| Class | Subject | Bundled pages | Pages with reviewed answers | Reviewed answers | Pages still requiring editorial exercise review |",
        "|---:|---|---:|---:|---:|---:|",
    ]
    for (grade, subject), count in sorted(grouped.items()):
        pending = count["pages"] - count["reviewed_pages"]
        lines.append(f'| {grade} | {subject} | {count["pages"]} | {count["reviewed_pages"]} | {count["answers"]} | {pending} |')
    lines += [
        "",
        f"Full page-level evidence: `{OUT.relative_to(ROOT).as_posix()}`",
        "",
        "Release rule: no book is complete until every printed exercise page has been visually checked and each answer is source-supported. Pages marked pending must not be described as answered.",
    ]
    SUMMARY.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(SUMMARY)
    print(OUT)


if __name__ == "__main__":
    main()
