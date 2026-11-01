package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.*;

public final class WaterlineTests {
    private static Block block(String name){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name));}
    @GameTest public void externalTankTriesValidSideAfterShorterRejectedApproach(GameTestHelper h){
        var source=h.absolutePos(new BlockPos(3,3,3));var level=h.getLevel();
        level.setBlock(source,block("water_tank").defaultBlockState(),Block.UPDATE_ALL);
        var tank=(MachineEntity)level.getBlockEntity(source);tank.serverTick();tank.copper.processFluid=81000;
        var target=source.east(2).south(2);level.setBlock(target,Blocks.GOLD_BLOCK.defaultBlockState(),Block.UPDATE_ALL);
        var water=net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(net.minecraft.world.level.material.Fluids.WATER);
        var receiver=new net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage<net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant>(){
            @Override protected net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant getBlankVariant(){return net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.blank();}
            @Override protected long getCapacity(net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant resource){return 81000;}
            @Override protected boolean canInsert(net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant resource){return resource.equals(water);}
        };
        int[] rejected={0};
        net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage.SIDED.registerForBlocks((world,pos,state,entity,side)->{
            if(world!=level||!pos.equals(target))return null;
            if(side==Direction.WEST)return receiver;rejected[0]++;return null;
        },Blocks.GOLD_BLOCK);
        // Short branch reaches the north face first; the external tank accepts only from the west.
        for(var pos:new BlockPos[]{source.east(),source.east(2),source.east(2).south()})level.setBlock(pos,block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);
        h.assertValueEqual(WaterSupply.transfer(level,tank,8100),0,"The short route cannot insert through the rejected north face");
        h.assertTrue(rejected[0]>0,"The external sided provider was actually queried on the invalid approach");
        for(var pos:new BlockPos[]{source.west(),source.west().south(),source.west().south(2),source.south(2),source.east().south(2)})level.setBlock(pos,block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);
        h.assertValueEqual(WaterSupply.transfer(level,tank,8100),8100,"A longer route can still insert through the valid west face");
        h.assertValueEqual(receiver.getAmount(),8100L,"The external tank receives the intended amount");
        h.assertValueEqual((long)tank.copper.processFluid+receiver.getAmount(),81000L,"Trying multiple sides cannot duplicate water");h.succeed();
    }
    @GameTest public void rejectedValveSideDoesNotHideLongerAxialRoute(GameTestHelper h){
        var source=h.absolutePos(new BlockPos(3,3,3));var level=h.getLevel();
        level.setBlock(source,block("water_tank").defaultBlockState(),Block.UPDATE_ALL);
        var tank=(MachineEntity)level.getBlockEntity(source);tank.serverTick();tank.copper.processFluid=81000;
        var valvePos=source.east(2).south(2);var target=valvePos.east(2).below();
        Multiblocks.get(ModBlocks.id("washing_trough")).parts().forEach((p,ingredient)->level.setBlock(target.offset(p),ingredient.block().defaultBlockState(),Block.UPDATE_ALL));
        level.setBlock(target,block("washing_trough").defaultBlockState(),Block.UPDATE_ALL);
        var washer=(MachineEntity)level.getBlockEntity(target);h.assertTrue(washer.tryAssemble(),"Receiving trough assembled");
        level.setBlock(valvePos,block("water_valve").defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.EAST).setValue(WorkshopPartBlock.OPEN,true),Block.UPDATE_ALL);
        level.setBlock(valvePos.east(),block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);
        level.setBlock(valvePos.east(2),block("water_outlet").defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.EAST).setValue(WorkshopPartBlock.OPEN,true),Block.UPDATE_ALL);
        // The four-edge branch reaches the valve from the north, which its east/west axis rejects.
        for(var pos:new BlockPos[]{source.east(),source.east(2),source.east(2).south()})level.setBlock(pos,block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);
        h.assertValueEqual(WaterSupply.transfer(level,tank,8100),0,"A perpendicular approach cannot feed the valve");
        // This six-edge branch reaches the west face later in BFS; rejection must not mark the valve visited.
        for(var pos:new BlockPos[]{source.west(),source.west().south(),source.west().south(2),source.south(2),source.east().south(2)})level.setBlock(pos,block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);
        h.assertValueEqual(WaterSupply.transfer(level,tank,8100),8100,"The longer valid axial route still reaches the outlet");
        h.assertValueEqual(washer.copper.processFluid,8100,"Water reaches the intended receiver");
        h.assertValueEqual(tank.copper.processFluid+washer.copper.processFluid,81000,"Both branches conserve the exact water amount");
        h.succeed();
    }
    @GameTest public void closedValveAndOutletStopFiniteWater(GameTestHelper h){
        var source=h.absolutePos(new BlockPos(2,3,2));var level=h.getLevel();level.setBlock(source,block("water_tank").defaultBlockState(),Block.UPDATE_ALL);var tank=(MachineEntity)level.getBlockEntity(source);tank.serverTick();tank.copper.processFluid=81000;
        var target=source.east(4).below();Multiblocks.get(ModBlocks.id("washing_trough")).parts().forEach((p,ingredient)->level.setBlock(target.offset(p),ingredient.block().defaultBlockState(),Block.UPDATE_ALL));level.setBlock(target,block("washing_trough").defaultBlockState(),Block.UPDATE_ALL);var washer=(MachineEntity)level.getBlockEntity(target);h.assertTrue(washer.tryAssemble(),"Receiving trough assembled");
        level.setBlock(source.east(),block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);level.setBlock(source.east(3),block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);
        var valve=block("water_valve").defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.EAST);level.setBlock(source.east(2),valve,Block.UPDATE_ALL);
        var outlet=block("water_outlet").defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.EAST);level.setBlock(source.east(4),outlet,Block.UPDATE_ALL);
        h.assertValueEqual(WaterSupply.transfer(level,tank,8100),0,"Closed valve prevents flow");level.setBlock(source.east(2),valve.setValue(WorkshopPartBlock.OPEN,true),Block.UPDATE_ALL);
        h.assertValueEqual(WaterSupply.transfer(level,tank,8100),0,"Closed outlet prevents flow");level.setBlock(source.east(4),outlet.setValue(WorkshopPartBlock.OPEN,true),Block.UPDATE_ALL);
        h.assertValueEqual(WaterSupply.transfer(level,tank,8100),8100,"Open line feeds receiving storage below outlet");h.assertValueEqual(tank.copper.processFluid+washer.copper.processFluid,81000,"Exact water conservation");
        level.setBlock(source.east(3),Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);h.assertValueEqual(WaterSupply.transfer(level,tank,8100),0,"Broken line stops transfer");h.succeed();
    }
}
