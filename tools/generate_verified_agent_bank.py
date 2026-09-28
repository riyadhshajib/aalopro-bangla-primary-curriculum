"""Promote second-pass agent review artifacts into a deterministic Kotlin bank.

The editorial JSON remains the source of truth. This generator never reads OCR
candidates and skips questions that are already present verbatim in the human-
reviewed production bank.
"""

from __future__ import annotations

import json
import re
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
REPORTS = ROOT / "build/reports/curriculum"
EXISTING = ROOT / "app/src/main/java/com/example/data/model/VerifiedOfflineQuestionBank.kt"
OUTPUT = ROOT / "app/src/main/java/com/example/data/model/VerifiedAgentQuestionBank.kt"


SOURCE_NAMES = {
    "bangla": "বাংলা",
    "english": "English for Today",
    "math": "প্রাথমিক গণিত",
    "science": "প্রাথমিক বিজ্ঞান",
    "bgs": "বাংলাদেশ ও বিশ্বপরিচয়",
    "islam": "ইসলাম শিক্ষা",
    "hindu": "হিন্দুধর্ম ও নৈতিক শিক্ষা",
    "buddhist": "বৌদ্ধধর্ম ও নৈতিক শিক্ষা",
    "christian": "খ্রিষ্টধর্ম ও নৈতিক শিক্ষা",
}


def q(value: object) -> str:
    return str(value or "").replace("\\", "\\\\").replace('"', '\\"').replace("\n", "\\n")


def first(items: object, fallback: int = 1) -> int:
    values = list(items or [])
    return int(values[0]) if values else fallback


def normalize(value: object) -> str:
    text = str(value or "").lower()
    text = re.sub(r"[?!.।,:;“”\"‘’()\-–—]", " ", text)
    return " ".join(text.split())


def load_rows() -> list[dict[str, object]]:
    rows: list[dict[str, object]] = []

    for class1 in sorted((REPORTS / "agent-class1").glob("class1-consolidated-batch-*-verified.json")):
        for group in json.loads(class1.read_text(encoding="utf-8")):
            if group.get("verificationStatus") != "second_pass_complete":
                continue
            for item in group.get("entries", []):
                follow = item.get("followUps", {})
                rows.append({
                    "grade": 1, "book": group["subject"],
                    # App page assets and tutor page lookup use PDF page numbers.
                    # Printed page numbers can differ because of covers and front matter.
                    "page": first(group.get("sourcePages") or group.get("printedPages")),
                    "source": f'{SOURCE_NAMES[group["subject"]]} — {group["chapter"]}',
                    "question": item["question"], "answer": item["answer"],
                    "explanation": item["explanation"], "bn": follow.get("bn", ""),
                    "banglish": follow.get("banglish", ""), "en": follow.get("en", ""),
                })

    for class2 in sorted((REPORTS / "agent-class2").glob("class2-consolidated-batch-*.json")):
        data = json.loads(class2.read_text(encoding="utf-8"))
        # A source-note suffix records a verified textbook typo or similar
        # editorial caveat; it does not invalidate the second-pass review.
        if str(data.get("verificationStatus", "")).startswith("second_pass_complete"):
            for group in data.get("books", []):
                for item in group.get("entries", []):
                    follow = item.get("followUps", {})
                    rows.append({
                        "grade": 2, "book": group["subject"],
                        "page": first(group.get("sourcePages") or group.get("printedPages")),
                        "source": f'{SOURCE_NAMES[group["subject"]]} — {group["chapter"]}',
                        "question": item["question"], "answer": item["answer"],
                        "explanation": item["explanation"], "bn": follow.get("bn", ""),
                        "banglish": follow.get("banglish", ""), "en": follow.get("en", ""),
                    })

    for core in sorted((REPORTS / "agent-class3-5-core").glob("verified-round-robin-batch-*.json")):
        data = json.loads(core.read_text(encoding="utf-8"))
        if data.get("reviewStatus") == "second-pass-verified":
            for item in data.get("entries", []):
                rows.append({
                    "grade": item["grade"], "book": item["subject"],
                    "page": page_from_item(item),
                    "source": f'{item.get("book") or SOURCE_NAMES[item["subject"]]} — পৃষ্ঠা {page_from_item(item)}',
                    "question": item["question"], "answer": item["answer"],
                    "explanation": item["explanation"],
                    "bn": item["followUpBangla"],
                    "banglish": item["followUpBanglish"],
                    "en": item["followUpEnglish"],
                })

    for path in sorted((REPORTS / "agent-class3").glob("*.json")):
        data = json.loads(path.read_text(encoding="utf-8"))
        if data.get("verificationStatus") != "second_pass_complete":
            continue
        for item in data.get("entries", []):
            follow = item.get("followUps", {})
            rows.append({
                "grade": 3, "book": data["subject"],
                "page": int(item.get("sourcePage") or first(data.get("sourcePages") or data.get("printedPages"))),
                "source": f'{SOURCE_NAMES[data["subject"]]} — {data["chapter"]}',
                "question": item["question"], "answer": item["answer"],
                "explanation": item["explanation"],
                "bn": follow.get("bn", ""), "banglish": follow.get("banglish", ""),
                "en": follow.get("en", ""),
            })

    for path in sorted((REPORTS / "agent-class4").glob("*.json")):
        data = json.loads(path.read_text(encoding="utf-8"))
        if data.get("status") == "superseded":
            continue
        for group in data.get("entries") or data.get("pages") or []:
            follow_value = group.get("follow_ups") or group.get("followUpSet") or {}
            follow = follow_value[0] if isinstance(follow_value, list) and follow_value else follow_value
            exercise_pages = list(group.get("exercise_pages") or group.get("source_pages") or [1])
            counts = list(group.get("page_question_counts") or [])
            page_sequence: list[int] = []
            if counts and len(counts) == len(exercise_pages) and sum(map(int, counts)) == len(group.get("questions", [])):
                for page, count in zip(exercise_pages, counts):
                    page_sequence.extend([int(page)] * int(count))
            else:
                page_sequence = [int(exercise_pages[0])] * len(group.get("questions", []))
            for item, page in zip(group.get("questions", []), page_sequence):
                rows.append({
                    "grade": 4, "book": group["subject"],
                    "page": int(item.get("page") or page),
                    "source": f'{SOURCE_NAMES[group["subject"]]} — {group["chapter"]}',
                    "question": item["question"], "answer": item["answer"],
                    "explanation": item["explanation"],
                    "bn": first_text(follow.get("examples_bn")),
                    "banglish": first_text(follow.get("examples_banglish")),
                    "en": first_text(follow.get("examples_en")),
                })

    for path in sorted((REPORTS / "agent-class5").glob("*.json")):
        data = json.loads(path.read_text(encoding="utf-8"))
        for group in data.get("pages", []):
            follow = group.get("followUpSet") or group.get("follow_ups") or data.get("contextualFollowUps", {})
            rows.extend({
                "grade": 5, "book": group["subject"],
                "page": first(group.get("exercisePages") or group.get("sourcePages")),
                "source": f'{SOURCE_NAMES[group["subject"]]} — {group["chapter"]}',
                "question": item["question"], "answer": item["answer"],
                "explanation": item["explanation"],
                "bn": first_text(follow.get("bn") or follow.get("examples_bn")),
                "banglish": first_text(follow.get("banglish") or follow.get("examples_banglish")),
                "en": first_text(follow.get("en") or follow.get("examples_en")),
            } for item in group.get("questions", []))
    return rows


def first_text(value: object) -> str:
    if isinstance(value, dict):
        return str(value.get("prompt") or value.get("question") or value.get("answer") or "")
    if isinstance(value, list):
        return str(value[0]) if value else ""
    return str(value or "")


def page_from_item(item: dict[str, object]) -> int:
    """Accept both the older explicit page field and newer asset-only batches."""
    for key in ("printedPage", "page"):
        value = item.get(key)
        if value not in (None, ""):
            return int(value)
    asset = str(item.get("sourceAsset") or "")
    match = re.search(r"page_(\d+)\.(?:webp|png|jpg)$", asset, re.IGNORECASE)
    if not match:
        raise ValueError(f"No source page in reviewed row: {item!r}")
    return int(match.group(1))


def main() -> None:
    existing_text = EXISTING.read_text(encoding="utf-8")
    existing_keys: set[tuple[int, str, str]] = set()
    existing_duplicate_keys: list[tuple[int, str, str]] = []
    pattern = re.compile(
        r'OfflineBookQuestion\((\d+),\s*"([^"]+)",\s*\d+,\s*'
        r'"(?:\\.|[^"])*",\s*"((?:\\.|[^"])*)"'
    )
    for match in pattern.finditer(existing_text):
        grade, book, question = match.groups()
        existing_key = (int(grade), book, normalize(question.replace('\\"', '"')))
        if existing_key in existing_keys:
            existing_duplicate_keys.append(existing_key)
        existing_keys.add(existing_key)
    rows = load_rows()
    unique: dict[tuple[int, str, str], dict[str, object]] = {}
    skipped_existing = 0
    for row in rows:
        key = (int(row["grade"]), str(row["book"]), normalize(row["question"]))
        if key in existing_keys:
            skipped_existing += 1
            continue
        unique.setdefault(key, row)

    chunk_size = 75
    row_chunks = [list(unique.values())[i:i + chunk_size] for i in range(0, len(unique), chunk_size)]
    lines = [
        "package com.example.data.model",
        "",
        "/** Generated only from second-pass source-reviewed agent artifacts. */",
        "object VerifiedAgentQuestionBank {",
        "    val entries: List<OfflineBookQuestion> by lazy {",
        "        buildList {",
    ]
    for index in range(len(row_chunks)):
        lines.append(f"            addAll(chunk{index}())")
    lines.extend(["        }", "    }"])
    for index, chunk in enumerate(row_chunks):
        lines.extend(["", f"    private fun chunk{index}(): List<OfflineBookQuestion> = listOf("])
        for row in chunk:
            lines.append(
                "        OfflineBookQuestion("
                f'{row["grade"]}, "{q(row["book"])}", {row["page"]}, '
                f'"{q(row["source"])}", "{q(row["question"])}", "{q(row["answer"])}", '
                f'"{q(row["explanation"])}", "{q(row["bn"])}", '
                f'"{q(row["banglish"])}", "{q(row["en"])}"),'
            )
        lines.append("    )")
    lines.extend(["}", ""])
    OUTPUT.write_text("\n".join(lines), encoding="utf-8")
    print(json.dumps({
        "agent_rows": len(rows),
        "skipped_existing": skipped_existing,
        "generated_unique": len(unique),
        "existing_duplicate_keys": existing_duplicate_keys,
        "output": str(OUTPUT),
    }, ensure_ascii=False))


if __name__ == "__main__":
    main()
