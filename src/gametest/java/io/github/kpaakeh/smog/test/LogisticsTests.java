package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class LogisticsTests {
    private static Block block(String id){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(id));}
    private static MachineEntity crane(GameTestHelper h){var origin=h.absolutePos(new BlockPos(5,5,4));Multiblocks.get(ModBlocks.id("loading_crane")).parts().forEach((offset,part)->h.getLevel().setBlock(origin.offset(offset),part.block().defaultBlockState(),Block.UPDATE_ALL));h.getLevel().setBlock(origin,block("loading_crane").defaultBlockState(),Block.UPDATE_ALL);var m=(MachineEntity)h.getLevel().getBlockEntity(origin);h.assertTrue(m.tryAssemble(),"Fixed gantry assembles");return m;}
    @GameTest public void cartKeepsFiftyFourSlotsAndCanBeLoaded(GameTestHelper h){
        var cart=CopperTransport.HANDCART.create(h.getLevel(),EntitySpawnReason.COMMAND);cart.setInitialPos(h.absolutePos(new BlockPos(3,2,3)).getX()+.5,h.absolutePos(new BlockPos(3,2,3)).getY(),h.absolutePos(new BlockPos(3,2,3)).getZ()+.5);h.getLevel().addFreshEntity(cart);
        h.assertValueEqual(cart.getContainerSize(),54,"Two chests worth of normal slots");cart.setItem(53,new ItemStack(Items.DIAMOND,17));h.assertValueEqual(cart.getItem(53).getCount(),17,"Last slot is real storage");
        h.assertTrue(LoadingCrane.cargo(h.getLevel(),cart.blockPosition())!=null,"Crane discovers parked cart");var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);h.assertTrue(cart.createMenu(1,player.getInventory(),player)!=null,"Native chest menu available");h.succeed();
    }
    @GameTest public void craneTransfersFiniteBatchesAndPreservesBlockedCargo(GameTestHelper h){
        var m=crane(h);var first=LoadingCrane.bay(m,false);var second=LoadingCrane.bay(m,true);h.getLevel().setBlock(first,Blocks.BARREL.defaultBlockState(),Block.UPDATE_ALL);h.getLevel().setBlock(second,Blocks.BARREL.defaultBlockState(),Block.UPDATE_ALL);
        var source=(net.minecraft.world.Container)h.getLevel().getBlockEntity(first);var target=(net.minecraft.world.Container)h.getLevel().getBlockEntity(second);source.setItem(0,new ItemStack(Items.COBBLESTONE,64));source.setItem(1,new ItemStack(Items.COBBLESTONE,64));
        h.assertValueEqual(LoadingCrane.transfer(h.getLevel(),m),64L,"One bounded batch");h.assertValueEqual(source.getItem(0).getCount()+source.getItem(1).getCount(),64,"Exactly one stack removed");
        for(int i=0;i<target.getContainerSize();i++)target.setItem(i,new ItemStack(Items.DIRT,64));h.assertValueEqual(LoadingCrane.transfer(h.getLevel(),m),0L,"Blocked output pauses without loss");h.assertValueEqual(source.getItem(1).getCount(),64,"Remaining cargo untouched");h.succeed();
    }
    @GameTest public void hearthSupportsSuitableMetalsButNotAluminumOre(GameTestHelper h){
        h.assertTrue(CopperWorkshops.melt(new ItemStack(Items.GOLD_INGOT))!=null,"Existing vanilla gold accepted");h.assertTrue(CopperWorkshops.melt(new ItemStack(BuiltInRegistries.ITEM.getValue(ModBlocks.id("aluminum_ingot"))))!=null,"Already reduced aluminum accepted");
        h.assertTrue(CopperWorkshops.melt(new ItemStack(BuiltInRegistries.ITEM.getValue(ModBlocks.id("raw_bauxite"))))==null,"Heating bauxite cannot produce metal");h.assertTrue(CopperWorkshops.meltingHeat(CopperWorkshops.melt(new ItemStack(Items.IRON_INGOT))[0])>1100,"Registered iron requires heat above this furnace's working range");
        var state=new CopperState();state.metal(3,9000);state.metal(7,18000);h.assertValueEqual(state.total(),27000,"Additional metals share capacity");h.assertValueEqual(state.metal(3),9000,"Independent lead store");h.assertValueEqual(state.metal(7),18000,"Independent gold store");h.succeed();
    }
    @GameTest public void ropewayPausesOnBrokenLinkWithoutLosingLoad(GameTestHelper h){
        var a=h.absolutePos(new BlockPos(2,3,2));var b=h.absolutePos(new BlockPos(6,3,2));for(int x=2;x<=6;x++)h.getLevel().setBlock(h.absolutePos(new BlockPos(x,3,2)),Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);h.getLevel().setBlock(a,block("rope_anchor").defaultBlockState(),Block.UPDATE_ALL);h.getLevel().setBlock(b,block("rope_anchor").defaultBlockState(),Block.UPDATE_ALL);var source=(MachineEntity)h.getLevel().getBlockEntity(a);var target=(MachineEntity)h.getLevel().getBlockEntity(b);
        h.assertTrue(Ropeway.link(h.getLevel(),source,target),"Two anchors form a clear physical span");source.setItem(0,new ItemStack(Items.DIAMOND,11));source.copper.queued=1;Ropeway.tick(h.getLevel(),source);h.assertValueEqual(source.getItem(1).getCount(),11,"Actual cargo in persistent travelling slot");
        Ropeway.unlink(h.getLevel(),source);for(int i=0;i<100;i++)Ropeway.tick(h.getLevel(),source);h.assertValueEqual(source.getItem(1).getCount(),11,"Broken line never deletes cargo");h.assertTrue(target.getItem(3).isEmpty(),"Disconnected endpoint receives nothing");
        h.assertTrue(Ropeway.link(h.getLevel(),source,target),"Can repair the line");for(int i=0;i<100;i++)Ropeway.tick(h.getLevel(),source);h.assertTrue(source.getItem(1).isEmpty(),"Load leaves once");h.assertValueEqual(target.getItem(3).getCount(),11,"All cargo arrives once");h.succeed();
    }
}
