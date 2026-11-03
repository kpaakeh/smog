package io.github.kpaakeh.smog.test;
import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.*;
import net.minecraft.world.item.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
public final class UtilityStatusTests {
 private static MachineEntity place(GameTestHelper h,BlockPos p,String name){h.getLevel().setBlock(p,BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name)).defaultBlockState(),Block.UPDATE_ALL);var m=(MachineEntity)h.getLevel().getBlockEntity(p);m.serverTick();return m;}
 @GameTest public void waterStatusExplainsSourceJobAndStoredFluidWithoutPumping(GameTestHelper h){
  var pos=h.absolutePos(new BlockPos(3,4,3));var tank=place(h,pos,"water_tank");h.assertValueEqual(UtilityWorkshopStatus.inspect(tank),UtilityWorkshopStatus.Water.EMPTY,"Empty tank is not claimed to be ready");tank.copper.processFluid=1;h.assertValueEqual(UtilityWorkshopStatus.inspect(tank),UtilityWorkshopStatus.Water.READY,"A sub-mB amount remains a real available fluid");
  var pump=place(h,pos.east(3),"hand_water_pump");h.assertValueEqual(UtilityWorkshopStatus.inspect(pump),UtilityWorkshopStatus.Water.NEED_SOURCE,"Missing source has a distinct reason");h.getLevel().setBlock(pump.getBlockPos().below(),Blocks.WATER.defaultBlockState(),Block.UPDATE_ALL);h.assertValueEqual(UtilityWorkshopStatus.inspect(pump),UtilityWorkshopStatus.Water.NEED_PRIME,"Source water does not create a job by inspection");WaterSupply.prime(h.getLevel(),pump);int budget=pump.copper.pumpBudget;h.assertValueEqual(UtilityWorkshopStatus.inspect(pump),UtilityWorkshopStatus.Water.PUMPING,"A primed pump reports its active finite job");h.assertValueEqual(pump.copper.pumpBudget,budget,"Inspecting never advances pumping");pump.copper.processFluid=WorkshopFluidStorage.CAPACITY;h.assertValueEqual(UtilityWorkshopStatus.inspect(pump),UtilityWorkshopStatus.Water.FULL,"A full pump reports storage backpressure");h.succeed();
 }
 @GameTest public void ropeStatusExplainsLinkAndDispatchWithoutSendingCargo(GameTestHelper h){
  var pos=h.absolutePos(new BlockPos(2,3,2));var a=place(h,pos,"rope_anchor");var b=place(h,pos.east(4),"rope_anchor");h.assertValueEqual(UtilityWorkshopStatus.inspect(a),UtilityWorkshopStatus.Rope.UNLINKED,"An unlinked anchor asks for its link");h.assertTrue(Ropeway.link(h.getLevel(),a,b),"Native link created");h.assertValueEqual(UtilityWorkshopStatus.inspect(a),UtilityWorkshopStatus.Rope.NO_INPUT,"Linked empty anchor asks for cargo");a.setItem(0,new ItemStack(Items.BRICKS,12));h.assertValueEqual(UtilityWorkshopStatus.inspect(a),UtilityWorkshopStatus.Rope.WAITING_DISPATCH,"Loading input is distinct from dispatching it");a.copper.queued=1;h.assertValueEqual(UtilityWorkshopStatus.inspect(a),UtilityWorkshopStatus.Rope.QUEUED,"A queued order is explicit");h.assertValueEqual(a.getItem(0).getCount(),12,"Inspecting cannot reserve or move cargo");a.enabled(false);h.assertValueEqual(UtilityWorkshopStatus.inspect(a),UtilityWorkshopStatus.Rope.DISABLED,"Disabled is not confused with broken link");h.succeed();
 }
}
