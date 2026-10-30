package io.github.kpaakeh.smog.registry;

import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.network.chat.Component;

/** Short instructions travel with the actual control block. */
public final class WorkshopControlItem extends BlockItem {
    public WorkshopControlItem(Block block,Properties properties){super(block,properties);}
    @Override public void appendHoverText(ItemStack stack,TooltipContext context,TooltipDisplay display,java.util.function.Consumer<Component> lines,TooltipFlag flag){super.appendHoverText(stack,context,display,lines,flag);String name=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(getBlock()).getPath();for(int n=1;n<=(name.equals("rope_anchor")?4:2);n++)lines.accept(Component.translatable("item.smog."+name+".help_"+n));}
}
