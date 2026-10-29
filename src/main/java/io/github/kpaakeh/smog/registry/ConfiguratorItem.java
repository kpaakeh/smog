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
        if(player==null)return InteractionResult.PASS;
        var clickedState=level.getBlockState(pos);
        if(player.isShiftKeyDown()&&clickedState.getBlock() instanceof io.github.kpaakeh.smog.copper.WorkshopPartBlock){
            if(!level.isClientSide()){level.setBlock(pos,clickedState.setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING,clickedState.getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING).getClockWise()),Block.UPDATE_ALL);player.sendOverlayMessage(Component.translatable("message.smog.configurator_rotated"));}return InteractionResult.SUCCESS;
        }
        MachineEntity machine=level.getBlockEntity(pos) instanceof MachineEntity value?value:level instanceof net.minecraft.server.level.ServerLevel server?io.github.kpaakeh.smog.multiblock.complex.ComplexMultiblocks.owner(server,pos):null;
        if(machine!=null&&io.github.kpaakeh.smog.copper.Smeltery.fuelTank(machine)&&!player.isShiftKeyDown()&&level instanceof net.minecraft.server.level.ServerLevel server){var owner=io.github.kpaakeh.smog.multiblock.complex.ComplexMultiblocks.owner(server,pos);if(owner!=null)machine=owner;else{var candidates=io.github.kpaakeh.smog.multiblock.complex.ComplexMultiblocks.nearbyControllers(server,pos);boolean formed=false;for(var candidate:candidates)if(candidate.tryAssembleAt(pos)){formed=true;break;}player.sendOverlayMessage(formed?Component.translatable("message.smog.multiblock_formed"):candidates.isEmpty()?Component.translatable("message.smog.controller_missing"):candidates.getFirst().assemblyProblem());return InteractionResult.SUCCESS;}}
        if(machine==null){
            var state=level.getBlockState(pos);
            if(!(state.getBlock() instanceof io.github.kpaakeh.smog.copper.WorkshopPartBlock))return InteractionResult.PASS;
            if(player.isShiftKeyDown()&&!level.isClientSide()){level.setBlock(pos,state.setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING,state.getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING).getClockWise()),Block.UPDATE_ALL);player.sendOverlayMessage(Component.translatable("message.smog.configurator_rotated"));}
            if(!player.isShiftKeyDown()&&level instanceof net.minecraft.server.level.ServerLevel server){
                var candidates=io.github.kpaakeh.smog.multiblock.complex.ComplexMultiblocks.nearbyControllers(server,pos);
                boolean formed=false;for(var candidate:candidates)if(candidate.tryAssembleAt(pos)){formed=true;break;}
                player.sendOverlayMessage(formed?Component.translatable("message.smog.multiblock_formed"):candidates.isEmpty()?Component.translatable("message.smog.controller_missing"):candidates.getFirst().assemblyProblem());
            }
            return InteractionResult.SUCCESS;
        }
        pos=machine.getBlockPos();
        if(level.isClientSide())return InteractionResult.SUCCESS;
        if(player.isShiftKeyDown()&&io.github.kpaakeh.smog.copper.LoadingCrane.handles(machine)&&(machine.copper.queued>0||!machine.getItem(1).isEmpty())){player.sendOverlayMessage(Component.translatable("message.smog.crane_busy"));return InteractionResult.SUCCESS;}
        if(player.isShiftKeyDown()) {
            machine.disassemble();
            var state=level.getBlockState(pos);
            level.setBlock(pos,state.setValue(MachineBlock.FACING,state.getValue(MachineBlock.FACING).getClockWise()),Block.UPDATE_ALL);
            player.sendOverlayMessage(Component.translatable("message.smog.configurator_rotated"));
        } else if(machine.multiblockController()) {
            boolean formed=machine.tryAssemble();
            player.sendOverlayMessage(formed?Component.translatable("message.smog.multiblock_formed"):machine.assemblyProblem());
        }
        return InteractionResult.SUCCESS;
    }
}
