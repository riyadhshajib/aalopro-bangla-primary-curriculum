import json
from pathlib import Path

root=Path('build/reports/curriculum/agent-class1')
rows=json.loads((root/'class1-page-by-page-zero-gap-matrix.json').read_text(encoding='utf-8'))
expected={'bangla':90,'english':98,'math':122}
keys=[(r['subject'],r['sourcePage']) for r in rows]
result={
 'matrixRows':len(rows),
 'expectedRows':sum(expected.values()),
 'duplicatePageKeys':len(keys)-len(set(keys)),
 'missingPageKeys':{s:sorted(set(range(1,n+1))-{p for ss,p in keys if ss==s}) for s,n in expected.items()},
 'missingSourceImages':[r['sourceImage'] for r in rows if not Path(r['sourceImage']).exists()],
 'unclassifiedPages':[{'subject':r['subject'],'page':r['sourcePage']} for r in rows if r['classification']=='unclassified'],
}
records=[]
for p in sorted(root.glob('class1-consolidated-batch-*-verified.json')):
    records.extend(json.loads(p.read_text(encoding='utf-8')))
entry_errors=[]; qkeys=[]
for record in records:
    for entry in record.get('entries',[]):
        missing=[k for k in ('question','answer','explanation','followUps') if not entry.get(k)]
        follow=entry.get('followUps',{})
        missing_follow=[k for k in ('bn','banglish','en') if not follow.get(k)]
        if missing or missing_follow: entry_errors.append({'id':entry.get('id'),'missing':missing,'missingFollowUps':missing_follow})
        qkeys.append((record['subject'],tuple(record.get('sourcePages',[])),entry.get('question','').strip().casefold()))
result['verifiedRecords']=len(records)
result['verifiedEntries']=sum(len(r.get('entries',[])) for r in records)
result['entryFieldErrors']=entry_errors
result['duplicateVerifiedQuestions']=len(qkeys)-len(set(qkeys))
result['status']='pass' if not any((result['duplicatePageKeys'],sum(map(len,result['missingPageKeys'].values())),len(result['missingSourceImages']),len(result['unclassifiedPages']),len(entry_errors),result['duplicateVerifiedQuestions'])) else 'fail'
(root/'class1-zero-gap-matrix-validation.json').write_text(json.dumps(result,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(result,ensure_ascii=False))
