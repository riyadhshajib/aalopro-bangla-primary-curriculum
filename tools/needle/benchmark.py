#!/usr/bin/env python3
"""Reproducible AaloPro Needle intent benchmark (no user content is collected)."""

from __future__ import annotations

import argparse
import json
import time
import urllib.request
from collections import Counter
from pathlib import Path


GRADES = [("প্রথম", "1"), ("দ্বিতীয়", "2"), ("তৃতীয়", "3"), ("চতুর্থ", "4"), ("পঞ্চম", "5")]
SUBJECTS = [("বাংলা", "Bangla"), ("ইংরেজি", "English"), ("গণিত", "Math"), ("বিজ্ঞান", "Science"), ("বাংলাদেশ ও বিশ্বপরিচয়", "BGS"), ("ইসলাম ও নৈতিক শিক্ষা", "Islam")]


def corpus() -> list[dict[str, str]]:
    rows: list[dict[str, str]] = []
    for grade_bn, grade_no in GRADES:
        for subject_bn, subject_en in SUBJECTS:
            prompts = [
                f"{grade_bn} শ্রেণির {subject_bn} খোলো",
                f"ক্লাস {grade_no} এর {subject_bn} দেখাও",
                f"class {grade_no} {subject_en} open koro",
                f"Open class {grade_no} {subject_en}",
                f"{grade_no} class er {subject_bn} porbo",
                f"আমাকে {grade_bn} শ্রেণির {subject_bn} পড়াও",
            ]
            rows.extend({"input": p, "expected": "open_curriculum", "language": lang}
                        for p, lang in zip(prompts, ["bn", "bn", "banglish", "en", "banglish", "bn"]))

    explain = {
        "bn": ["বুঝিনি সহজ করে বোঝাও", "আরেকটু বুঝিয়ে দাও", "ধাপে ধাপে দেখাও", "উদাহরণ দিয়ে বোঝাও", "সংক্ষেপে বলো"],
        "banglish": ["bujhini easy kore bujhao", "arekbar explain koro", "step by step dekhao", "example diye bujhao", "short kore bolo"],
        "en": ["Explain it simply", "Explain that again", "Show me step by step", "Give me an example", "Make it shorter"],
    }
    quiz = {
        "bn": ["এই পাঠ থেকে কুইজ দাও", "গণিতের কুইজ শুরু করো", "আমাকে পাঁচটি প্রশ্ন করো", "অনুশীলনী শুরু করি", "এই টপিক পরীক্ষা করো"],
        "banglish": ["ei lesson theke quiz dao", "math quiz start koro", "amake five ta question koro", "practice start kori", "ei topic test koro"],
        "en": ["Start a quiz from this lesson", "Start the math quiz", "Ask me five questions", "Start practice", "Test this topic"],
    }
    parent = {
        "bn": ["বাচ্চাকে বাড়িতে কীভাবে পড়াব", "পড়ার রুটিন কীভাবে করব", "আমার সন্তান মনোযোগ দেয় না", "বাড়ির কাজ করাতে সাহায্য চাই", "অভিভাবকের পরামর্শ দেখাও"],
        "banglish": ["bacchake basay kivabe porabo", "porar routine kivabe korbo", "amar child focus kore na", "homework korate help chai", "parent guide dekhao"],
        "en": ["How can I teach my child at home", "Make a home study routine", "My child cannot focus", "Help with homework", "Show the parent guide"],
    }
    teacher = {
        "bn": ["এই পাঠ কীভাবে পড়াব", "শ্রেণিকক্ষের কাজ দাও", "শিক্ষকের পরামর্শ দেখাও", "দুর্বল শিক্ষার্থীকে কীভাবে বোঝাব", "পাঠ পরিকল্পনা দাও"],
        "banglish": ["ei lesson kivabe porabo", "classroom activity dao", "teacher guide dekhao", "weak student ke kivabe bujhabo", "lesson plan dao"],
        "en": ["How should I teach this lesson", "Give a classroom activity", "Show the teacher guide", "Help a struggling learner", "Give me a lesson plan"],
    }
    notes = {
        "bn": ["এই উত্তরটা নোটে রাখো", "নোটে লিখে রাখ", "এটা সেভ করো", "মূল কথাটা নোট করো", "আমার নোটে যোগ করো"],
        "banglish": ["ei answer ta note e rakho", "note e likhe rakho", "eta save koro", "main point note koro", "amar note e add koro"],
        "en": ["Save this answer as a note", "Write it in my notes", "Save this", "Note the key point", "Add it to my notes"],
    }
    general = {
        "bn": ["বাংলাদেশের জাতীয় কবি কে", "আজকের আবহাওয়া কেমন", "চাঁদ কেন দেখা যায়", "কম্পিউটার কী", "একটি গল্প বলো"],
        "banglish": ["Bangladesher national poet ke", "ajker weather kemon", "chand keno dekha jay", "computer ki", "ekta story bolo"],
        "en": ["Who is the national poet of Bangladesh", "What is today's weather", "Why can we see the moon", "What is a computer", "Tell me a story"],
    }
    groups = [
        (explain, "explain_again"), (quiz, "start_quiz"),
        (parent, "open_parent_guide"), (teacher, "open_teacher_guide"),
        (notes, "save_note"), (general, "ask_general_online"),
    ]
    # Repeat with harmless punctuation/politeness variants so the fixed corpus has 540 cases.
    for group, expected in groups:
        for language, prompts in group.items():
            for prompt in prompts:
                for suffix in ("", "।", " please", "?"):
                    rows.append({"input": prompt + suffix, "expected": expected, "language": language})
    return rows


def call(port: int, text: str, timeout: int) -> dict:
    # Every case is independent. Without reset the server appends prior commands
    # to the context, making later cases slower and invalidating the comparison.
    reset = urllib.request.Request(
        f"http://127.0.0.1:{port}/reset", data=b"{}",
        headers={"Content-Type": "application/json"}, method="POST")
    with urllib.request.urlopen(reset, timeout=timeout):
        pass
    data = json.dumps({"input": text}, ensure_ascii=False).encode("utf-8")
    request = urllib.request.Request(
        f"http://127.0.0.1:{port}/complete", data=data,
        headers={"Content-Type": "application/json; charset=utf-8"}, method="POST")
    with urllib.request.urlopen(request, timeout=timeout) as response:
        return json.loads(response.read().decode("utf-8"))


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--port", type=int, required=True)
    parser.add_argument("--model", required=True)
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("--limit", type=int, default=0)
    parser.add_argument("--timeout", type=int, default=30)
    args = parser.parse_args()
    cases = corpus()[: args.limit or None]
    results = []
    started = time.time()
    for index, case in enumerate(cases, 1):
        try:
            raw = call(args.port, case["input"], args.timeout)
            calls = raw.get("function_calls") or []
            actual = calls[0].get("name") if calls else None
            if actual == "route_aalopro_action":
                actual = (calls[0].get("arguments") or {}).get("action")
                actual = {"parent_guide": "open_parent_guide", "teacher_guide": "open_teacher_guide",
                          "general_online": "ask_general_online"}.get(actual, actual)
            result = {**case, "actual": actual, "correct": actual == case["expected"],
                      "confidence": raw.get("confidence"), "error": raw.get("error")}
        except Exception as exc:
            result = {**case, "actual": None, "correct": False, "confidence": None,
                      "error": f"{type(exc).__name__}: {exc}"}
        results.append(result)
        if index % 25 == 0:
            print(f"{index}/{len(cases)}", flush=True)
    by_language = {}
    for language in ("bn", "banglish", "en"):
        subset = [row for row in results if row["language"] == language]
        by_language[language] = {
            "cases": len(subset),
            "correct": sum(bool(row["correct"]) for row in subset),
            "accuracy": round(sum(bool(row["correct"]) for row in subset) / max(1, len(subset)), 4),
        }
    report = {
        "model": args.model, "cases": len(results),
        "correct": sum(bool(row["correct"]) for row in results),
        "accuracy": round(sum(bool(row["correct"]) for row in results) / max(1, len(results)), 4),
        "elapsed_seconds": round(time.time() - started, 2),
        "by_language": by_language,
        "actual_counts": Counter(str(row["actual"]) for row in results),
        "results": results,
    }
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print(json.dumps({k: v for k, v in report.items() if k != "results"}, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
