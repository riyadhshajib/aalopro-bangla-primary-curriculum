#!/usr/bin/env python3
"""Create an editorial OCR queue for Classes 3-5 optional religion books.

OCR is evidence discovery only. Output from this script must never be copied to
VerifiedOfflineQuestionBank without checking the rendered source page.
"""

from __future__ import annotations

import csv
import json
import os
import re
import shutil
import subprocess
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / "build/reports/curriculum/pdf_asset_manifest.csv"
ASSETS = ROOT / "app/src/main/assets/books"
OUT_DIR = ROOT / "build/reports/curriculum/religion-review"
TMP_DIR = ROOT / "tmp/pdfs/religion-ocr"
TESSDATA = ROOT / "tmp/tessdata"
TESSERACT = Path(r"C:\Program Files\Tesseract-OCR\tesseract.exe")
PDFTOPPM = shutil.which("pdftoppm") or r"C:\Users\riyad\.cache\codex-runtimes\codex-primary-runtime\dependencies\native\poppler\Library\bin\pdftoppm.exe"

EXERCISE_RE = re.compile(
    r"প্রশ্ন|অনুশীলন|শূন্যস্থান|উত্তর|বলি|লিখি|লেখো|আলোচনা কর|দলগত কাজ|একক কাজ|activity|answer the|write|fill in",
    re.IGNORECASE,
)


def ocr_image(path: Path) -> str:
    env = os.environ.copy()
    env["TESSDATA_PREFIX"] = str(TESSDATA)
    result = subprocess.run(
        [str(TESSERACT), str(path), "stdout", "-l", "ben+eng", "--psm", "6"],
        capture_output=True,
        text=True,
        encoding="utf-8",
        errors="replace",
        env=env,
        check=False,
    )
    return re.sub(r"[ \t]+", " ", result.stdout).strip()


def process_book(row: dict[str, str]) -> dict[str, object]:
    grade, subject = row["grade"], row["subject"]
    pdf = Path(row["pdf"])
    asset_dir = ASSETS / f"class{grade}" / subject
    images = sorted(asset_dir.glob("page_*.webp"))
    if not images:
        book_dir = TMP_DIR / f"g{grade}-{subject}"
        book_dir.mkdir(parents=True, exist_ok=True)
        prefix = book_dir / "page"
        subprocess.run(
            [str(PDFTOPPM), "-jpeg", "-r", "115", str(pdf), str(prefix)],
            check=True,
            capture_output=True,
        )
        images = sorted(book_dir.glob("page-*.jpg"))
    with ThreadPoolExecutor(max_workers=4) as pool:
        texts = list(pool.map(ocr_image, images))
    pages = []
    for page, (image, text) in enumerate(zip(images, texts), start=1):
        if EXERCISE_RE.search(text):
            pages.append({
                "grade": int(grade),
                "subject": subject,
                "pdf": str(pdf),
                "page": page,
                "image": str(image),
                "ocr_text": text,
                "review_status": "pending_visual_review",
            })
    return {"grade": int(grade), "subject": subject, "pages": len(images), "candidates": pages}


def main() -> None:
    if not TESSERACT.exists() or not (TESSDATA / "ben.traineddata").exists():
        raise SystemExit("Tesseract and tmp/tessdata/ben.traineddata are required")
    rows = [row for row in csv.DictReader(MANIFEST.open(encoding="utf-8-sig"))
            if row["subject"] in {"hindu", "buddhist", "christian"}]
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    results = [process_book(row) for row in rows]
    queue = [page for result in results for page in result["candidates"]]
    (OUT_DIR / "religion_editorial_queue.jsonl").write_text(
        "".join(json.dumps(item, ensure_ascii=False) + "\n" for item in queue), encoding="utf-8"
    )
    summary = ["# Religion-book editorial OCR queue", "",
               "OCR finds candidate exercise pages; every answer still requires visual source review.", "",
               "| Class | Subject | PDF pages | Candidate pages |", "|---:|---|---:|---:|"]
    for result in results:
        summary.append(f"| {result['grade']} | {result['subject']} | {result['pages']} | {len(result['candidates'])} |")
    summary += ["", f"Total candidate pages: **{len(queue)}**", ""]
    (OUT_DIR / "README.md").write_text("\n".join(summary), encoding="utf-8")
    print(f"books={len(results)} candidate_pages={len(queue)}")
    print(OUT_DIR / "README.md")


if __name__ == "__main__":
    main()
