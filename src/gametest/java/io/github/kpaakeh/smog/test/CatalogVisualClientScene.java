package io.github.kpaakeh.smog.test;
import java.util.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.world.item.*;
import net.minecraft.core.registries.BuiltInRegistries;
import io.github.kpaakeh.smog.registry.ModBlocks;
/** Real native creative pages for every production item, followed by representative hands. */
public final class CatalogVisualClientScene {
 public static void run(ClientGameTestContext context){
  context.runOnClient(mc->{mc.getWindow().setWindowed(1280,960);mc.options.guiScale().set(2);mc.resizeGui();});context.waitTicks(8);
  try(var world=context.worldBuilder().create()){
   world.getServer().runCommand("fill -8 99 -8 8 99 8 minecraft:stone");world.getServer().runCommand("tp @a 0.5 101 0.5 0 0");world.getServer().runCommand("time set noon");
   world.getServer().runOnServer(server->server.getPlayerList().getPlayers().getFirst().setGameMode(net.minecraft.world.level.GameType.CREATIVE));
   context.waitFor(mc->mc.player!=null&&mc.player.isCreative());
   context.setScreen(()->{var mc=net.minecraft.client.Minecraft.getInstance();return new net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen(mc.player,mc.level.enabledFeatures(),true);});
   context.waitFor(mc->mc.gui.screen() instanceof net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen);
   for(String id:io.github.kpaakeh.smog.registry.ModCreativeTabs.IDS){
    var expected=new ArrayList<ItemStack>();var seen=new ArrayList<ItemStack>();
    context.runOnClient(mc->{var tab=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id(id));expected.addAll(tab.getDisplayItems());var screen=(net.fabricmc.fabric.api.client.creativetab.v1.FabricCreativeModeInventoryScreen)mc.gui.screen();screen.switchToPage(screen.getPage(tab));screen.setSelectedTab(tab);});
    for(int page=0;page<(expected.size()+44)/45;page++){
     if(page>0)context.runOnClient(mc->mc.gui.screen().mouseScrolled(mc.getWindow().getGuiScaledWidth()/2.0,mc.getWindow().getGuiScaledHeight()/2.0,0,-5));context.waitTicks(3);
     context.runOnClient(mc->{var menu=((net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen)mc.gui.screen()).getMenu();for(int slot=0;slot<45;slot++){var stack=menu.getSlot(slot).getItem();if(!stack.isEmpty())seen.add(stack.copy());}});
     context.takeScreenshot("catalog-"+id+"-page-"+page);
    }
    for(var stack:expected)if(seen.stream().noneMatch(s->ItemStack.isSameItemSameComponents(s,stack)))throw new IllegalStateException("Catalog entry never rendered: "+BuiltInRegistries.ITEM.getKey(stack.getItem())+" in "+id);
   }
   context.setScreen(()->null);
   for(String utility:List.of("water_tank","hand_water_pump","rope_anchor")){
    world.getServer().runOnServer(server->{var level=server.overworld();var pos=new net.minecraft.core.BlockPos(3,100,3);level.setBlockAndUpdate(pos,BuiltInRegistries.BLOCK.getValue(ModBlocks.id(utility)).defaultBlockState());var m=(io.github.kpaakeh.smog.machine.MachineEntity)level.getBlockEntity(pos);m.serverTick();server.getPlayerList().getPlayers().getFirst().openMenu(m);});
    context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(5);context.takeScreenshot("utility-gui-"+utility);context.setScreen(()->null);
   }
   for(var schema:io.github.kpaakeh.smog.copper.ToolDefinitions.templates()){
    world.getServer().runOnServer(server->{var p=server.getPlayerList().getPlayers().getFirst();p.getInventory().setSelectedSlot(0);p.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,io.github.kpaakeh.smog.copper.ToolParts.example(schema,"copper"));p.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND,io.github.kpaakeh.smog.copper.ToolParts.example(schema,"iron"));p.inventoryMenu.broadcastChanges();p.containerMenu.broadcastChanges();});
    context.runOnClient(mc->{mc.setCameraEntity(mc.player);mc.options.setCameraType(net.minecraft.client.CameraType.FIRST_PERSON);mc.options.fov().set(70);});context.waitFor(mc->!mc.player.getMainHandItem().isEmpty()&&!mc.player.getOffhandItem().isEmpty());context.waitTicks(30);context.takeScreenshot("hands-"+schema.id());
   }
  }
  VisualReviewClientScene.run(context);
 }
}
