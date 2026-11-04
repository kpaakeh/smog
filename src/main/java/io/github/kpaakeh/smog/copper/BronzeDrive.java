package io.github.kpaakeh.smog.copper;

import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;

/** Direct mechanical water power, no FE or fuel generator. Source water alone cannot turn the impeller. */
public final class BronzeDrive {
    public static boolean working(Level level,BlockPos pos){
        if(!level.hasChunkAt(pos))return false;var state=level.getBlockState(pos);if(!state.is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("bronze_drive"))))return false;
        var waterPos=pos.relative(state.getValue(WorkshopPartBlock.FACING));if(!level.hasChunkAt(waterPos))return false;var water=level.getFluidState(waterPos);
        return water.is(FluidTags.WATER)&&!water.isSource()&&(water.hasProperty(net.minecraft.world.level.material.FlowingFluid.FALLING)&&water.getValue(net.minecraft.world.level.material.FlowingFluid.FALLING)||water.getFlow(level,waterPos).lengthSqr()>.01);
    }
    public static boolean connected(MachineEntity machine){
        return io.github.kpaakeh.smog.bronze.BronzeNetwork.powered(machine);
    }
    public static java.util.List<BlockPos> directSources(MachineEntity machine){
        var result=new java.util.ArrayList<BlockPos>();var level=machine.getLevel();if(level==null)return result;
        if(CopperWorkshops.workshop(machine,"lever_press")){
            var definition=machine.structure();if(definition==null)return result;
            for(var offset:definition.parts().keySet()){var pos=machine.getBlockPos().offset(offset.rotate(machine.rotation()));if(level.hasChunkAt(pos)&&level.getBlockState(pos).is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("bronze_drive"))))result.add(pos);}
        }else if((CopperWorkshops.workshop(machine,"hand_bellows")||LoadingCrane.handles(machine)||Ropeway.handles(machine)))for(var side:Direction.values()){var pos=machine.getBlockPos().relative(side);if(level.hasChunkAt(pos)&&level.getBlockState(pos).is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("bronze_drive"))))result.add(pos);}
        return java.util.List.copyOf(result);
    }
}
