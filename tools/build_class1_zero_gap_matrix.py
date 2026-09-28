import json
from pathlib import Path

ROOT = Path('build/reports/curriculum/agent-class1')
TOTALS = {'bangla':90, 'english':98, 'math':122}
VISUAL_NON_EXERCISE = {
    'bangla': {1:'front cover',2:'national flag reference',3:'national anthem/reference',4:'title page',5:'illustration/context',6:'half-title',7:'publication credits',8:'preface',9:'contents',22:'story illustration/context',23:'story illustration/context',72:'awareness/context page',73:'awareness/context page',86:'reading/context page; no printed prompt',90:'back cover'},
    'english': {1:'front cover',2:'title page',3:'publication credits',4:'preface',5:'contents',6:'unit divider',16:'unit divider',69:'unit divider',79:'unit divider',90:'unit divider',98:'back cover'},
    'math': {1:'front cover',2:'title page',3:'publication credits',4:'preface',5:'book-use instructions',6:'contents',7:'illustration/context',122:'back cover'},
}

reviewed = {s:{} for s in TOTALS}
for p in sorted(ROOT.glob('class1-consolidated-batch-*-verified.json')):
    rows=json.loads(p.read_text(encoding='utf-8'))
    for row in rows:
        s=row['subject'].lower()
        for pg in row.get('sourcePages',[row.get('sourcePage')]):
            if pg is not None:
                reviewed[s].setdefault(int(pg),[]).append(p.name)

candidates={s:set() for s in TOTALS}
for line in (ROOT/'class1-editorial-queue.jsonl').read_text(encoding='utf-8').splitlines():
    if line.strip():
        r=json.loads(line); candidates[r['subject'].lower()].add(int(r['source_page']))

matrix=[]
for s,total in TOTALS.items():
    for pg in range(1,total+1):
        image=ROOT/'pages'/s/f'page-{pg:02d}.jpg'
        if not image.exists(): image=ROOT/'pages'/s/f'page-{pg:03d}.jpg'
        if pg in reviewed[s]:
            cls='exercise_verified'; status='complete'; reason='captured in verified batch'
        elif pg in VISUAL_NON_EXERCISE[s]:
            cls='non_exercise'; status='complete'; reason=VISUAL_NON_EXERCISE[s][pg]
        else:
            cls='exercise_or_guided_activity'; status='blocked_editorial_capture'; reason='visually classified from contact sheet; every printed item still requires individual transcription and source-grounded answer verification'
        matrix.append({'grade':1,'subject':s,'sourcePage':pg,'sourceImage':str(image.resolve()),'classification':cls,'status':status,'reason':reason,'verifiedBatches':reviewed[s].get(pg,[])})

(ROOT/'class1-page-by-page-zero-gap-matrix.json').write_text(json.dumps(matrix,ensure_ascii=False,indent=2),encoding='utf-8')
summary={}
for s in TOTALS:
    rows=[r for r in matrix if r['subject']==s]
    summary[s]={
        'totalPages':len(rows),
        'exercisePagesVerified':sum(r['classification']=='exercise_verified' for r in rows),
        'nonExercisePagesVerified':sum(r['classification']=='non_exercise' for r in rows),
        'exercisePagesBlockedCapture':sum(r['status']=='blocked_editorial_capture' for r in rows),
        'pagesBlockedClassification':sum(r['status']=='blocked_visual_classification' for r in rows),
        'blockedCapturePages':[r['sourcePage'] for r in rows if r['status']=='blocked_editorial_capture'],
        'blockedClassificationPages':[r['sourcePage'] for r in rows if r['status']=='blocked_visual_classification'],
    }
summary['totals']={k:sum(summary[s][k] for s in TOTALS) for k in ('totalPages','exercisePagesVerified','nonExercisePagesVerified','exercisePagesBlockedCapture','pagesBlockedClassification')}
(ROOT/'class1-zero-gap-status.json').write_text(json.dumps(summary,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(summary['totals']))
