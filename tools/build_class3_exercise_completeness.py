import json
from collections import Counter, defaultdict
from pathlib import Path

ROOT = Path(__file__).parents[1]
REPORTS = ROOT / "build/reports/curriculum/agent-class3"
SWEEP = REPORTS / "class3-full-visual-sweep.json"
OUT = REPORTS / "class3-exercise-completeness.json"


def load_entries(path: Path):
    try:
        payload = json.loads(path.read_text(encoding="utf-8-sig"))
    except (UnicodeDecodeError, json.JSONDecodeError):
        return []
    subject = payload.get("subject") or payload.get("book")
    default_pages = payload.get("sourcePages") or []
    default_page = default_pages[0] if default_pages else payload.get("sourcePage")
    rows = []
    for entry in payload.get("entries", []):
        page = entry.get("sourcePage", default_page)
        if not isinstance(page, int) or not subject:
            continue
        followups = entry.get("followUps") or {}
        rows.append({
            "subject": subject,
            "sourcePage": page,
            "question": entry.get("question", "").strip(),
            "answer": entry.get("answer", "").strip(),
            "explanation": entry.get("explanation", "").strip(),
            "followUpBangla": followups.get("bn", entry.get("followUpBangla", "")).strip(),
            "followUpBanglish": followups.get("banglish", entry.get("followUpBanglish", "")).strip(),
            "followUpEnglish": followups.get("en", entry.get("followUpEnglish", "")).strip(),
            "sourceFile": path.name,
        })
    return rows


sweep = json.loads(SWEEP.read_text(encoding="utf-8-sig"))
entries = []
for path in sorted(REPORTS.glob("g3-*.json")):
    entries.extend(load_entries(path))

by_page = defaultdict(list)
for entry in entries:
    by_page[(entry["subject"], entry["sourcePage"])].append(entry)

pages = []
for page in sweep["pages"]:
    key = (page["subject"], page["sourcePage"])
    page_entries = by_page.get(key, [])
    rich = [entry for entry in page_entries if all([
        entry["question"], entry["answer"], entry["explanation"],
        entry["followUpBangla"], entry["followUpBanglish"], entry["followUpEnglish"],
    ])]
    visual_category = page["category"]
    if visual_category == "exercise_or_open_activity" or page_entries:
        exercise_status = "confirmed_exercise"
    elif visual_category in {"contents_preface", "lesson_only", "other"}:
        exercise_status = "confirmed_non_exercise"
    else:
        exercise_status = "unclassified"
    if exercise_status == "confirmed_exercise" and page_entries and len(rich) == len(page_entries):
        completeness = "has_rich_reviewed_entries"
    elif exercise_status == "confirmed_exercise" and page_entries:
        completeness = "has_incomplete_reviewed_entries"
    elif exercise_status == "confirmed_exercise":
        completeness = "exercise_without_reviewed_entry"
    elif exercise_status == "confirmed_non_exercise":
        completeness = "not_applicable"
    else:
        completeness = "unclassified"
    pages.append({
        "grade": 3,
        "subject": page["subject"],
        "sourcePage": page["sourcePage"],
        "asset": page["asset"],
        "visualCategory": visual_category,
        "exerciseStatus": exercise_status,
        "completeness": completeness,
        "reviewedEntryCount": len(page_entries),
        "richEntryCount": len(rich),
        "sourceFiles": sorted({entry["sourceFile"] for entry in page_entries}),
    })

counts = Counter(page["exerciseStatus"] for page in pages)
completeness = Counter(page["completeness"] for page in pages)
subject_summary = {}
for subject in sorted({page["subject"] for page in pages}):
    subset = [page for page in pages if page["subject"] == subject]
    subject_summary[subject] = {
        "pages": len(subset),
        "confirmedExercise": sum(page["exerciseStatus"] == "confirmed_exercise" for page in subset),
        "confirmedNonExercise": sum(page["exerciseStatus"] == "confirmed_non_exercise" for page in subset),
        "unclassified": sum(page["exerciseStatus"] == "unclassified" for page in subset),
        "exercisePagesWithRichEntries": sum(page["completeness"] == "has_rich_reviewed_entries" for page in subset),
        "exercisePagesWithoutReviewedEntry": sum(page["completeness"] == "exercise_without_reviewed_entry" for page in subset),
        "reviewedEntries": sum(page["reviewedEntryCount"] for page in subset),
        "richEntries": sum(page["richEntryCount"] for page in subset),
    }

payload = {
    "grade": 3,
    "method": "Deterministic join of the human visual page classification and source-reviewed Class 3 batch artifacts. No OCR-only page is promoted to confirmed exercise or complete.",
    "summary": {
        "totalPages": len(pages),
        "confirmedExercisePages": counts["confirmed_exercise"],
        "confirmedNonExercisePages": counts["confirmed_non_exercise"],
        "unclassifiedPages": counts["unclassified"],
        "exercisePagesWithRichEntries": completeness["has_rich_reviewed_entries"],
        "exercisePagesWithIncompleteEntries": completeness["has_incomplete_reviewed_entries"],
        "exercisePagesWithoutReviewedEntry": completeness["exercise_without_reviewed_entry"],
        "parsedReviewedEntries": len(entries),
        "richReviewedEntries": sum(1 for entry in entries if all([
            entry["question"], entry["answer"], entry["explanation"],
            entry["followUpBangla"], entry["followUpBanglish"], entry["followUpEnglish"],
        ])),
    },
    "subjects": subject_summary,
    "pages": pages,
}
OUT.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
print(json.dumps(payload["summary"], ensure_ascii=False))
for subject, summary in subject_summary.items():
    print(subject, json.dumps(summary, ensure_ascii=False))
