"""Repair only explicitly agent-generated low-rated sprites; author assets are hash-protected.
The baseline snapshots and per-file manifest are also the source of presentation before/after images.
"""
from pathlib import Path
from zipfile import ZipFile
from io import BytesIO
import csv,json,hashlib,subprocess,colorsys,shutil,time
from PIL import Image,ImageDraw
ROOT=Path(__file__).resolve().parents[1];TEX=ROOT/'src/main/resources/assets/smog/textures';REVIEW=ROOT/'build/review/2026-11-04/texture-polish'
BASE=REVIEW/'before';BASE.mkdir(parents=True,exist_ok=True)
CAT=json.loads((ROOT/'src/main/content/smog.content.json').read_text(encoding='utf-8'))
RATINGS=list(csv.DictReader((ROOT/'docs/TEXTURE_RATINGS_004_INVENTORY.csv').open(encoding='utf-8-sig')))
GIT=['git','-c','safe.directory='+str(ROOT).replace('\\','/')]
USER_COMMITS=['7b13363','c90903f','19f1a52','88bedcd','88c004b','d296dd2','3b81125']
protected=set()
for commit in USER_COMMITS:
 for name in subprocess.check_output(GIT+['show','--format=','--name-only',commit],cwd=ROOT).decode('utf-8').splitlines():
  if '/textures/' in name and name.endswith(('.png','.pdn')):protected.add(name.split('/textures/',1)[1].replace('.pdn','.png'))
protected.update(['block/handcart.png','block/kaolin.png','item/configurator.png'])
# Explicit user authorization, 2026-11-04: redraw cart texture only, preserve model/UV.
protected.discard('block/handcart.png')
protected.update(p.relative_to(TEX).as_posix() for p in TEX.rglob('*_ore.png'))
sha=lambda p:hashlib.sha256(p.read_bytes()).hexdigest()
old_plan=REVIEW/'baseline.json'
if old_plan.exists():baseline=json.loads(old_plan.read_text(encoding='utf-8'))
else:
 head=subprocess.check_output(GIT+['rev-parse','HEAD'],cwd=ROOT).decode().strip()
 if not head.startswith('a7e2e09'):raise RuntimeError('Missing original baseline. Restore the 2026-11-04 baseline snapshot before regenerating comparisons.')
 baseline=[]
 for row in RATINGS:
  name=row['texture'];p=TEX/name
  if not p.exists():p=ROOT/'build/resources/main/assets/smog/textures'/name
  out=BASE/name;out.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(p,out)
  if p.with_suffix('.png.mcmeta').exists():shutil.copy2(p.with_suffix('.png.mcmeta'),out.with_suffix('.png.mcmeta'))
  baseline.append(dict(row,protected=name in protected,before_sha256=sha(p)))
 old_plan.write_text(json.dumps(baseline,ensure_ascii=False,indent=2),encoding='utf-8')
protected_hashes={name:sha(TEX/name) for name in protected if (TEX/name).is_file()}
Z=ZipFile(ROOT/'.gradle-user-home/caches/fabric-loom/26.3/minecraft-client.jar')
def native(name):return Image.open(BytesIO(Z.read('assets/minecraft/textures/'+name+'.png'))).convert('RGBA')
materials={m['name']:tuple(bytes.fromhex(m['color'])) for cat in ['ores','metals','alloys','deposits','fluids'] for m in CAT.get(cat,[]) if 'color' in m}
materials.setdefault('copper',(232,124,83));materials.setdefault('iron',(216,216,216));materials.setdefault('gold',(247,220,87))
def color(name):
 for material in sorted(materials,key=len,reverse=True):
  if name==material or name.startswith(material+'_'):return materials[material]
 return (178,151,99)
def palette_recolor(im,rgb,lo=.20,hi=.92):
 """Separate dark contour, body and illuminated facet without multiplying two dark palettes."""
 vals=[max(p[:3]) for p in im.getdata() if p[3]];mn,mx=min(vals),max(vals);h,s,v=colorsys.rgb_to_hsv(*(c/255 for c in rgb));out=im.copy()
 for y in range(im.height):
  for x in range(im.width):
   r,g,b,a=im.getpixel((x,y))
   if not a:continue
   q=(max(r,g,b)-mn)/max(1,mx-mn);brightness=lo+(hi-lo)*q**.72;saturation=s*(.90-.33*q)
   c=colorsys.hsv_to_rgb(h,saturation,brightness);out.putpixel((x,y),tuple(round(v*255) for v in c)+(a,))
 return out
def ore_fragment(material):
 # Every painted RGB comes from the corresponding raw ore. This is a new fractured lump, not flint.
 raw=TEX/('item/raw_'+material+'.png')
 if not raw.exists():raw=TEX/('block/'+material+'.png')
 if not raw.exists():raise RuntimeError('Missing raw-ore palette: '+material)
 palette=sorted({p[:3] for p in Image.open(raw).convert('RGBA').getdata() if p[3]},key=lambda c:.2126*c[0]+.7152*c[1]+.0722*c[2])
 c=[palette[round(i*(len(palette)-1)/5)] for i in range(6)]
 im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im)
 d.polygon([(2,5),(5,2),(10,1),(14,5),(13,10),(9,14),(4,13),(1,9)],fill=c[0]+(255,))
 d.polygon([(3,5),(6,3),(10,2),(12,5),(8,8),(3,8)],fill=c[4]+(255,))
 d.polygon([(3,9),(8,9),(9,12),(5,12),(2,9)],fill=c[2]+(255,))
 d.polygon([(9,8),(13,6),(12,10),(10,12)],fill=c[3]+(255,))
 d.line([(5,4),(8,3),(10,3)],fill=c[5]+(255,))
 d.line([(4,6),(7,7),(8,10)],fill=c[1]+(255,))
 d.line([(10,5),(11,7),(10,9)],fill=c[5]+(255,))
 d.point((5,10),fill=c[4]+(255,));d.point((8,11),fill=c[3]+(255,))
 return im

def cart_texture():
 model=ROOT/'src/main/resources/assets/smog/models/block/handcart.json';data=json.loads(model.read_text(encoding='utf-8'))
 im=Image.new('RGBA',(64,64));d=ImageDraw.Draw(im);wood=[(73,54,21),(104,78,30),(137,103,39),(162,130,79),(184,148,95),(204,167,112)]
 # Paint each existing UV island directly. No pasted plank bitmap, no geometry edits.
 for i,e in enumerate(data['elements']):
  if e.get('name')=='wheel':continue
  for face,f in e['faces'].items():
   u0,v0,u1,v1=f['uv'];x0,y0,x1,y1=[round(v*4) for v in (u0,v0,u1,v1)];w=x1-x0;h=y1-y0
   base=wood[4 if face in ['up','north','west'] else 3];d.rectangle((x0,y0,x1-1,y1-1),fill=base+(255,))
   if w>=h:
    if h>=2:
     d.line((x0,y1-1,x1-1,y1-1),fill=wood[2]+(255,));d.line((x0,y0,x1-1,y0),fill=wood[4]+(255,))
     for off in range(2,w-1,6):d.line((x0+off,y1-1,min(x1-1,x0+off+2),y1-1),fill=wood[1]+(255,))
    for off in range(2,h-1,3):
     start=x0+(i+off)%max(1,w//3);end=min(x1-1,start+max(1,w//2));d.line((start,y0+off,end,y0+off),fill=wood[2]+(255,))
   else:
    if w>=2:
     d.line((x1-1,y0,x1-1,y1-1),fill=wood[2]+(255,));d.line((x0,y0,x0,y1-1),fill=wood[4]+(255,))
     for off in range(2,h-1,6):d.line((x1-1,y0+off,x1-1,min(y1-1,y0+off+2)),fill=wood[1]+(255,))
    for off in range(2,w-1,3):
     start=y0+(i+off)%max(1,h//3);end=min(y1-1,start+max(1,h//2));d.line((x0+off,start,x0+off,end),fill=wood[2]+(255,))
 for e in data['elements']:
  if e.get('name')!='wheel':continue
  for face,f in e['faces'].items():
   x0,y0,x1,y1=[round(v*4) for v in f['uv']];w=x1-x0;h=y1-y0
   d.rectangle((x0,y0,x1-1,y1-1),fill=(25,27,30,255))
   if w==h and w>=6:
    d.ellipse((x0,y0,x1-1,y1-1),fill=(72,75,77,255));d.ellipse((x0+1,y0+1,x1-2,y1-2),fill=(30,32,35,255))
    cx=(x0+x1-1)//2;cy=(y0+y1-1)//2;d.line((cx,y0+1,cx,y1-2),fill=(78,79,76,255));d.line((x0+1,cy,x1-2,cy),fill=(78,79,76,255));d.rectangle((cx,cy,cx+1,cy+1),fill=(107,95,72,255))
   elif w<=3:d.line((x0,y0,x0,y1-1),fill=(63,65,66,255))
   else:d.line((x0,y0,x1-1,y0),fill=(63,65,66,255))
 return im

changes=[]
def save(name,im,reason):
 if name in protected:raise RuntimeError('Author asset protected: '+name)
 p=TEX/name
 if not p.exists():raise RuntimeError('Only source PNGs here: '+name)
 before=Image.open(BASE/name).convert('RGBA')
 if im.size!=before.size:raise RuntimeError('Frame dimensions changed: '+name)
 if p.read_bytes()==(BASE/name).read_bytes() and list(before.getdata())==list(im.getdata()):return
 temporary=p.with_suffix('.polish.tmp.png')
 for attempt in range(6):
  try:im.save(temporary);temporary.replace(p);break
  except OSError:
   if attempt==5:raise
   time.sleep(.2)
 changes.append({'texture':name,'reason':reason,'before_sha256':sha(BASE/name),'after_sha256':sha(p)})
for row in RATINGS:
 name=row['texture'];stem=Path(name).stem
 if name in protected or not (TEX/name).exists():continue
 if not(float(row['in_game_day_score'])<=4 or float(row['in_game_day_score'])<=6 and float(row['in_game_low_light_score'])<=3):continue
 old=Image.open(BASE/name).convert('RGBA')
 if name.startswith('item/') and '/' not in name[len('item/'):]:
  kind=next((k for k in ['dust','shard','chunk','nugget','plate'] if stem.endswith('_'+k)),None)
  if kind=='shard' and (TEX/('item/raw_'+stem.removesuffix('_shard')+'.png')).exists():
   save(name,ore_fragment(stem.removesuffix('_shard')),'Новый угловатый обломок с разломами и вкраплениями; RGB-палитра точно из соответствующей сырой руды.')
  elif kind=='chunk' and (TEX/('block/'+stem.removesuffix('_chunk')+'.png')).exists():
   save(name,ore_fragment(stem.removesuffix('_chunk')),'Новый угловатый кусок минерала; цвета взяты из соответствующей исходной породы.')
  elif kind:
   source={'dust':'redstone','shard':'flint','chunk':'flint','nugget':'iron_nugget','plate':'iron_ingot'}[kind]
   im=palette_recolor(native('item/'+source),color(stem),lo=.18 if kind=='dust' else .22,hi=.96)
   if kind=='plate':
    part=im.crop(im.getbbox()).resize((14,5),Image.Resampling.NEAREST);im=Image.new('RGBA',(16,16));im.alpha_composite(part,(1,6))
   save(name,im,'Ванильный силуэт сохранён; новая палитра разделяет обводку, материал и светлую грань.')
  elif stem in ['plant_fiber','waxed_cord','freight_rope']:
   save(name,palette_recolor(old,(215,180,106),lo=.28,hi=.98),'Высветлены нити и контрастные стыки; исходный размер и alpha-маска сохранены.')
  elif stem=='lubricating_compound':
   save(name,palette_recolor(native('item/slime_ball'),(131,137,62),lo=.19,hi=.83),'Добавлены читаемые свет и тень маслянистого комка на ванильной основе.')
  elif stem=='ceramic_core':
   save(name,palette_recolor(old,(185,151,109),lo=.24,hi=.94),'Светлая керамическая грань отделена от тёмного торца.')
  elif stem.endswith('_pipe_part'):
   save(name,palette_recolor(old,color(stem),lo=.17,hi=.96),'Осветлена стенка трубной заготовки; сохранены тёмное отверстие и исходный силуэт.')
  elif stem in ['copper_fitting','bronze_fitting']:
   # Two hollow native-sized links, wider than the old hairline but still a small metal fitting.
   im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im);rgb=color(stem);shade=tuple(round(c*.28) for c in rgb);body=tuple(min(255,round(c*1.05+20)) for c in rgb);edge=tuple(round(c*.45+140) for c in rgb)
   for x,y in [(2,6),(7,2)]:
    d.polygon([(x,y+2),(x+3,y),(x+6,y+3),(x+3,y+6),(x,y+4)],fill=shade+(255,));d.line([(x+1,y+2),(x+3,y+1),(x+5,y+3),(x+3,y+5)],fill=body+(255,),width=2);d.point((x+3,y+3),fill=(0,0,0,0));d.line([(x+1,y+2),(x+3,y+1),(x+4,y+2)],fill=edge+(255,))
   save(name,im,'Два различимых полых звена вместо тонкой вертикальной полосы.')
 elif name=='block/graphite.png':
  im=native('block/basalt_side');vals=[sum(p[:3])/3 for p in im.getdata()];mean=sum(vals)/len(vals)
  for y in range(16):
   for x in range(16):
    q=(sum(im.getpixel((x,y))[:3])/3/mean)**1.55;v=max(27,min(130,round(72*q)));im.putpixel((x,y),(round(v*.91),round(v*.96),v,255))
  save(name,im,'Базальтовый рисунок сохранён; графит остаётся тёмным, но грани различимы.')
 elif name=='block/bellows_leather.png':
  save(name,palette_recolor(old,(142,92,57),lo=.18,hi=.77),'Кожаные складки получили более выраженные светлые рёбра и тёмные впадины.')
 elif name=='block/crude_oil_overlay.png':
  # A functional uniform overlay is not a world surface; retain the original artwork.
  shutil.copy2(BASE/name,TEX/name);continue
 elif name.startswith('block/crude_oil_'):
  im=old.copy();values=[max(p[:3]) for p in old.getdata()];mn,mx=min(values),max(values)
  for y in range(im.height):
   for x in range(im.width):
    r,g,b,a=old.getpixel((x,y));q=(max(r,g,b)-mn)/max(1,mx-mn);v=round(10+53*q**1.3);im.putpixel((x,y),(v,round(v*.90),round(v*.83),a))
  save(name,im,'Нефть остаётся почти чёрной; блики и потоки различимее, кадры и alpha сохранены.')
 elif name.startswith('block/molten_') and not stem.endswith('_overlay'):
  # These textures are actually emissive in the game. Improve pattern separation without replacing their metal hue.
  h,s,v=colorsys.rgb_to_hsv(*(c/255 for c in color(stem)));values=[max(p[:3]) for p in old.getdata()];mn,mx=min(values),max(values);im=old.copy()
  for y in range(im.height):
   for x in range(im.width):
    r,g,b,a=old.getpixel((x,y));q=(max(r,g,b)-mn)/max(1,mx-mn);c=colorsys.hsv_to_rgb(h,s*(.8-.2*q),.29+.55*q**.85);im.putpixel((x,y),tuple(round(v*255) for v in c)+(a,))
  save(name,im,'Разделены светлые прожилки и основа расплава; сохранены цвет металла и анимация.')
 # Tiny component masks and fluid overlays require assembly context, not arbitrary enlargement.
save('block/handcart.png',cart_texture(),'Нарисованы деревянные UV-острова в палитре досок и тёмное колесо; модель/UV сохранены, изменение разрешено автором.')
for name,digest in protected_hashes.items():
 if sha(TEX/name)!=digest:raise RuntimeError('Author artwork changed: '+name)
plan={'baseline_commit':'a7e2e09','version':'0.0.4','protected':protected_hashes,'changes':changes,'unchanged_low_context':[r['texture'] for r in RATINGS if (float(r['in_game_day_score'])<=4 or float(r['in_game_low_light_score'])<=3) and r['texture'] not in {c['texture'] for c in changes}]}
(REVIEW/'changes.json').write_text(json.dumps(plan,ensure_ascii=False,indent=2),encoding='utf-8')
print('Changed source PNGs:',len(changes),'Protected author PNG hashes:',len(protected_hashes))
