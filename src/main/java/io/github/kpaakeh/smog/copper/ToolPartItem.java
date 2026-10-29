package io.github.kpaakeh.smog.copper;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

/** One implementation for every automatically registered material/form pair. Color is applied by the item model. */
public final class ToolPartItem extends Item {
    private final ToolDefinitions.Part definition;
    public ToolPartItem(Properties properties,ToolDefinitions.Part definition){super(properties.component(DataComponents.CUSTOM_MODEL_DATA,new CustomModelData(java.util.List.of(),java.util.List.of(),java.util.List.of(),java.util.List.of(definition.color()))));this.definition=definition;}
    @Override public void appendHoverText(ItemStack stack,TooltipContext context,TooltipDisplay display,java.util.function.Consumer<Component> lines,TooltipFlag flag){
        super.appendHoverText(stack,context,display,lines,flag);var profile=definition.material().profile(definition.form());
        if(definition.form().category().equals("head")){lines.accept(Component.translatable("tool.smog.profile_head",(int)ToolDefinitions.number(profile,"durability",1),ToolDefinitions.number(profile,"mining_speed",1)));lines.accept(Component.translatable("tool.smog.tier",Component.translatable("tool.smog.tier."+ToolDefinitions.string(profile,"harvest_tier","wooden"))));if(definition.cast())lines.accept(Component.translatable("tool.smog.finish_cast"));}
        else lines.accept(Component.translatable("tool.smog.profile_support",ToolDefinitions.number(profile,"speed_multiplier",1),ToolDefinitions.number(profile,"durability_multiplier",1)));
    }
}
