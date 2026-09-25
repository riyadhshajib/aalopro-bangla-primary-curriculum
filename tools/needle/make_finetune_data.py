#!/usr/bin/env python3
"""Build a deterministic AaloPro Needle 3 fine-tuning set from the benchmark corpus."""

from __future__ import annotations

import json
import re
from pathlib import Path

from benchmark import corpus

HERE = Path(__file__).resolve().parent
TOOLS = json.loads((HERE / "aalo-router-v3.json").read_text(encoding="utf-8"))
ACTION = {
    "open_curriculum": "open_curriculum",
    "start_quiz": "start_quiz",
    "explain_again": "explain_again",
    "open_parent_guide": "parent_guide",
    "open_teacher_guide": "teacher_guide",
    "save_note": "save_note",
    "ask_general_online": "general_online",
}

SUBJECTS = {
    "বাংলা": "bangla", "bangla": "bangla",
    "ইংরেজি": "english", "english": "english",
    "গণিত": "math", "অঙ্ক": "math", "math": "math",
    "বিজ্ঞান": "science", "science": "science",
    "বিশ্বপরিচয়": "bgs", "বিশ্বপরিচয়": "bgs", "bgs": "bgs",
    "ইসলাম": "islam", "islam": "islam",
}


def arguments_for(item: dict[str, str]) -> dict[str, object]:
    query = item["input"].lower()
    arguments: dict[str, object] = {"action": ACTION[item["expected"]], "subject": "unspecified"}
    for token, subject in SUBJECTS.items():
        if token in query:
            arguments["subject"] = subject
            break
    if item["expected"] == "open_curriculum":
        grade_patterns = [
            (1, r"(?:প্রথম|১ম|class\s*1|ক্লাস\s*[১1]|1\s*class)"),
            (2, r"(?:দ্বিতীয়|দ্বিতীয়|২য়|class\s*2|ক্লাস\s*[২2]|2\s*class)"),
            (3, r"(?:তৃতীয়|তৃতীয়|৩য়|class\s*3|ক্লাস\s*[৩3]|3\s*class)"),
            (4, r"(?:চতুর্থ|৪র্থ|class\s*4|ক্লাস\s*[৪4]|4\s*class)"),
            (5, r"(?:পঞ্চম|৫ম|class\s*5|ক্লাস\s*[৫5]|5\s*class)"),
        ]
        for grade, pattern in grade_patterns:
            if re.search(pattern, query):
                arguments["grade"] = grade
                break
    return arguments


def main() -> None:
    rows = []
    for item in corpus():
        action = ACTION[item["expected"]]
        arguments = arguments_for(item)
        rows.append({
            "query": item["input"],
            "tools": TOOLS,
            "answers": [{"name": "route_aalopro_action", "arguments": arguments}],
            "reasoning": f"The request expresses {action}; copy only its stated grade and subject.",
            "system": "locale: bn-BD; device: phone; assistant: AaloPro",
        })
    off_topic = [
        "হ্যালো", "ধন্যবাদ", "ঠিক আছে", "বিদায়", "তুমি কে", "কিছু না", "...", "asdf",
        "hello", "thanks", "okay", "goodbye", "who are you", "nothing", "hmm", "test",
        "valo achi", "thik ache", "dhonnobad", "bye", "ki khobor", "ami jani na",
    ]
    # Roughly one off-topic row per eight actionable rows, as recommended upstream.
    for index in range(68):
        rows.append({
            "query": off_topic[index % len(off_topic)] + ("।" if index % 2 else ""),
            "tools": TOOLS,
            "answers": [],
            "reasoning": "No declared AaloPro action is requested.",
            "system": "locale: bn-BD; device: phone; assistant: AaloPro",
        })
    output = HERE / "training" / "aalopro-needle3-608.jsonl"
    output.parent.mkdir(parents=True, exist_ok=True)
    with output.open("w", encoding="utf-8", newline="\n") as handle:
        for row in rows:
            handle.write(json.dumps(row, ensure_ascii=False, separators=(",", ":")) + "\n")
    print(f"wrote {len(rows)} examples to {output}")


if __name__ == "__main__":
    main()
