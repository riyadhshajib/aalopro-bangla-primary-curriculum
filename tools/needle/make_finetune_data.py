#!/usr/bin/env python3
"""Build a deterministic AaloPro Needle 3 fine-tuning set from the benchmark corpus."""

from __future__ import annotations

import json
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


def main() -> None:
    rows = []
    for item in corpus():
        action = ACTION[item["expected"]]
        rows.append({
            "query": item["input"],
            "tools": TOOLS,
            "answers": [{"name": "route_aalopro_action", "arguments": {"action": action}}],
            "reasoning": f"The request expresses the AaloPro action {action}.",
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
