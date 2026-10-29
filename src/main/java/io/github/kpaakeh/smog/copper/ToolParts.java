package io.github.kpaakeh.smog.copper;

import java.util.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

/** Composition depends on JSON roles and material profiles, never on a manually enumerated alloy or part count. */
public final class ToolParts {
    public static Item item(String name){return BuiltInRegistries.ITEM.getValue(name.contains(":")?Identifier.parse(name):ModBlocks.id(name));}
    public static String name(ItemStack stack){return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();}
    public static boolean category(ItemStack stack,String category){var p=ToolDefinitions.part(stack);return p!=null&&p.form().category().equals(category);}
    public static boolean head(ItemStack stack){return category(stack,"head");}
    public static boolean cast(ItemStack stack){var p=ToolDefinitions.part(stack);return p!=null&&p.cast();}
    public static boolean handle(ItemStack stack){return category(stack,"handle");}
    public static boolean binding(ItemStack stack){return category(stack,"binding");}
    public static boolean wrap(ItemStack stack){return category(stack,"wrap");}
    public static String shape(ItemStack head){var p=ToolDefinitions.part(head);return p==null?"":p.form().id().replaceFirst("_head$","");}
    public static String metal(ItemStack head){var p=ToolDefinitions.part(head);return p==null?"":p.material().id();}
    public static HeadWear wear(ItemStack head){return HeadWear.read(ModularToolItem.parts(head));}
    public static ToolDefinitions.Template schema(ItemStack tool){var data=ModularToolItem.parts(tool);String snapshot=data.getStringOr("template_snapshot","");if(!snapshot.isEmpty())try{return ToolDefinitions.snapshot(snapshot);}catch(RuntimeException malformed){return null;}return ToolDefinitions.template(data.getStringOr("template",BuiltInRegistries.ITEM.getKey(tool.getItem()).getPath().replace("modular_","")));}
    public static boolean hammer(ItemStack stack){return CopperWorkshops.is(stack,"smog:stone_work_hammer")||ModularToolItem.configured(stack)&&!ModularToolItem.broken(stack)&&components(stack).stream().anyMatch(p->shape(p).equals("hammer"));}
    public static List<ItemStack> components(ItemStack tool){
        var schema=schema(tool);if(schema==null)return List.of();var stored=tool.getOrDefault(DataComponents.CONTAINER,ItemContainerContents.EMPTY);var parts=NonNullList.withSize(schema.roles().size(),ItemStack.EMPTY);stored.copyInto(parts);
        int delta=tool.getDamageValue()-ModularToolItem.parts(tool).getIntOr("assembled_damage",0),maximum=Math.max(1,tool.getMaxDamage());
        for(int i=0;i<parts.size();i++)if(head(parts.get(i))){var part=parts.get(i).copy();var data=ModularToolItem.parts(part);wear(part).add(delta,maximum).write(data);part.set(DataComponents.CUSTOM_DATA,CustomData.of(data));
            if(i==ToolDefinitions.primary(schema)){if(tool.has(DataComponents.ENCHANTMENTS))part.set(DataComponents.ENCHANTMENTS,tool.get(DataComponents.ENCHANTMENTS));else part.remove(DataComponents.ENCHANTMENTS);}parts.set(i,part);
        }return List.copyOf(parts);
    }
    public static ItemStack extract(ItemStack tool,int slot){int role=ToolDefinitions.role(slot);var parts=components(tool);return role>=0&&role<parts.size()?parts.get(role).copy():ItemStack.EMPTY;}
    public static int[] melt(ItemStack stack){
        // Take assemblies apart first: dissimilar heads and non-metal parts must not become one guessed metal.
        var part=ToolDefinitions.part(stack);if(part==null)return null;
        int kind=java.util.Arrays.asList(MoltenMetalStorage.NAMES).indexOf(part.material().molten());int units=wear(stack).recover(units(part));return kind>=0&&units>0?new int[]{kind,units}:null;
    }
    public static String problem(ToolDefinitions.Template schema,List<ItemStack> parts){
        if(parts.size()!=schema.roles().size())return "count";
        for(int i=0;i<parts.size();i++){var role=schema.roles().get(i);var stack=parts.get(i);if(stack.isEmpty()&&role.optional())continue;var part=ToolDefinitions.part(stack);if(part==null||part.cast()||!role.accepts(part))return role.id();}return "";
    }
    public static String problem(ItemStack head,ItemStack handle,ItemStack binding,ItemStack wrap){var schema=singleHeadSchema(head);return schema==null?"head":problem(schema,List.of(head,handle,binding,wrap));}
    private static ToolDefinitions.Template singleHeadSchema(ItemStack head){var definition=ToolDefinitions.part(head);if(definition==null)return null;return ToolDefinitions.templates().stream().filter(t->t.roles().size()==4&&t.roles().getFirst().accepts(definition)).findFirst().orElse(null);}
    public static ItemStack assemble(ItemStack head,ItemStack handle,ItemStack binding,ItemStack wrap){var schema=singleHeadSchema(head);return schema==null?ItemStack.EMPTY:assemble(schema,List.of(head,handle,binding,wrap));}
    public static ItemStack assemble(ToolDefinitions.Template schema,List<ItemStack> components){
        if(!problem(schema,components).isEmpty())return ItemStack.EMPTY;
        List<ItemStack> parts=components.stream().map(s->s.isEmpty()?ItemStack.EMPTY:s.copyWithCount(1)).toList();List<ToolDefinitions.Part> heads=new ArrayList<>();float supportSpeed=1,supportLife=1,life=0,attack=0;HeadWear worst=new HeadWear(java.math.BigInteger.ZERO,java.math.BigInteger.ONE);
        for(var stack:parts){var p=ToolDefinitions.part(stack);if(p==null)continue;var profile=p.material().profile(p.form());if(p.form().category().equals("head")){
            heads.add(p);life+=ToolDefinitions.number(profile,"durability",1);attack+=ToolDefinitions.number(profile,"attack_damage",1);var wear=wear(stack);if(wear.numerator().multiply(worst.denominator()).compareTo(worst.numerator().multiply(wear.denominator()))>0)worst=wear;
        }else{supportSpeed*=ToolDefinitions.number(profile,"speed_multiplier",1);supportLife*=ToolDefinitions.number(profile,"durability_multiplier",1);}}
        if(heads.isEmpty())return ItemStack.EMPTY;supportSpeed=Math.min(schema.maxSpeed(),supportSpeed);supportLife=Math.min(schema.maxDurability(),supportLife);
        int maximum=Math.max(1,Math.round(life/heads.size()*supportLife));var result=new ItemStack(item("modular_"+schema.id()));if(result.isEmpty())return ItemStack.EMPTY;
        result.set(DataComponents.MAX_DAMAGE,maximum);result.setDamageValue(worst.damage(maximum));
        List<Tool.Rule> rules=new ArrayList<>(),wrongTierSpeeds=new ArrayList<>();final float speedMultiplier=supportSpeed;
        heads.sort(Comparator.comparingDouble((ToolDefinitions.Part p)->ToolDefinitions.number(p.material().profile(p.form()),"mining_speed",1)).reversed());
        for(var head:heads){if(head.form().miningTag().isEmpty())continue;var mining=TagKey.create(Registries.BLOCK,Identifier.parse(head.form().miningTag()));var profile=head.material().profile(head.form());String tier=ToolDefinitions.string(profile,"harvest_tier","wooden");if(tier.equals("wood"))tier="wooden";var incorrect=TagKey.create(Registries.BLOCK,Identifier.parse(ToolDefinitions.string(profile,"incorrect_for_drops","minecraft:incorrect_for_"+tier+"_tool")));float speed=ToolDefinitions.number(profile,"mining_speed",1)*speedMultiplier;
            var all=java.util.stream.StreamSupport.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(mining).spliterator(),false).toList();var correct=all.stream().filter(h->!h.is(incorrect)).toList();
            if(!correct.isEmpty())rules.add(Tool.Rule.minesAndDrops(HolderSet.direct(correct),speed));if(!all.isEmpty())wrongTierSpeeds.add(Tool.Rule.overrideSpeed(HolderSet.direct(all),speed));
        }
        rules.addAll(wrongTierSpeeds);result.set(DataComponents.TOOL,new Tool(List.copyOf(rules),1,1,false));
        var attributes=ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE,new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,attack/heads.size(),AttributeModifier.Operation.ADD_VALUE),EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED,new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,-2.8,AttributeModifier.Operation.ADD_VALUE),EquipmentSlotGroup.MAINHAND).build();result.set(DataComponents.ATTRIBUTE_MODIFIERS,attributes);
        result.set(DataComponents.ENCHANTABLE,new net.minecraft.world.item.enchantment.Enchantable(14));var primary=parts.get(ToolDefinitions.primary(schema));if(primary.has(DataComponents.ENCHANTMENTS))result.set(DataComponents.ENCHANTMENTS,primary.get(DataComponents.ENCHANTMENTS));
        var data=new CompoundTag();data.putString("template",schema.id());data.putString("template_snapshot",ToolDefinitions.snapshot(schema));data.putInt("assembled_damage",result.getDamageValue());result.set(DataComponents.CUSTOM_DATA,CustomData.of(data));result.set(DataComponents.CONTAINER,ItemContainerContents.fromItems(parts));
        List<String> forms=new ArrayList<>();List<Integer> colors=new ArrayList<>();for(var part:parts){var definition=ToolDefinitions.part(part);forms.add(definition==null?"":definition.form().id());colors.add(definition==null?0xFFFFFF:definition.color());}result.set(DataComponents.CUSTOM_MODEL_DATA,new CustomModelData(List.of(),List.of(),forms,colors));
        if(result.getDamageValue()>=maximum)result.remove(DataComponents.ATTRIBUTE_MODIFIERS);return result;
    }
    public static ItemStack example(ToolDefinitions.Template schema,String material){
        List<ItemStack> parts=new ArrayList<>();for(var role:schema.roles()){
            if(role.optional()){parts.add(ItemStack.EMPTY);continue;}
            String preferred=role.category().equals("head")?material:role.category().equals("handle")?"wood":"cord";
            var options=ToolDefinitions.parts().stream().filter(p->!p.cast()&&role.accepts(p)).toList();var match=options.stream().filter(p->p.material().id().equals(preferred)).findFirst().orElse(options.isEmpty()?null:options.getFirst());if(match==null)return ItemStack.EMPTY;parts.add(new ItemStack(item(match.item().toString())));
        }return assemble(schema,parts);
    }
    public static ToolDefinitions.Part nativeSource(ItemStack stack){
        String source=name(stack);return ToolDefinitions.parts().stream().filter(p->!p.cast()&&p.form().category().equals("head")&&!p.material().nativePrefix().isEmpty()&&!p.form().nativeSuffix().isEmpty()&&source.equals("minecraft:"+p.material().nativePrefix()+"_"+p.form().nativeSuffix())).findFirst().orElse(null);
    }
    public static ItemStack finish(ItemStack input){var definition=ToolDefinitions.part(input);if(definition!=null&&definition.cast()){var target=ToolDefinitions.find(definition.material().id(),definition.form().id(),false);return new ItemStack(item(target.item().toString()));}
        var source=nativeSource(input);if(source==null)return ItemStack.EMPTY;var result=new ItemStack(item(source.item().toString()));var data=new CompoundTag();new HeadWear(java.math.BigInteger.valueOf(input.getDamageValue()),java.math.BigInteger.valueOf(Math.max(1,input.getMaxDamage()))).write(data);result.set(DataComponents.CUSTOM_DATA,CustomData.of(data));if(input.has(DataComponents.ENCHANTMENTS))result.set(DataComponents.ENCHANTMENTS,input.get(DataComponents.ENCHANTMENTS));return result;
    }
    public static int units(ToolDefinitions.Part part){return Math.max(1,(int)ToolDefinitions.number(part.material().profile(part.form()),"material_units",part.form().metalUnits()));}
    public record Shaping(ItemStack result,int count){}
    public static List<ToolDefinitions.Part> shapingChoices(ItemStack ingredient){return ToolDefinitions.parts().stream().filter(p->{var profile=p.material().profile(p.form());return !p.cast()&&materialIngredient(ingredient,p.material())&&(profile.has("shaping")?profile.get("shaping").getAsBoolean():!p.form().category().equals("head"));}).toList();}
    public static Shaping shaping(ItemStack ingredient,ToolDefinitions.Part candidate){
        var profile=candidate.material().profile(candidate.form());int units=units(candidate),extra=Math.max(0,(int)ToolDefinitions.number(profile,"shaping_extra_units",0));int count=(units+extra+8999)/9000;int yield=units<=9000&&extra==0&&9000%units==0?Math.min(64,9000/units):1;
        return new Shaping(new ItemStack(item(candidate.item().toString()),yield),Math.max(1,count));
    }
    public static boolean materialIngredient(ItemStack ingredient,ToolDefinitions.Material material){String value=material.repairItem();if(value.isEmpty()||ingredient.isEmpty())return false;return value.startsWith("#")?ingredient.is(TagKey.create(Registries.ITEM,Identifier.parse(value.substring(1)))):name(ingredient).equals(value);}
    public static List<Item> repairIngredients(){var values=new LinkedHashSet<Item>();for(String value:ToolDefinitions.parts().stream().map(p->p.material().repairItem()).filter(s->!s.isEmpty()).distinct().toList()){
        if(value.startsWith("#"))BuiltInRegistries.ITEM.getTagOrEmpty(TagKey.create(Registries.ITEM,Identifier.parse(value.substring(1)))).forEach(holder->values.add(holder.value()));else{var id=Identifier.parse(value);if(BuiltInRegistries.ITEM.containsKey(id))values.add(BuiltInRegistries.ITEM.getValue(id));}
    }return List.copyOf(values);}
    public static Shaping shaping(ItemStack ingredient,ToolDefinitions.Template schema){
        var role=schema.roles().get(ToolDefinitions.primary(schema));for(var candidate:shapingChoices(ingredient))if(role.accepts(candidate)){var plan=shaping(ingredient,candidate);if(ingredient.getCount()>=plan.count())return plan;}return null;
    }
    public static ItemStack repair(ItemStack tool,ItemStack ingredient){
        var schema=schema(tool);if(schema==null)return ItemStack.EMPTY;var parts=new ArrayList<>(components(tool));
        for(int i=0;i<parts.size();i++){var p=ToolDefinitions.part(parts.get(i));if(p==null||!p.form().category().equals("head")||!materialIngredient(ingredient,p.material())||wear(parts.get(i)).numerator().signum()==0)continue;
            var repaired=parts.get(i).copy();var data=ModularToolItem.parts(repaired);wear(repaired).add(-9000,Math.max(36000,units(p))).write(data);repaired.set(DataComponents.CUSTOM_DATA,CustomData.of(data));parts.set(i,repaired);return assemble(schema,parts);
        }return ItemStack.EMPTY;
    }
}
