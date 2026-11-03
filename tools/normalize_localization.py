"""Normalize generated names using Russian grammar, not display-name concatenation.
Item/block names follow vanilla adjective+noun patterns. UI sentences are untouched.
"""
from pathlib import Path
import json
ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'build/generated/smog-content'
LANG=OUT/'assets/smog/lang'
root=json.loads((OUT/'smog.content.json').read_text(encoding='utf-8'))
ru=json.loads((LANG/'ru_ru.json').read_text(encoding='utf-8'))
en=json.loads((LANG/'en_us.json').read_text(encoding='utf-8'))
adjectives=dict(line.split('=',1) for line in """aluminum=алюминиевый
aluminum_bronze=алюминиево-бронзовый
brass=латунный
bronze=бронзовый
cast_iron=чугунный
chromium=хромовый
constantan=константановый
copper=медный
cord=верёвочный
cupronickel=мельхиоровый
duralumin=дюралюминиевый
electrum=электрумовый
fiber=волокнистый
gold=золотой
invar=инварный
iron=железный
lead=свинцовый
leather=кожаный
lithium=литиевый
manganese=марганцевый
molybdenum=молибденовый
netherite=незеритовый
nichrome=нихромовый
nickel=никелевый
nickel_superalloy=жаропрочный никелевый
silver=серебряный
solder=припойный
stainless_steel=нержавеющий стальной
steel=стальной
stone=каменный
tin=оловянный
titanium=титановый
titanium_alloy=титановый сплавной
tool_steel=инструментальный стальной
tungsten=вольфрамовый
tungsten_steel=вольфрамовый стальной
uranium=урановый
wood=деревянный
wool=шерстяной
wrought_iron=кричный железный
zinc=цинковый
bauxite=бокситовый
rare_earth=редкоземельный
""".strip().splitlines())
def adjective(material,gender='m'):
 words=adjectives[material].split();out=[]
 for word in words:
  ending={'f':'ая','n':'ое','p':'ые'}.get(gender)
  if ending:
   if word.endswith('ний'): ending={'f':'яя','n':'ее','p':'ие'}[gender]
   word=word[:-2]+ending
  out.append(word)
 return ' '.join(out)
complex_materials={a['name']:a['material_ru'] for a in root['alloys']+root['metals'] if '_' in a['name']}
def part_name(material,noun,gender):
 return noun+' из '+complex_materials[material] if material in complex_materials else adjective(material,gender)+' '+noun
def title(text):return text[0].upper()+text[1:]
def setname(key,text):ru[key]=title(text)
forms={f['id']:f for f in root['tool_forms']}
materials={m['id']:m for m in root['tool_materials']}
for key,part in root['tool_parts'].items():
 form=forms[part['form']];name=form['name']['ru_ru'].lower();gender='f' if name.startswith(('головка','обмотка','перевязь','обвязка')) else 'n' if name.startswith(('полотно','лезвие','крепление')) else 'm'
 prefix=({'f':'отлитая ','m':'отлитый ','n':'отлитое '}[gender]) if part['cast'] else ''
 setname('item.'+key.replace(':','.'),prefix+part_name(part['material'],name,gender))
for schema in root['tool_templates']:
 tool=schema['name']['ru_ru'].lower();gender='f' if schema['id'] in ['pickaxe','saw','shovel','hoe','katana'] else 'm'
 setname('item.smog.modular_'+schema['id'],('составная ' if gender=='f' else 'составной ')+tool)
 for material in root['tool_materials']:
  key='tool.smog.named.'+schema['id']+'.'+material['id'];setname(key,part_name(material['id'],tool,gender));en[key]=material['name']['en_us']+' '+schema['name']['en_us']
head_genitive={'pickaxe':'головки кирки','axe':'головки топора','shovel':'головки лопаты','hoe':'головки мотыги','hammer':'головки молота','saw':'полотна пилы','sword':'клинка меча','katana':'клинка катаны','machete':'клинка мачете','sickle':'лезвия серпа','ingot':'слитка','plate':'пластины','fitting':'фитинга','pipe':'трубы'}
for item in root['items']:
 name=item['name'];key='item.smog.'+name
 if name.endswith('_mold'):
  stem=name[:-5];prefix='Керамическая форма для '
  if stem.startswith('unfired_'):stem=stem[8:];prefix='Необожжённая форма для '
  elif stem.startswith('sand_'):stem=stem[5:];prefix='Песчаная форма для '
  elif stem.startswith('metal_'):stem=stem[6:];prefix='Металлическая форма для '
  if stem in head_genitive:ru[key]=prefix+head_genitive[stem]
for ore in root['ores']:
 material=ore['name'];adj=adjective(material);f=adjective(material,'f');p=adjective(material,'p')
 setname('block.smog.'+material+'_ore',f+' руда');setname('block.smog.deepslate_'+material+'_ore','глубинная '+f+' руда')
 raw_names={'tin':'рудное олово','zinc':'рудный цинк','lead':'рудный свинец','nickel':'рудный никель','bauxite':'необработанный боксит','manganese':'рудный марганец','chromium':'рудный хром','titanium':'рудный титан','tungsten':'рудный вольфрам','molybdenum':'рудный молибден','uranium':'рудный уран','lithium':'рудный литий','rare_earth':'редкоземельное сырьё'}
 setname('item.smog.raw_'+material,raw_names[material]);setname('block.smog.raw_'+material+'_block','блок рудного '+ore['material_ru'])
 for suffix,noun,gender in [('ingot','слиток','m'),('dust','пыль','f'),('nugget','самородок','m'),('plate','пластина','f')]:
  key='item.smog.'+material+'_'+suffix
  if key in ru:setname(key,(noun+' '+complex_materials[material] if material in complex_materials else adjective(material,gender)+' '+noun))
 setname('item.smog.crushed_'+material+'_ore','дроблёная '+f+' руда');setname('item.smog.'+material+'_shard',adj+' рудный осколок')
 if 'block.smog.'+material+'_block' in ru:setname('block.smog.'+material+'_block',adj+' блок')
for alloy in root['alloys']+root['metals']:
 material=alloy['name']
 for suffix,noun,gender in [('ingot','слиток','m'),('dust','пыль','f'),('nugget','самородок','m'),('plate','пластина','f')]:setname('item.smog.'+material+'_'+suffix,(noun+' '+complex_materials[material] if material in complex_materials else adjective(material,gender)+' '+noun))
 setname('block.smog.'+material+'_block','блок '+complex_materials[material] if material in complex_materials else adjective(material)+' блок')
ru['block.smog.raw_rare_earth_block']='Блок редкоземельного сырья'
# Keep terminology of existing blocks consistent with their newly added decorative families.
ru.update({'block.smog.firebrick_casing':'Огнеупорные кирпичи','block.smog.firebrick_tiles':'Огнеупорный плитняк','block.smog.carved_firebrick':'Резные огнеупорные кирпичи','block.smog.large_ceramic_crucible':'Большой керамический тигель','block.smog.copper_water_pipe':'Медная труба','block.smog.water_valve':'Медный запорный кран','block.smog.water_outlet':'Медный разливочный вывод','block.smog.water_tank':'Медный резервуар','block.smog.casting_faucet':'Литейный кран'})
genitives={m['name']:m['material_ru'] for m in root['ores']+root['alloys']+root['metals']}
genitives.update({'copper':'меди','iron':'железа','gold':'золота','netherite':'незерита'})
feminine={'copper','bronze','aluminum_bronze','brass','steel','stainless_steel','tool_steel','tungsten_steel'}
for fluid in root['fluids']:
 name=fluid['name']
 if name.startswith('molten_'):
  metal=name[7:];text=('расплавленной ' if metal in feminine else 'расплавленного ')+genitives[metal]
 else:text={'coolant':'хладагента','plant_oil':'растительного масла','crude_oil':'нефти','mineral_brine':'минерального рассола','smeltery_fuel':'топлива плавильни','blazing_fuel':'пылающего топлива'}.get(name)
 if text:ru['item.smog.'+name+'_bucket']='Ведро '+text
ru['block.smog.rope_anchor']='Анкер грузовой переправы'
# Fail on catalog-style separators in actual names, while allowing help/tooltip sentences.
name_keys={k for k in ru if k.startswith('block.smog.') or k.startswith('item.smog.') and not any(t in k for t in ['.help','.unlink','.tooltip'])}
bad={k:ru[k] for k in name_keys if ':' in ru[k] or ' — ' in ru[k]}
if bad:raise ValueError('Unnormalized item/block names: '+str(bad))
for name,data in [('ru_ru',ru),('en_us',en)]: (LANG/(name+'.json')).write_text(json.dumps(data,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
report=ROOT/'build/review/localization-ru-audit.json';report.parent.mkdir(parents=True,exist_ok=True);report.write_text(json.dumps({'entries':len(ru),'names':len(name_keys),'names_with_catalog_separators':bad,'names_reviewed':{k:ru[k] for k in sorted(name_keys)}},ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
print(f'Russian localization: {len(ru)} entries, {len(name_keys)} item/block names, no catalog separators')

