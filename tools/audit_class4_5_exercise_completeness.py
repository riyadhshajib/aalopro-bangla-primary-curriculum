#!/usr/bin/env python3
"""Classify evidenced Class 4-5 exercise pages and report exercise-level gaps.

The classifier is deliberately conservative. It never treats an unclassified
textbook page as an exercise gap. A page becomes a confirmed exercise page only
when a source-reviewed artifact names it as an exercise page, a reviewed flat
entry cites it, or an existing OCR review row contains an explicit exercise
heading. OCR is used only to locate a review gap, never to create an answer.
"""

from __future__ import annotations

import csv
import json
import re
from collections import Counter, defaultdict
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
REPORTS = ROOT / "build/reports/curriculum"
ASSETS = ROOT / "app/src/main/assets/books"
OUT_PAGES = REPORTS / "class-4-5-exercise-page-classification.csv"
OUT_EXERCISES = REPORTS / "class-4-5-exercise-completeness.csv"
OUT_REPORT = REPORTS / "class-4-5-exercise-completeness.md"
ARTIFACT_DIRS = (
    REPORTS / "agent-class4",
    REPORTS / "agent-class5",
    REPORTS / "agent-class3-5-core",
)
FOLLOWUP_REMEDIATION = REPORTS / "agent-class5/class5-religion-followup-remediation.json"
BANKS = (
    ROOT / "app/src/main/java/com/example/data/model/VerifiedOfflineQuestionBank.kt",
    ROOT / "app/src/main/java/com/example/data/model/VerifiedAgentQuestionBank.kt",
)
SUBJECTS = ("bangla", "english", "math", "science", "bgs", "islam", "hindu", "buddhist", "christian")
BANK_RE = re.compile(r'OfflineBookQuestion\(\s*(\d+)\s*,\s*"([^"]+)"\s*,\s*(\d+)\s*,')
PAGE_RE = re.compile(r"page_(\d+)\.webp$", re.I)
STRONG_EXERCISE_RE = re.compile(
    r"অনুশীলনী(?:\s|$|\|)|শূন্যস্থান|সঠিক উত্তর|টিক\s*\(?|সংক্ষেপে উত্তর|বর্ণনামূলক উত্তর|"
    r"প্রশ্নগুলোর উত্তর|মিল করো|বলি ও লিখি|লিখে উত্তর|exercise|answer the following|"
    r"fill in the blanks?|true or false|match (?:the following|column)", re.I
)


def as_list(value):
    if value is None:
        return []
    return value if isinstance(value, list) else [value]


def page_from_asset(value: str | None) -> int | None:
    match = PAGE_RE.search(value or "")
    return int(match.group(1)) if match else None


def infer_grade(path: Path, data: dict, entry: dict) -> int | None:
    grade = entry.get("grade") or data.get("grade")
    if grade:
        return int(grade)
    match = re.search(r"agent-class([45])", path.as_posix())
    return int(match.group(1)) if match else None


def complete_question(question: dict) -> bool:
    return all(str(question.get(field, "")).strip() for field in ("question", "answer", "explanation"))


def complete_follow_up(entry: dict, question: dict | None = None) -> bool:
    record = question or {}
    if all(str(record.get(field, "")).strip() for field in
           ("followUpBangla", "followUpBanglish", "followUpEnglish")):
        return True
    follow = entry.get("follow_ups") or entry.get("followUps") or {}
    if isinstance(follow, list):
        return bool(follow) and all(bool(str(item).strip()) for item in follow)
    if isinstance(follow, dict):
        groups = (
            follow.get("examples_bn") or follow.get("bangla"),
            follow.get("examples_banglish") or follow.get("banglish"),
            follow.get("examples_en") or follow.get("english"),
        )
        return all(bool(as_list(group)) and all(str(item).strip() for item in as_list(group)) for group in groups)
    return False


def artifact_rows():
    rows = []
    context_pages: set[tuple[int, str, int]] = set()
    for directory in ARTIFACT_DIRS:
        for path in sorted(directory.glob("*.json")):
            if path == FOLLOWUP_REMEDIATION:
                continue
            try:
                data = json.loads(path.read_text(encoding="utf-8-sig"))
            except (json.JSONDecodeError, UnicodeDecodeError):
                continue
            if data.get("status") == "superseded":
                continue
            containers = as_list(data.get("entries")) + as_list(data.get("pages"))
            for index, entry in enumerate(containers, 1):
                if not isinstance(entry, dict):
                    continue
                grade = infer_grade(path, data, entry)
                subject = entry.get("subject") or entry.get("book")
                if grade not in (4, 5) or subject not in SUBJECTS:
                    continue
                questions = [q for q in as_list(entry.get("questions")) if isinstance(q, dict)]
                if not questions and entry.get("question"):
                    questions = [entry]
                exercise_pages = [int(p) for p in as_list(entry.get("exercise_pages") or entry.get("exercisePages"))]
                asset_page = page_from_asset(entry.get("sourceAsset"))
                if not exercise_pages and asset_page:
                    exercise_pages = [asset_page]
                source_pages = [int(p) for p in as_list(entry.get("source_pages") or entry.get("sourcePages"))]
                context_pages.update((grade, subject, page) for page in source_pages if page not in exercise_pages)
                if not exercise_pages:
                    continue
                rows.append({
                    "grade": grade,
                    "subject": subject,
                    "exercise_pages": exercise_pages,
                    "artifact": path.relative_to(ROOT).as_posix(),
                    "artifact_entry": index,
                    "question_count": len(questions),
                    "answers_complete": bool(questions) and all(complete_question(q) for q in questions),
                    "followups_complete": bool(questions) and (
                        all(complete_follow_up(entry, q) for q in questions)
                        if all(any(k in q for k in ("followUpBangla", "followUpBanglish", "followUpEnglish")) for q in questions)
                        else complete_follow_up(entry)
                    ),
                })
    if FOLLOWUP_REMEDIATION.exists():
        supplement = json.loads(FOLLOWUP_REMEDIATION.read_text(encoding="utf-8-sig"))
        supplemental = {
            (int(item["grade"]), item["subject"], tuple(int(p) for p in item["exercisePages"]))
            for item in supplement.get("entries", [])
            if all(str(item.get(field, "")).strip() for field in
                   ("followUpBangla", "followUpBanglish", "followUpEnglish"))
        }
        for row in rows:
            key = (row["grade"], row["subject"], tuple(row["exercise_pages"]))
            if key in supplemental:
                row["followups_complete"] = True
                row["artifact"] += f" + {FOLLOWUP_REMEDIATION.relative_to(ROOT).as_posix()}"
    return rows, context_pages


def ocr_exercise_candidates() -> dict[tuple[int, str, int], list[str]]:
    found: dict[tuple[int, str, int], list[str]] = defaultdict(list)
    batches = REPORTS / "religion-review/batches"
    for path in batches.glob("g[45]-*-batch-*.csv"):
        for row in csv.DictReader(path.open(encoding="utf-8-sig")):
            text = row.get("prompt_candidates", "")
            if STRONG_EXERCISE_RE.search(text):
                key = (int(row["grade"]), row["subject"], int(row["page"]))
                found[key].append(path.relative_to(ROOT).as_posix())
    return found


def main() -> None:
    artifacts, context_pages = artifact_rows()
    reviewed_pages = {
        (row["grade"], row["subject"], page)
        for row in artifacts for page in row["exercise_pages"]
    }
    ocr_candidates = ocr_exercise_candidates()
    production = Counter()
    for bank in BANKS:
        for grade, subject, page in BANK_RE.findall(bank.read_text(encoding="utf-8")):
            if int(grade) in (4, 5):
                production[(int(grade), subject, int(page))] += 1

    page_rows = []
    all_pages = []
    for grade in (4, 5):
        for subject in SUBJECTS:
            for asset in sorted((ASSETS / f"class{grade}" / subject).glob("page_*.webp")):
                page = int(asset.stem.removeprefix("page_"))
                key = (grade, subject, page)
                all_pages.append(key)
                if key in reviewed_pages:
                    classification = "confirmed_exercise_reviewed_artifact"
                    basis = "source-reviewed artifact exercise_pages/sourceAsset"
                elif key in ocr_candidates:
                    classification = "confirmed_exercise_editorial_gap"
                    basis = "explicit exercise heading in existing OCR review queue"
                elif key in context_pages:
                    classification = "reviewed_lesson_context_not_exercise"
                    basis = "source-reviewed artifact source_pages excluding exercise_pages"
                else:
                    classification = "unclassified_not_counted_as_gap"
                    basis = "no deterministic exercise evidence"
                page_rows.append({
                    "grade": grade, "subject": subject, "page": page,
                    "classification": classification, "basis": basis,
                    "production_answers": production[key],
                    "asset": asset.relative_to(ROOT).as_posix(),
                })

    exercise_rows = []
    for row in artifacts:
        prod = sum(production[(row["grade"], row["subject"], page)] for page in row["exercise_pages"])
        status = "artifact_complete_integrated" if prod >= row["question_count"] else "artifact_complete_pending_bank_integration"
        if not row["answers_complete"] or not row["followups_complete"]:
            status = "artifact_editorial_gap"
        exercise_rows.append({
            "grade": row["grade"], "subject": row["subject"],
            "exercise_pages": ",".join(map(str, row["exercise_pages"])),
            "artifact_questions": row["question_count"], "production_answers_on_pages": prod,
            "answers_explanations_complete": str(row["answers_complete"]).lower(),
            "multilingual_followups_complete": str(row["followups_complete"]).lower(),
            "status": status, "evidence": f"{row['artifact']}#entry-{row['artifact_entry']}",
        })
    for key, sources in sorted(ocr_candidates.items()):
        if key in reviewed_pages:
            continue
        grade, subject, page = key
        exercise_rows.append({
            "grade": grade, "subject": subject, "exercise_pages": str(page),
            "artifact_questions": 0, "production_answers_on_pages": production[key],
            "answers_explanations_complete": "false", "multilingual_followups_complete": "false",
            "status": "confirmed_exercise_editorial_gap",
            "evidence": ";".join(sorted(set(sources))),
        })

    OUT_PAGES.parent.mkdir(parents=True, exist_ok=True)
    for path, rows in ((OUT_PAGES, page_rows), (OUT_EXERCISES, exercise_rows)):
        with path.open("w", encoding="utf-8-sig", newline="") as handle:
            writer = csv.DictWriter(handle, fieldnames=rows[0].keys())
            writer.writeheader(); writer.writerows(rows)

    page_counts = Counter(row["classification"] for row in page_rows)
    status_counts = Counter(row["status"] for row in exercise_rows)
    by_book = defaultdict(Counter)
    for row in exercise_rows:
        by_book[(int(row["grade"]), row["subject"])][row["status"]] += 1
    lines = [
        "# Classes 4 and 5 deterministic exercise completeness audit", "",
        "This audit counts a gap only when deterministic evidence identifies an exercise page. Ordinary lesson context is separated from unresolved pages. Unclassified pages are not reported as missing exercises.", "",
        f"- Bundled pages classified: **{len(page_rows)}**",
        f"- Confirmed exercise pages backed by reviewed artifacts: **{page_counts['confirmed_exercise_reviewed_artifact']}**",
        f"- Confirmed exercise pages still needing editorial work: **{page_counts['confirmed_exercise_editorial_gap']}**",
        f"- Confirmed lesson/context pages, not exercises: **{page_counts['reviewed_lesson_context_not_exercise']}**",
        f"- Pages still unclassified and not counted as gaps: **{page_counts['unclassified_not_counted_as_gap']}**", "",
        "## Exercise groups", "",
        f"- Complete and integrated: **{status_counts['artifact_complete_integrated']}**",
        f"- Complete reviewed artifact pending production-bank integration: **{status_counts['artifact_complete_pending_bank_integration']}**",
        f"- Artifact with answer/explanation/follow-up gap: **{status_counts['artifact_editorial_gap']}**",
        f"- Confirmed exercise page awaiting source review: **{status_counts['confirmed_exercise_editorial_gap']}**", "",
        "| Class | Subject | Integrated | Pending integration | Artifact gap | Source-review gap |", "|---:|---|---:|---:|---:|---:|",
    ]
    for key in sorted(by_book):
        count = by_book[key]
        lines.append(f"| {key[0]} | {key[1]} | {count['artifact_complete_integrated']} | {count['artifact_complete_pending_bank_integration']} | {count['artifact_editorial_gap']} | {count['confirmed_exercise_editorial_gap']} |")
    lines += ["", f"Page evidence: `{OUT_PAGES.relative_to(ROOT).as_posix()}`", f"Exercise evidence: `{OUT_EXERCISES.relative_to(ROOT).as_posix()}`", "", "No production bank was regenerated by this audit."]
    OUT_REPORT.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(OUT_REPORT)
    print(dict(page_counts)); print(dict(status_counts))


if __name__ == "__main__":
    main()
