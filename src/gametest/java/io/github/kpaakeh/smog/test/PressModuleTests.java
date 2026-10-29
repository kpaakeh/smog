package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class PressModuleTests {
    private static Block block(String name){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name));}
    private static MachineEntity press(GameTestHelper h,BlockPos origin){Multiblocks.get(ModBlocks.id("lever_press")).parts().forEach((offset,part)->h.getLevel().setBlock(origin.offset(offset),part.block().defaultBlockState(),Block.UPDATE_ALL));h.getLevel().setBlock(origin,block("lever_press").defaultBlockState(),Block.UPDATE_ALL);var m=(MachineEntity)h.getLevel().getBlockEntity(origin);h.assertTrue(m.tryAssemble(),"Initial module configuration assembles");return m;}
    @GameTest public void basketChangesRecipesWithoutChangingStoredOil(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(5,2,4));var m=press(h,origin);m.copper.processFluid=12345;
        h.getLevel().setBlock(origin.offset(0,0,1),block("fiber_press_basket").defaultBlockState(),Block.UPDATE_ALL);h.assertTrue(m.tryAssemble(),"Compatible basket preserves structure");
        h.assertTrue(ManualWorkshops.accepts(m,new ItemStack(Items.WHEAT)),"New basket accepts fibrous plants");h.assertFalse(ManualWorkshops.accepts(m,new ItemStack(Items.WHEAT_SEEDS)),"Oil seeds not silently reused by new process");
        m.setItem(0,new ItemStack(Items.WHEAT,8));ManualWorkshops.queue(m);for(int i=0;i<100;i++)CopperWorkshops.tick(h.getLevel(),m);
        h.assertTrue(CopperWorkshops.is(m.getItem(3),"smog:plant_fiber"),"New module has a real product");h.assertValueEqual(m.getItem(3).getCount(),4,"Declared fiber batch");h.assertValueEqual(m.copper.processFluid,12345,"Module change neither consumes nor replaces oil");h.succeed();
    }
    @GameTest public void hydraulicDriveNeedsFlowAndPausesAutomaticWork(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(5,2,4));var m=press(h,origin);var drive=origin.above();var water=drive.north();
        h.getLevel().setBlock(drive,block("bronze_drive").defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.NORTH),Block.UPDATE_ALL);h.assertTrue(m.tryAssemble(),"Drive replaces frame slot");m.setItem(0,new ItemStack(Items.WHEAT_SEEDS,8));
        h.getLevel().setBlock(water,Blocks.WATER.defaultBlockState(),Block.UPDATE_ALL);h.assertFalse(BronzeDrive.connected(m),"Standing source alone does not turn drive");
        h.getLevel().setBlock(water,Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL,8),Block.UPDATE_ALL);h.assertTrue(BronzeDrive.connected(m),"Falling water powers the mechanical impeller");CopperWorkshops.tick(h.getLevel(),m);h.assertTrue(m.copper.queued==1&&m.copper.autoWork,"Drive starts work without manual queue action");int work=m.copper.work;
        h.getLevel().setBlock(water,Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);CopperWorkshops.tick(h.getLevel(),m);h.assertValueEqual(m.copper.work,work,"Loss of flow pauses automatic work");h.assertValueEqual(m.getItem(0).getCount(),8,"Pause retains input");
        ManualWorkshops.queue(m);for(int i=0;i<100;i++)CopperWorkshops.tick(h.getLevel(),m);h.assertValueEqual(m.copper.processFluid,20250,"Manual fallback still performs exactly one oil batch");h.succeed();
    }
}
