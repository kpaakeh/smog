package io.github.kpaakeh.smog.registry;

import io.github.kpaakeh.smog.machine.MachineBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public final class WrenchItem extends Item {
    public WrenchItem(Properties properties){super(properties);}
    @Override public InteractionResult useOn(UseOnContext context){
        var state=context.getLevel().getBlockState(context.getClickedPos());if(!state.hasProperty(MachineBlock.FACING))return InteractionResult.PASS;
        if(!context.getLevel().isClientSide())context.getLevel().setBlock(context.getClickedPos(),state.setValue(MachineBlock.FACING,state.getValue(MachineBlock.FACING).getClockWise()),Block.UPDATE_ALL);
        return InteractionResult.SUCCESS;
    }
}
