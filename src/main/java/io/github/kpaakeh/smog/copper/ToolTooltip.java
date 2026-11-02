package io.github.kpaakeh.smog.copper;

import java.util.*;
import java.util.function.IntSupplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

/** Three readable views of the same live stats: overview, Ctrl properties, Alt construction. */
public final class ToolTooltip {
    public static final int PROPERTIES=1,CONSTRUCTION=2;
    public static final int ACCENT=0x71CFC3,LABEL=0xA5B4BC,VALUE=0xE5E1D8,COMBAT=0xE49D87;
    private static IntSupplier viewerMode=()->0;
    private ToolTooltip(){}
    public static void viewerMode(IntSupplier supplier){viewerMode=Objects.requireNonNull(supplier);}
    public static void append(ItemStack stack,java.util.function.Consumer<Component> output){lines(stack,viewerMode.getAsInt()).forEach(output);}
    private static MutableComponent text(String key,Object...args){return Component.translatable("tool.smog.tip."+key,args);}
    private static MutableComponent color(Component value,int color){return value.copy().withStyle(s->s.withColor(color).withItalic(false));}
    private static Component row(String key,Object value){return color(text(key),LABEL).append(color(Component.literal("  "),LABEL)).append(color(value instanceof Component c?c:Component.literal(String.valueOf(value)),VALUE));}
    private static Component heading(String key){return color(text(key),ACCENT).withStyle(s->s.withBold(true));}
    private static String number(double value){return String.format(Locale.ROOT,"%.2f",value).replaceAll("\\.?0+$","");}
    private static int materialColor(int color){
        int r=(color>>16)&255,g=(color>>8)&255,b=color&255;double brightness=.2126*r+.7152*g+.0722*b;
        if(brightness>=145)return color;
        double lift=(145-brightness)/(255-brightness);
        return ((int)Math.round(r+(255-r)*lift)<<16)|((int)Math.round(g+(255-g)*lift)<<8)|(int)Math.round(b+(255-b)*lift);
    }
    private static Component material(ToolDefinitions.Part part){return color(Component.translatable("tool.smog.material."+part.material().id()),materialColor(part.color()));}
    public static List<Component> lines(ItemStack stack,int mode){
        var lines=new ArrayList<Component>();var direct=ToolDefinitions.part(stack);boolean assembled=ModularToolItem.configured(stack);
        if(direct==null&&!assembled){lines.add(color(Component.translatable("tool.smog.unconfigured"),COMBAT));return List.copyOf(lines);}
        var parts=assembled?ToolParts.components(stack):List.of(stack);
        if(assembled){
            lines.add(row("durability",Math.max(0,stack.getMaxDamage()-stack.getDamageValue())+" / "+stack.getMaxDamage()));
            if(ModularToolItem.broken(stack))lines.add(color(Component.translatable("tool.smog.broken"),COMBAT));
            else lines.add(color(text("use."+ToolParts.schema(stack).behavior()),LABEL));
        }else{
            lines.add(row("material",material(direct)));
            if(direct.cast())lines.add(color(Component.translatable("tool.smog.finish_cast"),COMBAT));
        }
        var traits=new LinkedHashMap<String,ToolTraits.Trait>();for(var trait:ToolTraits.traits(stack))traits.putIfAbsent(trait.id(),trait);
        if(!traits.isEmpty()){
            var summary=color(text("traits"),LABEL).append("  ");int n=0;
            for(var trait:traits.values()){if(n++>0)summary.append(color(Component.literal(" · "),LABEL));summary.append(color(text("trait."+trait.id()),materialColor(trait.source().color())));}
            lines.add(summary);
        }
        if((mode&PROPERTIES)!=0){
            lines.add(Component.empty());lines.add(heading("properties"));
            if(assembled){
                var attributes=stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS,ItemAttributeModifiers.EMPTY);
                if(!ModularToolItem.broken(stack)){
                    lines.add(row("damage",number(attributes.compute(Attributes.ATTACK_DAMAGE,1,EquipmentSlot.MAINHAND))));
                    lines.add(row("attack_speed",number(attributes.compute(Attributes.ATTACK_SPEED,4,EquipmentSlot.MAINHAND))));
                }
                var tool=stack.get(DataComponents.TOOL);
                if(tool!=null&&parts.stream().map(ToolDefinitions::part).filter(Objects::nonNull).anyMatch(p->!p.form().miningTag().isEmpty()))lines.add(row("mining_speed",number(tool.rules().stream().flatMap(r->r.speed().stream()).max(Float::compare).orElse(tool.defaultMiningSpeed()))));
            }else{
                var profile=direct.material().profile(direct.form());
                if(direct.form().category().equals("head")){
                    int maximum=Math.max(1,(int)ToolDefinitions.number(profile,"durability",1));
                    lines.add(row("durability",Math.max(0,maximum-ToolParts.wear(stack).damage(maximum))+" / "+maximum));
                    lines.add(row("mining_speed",number(ToolDefinitions.number(profile,"mining_speed",1))));
                    lines.add(row("head_damage",number(ToolDefinitions.number(profile,"attack_damage",1))));
                }else{
                    lines.add(row("speed_factor",number(ToolDefinitions.number(profile,"speed_multiplier",1))+"×"));
                    lines.add(row("life_factor",number(ToolDefinitions.number(profile,"durability_multiplier",1))+"×"));
                }
            }
            for(var partStack:parts){var part=ToolDefinitions.part(partStack);if(part==null||!part.form().category().equals("head"))continue;
                var profile=part.material().profile(part.form());String tier=ToolDefinitions.string(profile,"harvest_tier","wooden");if(tier.equals("wood"))tier="wooden";
                lines.add(row("tier",Component.translatable("tool.smog.tier."+tier)));
                lines.add(row("repair",material(part)));
            }
            if(!traits.isEmpty()){
                lines.add(Component.empty());lines.add(heading("trait_details"));
                var descriptions=new ArrayList<Component>();ToolTraits.tooltip(stack,descriptions::add);int index=0;
                for(var description:descriptions){var source=ToolTraits.traits(stack).get(Math.min(index++,ToolTraits.traits(stack).size()-1));lines.add(color(description,materialColor(source.source().color())));}
            }
        }
        if((mode&CONSTRUCTION)!=0){
            lines.add(Component.empty());lines.add(heading("construction"));
            if(assembled){
                var schema=ToolParts.schema(stack);
                for(int i=0;i<parts.size();i++){var part=ToolDefinitions.part(parts.get(i));if(part==null)continue;lines.add(row("part."+schema.roles().get(i).category(),material(part)));}
                if(ToolParts.hammerAssembly(stack))lines.add(color(Component.translatable("tool.smog.mount_hammer"),LABEL));
            }else{
                lines.add(row("part_type",text("part."+direct.form().category())));
                if(!direct.material().molten().isEmpty())lines.add(row("content",FluidAmounts.format(ToolParts.units(direct),false)));
            }
        }
        lines.add(Component.empty());
        lines.add(color(text("keys"),ACCENT));
        return List.copyOf(lines);
    }
}
