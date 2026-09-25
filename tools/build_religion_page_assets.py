#!/usr/bin/env python3
"""Render Classes 3-5 optional-religion PDFs into compact app page assets."""

from __future__ import annotations

import csv
import shutil
import subprocess
import tempfile
from pathlib import Path

from PIL import Image


ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "build/reports/curriculum/pdf_asset_manifest.csv"
ASSETS = ROOT / "app/src/main/assets/books"
PDFTOPPM = shutil.which("pdftoppm") or (
    r"C:\Users\riyad\.cache\codex-runtimes\codex-primary-runtime\dependencies"
    r"\native\poppler\Library\bin\pdftoppm.exe"
)
SUBJECTS = {"hindu", "buddhist", "christian"}


def render_book(row: dict[str, str]) -> tuple[int, str, int]:
    grade = int(row["grade"])
    subject = row["subject"]
    pdf = Path(row["pdf"])
    pages = int(row["pages"])
    out_dir = ASSETS / f"class{grade}" / subject
    out_dir.mkdir(parents=True, exist_ok=True)

    with tempfile.TemporaryDirectory(prefix=f"aalopro-g{grade}-{subject}-") as temp:
        prefix = Path(temp) / "page"
        subprocess.run(
            [str(PDFTOPPM), "-jpeg", "-jpegopt", "quality=86", "-r", "105", str(pdf), str(prefix)],
            check=True,
        )
        images = sorted(Path(temp).glob("page-*.jpg"))
        if len(images) != pages:
            raise RuntimeError(f"{pdf}: expected {pages} pages, rendered {len(images)}")
        for index, image_path in enumerate(images, start=1):
            destination = out_dir / f"page_{index:03d}.webp"
            with Image.open(image_path) as image:
                image.convert("RGB").save(destination, "WEBP", quality=72, method=4)

    generated = len(list(out_dir.glob("page_*.webp")))
    if generated != pages:
        raise RuntimeError(f"{out_dir}: expected {pages} assets, found {generated}")
    return grade, subject, generated


def main() -> None:
    rows = [
        row
        for row in csv.DictReader(MANIFEST.open(encoding="utf-8-sig"))
        if row["subject"] in SUBJECTS and int(row["grade"]) in range(3, 6)
    ]
    if len(rows) != 9:
        raise SystemExit(f"Expected 9 religion books, found {len(rows)}")
    for row in rows:
        grade, subject, pages = render_book(row)
        print(f"class={grade} subject={subject} pages={pages}", flush=True)


if __name__ == "__main__":
    main()
