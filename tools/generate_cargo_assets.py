"""Editable copper logistics models, native textures and 16 pixels per block."""
from pathlib import Path
import ast,json
ROOT=Path(__file__).resolve().parents[1];A=ROOT/'src/main/resources/assets/smog'
source=ast.parse((ROOT/'tools/generate_copper_assets.py').read_text(encoding='utf-8'))
for node in source.body:
 if isinstance(node,ast.FunctionDef) and node.name in {'box','uv','surface','model'}:exec(compile(ast.Module(body=[node],type_ignores=[]),'<model helpers>','exec'))
textures={'wood':'minecraft:block/oak_planks','log':'minecraft:block/stripped_oak_log','copper':'minecraft:block/copper_block','rope':'minecraft:block/white_wool','dark':'minecraft:block/dark_oak_planks'}
model('loading_crane',[box([0,11,0],[16,16,16],'log'),box([3,5,3],[13,11,13],'wood'),box([2,6,2],[4,10,14],'copper'),box([12,6,2],[14,10,14],'copper'),box([7,0,7],[9,5,9],'rope')],textures)
parts=[box([2,4,0],[14,7,16],'wood'),box([1,7,0],[3,14,16],'wood'),box([13,7,0],[15,14,16],'wood'),box([3,7,0],[13,14,2],'wood'),box([3,7,14],[13,14,16],'wood'),box([0,3,7],[16,5,9],'copper'),box([3,5,16],[5,7,24],'log'),box([11,5,16],[13,7,24],'log')]
for x in [0,14]:parts.extend([box([x,0,5],[x+2,6,11],'dark'),box([x,2,7],[x+2,4,9],'copper')])
elements=surface(parts)
# Tile local UV coordinates instead of stretching faces over elongated handles.
for e in elements:
 for direction,face in e['faces'].items():
  a,b=e['from'],e['to'];dims=[b[i]-a[i] for i in range(3)];w,h=({'north':(dims[0],dims[1]),'south':(dims[0],dims[1]),'east':(dims[2],dims[1]),'west':(dims[2],dims[1]),'up':(dims[0],dims[2]),'down':(dims[0],dims[2])})[direction];face['uv']=[0,0,w,h]
data=dict(parent='minecraft:block/block',textures={**textures,'particle':textures['wood']},elements=elements,display={'gui':{'rotation':[25,225,0],'translation':[0,-1,0],'scale':[.55,.55,.55]},'ground':{'scale':[.4,.4,.4]},'fixed':{'scale':[.5,.5,.5]}})
p=A/'models/block/handcart.json';p.write_text(json.dumps(data,indent=2)+'\n',encoding='utf-8')

model('closed_crucible_insert',[box([0,0,0],[16,16,16],'brick'),box([4,5,0],[12,12,1],'dark')],{'brick':'smog:block/refractory_bricks','dark':'minecraft:block/deepslate'})

model('rope_anchor',[box([2,0,2],[14,3,14],'wood'),box([5,3,5],[11,12,11],'log'),box([2,8,2],[14,11,14],'wood'),box([3,11,5],[5,15,11],'copper'),box([11,11,5],[13,15,11],'copper'),box([5,12,6],[11,14,10],'rope')],textures)
from PIL import Image
from zipfile import ZipFile
from io import BytesIO
target=A/'textures/item/freight_rope.png'
if not target.exists():
 with ZipFile(ROOT/'.gradle-user-home/caches/fabric-loom/26.3/minecraft-client.jar') as archive:
  im=Image.open(BytesIO(archive.read('assets/minecraft/textures/item/string.png'))).convert('RGBA')
  for y in range(16):
   for x in range(16):
    r,g,b,a=im.getpixel((x,y));v=max(r,g,b)/255;im.putpixel((x,y),(round(v*190),round(v*150),round(v*90),a))
  im.save(target)

for metal,texture in [('copper','minecraft:block/copper_block'),('bronze','smog:block/bronze_block'),('silver','smog:block/silver_block'),('gold','minecraft:block/gold_block')]:
 model(metal+'_relief',[box([0,0,14],[16,16,16],'metal'),box([2,2,12],[14,14,14],'metal'),box([4,4,10],[12,6,12],'metal'),box([7,6,10],[9,12,12],'metal')],{'metal':texture})
for name,source,color in [('molding_sand','sand',(185,160,111)),('relief_pattern','oak_planks',(155,113,67)),('wax_relief_model','honeycomb',(227,181,72)),('sand_relief_mold','sand',(180,153,97)),('unfired_relief_mold','clay',(155,136,118)),('relief_mold','bricks',(143,119,86)),('ceramic_core','brick',(168,131,87))]:
 target=A/('textures/item/'+name+'.png')
 if target.exists():continue
 with ZipFile(ROOT/'.gradle-user-home/caches/fabric-loom/26.3/minecraft-client.jar') as archive:
  folder='block/' if source in {'sand','oak_planks','clay','bricks'} else 'item/'
  im=Image.open(BytesIO(archive.read('assets/minecraft/textures/'+folder+source+'.png'))).convert('RGBA')
  for y in range(im.height):
   for x in range(im.width):
    r,g,b,a=im.getpixel((x,y));v=max(r,g,b)/255;im.putpixel((x,y),tuple(round(v*c) for c in color)+(a,))
  if 'mold' in name or name in {'relief_pattern','wax_relief_model'}:
   from PIL import ImageDraw
   d=ImageDraw.Draw(im);d.rectangle((3,3,12,12),fill=tuple(round(c*.45) for c in color)+(255,));d.line((5,6,10,6),fill=color+(255,),width=2);d.line((8,6,8,10),fill=color+(255,),width=2)
  im.save(target)
