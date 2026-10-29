"""Only new smeltery assets. Existing artist PNGs and Blockbench models are preserved."""
from pathlib import Path
from zipfile import ZipFile
from io import BytesIO
import json,ast
from PIL import Image
ROOT=Path(__file__).resolve().parents[1];A=ROOT/'src/main/resources/assets/smog'
source=ast.parse((ROOT/'tools/generate_copper_assets.py').read_text(encoding='utf-8'))
for node in source.body:
 if isinstance(node,ast.FunctionDef) and node.name in {'box','uv','surface','model'}:exec(compile(ast.Module(body=[node],type_ignores=[]),'<model helpers>','exec'))
textures={'brick':'smog:block/refractory_bricks','glass':{'sprite':'minecraft:block/glass','force_translucent':True},'copper':'minecraft:block/copper_block'}
model('casting_basin',[box([0,0,0],[16,3,16],'brick'),box([0,3,0],[2,16,16],'brick'),box([14,3,0],[16,16,16],'brick'),box([2,3,0],[14,16,2],'brick'),box([2,3,14],[14,16,16],'brick')],textures)
model('smeltery_fuel_tank',[box([0,0,0],[16,3,16],'brick'),box([0,13,0],[16,16,16],'brick')]+[box([x,3,z],[x+3,13,z+3],'brick') for x,z in [(0,0),(13,0),(0,13),(13,13)]]+[box([3,3,0],[13,13,1],'glass'),box([3,3,15],[13,13,16],'glass'),box([0,3,3],[1,13,13],'glass'),box([15,3,3],[16,13,13],'glass')],textures)
model('smeltery_glass',[box([0,0,0],[16,16,16],'glass')]+[box([0,0,0],[16,2,16],'brick'),box([0,14,0],[16,16,16],'brick'),box([0,2,0],[2,14,16],'brick'),box([14,2,0],[16,14,16],'brick')],textures)
with ZipFile(ROOT/'.gradle-user-home/caches/fabric-loom/26.3/minecraft-client.jar') as archive:
 def native(name):return Image.open(BytesIO(archive.read('assets/minecraft/textures/'+name+'.png'))).convert('RGBA')
 gui=A/'textures/gui/smeltery.png'
 if not gui.exists():
  chest=native('gui/container/generic_54');furnace=native('gui/container/furnace');im=Image.new('RGBA',(248,244),(0,0,0,0));im.paste(Image.new('RGBA',(176,148),furnace.getpixel((10,10))),(72,0));im.paste(chest.crop((0,0,176,17)),(72,0))
  for y in range(17,148,18):
   h=min(18,148-y);im.paste(chest.crop((0,17,7,17+h)),(72,y));im.paste(chest.crop((169,17,176,17+h)),(241,y))
  im.paste(chest.crop((0,126,176,222)),(72,148));im.paste(Image.new('RGBA',(70,140),furnace.getpixel((10,10))),(0,0));im.paste(chest.crop((0,0,63,17)),(0,0));im.paste(chest.crop((169,0,176,17)),(63,0))
  for y in range(17,133,18):
   h=min(18,133-y);im.paste(chest.crop((0,17,7,17+h)),(0,y));im.paste(chest.crop((169,17,176,17+h)),(63,y))
  im.paste(chest.crop((0,215,63,222)),(0,133));im.paste(chest.crop((169,215,176,222)),(63,133));im.save(gui)
 # A metal canister keeps native item shading; its capability carries arbitrary fluid amounts.
 canister=A/'textures/item/fluid_canister.png'
 if not canister.exists():
  im=native('item/bucket');im.save(canister)
 color=(247,203,85)
 for kind in ['still','flow','overlay']:
  target=A/('textures/block/blazing_fuel_'+kind+'.png')
  if target.exists():continue
  im=native('block/'+('water_overlay' if kind=='overlay' else 'lava_'+kind))
  for y in range(im.height):
   for x in range(im.width):
    r,g,b,a=im.getpixel((x,y));v=max(r,g,b)/255;im.putpixel((x,y),tuple(round(v*c) for c in color)+(a,))
  im.save(target)
  metadata='assets/minecraft/textures/block/'+('water_overlay' if kind=='overlay' else 'lava_'+kind)+'.png.mcmeta'
  if metadata in archive.namelist():target.with_suffix('.png.mcmeta').write_bytes(archive.read(metadata))
 target=A/'textures/item/blazing_fuel_bucket.png'
 if not target.exists():
  im=native('item/lava_bucket')
  for y in range(16):
   for x in range(16):
    r,g,b,a=im.getpixel((x,y))
    if a and r>g*1.12 and r>b*1.4:im.putpixel((x,y),(r,min(255,round(g*1.25)),round(b*.8),a))
  im.save(target)
forms=json.loads((ROOT/'src/main/content/tool_templates.json').read_text(encoding='utf-8'))['forms']
shapes=[('ingot','ingot_mold'),('plate','plate_mold'),('fitting','fitting_mold'),('pipe','pipe_mold')]+[(f['id'],f['id'].replace('_head','_mold')) for f in forms if f['category']=='head']
for shape,base in shapes:
 target=A/('textures/item/casting_molds/'+base+'.png')
 if target.exists():continue
 source=A/('textures/item/tool_molds/'+shape+'.png') if shape.endswith('_head') else A/('textures/item/'+base+'.png')
 im=Image.open(source).convert('RGBA')
 for y in range(im.height):
  for x in range(im.width):
   r,g,b,a=im.getpixel((x,y));v=max(r,g,b);im.putpixel((x,y),(v,v,v,a))
 target.parent.mkdir(parents=True,exist_ok=True);im.save(target)
print('Smeltery assets prepared; existing artist assets preserved.')

for name,source in [('smeltery_fuel_surface',None),('blazing_fuel_surface',A/'textures/block/blazing_fuel_still.png')]:
 target=A/('textures/block/'+name+'.png')
 if not target.exists():
  if source is None:
   with ZipFile(ROOT/'.gradle-user-home/caches/fabric-loom/26.3/minecraft-client.jar') as archive:im=Image.open(BytesIO(archive.read('assets/minecraft/textures/block/lava_still.png'))).convert('RGBA')
  else:im=Image.open(source)
  im.crop((0,0,16,16)).save(target)
