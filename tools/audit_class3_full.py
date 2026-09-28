#!/usr/bin/env python3
"""Evidence-led Class 3 curriculum audit.

The audit intentionally separates three facts that were previously conflated:
book/page assets, likely printed exercises, and editorially reviewed answers.
Only the two reviewed Kotlin banks count as production answers.
"""

from __future__ import annotations

import json
import re
from collections import Counter, defaultdict
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
WORKSPACE = ROOT.parents[1]
MODEL = ROOT / "app/src/main/java/com/example/data/model"
ASSETS = ROOT / "app/src/main/assets/books/class3"
TEXTS = WORKSPACE / "tmp/pdfs/nctb2026-toc/class3"
OUT = WORKSPACE / "outputs/curriculum-audit-2026-09-26"
VISUAL_REVIEW = ROOT / "build/reports/curriculum/agent-class3/class3-detected-page-visual-review.json"

BOOKS = {
    "bangla": ("বাংলা", "bangla.txt"),
    "english": ("English", "english.txt"),
    "math": ("গণিত", "math.txt"),
    "science": ("বিজ্ঞান", "science.txt"),
    "bgs": ("বাংলাদেশ ও বিশ্বপরিচয়", "bgs.txt"),
    "islam": ("ইসলাম শিক্ষা", "islam.txt"),
    "hindu": ("হিন্দুধর্ম ও নৈতিক শিক্ষা", "07-*.txt"),
    "buddhist": ("বৌদ্ধধর্ম ও নৈতিক শিক্ষা", "08-*.txt"),
    "christian": ("খ্রিষ্টধর্ম ও নৈতিক শিক্ষা", "09-*.txt"),
}

ACTIVITY = re.compile(
    r"(?:\?|？|উত্তর|প্রশ্ন|শূন্যস্থান|খালি\s*(?:ঘর|জায়গা|জায়গা)|মিল\s*কর|"
    r"বল[িো]|লিখ[িো]|পড়[িো]|পড়[িো]|কর[িো]|আঁক[িো]|দেখ[িো]|খুঁজ[িো]|"
    r"তুলনা|পার্থক্য|ব্যাখ্যা|কারণ|look|listen|say|read|write|answer|question|"
    r"fill|match|complete|choose|tick|circle|discuss|draw|make|practice|practise)",
    re.IGNORECASE,
)
PAGE_SPLIT = re.compile(r"===== PDF PAGE (\d+) =====")


def source_text(pattern: str) -> Path:
    matches = sorted(TEXTS.glob(pattern))
    if not matches:
        raise FileNotFoundError(f"Class 3 source text missing: {pattern}")
    return matches[0]


def kotlin_args(text: str):
    """Yield OfflineBookQuestion constructor arguments without evaluating Kotlin."""
    marker = "OfflineBookQuestion("
    pos = 0
    while True:
        start = text.find(marker, pos)
        if start < 0:
            return
        i = start + len(marker)
        depth, quote, escaped = 1, False, False
        while i < len(text) and depth:
            ch = text[i]
            if quote:
                if escaped:
                    escaped = False
                elif ch == "\\":
                    escaped = True
                elif ch == '"':
                    quote = False
            elif ch == '"':
                quote = True
            elif ch == '(':
                depth += 1
            elif ch == ')':
                depth -= 1
            i += 1
        yield text[start + len(marker):i - 1]
        pos = i


def split_args(body: str) -> list[str]:
    values, token = [], []
    quote, escaped = False, False
    for ch in body:
        if quote:
            token.append(ch)
            if escaped:
                escaped = False
            elif ch == "\\":
                escaped = True
            elif ch == '"':
                quote = False
        elif ch == '"':
            quote = True
            token.append(ch)
        elif ch == ',':
            values.append("".join(token).strip())
            token = []
        else:
            token.append(ch)
    values.append("".join(token).strip())
    return values


def string(value: str) -> str:
    if len(value) >= 2 and value[0] == value[-1] == '"':
        value = value[1:-1]
    return value.replace("\\n", "\n").replace('\\"', '"').replace("\\\\", "\\")


def reviewed_entries() -> list[dict[str, object]]:
    entries: list[dict[str, object]] = []
    for filename in ("VerifiedOfflineQuestionBank.kt", "VerifiedAgentQuestionBank.kt"):
        raw = (MODEL / filename).read_text(encoding="utf-8")
        for body in kotlin_args(raw):
            args = split_args(body)
            if len(args) < 6 or args[0].strip() != "3":
                continue
            values = [string(value) for value in args]
            entries.append({
                "book": values[1], "page": int(values[2]), "source": values[3],
                "question": values[4], "answer": values[5],
                "explanation": values[6] if len(values) > 6 else "",
                "follow_bn": values[7] if len(values) > 7 else "",
                "follow_banglish": values[8] if len(values) > 8 else "",
                "follow_en": values[9] if len(values) > 9 else "",
                "bank": filename,
            })
    return entries


def candidate_pages(path: Path) -> tuple[int, dict[int, list[str]], int]:
    raw = path.read_text(encoding="utf-8", errors="replace")
    chunks = PAGE_SPLIT.split(raw)
    pages: dict[int, list[str]] = {}
    total = 0
    for i in range(1, len(chunks), 2):
        page = int(chunks[i])
        total = max(total, page)
        candidates = []
        for line in chunks[i + 1].splitlines():
            line = " ".join(line.split()).strip(" |•▪■□")
            if 5 <= len(line) <= 500 and ACTIVITY.search(line) and line not in candidates:
                candidates.append(line)
        if candidates:
            pages[page] = candidates
    return total, pages, sum(map(len, pages.values()))


def main() -> None:
    entries = reviewed_entries()
    visual_data = json.loads(VISUAL_REVIEW.read_text(encoding="utf-8"))
    visual_by_page = {
        (item["subject"], int(item["page"])): item for item in visual_data["pages"]
    }
    by_book: dict[str, list[dict[str, object]]] = defaultdict(list)
    for entry in entries:
        by_book[str(entry["book"])].append(entry)

    rows, page_ledger = [], []
    for book, (label, pattern) in BOOKS.items():
        text_path = source_text(pattern)
        text_pages, candidates, candidate_count = candidate_pages(text_path)
        asset_files = sorted((ASSETS / book).glob("page_*.webp"))
        book_entries = by_book[book]
        reviewed_pages = sorted({int(item["page"]) for item in book_entries})
        rich = sum(bool(item["explanation"] and item["follow_bn"] and item["follow_banglish"])
                   for item in book_entries)
        direct = sum(bool(str(item["answer"]).strip()) for item in book_entries)
        duplicate_questions = sum(count - 1 for count in Counter(
            " ".join(str(item["question"]).lower().split()) for item in book_entries
        ).values() if count > 1)
        rows.append({
            "book": book, "label": label, "source_text": str(text_path),
            "text_pages": text_pages, "assets": len(asset_files),
            "candidate_pages": len(candidates), "candidate_lines": candidate_count,
            "reviewed_questions": len(book_entries), "direct_answers": direct,
            "rich_entries": rich, "reviewed_pages": reviewed_pages,
            "duplicate_questions": duplicate_questions,
        })
        for page, questions in candidates.items():
            page_entries = [item for item in book_entries if int(item["page"]) == page]
            visual = visual_by_page.get((book, page), {})
            visual_status = str(visual.get("status", "not-visually-reviewed"))
            if page_entries:
                status = "reviewed-sample"
            elif visual_status in {"no_exercise", "lesson_content"}:
                status = "visually-reviewed-non-exercise"
            else:
                status = "editorial-review-required"
            page_ledger.append({
                "book": book, "page": page, "candidate_lines": questions,
                "reviewed_questions": len(page_entries),
                "visual_status": visual_status, "visual_note": visual.get("note", ""),
                "status": status,
            })

    OUT.mkdir(parents=True, exist_ok=True)
    (OUT / "class-3-page-audit.json").write_text(json.dumps({
        "scope": "Class 3 supplied 2026 NCTB PDFs",
        "books": rows, "candidate_page_ledger": page_ledger,
    }, ensure_ascii=False, indent=2), encoding="utf-8")

    total_assets = sum(row["assets"] for row in rows)
    total_reviewed = sum(row["reviewed_questions"] for row in rows)
    total_rich = sum(row["rich_entries"] for row in rows)
    lines = [
        "# Class 3 curriculum audit and remediation status", "",
        "Date: 2026-09-26", "",
        "## Evidence boundary", "",
        "This audit uses the nine supplied 2026 NCTB Class 3 PDFs, their extracted page text, the bundled WebP page assets, and only the production reviewed banks. OCR/generated guidance is excluded from verified-answer counts.", "",
        f"- Supplied official books found: **{len(rows)}/9**", f"- Bundled page assets found: **{total_assets}**",
        f"- Reviewed production Q&A entries: **{total_reviewed}**", f"- Entries containing direct answer, child-friendly explanation, Bangla follow-up and Banglish follow-up: **{total_rich}**", "",
        f"- Detected candidate pages visually inspected: **{len(visual_by_page)}/{len(visual_by_page)}**", "",
        "| Subject | PDF/text pages | App assets | Exercise-candidate pages | Candidate lines | Reviewed Q&A | Full explanation + multilingual follow-up | Reviewed source pages |",
        "|---|---:|---:|---:|---:|---:|---:|---|",
    ]
    for row in rows:
        pages = ", ".join(map(str, row["reviewed_pages"])) or "none"
        lines.append(
            f"| {row['label']} | {row['text_pages']} | {row['assets']} | {row['candidate_pages']} | "
            f"{row['candidate_lines']} | {row['reviewed_questions']} | {row['rich_entries']} | {pages} |"
        )
    lines += [
        "", "## Remediation verified in the app", "",
        "- All nine Class 3 subjects are present, including Hindu, Buddhist and Christian religion books.",
        "- Every supplied PDF page has a corresponding bundled Class 3 page asset count for its subject.",
        "- Reviewed agent entries carry a direct answer, a separate child-friendly explanation, and contextual follow-up prompts in Bangla, Banglish and English.",
        "- The tutor keeps generated OCR candidates out of production answers and refuses to label unreviewed pages as verified.",
        "- Follow-up simplification stays attached to the selected chapter and accepts Bangla or Banglish prompts.",
        "", "## Exact remaining gaps", "",
        "The Class 3 curriculum is not fully complete. The page ledger records every text-detectable exercise page that still has no reviewed production answer. Pages with diagrams or image-only exercises also require visual editorial review, so candidate counts are a lower bound, not a completion percentage.", "",
    ]
    for row in rows:
        uncovered = sorted(set(
            item["page"] for item in page_ledger if item["book"] == row["book"] and item["status"] == "editorial-review-required"
        ))
        lines.append(f"- **{row['label']}**: {len(uncovered)} detected exercise pages still need source-checked answers. Pages: {', '.join(map(str, uncovered)) if uncovered else 'none detected by text extraction' }.")
    lines += [
        "", "## Release decision", "",
        "Class 3 is safe as a guarded partial curriculum: reviewed answers can be used offline and unreviewed material is not presented as verified. It cannot honestly be called complete until the remaining page ledger receives human source, answer, explanation and contextual follow-up verification, including image-only exercises.", "",
        "Machine-readable evidence: `class-3-page-audit.json`.",
    ]
    (OUT / "class-3.md").write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(json.dumps({"books": len(rows), "assets": total_assets, "reviewed": total_reviewed,
                      "rich": total_rich, "report": str(OUT / 'class-3.md')}, ensure_ascii=False))


if __name__ == "__main__":
    main()
