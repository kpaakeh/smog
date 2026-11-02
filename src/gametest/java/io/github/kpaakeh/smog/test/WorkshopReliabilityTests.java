package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.gui.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.*;

/** Server-authoritative ordering and persistence; not a substitute for remote-client latency tests. */
public final class WorkshopReliabilityTests {
    private static Block block(String id){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(id));}
    private static MachineEntity machine(GameTestHelper h,BlockPos pos,String id){h.getLevel().setBlock(pos,block(id).defaultBlockState(),Block.UPDATE_ALL);return (MachineEntity)h.getLevel().getBlockEntity(pos);}
    private static ServerPlayer player(GameTestHelper h,BlockPos pos){var p=(ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL);p.connection=h.makeMockServerPlayerInLevel().connection;p.setPos(pos.getX()+.5,pos.getY()+.5,pos.getZ()-1);return p;}
    @GameTest public void smelteryPagesBelongToEachViewerAndStructureChangeInvalidatesBoth(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(6,20,2));
        for(int x=-2;x<=2;x++)for(int y=-1;y<4;y++)for(int z=0;z<5;z++)h.getLevel().setBlock(origin.offset(x,y,z),(y==-1?Blocks.COBBLESTONE:x==-2||x==2||z==0||z==4?Blocks.BRICKS:Blocks.AIR).defaultBlockState(),Block.UPDATE_ALL);
        h.getLevel().setBlock(origin.offset(0,-1,1),block("solid_firebox").defaultBlockState(),Block.UPDATE_ALL);
        var m=machine(h,origin,"copper_hearth");h.assertTrue(m.tryAssemble(),"Large test hearth assembles");h.assertTrue(Smeltery.workers(m)>Smeltery.PAGE_SIZE,"Fixture has more than one actual worker page");
        var first=player(h,origin);var second=player(h,origin);var a=new MachineMenu(GuiLayouts.get("smeltery"),1,first.getInventory(),m,m.data);var b=new MachineMenu(GuiLayouts.get("smeltery"),2,second.getInventory(),m,m.data);
        h.assertTrue(a.clickMenuButton(first,Smeltery.NEXT),"First viewer changes worker page");h.assertValueEqual(a.data.get(23),1,"First viewer is on the second page");h.assertValueEqual(b.data.get(23),0,"Second viewer stays on its original page");
        h.assertTrue(b.clickMenuButton(second,Smeltery.NEXT),"Second viewer changes its own page");h.assertTrue(a.clickMenuButton(first,Smeltery.PREVIOUS),"First viewer independently returns");h.assertValueEqual(b.data.get(23),1,"First viewer cannot move the second viewer's page");
        m.disassemble();h.assertFalse(a.stillValid(first)||b.stillValid(second),"Both open inventories become invalid when the structure is disassembled");h.succeed();
    }
    @GameTest public void twoViewersCannotReceiveTheSameFinishedCastingStack(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,2,3));var table=machine(h,pos,"casting_table");table.setItem(3,new ItemStack(Items.COPPER_INGOT,5));
        var first=player(h,pos);var second=player(h,pos);var a=new MachineMenu(GuiLayouts.get("casting_table"),1,first.getInventory(),table,table.data);var b=new MachineMenu(GuiLayouts.get("casting_table"),2,second.getInventory(),table,table.data);
        a.clicked(2,0,ContainerInput.PICKUP,first);b.clicked(2,0,ContainerInput.PICKUP,second);
        h.assertValueEqual(a.getCarried().getCount()+b.getCarried().getCount(),5,"Two authoritative requests deliver the five real output items only once");h.assertTrue(table.getItem(3).isEmpty(),"No copy is left in persistent output");
        a.removed(first);b.removed(second);h.assertValueEqual(first.getInventory().countItem(Items.COPPER_INGOT)+second.getInventory().countItem(Items.COPPER_INGOT),5,"Closing both menus cannot recreate output or lose the carried stack");h.succeed();
    }
    @GameTest public void cartReleasesAbsentAndDistantPullersWithoutLosingSavedCargo(GameTestHelper h){
        var level=h.getLevel();var pos=h.absolutePos(new BlockPos(3,20,3));var cart=CopperTransport.HANDCART.create(level,EntitySpawnReason.COMMAND);cart.setInitialPos(pos.getX()+.5,pos.getY(),pos.getZ()+.5);cart.setItem(53,new ItemStack(Items.DIAMOND,17));level.addFreshEntity(cart);
        var absent=player(h,pos);absent.setUUID(java.util.UUID.randomUUID());cart.pull(absent);cart.tick();h.assertFalse(cart.pulling(),"A puller absent from this world's player list is released");
        var distant=h.makeMockServerPlayerInLevel();distant.setPos(pos.getX()+20,pos.getY(),pos.getZ());cart.pull(distant);cart.tick();h.assertFalse(cart.pulling(),"Leaving the six-block tether range releases the cart");
        var output=TagValueOutput.createWithContext(ProblemReporter.DISCARDING,level.registryAccess());cart.saveWithoutId(output);var restored=CopperTransport.HANDCART.create(level,EntitySpawnReason.LOAD);restored.load(TagValueInput.create(ProblemReporter.DISCARDING,level.registryAccess(),output.buildResult()));
        h.assertValueEqual(restored.getItem(53).getCount(),17,"Cargo in the last slot survives current-version entity save and reload");h.assertFalse(restored.pulling(),"Reloading cannot retain an absent puller or create a remote tether");h.succeed();
    }
    @GameTest public void unloadedRopeDestinationNeverLoadsChunksOrSpendsReservedCargo(GameTestHelper h){
        var level=h.getLevel();var pos=h.absolutePos(new BlockPos(3,20,3));var anchor=machine(h,pos,"rope_anchor");var far=pos.offset(1600000,0,1600000);
        h.assertTrue(level.getChunkSource().getChunkNow(far.getX()>>4,far.getZ()>>4)==null,"Destination chunk begins unloaded");
        int forced=level.getChunkSource().getForceLoadedChunks().size();anchor.copper.ropeLinked=true;anchor.copper.ropeTarget=far.asLong();anchor.copper.work=19;anchor.setItem(1,new ItemStack(Items.COPPER_INGOT,23));
        var saved=anchor.saveCustomOnly(level.registryAccess());anchor.loadCustomOnly(TagValueInput.create(ProblemReporter.DISCARDING,level.registryAccess(),saved));
        for(int n=0;n<100;n++)Ropeway.tick(level,anchor);
        h.assertValueEqual(anchor.getItem(1).getCount(),23,"Reserved cargo survives reload and unloaded-endpoint pauses");h.assertValueEqual(anchor.copper.work,19,"Progress cannot advance through an unloaded destination");
        h.assertTrue(level.getChunkSource().getChunkNow(far.getX()>>4,far.getZ()>>4)==null,"Repeated checks never generate or load the remote chunk");h.assertValueEqual(level.getChunkSource().getForceLoadedChunks().size(),forced,"No forced-chunk ticket is added");h.succeed();
    }
}
