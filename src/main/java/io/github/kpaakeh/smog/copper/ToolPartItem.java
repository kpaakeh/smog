package io.github.kpaakeh.smog.copper;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

/** One implementation for every automatically registered material/form pair. Color is applied by the item model. */
public final class ToolPartItem extends Item {
    private final ToolDefinitions.Part definition;
    public ToolPartItem(Properties properties,ToolDefinitions.Part definition){super(properties.component(DataComponents.CUSTOM_MODEL_DATA,new CustomModelData(java.util.List.of(),java.util.List.of(),java.util.List.of(),java.util.List.of(definition.color()))));this.definition=definition;}
    @Override public boolean isBarVisible(ItemStack stack){return definition.form().category().equals("head")&&ToolParts.wear(stack).numerator().signum()>0;}
    @Override public int getBarWidth(ItemStack stack){var wear=ToolParts.wear(stack);return Math.clamp(13-wear.damage(13),0,13);}
    @Override public int getBarColor(ItemStack stack){return getBarWidth(stack)==0?0xAA3333:0x79A943;}
    @Override public void appendHoverText(ItemStack stack,TooltipContext context,TooltipDisplay display,java.util.function.Consumer<Component> lines,TooltipFlag flag){
        super.appendHoverText(stack,context,display,lines,flag);var profile=definition.material().profile(definition.form());
        if(definition.form().category().equals("head")){lines.accept(Component.translatable("tool.smog.profile_head",(int)ToolDefinitions.number(profile,"durability",1),ToolDefinitions.number(profile,"mining_speed",1)));lines.accept(Component.translatable("tool.smog.tier",Component.translatable("tool.smog.tier."+ToolDefinitions.string(profile,"harvest_tier","wooden"))));int maximum=Math.max(1,(int)ToolDefinitions.number(profile,"durability",1));var wear=ToolParts.wear(stack);if(wear.numerator().signum()>0)lines.accept(Component.translatable("tool.smog.head_wear",Math.max(0,maximum-wear.damage(maximum)),maximum));if(definition.cast())lines.accept(Component.translatable("tool.smog.finish_cast"));}
        else lines.accept(Component.translatable("tool.smog.profile_support",ToolDefinitions.number(profile,"speed_multiplier",1),ToolDefinitions.number(profile,"durability_multiplier",1)));
    }
}
