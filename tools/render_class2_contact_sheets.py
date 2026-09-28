from pathlib import Path

import fitz
from PIL import Image, ImageDraw


BOOKS = {
    "bangla": Path(r"C:\Users\riyad\Downloads\Class-2, Bangla_compressed.pdf"),
    "english": Path(r"C:\Users\riyad\Downloads\english_compressed.pdf"),
    "math": Path(r"C:\Users\riyad\Downloads\Class-2, Math_compressed.pdf"),
}
OUT = Path("build/reports/curriculum/agent-class2/contact-sheets")
# Full-size page evidence is kept separately from contact sheets so every
# ledger row can point to a file that actually exists.  These renders are
# review inputs only.  They are never treated as verified text by themselves.
PAGE_OUT = OUT.parent / "pages"
DETAIL_PAGES = {
    "bangla": [37, 38, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 55, 56, 60],
    "english": [26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40],
    "math": [39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 54, 56],
}


def main() -> None:
    OUT.mkdir(parents=True, exist_ok=True)
    for name, path in BOOKS.items():
        doc = fitz.open(path)
        page_dir = PAGE_OUT / name
        page_dir.mkdir(parents=True, exist_ok=True)
        thumbs = []
        for index, page in enumerate(doc):
            evidence_path = page_dir / f"page-{index + 1:03d}.jpg"
            if not evidence_path.exists():
                evidence = page.get_pixmap(matrix=fitz.Matrix(1.5, 1.5), alpha=False)
                evidence.save(evidence_path)
            pix = page.get_pixmap(matrix=fitz.Matrix(0.18, 0.18), alpha=False)
            image = Image.frombytes("RGB", (pix.width, pix.height), pix.samples)
            tile = Image.new("RGB", (220, 310), "white")
            image.thumbnail((200, 270))
            tile.paste(image, ((220 - image.width) // 2, 24))
            ImageDraw.Draw(tile).text((8, 4), f"PDF {index + 1}", fill="black")
            thumbs.append(tile)
        for start in range(0, len(thumbs), 20):
            sheet = Image.new("RGB", (1100, 1240), "#eeeeee")
            for offset, thumb in enumerate(thumbs[start : start + 20]):
                sheet.paste(thumb, ((offset % 5) * 220, (offset // 5) * 310))
            sheet.save(OUT / f"{name}-{start + 1:03d}-{min(start + 20, len(thumbs)):03d}.jpg", quality=86)
        detail_dir = OUT.parent / "detail-pages" / name
        detail_dir.mkdir(parents=True, exist_ok=True)
        for page_number in DETAIL_PAGES[name]:
            pix = doc[page_number - 1].get_pixmap(matrix=fitz.Matrix(1.5, 1.5), alpha=False)
            pix.save(detail_dir / f"pdf-{page_number:03d}.png")


if __name__ == "__main__":
    main()
