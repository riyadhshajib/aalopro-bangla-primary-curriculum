#!/usr/bin/env python3
"""Turn religion OCR output into stable, review-sized editorial CSV batches."""

from __future__ import annotations

import csv
import json
import re
from collections import Counter
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "build/reports/curriculum/religion-review/religion_editorial_queue.jsonl"
OUT = ROOT / "build/reports/curriculum/religion-review/batches"

PROMPT_RE = re.compile(
    r"(?:^|\s)(?:প্রশ্ন|অনুশীলন|শূন্যস্থান|উত্তর|বলি|লিখি|লেখো|আলোচনা কর|দলগত কাজ|একক কাজ|activity|answer the|write|fill in)",
    re.IGNORECASE,
)
NOISE_RE = re.compile(r"প্রসঙ্গকথা|সূচিপত্র|লেখক পরিচিতি|জাতীয় শিক্ষাক্রম ও পাঠ্যপুস্তক বোর্ড")


def clean(value: str) -> str:
    return re.sub(r"\s+", " ", value).strip()


def candidate_lines(text: str) -> list[str]:
    lines = [clean(line) for line in text.splitlines()]
    return [line for line in lines if len(line) >= 5 and PROMPT_RE.search(line)]


def main() -> None:
    records = [json.loads(line) for line in SOURCE.read_text(encoding="utf-8").splitlines() if line.strip()]
    accepted = []
    for item in records:
        prompts = candidate_lines(item["ocr_text"])
        if not prompts or (item["page"] <= 6 and NOISE_RE.search(item["ocr_text"])):
            continue
        accepted.append({
            "grade": item["grade"],
            "subject": item["subject"],
            "page": item["page"],
            "pdf": item["pdf"],
            "image": item["image"],
            "prompt_candidates": " | ".join(prompts[:12]),
            "source_checked": "no",
            "answer_checked": "no",
            "follow_up_checked": "no",
            "reviewer_note": "",
        })

    OUT.mkdir(parents=True, exist_ok=True)
    counts = Counter((row["grade"], row["subject"]) for row in accepted)
    for (grade, subject), count in sorted(counts.items()):
        rows = [row for row in accepted if row["grade"] == grade and row["subject"] == subject]
        for batch_no, start in enumerate(range(0, len(rows), 25), start=1):
            path = OUT / f"g{grade}-{subject}-batch-{batch_no:02d}.csv"
            with path.open("w", newline="", encoding="utf-8-sig") as handle:
                writer = csv.DictWriter(handle, fieldnames=rows[0].keys())
                writer.writeheader()
                writer.writerows(rows[start:start + 25])

    summary = [
        "# Religion editorial batches",
        "",
        "Each row is an OCR-discovered prompt candidate. It becomes app content only after all three review flags are `yes`.",
        "",
        "| Class | Subject | Review pages | Batches |",
        "|---:|---|---:|---:|",
    ]
    for (grade, subject), count in sorted(counts.items()):
        summary.append(f"| {grade} | {subject} | {count} | {(count + 24) // 25} |")
    summary += ["", f"Total focused review pages: **{len(accepted)}**", ""]
    (OUT / "README.md").write_text("\n".join(summary), encoding="utf-8")
    print(f"focused_pages={len(accepted)} batches={sum((count + 24) // 25 for count in counts.values())}")
    print(OUT / "README.md")


if __name__ == "__main__":
    main()
