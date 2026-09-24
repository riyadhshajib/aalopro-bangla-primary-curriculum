#!/usr/bin/env python3
"""Fast, deterministic structural audit of AaloPro curriculum source.

This does not claim textbook completeness or answer correctness. It identifies
where curriculum declarations, direct Q&A, page questions, sources, and
catalog-only placeholders live so that deeper verification can be prioritized.
"""

from __future__ import annotations

import csv
import re
from dataclasses import dataclass, asdict
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
MODEL_DIR = ROOT / "app" / "src" / "main" / "java" / "com" / "example" / "data" / "model"
REPORT_DIR = ROOT / "build" / "reports" / "curriculum"


@dataclass
class FileInventory:
    file: str
    curriculum_topics: int
    chapter_rows: int
    lesson_rows: int
    direct_qa_literals: int
    visual_exercise_literals: int
    page_question_maps: int
    source_references: int
    catalog_only_markers: int
    question_literals: int
    answer_literals: int


def count(pattern: str, text: str) -> int:
    return len(re.findall(pattern, text, flags=re.MULTILINE))


def inspect_file(path: Path) -> FileInventory:
    text = path.read_text(encoding="utf-8")
    return FileInventory(
        file=path.name,
        curriculum_topics=count(r"\bCurriculumTopic\s*\(", text),
        chapter_rows=max(0, count(r"\bChapter\s*\(", text) - count(r"data\s+class\s+Chapter\s*\(", text)),
        lesson_rows=max(0, count(r"\bLesson\s*\(", text) - count(r"data\s+class\s+Lesson\s*\(", text)),
        direct_qa_literals=count(r"\bTopicQA\s*\(", text),
        visual_exercise_literals=count(r"\bVisualExercise\s*\(", text),
        page_question_maps=count(r"pageQuestions\s*=", text),
        source_references=count(r"sourceBangla\s*=", text),
        catalog_only_markers=count(r"catalogOnly\s*=\s*true", text),
        question_literals=count(r"\bquestion\s*=", text) + count(r"questionBangla\s*=", text),
        answer_literals=count(r"\banswer\s*=", text) + count(r"answerBangla\s*=", text),
    )


def main() -> None:
    paths = sorted(MODEL_DIR.glob("*.kt"))
    records = [inspect_file(path) for path in paths]
    relevant = [
        record for record in records
        if any(
            (
                record.curriculum_topics,
                record.chapter_rows,
                record.lesson_rows,
                record.direct_qa_literals,
                record.visual_exercise_literals,
                record.catalog_only_markers,
            )
        )
    ]

    REPORT_DIR.mkdir(parents=True, exist_ok=True)
    csv_path = REPORT_DIR / "structure.csv"
    md_path = REPORT_DIR / "structure.md"

    with csv_path.open("w", encoding="utf-8-sig", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=list(asdict(relevant[0]).keys()))
        writer.writeheader()
        writer.writerows(asdict(record) for record in relevant)

    totals = {
        field: sum(getattr(record, field) for record in relevant)
        for field in FileInventory.__dataclass_fields__
        if field != "file"
    }

    lines = [
        "# AaloPro curriculum structural audit",
        "",
        "This is a fast source-structure inventory. It is **not** proof that every textbook exercise is present or that an answer is correct. Generated topics and page ranges can make literal counts differ from runtime counts.",
        "",
        f"- Relevant source files: {len(relevant)}",
        f"- Literal curriculum topics: {totals['curriculum_topics']}",
        f"- Generated chapter rows: {totals['chapter_rows']}",
        f"- Generated lesson rows: {totals['lesson_rows']}",
        f"- Literal direct Q&A entries: {totals['direct_qa_literals']}",
        f"- Literal source references: {totals['source_references']}",
        f"- Catalog-only markers: {totals['catalog_only_markers']}",
        "",
        "| File | Topics | Chapter rows | Lesson rows | Direct Q&A | Source refs | Catalog-only | Question fields | Answer fields |",
        "|---|---:|---:|---:|---:|---:|---:|---:|---:|",
    ]
    for record in relevant:
        lines.append(
            f"| {record.file} | {record.curriculum_topics} | {record.chapter_rows} | "
            f"{record.lesson_rows} | {record.direct_qa_literals} | {record.source_references} | "
            f"{record.catalog_only_markers} | {record.question_literals} | {record.answer_literals} |"
        )

    lines.extend(
        [
            "",
            "## Interpretation rules",
            "",
            "- A chapter/title in a catalog is navigation coverage, not verified answer coverage.",
            "- A generic generated `TopicQA` is not automatically a captured textbook exercise.",
            "- Files with many topics but few direct Q&A/source references are the first manual-audit priority.",
            "- The next deeper audit must compare each book page/exercise to a source-linked entry and record missing items by class, subject, chapter, and page.",
        ]
    )
    md_path.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(md_path)
    print(csv_path)


if __name__ == "__main__":
    main()
