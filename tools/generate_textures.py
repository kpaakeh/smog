"""Recolor local Minecraft assets, preserving vanilla silhouettes, shading and animation.
Usage: python tools/generate_textures.py --force [--minecraft-jar PATH]
No original game assets are extracted into the repository.
"""
from pathlib import Path
from zipfile import ZipFile
from io import BytesIO
import argparse, json, colorsys
from PIL import Image, ImageDraw
ROOT=Path(__file__).resolve().parents[1]
parser=argparse.ArgumentParser(description=__doc__)
parser.add_argument('--force',action='store_true')
parser.add_argument('--minecraft-jar',type=Path,default=ROOT/'.gradle-user-home/caches/fabric-loom/26.3/minecraft-client.jar')
args=parser.parse_args()
OUT=ROOT/'src/main/resources/assets/smog/textures'
CATALOG=json.loads((ROOT/'src/main/content/smog.content.json').read_text(encoding='utf-8'))
archive=ZipFile(args.minecraft_jar)

def vanilla(name):
    return Image.open(BytesIO(archive.read('assets/minecraft/textures/'+name+'.png'))).convert('RGBA')
def rgb(hex):return tuple(bytes.fromhex(hex))
def recolor(im,color):
    # Preserve the vanilla luminance contrast; change only its palette.
    im=im.copy();target=tuple(v/255 for v in color);h,s,v=colorsys.rgb_to_hsv(*target)
    for y in range(im.height):
        for x in range(im.width):
            r,g,b,a=im.getpixel((x,y))
            if not a:continue
            l=(max(r,g,b)+min(r,g,b))/510
            light=min(1,max(0,l*(.52+.58*v)))
            saturation=s*(.9 if light<.72 else .65)
            r,g,b=colorsys.hsv_to_rgb(h,saturation,light)
            im.putpixel((x,y),(round(r*255),round(g*255),round(b*255),a))
    return im

def save(im,name):
    path=OUT/(name+'.png');path.parent.mkdir(parents=True,exist_ok=True)
    if args.force or not path.exists():
        temporary=path.with_suffix('.tmp.png')
        im.save(temporary)
        temporary.replace(path)

def ore(color,deep):
    # Exact vanilla host rock outside the iron-ore inclusion mask. Both ore faces
    # use the game's own deep/shallow shading and mineral outlines.
    base=vanilla('block/deepslate' if deep else 'block/stone')
    sample=vanilla('block/deepslate_iron_ore' if deep else 'block/iron_ore')
    tinted=recolor(sample,color)
    for y in range(16):
        for x in range(16):
            if sample.getpixel((x,y))!=base.getpixel((x,y)):
                base.putpixel((x,y),tinted.getpixel((x,y)))
    return base

forms={'raw':'item/raw_iron','ingot':'item/iron_ingot','gem':'item/diamond','nugget':'item/iron_nugget',
       'dust':'item/redstone','crushed':'item/glowstone_dust','shard':'item/flint','plate':'item/iron_ingot'}
def form(name,kind,color):
    im=recolor(vanilla(forms[kind]),color)
    if kind=='plate':
        # Flatten the existing ingot sprite; do not invent a separate pixel-art style.
        part=im.getbbox();cropped=im.crop(part);im=Image.new('RGBA',(16,16));im.alpha_composite(cropped.resize((14,5),Image.Resampling.NEAREST),(1,6))
    save(im,'item/'+name)
for m in CATALOG['ores']:
    n=m['name'];c=rgb(m['color']);product=m.get('product','ingot')
    save(ore(c,False),'block/'+n+'_ore');save(ore(c,True),'block/deepslate_'+n+'_ore')
    save(recolor(vanilla('block/raw_iron_block'),c),'block/raw_'+n+'_block')
    for name,kind in [('raw_'+n,'raw'),(n+'_dust','dust'),('crushed_'+n+'_ore','crushed'),(n+'_shard','shard')]:form(name,kind,c)
    if product!='none':
        save(recolor(vanilla('block/iron_block'),c),'block/'+n+'_block')
        for name,kind in [(n+'_'+product,product),(n+'_nugget','nugget'),(n+'_plate','plate')]:form(name,kind,c)
for m in CATALOG.get('alloys',[])+CATALOG.get('metals',[]):
    n=m['name'];c=rgb(m['color'])
    for kind in ['ingot','dust','nugget','plate']:form(n+'_'+kind,kind,c)
    save(recolor(vanilla('block/iron_block'),c),'block/'+n+'_block')
rocks={'limestone':'calcite','dolomite':'diorite','kaolin':'clay','rock_salt':'calcite','fluorite':'amethyst_block','phosphorite':'tuff','graphite':'basalt_side'}
for m in CATALOG.get('deposits',[]):
    n=m['name'];c=rgb(m['color']);save(recolor(vanilla('block/'+rocks[n]),c),'block/'+n)
    form(n+'_chunk','shard',c);form(n+'_dust','dust',c)
for f in CATALOG['fluids']:
    n=f['name'];c=rgb(f['color']);bucket=vanilla('item/water_bucket')
    tinted=recolor(bucket,c)
    # Keep the original metal rim/handle; recolor only blue water pixels.
    for y in range(16):
        for x in range(16):
            r,g,b,a=bucket.getpixel((x,y))
            if a and b>r*1.2 and b>g*1.05:
                pixel=tinted.getpixel((x,y))
                if n=='crude_oil':pixel=tuple(round(v*.18) for v in pixel[:3])+(255,)
                bucket.putpixel((x,y),pixel)
    save(bucket,'item/'+n+'_bucket')
    for kind in ['still','flow','overlay']:
        im=vanilla('block/water_'+kind)
        im=recolor(im,c)
        # Oil is opaque; aqueous materials preserve the original water alpha.
        if n=='crude_oil':
            for y in range(im.height):
                for x in range(im.width):
                    r,g,b,a=im.getpixel((x,y));im.putpixel((x,y),(round(r*.2),round(g*.18),round(b*.16),255))
        save(im,'block/'+n+'_'+kind)
        meta='assets/minecraft/textures/block/water_'+kind+'.png.mcmeta'
        if meta in archive.namelist():
            (OUT/('block/'+n+'_'+kind+'.png.mcmeta')).write_bytes(archive.read(meta))
# Visual QA contact sheet is a build artifact, never a game texture.
files=sorted(OUT.rglob('*.png'));sheet=Image.new('RGB',(9*140,((len(files)+8)//9)*104),(36,39,44));draw=ImageDraw.Draw(sheet)
for i,p in enumerate(files):
    x=i%9*140;y=i//9*104;im=Image.open(p).convert('RGBA');im=im.crop((0,0,im.width,im.width)).resize((64,64),Image.Resampling.NEAREST)
    sheet.paste(im,(x+38,y+2),im);draw.text((x+3,y+72),p.stem[:23],fill='white')
(ROOT/'build').mkdir(exist_ok=True);sheet.save(ROOT/'build/texture-contact-sheet.png')
print('Vanilla-derived textures:',len(files))
