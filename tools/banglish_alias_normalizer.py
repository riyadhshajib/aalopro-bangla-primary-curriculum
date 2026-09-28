"""Deterministic Banglish matching against verified Bengali question text.

The index is generated from every bank question with one generic transliterator.
It never reads benchmark labels or expected answers.
"""
from __future__ import annotations
from difflib import SequenceMatcher
import re

VOWELS = {"অ":"o","আ":"a","ই":"i","ঈ":"i","উ":"u","ঊ":"u","ঋ":"ri","এ":"e","ঐ":"oi","ও":"o","ঔ":"ou"}
SIGNS = {"া":"a","ি":"i","ী":"i","ু":"u","ূ":"u","ৃ":"ri","ে":"e","ৈ":"oi","ো":"o","ৌ":"ou"}
CONS = {"ক":"k","খ":"kh","গ":"g","ঘ":"gh","ঙ":"ng","চ":"ch","ছ":"chh","জ":"j","ঝ":"jh","ঞ":"n",
        "ট":"t","ঠ":"th","ড":"d","ঢ":"dh","ণ":"n","ত":"t","থ":"th","দ":"d","ধ":"dh","ন":"n",
        "প":"p","ফ":"ph","ব":"b","ভ":"bh","ম":"m","য":"j","য়":"y","র":"r","ল":"l","শ":"sh",
        "ষ":"sh","স":"s","হ":"h","ড়":"r","ঢ়":"rh","ৎ":"t"}
MARKERS = {"ki","kivabe","kibhabe","keno","ke","kothay","koto","kon","konta","nam","bolo","bolte",
           "bojhay","hoy","hoyeche","chhilo","chhilen","kore","korle","porle","amader","shob","ar"}
STOP = {"ki","ke","ar","er","oi","kore","korle","bolte","bolo","hoy","hoye","chhilo","chhilen","nam"}

def romanize_bengali(text: str) -> str:
    out=[]; i=0
    while i < len(text):
        ch=text[i]
        if ch in CONS:
            out.append(CONS[ch]); nxt=text[i+1] if i+1 < len(text) else ""
            if nxt in SIGNS: out.append(SIGNS[nxt]); i += 1
            elif nxt != "্": out.append("o")
            else: i += 1
        elif ch in VOWELS: out.append(VOWELS[ch])
        elif ch == "ং": out.append("ng")
        elif ch == "ঃ": out.append("h")
        elif ch == "ঁ": out.append("n")
        else: out.append(ch)
        i += 1
    return "".join(out)

def normalize_latin(text: str) -> str:
    value=re.sub(r"[^a-z0-9]+", " ", text.lower())
    for old,new in (("chh","c"),("ch","c"),("sh","s"),("kh","k"),("gh","g"),("jh","j"),
                    ("th","t"),("dh","d"),("ph","f"),("bh","b"),("aa","a"),("ee","i"),
                    ("oo","u"),("ou","u"),("oi","i"),("y","i")):
        value=value.replace(old,new)
    return " ".join(value.split())

def looks_banglish(query: str) -> bool:
    if re.search(r"[\u0980-\u09ff]", query): return False
    tokens=set(normalize_latin(query).split())
    return bool(tokens & MARKERS) and not ({"what","who","why","how","which","where"} & tokens)

def _skeleton(token: str) -> str:
    return re.sub(r"[aeiou]", "", token)

def alias_similarity(query: str, question: str) -> float:
    q=[x for x in normalize_latin(query).split() if x not in STOP and len(x)>1]
    a=[x for x in normalize_latin(romanize_bengali(question)).split() if x not in STOP and len(x)>1]
    if not q or not a: return 0.0
    def token_score(x,y):
        direct=SequenceMatcher(None,x,y).ratio()
        sx,sy=_skeleton(x),_skeleton(y)
        consonants=SequenceMatcher(None,sx,sy).ratio() if sx and sy else 0
        return max(direct, 0.92*consonants)
    forward=sum(max(token_score(x,y) for y in a) for x in q)/len(q)
    backward=sum(max(token_score(y,x) for x in q) for y in a)/len(a)
    return 0.72*forward+0.28*backward

def alias_scores(query: str, questions: list[str]) -> list[float]:
    if not looks_banglish(query): return [0.0]*len(questions)
    return [alias_similarity(query, question) for question in questions]
