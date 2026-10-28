"""Editable copper-era prototype block models and vanilla-derived item sprites. No paid assets."""
from pathlib import Path
from zipfile import ZipFile
from io import BytesIO
import json,shutil,sys,colorsys
REFRESH_ICONS="--refresh-icons" in sys.argv
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
def luminance(c):return .2126*c[0]+.7152*c[1]+.0722*c[2]
def palette(im):return sorted({p[:3] for p in im.getdata() if p[3]},key=luminance)
def remap(im,colors):
 im=im.copy();levels=sorted({luminance(p) for p in im.getdata() if p[3]});ranks={v:i for i,v in enumerate(levels)}
 for y in range(im.height):
  for x in range(im.width):
   r,g,b,a=im.getpixel((x,y))
   if a:im.putpixel((x,y),colors[round(ranks[luminance((r,g,b))]*(len(colors)-1)/max(1,len(levels)-1))]+(a,))
 return im
COPPER=palette(image('item/copper_ingot'));RAW_CLAY=palette(image('item/clay_ball'))
BRONZE=[]
for c in COPPER:
 h,sat,val=colorsys.rgb_to_hsv(*(v/255 for v in c));BRONZE.append(tuple(round(v*255) for v in colorsys.hsv_to_rgb(h+.035,sat*.9,val*.9)))
FIRED=palette(ImageEnhance.Brightness(ImageEnhance.Color(image('item/brick')).enhance(1.2)).enhance(.72))
def sprite(n,im):
 p=A/'textures/item'/(n+'.png');p.parent.mkdir(parents=True,exist_ok=True)
 if REFRESH_ICONS or not p.exists():im.save(p)
def plate(colors):
 raw=remap(image('item/iron_ingot'),colors);im=Image.new('RGBA',(16,16));part=raw.crop(raw.getbbox()).resize((14,5),Image.Resampling.NEAREST);im.alpha_composite(part,(1,6));return im
for e in C['items']:
 n=e['name']
 if n=='copper_plate':im=plate(COPPER)
 elif n.endswith('_fitting'):im=remap(image('item/iron_chain'),COPPER if n.startswith('copper') else BRONZE)
 elif n.endswith('_pipe_part'):im=remap(image('item/spyglass'),COPPER if n.startswith('copper') else BRONZE)
 elif n=='metal_residue':im=image('item/iron_nugget')
 elif n=='tempered_clay':
  im=image('item/clay_ball');d=ImageDraw.Draw(im)
  for pos in [(5,7),(9,10),(10,6)]:d.point(pos,fill=(128,114,98,255))
 elif 'crucible' in n:im=remap(image('item/bowl'),RAW_CLAY)
 elif n=='configurator':im=remap(image('item/stick'),COPPER)
 else:
  colors=RAW_CLAY if n.startswith('unfired_') else FIRED;im=remap(image('item/brick'),colors)
  if 'mold' in n:
   kind=n.removeprefix('unfired_').removesuffix('_mold')
   grooves={'ingot':[(5,7,10,9)],'plate':[(4,6,11,10)],'fitting':[(5,6,6,7),(8,6,9,7),(5,9,6,10),(8,9,9,10)],'pipe':[(4,7,11,7),(5,9,12,9)]}[kind]
   for x0,y0,x1,y1 in grooves:
    for y in range(y0,y1+1):
     for x in range(x0,x1+1):
      if im.getpixel((x,y))[3]:im.putpixel((x,y),colors[0 if y==y0 else 1]+(255,))
    for x in range(x0,x1+1):
     if y1+1<16 and im.getpixel((x,y1+1))[3]:im.putpixel((x,y1+1),colors[-3]+(255,))
 sprite(n,im)
# The bronze family uses the same small ramp and vanilla silhouettes as copper parts.
for kind,base in [('ingot','iron_ingot'),('nugget','iron_nugget'),('dust','redstone')]:sprite('bronze_'+kind,remap(image('item/'+base),BRONZE))
sprite('bronze_plate',plate(BRONZE))
if REFRESH_ICONS or not (A/'textures/block/bronze_block.png').exists():remap(image('block/iron_block'),BRONZE).save(A/'textures/block/bronze_block.png')

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
   if (i+offset[0],j+offset[1],k+offset[2]) not in occupied:faces[side]={'texture':'#'+(texture.get(side,texture.get('all')) if isinstance(texture,dict) else texture),'uv':uv(side,a,b)}
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
# Native furnace apertures, copied at 1:1 scale onto the shared brick face.
for name,rect in [('hearth',(3,7,13,14)),('kiln',(3,4,13,14)),('firebox',(3,7,13,14))]:
 front=refractory.copy();furnace=image('block/furnace_front');front.alpha_composite(furnace.crop(rect),(rect[0],rect[1]));front.save(A/('textures/block/refractory_'+name+'_front.png'))
leather=image('block/brown_terracotta')
for y in [6,10,13]:
 for x in range(16):
  r,g,b,a=leather.getpixel((x,y));leather.putpixel((x,y),(round(r*.78),round(g*.78),round(b*.78),a))
leather.save(A/'textures/block/bellows_leather.png')
brick='smog:block/refractory_bricks';clay='smog:block/refractory_bricks';wood='minecraft:block/oak_planks';copper='minecraft:block/copper_block'
for n,size in [('ceramic_crucible',12),('large_ceramic_crucible',16)]:
 lo=(16-size)/2;hi=16-lo;model(n,[box([lo,0,lo],[hi,3,hi],'clay'),box([lo,3,lo],[lo+2,14,hi],'clay'),box([hi-2,3,lo],[hi,14,hi],'clay'),box([lo+2,3,lo],[hi-2,14,lo+2],'clay'),box([lo+2,3,hi-2],[hi-2,14,hi],'clay')],dict(clay=clay))
model('firebrick_casing',[box([0,0,0],[16,16,16],'brick')],dict(brick=brick))
model('casting_spout',[box([0,0,4],[16,16,16],'brick'),box([3,12,0],[13,14,16],'clay'),box([3,14,0],[5,16,16],'clay'),box([11,14,0],[13,16,16],'clay')],dict(brick=brick,clay=clay))
model('casting_channel',[box([0,12,0],[16,14,16],'clay'),box([0,14,0],[3,16,16],'clay'),box([13,14,0],[16,16,16],'clay')],dict(clay=clay))
model('air_intake',[box([0,0,4],[16,16,16],'brick'),box([4,4,0],[12,12,6],'dark')],dict(brick=brick,dark='minecraft:block/coal_block'))
model('kiln_vent',[box([0,0,0],[16,5,16],'brick'),box([3,5,3],[6,16,13],'brick'),box([10,5,3],[13,16,13],'brick'),box([6,5,3],[10,16,6],'brick'),box([6,5,10],[10,16,13],'brick')],dict(brick=brick))
legs=[box([x,0,z],[x+3,12,z+3],{'all':'leg','up':'end','down':'end'}) for x,z in [(1,1),(12,1),(1,12),(12,12)]]
model('potter_table',legs+[box([0,11,0],[16,14,16],'wood'),box([3,14,3],[13,16,13],'clay')],dict(wood=wood,clay=clay,leg='minecraft:block/stripped_oak_log',end='minecraft:block/stripped_oak_log_top'))
model('casting_table',[box([0,0,0],[3,10,16],'brick'),box([13,0,0],[16,10,16],'brick'),box([0,9,0],[16,11,16],'brick'),box([2,11,2],[14,12,14],'clay')],dict(brick=brick,clay=clay))
model('hand_bellows',[box([1,0,1],[15,3,15],'wood'),box([2,3,2],[14,10,14],'leather'),box([1,10,1],[15,13,15],'wood'),box([0,4,5],[3,9,11],'dark')],dict(wood=wood,leather='smog:block/bellows_leather',dark='minecraft:block/coal_block'))
for n,face in [('copper_hearth','hearth'),('ceramic_kiln','kiln'),('solid_firebox','firebox')]:
 model(n,[box([0,0,1],[16,16,16],{'all':'brick','north':'front'}),box([5,6,0],[11,8,2],'handle')],dict(brick=brick,front='smog:block/refractory_'+face+'_front',handle='minecraft:block/copper_block'))
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
