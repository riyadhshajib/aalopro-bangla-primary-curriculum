#!/usr/bin/env python3
"""Audit optional-religion assets and editorial verification without overstating coverage."""

from __future__ import annotations

import csv
import re
from collections import Counter
from pathlib import Path

from PIL import Image


ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "build/reports/curriculum/pdf_asset_manifest.csv"
ASSETS = ROOT / "app/src/main/assets/books"
BATCHES = ROOT / "build/reports/curriculum/religion-review/batches"
VERIFIED = ROOT / "app/src/main/java/com/example/data/model/VerifiedOfflineQuestionBank.kt"
AGENT_VERIFIED = ROOT / "app/src/main/java/com/example/data/model/VerifiedAgentQuestionBank.kt"
OUT = ROOT / "build/reports/curriculum/religion-content-audit.md"
SUBJECTS = {"hindu", "buddhist", "christian"}
ENTRY_RE = re.compile(r'OfflineBookQuestion\(\s*(\d+)\s*,\s*"([^"]+)"')


def main() -> None:
    manifest = [
        row for row in csv.DictReader(MANIFEST.open(encoding="utf-8-sig"))
        if row["subject"] in SUBJECTS and int(row["grade"]) in range(3, 6)
    ]
    verified_text = VERIFIED.read_text(encoding="utf-8") + "\n" + AGENT_VERIFIED.read_text(encoding="utf-8")
    verified_counts = Counter((int(g), subject) for g, subject in ENTRY_RE.findall(verified_text))
    review_counts: Counter[tuple[int, str, str]] = Counter()
    focused_counts: Counter[tuple[int, str]] = Counter()
    for batch in BATCHES.glob("g*-*-batch-*.csv"):
        for row in csv.DictReader(batch.open(encoding="utf-8-sig")):
            key = (int(row["grade"]), row["subject"])
            focused_counts[key] += 1
            status = "complete" if all(row[field].strip().lower() == "yes" for field in (
                "source_checked", "answer_checked", "follow_up_checked"
            )) else "pending"
            review_counts[(key[0], key[1], status)] += 1

    rows = []
    corrupt = []
    for record in manifest:
        grade, subject, expected = int(record["grade"]), record["subject"], int(record["pages"])
        files = sorted((ASSETS / f"class{grade}" / subject).glob("page_*.webp"))
        for path in files:
            try:
                with Image.open(path) as image:
                    image.verify()
            except Exception as exc:
                corrupt.append(f"{path}: {exc}")
        key = (grade, subject)
        rows.append((grade, subject, expected, len(files), focused_counts[key],
                     review_counts[(grade, subject, "complete")], verified_counts[key]))

    lines = [
        "# Optional religion content audit", "",
        "Scope: Classes 3-5 Hindu, Buddhist and Christian religion books.", "",
        f"- Books: **{len(rows)}/9**",
        f"- PDF pages expected: **{sum(row[2] for row in rows)}**",
        f"- App page assets present: **{sum(row[3] for row in rows)}**",
        f"- Corrupt page assets: **{len(corrupt)}**",
        f"- Focused exercise/review pages: **{sum(row[4] for row in rows)}**",
        f"- Pages completing source + answer + follow-up review: **{sum(row[5] for row in rows)}**",
        f"- Verified question-answer entries currently in production bank: **{sum(row[6] for row in rows)}**",
        "", "| Class | Subject | PDF pages | Assets | Review pages | Fully reviewed pages | Verified Q&A |",
        "|---:|---|---:|---:|---:|---:|---:|",
    ]
    lines.extend(
        f"| {g} | {s} | {pdf} | {assets} | {focused} | {reviewed} | {qa} |"
        for g, s, pdf, assets, focused, reviewed, qa in rows
    )
    lines += [
        "", "## Release interpretation", "",
        "- Asset coverage is complete only when PDF pages and decoded app assets match.",
        "- A review page is not verified until the source, answer, and contextual follow-up flags are all yes.",
        "- OCR text is an editorial lead, never a verified textbook answer.",
        "- Generic chapter guidance must not be reported as complete exercise-answer coverage.",
    ]
    if corrupt:
        lines += ["", "## Corrupt assets", ""] + [f"- {item}" for item in corrupt]
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(OUT)
    print(f"assets={sum(row[3] for row in rows)}/{sum(row[2] for row in rows)} corrupt={len(corrupt)}")
    print(f"fully_reviewed_pages={sum(row[5] for row in rows)}/{sum(row[4] for row in rows)} verified_qa={sum(row[6] for row in rows)}")


if __name__ == "__main__":
    main()
