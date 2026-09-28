#!/usr/bin/env python3
"""Build a local OCR review queue for the three Class 1 NCTB books.

OCR is discovery evidence only. Every generated answer must still be checked
against the rendered textbook page before it can enter a verified bank.
"""

from __future__ import annotations

import json
import os
import re
import subprocess
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
SOURCE_DIR = Path(r"C:\Users\riyad\Downloads\Documents")
OUT_DIR = ROOT / "build/reports/curriculum/agent-class1"
PAGE_DIR = OUT_DIR / "pages"
TESSDATA = ROOT / "tmp/tessdata"
TESSERACT = Path(r"C:\Program Files\Tesseract-OCR\tesseract.exe")
PDFTOPPM = Path(
    r"C:\Users\riyad\.cache\codex-runtimes\codex-primary-runtime"
    r"\dependencies\native\poppler\Library\bin\pdftoppm.exe"
)

BOOKS = {
    "bangla": SOURCE_DIR / "Class 1 Bangla_compressed.pdf",
    "english": SOURCE_DIR / "Class 1 English_compressed.pdf",
    "math": SOURCE_DIR / "Class 1 Math_compressed.pdf",
}
EXERCISE_RE = re.compile(
    r"প্রশ্ন|অনুশীলন|উত্তর|বলি|লিখি|লেখো|শিখি|করো|করি|মেলাই|শূন্যস্থান|"
    r"answer|write|fill|match|say|read|ask|draw|colour|circle|count|exercise|activity",
    re.IGNORECASE,
)


def render(subject: str, pdf: Path) -> list[Path]:
    target = PAGE_DIR / subject
    target.mkdir(parents=True, exist_ok=True)
    images = sorted(target.glob("page-*.jpg"))
    if images:
        return images
    subprocess.run(
        [str(PDFTOPPM), "-jpeg", "-r", "130", str(pdf), str(target / "page")],
        check=True,
        capture_output=True,
    )
    return sorted(target.glob("page-*.jpg"))


def ocr(image: Path) -> str:
    env = os.environ.copy()
    env["TESSDATA_PREFIX"] = str(TESSDATA)
    result = subprocess.run(
        [str(TESSERACT), str(image), "stdout", "-l", "ben+eng", "--psm", "6"],
        capture_output=True,
        text=True,
        encoding="utf-8",
        errors="replace",
        env=env,
        check=False,
    )
    return re.sub(r"[ \t]+", " ", result.stdout).strip()


def main() -> None:
    required = [TESSERACT, PDFTOPPM, TESSDATA / "ben.traineddata", TESSDATA / "eng.traineddata"]
    missing = [str(path) for path in required if not path.exists()]
    if missing:
        raise SystemExit("Missing local OCR dependencies: " + ", ".join(missing))
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    queue: list[dict[str, object]] = []
    summary: list[dict[str, object]] = []
    for subject, pdf in BOOKS.items():
        images = render(subject, pdf)
        with ThreadPoolExecutor(max_workers=4) as pool:
            texts = list(pool.map(ocr, images))
        candidates = 0
        for page, (image, text) in enumerate(zip(images, texts), start=1):
            if EXERCISE_RE.search(text):
                candidates += 1
                queue.append({
                    "grade": 1,
                    "subject": subject,
                    "source_pdf": str(pdf),
                    "source_page": page,
                    "source_image": str(image),
                    "ocr_text": text,
                    "review_status": "pending_visual_review",
                })
        summary.append({"subject": subject, "pages": len(images), "candidate_pages": candidates})
    (OUT_DIR / "class1-editorial-queue.jsonl").write_text(
        "".join(json.dumps(row, ensure_ascii=False) + "\n" for row in queue),
        encoding="utf-8",
    )
    (OUT_DIR / "class1-ocr-summary.json").write_text(
        json.dumps(summary, ensure_ascii=False, indent=2), encoding="utf-8"
    )
    print(json.dumps({"books": len(summary), "candidate_pages": len(queue), "summary": summary}, ensure_ascii=False))


if __name__ == "__main__":
    main()
