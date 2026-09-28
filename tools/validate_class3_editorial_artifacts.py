#!/usr/bin/env python3
"""Deterministically validate source-reviewed Class 3 editorial artifacts."""

from __future__ import annotations

import json
from collections import Counter
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
REPORTS = ROOT / "build/reports/curriculum/agent-class3"
ASSETS = ROOT / "app/src/main/assets/books/class3"
OUT = REPORTS / "class3-editorial-validation.json"
REQUIRED = ("question", "answer", "explanation")
FOLLOWUPS = ("bn", "banglish", "en")


def main() -> None:
    errors: list[str] = []
    rows: list[dict[str, object]] = []
    scoped_ids: list[str] = []
    for path in sorted(REPORTS.glob("g3-*.json")):
        try:
            payload = json.loads(path.read_text(encoding="utf-8-sig"))
        except json.JSONDecodeError as exc:
            errors.append(f"{path.name}: invalid JSON: {exc}")
            continue
        subject = str(payload.get("subject", "")).strip()
        entries = payload.get("entries", [])
        if not subject or not isinstance(entries, list):
            continue
        default_pages = payload.get("sourcePages") or []
        default_page = default_pages[0] if default_pages else payload.get("sourcePage")
        for index, entry in enumerate(entries):
            label = f"{path.name} entry {index + 1}"
            entry_id = str(entry.get("id", "")).strip()
            if not entry_id:
                errors.append(f"{label}: missing id")
            else:
                scoped_ids.append(f"{path.name}:{entry_id}")
            page = entry.get("sourcePage", default_page)
            if not isinstance(page, int) or page < 1:
                errors.append(f"{label}: invalid sourcePage")
            elif not (ASSETS / subject / f"page_{page:03d}.webp").is_file():
                errors.append(f"{label}: source asset missing for {subject} page {page}")
            for field in REQUIRED:
                if not str(entry.get(field, "")).strip():
                    errors.append(f"{label}: missing {field}")
            followups = entry.get("followUps", {})
            for language in FOLLOWUPS:
                if not str(followups.get(language, "")).strip():
                    errors.append(f"{label}: missing followUps.{language}")
            rows.append({"file": path.name, "id": entry_id, "subject": subject, "sourcePage": page})

    duplicates = sorted(item for item, count in Counter(scoped_ids).items() if count > 1)
    errors.extend(f"duplicate id: {item}" for item in duplicates)
    payload = {
        "status": "pass" if not errors else "fail",
        "artifactFiles": len({row["file"] for row in rows}),
        "entries": len(rows),
        "uniqueIds": len(set(scoped_ids)),
        "errors": errors,
    }
    OUT.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
    print(json.dumps(payload, ensure_ascii=False))
    raise SystemExit(0 if not errors else 1)


if __name__ == "__main__":
    main()
