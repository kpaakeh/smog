"""Read-only audit of PNGs reachable from active block/item models and fluid rendering."""
from pathlib import Path
import json,sys
ROOT=Path(__file__).resolve().parents[1]
SOURCE=ROOT/'src/main/resources';GENERATED=ROOT/'build/generated/smog-content'
if not GENERATED.exists():raise SystemExit('Run gradlew generateContent first.')
used=set();seen=set();missing=[]
def load(path):
    for folder in [SOURCE,GENERATED]:
        p=folder/path
        if p.is_file():return json.loads(p.read_text(encoding='utf-8-sig'))
    missing.append(path);return {}
def model(name):
    if not name.startswith('smog:') or name in seen:return
    seen.add(name);data=load('assets/smog/models/'+name.split(':',1)[1]+'.json')
    if data.get('parent'):model(data['parent'])
    for texture in data.get('textures',{}).values():
        if texture.startswith('smog:'):used.add(texture.split(':',1)[1]+'.png')
def walk(value):
    if isinstance(value,dict):
        for key,entry in value.items():
            if key=='model' and isinstance(entry,str):model(entry)
            else:walk(entry)
    elif isinstance(value,list):
        for entry in value:walk(entry)
for kind in ['items','blockstates']:
    for p in (GENERATED/'assets/smog'/kind).glob('*.json'):walk(json.loads(p.read_text(encoding='utf-8')))
content=json.loads((ROOT/'src/main/content/smog.content.json').read_text(encoding='utf-8'))
for fluid in content['fluids']:
    for kind in ['still','flow','overlay']:used.add('block/'+fluid['name']+'_'+kind+'.png')
if any(m.get('kind')=='COPPER_WORKSHOP' for m in content.get('machines',[])):
    used.update('gui/'+n+'.png' for n in ['copper_workshop','slot','structure_preview','copper_progress_empty','copper_progress_full','copper_lit_progress'])
for fluid in content['fluids']:
    if fluid['name'].startswith('molten_'):used.add('block/'+fluid['name']+'_surface.png')
textures=SOURCE/'assets/smog/textures';actual={p.relative_to(textures).as_posix() for p in textures.rglob('*.png')}
unused=sorted(actual-used);absent=sorted(used-actual)
print('Active PNGs:',len(actual),'Unused:',len(unused),'Missing:',len(absent))
for item in unused:print('UNUSED',item)
for item in absent:print('MISSING',item)
for item in missing:print('MISSING MODEL',item)
if '--list' in sys.argv:
    for item in sorted(actual&used):print(item)
raise SystemExit(bool(unused or absent or missing))
