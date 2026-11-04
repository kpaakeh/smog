package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class BronzeNetworkTests {
    private static void node(GameTestHelper h,BlockPos pos,String id,Direction face){h.getLevel().setBlockAndUpdate(pos,BronzeContent.entry(id).block().defaultBlockState().setValue(BronzeBlock.FACING,face));}
    private static MachineEntity machine(GameTestHelper h,BlockPos pos,String id){h.getLevel().setBlockAndUpdate(pos,BuiltInRegistries.BLOCK.getValue(ModBlocks.id(id)).defaultBlockState());return (MachineEntity)h.getLevel().getBlockEntity(pos);}
    @GameTest public void crankShaftAndCouplerPerformRealPressWorkAndClutchPausesIt(GameTestHelper h){
        var p=h.absolutePos(new BlockPos(5,3,7));Multiblocks.get(ModBlocks.id("lever_press")).parts().forEach((offset,part)->h.getLevel().setBlockAndUpdate(p.offset(offset),part.block().defaultBlockState()));var press=machine(h,p,"lever_press");h.assertTrue(press.tryAssemble(),"Physical press assembled");press.setItem(0,new ItemStack(Items.WHEAT_SEEDS,8));
        node(h,p.north(),"bronze_drive_coupler",Direction.SOUTH);node(h,p.north(2),"bronze_clutch",Direction.NORTH);node(h,p.north(3),"bronze_shaft",Direction.NORTH);node(h,p.north(4),"bronze_hand_crank",Direction.NORTH);
        var crank=(BronzeEntity)h.getLevel().getBlockEntity(p.north(4));h.assertFalse(BronzeDrive.connected(press),"A stopped crank cannot provide energy");crank.crank();h.assertTrue(BronzeDrive.connected(press),"Reciprocal ports power the real press");
        for(int i=0;i<30;i++)ManualWorkshops.tick(h.getLevel(),press);int before=press.copper.work;
        var clutch=p.north(2);h.getLevel().setBlockAndUpdate(clutch,h.getLevel().getBlockState(clutch).setValue(BronzeBlock.OPEN,false));BronzeNetwork.invalidate(h.getLevel());for(int i=0;i<20;i++)ManualWorkshops.tick(h.getLevel(),press);h.assertValueEqual(press.copper.work,before,"Disconnected power preserves progress");h.assertValueEqual(press.getItem(0).getCount(),8,"Paused work does not consume the batch");
        h.getLevel().setBlockAndUpdate(clutch,h.getLevel().getBlockState(clutch).setValue(BronzeBlock.OPEN,true));BronzeNetwork.invalidate(h.getLevel());for(int i=0;i<100;i++)ManualWorkshops.tick(h.getLevel(),press);h.assertTrue(press.getItem(0).isEmpty(),"Exactly one paid batch is consumed");h.assertValueEqual(press.copper.processFluid,20250,"Real plant oil is produced");h.assertValueEqual(press.getItem(3).getCount(),1,"The real output exists");h.succeed();
    }
    @GameTest public void splitterSharesOneSourceBudgetInsteadOfMultiplyingIt(GameTestHelper h){
        var p=h.absolutePos(new BlockPos(5,3,3));node(h,p,"bronze_hand_crank",Direction.NORTH);node(h,p.south(),"bronze_shaft",Direction.NORTH);var split=p.south(2);node(h,split,"bronze_splitter",Direction.SOUTH);
        for(var side:java.util.List.of(Direction.SOUTH,Direction.EAST,Direction.WEST)){node(h,split.relative(side),"bronze_drive_coupler",side);machine(h,split.relative(side,2),"hand_bellows");}
        ((BronzeEntity)h.getLevel().getBlockEntity(p)).crank();var overloaded=BronzeNetwork.query(h.getLevel(),p);h.assertValueEqual(overloaded.available(),4,"A split does not duplicate the crank's four power");h.assertValueEqual(overloaded.required(),6,"All three loads are counted");h.assertFalse(overloaded.running(),"An overloaded circuit pauses as one network");
        h.getLevel().setBlockAndUpdate(split.west(2),Blocks.AIR.defaultBlockState());BronzeNetwork.invalidate(h.getLevel());h.assertTrue(BronzeNetwork.query(h.getLevel(),p).running(),"Removing one load restores the finite source budget");h.succeed();
    }
    @GameTest public void wrongAxisAndLongUnsupportedShaftCannotTransmit(GameTestHelper h){
        var p=h.absolutePos(new BlockPos(5,3,3));node(h,p,"bronze_hand_crank",Direction.NORTH);((BronzeEntity)h.getLevel().getBlockEntity(p)).crank();node(h,p.south(),"bronze_shaft",Direction.EAST);node(h,p.south(2),"bronze_drive_coupler",Direction.SOUTH);var bellows=machine(h,p.south(3),"hand_bellows");h.assertFalse(BronzeDrive.connected(bellows),"Adjacent faces without matching ports do not transmit");
        for(int i=1;i<=9;i++)node(h,p.south(i),"bronze_shaft",Direction.NORTH);BronzeNetwork.invalidate(h.getLevel());h.assertTrue(BronzeNetwork.query(h.getLevel(),p).status().equals("support"),"Nine unsupported shaft blocks require an actual bearing");h.getLevel().setBlockAndUpdate(p.south(5).below(),Blocks.STONE.defaultBlockState());node(h,p.south(5),"bronze_shaft_support",Direction.NORTH);BronzeNetwork.invalidate(h.getLevel());h.assertFalse(BronzeNetwork.query(h.getLevel(),p).status().equals("support"),"A real shaft support divides the unsupported spans");h.succeed();
    }
}
