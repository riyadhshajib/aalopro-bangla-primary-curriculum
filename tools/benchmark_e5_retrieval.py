"""Reproducible multilingual retrieval audit for E5 Small INT8.

All labelled targets come from VerifiedOfflineQuestionBank.kt. Paraphrases only
change wording/language and never introduce new answer content.
"""
from __future__ import annotations
import json
import re
from pathlib import Path
import numpy as np
import onnxruntime as ort
from tokenizers import Tokenizer
from banglish_alias_normalizer import alias_scores, looks_banglish

ROOT = Path(__file__).resolve().parents[1]
MODEL_DIR = ROOT / "tmp/e5-small-int8"
BANK = ROOT / "app/src/main/java/com/example/data/model/VerifiedOfflineQuestionBank.kt"
ARG = r'"(?:\\.|[^"\\])*"'
ROW = re.compile(rf"OfflineBookQuestion\(\s*(\d+)\s*,\s*({ARG})\s*,\s*(\d+)\s*,\s*({ARG})\s*,\s*({ARG})\s*,\s*({ARG})\s*\)")

# The same 12 verified targets are tested in Bengali, Banglish and English.
TARGETS = [
    "আমিন শহরে কী কী দেখে", "গ্রাম ও শহরের মিল-অমিল", "আমিনের ফুপাতো বোন কে",
    "আমরা সকল সৃষ্টিকে ভালোবাসব কেন", "উপাসনার অর্থ কী", "সৌরজগৎ কী কী নিয়ে গঠিত",
    "দিন-রাত কীভাবে হয়", "সিদ্ধার্থ গৌতমের স্ত্রীর নাম", "সিদ্ধার্থের প্রিয় ঘোড়ার নাম কী ছিল?",
    "ত্রিপিটক কাকে বলে", "গৌতম বুদ্ধ কত বছর ধর্ম প্রচার", "সূত্র পিটক পাঠের উপকারিতা",
]
QUERIES = {
    "bn": [
        "শহরে গিয়ে আমিন কোন কোন জিনিস দেখে অবাক হয়েছিল?",
        "শহর ও গ্রামের মধ্যে কী কী তফাৎ আছে?",
        "আমিনের ওই ফুপাতো বোনটির নাম কী?",
        "সব সৃষ্ট জীবকে আমাদের ভালোবাসা উচিত কেন?",
        "উপাসনা বলতে কী বোঝানো হয়?",
        "কোন কোন বস্তু নিয়ে সৌরজগৎ তৈরি?",
        "দিন আর রাত হওয়ার কারণ কী?",
        "সিদ্ধার্থ গৌতমের স্ত্রী কে ছিলেন?",
        "সিদ্ধার্থের সবচেয়ে পছন্দের ঘোড়ার নাম বলো।",
        "ত্রিপিটক বলতে কী বোঝায়?",
        "বুদ্ধ কত বছর ধরে ধর্মের বাণী প্রচার করেছিলেন?",
        "সূত্র পিটক পড়লে কী লাভ হয়?",
    ],
    "banglish": [
        "Shohore giye Amin kon kon jinis dekhe obak hoyechilo?",
        "Shohor ar gramer moddhe ki ki parthokko ache?",
        "Aminer oi fupato boner nam ki?",
        "Shob srishtike amader bhalobasha uchit keno?",
        "Upashona bolte ki bojhay?",
        "Kon kon bostu niye shourojogot toiri?",
        "Din ar raat howar karon ki?",
        "Siddhartha Gautamer stri ke chhilen?",
        "Siddharthar priyo ghorar nam bolo.",
        "Tripitok bolte ki bojhay?",
        "Buddha koto bochor dhormo prochar korechhilen?",
        "Sutro pitok porle ki upokar hoy?",
    ],
    "en": [
        "What things surprised Amin when he visited the city?",
        "How are a village and a city different?",
        "What is the name of Amin's paternal aunt's daughter?",
        "Why should we love every creation?",
        "What does worship mean?",
        "What objects make up the solar system?",
        "What causes day and night?",
        "Who was Siddhartha Gautama's wife?",
        "What was the name of Siddhartha's favourite horse?",
        "What is the Tripitaka?",
        "For how many years did Buddha preach the Dharma?",
        "What are the benefits of studying the Sutta Pitaka?",
    ],
}

def load_rows():
    rows = []
    for m in ROW.finditer(BANK.read_text(encoding="utf-8")):
        grade, subject, page, title, question, answer = m.groups()
        rows.append({"grade": int(grade), "subject": json.loads(subject), "page": int(page),
                     "title": json.loads(title), "question": json.loads(question), "answer": json.loads(answer)})
    return rows

def embed(session, tokenizer, texts, prefix):
    vectors = []
    for start in range(0, len(texts), 16):
        encoded = tokenizer.encode_batch([prefix + x for x in texts[start:start + 16]])
        width = min(192, max(len(x.ids) for x in encoded)); ids = []; masks = []
        for item in encoded:
            token_ids = item.ids[:width]; mask = [1] * len(token_ids)
            ids.append(token_ids + [tokenizer.token_to_id("<pad>") or 1] * (width - len(token_ids)))
            masks.append(mask + [0] * (width - len(mask)))
        input_ids = np.asarray(ids, dtype=np.int64); attention = np.asarray(masks, dtype=np.int64)
        hidden = session.run(None, {"input_ids": input_ids, "attention_mask": attention,
                                    "token_type_ids": np.zeros_like(input_ids)})[0]
        pooled = (hidden * attention[:, :, None]).sum(1) / np.maximum(attention.sum(1, keepdims=True), 1)
        pooled /= np.maximum(np.linalg.norm(pooled, axis=1, keepdims=True), 1e-12); vectors.append(pooled)
    return np.concatenate(vectors)

def rates(order, targets, indices):
    return {"cases": len(indices),
            "top1": round(float(np.mean([targets[i] == order[i, 0] for i in indices])), 4),
            "top3": round(float(np.mean([targets[i] in order[i, :3] for i in indices])), 4)}

def result_rows(cases, rows, targets, scores, order):
    output=[]
    for i, (language, query, _) in enumerate(cases):
        target, ranked=targets[i], order[i]
        output.append({"language": language, "query": query, "expected_question": rows[target]["question"],
            "top1_question": rows[ranked[0]]["question"], "top1_score": round(float(scores[i,ranked[0]]),4),
            "top1_margin": round(float(scores[i,ranked[0]]-scores[i,ranked[1]]),4),
            "expected_rank": int(np.where(ranked==target)[0][0])+1})
    return output

def main():
    rows = load_rows()
    if len(rows) < 20: raise SystemExit(f"Only {len(rows)} verified rows parsed")
    tokenizer = Tokenizer.from_file(str(MODEL_DIR / "tokenizer.json"))
    session = ort.InferenceSession(str(MODEL_DIR / "onnx_model_int8.onnx"), providers=["CPUExecutionProvider"])
    passages = [f"শ্রেণি {r['grade']}। {r['title']}। প্রশ্ন: {r['question']}। উত্তর: {r['answer']}" for r in rows]
    passage_vectors = embed(session, tokenizer, passages, "passage: ")
    exact_order = np.argsort(-(embed(session, tokenizer, [r["question"] for r in rows], "query: ") @ passage_vectors.T), axis=1)
    cases = [(lang, q, TARGETS[i]) for lang, queries in QUERIES.items() for i, q in enumerate(queries)]
    targets = []
    for _, _, fragment in cases:
        matches = [i for i, row in enumerate(rows) if fragment in row["question"]]
        if len(matches) != 1: raise SystemExit(f"Target fragment {fragment!r} matched {matches}")
        targets.append(matches[0])
    scores = embed(session, tokenizer, [x[1] for x in cases], "query: ") @ passage_vectors.T
    baseline_order=np.argsort(-scores,axis=1)
    # Reciprocal-rank fusion is scale independent. The alias rank is generated
    # against all bank questions without access to benchmark target labels.
    fused_scores=scores.copy(); alias_details=[]
    questions=[r["question"] for r in rows]
    for i, (_,query,_) in enumerate(cases):
        lexical=np.asarray(alias_scores(query,questions))
        if not looks_banglish(query):
            alias_details.append(None); continue
        e5_rank=np.empty(len(rows),dtype=int); e5_rank[baseline_order[i]]=np.arange(len(rows))
        alias_order=np.argsort(-lexical); alias_rank=np.empty(len(rows),dtype=int); alias_rank[alias_order]=np.arange(len(rows))
        # Romanized-bank evidence gets twice the weight because raw E5 has no
        # Bengali-script overlap for these inputs; semantic E5 rank remains a
        # backstop instead of being discarded.
        fused_scores[i]=1/(60+e5_rank)+2/(60+alias_rank)
        alias_details.append({"matched_question": questions[alias_order[0]], "similarity": round(float(lexical[alias_order[0]]),4)})
    order=np.argsort(-fused_scores,axis=1)
    baseline_results=result_rows(cases,rows,targets,scores,baseline_order)
    results=result_rows(cases,rows,targets,fused_scores,order)
    for i,detail in enumerate(alias_details): results[i]["alias_match"]=detail
    false_positives = [x for x in results if x["expected_rank"] != 1]
    top1_gates = []; top3_gates = []
    for threshold in np.arange(0.60, 0.901, 0.01):
        for margin in np.arange(0, 0.101, 0.01):
            accepted = [i for i, x in enumerate(baseline_results) if x["top1_score"] >= threshold and x["top1_margin"] >= margin]
            correct = sum(baseline_results[i]["expected_rank"] == 1 for i in accepted)
            if accepted and correct == len(accepted): top1_gates.append((len(accepted), threshold, margin))
            top3_correct = sum(targets[i] in baseline_order[i, :3] for i in accepted)
            if accepted and top3_correct == len(accepted): top3_gates.append((len(accepted), threshold, margin))
    accepted, threshold, margin = max(top1_gates, key=lambda x: (x[0], -x[1], -x[2]))
    top3_accepted, top3_threshold, top3_margin = max(top3_gates, key=lambda x: (x[0], -x[1], -x[2]))
    by_language = {lang: rates(order, targets, [i for i, x in enumerate(cases) if x[0] == lang]) for lang in QUERIES}
    baseline_by_language={lang: rates(baseline_order,targets,[i for i,x in enumerate(cases) if x[0]==lang]) for lang in QUERIES}
    report = {
        "model": "Xenova/multilingual-e5-small model_int8.onnx",
        "bank": str(BANK.relative_to(ROOT)).replace("\\", "/"), "verified_entries": len(rows),
        "exact_question": rates(exact_order, list(range(len(rows))), list(range(len(rows)))),
        "baseline_paraphrase": {"cases": len(cases), "languages": baseline_by_language,
                       "overall": rates(baseline_order, targets, list(range(len(cases)))),
                       "false_positive_count": sum(x["expected_rank"]!=1 for x in baseline_results)},
        "paraphrase": {"preprocessing": "Banglish-only deterministic romanized verified-bank alias plus 2:1 alias/E5 RRF",
                       "cases": len(cases), "languages": by_language,
                       "overall": rates(order, targets, list(range(len(cases))))},
        "false_positive_count": len(false_positives), "false_positives": false_positives,
        "empirical_top1_gate": {"minimum_similarity": round(float(threshold), 2),
            "minimum_top1_margin": round(float(margin), 2), "accepted": accepted,
            "accepted_correct": accepted, "coverage": round(accepted / len(cases), 4), "observed_precision": 1.0,
            "warning": "Small labelled set; use as a conservative starting gate, not a calibrated probability."},
        "empirical_top3_gate": {"minimum_top1_similarity": round(float(top3_threshold), 2),
            "minimum_top1_margin": round(float(top3_margin), 2), "accepted": top3_accepted,
            "accepted_with_expected_in_top3": top3_accepted, "coverage": round(top3_accepted / len(cases), 4),
            "observed_recall": 1.0,
            "recommendation": "Return top 3 only for deterministic disambiguation; never auto-answer from ranks 2-3."},
        "cases": results, "model_bytes": (MODEL_DIR / "onnx_model_int8.onnx").stat().st_size,
        "tokenizer_bytes": (MODEL_DIR / "tokenizer.json").stat().st_size}
    (ROOT / "tools/e5-retrieval-benchmark.json").write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print(json.dumps(report, ensure_ascii=False, indent=2))

if __name__ == "__main__": main()
