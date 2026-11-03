package io.github.kpaakeh.smog.test;

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

public final class ManualWorkshopStatusTests {
    private static Block block(String name){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name));}
    private static MachineEntity machine(GameTestHelper h,String name){
        var origin=h.absolutePos(new BlockPos(5,3,4));
        Multiblocks.get(ModBlocks.id(name)).parts().forEach((offset,part)->h.getLevel().setBlock(origin.offset(offset),part.block().defaultBlockState(),Block.UPDATE_ALL));
        h.getLevel().setBlock(origin,block(name).defaultBlockState(),Block.UPDATE_ALL);
        var machine=(MachineEntity)h.getLevel().getBlockEntity(origin);h.assertTrue(machine.tryAssemble(),"Workshop fixture assembled");return machine;
    }
    @GameTest public void idleLoadedPressWaitsForStartAndOnlyFinishedProductionIsReady(GameTestHelper h){
        var press=machine(h,"lever_press");press.setItem(0,new ItemStack(Items.WHEAT_SEEDS,8));
        h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.WAITING_START,"Loaded idle press must ask for a start, not claim the result is ready");
        ManualWorkshops.queue(press);h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.WORKING_MANUAL,"The queued press is doing manual work");
        for(int tick=0;tick<ManualWorkshops.workTime(press);tick++)ManualWorkshops.tick(h.getLevel(),press);
        h.assertTrue(CopperWorkshops.is(press.getItem(3),"smog:seed_cake"),"Readiness is backed by a real finished output");
        h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.OUTPUT_READY,"A completed batch requests collection of the output");
        press.setItem(0,new ItemStack(Items.WHEAT_SEEDS,8));h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.WAITING_START,"A partially filled compatible output must not disable starting the next manual batch");press.setItem(0,ItemStack.EMPTY);
        press.removeItemNoUpdate(3);h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.NO_INPUT,"After collection an empty press asks for input");h.succeed();
    }
    @GameTest public void washerWaterAndPressOilCapacityHaveDifferentExactReasons(GameTestHelper h){
        var washer=machine(h,"washing_trough");var recipe=WashingRecipes.find(new ItemStack(Items.RAW_COPPER));washer.setItem(0,recipe.ingredients());washer.copper.processFluid=recipe.water()-1;
        var water=ManualWorkshopStatus.inspect(washer);h.assertValueEqual(water.code(),ManualWorkshopStatus.NEED_WATER,"Washer explicitly needs water");h.assertValueEqual(water.needed(),recipe.water(),"The message uses this recipe's exact water requirement");h.assertValueEqual(water.available(),recipe.water()-1,"Fractional mB cannot be rounded into enough water");
        washer.disassemble();var press=machine(h,"lever_press");press.setItem(0,new ItemStack(Items.WHEAT_SEEDS,8));press.copper.processFluid=WorkshopFluidStorage.CAPACITY-WorkshopFluidStorage.BUCKET/4+1;
        var oil=ManualWorkshopStatus.inspect(press);h.assertValueEqual(oil.code(),ManualWorkshopStatus.NEED_OIL_SPACE,"Oil production asks for free capacity, never for water");h.assertValueEqual(oil.needed(),WorkshopFluidStorage.BUCKET/4,"One real oil batch must fit");h.assertValueEqual(oil.available(),WorkshopFluidStorage.BUCKET/4-1,"The precise missing space is visible");h.succeed();
    }
    @GameTest public void inputOutputAndDisabledReasonsAreSpecificAndInspectionNeverChangesState(GameTestHelper h){
        var press=machine(h,"lever_press");press.setItem(0,new ItemStack(Items.STICK,8));h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.WRONG_INPUT,"Unsupported material is not a generic idle status");
        press.setItem(0,new ItemStack(Items.WHEAT_SEEDS,3));var shortBatch=ManualWorkshopStatus.inspect(press);h.assertValueEqual(shortBatch.code(),ManualWorkshopStatus.INSUFFICIENT_INPUT,"An incomplete batch says what is missing");h.assertValueEqual(shortBatch.needed(),8,"Full batch size");h.assertValueEqual(shortBatch.available(),3,"Actual input count");
        press.setItem(0,new ItemStack(Items.WHEAT_SEEDS,8));press.setItem(3,new ItemStack(Items.STICK,64));h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.OUTPUT_BLOCKED,"A foreign occupied output cannot claim successful work");
        var before=press.saveCustomOnly(h.getLevel().registryAccess());for(int i=0;i<16;i++)ManualWorkshopStatus.inspect(press);h.assertTrue(before.equals(press.saveCustomOnly(h.getLevel().registryAccess())),"State queries do not queue jobs, spend materials, advance work or change persisted status");
        press.enabled(false);h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.DISABLED,"Disabled is distinct from missing materials");press.enabled(true);press.disassemble();h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.UNASSEMBLED,"Broken structure is distinct from disabled");h.succeed();
    }
    @GameTest public void automaticPressExplainsReadyRunningAndLostWaterFlow(GameTestHelper h){
        var press=machine(h,"lever_press");var drive=press.getBlockPos().above();var level=h.getLevel();
        level.setBlock(drive,block("bronze_drive").defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.NORTH),Block.UPDATE_ALL);h.assertTrue(press.tryAssemble(),"Drive fits the existing press frame");
        level.setBlock(drive.north(),Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL,8),Block.UPDATE_ALL);press.setItem(0,new ItemStack(Items.WHEAT_SEEDS,8));
        h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.AUTOMATIC_READY,"Powered equipment starts automatically rather than asking for an unnecessary click");
        ManualWorkshops.tick(level,press);h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.WORKING_AUTO,"Actual automatic queue is distinguished from manual work");
        var before=press.saveCustomOnly(level.registryAccess());level.setBlock(drive.north(),Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);h.assertValueEqual(ManualWorkshopStatus.inspect(press).code(),ManualWorkshopStatus.AUTO_PAUSED,"Stopped moving water has its own reason");h.assertTrue(before.equals(press.saveCustomOnly(level.registryAccess())),"Explaining the pause cannot cancel the queue or consume its input");h.succeed();
    }
}
