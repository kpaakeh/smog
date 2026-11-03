"""Recolor vanilla raw-ore sprites without changing their pixel geometry or alpha."""
from pathlib import Path
from zipfile import ZipFile
from io import BytesIO
import json
from PIL import Image,ImageDraw
ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'src/main/resources/assets/smog/textures/item'
JAR=ROOT/'.gradle-user-home/caches/fabric-loom/26.3/minecraft-client.jar'
def luminance(p):return .2126*p[0]+.7152*p[1]+.0722*p[2]
with ZipFile(JAR) as jar:
 preview=Image.new('RGBA',(5*128,160),(35,37,39,255));draw=ImageDraw.Draw(preview)
 for index,metal in enumerate(['copper','tin','lead','iron','gold']):
  source='raw_'+(metal if metal in ['copper','iron','gold'] else 'iron')
  original=Image.open(BytesIO(jar.read('assets/minecraft/textures/item/'+source+'.png'))).convert('RGBA')
  material=json.loads((ROOT/'src/main/content/tool_materials'/f'{metal}.json').read_text(encoding='utf-8'))
  color=tuple(bytes.fromhex(material['color']));pixels=list(original.getdata());mean=sum(luminance(p) for p in pixels if p[3])/sum(p[3]>0 for p in pixels)
  result=Image.new('RGBA',original.size)
  result.putdata([tuple(max(0,min(255,round(channel*luminance(pixel)/mean))) for channel in color)+(pixel[3],) if pixel[3] else pixel for pixel in pixels])
  assert list(original.getchannel('A').getdata())==list(result.getchannel('A').getdata())
  result.save(OUT/f'{metal}_concentrate.png')
  preview.alpha_composite(result.resize((128,128),Image.Resampling.NEAREST),(index*128,0));draw.text((index*128+8,138),metal,fill='white')
 path=ROOT/'build/review/concentrates.png';path.parent.mkdir(parents=True,exist_ok=True);preview.save(path)
print('5 recolored 16x16 raw ore sprites; original alpha masks preserved')
