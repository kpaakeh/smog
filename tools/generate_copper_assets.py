"""Editable copper-era prototype block models and vanilla-derived item sprites. No paid assets."""
from pathlib import Path
from zipfile import ZipFile
from io import BytesIO
import json,shutil
from PIL import Image,ImageDraw,ImageEnhance
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
 n=e['name'];base='item/iron_nugget' if n=='metal_residue' else 'item/brick' if 'firebrick' in n else 'item/bowl' if 'crucible' in n else 'item/clay_ball' if n=='tempered_clay' else 'item/stick' if n=='configurator' else 'item/iron_ingot' if 'plate'==n.split('_')[-1] else 'item/iron_nugget' if 'fitting'==n.split('_')[-1] else 'item/bamboo' if 'pipe_part' in n else 'item/brick'
 color=(170,170,170) if n=='metal_residue' else (174,109,64) if n.startswith('copper') else (179,134,76) if n.startswith('bronze') else (151,160,179) if n.startswith('unfired') else (220,191,154)
 im=tint(image(base),color)
 if 'mold' in n:
  d=ImageDraw.Draw(im);size={'ingot':(5,7,10,9),'plate':(4,6,11,10),'fitting':(6,7,8,8),'pipe':(3,8,12,8)}[n.removeprefix('unfired_').removesuffix('_mold')];d.rectangle(size,fill=(65,52,43,255))
 sprite(n,im)
def box(a,b,texture):return {'from':a,'to':b,'material':texture}
def uv(side,a,b):
 x,y,z=a;xx,yy,zz=b
 return {'north':[16-xx,16-yy,16-x,16-y],'south':[x,16-yy,xx,16-y],'west':[z,16-yy,zz,16-y],'east':[16-zz,16-yy,16-z,16-y],'up':[x,z,xx,zz],'down':[x,16-zz,xx,16-z]}[side]
def surface(elements):
 # Emit the exterior of the cuboid union. Internal/contact/overlapping faces
 # never enter the model, so they cannot fight for the same depth pixels.
 axes=[sorted({e[key][d] for e in elements for key in ['from','to']}) for d in range(3)]
 occupied={}
 for i in range(len(axes[0])-1):
  for j in range(len(axes[1])-1):
   for k in range(len(axes[2])-1):
    mid=[(axes[d][n]+axes[d][n+1])/2 for d,n in enumerate([i,j,k])]
    hits=[e for e in elements if all(e['from'][d]<mid[d]<e['to'][d] for d in range(3))]
    if hits:occupied[i,j,k]=hits[-1]['material']
 result=[]
 for (i,j,k),texture in occupied.items():
  a=[axes[0][i],axes[1][j],axes[2][k]];b=[axes[0][i+1],axes[1][j+1],axes[2][k+1]];faces={}
  for side,offset in [('west',(-1,0,0)),('east',(1,0,0)),('down',(0,-1,0)),('up',(0,1,0)),('north',(0,0,-1)),('south',(0,0,1))]:
   if (i+offset[0],j+offset[1],k+offset[2]) not in occupied:faces[side]={'texture':'#'+texture,'uv':uv(side,a,b)}
  if faces:result.append({'from':a,'to':b,'faces':faces})
 return result

def model(n,elements,textures):
 p=A/'models/block'/(n+'.json');p.parent.mkdir(parents=True,exist_ok=True)
 textures=dict(textures);textures['particle']=next(iter(textures.values()))
 p.write_text(json.dumps(dict(parent='minecraft:block/block',textures=textures,elements=surface(elements),display=dict(gui=dict(rotation=[30,225,0],translation=[0,0,0],scale=[.6,.6,.6]),fixed=dict(rotation=[0,180,0],scale=[.5,.5,.5]))),indent=2)+'\n',encoding='utf-8')

refractory=ImageEnhance.Brightness(ImageEnhance.Color(image('block/bricks')).enhance(1.2)).enhance(.72)
refractory.save(A/'textures/block/refractory_bricks.png')
firebrick=ImageEnhance.Brightness(ImageEnhance.Color(image('item/brick')).enhance(1.2)).enhance(.72)
firebrick.save(A/'textures/item/firebrick.png')
brick='smog:block/refractory_bricks';clay='smog:block/refractory_bricks';wood='minecraft:block/oak_planks';copper='minecraft:block/copper_block'
for n,size in [('ceramic_crucible',12),('large_ceramic_crucible',16)]:
 lo=(16-size)/2;hi=16-lo;model(n,[box([lo,0,lo],[hi,3,hi],'clay'),box([lo,3,lo],[lo+2,14,hi],'clay'),box([hi-2,3,lo],[hi,14,hi],'clay'),box([lo+2,3,lo],[hi-2,14,lo+2],'clay'),box([lo+2,3,hi-2],[hi-2,14,hi],'clay')],dict(clay=clay))
model('firebrick_casing',[box([0,0,0],[16,16,16],'brick')],dict(brick=brick))
model('casting_spout',[box([0,0,4],[16,16,16],'brick'),box([3,12,0],[13,14,16],'clay'),box([3,14,0],[5,16,16],'clay'),box([11,14,0],[13,16,16],'clay')],dict(brick=brick,clay=clay))
model('casting_channel',[box([0,12,0],[16,14,16],'clay'),box([0,14,0],[3,16,16],'clay'),box([13,14,0],[16,16,16],'clay')],dict(clay=clay))
model('air_intake',[box([0,0,4],[16,16,16],'brick'),box([4,4,0],[12,12,6],'dark')],dict(brick=brick,dark='minecraft:block/coal_block'))
model('kiln_vent',[box([0,0,0],[16,5,16],'brick'),box([3,5,3],[6,16,13],'brick'),box([10,5,3],[13,16,13],'brick'),box([6,5,3],[10,16,6],'brick'),box([6,5,10],[10,16,13],'brick')],dict(brick=brick))
legs=[box([x,0,z],[x+3,12,z+3],'wood') for x,z in [(1,1),(12,1),(1,12),(12,12)]]
model('potter_table',legs+[box([0,11,0],[16,14,16],'wood'),box([3,14,3],[13,16,13],'clay')],dict(wood=wood,clay=clay))
model('casting_table',[box([0,0,0],[3,10,16],'brick'),box([13,0,0],[16,10,16],'brick'),box([0,9,0],[16,11,16],'brick'),box([2,11,2],[14,12,14],'clay')],dict(brick=brick,clay=clay))
model('hand_bellows',[box([1,0,1],[15,3,15],'wood'),box([2,3,2],[14,10,14],'leather'),box([1,10,1],[15,13,15],'wood'),box([0,4,5],[3,9,11],'dark')],dict(wood=wood,leather='minecraft:block/brown_terracotta',dark='minecraft:block/coal_block'))
for n in ['copper_hearth','ceramic_kiln','solid_firebox']:model(n,[box([0,0,0],[16,16,16],'brick'),box([3,4,0],[13,12,1],'dark'),box([5,6,0],[11,8,2],'clay')],dict(brick=brick,dark='minecraft:block/coal_block',clay=clay))
# Only currently needed GUI images are installed in main resources.
gui=A/'textures/gui';gui.mkdir(parents=True,exist_ok=True)
for n in ['slot','structure_preview']:
 p=gui/(n+'.png')
 if not p.exists():shutil.copy2(ROOT/'src/gametest/resources/assets/smog/textures/gui'/(n+'.png'),p)
# Assemble the vanilla chest frame/inventory and furnace sprites without stretching pixels.
chest=image('gui/container/generic_54');furnace=image('gui/container/furnace')
im=Image.new('RGBA',(176,190),furnace.getpixel((10,10)))
im.paste(chest.crop((0,0,176,17)),(0,0));im.paste(chest.crop((0,17,7,94)),(0,17));im.paste(chest.crop((169,17,176,94)),(169,17));im.paste(chest.crop((0,126,176,222)),(0,94))
im.save(gui/'copper_workshop.png');chest.crop((7,17,25,35)).save(gui/'slot.png')
furnace.crop((79,34,103,50)).save(gui/'copper_progress_empty.png')
image('gui/sprites/container/furnace/burn_progress').save(gui/'copper_progress_full.png')
image('gui/sprites/container/furnace/lit_progress').save(gui/'copper_lit_progress.png')
for fluid in C['fluids']:
 if fluid['name'].startswith('molten_'):
  im=Image.open(A/('textures/block/'+fluid['name']+'_still.png')).convert('RGBA');im.crop((0,0,16,16)).save(A/('textures/block/'+fluid['name']+'_surface.png'))
print('Copper models, vanilla GUI and molten surfaces ready.')
