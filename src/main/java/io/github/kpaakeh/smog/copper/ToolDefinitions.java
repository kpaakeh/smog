package io.github.kpaakeh.smog.copper;

import java.util.*;
import com.google.gson.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/** Material profiles and tool schemas; item identities are expanded automatically from these JSON records. */
public final class ToolDefinitions {
    public record Material(String id,int color,String repairItem,String nativePrefix,String molten,JsonObject profiles) {
        public JsonObject profile(Form form){var value=profiles.getAsJsonObject(form.category);if(value==null)return new JsonObject();var result=value.deepCopy();if(value.has("variants")&&value.getAsJsonObject("variants").has(form.variant))value.getAsJsonObject("variants").getAsJsonObject(form.variant).entrySet().forEach(e->result.add(e.getKey(),e.getValue()));return result;}
    }
    public record Form(String id,String category,String variant,int metalUnits,String miningTag,String nativeSuffix){}
    public record Part(Identifier item,Material material,Form form,boolean cast){public int color(){var profile=material.profile(form);return profile.has("color")?parseColor(profile.get("color").getAsString()):material.color;}}
    public record Role(String id,String category,List<String> forms,boolean optional,boolean primary){public boolean accepts(Part part){return part!=null&&part.form.category.equals(category)&&(forms.isEmpty()||forms.contains(part.form.id));}}
    public record Template(String id,String behavior,List<Role> roles,float maxSpeed,float maxDurability){}
    private static final Map<String,Material> MATERIALS=new LinkedHashMap<>();
    private static final Map<String,Form> FORMS=new LinkedHashMap<>();
    private static final Map<Identifier,Part> PARTS=new LinkedHashMap<>();
    private static final Map<String,Template> TEMPLATES=new LinkedHashMap<>();
    public static String string(JsonObject o,String key,String fallback){return o.has(key)?o.get(key).getAsString():fallback;}
    public static float number(JsonObject o,String key,float fallback){float value=o.has(key)?o.get(key).getAsFloat():fallback;if(!Float.isFinite(value))throw new IllegalArgumentException("Non-finite tool stat: "+key);return value;}
    public static int parseColor(String color){String hex=color.startsWith("#")?color.substring(1):color;if(!hex.matches("[0-9a-fA-F]{6}"))throw new IllegalArgumentException("Tool color needs six hex digits: "+color);return Integer.parseInt(hex,16);}
    public static void initialize(JsonObject root){
        MATERIALS.clear();FORMS.clear();PARTS.clear();TEMPLATES.clear();
        for(var value:root.getAsJsonArray("tool_materials")){var o=value.getAsJsonObject();String id=o.get("id").getAsString();MATERIALS.put(id,new Material(id,parseColor(o.get("color").getAsString()),string(o,"repair_ingredient",string(o,"repair_item","")),string(o,"native_prefix",""),string(o,"molten",""),o.getAsJsonObject("profiles")));}
        for(var value:root.getAsJsonArray("tool_forms")){var o=value.getAsJsonObject();String id=o.get("id").getAsString();FORMS.put(id,new Form(id,o.get("category").getAsString(),string(o,"variant","standard"),(int)number(o,"material_units",number(o,"metal_units",0)),string(o,"mining_tag",""),string(o,"native_suffix","")));}
        root.getAsJsonObject("tool_parts").entrySet().forEach(e->{var o=e.getValue().getAsJsonObject();var id=Identifier.parse(e.getKey());var material=MATERIALS.get(o.get("material").getAsString());var form=FORMS.get(o.get("form").getAsString());if(material==null||form==null)throw new IllegalArgumentException("Unknown tool part definition: "+id);PARTS.put(id,new Part(id,material,form,o.get("cast").getAsBoolean()));});
        for(var value:root.getAsJsonArray("tool_templates"))registerTemplate(parseTemplate(value.getAsJsonObject()));
        if(TEMPLATES.isEmpty())throw new IllegalArgumentException("No tool templates");
        for(var form:FORMS.values())if(form.category.equals("head")&&form.metalUnits<1)throw new IllegalArgumentException("Head form requires positive material units: "+form.id);
        for(var part:PARTS.values()){var profile=part.material.profile(part.form);if(part.form.category.equals("head")&&(number(profile,"durability",0)<1||number(profile,"mining_speed",1)<=0))throw new IllegalArgumentException("Invalid head stats: "+part.item);if(number(profile,"speed_multiplier",1)<=0||number(profile,"durability_multiplier",1)<=0)throw new IllegalArgumentException("Invalid support stats: "+part.item);part.color();}
    }
    private static final Map<String,Template> SNAPSHOTS=Collections.synchronizedMap(new LinkedHashMap<>(32,.75f,true){@Override protected boolean removeEldestEntry(Map.Entry<String,Template> entry){return size()>128;}});
    public static String snapshot(Template template){var o=new JsonObject();o.addProperty("id",template.id);o.addProperty("behavior",template.behavior);o.addProperty("max_support_speed_multiplier",template.maxSpeed);o.addProperty("max_support_durability_multiplier",template.maxDurability);var roles=new JsonArray();for(var role:template.roles){var r=new JsonObject();r.addProperty("id",role.id);r.addProperty("category",role.category);r.addProperty("optional",role.optional);r.addProperty("primary",role.primary);var forms=new JsonArray();role.forms.forEach(forms::add);r.add("forms",forms);roles.add(r);}o.add("parts",roles);return o.toString();}
    public static Template snapshot(String json){var cached=SNAPSHOTS.get(json);if(cached!=null)return cached;var parsed=parseTemplate(JsonParser.parseString(json).getAsJsonObject());SNAPSHOTS.put(json,parsed);return parsed;}
    public static Template parseTemplate(JsonObject o){
        List<Role> roles=new ArrayList<>();for(var entry:o.getAsJsonArray("parts")){var r=entry.getAsJsonObject();List<String> forms=new ArrayList<>();if(r.has("forms"))r.getAsJsonArray("forms").forEach(f->forms.add(f.getAsString()));int count=r.has("count")?r.get("count").getAsInt():1;if(count<1||count>254)throw new IllegalArgumentException("Invalid part count");for(int i=0;i<count;i++)roles.add(new Role(r.get("id").getAsString()+(count==1?"":"_"+(i+1)),r.get("category").getAsString(),List.copyOf(forms),r.has("optional")&&r.get("optional").getAsBoolean(),r.has("primary")&&r.get("primary").getAsBoolean()));}
        if(roles.isEmpty()||roles.size()>254||roles.stream().noneMatch(r->r.category.equals("head")))throw new IllegalArgumentException("Tool needs heads and at most 254 component slots");
        return new Template(o.get("id").getAsString(),string(o,"behavior","pickaxe"),List.copyOf(roles),number(o,"max_support_speed_multiplier",1.15f),number(o,"max_support_durability_multiplier",1.3f));
    }
    public static void registerTemplate(Template template){TEMPLATES.put(template.id,template);}
    public static Part part(ItemStack stack){return PARTS.get(net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()));}
    public static Part part(Identifier item){return PARTS.get(item);}
    public static List<Part> parts(){return List.copyOf(PARTS.values());}
    public static List<Form> headForms(){return FORMS.values().stream().filter(f->f.category.equals("head")).toList();}
    public static List<Template> templates(){return List.copyOf(TEMPLATES.values());}
    public static Template template(String id){return TEMPLATES.get(id);}
    public static Template at(int index){var values=templates();return values.get(Math.floorMod(index,values.size()));}
    public static int index(String id){for(int i=0;i<templates().size();i++)if(templates().get(i).id.equals(id))return i;return 0;}
    public static int slot(int role){return role<3?role:role==3?4:role+2;}
    public static int role(int slot){return slot<3?slot:slot==4?3:slot>=6?slot-2:-1;}
    public static int maxParts(){return templates().stream().mapToInt(t->t.roles.size()).max().orElse(4);}
    public static int inventorySize(){return Math.max(6,slot(maxParts()-1)+1);}
    public static int primary(Template template){for(int i=0;i<template.roles.size();i++)if(template.roles.get(i).category.equals("head")&&template.roles.get(i).primary)return i;for(int i=0;i<template.roles.size();i++)if(template.roles.get(i).category.equals("head"))return i;return 0;}
    public static Part find(String material,String form,boolean cast){return PARTS.values().stream().filter(p->p.material.id.equals(material)&&p.form.id.equals(form)&&p.cast==cast).findFirst().orElse(null);}
}
