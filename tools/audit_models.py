"""Static audit of copper-model UV density and coincident exterior faces."""
from pathlib import Path
from itertools import combinations
import json
from PIL import Image
ROOT=Path(__file__).resolve().parents[1]/'src/main/resources/assets/smog/models/block'
errors=[];faces=0
for p in sorted(ROOT.glob('*.json')):
 data=json.loads(p.read_text(encoding='utf-8'));planes={}
 for e in data.get('elements',[]):
  a=e['from'];b=e['to']
  for side,f in e.get('faces',{}).items():
   faces+=1
   axis,index,dimensions={'west':(0,0,(2,1)),'east':(0,1,(2,1)),'north':(2,0,(0,1)),'south':(2,1,(0,1)),'down':(1,0,(0,2)),'up':(1,1,(0,2))}[side]
   coord=(a if index==0 else b)[axis];u,v=dimensions;uv=f['uv']
   if abs(abs(uv[2]-uv[0])-(b[u]-a[u]))>1e-6 or abs(abs(uv[3]-uv[1])-(b[v]-a[v]))>1e-6:errors.append(f'{p.name}: UV density {side}')
   planes.setdefault((axis,coord),[]).append((a[u],b[u],a[v],b[v]))
 for plane,rectangles in planes.items():
  for r,s in combinations(rectangles,2):
   if min(r[1],s[1])-max(r[0],s[0])>1e-6 and min(r[3],s[3])-max(r[2],s[2])>1e-6:errors.append(f'{p.name}: coincident faces {plane}')
textures=ROOT.parents[1]/'textures'
for p in textures.rglob('*.png'):
 if p.relative_to(textures).parts[0] not in ['block','item']:continue
 im=Image.open(p)
 animated=p.with_suffix('.png.mcmeta').is_file()
 if not animated and im.size!=(16,16):errors.append(f'{p.name}: static art must be 16x16, got {im.size}')
 if animated and (im.width not in [16,32] or im.height%im.width):errors.append(f'{p.name}: invalid native animation strip {im.size}')
 if p.parent.name=='item' and any(a not in [0,255] for a in im.convert('RGBA').getchannel('A').getdata()):errors.append(f'{p.name}: soft item alpha breaks pixel edges')
for error in errors:print(error)
print('Models:',len(list(ROOT.glob('*.json'))),'faces:',faces,'errors:',len(errors))
raise SystemExit(bool(errors))
