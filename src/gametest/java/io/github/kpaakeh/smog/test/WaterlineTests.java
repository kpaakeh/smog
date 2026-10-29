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
