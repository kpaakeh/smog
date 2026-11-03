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
    @GameTest public void redstoneClosureHasAnExplicitVisualStateAndKeepsTheManualSetting(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,3,3));for(String name:java.util.List.of("water_valve","water_outlet")){
            h.getLevel().setBlockAndUpdate(pos,block(name).defaultBlockState().setValue(WorkshopPartBlock.OPEN,true));h.getLevel().setBlockAndUpdate(pos.east(),Blocks.REDSTONE_BLOCK.defaultBlockState());var powered=h.getLevel().getBlockState(pos);
            h.assertTrue(powered.getValue(WorkshopPartBlock.POWERED),"Client-visible state follows actual redstone");h.assertTrue(powered.getValue(WorkshopPartBlock.OPEN),"Signal override does not erase the manual setting");h.getLevel().setBlockAndUpdate(pos.east(),Blocks.AIR.defaultBlockState());h.assertFalse(h.getLevel().getBlockState(pos).getValue(WorkshopPartBlock.POWERED),"Removing power restores the open visual state");h.getLevel().setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
        }h.succeed();
    }
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

    @GameTest public void reservoirPreservesFluidIdentityAcrossRollbackReloadAndRefilling(GameTestHelper h){
        var level=h.getLevel();var pos=h.absolutePos(new BlockPos(2,3,2));level.setBlock(pos,block("water_tank").defaultBlockState(),Block.UPDATE_ALL);
        var tank=(MachineEntity)level.getBlockEntity(pos);tank.serverTick();
        var oil=net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(BuiltInRegistries.FLUID.getValue(ModBlocks.id("plant_oil")));
        var water=net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(net.minecraft.world.level.material.Fluids.WATER);
        var customized=oil.withComponents(net.minecraft.core.component.DataComponentPatch.builder().set(net.minecraft.core.component.DataComponents.CUSTOM_NAME,net.minecraft.network.chat.Component.literal("Custom oil")).build());
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(tank.processFluid.insert(customized,81,tx),0L,"A fluid carrying components unsupported by workshop canisters is rejected rather than losing its data");tx.commit();}
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(tank.processFluid.insert(oil,12345,tx),12345L,"An empty reservoir accepts oil");}
        h.assertValueEqual(tank.copper.processFluid,0,"Aborting restores the volume");h.assertTrue(tank.processFluid.resource().equals(water),"Aborting also restores the previous fluid identity");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){tank.processFluid.insert(oil,12345,tx);tx.commit();}
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(tank.processFluid.insert(water,81,tx),0L,"Water cannot mix into oil");h.assertValueEqual(tank.processFluid.insert(net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(net.minecraft.world.level.material.Fluids.LAVA),81,tx),0L,"Copper reservoirs reject lava");tx.commit();}
        var saved=tank.saveCustomOnly(level.registryAccess());tank.loadCustomOnly(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING,level.registryAccess(),saved));
        h.assertValueEqual(tank.copper.processFluid,12345,"Exact sub-bucket amount survives current save");h.assertValueEqual(CopperWorkshops.data(tank,23)|(CopperWorkshops.data(tank,24)<<16),12345,"Menu synchronization preserves fractional mB instead of rounding the tank");h.assertTrue(tank.processFluid.resource().equals(oil),"Oil cannot reload as water");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){tank.processFluid.extract(oil,12345,tx);h.assertValueEqual(tank.processFluid.insert(water,81,tx),81L,"A completely emptied reservoir can receive a different allowed fluid");tx.commit();}
        h.assertTrue(tank.processFluid.resource().equals(water),"New fluid identity follows the actual contents");h.succeed();
    }
    @GameTest public void oilFlowsFromPressThroughValveAndReservoirsEqualizeWithoutOscillation(GameTestHelper h){
        var level=h.getLevel();var source=h.absolutePos(new BlockPos(2,3,2));
        Multiblocks.get(ModBlocks.id("lever_press")).parts().forEach((p,ingredient)->level.setBlock(source.offset(p),ingredient.block().defaultBlockState(),Block.UPDATE_ALL));
        level.setBlock(source,block("lever_press").defaultBlockState(),Block.UPDATE_ALL);var press=(MachineEntity)level.getBlockEntity(source);h.assertTrue(press.tryAssemble(),"Press assembled");press.copper.processFluid=81000;
        var target=source.west(4);level.setBlock(target,block("water_tank").defaultBlockState(),Block.UPDATE_ALL);var tank=(MachineEntity)level.getBlockEntity(target);tank.serverTick();
        level.setBlock(source.west(),block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);level.setBlock(source.west(3),block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);
        var valve=block("water_valve").defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.EAST).setValue(WorkshopPartBlock.OPEN,true);level.setBlock(source.west(2),valve,Block.UPDATE_ALL);
        h.assertValueEqual(WaterSupply.transfer(level,press,8100),8100,"Press can deliver oil through the copper line");h.assertTrue(tank.processFluid.resource().equals(press.processFluid.resource()),"Received oil retains identity");
        level.setBlock(source.west(2).above(),Blocks.REDSTONE_BLOCK.defaultBlockState(),Block.UPDATE_ALL);h.assertValueEqual(WaterSupply.transfer(level,press,8100),0,"Redstone closes a manually open valve");
        level.setBlock(source.west(2).above(),Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);h.assertValueEqual(WaterSupply.transfer(level,press,8100),8100,"Removing redstone restores the manual setting");
        press.enabled(false);h.assertValueEqual(WaterSupply.transfer(level,press,8100),0,"Disabled equipment cannot pump through direct transfer calls");
        var second=target.west(2);level.setBlock(target.west(),block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);level.setBlock(second,block("water_tank").defaultBlockState(),Block.UPDATE_ALL);var receiver=(MachineEntity)level.getBlockEntity(second);receiver.serverTick();
        h.assertValueEqual(WaterSupply.transfer(level,tank,81000),8100,"Reservoirs transfer only half their level difference");
        h.assertValueEqual(WaterSupply.transfer(level,receiver,81000),0,"Equal levels do not reverse into a perpetual oscillation");
        h.assertValueEqual(press.copper.processFluid+tank.copper.processFluid+receiver.copper.processFluid,81000,"The complete oil line conserves every unit");h.succeed();
    }
    @GameTest public void pipeArmsMatchActualValveAndOutletFaces(GameTestHelper h){
        var level=h.getLevel();var pos=h.absolutePos(new BlockPos(2,3,2));var valve=block("water_valve").defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.EAST);
        level.setBlock(pos,valve,Block.UPDATE_ALL);
        h.assertTrue(WaterSupply.connects(level,pos,Direction.WEST),"Pipe connects to valve axis");h.assertFalse(WaterSupply.connects(level,pos,Direction.NORTH),"Pipe has no false perpendicular arm");
        level.setBlock(pos,block("water_outlet").defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.EAST),Block.UPDATE_ALL);
        h.assertTrue(WaterSupply.connects(level,pos,Direction.WEST),"Outlet inlet is behind its facing");h.assertFalse(WaterSupply.connects(level,pos,Direction.EAST),"The nozzle cannot accept from its output front");
        var state=block("copper_water_pipe").defaultBlockState().setValue(WaterPipeBlock.property(Direction.NORTH),true).setValue(WaterPipeBlock.property(Direction.UP),true);
        var turned=state.rotate(Rotation.CLOCKWISE_90);h.assertTrue(turned.getValue(WaterPipeBlock.property(Direction.EAST)),"Native structure rotation rotates pipe arms");h.assertTrue(turned.getValue(WaterPipeBlock.property(Direction.UP)),"Rotation preserves vertical connection");h.succeed();
    }
    @GameTest public void routeLimitCountsActualPipeStepsRatherThanBoundingBox(GameTestHelper h){
        var level=h.getLevel();var source=h.absolutePos(new BlockPos(2,3,2));level.setBlock(source,block("water_tank").defaultBlockState(),Block.UPDATE_ALL);var tank=(MachineEntity)level.getBlockEntity(source);tank.serverTick();tank.copper.processFluid=81000;
        for(int x=1;x<=8;x++)level.setBlock(source.east(x),block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);
        for(int z=1;z<=2;z++)level.setBlock(source.east(8).south(z),block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);
        for(int x=7;x>=2;x--)level.setBlock(source.east(x).south(2),block("copper_water_pipe").defaultBlockState(),Block.UPDATE_ALL);
        var far=source.east().south(2);level.setBlock(far,block("water_tank").defaultBlockState(),Block.UPDATE_ALL);((MachineEntity)level.getBlockEntity(far)).serverTick();
        h.assertValueEqual(WaterSupply.transfer(level,tank,8100),0,"A 17-step folded line cannot bypass the 16-step limit inside a tiny bounding box");
        var near=source.east(2).south(2);level.setBlock(near,block("water_tank").defaultBlockState(),Block.UPDATE_ALL);var receiver=(MachineEntity)level.getBlockEntity(near);receiver.serverTick();
        h.assertValueEqual(WaterSupply.transfer(level,tank,8100),8100,"A receiver at the exact 16-step boundary remains usable");h.assertValueEqual(receiver.copper.processFluid,8100,"Boundary route transfers the exact bounded amount");h.succeed();
    }
}
