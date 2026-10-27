"""Editable pixel GUI assets. Create missing files only; never replace user PNGs."""
from pathlib import Path
from PIL import Image,ImageDraw
import json
PROJECT=Path(__file__).resolve().parents[1]
CATALOG=json.loads((PROJECT/'src/main/content/smog.content.json').read_text(encoding='utf-8'))
ROOT=PROJECT/('src/main/resources' if CATALOG.get('machines') else 'src/gametest/resources')/'assets/smog/textures/gui'
ROOT.mkdir(parents=True,exist_ok=True)
def save(name,im):
    p=ROOT/(name+'.png')
    if not p.exists():im.save(p)
def panel(w,h,base=(198,198,198,255),raised=True):
    im=Image.new('RGBA',(w,h),base);d=ImageDraw.Draw(im)
    light=(245,245,245,255);dark=(78,78,78,255)
    d.rectangle((0,0,w-1,h-1),outline=(35,35,35,255))
    d.line([(1,h-2),(1,1),(w-2,1)],fill=light if raised else dark)
    d.line([(2,h-2),(w-2,h-2),(w-2,2)],fill=dark if raised else light)
    return im
for name in ['processor','generator','environment']:save(name,panel(176,190))
save('slot',panel(18,18,(139,139,139,255),False))
for name,w,h in [('button',34,16),('voltage_button',22,12)]:
    save(name,panel(w,h,(174,174,174,255)))
    save(name+'_hover',panel(w,h,(208,208,208,255)))
    save(name+'_disabled',panel(w,h,(120,120,120,255),False))
for active in [False,True]:
    im=Image.new('RGBA',(41,14));d=ImageDraw.Draw(im)
    color=(192,126,53,255) if active else (110,110,110,255)
    d.polygon([(0,4),(30,4),(30,0),(40,7),(30,13),(30,9),(0,9)],fill=color)
    d.line([(1,4),(30,4),(30,1),(39,7)],fill=(242,208,128,255) if active else (80,80,80,255))
    save('progress_full' if active else 'progress_empty',im)
for active in [False,True]:
    im=panel(12,52,(82,82,82,255),False);d=ImageDraw.Draw(im)
    if active:
        d.rectangle((2,2,9,49),fill=(208,145,42,255));d.line((3,2,3,49),fill=(247,208,102,255))
        for y in range(5,50,6):d.line((4,y,9,y),fill=(172,114,28,255))
    save('energy_full' if active else 'energy_empty',im)
for active in [False,True]:
    im=Image.new('RGBA',(14,14));d=ImageDraw.Draw(im)
    d.polygon([(1,12),(0,7),(3,9),(4,4),(7,0),(8,6),(11,3),(13,8),(12,12),(9,13),(4,13)],fill=(219,132,41,255) if active else (111,111,111,255))
    if active:d.polygon([(4,11),(6,7),(7,5),(8,10),(10,9),(9,12),(6,13)],fill=(248,213,111,255))
    save('fuel_full' if active else 'fuel_empty',im)
im=Image.new('RGBA',(162,76),(35,44,52,255));ImageDraw.Draw(im).rectangle((0,0,161,75),outline=(83,90,95,255));save('structure_preview',im)
print('Missing GUI PNGs created; existing artwork preserved.')
