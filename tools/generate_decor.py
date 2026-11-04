"""Native stone families. Only recolor vanilla patterns; preserve 16px geometry and alpha."""
from pathlib import Path
from zipfile import ZipFile
from io import BytesIO
import json,sys,math
from PIL import Image

ROOT=Path(__file__).resolve().parents[1]
OUT=Path(sys.argv[1]) if len(sys.argv)>1 else ROOT/'build/generated/decor'
JAR=ROOT/'.gradle-user-home/caches/fabric-loom/26.3/minecraft-client.jar'
DATA=json.loads((ROOT/'src/main/content/decor.json').read_text(encoding='utf-8'))
TEXTURES=ROOT/'src/main/resources/assets/smog/textures/block'
styles={'polished':'polished_andesite','bricks':'stone_bricks','tiles':'deepslate_tiles','chiseled':'chiseled_stone_bricks','cracked_bricks':'cracked_stone_bricks','mossy_bricks':'mossy_stone_bricks'}
en,ru,tags,entries={},{},{},[]
seen=set()
def write(path,data):
 p=OUT/path;p.parent.mkdir(parents=True,exist_ok=True);p.write_text(json.dumps(data,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
def tag(registry,name,value):tags.setdefault(f'data/minecraft/tags/{registry}/{name}.json',[]).append('smog:'+value)
def named(family,style):
 if style=='base':return family['base']
 return family.get('existing',{}).get(style,('polished_'+family['stem'] if style=='polished' else 'chiseled_'+family['stem'] if style=='chiseled' else 'cracked_'+family['stem']+'_bricks' if style=='cracked_bricks' else 'mossy_'+family['stem']+'_bricks' if style=='mossy_bricks' else family['stem']+'_'+style))
def recolor(vanilla,color,target,contrast=1.0,moss=False):
 image=Image.open(BytesIO(vanilla)).convert('RGBA');pixels=list(image.getdata());mean=sum(.2126*r+.7152*g+.0722*b for r,g,b,a in pixels if a)/max(1,sum(a>0 for r,g,b,a in pixels))
 result=Image.new('RGBA',image.size)
 for y in range(image.height):
  for x in range(image.width):
   r,g,b,a=image.getpixel((x,y));ratio=((.2126*r+.7152*g+.0722*b)/max(1,mean))**contrast
   tint=(99,126,62) if moss and g>r*1.04 and g>b*1.1 else color
   result.putpixel((x,y),tuple(max(0,min(255,round(c*ratio))) for c in tint)+(a,))
 target.parent.mkdir(parents=True,exist_ok=True);result.save(target)
def russian(family,style,shape=None):
 adj={'firebrick':'огнеупорн','limestone':'известняков','fluorite':'флюоритов','phosphorite':'фосфоритов','kaolin':'каолинов','dolomite':'доломитов','rock_salt':'солян','graphite':'графитов'}[family['stem']]
 base=family['ru'].lower()
 if family['stem']=='firebrick' and not shape and style in ['base','polished']:return ('Полированные ' if style=='polished' else 'Огнеупорные ')+('огнеупорные кирпичи' if style=='polished' else 'кирпичи')
 if not shape:
  return {'base':base,'polished':('полированная ' if family['stem']=='rock_salt' else 'полированный ')+base,'bricks':adj+'ые кирпичи','tiles':adj+'ый плитняк','chiseled':'резные '+adj+'ые кирпичи','cracked_bricks':'потрескавшиеся '+adj+'ые кирпичи','mossy_bricks':'замшелые '+adj+'ые кирпичи'}[style].capitalize()
 noun={'stairs':'ступеньки','slab':'плита','wall':'ограда'}[shape]
 if style in ['base','polished'] and family['stem']!='firebrick':
  plural=shape=='stairs';prefix=('полированные ' if plural else 'полированная ') if style=='polished' else ''
  return (prefix+adj+('ые ' if plural else 'ая ')+noun).capitalize()
 material={'base':adj+'ых кирпичей','bricks':adj+'ых кирпичей','tiles':adj+'ого плитняка','polished':'полированных '+adj+'ых кирпичей','chiseled':'резных '+adj+'ых кирпичей','cracked_bricks':'потрескавшихся '+adj+'ых кирпичей','mossy_bricks':'замшелых '+adj+'ых кирпичей'}[style]
 return (noun+' из '+material).capitalize()

def loot(name,slab=False):
 item={'type':'minecraft:item','name':'smog:'+name,'modifier':[]}
 if slab:item['modifier'].append({'type':'minecraft:set_count','count':2,'condition':{'type':'minecraft:match_block','blocks':'smog:'+name,'state':{'type':'double'}}})
 item['modifier'].append({'type':'minecraft:explosion_decay'})
 write('data/smog/loot_table/blocks/'+name+'.json',{'type':'minecraft:block','pools':[{'rolls':1,'entries':[item]}]})

def recipe(name,data):write('data/smog/recipe/'+name+'.json',data)
def stonecut(source,result,count=1):recipe(result+'_from_'+source+'_stonecutting',{'type':'minecraft:stonecutting','ingredient':'smog:'+source,'result':{'id':'smog:'+result,'count':count}})
with ZipFile(JAR) as jar:
 for family in DATA['families']:
  base=family['base'];base_image=Image.open(TEXTURES/(family['texture']+'.png')).convert('RGBA');opaque=[p[:3] for p in base_image.getdata() if p[3]];color=tuple(sum(p[n] for p in opaque)/len(opaque) for n in range(3))
  for style in ['base',*styles]:
   name=named(family,style);existing=style=='base' or style in family.get('existing',{})
   texture=family['texture'] if style=='base' or family['stem']=='firebrick' and style=='bricks' else name
   if not existing:
    repair=family['stem']=='graphite' or family['stem']=='phosphorite' and style in ['polished','cracked_bricks','mossy_bricks']
    palette=tuple(min(255,c+18) for c in color) if repair and family['stem']=='phosphorite' else color
    recolor(jar.read('assets/minecraft/textures/block/'+styles[style]+'.png'),palette,OUT/'assets/smog/textures/block'/f'{texture}.png',contrast=1.7 if repair else 1.0,moss=repair and style=='mossy_bricks')
    entries.append({'name':name,'kind':'cube','parent':base});seen.add(name)
    write('assets/smog/blockstates/'+name+'.json',{'variants':{'':{'model':'smog:block/'+name}}})
    write('assets/smog/models/block/'+name+'.json',{'parent':'minecraft:block/cube_all','textures':{'all':'smog:block/'+texture}})
    write('assets/smog/items/'+name+'.json',{'model':{'type':'minecraft:model','model':'smog:block/'+name}});loot(name);tag('block','mineable/pickaxe',name)
    labels={'polished':('Polished '+family['en'],'Полированный блок: '+family['ru']),'bricks':(family['en']+' Bricks','Кирпичи: '+family['ru']),'tiles':(family['en']+' Tiles','Плитняк: '+family['ru']),'chiseled':('Chiseled '+family['en'],'Резной блок: '+family['ru']),'cracked_bricks':('Cracked '+family['en']+' Bricks','Треснувшие кирпичи: '+family['ru']),'mossy_bricks':('Mossy '+family['en']+' Bricks','Мшистые кирпичи: '+family['ru'])};en['block.smog.'+name]=labels[style][0];ru['block.smog.'+name]=russian(family,style)
    if style=='cracked_bricks':recipe(name+'_smelting',{'type':'minecraft:smelting','ingredient':'smog:'+named(family,'bricks'),'result':{'id':'smog:'+name},'experience':0.1,'cookingtime':200})
    elif style=='mossy_bricks':
     for ingredient in ['vine','moss_block']:recipe(name+'_from_'+ingredient,{'type':'minecraft:crafting_shapeless','ingredients':['smog:'+named(family,'bricks'),'minecraft:'+ingredient],'result':{'id':'smog:'+name}})
    else:
     stonecut(base,name)
     source=base if style=='polished' else named(family,'polished') if style=='bricks' else named(family,'bricks') if style=='tiles' else (family['stem'] if family['stem']=='firebrick' else base)+'_slab'
     recipe(name,{'type':'minecraft:crafting_shaped','pattern':['S','S'] if style=='chiseled' else ['SS','SS'],'key':{'S':'smog:'+source},'result':{'id':'smog:'+name,'count':1 if style=='chiseled' else 4}})
   for shape in ['stairs','slab','wall']:
    stem=family['stem'] if family['stem']=='firebrick' and style in ['base','bricks'] else name
    target=stem+'_'+shape
    if target in seen:continue
    seen.add(target);entries.append({'name':target,'kind':shape,'parent':name});tag('block','mineable/pickaxe',target);tag('block',shape+'s',target);tag('item',shape+'s',target)
    label={'stairs':(' Stairs',' — ступени'),'slab':(' Slab',' — плита'),'wall':(' Wall',' — ограда')}[shape];style_en=en.get('block.smog.'+name,family['en']);style_ru=ru.get('block.smog.'+name,family['ru']);en['block.smog.'+target]=style_en+label[0];ru['block.smog.'+target]=russian(family,style,shape)
    source='stone_stairs' if shape=='stairs' else 'stone_slab' if shape=='slab' else 'cobblestone_wall'
    states=jar.read('assets/minecraft/blockstates/'+source+'.json').decode().replace('minecraft:block/'+source,'smog:block/'+target)
    if shape=='slab':states=states.replace('minecraft:block/stone','smog:block/'+name)
    write('assets/smog/blockstates/'+target+'.json',json.loads(states))
    variants={'stairs':[('', 'stairs'),('_inner','inner_stairs'),('_outer','outer_stairs')],'slab':[('','slab'),('_top','slab_top')],'wall':[('_post','template_wall_post'),('_side','template_wall_side'),('_side_tall','template_wall_side_tall'),('_inventory','wall_inventory')]}[shape]
    for suffix,parent in variants:write('assets/smog/models/block/'+target+suffix+'.json',{'parent':'minecraft:block/'+parent,'textures':({'wall':'smog:block/'+texture} if shape=='wall' else {'bottom':'smog:block/'+texture,'top':'smog:block/'+texture,'side':'smog:block/'+texture})})
    write('assets/smog/items/'+target+'.json',{'model':{'type':'minecraft:model','model':'smog:block/'+target+('_inventory' if shape=='wall' else '')}});loot(target,shape=='slab');stonecut(name,target,2 if shape=='slab' else 1)
    recipe(target,{'type':'minecraft:crafting_shaped','pattern':{'stairs':['S  ','SS ','SSS'],'slab':['SSS'],'wall':['SSS','SSS']}[shape],'key':{'S':'smog:'+name},'result':{'id':'smog:'+target,'count':{'stairs':4,'slab':6,'wall':6}[shape]}})
write('decor.blocks.json',entries);write('decor.fragments.json',{'en_us':en,'ru_ru':ru,'tags':tags})
print('Native decor blocks:',len(entries),'; recolored vanilla patterns only; no source textures overwritten.')
