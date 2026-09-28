"""Generate E5 passage vectors in the exact verified-bank source order."""
from __future__ import annotations

import json
import re
from pathlib import Path

import numpy as np
import onnxruntime as ort
from tokenizers import Tokenizer

ROOT = Path(__file__).resolve().parents[1]
MODEL_DIR = ROOT / "tmp/e5-small-int8"
OUTPUT = ROOT / "app/src/main/assets/e5/verified_passages.f32"
BANKS = [
    ROOT / "app/src/main/java/com/example/data/model/VerifiedOfflineQuestionBank.kt",
    ROOT / "app/src/main/java/com/example/data/model/VerifiedAgentQuestionBank.kt",
]
ARG = r'"(?:\\.|[^"\\])*"'
ROW = re.compile(
    rf"OfflineBookQuestion\(\s*(\d+)\s*,\s*({ARG})\s*,\s*(\d+)\s*,\s*"
    rf"({ARG})\s*,\s*({ARG})\s*,\s*({ARG})(?:\s*,|\s*\))"
)


def load_rows() -> list[dict]:
    rows = []
    for bank in BANKS:
        for match in ROW.finditer(bank.read_text(encoding="utf-8")):
            grade, subject, page, source, question, answer = match.groups()
            rows.append(
                {
                    "grade": int(grade),
                    "subject": json.loads(subject),
                    "page": int(page),
                    "source": json.loads(source),
                    "question": json.loads(question),
                    "answer": json.loads(answer),
                }
            )
    return rows


def embed(session, tokenizer, texts: list[str]) -> np.ndarray:
    vectors = []
    for start in range(0, len(texts), 16):
        encoded = tokenizer.encode_batch(["passage: " + value for value in texts[start : start + 16]])
        width = min(192, max(len(item.ids) for item in encoded))
        ids, masks = [], []
        for item in encoded:
            token_ids = item.ids[:width]
            mask = [1] * len(token_ids)
            ids.append(token_ids + [tokenizer.token_to_id("<pad>") or 1] * (width - len(token_ids)))
            masks.append(mask + [0] * (width - len(mask)))
        input_ids = np.asarray(ids, dtype=np.int64)
        attention = np.asarray(masks, dtype=np.int64)
        hidden = session.run(
            None,
            {
                "input_ids": input_ids,
                "attention_mask": attention,
                "token_type_ids": np.zeros_like(input_ids),
            },
        )[0]
        pooled = (hidden * attention[:, :, None]).sum(1) / np.maximum(attention.sum(1, keepdims=True), 1)
        pooled /= np.maximum(np.linalg.norm(pooled, axis=1, keepdims=True), 1e-12)
        vectors.append(pooled.astype("<f4"))
    return np.concatenate(vectors)


def main() -> None:
    rows = load_rows()
    if not rows:
        raise SystemExit("No verified rows found")
    tokenizer = Tokenizer.from_file(str(MODEL_DIR / "tokenizer.json"))
    session = ort.InferenceSession(
        str(MODEL_DIR / "onnx_model_int8.onnx"), providers=["CPUExecutionProvider"]
    )
    passages = [
        f"শ্রেণি {row['grade']}। {row['source']}। প্রশ্ন: {row['question']}। উত্তর: {row['answer']}"
        for row in rows
    ]
    vectors = embed(session, tokenizer, passages)
    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    OUTPUT.write_bytes(vectors.tobytes(order="C"))
    print(json.dumps({"entries": len(rows), "dimensions": vectors.shape[1], "bytes": OUTPUT.stat().st_size}))


if __name__ == "__main__":
    main()
