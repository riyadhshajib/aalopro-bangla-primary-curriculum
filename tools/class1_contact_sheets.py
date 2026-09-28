import json
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path('build/reports/curriculum/agent-class1')
OUT = ROOT / 'contact-sheets'
OUT.mkdir(parents=True, exist_ok=True)

reviewed = {s: set() for s in ('bangla','english','math')}
for p in ROOT.glob('class1-consolidated-batch-*-verified.json'):
    data = json.loads(p.read_text(encoding='utf-8'))
    rows = data if isinstance(data, list) else data.get('records', data.get('items', []))
    for r in rows:
        subject = str(r.get('subject','')).lower()
        pages = r.get('sourcePages') or [r.get('sourcePage', r.get('page'))]
        if subject in reviewed:
            reviewed[subject].update(int(x) for x in pages if x is not None)

queue = {'bangla': set(), 'english': set(), 'math': set()}
qpath = ROOT / 'class1-editorial-queue.jsonl'
for line in qpath.read_text(encoding='utf-8').splitlines():
    if not line.strip(): continue
    r = json.loads(line)
    subject = str(r.get('subject','')).lower()
    page = r.get('source_page', r.get('sourcePage', r.get('page')))
    if subject in queue and page is not None:
        queue[subject].add(int(page))

font = ImageFont.load_default()
for subject in queue:
    pages = sorted(queue[subject] - reviewed[subject])
    for start in range(0, len(pages), 12):
        block = pages[start:start+12]
        sheet = Image.new('RGB', (1200, 1200), 'white')
        draw = ImageDraw.Draw(sheet)
        for i, page in enumerate(block):
            src = ROOT / 'pages' / subject / f'page-{page:02d}.jpg'
            if not src.exists(): src = ROOT / 'pages' / subject / f'page-{page:03d}.jpg'
            im = Image.open(src).convert('RGB')
            im.thumbnail((280, 350))
            x = (i % 4) * 300 + 10
            y = (i // 4) * 400 + 30
            sheet.paste(im, (x, y))
            draw.rectangle((x, y-25, x+110, y-2), fill='black')
            draw.text((x+5, y-23), f'{subject} p{page}', fill='white', font=font)
        out = OUT / f'{subject}-remaining-{start//12+1:02d}.jpg'
        sheet.save(out, quality=90)

(OUT/'index.json').write_text(json.dumps({s: {'reviewed': sorted(reviewed[s]), 'remainingCandidates': sorted(queue[s]-reviewed[s])} for s in queue}, indent=2), encoding='utf-8')

# Pages missed by OCR filtering must also be visually classified; OCR absence is not proof.
totals={'bangla':90,'english':98,'math':122}
for subject,total in totals.items():
    pages=sorted(set(range(1,total+1))-queue[subject]-reviewed[subject])
    for start in range(0,len(pages),12):
        block=pages[start:start+12]
        sheet=Image.new('RGB',(1200,1200),'white'); draw=ImageDraw.Draw(sheet)
        for i,page in enumerate(block):
            src=ROOT/'pages'/subject/f'page-{page:02d}.jpg'
            if not src.exists(): src=ROOT/'pages'/subject/f'page-{page:03d}.jpg'
            im=Image.open(src).convert('RGB'); im.thumbnail((280,350))
            x=(i%4)*300+10; y=(i//4)*400+30; sheet.paste(im,(x,y))
            draw.rectangle((x,y-25,x+110,y-2),fill='black'); draw.text((x+5,y-23),f'{subject} p{page}',fill='white',font=font)
        sheet.save(OUT/f'{subject}-unclassified-{start//12+1:02d}.jpg',quality=90)
print(json.dumps({s: {'reviewed':len(reviewed[s]), 'remaining':len(queue[s]-reviewed[s])} for s in queue}))
