#!/usr/bin/env python3
"""Inventory available NCTB source PDFs and compare them with bundled book assets."""

from __future__ import annotations

import csv
import re
from dataclasses import dataclass, asdict
from pathlib import Path

from pypdf import PdfReader


ROOT = Path(__file__).resolve().parents[1]
DOWNLOADS = Path.home() / "Downloads"
ASSETS = ROOT / "app" / "src" / "main" / "assets" / "books"
REPORT_DIR = ROOT / "build" / "reports" / "curriculum"


@dataclass
class BookSource:
    grade: int
    subject: str
    title: str
    pdf: str
    pages: int
    size_mb: float
    extractable_sample_chars: int
    asset_pages: int
    asset_status: str


def classify(path: Path) -> tuple[int, str] | None:
    value = f"{path.parent.name} {path.name}".lower()
    grade = 0
    for number in range(1, 6):
        if re.search(rf"(?:class[\s_-]*|class-|class\s){number}\b", value):
            grade = number
            break
        if re.search(rf"\bc{number}r\b", value):
            grade = number
            break
    if not grade and path.parent == DOWNLOADS and path.name.lower() in {"english.pdf", "english_compressed.pdf"}:
        grade = 2
    if not grade:
        return None

    # Check the actual subject token before language/edition markers such as "Math ... Bangla".
    if "math" in value or "গণিত" in value:
        subject = "math"
    elif "english" in value:
        subject = "english"
    elif "bangla" in value or "বাংলা" in value:
        subject = "bangla"
    elif "science" in value:
        subject = "science"
    elif "bgs" in value:
        subject = "bgs"
    elif "islam" in value:
        subject = "islam"
    elif "hindu" in value:
        subject = "hindu"
    elif "budd" in value:
        subject = "buddhist"
    elif "khristo" in value or "christ" in value:
        subject = "christian"
    else:
        return None
    return grade, subject


def sample_text(reader: PdfReader) -> int:
    indexes = sorted(set(range(min(3, len(reader.pages)))) | set(range(max(0, len(reader.pages) - 2), len(reader.pages))))
    total = 0
    for index in indexes:
        try:
            total += len((reader.pages[index].extract_text() or "").strip())
        except Exception:
            pass
    return total


def main() -> None:
    candidates: dict[tuple[int, str], Path] = {}
    for path in DOWNLOADS.rglob("*.pdf"):
        identified = classify(path)
        if not identified:
            continue
        current = candidates.get(identified)
        # Prefer the largest file when duplicate sources exist; it is usually the full-resolution book.
        if current is None or path.stat().st_size > current.stat().st_size:
            candidates[identified] = path

    records: list[BookSource] = []
    for (grade, subject), path in sorted(candidates.items()):
        try:
            reader = PdfReader(str(path))
            pages = len(reader.pages)
            chars = sample_text(reader)
        except Exception:
            pages = 0
            chars = 0
        asset_dir = ASSETS / f"class{grade}" / subject
        asset_pages = len(list(asset_dir.glob("page_*.webp"))) if asset_dir.is_dir() else 0
        if asset_pages == 0:
            status = "missing"
        elif pages and asset_pages >= max(1, pages - 6):
            status = "present-near-full"
        else:
            status = "present-partial-or-offset"
        records.append(
            BookSource(
                grade=grade,
                subject=subject,
                title=path.stem,
                pdf=str(path),
                pages=pages,
                size_mb=round(path.stat().st_size / 1024 / 1024, 2),
                extractable_sample_chars=chars,
                asset_pages=asset_pages,
                asset_status=status,
            )
        )

    REPORT_DIR.mkdir(parents=True, exist_ok=True)
    csv_path = REPORT_DIR / "pdf_asset_manifest.csv"
    with csv_path.open("w", encoding="utf-8-sig", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=list(asdict(records[0]).keys()))
        writer.writeheader()
        writer.writerows(asdict(record) for record in records)

    expected = {(1, "bangla"), (1, "english"), (1, "math"), (2, "bangla"), (2, "english"), (2, "math")}
    for grade in range(3, 6):
        expected.update((grade, subject) for subject in ("bangla", "english", "math", "science", "bgs", "islam", "hindu", "buddhist", "christian"))
    available = {(record.grade, record.subject) for record in records}
    missing_sources = sorted(expected - available)
    missing_assets = [(record.grade, record.subject) for record in records if record.asset_pages == 0]
    low_text = [(record.grade, record.subject) for record in records if record.extractable_sample_chars < 200]

    lines = [
        "# NCTB PDF and bundled-asset manifest",
        "",
        "This report identifies available source books and bundled page assets. It does not prove that every exercise has a verified answer.",
        "",
        f"- Expected primary book slots under the current product decision: {len(expected)}",
        f"- Source PDFs found: {len(records)}",
        f"- Missing source slots: {len(missing_sources)}",
        f"- Found PDFs without bundled page assets: {len(missing_assets)}",
        f"- PDFs requiring OCR/visual extraction in the sample: {len(low_text)}",
        "",
        "| Class | Subject | PDF pages | Asset pages | Text sample chars | Asset status |",
        "|---:|---|---:|---:|---:|---|",
    ]
    for record in records:
        lines.append(
            f"| {record.grade} | {record.subject} | {record.pages} | {record.asset_pages} | "
            f"{record.extractable_sample_chars} | {record.asset_status} |"
        )
    lines.extend(["", "## Missing source PDFs", ""])
    if missing_sources:
        lines.extend(f"- Class {grade}: {subject}" for grade, subject in missing_sources)
    else:
        lines.append("- None")
    lines.extend(["", "## PDFs found but page assets missing", ""])
    if missing_assets:
        lines.extend(f"- Class {grade}: {subject}" for grade, subject in missing_assets)
    else:
        lines.append("- None")
    lines.extend(["", "## Extraction rule", "", "PDFs with little extractable text must be audited from rendered pages/OCR; they must not be treated as empty books."])
    (REPORT_DIR / "pdf_asset_manifest.md").write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(REPORT_DIR / "pdf_asset_manifest.md")
    print(csv_path)


if __name__ == "__main__":
    main()
