package io.github.kpaakeh.smog.registry;

import io.github.kpaakeh.smog.machine.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

/** The old wrench item ID is retained so existing tools survive the update. */
public final class ConfiguratorItem extends Item {
    public ConfiguratorItem(Properties properties){super(properties);}
    @Override public InteractionResult useOn(UseOnContext context){
        var level=context.getLevel();var pos=context.getClickedPos();var player=context.getPlayer();
        if(!(level.getBlockEntity(pos) instanceof MachineEntity machine) || player==null)return InteractionResult.PASS;
        if(level.isClientSide())return InteractionResult.SUCCESS;
        if(player.isShiftKeyDown()) {
            machine.disassemble();
            var state=level.getBlockState(pos);
            level.setBlock(pos,state.setValue(MachineBlock.FACING,state.getValue(MachineBlock.FACING).getClockWise()),Block.UPDATE_ALL);
            player.sendSystemMessage(Component.translatable("message.smog.configurator_rotated"));
        } else if(machine.multiblockController()) {
            boolean formed=machine.tryAssemble();
            player.sendSystemMessage(Component.translatable(formed?"message.smog.multiblock_formed":"message.smog.multiblock_incomplete"));
        } else {
            var state=level.getBlockState(pos);
            level.setBlock(pos,state.setValue(MachineBlock.FACING,state.getValue(MachineBlock.FACING).getClockWise()),Block.UPDATE_ALL);
        }
        return InteractionResult.SUCCESS;
    }
}
