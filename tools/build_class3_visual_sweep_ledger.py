import json
from pathlib import Path

ROOT = Path(__file__).parents[1]
ASSETS = ROOT / "app/src/main/assets/books/class3"
OUT = ROOT / "build/reports/curriculum/agent-class3/class3-full-visual-sweep.json"

reviewed = {
    "bangla": {
        "contents_preface": set(range(1, 10)),
        "lesson_only": {10, 12, 13, 15, 16, 17, 18, 19, 28, 29, 30, 32, 35, 36,
                        40, 41, 42, 43, 46, 47, 51, 55, 59, 64, 67, 68, 71, 74,
                        78, 81, 85, 89, 93, 95, 96, 99, 101, 104},
        "other": {108, 109, 110},
    },
    "bgs": {
        "contents_preface": set(range(1, 6)),
        "lesson_only": set(),
        "other": {117, 118},
    },
    "english": {
        "contents_preface": set(range(1, 7)),
        "lesson_only": set(),
        "other": {130},
    },
    "science": {
        "contents_preface": set(range(1, 7)),
        "lesson_only": set(),
        "other": {151, 152, 153, 154},
    },
}

new_second_pass_pages = {
    "bangla": {20, 21, 22, 24, 25, 26, 27},
    "bgs": {15, 16},
    "science": {16},
}

rows = []
for subject_dir in sorted(path for path in ASSETS.iterdir() if path.is_dir()):
    subject = subject_dir.name
    pages = sorted(subject_dir.glob("page_*.webp"))
    for path in pages:
        page = int(path.stem.split("_")[-1])
        if subject not in reviewed:
            category = "pending_visual_review"
            status = "pending"
            note = "Full contact sheet exists, but this page has not received the second human visual pass."
        else:
            status = "visual_contact_sheet_reviewed"
            groups = reviewed[subject]
            if page in groups["contents_preface"]:
                category = "contents_preface"
            elif page in groups["lesson_only"]:
                category = "lesson_only"
            elif page in groups["other"]:
                category = "other"
            else:
                category = "exercise_or_open_activity"
            note = "Classified from the full-book contact-sheet sweep. Exercise text still needs page-level transcription unless already present in a second-pass batch."
        rows.append({
            "grade": 3,
            "subject": subject,
            "sourcePage": page,
            "asset": f"books/class3/{subject}/{path.name}",
            "category": category,
            "reviewStatus": status,
            "note": note,
            "newSecondPassAnswerBatch": page in new_second_pass_pages.get(subject, set()),
        })

payload = {
    "grade": 3,
    "scope": "All bundled Class 3 page assets",
    "classificationRule": "A page is exercise_or_open_activity when it contains at least one learner prompt, blank, table, matching task, question, drawing task, speaking task, or personal-response activity.",
    "warning": "Contact-sheet classification is not answer verification. Only source-page transcription with a second pass can produce production-ready answers.",
    "summary": {
        "totalPages": len(rows),
        "visuallyClassified": sum(row["reviewStatus"] != "pending" for row in rows),
        "pending": sum(row["reviewStatus"] == "pending" for row in rows),
    },
    "pages": rows,
}
OUT.parent.mkdir(parents=True, exist_ok=True)
OUT.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
print(json.dumps(payload["summary"], ensure_ascii=False))
