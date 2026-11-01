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
    @GameTest public void returnedOilCanisterRefillsPressTransactionallyAndBottlesExactAmount(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(5,2,4));var level=h.getLevel();var m=press(h,origin);
        int stored=2*(WorkshopFluidStorage.BUCKET/4)+17;m.copper.processFluid=stored;
        CastingProcesses.drop(level,m);
        h.assertValueEqual(m.copper.processFluid,0,"Returning stored oil clears the original receiver");
        var drops=level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class,new net.minecraft.world.phys.AABB(origin).inflate(1),e->CopperWorkshops.is(e.getItem(),"smog:fluid_canister"));
        h.assertValueEqual(drops.size(),1,"The returned oil is contained in exactly one canister");
        var player=h.makeMockServerPlayerInLevel();var hand=net.minecraft.world.InteractionHand.MAIN_HAND;
        player.setItemInHand(hand,drops.getFirst().getItem().copy());drops.getFirst().discard();
        var portable=net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext.ofPlayerHand(player,hand).find(net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage.ITEM);
        h.assertTrue(portable!=null,"Returned canister exposes the standard item fluid storage");
        var oil=m.processFluid.resource();
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
            h.assertValueEqual(net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil.move(portable,m.processFluid,oil::equals,stored,tx),(long)stored,"A returned partial canister can refill the press");
        }
        h.assertValueEqual(m.copper.processFluid,0,"Rolled-back refill leaves the receiver unchanged");
        h.assertValueEqual(portable.iterator().next().getAmount(),(long)stored,"Rolled-back refill preserves every unit in the canister");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
            h.assertValueEqual(net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil.move(portable,m.processFluid,oil::equals,stored,tx),(long)stored,"Committed refill transfers the exact partial amount");tx.commit();
        }
        h.assertValueEqual(m.copper.processFluid,stored,"The press receives all returned oil without rounding");
        h.assertValueEqual(player.getItemInHand(hand).getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getIntOr("smog_fluid_units",0),0,"Committed refill removes the oil from the canister");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
            h.assertValueEqual(m.processFluid.insert(net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(net.minecraft.world.level.material.Fluids.WATER),81000,tx),0L,"The oil receiver still rejects water");tx.commit();
        }
        player.setItemInHand(hand,new ItemStack(Items.GLASS_BOTTLE));
        CopperInteractions.use(level,m,player,hand,origin);
        h.assertValueEqual(m.copper.processFluid,stored-WorkshopFluidStorage.BUCKET/4,"World bottling consumes exactly one quarter bucket, retaining the fractional residue");
        var bottle=BuiltInRegistries.ITEM.getValue(ModBlocks.id("plant_oil_bottle"));
        h.assertValueEqual(player.getInventory().countItem(bottle),1,"The returned oil produces a usable recipe bottle");h.succeed();
    }
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
