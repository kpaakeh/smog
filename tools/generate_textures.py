"""Original 16px pixel art for Smog. Rebuild with Python + Pillow; no downloaded art."""
from pathlib import Path
import hashlib, json, random, sys
FORCE = "--force" in sys.argv
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'src/main/resources/assets/smog/textures'
CATALOG = json.loads((ROOT/'src/main/content/smog.content.json').read_text(encoding='utf-8'))

def shade(color, factor):
    return tuple(max(0, min(255, round(x * factor))) for x in color) + (255,)

def save(image, name):
    path = OUT / (name + '.png'); path.parent.mkdir(parents=True, exist_ok=True)
    if FORCE or not path.exists(): image.save(path)

def rng(name):
    return random.Random(int.from_bytes(hashlib.sha256(name.encode()).digest()[:8], 'big'))

def stone(name, base):
    im=Image.new('RGBA',(16,16),shade(base,1))
    return im

def ore(name, color, deep=False):
    im=stone('deep' if deep else 'stone', (62,64,69) if deep else (113,115,118));d=ImageDraw.Draw(im);r=rng(name)
    for x,y in [(2,3),(10,2),(7,7),(2,12),(12,11)]:
        x=max(1,min(12,x+r.randrange(-1,2)));y=max(1,min(12,y+r.randrange(-1,2)))
        d.polygon([(x,y),(x+2,y-1),(x+3,y+1),(x+2,y+3),(x,y+2)], fill=shade(color,.38))
        d.rectangle((x,y,x+1,y+1),fill=shade(color,1));d.point((x,y),fill=shade(color,1.5));d.point((x+2,y+2),fill=shade(color,.72))
    return im

def item(name, color, kind):
    im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im);dark=shade(color,.32)
    if kind=='ingot':
        d.polygon([(1,9),(4,4),(11,4),(14,8),(13,12),(3,12)],fill=dark)
        d.polygon([(3,8),(5,5),(10,5),(12,8)],fill=shade(color,1.35))
        d.polygon([(3,9),(12,9),(11,11),(3,11)],fill=shade(color,.85));d.line((4,6,9,6),fill=shade(color,1.8));d.line((13,9,12,11),fill=shade(color,.55))
    elif kind=='gem':
        d.polygon([(6,1),(10,2),(14,7),(10,14),(5,13),(2,8)],fill=dark)
        d.polygon([(6,3),(9,3),(11,7),(8,12),(5,10),(4,7)],fill=shade(color,1.1))
        d.polygon([(6,3),(8,4),(7,10),(5,10),(4,7)],fill=shade(color,1.6));d.line((9,4,11,7,9,11),fill=shade(color,.7));d.point((6,3),fill=(230,255,255,255))
    elif kind in ('raw','crushed'):
        pieces=[(3,3,8,9),(7,7,13,13),(1,9,5,13)] if kind=='raw' else [(2,5,6,9),(8,2,12,6),(7,10,12,13),(2,12,4,14)]
        r=rng(name)
        for x,y,xx,yy in pieces:
            d.polygon([(x+1,y),(xx-1,y),(xx,yy-2),(xx-2,yy),(x,yy-1),(x,y+2)],fill=dark)
            d.rectangle((x+1,y+1,xx-1,yy-1),fill=shade(color,.85));d.line((x+1,y+1,xx-2,y+1),fill=shade(color,1.5))
            if kind=='raw':d.point((x+2,yy-1),fill=(107,93,78,255))
    elif kind=='dust':
        d.polygon([(1,12),(4,8),(6,7),(7,4),(10,5),(12,9),(14,12),(12,14),(3,14)],fill=dark)
        d.polygon([(3,11),(6,8),(8,6),(10,8),(12,11),(10,12),(4,12)],fill=shade(color,1.1))
        r=rng(name)
        for _ in range(12):
            x,y=r.randrange(4,12),r.randrange(8,12); d.point((x,y),fill=shade(color,r.choice([.75,1.6])))
        d.point((2,6),fill=shade(color,1.4));d.point((12,4),fill=shade(color,1))
    else:
        d.polygon([(5,5),(9,3),(12,6),(11,10),(6,12),(3,9)],fill=dark)
        d.polygon([(5,6),(9,5),(10,7),(8,10),(5,9)],fill=shade(color,1.25));d.point((6,6),fill=shade(color,1.8))
    return im

for material in CATALOG['ores']:
    n=material['name'];c=tuple(bytes.fromhex(material['color']));product=material.get('product','ingot')
    save(ore(n,c),'block/'+n+'_ore');save(ore(n,c,True),'block/deepslate_'+n+'_ore')
    raw=stone(n+'raw',tuple(int(v*.55) for v in c))
    for x,y in [(0,0),(7,1),(2,8),(10,9)]:
        piece=item(n,c,'raw').crop((1,2,14,14)).resize((8,8),Image.Resampling.NEAREST);raw.alpha_composite(piece,(x,y))
    save(raw,'block/raw_'+n+'_block')
    forms=[('raw_'+n,'raw'),(n+'_dust','dust'),('crushed_'+n+'_ore','crushed'),(n+'_shard','crushed')]
    if product!='none':
        metal=stone(n+'metal',c);d=ImageDraw.Draw(metal);d.rectangle((0,0,15,15),outline=shade(c,.45));d.line((1,1,14,1),fill=shade(c,1.45));d.line((1,1,1,14),fill=shade(c,1.25))
        save(metal,'block/'+n+'_block');forms += [(n+'_'+product,product),(n+'_nugget','small'),(n+'_plate','ingot')]
    for filename,kind in forms:save(item(n+kind,c,kind),'item/'+filename)
for material in CATALOG.get('alloys',[])+CATALOG.get('metals',[]):
    n=material['name'];c=tuple(bytes.fromhex(material['color']))
    for kind in ['ingot','dust','nugget','plate']:save(item(n+kind,c,'small' if kind=='nugget' else 'ingot' if kind=='plate' else kind),'item/'+n+'_'+kind)
    save(stone(n,c),'block/'+n+'_block')
for material in CATALOG.get('deposits',[]):
    n=material['name'];c=tuple(bytes.fromhex(material['color']));im=stone(n,c);d=ImageDraw.Draw(im)
    for x,y in [(1,3),(8,1),(5,9),(12,12)]:d.rectangle((x,y,min(15,x+3),min(15,y+2)),fill=shade(c,.86))
    save(im,'block/'+n);save(item(n,c,'raw'),'item/'+n+'_chunk');save(item(n,c,'dust'),'item/'+n+'_dust')
for fluid in CATALOG.get('fluids',[]):
    if fluid['name']=='coolant':continue
    n=fluid['name'];c=tuple(bytes.fromhex(fluid['color']))
    bucket=Image.new('RGBA',(16,16));d=ImageDraw.Draw(bucket);d.polygon([(2,3),(13,3),(12,13),(4,14),(2,5)],fill=(38,43,49,255));d.polygon([(3,5),(12,5),(11,12),(5,13)],fill=(159,170,179,255));d.rectangle((4,4,11,8),fill=shade(c,1));d.line((3,2,12,2),fill=(205,212,220,255));save(bucket,'item/'+n+'_bucket')
    for kind,side in [('still',16),('flow',32)]:
        strip=Image.new('RGBA',(side,side*4))
        for frame in range(4):
            for y in range(side):
                for x in range(side):
                    v=225 if ((x+frame*2)//4+(y+frame)//4)%4 else 200;strip.putpixel((x,y+frame*side),(v,v,v,230))
        save(strip,'block/'+n+'_'+kind)
        meta=OUT/('block/'+n+'_'+kind+'.png.mcmeta')
        if not meta.exists():meta.write_text(json.dumps({'animation':{'frametime':5}})+'\n')
    save(Image.new('RGBA',(16,16),(220,220,220,130)),'block/'+n+'_overlay')

def machine(name, active=False, panel=False):
    c=(67,80,91);im=stone(name,c);d=ImageDraw.Draw(im)
    d.rectangle((0,0,15,15),outline=(22,30,37,255));d.rectangle((1,1,14,14),outline=(106,124,134,255));d.line((2,13,13,13),fill=(30,39,49,255))
    for x,y in [(2,2),(13,2),(2,13),(13,13)]:d.point((x,y),fill=(180,194,191,255))
    if panel:
        d.rectangle((4,3,11,8),fill=(15,29,32,255));d.rectangle((5,4,10,6),fill=(56,196,158,255) if active else (45,106,118,255));d.point((9,5),fill=(186,253,214,255))
        d.rectangle((4,10,11,11),fill=(18,24,30,255));d.point((5,10),fill=(229,164,56,255))
    else:
        for y in [5,7,9]:d.line((4,y,11,y),fill=(28,39,48,255))
    return im
if CATALOG.get('machines'):
    save(machine('casing'),'block/machine_casing');save(machine('idle',False,True),'block/machine_idle');save(machine('active',True,True),'block/machine_active');save(machine('controller',True,True),'block/controller')
    save(machine('shell'),'block/multiblock_shell');save(machine('multiblock_panel',True,True),'block/multiblock_panel')
im=Image.new('RGBA',(16,16));d=ImageDraw.Draw(im)
d.polygon([(2,3),(13,3),(12,13),(4,14),(2,5)],fill=(35,49,62,255));d.polygon([(3,5),(12,5),(11,12),(5,13)],fill=(142,166,182,255));d.rectangle((4,4,11,8),fill=(58,175,206,255));d.line((4,4,10,4),fill=(158,246,255,255));d.line((4,9,5,12),fill=(203,220,230,255));d.line((3,2,12,2),fill=(181,198,211,255));save(im,'item/coolant_bucket')
for kind,side in [('still',16),('flow',32)]:
    strip=Image.new('RGBA',(side,side*4))
    for f in range(4):
        for y in range(side):
            for x in range(side):
                k=((x+f*2)//4+(y+f)//4)%4
                strip.putpixel((x,y+f*side),[(183,221,229,210),(150,203,216,210),(166,214,226,210),(199,234,238,210)][k])
    save(strip,'block/coolant_'+kind)
    (OUT/('block/coolant_'+kind+'.png.mcmeta')).write_text(json.dumps({'animation':{'frametime':5}})+'\n')
save(Image.new('RGBA',(16,16),(180,220,230,130)),'block/coolant_overlay')

# A single normalized industrial model. The renderer fits it to the complete structure's bounds.
models=ROOT/'src/main/resources/assets/smog/models/block';models.mkdir(parents=True,exist_ok=True)
def cuboid(a,b,texture):
    return {'from':a,'to':b,'faces':{face:{'texture':'#'+texture,'uv':[0,0,16,16]} for face in ['north','south','west','east','up','down']}}
model={'textures':{'shell':'smog:block/multiblock_shell','panel':'smog:block/multiblock_panel','particle':'smog:block/multiblock_shell'},'elements':[
 cuboid([.35,1,.35],[15.65,14.5,15.65],'shell'),cuboid([0,0,0],[16,1.5,16],'shell'),cuboid([0,14,0],[16,16,16],'shell'),cuboid([5,4,0],[11,12,.4],'panel')]}
if CATALOG.get('multiblocks') and (FORCE or not (models/'assembled_machine.json').exists()):
    (models/'assembled_machine.json').write_text(json.dumps(model,indent=2)+'\n')

# Contact sheet for visual inspection; not bundled into the mod.
files=sorted(OUT.rglob('*.png'));sheet=Image.new('RGB',(9*112,((len(files)+8)//9)*100),(24,29,37));draw=ImageDraw.Draw(sheet)
for i,p in enumerate(files):
    x=(i%9)*112;y=(i//9)*100;im=Image.open(p).convert('RGBA');im=im.crop((0,0,im.width,im.width)).resize((64,64),Image.Resampling.NEAREST);sheet.paste(im,(x+24,y+2),im);draw.text((x+2,y+70),p.stem[:20],fill='white')
(ROOT/'build').mkdir(exist_ok=True);sheet.save(ROOT/'build/texture-contact-sheet.png')
print(f'Wrote {len(files)} original PNG textures and normalized assembled model.')
