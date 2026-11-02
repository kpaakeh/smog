package io.github.kpaakeh.smog.copper;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

/** One implementation for every automatically registered material/form pair. Color is applied by the item model. */
public final class ToolPartItem extends Item {
    private final ToolDefinitions.Part definition;
    public ToolPartItem(Properties properties,ToolDefinitions.Part definition){super(properties.component(DataComponents.TOOLTIP_STYLE,io.github.kpaakeh.smog.registry.ModBlocks.id("workshop")).component(DataComponents.CUSTOM_MODEL_DATA,new CustomModelData(java.util.List.of(),java.util.List.of(),java.util.List.of(),java.util.List.of(definition.color()))));this.definition=definition;}
    @Override public boolean isBarVisible(ItemStack stack){return definition.form().category().equals("head")&&ToolParts.wear(stack).numerator().signum()>0;}
    @Override public int getBarWidth(ItemStack stack){var wear=ToolParts.wear(stack);return Math.clamp(13-wear.damage(13),0,13);}
    @Override public int getBarColor(ItemStack stack){return getBarWidth(stack)==0?0xAA3333:0x79A943;}
    @Override public void appendHoverText(ItemStack stack,TooltipContext context,TooltipDisplay display,java.util.function.Consumer<Component> lines,TooltipFlag flag){
        super.appendHoverText(stack,context,display,lines,flag);ToolTooltip.append(stack,lines);
    }
}
