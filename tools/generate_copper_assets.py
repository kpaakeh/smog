"""Editable copper-era prototype block models and vanilla-derived item sprites. No paid assets."""
from pathlib import Path
from zipfile import ZipFile
from io import BytesIO
import json,shutil,sys,colorsys
REFRESH_ICONS="--refresh-icons" in sys.argv
MANUAL_ICONS={"copper_concentrate","seed_cake","plant_oil_bottle","plant_oil_bucket","lubricating_compound","waxed_cord"}
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
FIRED=[(46,38,31),(70,57,45),(99,80,61),(128,105,80),(155,132,103),(184,160,126),(209,186,151)]
TOOL_SHAPES={
 'pickaxe':[(4,1),(10,1),(14,5),(14,9),(12,9),(11,6),(8,4),(4,4)],
 'axe':[(8,1),(13,1),(15,3),(15,6),(11,8),(8,7)],
 'shovel':[(11,1),(14,2),(15,5),(12,8),(9,7),(8,5)],
 'hoe':[(5,2),(12,2),(14,4),(14,7),(11,7),(11,5),(6,5)],
 'hammer':[(6,1),(9,1),(15,7),(15,10),(12,10),(6,4)],
 'chisel':[(11,1),(14,1),(14,4),(10,8),(8,7)]}
TOOL_PART_NAMES={'wooden_handle','light_handle','reinforced_handle','short_handle','hollow_copper_handle','cord_binding','leather_binding','copper_clamp','bronze_clamp','fiber_wrap','leather_wrap','wool_wrap','tool_handle','stone_work_hammer'}
def tool_asset(n):return n in TOOL_PART_NAMES or n.startswith(('modular_','tool_binding_','tool_wrap_')) or n.endswith('_head') or any(n.endswith(k+'_mold') for k in TOOL_SHAPES)
def head_icon(shape,colors,cast=False):
 mask=Image.new('1',(16,16));ImageDraw.Draw(mask).polygon(TOOL_SHAPES[shape],fill=1);im=Image.new('RGBA',(16,16))
 for y in range(16):
  for x in range(16):
   if not mask.getpixel((x,y)):continue
   left=x==0 or not mask.getpixel((x-1,y));top=y==0 or not mask.getpixel((x,y-1));right=x==15 or not mask.getpixel((x+1,y));bottom=y==15 or not mask.getpixel((x,y+1))
   index=0 if right or bottom else len(colors)-1 if top else len(colors)-2 if left else 3 if (x+y)%4 else 4
   im.putpixel((x,y),colors[index]+(255,))
 if cast:
  d=ImageDraw.Draw(im);d.line((9,7,6,11),fill=colors[1]+(255,),width=2);d.rectangle((4,11,7,13),fill=colors[2]+(255,));d.point((5,11),fill=colors[-1]+(255,))
 return im

def handle_icon(name):
 im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im);short=name=='short_handle';a=(6,11) if short else (2,15)
 color=(192,131,76) if name=='hollow_copper_handle' else (141,101,54)
 d.line([a,(9,7)],fill=(51,36,22,255),width=3);d.line([a,(9,7)],fill=color+(255,),width=2);d.line([(a[0],a[1]-1),(8,7)],fill=(210,170,102,255),width=1)
 if name=='reinforced_handle':
  for x,y in [(4,12),(7,9)]:d.line((x-1,y-1,x+1,y+1),fill=COPPER[3]+(255,),width=2)
 if name=='light_handle':
  for x,y in [(4,12),(6,10)]:d.point((x,y),fill=(65,78,32,255))
 if name=='hollow_copper_handle':d.rectangle((1,13,3,15),fill=COPPER[0]+(255,));d.point((2,14),fill=(20,17,14,255))
 return im

def sprite(n,im):
 p=A/'textures/item'/(n+'.png');p.parent.mkdir(parents=True,exist_ok=True)
 if REFRESH_ICONS or tool_asset(n) and "--refresh-tool-icons" in sys.argv or n in MANUAL_ICONS and "--refresh-manual-icons" in sys.argv or not p.exists():im.save(p)
def plate(colors):
 raw=remap(image('item/iron_ingot'),colors);im=Image.new('RGBA',(16,16));part=raw.crop(raw.getbbox()).resize((14,5),Image.Resampling.NEAREST);im.alpha_composite(part,(1,6));return im
def pipe(colors):
 im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im)
 # Short diagonal cylinder with an open bore; no lenses, glass or telescope bands.
 d.polygon([(2,8),(8,2),(12,3),(14,7),(8,14),(4,13)],fill=colors[1]+(255,))
 d.polygon([(3,8),(9,3),(12,4),(13,7),(7,13),(4,12)],fill=colors[4]+(255,))
 d.line([(4,8),(9,3),(11,4)],fill=colors[-1]+(255,));d.line([(8,12),(13,7)],fill=colors[2]+(255,))
 d.polygon([(2,8),(5,7),(8,9),(9,12),(7,14),(4,13),(2,11)],fill=colors[3]+(255,))
 d.polygon([(3,9),(5,8),(7,10),(8,12),(6,13),(4,12)],fill=tuple(max(12,round(v*.28)) for v in colors[0])+(255,))
 d.line([(3,9),(5,8),(7,10)],fill=colors[-2]+(255,));d.line([(4,12),(6,13),(8,12)],fill=colors[1]+(255,))
 return im

def mold_icon(kind,colors):
 im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im)
 # Shallow rhomboid tray in three-quarter view, with a visible thick front rim.
 d.polygon([(1,6),(10,2),(14,6),(14,10),(5,14),(1,10)],fill=colors[1]+(255,))
 d.polygon([(1,6),(10,2),(14,6),(5,10)],fill=colors[4]+(255,))
 d.polygon([(1,7),(5,11),(14,7),(14,10),(5,14),(1,10)],fill=colors[2]+(255,))
 d.line([(1,6),(10,2),(14,6)],fill=colors[-1]+(255,));d.line([(1,7),(5,11),(14,7)],fill=colors[3]+(255,))
 # Project cavity outlines in the same inclined top plane, never across side faces.
 shapes={'ingot':[[(4,6),(9,4),(11,6),(6,8)]],
 'plate':[[(3,6),(9,3),(12,6),(6,9)]],
 'fitting':[[(4,6),(5,5),(6,6),(5,7)],[(7,5),(8,4),(9,5),(8,6)],[(7,7),(8,6),(9,7),(8,8)],[(10,6),(11,5),(12,6),(11,7)]],
 'pipe':[[(3,6),(9,3),(10,4),(4,7)],[(6,8),(12,5),(13,6),(7,9)]]}.get(kind,[[(3,6),(7,4),(12,5),(10,7),(7,6),(6,8)]])
 if kind in TOOL_SHAPES:shapes=[[(round(2+(x+y)*.34),round(6+(y-x)*.18)) for x,y in TOOL_SHAPES[kind]]]
 for points in shapes:
  d.polygon(points,fill=colors[0]+(255,));d.line(points[:2],fill=colors[1]+(255,));d.line(points[2:]+[points[0]],fill=colors[-2]+(255,))
 return im

for e in C['items']:
 n=e['name']
 if n in MANUAL_ICONS or n=='plant_fiber':continue
 if n.startswith('modular_') or n.endswith('_head'):
  shape=n.removeprefix('modular_') if n.startswith('modular_') else next(k for k in ['pickaxe','axe','shovel','hoe','hammer','chisel'] if n.endswith('_'+k+'_head'))
  colors=[(v,v,v) for v in [35,65,100,145,180,215,245]] if n.startswith('modular_') else BRONZE if n.startswith('bronze') else COPPER
  im=head_icon(shape,colors,'_cast_' in n)
 elif 'handle' in n:im=handle_icon(n)
 elif n.endswith('_binding') or n.endswith('_wrap'):im=image('item/leather' if n.startswith('leather') else 'block/white_wool' if n.startswith('wool') else 'item/string')
 elif n.endswith('_clamp'):im=remap(image('item/iron_nugget'),BRONZE if n.startswith('bronze') else COPPER)
 elif n=='stone_work_hammer':
  im=handle_icon('wooden_handle');im.alpha_composite(head_icon('hammer',[(v,v,v) for v in [25,45,65,95,125,155,185]]))
 elif n=='copper_plate':im=plate(COPPER)
 elif n.endswith('_fitting'):im=remap(image('item/iron_chain'),COPPER if n.startswith('copper') else BRONZE)
 elif n.endswith('_pipe_part'):im=pipe(COPPER if n.startswith('copper') else BRONZE)
 elif n=='metal_residue':im=image('item/iron_nugget')
 elif n=='tempered_clay':
  im=image('item/clay_ball');d=ImageDraw.Draw(im)
  for pos in [(5,7),(9,10),(10,6)]:d.point(pos,fill=(128,114,98,255))
 elif 'crucible' in n:im=remap(image('item/bowl'),RAW_CLAY)
 elif n=='configurator':im=remap(image('item/stick'),COPPER)
 else:
  colors=RAW_CLAY if n.startswith('unfired_') else FIRED;im=remap(image('item/brick'),colors)
  if 'mold' in n:im=mold_icon(n.removeprefix('unfired_').removesuffix('_mold'),colors)
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
 if p.exists() and 'Blockbench' in json.loads(p.read_text(encoding='utf-8')).get('credit',''):return
 textures=dict(textures);textures['particle']=next(iter(textures.values()))
 p.write_text(json.dumps(dict(parent='minecraft:block/block',textures=textures,elements=surface(elements),display=dict(gui=dict(rotation=[30,225,0],translation=[0,0,0],scale=[.6,.6,.6]),fixed=dict(rotation=[0,180,0],scale=[.5,.5,.5]))),indent=2)+'\n',encoding='utf-8')

# Large staggered refractory blocks in an ash/sand palette, not red vanilla brickwork.
refractory=Image.new('RGBA',(16,16),FIRED[1]+(255,));d=ImageDraw.Draw(refractory)
for row,(y0,y1) in enumerate([(0,6),(8,14)]):
 for x0,x1 in ([(-4,5),(7,16)] if row else [(0,9),(11,20)]):
  d.rectangle((x0,y0,x1,y1),fill=FIRED[3]+(255,))
  d.line((x0,y0,x1,y0),fill=FIRED[4]+(255,));d.line((x0,y0,x0,y1),fill=FIRED[4]+(255,))
  d.line((x0,y1,x1,y1),fill=FIRED[2]+(255,))
for x,y in [(2,3),(7,5),(13,2),(2,11),(10,10),(14,13)]:d.point((x,y),fill=FIRED[2]+(255,))
refractory.save(A/'textures/block/refractory_bricks.png')
remap(image('item/brick'),FIRED).save(A/'textures/item/firebrick.png')
# Hand-built device panels, avoiding the recognizable furnace mouth.
for name in ['hearth','kiln','firebox']:
 front=refractory.copy();d=ImageDraw.Draw(front)
 if name=='hearth':
  d.rectangle((3,3,12,12),fill=FIRED[1]+(255,));d.rectangle((4,4,11,11),fill=FIRED[4]+(255,))
  d.rectangle((5,5,10,10),fill=FIRED[2]+(255,));d.line((5,5,10,5),fill=FIRED[1]+(255,))
  d.rectangle((6,7,9,9),fill=COPPER[3]+(255,));d.line((6,7,9,7),fill=COPPER[-1]+(255,))
 elif name=='kiln':
  d.polygon([(3,12),(3,5),(5,3),(10,3),(12,5),(12,12)],fill=FIRED[1]+(255,))
  d.polygon([(4,11),(4,5),(6,4),(9,4),(11,5),(11,11)],fill=FIRED[3]+(255,))
  d.line((4,6,11,6),fill=FIRED[2]+(255,));d.line((4,10,11,10),fill=FIRED[2]+(255,))
  for y in [6,9]:d.rectangle((5,y,6,y+1),fill=COPPER[2]+(255,))
 else:
  d.rectangle((3,4,12,12),fill=FIRED[2]+(255,))
  for x in [4,7,10]:d.rectangle((x,5,x+1,11),fill=FIRED[0]+(255,));d.line((x,5,x,10),fill=FIRED[4]+(255,))
 front.save(A/('textures/block/refractory_'+name+'_front.png'))
leather=image('block/brown_terracotta')
for y in [6,10,13]:
 for x in range(16):
  r,g,b,a=leather.getpixel((x,y));leather.putpixel((x,y),(round(r*.78),round(g*.78),round(b*.78),a))
leather.save(A/'textures/block/bellows_leather.png')
brick='smog:block/refractory_bricks';clay='smog:block/refractory_bricks';wood='minecraft:block/oak_planks';copper='minecraft:block/copper_block'
for n,size in [('ceramic_crucible',12),('large_ceramic_crucible',16)]:
 lo=(16-size)/2;hi=16-lo;model(n,[box([lo,0,lo],[hi,3,hi],'clay'),box([lo,3,lo],[lo+2,14,hi],'clay'),box([hi-2,3,lo],[hi,14,hi],'clay'),box([lo+2,3,lo],[hi-2,14,lo+2],'clay'),box([lo+2,3,hi-2],[hi-2,14,hi],'clay')],dict(clay=clay))
model('firebrick_casing',[box([0,0,0],[16,16,16],'brick')],dict(brick=brick))
# Wall ports are flat sockets; detachable fittings provide the external geometry.
model('casting_spout',[box([0,0,0],[16,16,16],'brick'),box([4,4,0],[12,12,1],'brick'),box([6,6,0],[10,10,2],'dark')],dict(brick=brick,copper=copper,dark='minecraft:block/black_terracotta'))
model('casting_channel',[box([0,12,0],[16,14,16],'clay'),box([0,14,0],[3,16,16],'clay'),box([13,14,0],[16,16,16],'clay')],dict(clay=clay))
model('air_intake',[box([0,0,0],[16,16,16],'brick'),box([3,5,0],[13,11,1],'brick'),box([5,6,0],[11,10,2],'dark')],dict(brick=brick,copper=copper,dark='minecraft:block/black_terracotta'))
for name in ['casting_faucet','air_nozzle']:
 for opened in [False,True]:
  handle=box([7,13,3],[9,15,13],'hardware') if opened else box([3,13,7],[13,15,9],'hardware')
  if name=='casting_faucet':
   body=[box([4,6,12],[12,12,16],'copper'),box([6,7,7],[10,11,16],'copper'),box([6,3,6],[10,9,10],'copper'),box([4,0,4],[6,3,12],'copper'),box([10,0,4],[12,3,12],'copper'),box([6,0,4],[10,3,6],'copper'),box([6,0,10],[10,3,12],'copper'),box([6,2,6],[10,3,10],'dark'),box([7,11,7],[9,13,9],'copper')]
  else:
   body=[box([4,4,11],[12,12,16],'copper'),box([5,5,3],[11,11,12],'copper'),box([4,4,0],[6,12,4],'copper'),box([10,4,0],[12,12,4],'copper'),box([6,4,0],[10,6,4],'copper'),box([6,10,0],[10,12,4],'copper'),box([6,6,3],[10,10,4],'dark'),box([7,11,7],[9,13,9],'copper')]
  model(name+('_open' if opened else ''),body+[handle],dict(copper=brick,hardware=copper,dark='minecraft:block/black_terracotta'))
model('kiln_vent',[box([0,0,0],[16,5,16],'brick'),box([3,5,3],[6,16,13],'brick'),box([10,5,3],[13,16,13],'brick'),box([6,5,3],[10,16,6],'brick'),box([6,5,10],[10,16,13],'brick')],dict(brick=brick))
legs=[box([x,0,z],[x+3,12,z+3],{'all':'leg','up':'end','down':'end'}) for x,z in [(1,1),(12,1),(1,12),(12,12)]]
model('potter_table',legs+[box([0,11,0],[16,14,16],'wood'),box([3,14,3],[13,16,13],'clay')],dict(wood=wood,clay=clay,leg='minecraft:block/stripped_oak_log',end='minecraft:block/stripped_oak_log_top'))
model('casting_table',[box([0,0,0],[3,10,16],'brick'),box([13,0,0],[16,10,16],'brick'),box([0,9,0],[16,11,16],'brick'),box([2,11,2],[14,12,14],'clay')],dict(brick=brick,clay=clay))
model('hand_bellows',[box([1,0,1],[15,3,15],'wood'),box([2,3,2],[14,10,14],'leather'),box([1,10,1],[15,13,15],'wood'),box([0,4,5],[3,9,11],'dark')],dict(wood=wood,leather='smog:block/bellows_leather',dark='minecraft:block/coal_block'))
for n,face in [('copper_hearth','hearth'),('ceramic_kiln','kiln'),('solid_firebox','firebox')]:
 details={'hearth':[box([6,6,0],[10,10,2],'handle')],'kiln':[box([10,6,0],[12,10,2],'handle')],'firebox':[box([4,3,0],[12,5,2],'handle')]}[face]
 model(n,[box([0,0,1],[16,16,16],{'all':'brick','north':'front'})]+details,dict(brick=brick,front='smog:block/refractory_'+face+'_front',handle='minecraft:block/copper_block'))
# Modular manual workshop pieces keep their own world-scale UVs after assembly.
woodparts=dict(wood=wood,metal=copper,mesh='minecraft:block/iron_bars',log='minecraft:block/stripped_oak_log')
trough=[box([0,3,0],[16,5,16],'wood'),box([0,5,0],[2,13,16],'wood'),box([14,5,0],[16,13,16],'wood'),box([2,5,0],[14,13,2],'wood')]
model('water_tank',[box([0,0,0],[16,3,16],'wood'),box([0,3,0],[2,16,16],'wood'),box([14,3,0],[16,16,16],'wood'),box([2,3,0],[14,16,2],'wood'),box([2,3,14],[14,16,16],'wood'),box([0,5,0],[16,7,1],'metal'),box([0,12,0],[16,14,1],'metal')],woodparts)
model('hand_water_pump',[box([3,0,3],[13,3,13],'wood'),box([6,3,6],[10,12,10],'metal'),box([7,12,7],[9,16,9],'metal'),box([2,14,7],[14,16,9],'wood'),box([8,7,2],[10,9,6],'metal')],woodparts)
model('washing_section',trough,woodparts)
model('washing_trough',trough+[box([2,5,14],[14,13,16],'wood'),box([7,0,7],[9,3,9],'metal')],woodparts)
model('washing_sieve',[box([0,3,0],[16,5,16],'wood'),box([0,5,0],[2,9,16],'wood'),box([14,5,0],[16,9,16],'wood')]+[box([2,5,z],[14,6,z+1],'metal') for z in range(1,16,3)],woodparts)
model('workshop_support',[box([1,0,1],[5,16,5],'log'),box([11,0,11],[15,16,15],'log'),box([0,9,0],[16,12,16],'wood')],woodparts)
model('press_frame',[box([0,0,0],[4,16,4],'log'),box([12,0,12],[16,16,16],'log'),box([0,12,0],[16,16,16],'wood')],woodparts)
model('press_basket',[box([1,0,1],[15,3,15],'wood')]+[box([x,3,z],[x+2,13,z+2],'wood') for x in range(1,15,3) for z in range(1,15,3) if x in [1,13] or z in [1,13]],woodparts)
model('lever_press',[box([0,0,0],[16,4,16],'wood'),box([1,4,1],[4,16,4],'log'),box([12,4,12],[15,16,15],'log'),box([4,6,4],[12,8,12],'wood'),box([7,8,7],[9,15,9],'metal'),box([3,14,7],[13,16,9],'wood')],woodparts)
sprite('copper_concentrate',remap(image('item/raw_copper'),COPPER))
sprite('seed_cake',remap(image('item/dried_kelp'),FIRED))
sprite('plant_oil_bottle',tint(image('item/honey_bottle'),(210,161,57)))
sprite('lubricating_compound',tint(image('item/slime_ball'),(85,77,46)))
sprite('waxed_cord',tint(image('item/string'),(181,142,75)))
# New oil assets reuse water shading and animation, preserving the bucket's metal rim.
oil=(187,141,49)
for kind in ['still','flow','overlay']:
 path=A/('textures/block/plant_oil_'+kind+'.png')
 if not path.exists():
  tint(image('block/water_'+kind),oil).save(path)
  meta='assets/minecraft/textures/block/water_'+kind+'.png.mcmeta'
  if meta in Z.namelist():Path(str(path)+'.mcmeta').write_bytes(Z.read(meta))
bucket=image('item/water_bucket');colored=tint(bucket,oil)
for y in range(16):
 for x in range(16):
  r,g,b,a=bucket.getpixel((x,y))
  if a and b>r*1.2 and b>g*1.05:bucket.putpixel((x,y),colored.getpixel((x,y)))
sprite('plant_oil_bucket',bucket)
for name,im in [('workshop_water_surface',tint(image('block/water_still'),(70,116,210))),('plant_oil_surface',tint(image('block/water_still'),oil))]:
 path=A/('textures/block/'+name+'.png')
 if not path.exists():
  im=im.crop((0,0,16,16));im.putalpha(255);im.save(path)
model('copper_water_pipe_core',[box([5,5,5],[11,11,11],'copper')],dict(copper=copper))
model('copper_water_pipe_arm',[box([5,5,0],[11,11,5],'copper')],dict(copper=copper))
model('copper_water_pipe',[box([5,5,0],[11,11,16],'copper'),box([4,4,6],[12,12,10],'copper')],dict(copper=copper))
for name in ['water_valve','water_outlet']:
 for opened in [False,True]:
  body=[box([5,5,0],[11,11,16],'copper')] if name=='water_valve' else [box([5,5,8],[11,11,16],'copper'),box([5,0,5],[11,11,11],'copper')]
  handle=box([7,12,3],[9,14,13],'wood') if opened else box([3,12,7],[13,14,9],'wood')
  model(name+('_open' if opened else ''),body+[box([7,10,7],[9,12,9],'copper'),handle],dict(copper=copper,wood=wood))
sprite('plant_fiber',tint(image('item/string'),(192,181,112)))
model('fiber_press_basket',[box([1,0,1],[15,3,15],'wood'),box([1,3,1],[3,13,15],'wood'),box([13,3,1],[15,13,15],'wood'),box([3,3,1],[13,11,3],'cloth'),box([3,3,13],[13,11,15],'cloth')],dict(wood=wood,cloth='minecraft:block/white_wool'))
model('bronze_drive',[box([3,3,6],[13,13,16],'bronze'),box([6,6,0],[10,10,6],'bronze'),box([1,7,1],[15,9,3],'wood'),box([7,1,1],[9,15,3],'wood'),box([2,2,1],[5,5,3],'wood'),box([11,11,1],[14,14,3],'wood'),box([2,11,1],[5,14,3],'wood'),box([11,2,1],[14,5,3],'wood')],dict(bronze='smog:block/bronze_block',wood=wood))
model('tool_workbench',[box([x,0,z],[x+3,13,z+3],'leg') for x,z in [(1,1),(12,1),(1,12),(12,12)]]+[box([0,13,0],[16,16,16],'wood'),box([1,16,1],[5,18,5],'stone')],dict(leg='minecraft:block/stripped_oak_log',wood=wood,stone='minecraft:block/smooth_stone'))
for name,tiles in [('firebrick_tiles',True),('carved_firebrick',False)]:
 im=refractory.copy();d=ImageDraw.Draw(im)
 if tiles:
  for x in [0,8]:d.line((x,0,x,15),fill=FIRED[1]+(255,))
  for y in [0,8]:d.line((0,y,15,y),fill=FIRED[1]+(255,))
 else:
  d.rectangle((2,2,13,13),outline=FIRED[1]+(255,));d.rectangle((4,4,11,11),outline=FIRED[4]+(255,));d.line((4,7,11,7),fill=FIRED[2]+(255,))
 p=A/('textures/block/'+name+'.png')
 if not p.exists():im.save(p)
 model(name,[box([0,0,0],[16,16,16],'brick')],dict(brick='smog:block/'+name))
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

# Tool sprites are shape masks shared by all material profiles.
import subprocess
subprocess.run([sys.executable,str(ROOT/"tools/generate_tool_templates.py")]+(["--refresh-tool-templates"] if "--refresh-tool-templates" in sys.argv else []),check=True)
