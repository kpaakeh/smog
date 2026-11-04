"""Local editable PPTX + matching PDF from the audited PNG manifest."""
from pathlib import Path
import json,csv,math,collections,hashlib,sys
from io import BytesIO
from PIL import Image,ImageDraw
from pptx import Presentation
from pptx.util import Inches,Pt
from pptx.dml.color import RGBColor
from pptx.enum.shapes import MSO_SHAPE
from reportlab.pdfgen import canvas
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.lib.utils import ImageReader
ROOT=Path(__file__).resolve().parents[1];R=ROOT/'build/review/2026-11-04/texture-polish';OUT=ROOT/'build/presentation/Smog_0.0.4';MEDIA=OUT/'media';MEDIA.mkdir(parents=True,exist_ok=True)
rows=json.loads((R/'comparison.json').read_text(encoding='utf-8'));afterfile=ROOT/'docs/TEXTURE_REWORK_004_RATINGS.csv';after={}
if afterfile.exists():after={r['texture']:r for r in csv.DictReader(afterfile.open(encoding='utf-8-sig'))}
draft='--draft' in sys.argv
if not draft:assert all(r['texture'] in after for r in rows if r['changed']),'Independent after ratings incomplete'
for r in rows:
 a=after.get(r['texture']) if r['changed'] else None;r['day_before']=float(r['in_game_day_score']);r['low_before']=float(r['in_game_low_light_score']);r['day_after']=float(a['day_after']) if a else None if r['changed'] else r['day_before'];r['low_after']=float(a['low_after']) if a else None if r['changed'] else r['low_before'];r['reason_after']=a.get('reason_after','') if a else ''
PAPER='F1EEE5';CARD='FFFCF5';INK='222A29';MUTED='6C716B';COPPER='AD623E';LINE='DAD4C7';GREEN='356C50';RED='A24D3B'
W,H=13.333333,7.5
pdfmetrics.registerFont(TTFont('Arial','C:/Windows/Fonts/arial.ttf'));pdfmetrics.registerFont(TTFont('Arial-Bold','C:/Windows/Fonts/arialbd.ttf'))
score=lambda v:'—' if v is None else str(int(v)) if v==int(v) else f'{v:.1f}'
byname={r['texture']:r for r in rows};changed=[r for r in rows if r['changed']]
LANG=json.loads((ROOT/'build/resources/main/assets/smog/lang/ru_ru.json').read_text(encoding='utf-8'))
MATERIAL_LABEL={k.removeprefix('tool.smog.material.'):v for k,v in LANG.items() if k.startswith('tool.smog.material.')}
def friendly(name):
 stem=Path(name).stem
 for prefix in ['item.smog.','block.smog.']:
  if prefix+stem in LANG:return LANG[prefix+stem]
 gui={'copper_workshop':'Окно мастерской','smeltery':'Окно плавильни','slot':'Ячейка','structure_preview':'Просмотр конструкции','copper_progress_empty':'Фон прогресса','copper_progress_full':'Заполнение прогресса','copper_lit_progress':'Подсветка прогресса'}
 return gui.get(stem,stem.replace('_',' '))

def path_for(name,before=False):return R/'before'/name if before else ROOT/'build/resources/main/assets/smog/textures'/name

def thumb(name,before=False):
 p=path_for(name,before);key=hashlib.sha256(p.read_bytes()+name.encode()+b'preview3').hexdigest()[:16];out=MEDIA/(key+'.png')
 if out.exists():return out
 im=Image.open(p).convert('RGBA')
 if p.with_suffix('.png.mcmeta').exists() and im.height>im.width:im=im.crop((0,0,im.width,im.width))
 if 'tool_forms/' in name or 'tool_overlays/' in name:
  tint=(255,150,108);im.putdata([tuple(round(c*t/255) for c,t in zip(px[:3],tint))+(px[3],) for px in im.getdata()])
 result=Image.new('RGB',(256,256),(225,223,215));d=ImageDraw.Draw(result)
 for y in range(0,256,32):
  for x in range(0,256,32):
   if (x//32+y//32)%2:d.rectangle((x,y,x+31,y+31),fill=(236,234,227))
 factor=min(224/im.width,224/im.height);im=im.resize((max(1,round(im.width*factor)),max(1,round(im.height*factor))),Image.Resampling.NEAREST);result.paste(im,((256-im.width)//2,(256-im.height)//2),im);result.save(out);return out

def wrap(text,w,size,bold=False):
 font='Arial-Bold' if bold else 'Arial';out=[]
 for line in str(text).split('\n'):
  current=''
  for word in line.split(' '):
   trial=(current+' '+word).strip()
   if current and pdfmetrics.stringWidth(trial,font,size)>w*72:out.append(current);current=word
   else:current=trial
  out.append(current)
 return out
class Deck:
 def __init__(self):
  self.p=Presentation();self.p.slide_width=Inches(W);self.p.slide_height=Inches(H);self.pdf=canvas.Canvas(str(OUT/'Smog_0.0.4_Текстуры.pdf'),pagesize=(W*72,H*72));self.n=0;self.index=[]
 def rect(self,x,y,w,h,fill=CARD,line=None):
  sh=self.s.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(x), Inches(y), Inches(w), Inches(h));sh.fill.solid();sh.fill.fore_color.rgb=RGBColor.from_string(fill);sh.line.fill.background() if line is None else None
  if line:sh.line.color.rgb=RGBColor.from_string(line);sh.line.width=Pt(.6)
  self.pdf.setFillColor('#'+fill);self.pdf.setStrokeColor('#'+(line or fill));self.pdf.rect(x*72,(H-y-h)*72,w*72,h*72,fill=1,stroke=bool(line))
 def text(self,text,x,y,w,h,size=16,color=INK,bold=False):
  lines=wrap(text,w,size,bold);required=len(lines)*size*1.18/72
  if required>h+.08:raise RuntimeError(f'Text overflow slide{self.n}: {text[:55]} needs{required:.2f} in{h}')
  sh=self.s.shapes.add_textbox(Inches(x),Inches(y), Inches(w), Inches(h));tf=sh.text_frame;tf.word_wrap=False;tf.margin_left=tf.margin_right=tf.margin_top=tf.margin_bottom=0
  for i,line in enumerate(lines):
   p=tf.paragraphs[0] if i==0 else tf.add_paragraph();p.space_before=Pt(0);p.space_after=Pt(0);p.line_spacing=Pt(size*1.18);r=p.add_run();r.text=line;r.font.name='Arial';r.font.size=Pt(size);r.font.bold=bold;r.font.color.rgb=RGBColor.from_string(color)
   self.pdf.setFont('Arial-Bold' if bold else 'Arial',size);self.pdf.setFillColor('#'+color);self.pdf.drawString(x*72,(H-y)*72-size*.94-i*size*1.18,line)
 def image(self,path,x,y,w,h):
  im=Image.open(path);ratio=min(w/im.width,h/im.height);iw,ih=im.width*ratio,im.height*ratio;ix=x+(w-iw)/2;iy=y+(h-ih)/2
  self.s.shapes.add_picture(str(path),Inches(ix),Inches(iy),width=Inches(iw),height=Inches(ih));self.pdf.drawImage(ImageReader(im),ix*72,(H-iy-ih)*72,iw*72,ih*72,mask='auto')
 def slide(self,title,sub=''):
  if self.n:self.pdf.showPage()
  self.n+=1;self.s=self.p.slides.add_slide(self.p.slide_layouts[6]);self.rect(0,0,W,H,PAPER);self.rect(.45,.32,.08,.43,COPPER);self.text(title,.68,.29,12.1,.55,26,bold=True)
  if sub:self.text(sub,.68,.91,12,.38,11,MUTED)
  self.text('SMOG · 0.0.4 · 04.11.2026',.48,7.12,9,.2,8,MUTED);self.text(f'{self.n:03}',12.2,7.10,.65,.24,10,MUTED)
 def card(self,row,x,y,w=4.0,h=2.62):
  self.rect(x,y,w,h,CARD,LINE);self.text(friendly(row['texture']),x+.15,y+.12,w-.3,.27,10,bold=True);self.text(row['texture'],x+.15,y+.48,w-.3,.18,7,MUTED)
  if row['changed']:
   self.text('ДО',x+.28,y+.58,1.1,.2,8,MUTED);self.text('ПОСЛЕ',x+1.87,y+.58,1.25,.2,8,COPPER,bold=True);self.image(thumb(row['texture'],True),x+.16,y+.82,1.25,1.12);self.image(thumb(row['texture']),x+1.74,y+.82,1.25,1.12)
   if row['texture']=='block/handcart.png':
    self.text(f"Модель: день {score(row['day_after'])} / 10",x+.19,y+2.05,w-.38,.24,11);self.text(f"Модель: слабый свет {score(row['low_after'])} / 10",x+.19,y+2.33,w-.38,.24,10,MUTED)
   else:
    self.text(f"День: {score(row['day_before'])} → {score(row['day_after'])} / 10",x+.19,y+2.05,w-.38,.24,11)
    self.text(f"Слабый свет: {score(row['low_before'])} → {score(row['low_after'])} / 10",x+.19,y+2.33,w-.38,.24,10,MUTED)
  else:
   self.image(thumb(row['texture']),x+.18,y+.73,1.47,1.47);self.text(f"День\n{score(row['day_after'])} / 10",x+1.98,y+.82,1.7,.69,16,bold=True);self.text(f"Слабый свет\n{score(row['low_after'])} / 10",x+1.98,y+1.62,1.7,.65,13,MUTED)
 def save(self):self.pdf.save();self.p.save(str(OUT/'Smog_0.0.4_Текстуры.pptx'));(OUT/'slide-index.json').write_text(json.dumps(self.index,ensure_ascii=False,indent=2),encoding='utf-8')
d=Deck();d.slide('Медная мастерская','Smog 0.0.4 · независимый аудит и переработка текстур')
d.text('Текстуры, которые\nчитаются в игре',.7,1.55,5.0,1.6,31,bold=True);d.text('Новые обломки руды, деревянная тележка, светлые грани материалов и различимый декор.',.72,3.3,4.8,1.4,18,MUTED)
hero=next(iter((R/'cart-final').glob('*cart-texture-day-view-0.png')),None) or next(iter((R/'game').glob('*cart-texture-day-view-0.png')),None)
if hero:d.image(hero,5.65,1.47,7.1,4.65)
for i,(value,label) in enumerate([('611','текстур в аудите'),(str(len(changed)),'переработано'),('41','авторский PNG сохранён')]):
 x=.72+i*4.18;d.rect(x,6.2,3.95,.68,CARD);d.text(value,x+.14,6.3,.85,.4,23,COPPER,True);d.text(label,x+1.05,6.42,2.8,.25,10)
d.slide('Как читать оценки','Одинаковые игровые условия: дневной свет и blocklight 7')
for i,(title,body) in enumerate([('Две оценки','Дневная читаемость и видимость в слабом освещении оцениваются отдельно по шкале 1–10.'),('Настоящие кадры','Оценка рисунка сделана на негорящих игровых карточках. Тележка дополнительно показана как реальная модель.'),('Изменения видны','Только переработанные PNG показываются парой «до / после». Остальные — одна картинка и текущая оценка.')]):
 y=1.6+i*1.65;d.rect(.7,y,11.9,1.4,CARD);d.text(title,.95,y+.2,3,.75,20,bold=True);d.text(body,4.25,y+.23,7.9,.9,17)
d.slide('Что переработано','Палитра, форма и контраст — без изменения версии и игровых рецептов')
items=[('Осколки руды','Новый угловатый обломок с гранями и вкраплениями. Все RGB взяты из соответствующей сырой руды.'),('Тележка','Нарисованные деревянные поверхности в цветах досок и тёмное колесо. Модель и UV сохранены.'),('Пыль и материалы','Устранено повторное затемнение. Светлая грань отделена от основной массы и обводки.'),('Графит и фосфорит','Более различимые швы, трещины и мох. Сохранена основа из ванильных узоров.')]
for i,(title,body) in enumerate(items):
 x=.7+(i%2)*6.15;y=1.55+(i//2)*2.55;d.rect(x,y,5.9,2.3,CARD);d.text(title,x+.23,y+.2,5.4,.6,22,bold=True);d.text(body,x+.23,y+.91,5.4,1.1,17,MUTED)
for title,names in [('Новые осколки руды',['item/tin_shard.png','item/bauxite_shard.png','item/uranium_shard.png']),('Свет и фактура материалов',['item/tin_dust.png','item/rock_salt_chunk.png','item/ceramic_core.png']),('Декоративные поверхности',['block/graphite.png','block/mossy_graphite_bricks.png','block/mossy_phosphorite_bricks.png'])]:
 d.slide(title,'Оценки до и после относятся к одной текстуре и одинаковому типу освещения')
 for i,n in enumerate(names):d.card(byname[n],.55+i*4.22,1.68,4.0,2.62)
 d.text('Изменения',.73,4.74,2,.4,18,COPPER,True)
 for i,n in enumerate(names):d.text(byname[n]['reason_change'],.73+i*4.22,5.27,3.75,1.2,13,MUTED)
d.slide('Тележка: дерево и тёмное колесо','Текстура перерисована с разрешения автора; геометрия и UV не менялись')
for i,old in enumerate([True,False]):d.text('ДО' if old else 'ПОСЛЕ',.72+i*2.35,1.55,2,.3,11,COPPER,True);d.image(thumb('block/handcart.png',old),.65+i*2.35,1.98,2.15,2.15)
if hero:d.image(hero,5.65,1.5,7.05,4.5)
r=byname['block/handcart.png'];d.text(f"Игровая модель после переработки: день {score(r['day_after'])} / 10; слабый свет {score(r['low_after'])} / 10",.75,4.62,4.7,1.0,15)
d.text('Оценка после дана по игровой модели. Старая оценка плоского атласа напрямую с ней не сравнивается.',.75,6.1,11.8,.6,14,MUTED)
d.slide('Авторские текстуры сохранены','Исходники сверены по SHA-256. Исключение по разрешению автора — тележка.')
for i,n in enumerate(['block/deepslate_bauxite_ore.png','block/kaolin.png','item/configurator.png']):d.card(byname[n],.55+i*4.22,1.66)
d.text('В этой части нет сравнения «до / после»: файлы не перерабатывались.',.75,5.1,11.8,.7,18,MUTED)
d.slide('Результат независимой переоценки','Сопоставимые игровые карточки 111 переработанных PNG; тележка оценена отдельно по модели')
for j,(label,before_key,after_key) in enumerate([('Дневной свет','day_before','day_after'),('Слабый свет · уровень 7','low_before','low_after')]):
 y=1.65+j*2.4;d.text(label,.8,y,5.5,.5,22,bold=True)
 for k,(caption,key,color) in enumerate([('До',before_key,MUTED),('После',after_key,COPPER)]):
  vals=[r[key] for r in changed if r[key] is not None and r['texture']!='block/handcart.png'];mean=sum(vals)/len(vals) if vals else 0;yy=y+.7+k*.55;d.text(caption,.82,yy,1,.35,14,color);d.rect(2,yy+.02,max(.02,mean*.78),.3,color);d.text(f'{mean:.2f} / 10' if vals else 'Ожидает оценки',10.2,yy,2,.35,16,color,True)
d.text('Галерея далее: каждая из 611 текстур показана отдельно в небольшой группе.',.82,6.65,11.8,.3,13,MUTED)
brief_count=d.n
# Every texture appears once in the reference gallery, in small groups.

materials=sorted({m['name'] for cat in ['ores','metals','alloys','deposits'] for m in json.loads((ROOT/'src/main/content/smog.content.json').read_text(encoding='utf-8')).get(cat,[])},key=len,reverse=True)
def group(r):
 n=r['texture'];stem=Path(n).stem
 if n.startswith('gui/'):return 'Интерфейсы'
 if 'tool_forms/' in n:return 'Части инструментов · '+('отливки' if stem.startswith('cast_') else 'готовые части')
 if 'tool_overlays/' in n:return 'Накладки и крепления'
 if 'tool_molds/' in n:return 'Керамические формы · '+('сырые' if stem.startswith('unfired_') else 'обожжённые')
 if 'casting_molds/' in n:return 'Литейные формы'
 if stem.startswith('molten_'):
  material=stem.removeprefix('molten_').rsplit('_',1)[0];return 'Расплав · '+MATERIAL_LABEL.get(material,material)
 for m in materials:
  if stem==m or stem.startswith(m+'_') or stem.startswith('raw_'+m) or stem=='deepslate_'+m+'_ore' or stem.startswith(('polished_'+m,'mossy_'+m,'cracked_'+m,'chiseled_'+m)):return ('Блоки · ' if n.startswith('block/') else 'Предметы · ')+MATERIAL_LABEL.get(m,m)
 if n.startswith('block/'):return 'Оборудование и рабочие поверхности'
 return 'Материалы и компоненты мастерской'
groups=collections.OrderedDict()
for r in rows:groups.setdefault(group(r),[]).append(r)
for name,rs in groups.items():
 for start in range(0,len(rs),6):
  part=rs[start:start+6];d.slide(name,f'Каталог текстур · {start+1}–{start+len(part)} из {len(rs)} · день / слабый свет')
  for i,r in enumerate(part):d.card(r,.45+(i%3)*4.2,1.45+(i//3)*2.78)
  d.index.append({'slide':d.n,'group':name,'textures':[r['texture'] for r in part]})
d.save()
(OUT/'review-data.json').write_text(json.dumps(rows,ensure_ascii=False,indent=2),encoding='utf-8')
(OUT/'README.txt').write_text('Smog 0.0.4 — обзор текстур\n\nПолная презентация: обзор и каталог всех 611 текстур, небольшими группами.\nPPTX: редактируемый текст и встроенные изображения.\nPDF: версия для просмотра.\n\nИсходный аудит: 611 текстур. Сравнение до/после есть только у фактически переработанных PNG.\n',encoding='utf-8')
print(json.dumps({'output':str(OUT),'slides':d.n,'gallery_textures':sum(len(x['textures']) for x in d.index),'changed':len(changed),'draft':draft},ensure_ascii=False))
