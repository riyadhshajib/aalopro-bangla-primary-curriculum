#!/usr/bin/env python3
"""Deterministically validate source-reviewed Class 4 and 5 artifacts.

This does not promote content. It checks that reviewed JSON is parseable, that
every cited asset exists, and that each candidate has an answer, explanation,
and Bangla, Banglish, and English follow-up coverage.
"""

from __future__ import annotations

import json
import re
from collections import Counter
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
REPORTS = ROOT / "build/reports/curriculum"
DIRECTORIES = (REPORTS / "agent-class4", REPORTS / "agent-class5")
OUT_JSON = REPORTS / "class-4-5-artifact-validation.json"
OUT_MD = REPORTS / "class-4-5-artifact-validation.md"


def as_list(value):
    if value is None:
        return []
    return value if isinstance(value, list) else [value]


def multilingual_groups_complete(follow: dict) -> bool:
    groups = (
        follow.get("examples_bn") or follow.get("bangla") or follow.get("bn"),
        follow.get("examples_banglish") or follow.get("banglish"),
        follow.get("examples_en") or follow.get("english") or follow.get("en"),
    )
    return all(any(str(item).strip() for item in as_list(group)) for group in groups)


def has_followups(data: dict, entry: dict, question: dict) -> bool:
    direct = ("followUpBangla", "followUpBanglish", "followUpEnglish")
    if all(str(question.get(key, "")).strip() for key in direct):
        return True
    follow_set = entry.get("followUpSet")
    if isinstance(follow_set, dict) and all(
        isinstance(follow_set.get(key), dict)
        and str(follow_set[key].get("prompt", "")).strip()
        and str(follow_set[key].get("answer", "")).strip()
        for key in ("bn", "banglish", "en")
    ):
        return True
    follow = entry.get("follow_ups") or entry.get("followUps") or data.get("contextualFollowUps")
    if isinstance(follow, list):
        follow = follow[0] if follow else {}
    if not isinstance(follow, dict):
        return False
    return multilingual_groups_complete(follow)


def asset_paths(entry: dict, subject: str, grade: int) -> list[Path]:
    values = as_list(entry.get("asset_pages"))
    if entry.get("sourceAsset"):
        values.append(entry["sourceAsset"])
    if not values:
        pages = as_list(entry.get("exercise_pages") or entry.get("exercisePages"))
        values = [f"app/src/main/assets/books/class{grade}/{subject}/page_{int(page):03d}.webp" for page in pages]
    normalized = []
    for value in values:
        path = Path(str(value).replace("books/", "app/src/main/assets/books/", 1)) if str(value).startswith("books/") else Path(value)
        normalized.append(ROOT / path)
    return normalized


def main() -> None:
    files = entries = questions = 0
    problems = []
    subjects = Counter()
    seen = Counter()
    for directory in DIRECTORIES:
        grade = int(re.search(r"class([45])", directory.name).group(1))
        for path in sorted(directory.glob("*.json")):
            if path.name.endswith("remediation.json"):
                continue
            files += 1
            try:
                data = json.loads(path.read_text(encoding="utf-8-sig"))
            except Exception as exc:
                problems.append({"artifact": path.relative_to(ROOT).as_posix(), "problem": f"invalid JSON: {exc}"})
                continue
            if data.get("status") == "superseded":
                files -= 1
                continue
            containers = as_list(data.get("entries")) + as_list(data.get("pages"))
            for index, entry in enumerate(containers, 1):
                if not isinstance(entry, dict):
                    continue
                entries += 1
                subject = str(entry.get("subject") or entry.get("book") or "").strip()
                if not subject:
                    problems.append({"artifact": path.relative_to(ROOT).as_posix(), "entry": index, "problem": "missing subject"})
                    continue
                subjects[f"{grade}:{subject}"] += 1
                records = [q for q in as_list(entry.get("questions")) if isinstance(q, dict)]
                if not records and entry.get("question"):
                    records = [entry]
                if not records:
                    problems.append({"artifact": path.relative_to(ROOT).as_posix(), "entry": index, "problem": "no questions"})
                    continue
                questions += len(records)
                for record_index, record in enumerate(records, 1):
                    missing = [field for field in ("question", "answer", "explanation") if not str(record.get(field, "")).strip()]
                    if missing:
                        problems.append({"artifact": path.relative_to(ROOT).as_posix(), "entry": index, "question": record_index, "problem": "missing " + ", ".join(missing)})
                    if not has_followups(data, entry, record):
                        problems.append({"artifact": path.relative_to(ROOT).as_posix(), "entry": index, "question": record_index, "problem": "missing multilingual follow-up"})
                    key = (grade, subject, str(record.get("question", "")).strip())
                    seen[key] += 1
                for asset in asset_paths(entry, subject, grade):
                    if not asset.is_file():
                        problems.append({"artifact": path.relative_to(ROOT).as_posix(), "entry": index, "problem": f"missing asset {asset.relative_to(ROOT).as_posix()}"})
    duplicates = [
        {"grade": key[0], "subject": key[1], "question": key[2], "count": count}
        for key, count in seen.items() if count > 1
    ]
    result = {
        "artifacts": files,
        "entries": entries,
        "questions": questions,
        "subjects": dict(sorted(subjects.items())),
        "blockingProblems": problems,
        "duplicateQuestions": duplicates,
        "status": "pass" if not problems else "fail",
        "boundary": "Structural validation only. Visual source review remains authoritative.",
    }
    OUT_JSON.write_text(json.dumps(result, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    lines = [
        "# Classes 4 and 5 reviewed-artifact validation", "",
        f"- Artifacts checked: **{files}**",
        f"- Entries checked: **{entries}**",
        f"- Questions checked: **{questions}**",
        f"- Blocking structural or asset problems: **{len(problems)}**",
        f"- Duplicate question texts within the same class and subject: **{len(duplicates)}**",
        f"- Result: **{result['status']}**", "",
        "This is a deterministic structural and asset validation. It does not replace visual source review and does not promote any record to the production bank.", "",
    ]
    if problems:
        lines += ["## Blocking problems", ""] + [f"- `{item}`" for item in problems]
    OUT_MD.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(json.dumps({"status": result["status"], "artifacts": files, "entries": entries, "questions": questions, "problems": len(problems), "duplicates": len(duplicates)}))


if __name__ == "__main__":
    main()
