package io.github.kpaakeh.smog.gui;

import io.github.kpaakeh.smog.copper.*;
import java.util.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/** Read-only bench comparison: actual assembled components, before enchantments or situational traits. */
public final class ToolPreviewDetails {
    private ToolPreviewDetails(){}
    private static String decimal(float value){return String.format(Locale.ROOT,"%.2f",value);}
    private static String change(float result,float base){return String.format(Locale.ROOT,"%+.1f%%",100*(result/base-1));}
    public record SpeedComparison(String family,float base,float result){}
    /** Each mining family has its own rules; an axe head must not advertise its speed for stone. */
    public static List<SpeedComparison> speeds(ItemStack preview){
        if(!ModularToolItem.configured(preview))return List.of();
        var tool=preview.get(DataComponents.TOOL);if(tool==null)return List.of();
        var bases=new LinkedHashMap<String,Float>();
        for(var stack:ToolParts.components(preview)){
            var part=ToolDefinitions.part(stack);if(part==null||!part.form().category().equals("head")||part.form().miningTag().isEmpty())continue;
            bases.merge(part.form().miningTag(),ToolDefinitions.number(part.material().profile(part.form()),"mining_speed",1),Math::max);
        }
        var result=new ArrayList<SpeedComparison>();
        bases.forEach((tag,base)->{
            var key=net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.BLOCK,net.minecraft.resources.Identifier.parse(tag));
            float speed=(float)java.util.stream.StreamSupport.stream(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getTagOrEmpty(key).spliterator(),false).mapToDouble(block->tool.getMiningSpeed(block.value().defaultBlockState())).max().orElse(tool.defaultMiningSpeed());
            result.add(new SpeedComparison(tag.substring(tag.lastIndexOf('/')+1),base,speed));
        });return List.copyOf(result);
    }
    public static List<Component> lines(ItemStack preview,ItemStack mounted){
        var lines=new ArrayList<Component>();lines.add(preview.getHoverName());
        if(!ModularToolItem.configured(preview))return lines;
        var schema=ToolParts.schema(preview);var parts=ToolParts.components(preview);
        float headLife=0;int heads=0;var purposes=new LinkedHashSet<String>();
        for(var stack:parts){
            var part=ToolDefinitions.part(stack);if(part==null||!part.form().category().equals("head"))continue;
            var profile=part.material().profile(part.form());heads++;headLife+=ToolDefinitions.number(profile,"durability",1);
            String mining=part.form().miningTag();
            if(!mining.isEmpty()){
                String family=mining.substring(mining.lastIndexOf('/')+1);
                if(Set.of("pickaxe","axe","shovel","hoe").contains(family))purposes.add(family);
                String tier=ToolDefinitions.string(profile,"harvest_tier","wooden");if(tier.equals("wood"))tier="wooden";
                lines.add(Component.translatable("tool.smog.preview_head_tier",stack.getHoverName(),Component.translatable("tool.smog.tier."+tier)));
            }
        }
        if(schema.behavior().equals("hammer"))purposes.add("hammer");
        if(java.util.Set.of("saw","sword","katana","machete","sickle").contains(schema.behavior()))purposes.add(schema.behavior());
        for(String purpose:purposes)lines.add(Component.translatable("tool.smog.preview_purpose",Component.translatable("tool.smog.preview_use_"+purpose)));
        if(heads==0)return lines;
        int baseLife=Math.max(1,Math.round(headLife/heads));
        lines.add(Component.translatable("tool.smog.preview_baseline"));
        lines.add(Component.translatable("tool.smog.preview_life",baseLife,preview.getMaxDamage(),change(preview.getMaxDamage(),baseLife)));
        for(var speed:speeds(preview))lines.add(Component.translatable("tool.smog.preview_speed_family",Component.translatableWithFallback("tool.smog.preview_use_"+speed.family(),speed.family()),decimal(speed.base()),decimal(speed.result()),change(speed.result(),speed.base())));
        lines.add(Component.translatable("tool.smog.durability",Math.max(0,preview.getMaxDamage()-preview.getDamageValue()),preview.getMaxDamage()));
        lines.add(Component.translatable("tool.smog.preview_conditions"));
        if(ModularToolItem.configured(mounted))lines.add(Component.translatable("tool.smog.rebuild_compare",mounted.getMaxDamage(),preview.getMaxDamage()));
        return lines;
    }
}
