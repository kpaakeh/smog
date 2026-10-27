"""Create missing placeholder models/textures. Existing user artwork is never overwritten."""
from pathlib import Path
import json
from PIL import Image,ImageDraw

ROOT=Path(__file__).resolve().parents[1]
ASSETS=ROOT/'src/main/resources/assets/smog'
data=json.loads((ROOT/'src/main/content/smog.content.json').read_text(encoding='utf-8'))

def cube(a,b,tex='body'):
    return {'from':a,'to':b,'faces':{face:{'texture':'#'+tex,'uv':[0,0,16,16]} for face in ['up','down','north','south','east','west']}}
def model(name,elements):
    path=ASSETS/'models/block'/f'{name}.json';path.parent.mkdir(parents=True,exist_ok=True)
    if path.exists():return
    path.write_text(json.dumps({'textures':{'body':f'smog:block/{name}','particle':f'smog:block/{name}'},'elements':elements},indent=2)+'\n',encoding='utf-8')
def texture(name,color=(116,129,139),item=False):
    path=ASSETS/'textures'/('item' if item else 'block')/f'{name}.png';path.parent.mkdir(parents=True,exist_ok=True)
    if path.exists():return
    im=Image.new('RGBA',(16,16),(0,0,0,0) if item else color+(255,));d=ImageDraw.Draw(im)
    if item:
        if 'wire_coil' in name:
            d.ellipse((2,2,13,13),fill=color+(255,));d.ellipse((5,5,10,10),fill=(0,0,0,0));d.line((11,11,14,14),fill=color+(255,),width=2)
        elif name=='wrench':
            d.polygon([(2,1),(2,5),(5,7),(10,14),(13,11),(7,5),(6,1),(5,4),(3,4)],fill=color+(255,))
        else:d.polygon([(2,7),(8,3),(14,7),(8,12)],fill=color+(255,))
    else:
        d.rectangle((0,0,15,15),outline=tuple(int(c*.55) for c in color)+(255,));d.rectangle((5,5,10,10),fill=tuple(min(255,c+35) for c in color)+(255,))
    im.save(path)

base=[cube([1,0,1],[15,3,15]),cube([2,3,2],[14,14,14])]
frame=[cube([0,0,0],[16,2,16]),cube([0,14,0],[16,16,16])]+[cube([x,2,z],[x+2,14,z+2]) for x,z in [(0,0),(14,0),(0,14),(14,14)]]
for entry in data['blocks']+data['machines']:
    name=entry['name'];elements=base
    color=(115,128,140)
    if name=='machine_casing' or name=='generator_frame':elements=frame
    elif name=='solar_cell':elements=[cube([0,0,0],[16,2,16]),cube([1,2,1],[15,3,15])];color=(55,92,142)
    elif name in ['copper_coil','transformer']:
        elements=[cube([1,0,1],[15,2,15])]+[cube([x,2,z],[x+3,14,z+3]) for x,z in [(3,3),(10,3),(3,10),(10,10)]];color=(169,107,67)
    elif name=='rotor_blade':elements=[cube([0,6,6],[16,9,10]),cube([6,4,5],[10,11,11])]
    elif name=='heat_exchanger':elements=[cube([0,0,0],[16,2,16])]+[cube([x,2,1],[x+1,15,15]) for x in [1,4,7,10,13]]
    elif name=='lightning_tip':elements=[cube([4,0,4],[12,3,12]),cube([7,3,7],[9,16,9])];color=(179,126,76)
    elif name in ['steam_boiler','solid_generator','electric_furnace']:
        elements=[cube([1,0,1],[15,13,15]),cube([3,13,3],[13,15,13]),cube([10,12,10],[13,18,13]),cube([4,3,0],[12,9,1])]
    elif name=='crusher':elements=[cube([0,0,0],[16,3,16]),cube([1,3,1],[4,15,15]),cube([12,3,1],[15,15,15]),cube([4,8,2],[8,13,14]),cube([9,8,2],[12,13,14])]
    elif name=='press':elements=[cube([0,0,0],[16,3,16]),cube([1,3,3],[4,16,13]),cube([12,3,3],[15,16,13]),cube([1,13,2],[15,16,14]),cube([5,8,4],[11,10,12])]
    elif name=='block_breaker':elements=base+[cube([6,4,-3],[10,10,3]),cube([4,6,-2],[12,8,1])]
    elif name=='block_placer':elements=base+[cube([4,4,-1],[12,11,3]),cube([6,6,-2],[10,9,0])]
    elif name in ['fluid_pump','fluid_placer']:elements=[cube([2,0,2],[14,9,14]),cube([6,9,6],[10,16,10]),cube([6,3,-2],[10,7,4])];color=(82,138,157)
    elif name.startswith('connector_'):
        elements=[cube([4,0,4],[12,3,12]),cube([6,3,6],[10,10,10]),cube([5,10,5],[11,13,11]),cube([7,13,7],[9,16,9])]
        color=tuple(bytes.fromhex(entry.get('color','9FB0B8')))
    texture(name,color);model(name,elements)

generators={
'solar':[cube([5,0,4],[11,2,12]),cube([7,2,7],[9,12,9]),cube([0,12,0],[16,14,16])],
'lightning':[cube([2,0,2],[14,4,14]),cube([7,4,7],[9,16,9]),cube([5,14,5],[11,15,11])],
'thermal':[cube([0,0,0],[16,3,16])]+[cube([x,3,1],[x+1,15,15]) for x in [1,4,7,10,13]],
'flow':[cube([2,0,2],[14,4,14]),cube([6,4,6],[10,14,10]),cube([0,8,3],[16,11,5]),cube([7,3,3],[9,16,5])],
'wind':[cube([5,0,6],[11,2,12]),cube([7,2,8],[9,14,10]),cube([6,11,4],[10,14,8]),cube([0,11,2],[16,13,4]),cube([7,7,2],[9,16,4])],
'steam':[cube([1,0,1],[11,12,14]),cube([2,12,2],[10,14,13]),cube([12,0,2],[16,10,14]),cube([7,10,9],[9,16,11])],
'geothermal':[cube([0,0,0],[16,3,16]),cube([2,3,2],[14,13,14]),cube([4,13,4],[12,16,12])],
}
for kind,elements in (generators.items() if any(e.get("kind")=="GENERATOR_CONTROLLER" for e in data.get("machines",[])) else []):
    name='generator_'+kind;texture(name,(61,100,154) if kind=='solar' else (128,138,143));model(name,elements)
for item in data.get('items',[]):texture(item['name'],tuple(bytes.fromhex(item['color'])) if item.get('color') else {'wire_coil_lv':(190,119,66),'wire_coil_mv':(210,178,83),'wire_coil_hv':(123,133,150),'copper_plate':(187,119,77)}.get(item['name'],(154,164,172)),True)
print('Created missing industry placeholder JSON models and textures; existing artwork preserved.')
