"""Material-free 16px tool masks. New materials use runtime tinting; no per-material art is generated."""
from pathlib import Path
from zipfile import ZipFile
from io import BytesIO
import json,sys
from PIL import Image,ImageDraw
ROOT=Path(__file__).resolve().parents[1];ASSETS=ROOT/'src/main/resources/assets/smog';DATA=json.loads((ROOT/'src/main/content/tool_templates.json').read_text(encoding='utf-8'))
FORCE='--refresh-tool-templates' in sys.argv
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
 # No corresponding vanilla hammer/chisel exists; these two explicit shape templates stay editable.
 im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im)
 points={'hammer':[(6,1),(9,1),(15,7),(15,10),(12,10),(6,4)],'chisel':[(11,1),(14,1),(14,4),(10,8),(8,7)]}[form['prototype_shape']]
 d.polygon(points,fill=(160,160,160,255));d.line(points+[points[0]],fill=(60,60,60,255));d.line(points[:2],fill=(245,245,245,255));return im

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

def overlay(form):
 im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im);x,y=(4,10) if form['category']=='wrap' else (8,6)
 d.line((x,y,x+2,y+2),fill=(45,45,45,255),width=3);d.line((x,y-1,x+2,y+1),fill=(225,225,225,255),width=2)
 if form['category']=='wrap':d.line((x+1,y-2,x+3,y),fill=(160,160,160,255),width=2)
 if form['id']=='clamp':d.rectangle((7,5,11,8),outline=(245,245,245,255));d.point((8,6),fill=(45,45,45,255))
 return im

def save(path,im):
 path.parent.mkdir(parents=True,exist_ok=True)
 if FORCE or not path.exists():im.save(path)

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
  im=head(form)
  for raw in [False,True]:save(ASSETS/('textures/item/tool_molds/'+('unfired_' if raw else '')+form['id']+'.png'),mold(im,raw))
  cast=im.copy();d=ImageDraw.Draw(cast);d.line((9,7,6,11),fill=(85,85,85,255),width=2);d.rectangle((4,11,7,13),fill=(130,130,130,255));d.point((5,11),fill=(230,230,230,255));save(ASSETS/('textures/item/tool_forms/cast_'+form['id']+'.png'),cast)
 elif category=='handle':im=handle(form)
 else:
  base='minecraft:item/iron_nugget' if form['id']=='clamp' else 'minecraft:item/string';im=gray(source(base));save(ASSETS/('textures/item/tool_overlays/'+form['id']+'.png'),overlay(form))
 save(ASSETS/('textures/item/tool_forms/'+form['id']+'.png'),im)
print('Shared grayscale tool templates:',len(DATA['forms']),'; materials require no additional PNGs.')
