package io.github.kpaakeh.smog.test;
import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
/** Real finite transfers with open, closed, powered and full receiver states, for both supported fluids. */
public final class PlumbingVisualClientScene {
 public static void run(ClientGameTestContext context){
  try(var world=context.worldBuilder().create()){
   var server=world.getServer();server.runCommand("fill -5 99 -5 10 99 15 minecraft:stone");server.runCommand("time set noon");
   for(boolean oil:new boolean[]{false,true}){
    int z=oil?8:0;var source=new BlockPos(0,100,z);var target=source.east(4);var valve=source.east(2);String label=oil?"oil":"water";
    server.runOnServer(a->{var l=a.overworld();for(var p:new BlockPos[]{source,target}){l.setBlockAndUpdate(p,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("water_tank")).defaultBlockState());((MachineEntity)l.getBlockEntity(p)).serverTick();}var from=(MachineEntity)l.getBlockEntity(source);var to=(MachineEntity)l.getBlockEntity(target);to.enabled(false);
     var fluid=net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(oil?BuiltInRegistries.FLUID.getValue(ModBlocks.id("plant_oil")):net.minecraft.world.level.material.Fluids.WATER);try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){if(from.processFluid.insert(fluid,648000,tx)!=648000)throw new IllegalStateException("Source fill rejected");tx.commit();}
     for(int dx:new int[]{1,3})l.setBlockAndUpdate(source.east(dx),BuiltInRegistries.BLOCK.getValue(ModBlocks.id("copper_water_pipe")).defaultBlockState());l.setBlockAndUpdate(valve,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("water_valve")).defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.EAST).setValue(WorkshopPartBlock.OPEN,false));from.sync();to.sync();
    });
    server.runCommand("tp @a 2.5 101 "+(z-4)+" 0 20");context.waitTicks(10);context.getInput().lookAt(valve);context.waitTicks(5);context.takeScreenshot("plumbing-"+label+"-closed");
    server.runOnServer(a->{var l=a.overworld();l.setBlockAndUpdate(valve,l.getBlockState(valve).setValue(WorkshopPartBlock.OPEN,true));var from=(MachineEntity)l.getBlockEntity(source);if(WaterSupply.transfer(l,from,8100)!=8100)throw new IllegalStateException("Open pipe did not transfer "+label);from.sync();((MachineEntity)l.getBlockEntity(target)).sync();});context.waitTicks(8);context.takeScreenshot("plumbing-"+label+"-open-transferring");
    server.runOnServer(a->{var l=a.overworld();l.setBlockAndUpdate(valve.north(),Blocks.REDSTONE_BLOCK.defaultBlockState());var from=(MachineEntity)l.getBlockEntity(source);if(WaterSupply.transfer(l,from,8100)!=0)throw new IllegalStateException("Powered valve leaked "+label);});context.waitTicks(8);context.takeScreenshot("plumbing-"+label+"-redstone-stopped");
    server.runOnServer(a->{var l=a.overworld();l.setBlockAndUpdate(valve.north(),Blocks.AIR.defaultBlockState());var from=(MachineEntity)l.getBlockEntity(source);var to=(MachineEntity)l.getBlockEntity(target);to.copper.processFluid=WorkshopFluidStorage.CAPACITY;to.sync();if(WaterSupply.transfer(l,from,8100)!=0)throw new IllegalStateException("Full tank accepted fluid");});context.waitTicks(8);context.takeScreenshot("plumbing-"+label+"-receiver-full");
    server.runOnServer(a->a.getPlayerList().getPlayers().getFirst().openMenu((MachineEntity)a.overworld().getBlockEntity(target)));context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(4);context.takeScreenshot("plumbing-"+label+"-full-gui");context.setScreen(()->null);
   }
  }
 }
}
