"""Editable copper-era prototype block models and vanilla-derived item sprites. No paid assets."""
from pathlib import Path
from zipfile import ZipFile
from io import BytesIO
import json,shutil
from PIL import Image,ImageDraw
ROOT=Path(__file__).resolve().parents[1];A=ROOT/'src/main/resources/assets/smog';C=json.loads((ROOT/'src/main/content/smog.content.json').read_text(encoding='utf-8'))
Z=ZipFile(ROOT/'.gradle-user-home/caches/fabric-loom/26.3/minecraft-client.jar')
def image(name):return Image.open(BytesIO(Z.read('assets/minecraft/textures/'+name+'.png'))).convert('RGBA')
def tint(im,color):
 im=im.copy()
 for y in range(im.height):
  for x in range(im.width):
   r,g,b,a=im.getpixel((x,y));v=max(r,g,b)/255
   im.putpixel((x,y),tuple(round(channel*v) for channel in color)+(a,))
 return im
def sprite(n,im):
 p=A/'textures/item'/(n+'.png');p.parent.mkdir(parents=True,exist_ok=True)
 if not p.exists():im.save(p)
for e in C['items']:
 n=e['name'];base='item/brick' if 'firebrick' in n else 'item/bowl' if 'crucible' in n else 'item/clay_ball' if n=='tempered_clay' else 'item/stick' if n=='configurator' else 'item/iron_ingot' if 'plate'==n.split('_')[-1] else 'item/iron_nugget' if 'fitting'==n.split('_')[-1] else 'item/bamboo' if 'pipe_part' in n else 'item/brick'
 color=(174,109,64) if n.startswith('copper') else (179,134,76) if n.startswith('bronze') else (151,160,179) if n.startswith('unfired') else (220,191,154)
 im=tint(image(base),color)
 if 'mold' in n:
  d=ImageDraw.Draw(im);size={'ingot':(5,7,10,9),'plate':(4,6,11,10),'fitting':(6,7,8,8),'pipe':(3,8,12,8)}[n.removeprefix('unfired_').removesuffix('_mold')];d.rectangle(size,fill=(65,52,43,255))
 sprite(n,im)
def box(a,b,texture):return dict(**{'from':a,'to':b},faces={side:dict(texture='#'+texture,uv=[0,0,16,16]) for side in ['north','south','west','east','up','down']})
def model(n,elements,textures):
 p=A/'models/block'/(n+'.json');p.parent.mkdir(parents=True,exist_ok=True)
 textures=dict(textures);textures['particle']=next(iter(textures.values()))
 p.write_text(json.dumps(dict(parent='minecraft:block/block',textures=textures,elements=elements,display=dict(gui=dict(rotation=[30,225,0],translation=[0,0,0],scale=[.6,.6,.6]),fixed=dict(rotation=[0,180,0],scale=[.5,.5,.5]))),indent=2)+'\n',encoding='utf-8')
brick='minecraft:block/bricks';clay='minecraft:block/terracotta';wood='minecraft:block/oak_planks';copper='minecraft:block/copper_block'
for n,size in [('ceramic_crucible',12),('large_ceramic_crucible',16)]:
 lo=(16-size)/2;hi=16-lo;model(n,[box([lo,0,lo],[hi,3,hi],'clay'),box([lo,3,lo],[lo+2,14,hi],'clay'),box([hi-2,3,lo],[hi,14,hi],'clay'),box([lo+2,3,lo],[hi-2,14,lo+2],'clay'),box([lo+2,3,hi-2],[hi-2,14,hi],'clay')],dict(clay=clay))
model('firebrick_casing',[box([0,0,0],[16,16,16],'brick')],dict(brick='minecraft:block/white_terracotta'))
model('casting_spout',[box([0,0,0],[16,16,12],'brick'),box([3,4,0],[13,7,16],'clay'),box([3,7,0],[5,10,16],'clay'),box([11,7,0],[13,10,16],'clay')],dict(brick=brick,clay=clay))
model('casting_channel',[box([0,0,0],[16,3,16],'clay'),box([0,3,0],[3,8,16],'clay'),box([13,3,0],[16,8,16],'clay')],dict(clay=clay))
model('air_intake',[box([0,0,4],[16,16,16],'brick'),box([4,4,0],[12,12,6],'dark')],dict(brick=brick,dark='minecraft:block/coal_block'))
model('kiln_vent',[box([0,0,0],[16,5,16],'brick'),box([3,5,3],[6,16,13],'brick'),box([10,5,3],[13,16,13],'brick'),box([6,5,3],[10,16,6],'brick'),box([6,5,10],[10,16,13],'brick')],dict(brick=brick))
legs=[box([x,0,z],[x+3,12,z+3],'wood') for x,z in [(1,1),(12,1),(1,12),(12,12)]]
model('potter_table',legs+[box([0,11,0],[16,14,16],'wood'),box([3,14,3],[13,16,13],'clay')],dict(wood=wood,clay=clay))
model('casting_table',[box([0,0,0],[3,12,16],'brick'),box([13,0,0],[16,12,16],'brick'),box([0,11,0],[16,14,16],'brick'),box([2,14,2],[14,16,14],'clay')],dict(brick=brick,clay=clay))
model('hand_bellows',[box([1,0,1],[15,3,15],'wood'),box([2,3,2],[14,10,14],'leather'),box([1,10,1],[15,13,15],'wood'),box([0,4,5],[3,9,11],'dark')],dict(wood=wood,leather='minecraft:block/brown_terracotta',dark='minecraft:block/coal_block'))
for n in ['copper_hearth','ceramic_kiln','solid_firebox']:model(n,[box([0,0,0],[16,16,16],'brick'),box([3,4,0],[13,12,1],'dark'),box([5,6,0],[11,8,2],'clay')],dict(brick=brick,dark='minecraft:block/coal_block',clay=clay))
model('copper_workshop_lantern',[box([2,0,2],[14,2,14],'copper'),box([2,12,2],[14,14,14],'copper'),box([3,2,3],[5,12,5],'copper'),box([11,2,11],[13,12,13],'copper'),box([5,3,5],[11,11,11],'light'),box([6,14,6],[10,16,10],'copper')],dict(copper=copper,light='minecraft:block/glowstone'))
# Only currently needed GUI images are installed in main resources.
gui=A/'textures/gui';gui.mkdir(parents=True,exist_ok=True)
for n in ['slot','structure_preview']:
 p=gui/(n+'.png')
 if not p.exists():shutil.copy2(ROOT/'src/gametest/resources/assets/smog/textures/gui'/(n+'.png'),p)
p=gui/'copper_workshop.png'
if not p.exists():
 im=Image.new('RGBA',(176,190),(198,198,198,255));d=ImageDraw.Draw(im);d.rectangle((0,0,175,189),outline=(35,35,35,255));d.line([(1,188),(1,1),(174,1)],fill=(245,245,245,255));d.line([(2,188),(174,188),(174,2)],fill=(78,78,78,255));im.save(p)
print('Copper models and item/GUI textures ready.')
