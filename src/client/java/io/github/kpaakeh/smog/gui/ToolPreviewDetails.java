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
    public static List<Component> lines(ItemStack preview,ItemStack mounted){
        var lines=new ArrayList<Component>();lines.add(preview.getHoverName());
        if(!ModularToolItem.configured(preview))return lines;
        var schema=ToolParts.schema(preview);var parts=ToolParts.components(preview);
        float headLife=0,headSpeed=0;int heads=0;var purposes=new LinkedHashSet<String>();
        for(var stack:parts){
            var part=ToolDefinitions.part(stack);if(part==null||!part.form().category().equals("head"))continue;
            var profile=part.material().profile(part.form());heads++;headLife+=ToolDefinitions.number(profile,"durability",1);
            String mining=part.form().miningTag();
            if(!mining.isEmpty()){
                headSpeed=Math.max(headSpeed,ToolDefinitions.number(profile,"mining_speed",1));
                String family=mining.substring(mining.lastIndexOf('/')+1);
                if(Set.of("pickaxe","axe","shovel","hoe").contains(family))purposes.add(family);
                String tier=ToolDefinitions.string(profile,"harvest_tier","wooden");if(tier.equals("wood"))tier="wooden";
                lines.add(Component.translatable("tool.smog.preview_head_tier",stack.getHoverName(),Component.translatable("tool.smog.tier."+tier)));
            }
        }
        if(schema.behavior().equals("hammer"))purposes.add("hammer");
        if(schema.behavior().equals("chisel"))purposes.add("chisel");
        for(String purpose:purposes)lines.add(Component.translatable("tool.smog.preview_purpose",Component.translatable("tool.smog.preview_use_"+purpose)));
        if(heads==0)return lines;
        int baseLife=Math.max(1,Math.round(headLife/heads));
        lines.add(Component.translatable("tool.smog.preview_baseline"));
        lines.add(Component.translatable("tool.smog.preview_life",baseLife,preview.getMaxDamage(),change(preview.getMaxDamage(),baseLife)));
        var mining=preview.get(DataComponents.TOOL);
        if(headSpeed>0&&mining!=null){float speed=mining.rules().stream().flatMap(rule->rule.speed().stream()).max(Float::compare).orElse(mining.defaultMiningSpeed());
            lines.add(Component.translatable("tool.smog.preview_speed",decimal(headSpeed),decimal(speed),change(speed,headSpeed)));
        }
        lines.add(Component.translatable("tool.smog.durability",Math.max(0,preview.getMaxDamage()-preview.getDamageValue()),preview.getMaxDamage()));
        lines.add(Component.translatable("tool.smog.preview_conditions"));
        if(ModularToolItem.configured(mounted))lines.add(Component.translatable("tool.smog.rebuild_compare",mounted.getMaxDamage(),preview.getMaxDamage()));
        return lines;
    }
}
