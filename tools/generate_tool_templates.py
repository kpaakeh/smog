"""Material-free 16px tool masks. New materials use runtime tinting; no per-material art is generated."""
from pathlib import Path
from zipfile import ZipFile
from io import BytesIO
import json,sys
from PIL import Image,ImageDraw
ROOT=Path(__file__).resolve().parents[1];ASSETS=ROOT/'src/main/resources/assets/smog';DATA=json.loads((ROOT/'src/main/content/tool_templates.json').read_text(encoding='utf-8'))
FORCE='--refresh-tool-templates' in sys.argv
PARTS_ONLY='--refresh-parts' in sys.argv
Z=ZipFile(ROOT/'.gradle-user-home/caches/fabric-loom/26.3/minecraft-client.jar')
def source(name):return Image.open(BytesIO(Z.read('assets/'+name.split(':')[0]+'/textures/'+name.split(':')[1]+'.png'))).convert('RGBA')
def gray(im):
 result=im.copy();maximum=max((max(p[:3]) for p in im.getdata() if p[3]),default=255)
 for y in range(im.height):
  for x in range(im.width):
   r,g,b,a=im.getpixel((x,y));v=round(max(r,g,b)*255/max(1,maximum));result.putpixel((x,y),(v,v,v,a))
 return result

def head(form):
 if form.get('source_texture'):
  im=source(form['source_texture']);x0,y0,x1,y1=form['head_region']
  for y in range(16):
   for x in range(16):
    r,g,b,a=im.getpixel((x,y));wood=r>g*1.12 and g>b*1.12
    if not(x0<=x<x1 and y0<=y<y1) or wood:im.putpixel((x,y),(0,0,0,0))
  if form['id']=='hoe_head':
   for pixel in [(10,7),(9,8)]:im.putpixel(pixel,(0,0,0,0))
  return gray(im)
 # Shared hand-edited masks for forms without a vanilla equivalent.
 im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im)
 points={'hammer':[(6,1),(9,1),(15,7),(15,10),(12,10),(6,4)],'saw':[(5,7),(10,2),(14,1),(15,2),(15,5),(13,7),(12,6),(11,9),(10,8),(9,11),(8,10),(7,12),(5,10)],'katana':[(5,10),(8,6),(11,2),(14,0),(14,3),(11,7),(8,10),(6,12)],'machete':[(6,9),(10,3),(15,1),(15,5),(10,10),(7,12)],'sickle':[(4,2),(7,0),(11,0),(14,2),(15,5),(14,8),(10,10),(8,9),(9,7),(12,6),(12,3),(10,2),(7,2),(5,3)]}[form['prototype_shape']]
 d.polygon(points,fill=(196,196,196,255));d.line(points+[points[0]],fill=(48,48,48,255))
 # Broad coherent bevels, never random noise: a dark back and bright cutting edge.
 accents={'hammer':[(7,2),(9,2),(14,7)],'saw':[(6,7),(11,2),(14,2)],'katana':[(6,10),(9,6),(13,1)],'machete':[(7,9),(11,4),(14,2)],'sickle':[(5,2),(7,1),(11,1),(13,2),(14,5),(13,7),(10,9)]}
 d.line(accents[form['prototype_shape']],fill=(255,255,255,255),width=1)
 if form['prototype_shape']=='sickle':d.line([(10,7),(12,6),(12,3),(10,2)],fill=(112,112,112,255))
 return im

def handle(form):
 im=source('minecraft:item/iron_pickaxe')
 for y in range(16):
  for x in range(16):
   r,g,b,a=im.getpixel((x,y));wood=r>g*1.12 and g>b*1.12
   if not wood and not(x<8 and y>7):im.putpixel((x,y),(0,0,0,0))
 if form['id']=='short_handle':
  for y in range(12,16):
   for x in range(16):im.putpixel((x,y),(0,0,0,0))
 im=gray(im);d=ImageDraw.Draw(im)
 if form['id']=='reinforced_handle':
  for x,y in [(4,12),(7,9)]:d.line((x-1,y-1,x+1,y+1),fill=(65,65,65,255),width=2);d.point((x,y-1),fill=(235,235,235,255))
 elif form['id']=='light_handle':
  for x,y in [(4,12),(6,10)]:d.point((x,y),fill=(65,65,65,255))
 elif form['id']=='hollow_handle':d.rectangle((1,13,3,15),fill=(205,205,205,255));d.point((2,14),fill=(30,30,30,255))
 return im

def clean(im):
 result=im.copy();tones=[52,120,220,255]
 for y in range(16):
  for x in range(16):
   r,g,b,a=im.getpixel((x,y))
   if a:
    v=min(tones,key=lambda tone:abs(tone-max(r,g,b)));result.putpixel((x,y),(v,v,v,255))
 return result

def overlay(form):
 im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im)
 if form['category']=='wrap':
  # One continuous grip band; no bright checkerboard islands across the handle.
  for x in range(3,7):
   y=16-x;d.point((x,y),fill=(156,156,156,255));d.point((x,y-1),fill=(212,212,212,255))
  d.point((3,12),fill=(248,248,248,255))
 else:
  # A small diagonal joint, not a white box painted across the whole head.
  for x,y,v in [(8,6,156),(9,7,212),(8,7,212),(7,7,156),(8,8,156)]:d.point((x,y),fill=(v,v,v,255))
  if form['id']=='clamp':d.point((9,6),fill=(248,248,248,255));d.point((7,8),fill=(92,92,92,255))
 return im


def save(path,im):
 path.parent.mkdir(parents=True,exist_ok=True)
 if FORCE or PARTS_ONLY and (path.parent.name in ['tool_forms','tool_overlays'] or 'sickle' in path.stem) or not path.exists():im.save(path)

def mold(mask,raw):
 colors=[(45,52,62),(67,76,88),(100,111,123),(129,142,154),(159,174,185),(188,203,213),(216,228,235)] if raw else [(46,38,31),(70,57,45),(99,80,61),(128,105,80),(155,132,103),(184,160,126),(209,186,151)]
 im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im);d.polygon([(1,6),(10,2),(14,6),(14,10),(5,14),(1,10)],fill=colors[1]+(255,));d.polygon([(1,6),(10,2),(14,6),(5,10)],fill=colors[4]+(255,));d.polygon([(1,7),(5,11),(14,7),(14,10),(5,14),(1,10)],fill=colors[2]+(255,));d.line([(1,6),(10,2),(14,6)],fill=colors[-1]+(255,));d.line([(1,7),(5,11),(14,7)],fill=colors[3]+(255,))
 for y in range(16):
  for x in range(16):
   if mask.getpixel((x,y))[3]:d.point((round(2+(x+y)*.34),round(6+(y-x)*.18)),fill=colors[0]+(255,))
 return im

for form in DATA['forms']:
 category=form['category']
 if category=='head':
  im=clean(head(form))
  for raw in [False,True]:save(ASSETS/('textures/item/tool_molds/'+('unfired_' if raw else '')+form['id']+'.png'),mold(im,raw))
  save(ASSETS/('textures/item/casting_molds/'+form['id'].replace('_head','_mold')+'.png'),gray(mold(im,False)))
  cast=im.copy();d=ImageDraw.Draw(cast);d.line((8,7,7,9),fill=(156,156,156,255),width=1);d.point((7,10),fill=(212,212,212,255));save(ASSETS/('textures/item/tool_forms/cast_'+form['id']+'.png'),cast)
 elif category=='handle':im=clean(handle(form))
 else:
  base='minecraft:item/iron_nugget' if form['id']=='clamp' else 'minecraft:item/string';im=clean(gray(source(base)));save(ASSETS/('textures/item/tool_overlays/'+form['id']+'.png'),overlay(form))
 save(ASSETS/('textures/item/tool_forms/'+form['id']+'.png'),im)
print('Shared grayscale tool templates:',len(DATA['forms']),'; materials require no additional PNGs.')
