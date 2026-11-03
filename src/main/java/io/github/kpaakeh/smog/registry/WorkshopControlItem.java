package io.github.kpaakeh.smog.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/** Controls use ordinary block tooltips; operating instructions live in the UI and JEI. */
public final class WorkshopControlItem extends BlockItem {
    public WorkshopControlItem(Block block,Properties properties){super(block,properties);}
}
