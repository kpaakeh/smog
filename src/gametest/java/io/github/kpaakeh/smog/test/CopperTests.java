package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.copper.CopperWorkshops;
import io.github.kpaakeh.smog.multiblock.complex.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class CopperTests {
    private static final int U=io.github.kpaakeh.smog.copper.MoltenMetalStorage.QUARTER;
    private static Block block(String name){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name));}
    private static Item item(String name){return BuiltInRegistries.ITEM.getValue(ModBlocks.id(name));}
    private static void set(GameTestHelper h,BlockPos pos,Block block){h.getLevel().setBlock(pos,block.defaultBlockState(),Block.UPDATE_ALL);}
    private static MachineEntity hearth(GameTestHelper h,BlockPos origin,int width){return hearth(h,origin,width,3);}
    private static MachineEntity hearth(GameTestHelper h,BlockPos origin,int width,int height){
        int left=width/2;
        for(int x=-left;x<width-left;x++)for(int y=-1;y<height-1;y++)for(int z=0;z<width;z++){
            if(x==0&&y==0&&z==0)continue;
            var b=y==-1?Blocks.COBBLESTONE:x==-left||x==width-left-1||z==0||z==width-1?Blocks.BRICKS:Blocks.AIR;
            set(h,origin.offset(x,y,z),b);
        }
        set(h,origin.offset(0,-1,1),block("solid_firebox"));
        set(h,origin.offset(-left,0,1),block("casting_spout"));set(h,origin.offset(left,0,1),block("air_intake"));
        if(!h.getLevel().getBlockState(origin).is(block("copper_hearth")))set(h,origin,block("copper_hearth"));
        return (MachineEntity)h.getLevel().getBlockEntity(origin);
    }
    @GameTest public void complexHearthResizesWithEmptyChamber(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(6,2,2));var m=hearth(h,origin,3);
        h.assertTrue(m.tryAssemble(),"3x3x3 hollow hearth assembles");h.assertValueEqual(m.complexShape().height(),3,"Actual rim height");h.assertValueEqual(CopperWorkshops.capacity(m),64*U,"Small chamber = 16 ingots");
        m.copper.copper=40*U;m.disassemble();hearth(h,origin,5);
        h.assertTrue(m.tryAssemble(),"5x3x5 hollow hearth assembles");h.assertValueEqual(m.complexShape().width(),5,"Width changed");h.assertValueEqual(CopperWorkshops.capacity(m),576*U,"Capacity follows free chamber volume");h.assertValueEqual(m.copper.copper,40*U,"Stored metal survives resizing");
        m.disassemble();set(h,origin.offset(0,0,1),block("ceramic_crucible"));h.assertFalse(m.tryAssemble(),"Crucible obstructs required empty chamber");h.assertValueEqual(m.copper.copper,40*U,"Invalid structure preserves contents");h.succeed();
    }
    @GameTest public void fullHeightAndCornerRules(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(7,2,2));var m=hearth(h,origin,11,11);
        h.assertTrue(m.tryAssemble(),"Maximum 11x11x11 hearth assembles");h.assertValueEqual(m.complexShape().height(),11,"Tall shell cannot be detected as smaller open prefix");h.assertValueEqual(CopperWorkshops.capacity(m),6480*9000,"Maximum chamber capacity");
        m.disassemble();set(h,origin.offset(-5,0,1),Blocks.BRICKS);set(h,origin.offset(-5,0,0),block("casting_spout"));h.assertFalse(m.tryAssemble(),"Wall corner rejects functional component");
        set(h,origin.offset(-5,0,0),Blocks.BRICKS);set(h,origin.offset(-5,0,1),block("casting_spout"));h.assertTrue(m.tryAssemble(),"Face-mounted component works");h.succeed();
    }
    @GameTest public void wallFireboxAndIndependentOrientation(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(6,2,3));var m=hearth(h,origin,3);
        set(h,origin.offset(0,-1,1),Blocks.COBBLESTONE);set(h,origin.offset(0,0,2),block("solid_firebox"));
        h.getLevel().setBlock(origin,m.getBlockState().setValue(MachineBlock.FACING,Direction.SOUTH),Block.UPDATE_ALL);
        var clicked=origin.offset(-1,0,1);
        h.assertTrue(ComplexMultiblocks.nearbyControllers(h.getLevel(),clicked).contains(m),"Unassembled port finds its controller");
        var player=h.makeMockServerPlayerInLevel();var tool=new ItemStack(item("configurator"));player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,tool);
        var hit=new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(clicked),Direction.WEST,clicked,false);
        player.gameMode.useItemOn(player,h.getLevel(),tool,net.minecraft.world.InteractionHand.MAIN_HAND,hit);
        h.assertTrue(m.assembled(),"Real configurator click on an unassembled port assembles reversed controller and wall firebox");
        h.assertTrue(m.rotation()==Rotation.NONE,"Shell direction follows actual walls");
        h.assertTrue(m.getBlockState().getValue(MachineBlock.FACING)==Direction.SOUTH,"Assembly does not rotate controller");
        h.assertTrue(ComplexMultiblocks.owner(h.getLevel(),clicked)==m,"Ownership uses detected orientation");
        m.disassemble();h.assertFalse(m.tryAssembleAt(origin.offset(4,0,0)),"Unrelated clicked part cannot assemble this hearth");h.succeed();
    }
    private static MachineEntity manual(GameTestHelper h,String name,BlockPos origin){
        var definition=io.github.kpaakeh.smog.multiblock.Multiblocks.get(ModBlocks.id(name));
        definition.parts().forEach((offset,ingredient)->set(h,origin.offset(offset),ingredient.block()));set(h,origin,block(name));
        var machine=(MachineEntity)h.getLevel().getBlockEntity(origin);h.assertTrue(machine.tryAssemble(),"Manual workshop assembles");return machine;
    }
    @GameTest public void washingConsumesOnlyPrimaryOreAndWater(GameTestHelper h){
        var m=manual(h,"washing_trough",h.absolutePos(new BlockPos(5,2,4)));m.setItem(0,new ItemStack(Items.RAW_COPPER,16));
        h.assertFalse(m.canPlaceItem(0,new ItemStack(Items.COPPER_INGOT)),"Finished metal cannot be enriched");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){m.processFluid.insert(net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(net.minecraft.world.level.material.Fluids.WATER),81000,tx);}
        h.assertValueEqual(m.copper.processFluid,0,"Water transfer rollback");
        io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m);for(int i=0;i<120;i++)CopperWorkshops.tick(h.getLevel(),m);
        h.assertValueEqual(m.getItem(0).getCount(),16,"No water means no input consumption");m.copper.processFluid=81000;
        for(int i=0;i<100;i++)CopperWorkshops.tick(h.getLevel(),m);
        h.assertValueEqual(m.getItem(0).getCount(),8,"Exactly eight raw copper per batch");h.assertValueEqual(m.getItem(3).getCount(),5,"Five concentrate per batch");h.assertValueEqual(m.copper.processFluid,0,"One bucket consumed");
        h.assertValueEqual(CopperWorkshops.melt(m.getItem(3))[1],18000,"Concentrate melts to two ingots");h.succeed();
    }
    @GameTest public void pressingQueuesAndPausesWithoutDuplication(GameTestHelper h){
        var m=manual(h,"lever_press",h.absolutePos(new BlockPos(5,2,4)));m.setItem(0,new ItemStack(Items.WHEAT_SEEDS,16));
        h.assertValueEqual(io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m),2,"One action queues loaded batches");
        for(int i=0;i<20;i++){io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m);CopperWorkshops.tick(h.getLevel(),m);}
        h.assertTrue(m.getItem(3).isEmpty(),"Repeated clicks do not finish early");
        for(int i=0;i<180;i++)CopperWorkshops.tick(h.getLevel(),m);
        h.assertTrue(m.getItem(0).isEmpty(),"Two batches consume sixteen seeds");h.assertValueEqual(m.getItem(3).getCount(),2,"Two cakes produced");h.assertValueEqual(m.copper.processFluid,40500,"Two quarter-bucket oil batches");h.assertValueEqual(m.copper.queued,0,"Finite queue ends");
        for(int i=0;i<100;i++)CopperWorkshops.tick(h.getLevel(),m);h.assertValueEqual(m.copper.processFluid,40500,"Idle device cannot duplicate oil");
        h.assertTrue(item("seed_cake").getDefaultInstance().has(net.minecraft.core.component.DataComponents.COMPOSTABLE),"Cake is actually compostable");h.succeed();
    }
    @GameTest public void manualWaterSupplyIsFiniteAndTransactional(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(5,2,5));set(h,pos,block("hand_water_pump"));set(h,pos.below(),Blocks.WATER);
        set(h,pos.east(),block("water_tank"));var pump=(MachineEntity)h.getLevel().getBlockEntity(pos);var tank=(MachineEntity)h.getLevel().getBlockEntity(pos.east());pump.serverTick();tank.serverTick();
        h.assertTrue(io.github.kpaakeh.smog.copper.WaterSupply.prime(h.getLevel(),pump),"Source primes pump");
        h.assertValueEqual(pump.copper.pumpBudget,324000,"One action grants four buckets");
        io.github.kpaakeh.smog.copper.WaterSupply.prime(h.getLevel(),pump);h.assertValueEqual(pump.copper.pumpBudget,324000,"Repeated use does not extend active budget");
        tank.copper.processFluid=81000;
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
            h.assertValueEqual(tank.processFluid.extract(tank.processFluid.resource(),12345,tx),12345L,"Exact finite extraction");
        }
        h.assertValueEqual(tank.copper.processFluid,81000,"Cancelled transfer preserves tank");
        set(h,pos.below(),Blocks.AIR);h.assertFalse(io.github.kpaakeh.smog.copper.WaterSupply.prime(h.getLevel(),pump),"No source cannot prime");h.succeed();
    }
    @GameTest public void moltenTransferRollsBackWithoutRounding(GameTestHelper h){
        var m=hearth(h,h.absolutePos(new BlockPos(6,2,2)),3);h.assertTrue(m.tryAssemble(),"Assembled tank");
        var copper=io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0);
        try(var transaction=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(m.molten.insert(copper,9001,transaction),9001L,"Accept exact fluid units");}
        h.assertValueEqual(m.copper.total(),0,"Uncommitted transfer rolls back");
        try(var transaction=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){m.molten.insert(copper,9001,transaction);transaction.commit();}
        try(var transaction=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(m.molten.extract(copper,1,transaction),1L,"Single-unit extraction is supported");transaction.commit();}
        h.assertValueEqual(m.copper.copper,9000,"No quarter-ingot rounding");h.assertTrue(CopperWorkshops.melt(ItemStack.EMPTY)==null,"Empty input cannot create tin from absent item IDs");h.succeed();
    }
    @GameTest public void complexOwnershipPersistsAndRejectsOverlap(GameTestHelper h){
        var data=new ComplexFormationData();var a=new BlockPos(1,2,3);var b=new BlockPos(4,5,6);var part=new BlockPos(7,8,9);
        h.assertTrue(data.claim(a,java.util.List.of(a,part)),"Initial exclusive claim");h.assertFalse(data.claim(b,java.util.List.of(b,part)),"Shared functional block rejected");
        var encoded=ComplexFormationData.TYPE.codec().encodeStart(com.mojang.serialization.JsonOps.INSTANCE,data).getOrThrow();var restored=ComplexFormationData.TYPE.codec().parse(com.mojang.serialization.JsonOps.INSTANCE,encoded).getOrThrow();
        h.assertTrue(restored.owner(part).equals(a),"Ownership survives save/load");restored.release(a);h.assertTrue(restored.claim(b,java.util.List.of(b,part)),"Disassembly releases ownership");h.succeed();
    }
    @GameTest public void bronzeAndCastingConserveMetalAndKeepMolds(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(6,2,3));var m=hearth(h,origin,3);h.assertTrue(m.tryAssemble(),"Working hearth");m.copper.heat=1000;m.copper.copper=12*U;m.copper.tin=4*U;
        CopperWorkshops.tick(h.getLevel(),m);h.assertValueEqual(m.copper.bronze,16*U,"3 copper + 1 tin = 4 bronze");h.assertValueEqual(m.copper.total(),16*U,"No alloy multiplication");
        var tablePos=origin.offset(-2,-1,1);set(h,tablePos,block("casting_table"));var table=(MachineEntity)h.getLevel().getBlockEntity(tablePos);table.copper.selection=2;m.copper.lastPour=h.getLevel().getGameTime()-10;table.setItem(1,new ItemStack(item("ingot_mold")));
        for(int i=0;i<110;i++)CopperWorkshops.tick(h.getLevel(),table);
        h.assertTrue(table.getItem(3).isEmpty(),"Bare wall port does not pour");
        var faucet=tablePos.above();var closed=block("casting_faucet").defaultBlockState().setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING,Direction.WEST);
        h.getLevel().setBlock(faucet,closed,Block.UPDATE_ALL);
        for(int i=0;i<110;i++)CopperWorkshops.tick(h.getLevel(),table);
        h.assertTrue(table.getItem(3).isEmpty(),"Closed faucet does not pour");
        h.getLevel().setBlock(faucet,closed.setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.OPEN,true),Block.UPDATE_ALL);
        for(int i=0;i<110;i++)CopperWorkshops.tick(h.getLevel(),table);
        h.assertTrue(table.getItem(3).is(item("bronze_ingot")),"Connected table casts bronze");h.assertTrue(table.getItem(1).is(item("ingot_mold")),"Mold is reusable");h.assertValueEqual(m.copper.total()+table.getItem(3).getCount()*4*U,16*U,"Casting conserves quarter-ingot units");h.succeed();
    }
    @GameTest public void airPortNeedsOpenNozzle(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(6,2,2));var m=hearth(h,origin,3);h.assertTrue(m.tryAssemble(),"Formed hearth");
        var bellowsPos=origin.offset(3,0,1);set(h,bellowsPos,block("hand_bellows"));var bellows=(MachineEntity)h.getLevel().getBlockEntity(bellowsPos);
        h.assertFalse(CopperWorkshops.pump(h.getLevel(),bellows),"Port without a nozzle has no air connection");
        var pos=origin.offset(2,0,1);var nozzle=block("air_nozzle").defaultBlockState().setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING,Direction.EAST);
        h.getLevel().setBlock(pos,nozzle,Block.UPDATE_ALL);h.assertFalse(CopperWorkshops.pump(h.getLevel(),bellows),"Closed nozzle blocks air");
        h.getLevel().setBlock(pos,nozzle.setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.OPEN,true),Block.UPDATE_ALL);h.assertTrue(CopperWorkshops.pump(h.getLevel(),bellows),"Open nozzle supplies air through wall port");h.assertValueEqual(m.copper.air,8,"Air budget preserved");h.succeed();
    }
    @GameTest public void loosePartsRotateOnlyWithShift(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,1,3));set(h,pos,block("casting_spout"));var player=h.makeMockServerPlayerInLevel();var tool=new ItemStack(item("configurator"));player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,tool);
        var hit=new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(pos),Direction.UP,pos,false);
        player.gameMode.useItemOn(player,h.getLevel(),tool,net.minecraft.world.InteractionHand.MAIN_HAND,hit);h.assertTrue(h.getLevel().getBlockState(pos).getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING)==Direction.NORTH,"Normal click never turns part");
        player.setShiftKeyDown(true);player.gameMode.useItemOn(player,h.getLevel(),tool,net.minecraft.world.InteractionHand.MAIN_HAND,hit);h.assertTrue(h.getLevel().getBlockState(pos).getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING)==Direction.EAST,"Sneak item path rotates exactly once");h.succeed();
    }

}
