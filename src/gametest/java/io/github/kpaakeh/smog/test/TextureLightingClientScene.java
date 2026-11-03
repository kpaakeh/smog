package io.github.kpaakeh.smog.test;
import java.nio.file.*;
import java.util.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.LightLayer;
/** Every production PNG is displayed as a native, non-glowing world-lit swatch, with an index manifest.
 * This measures the texture itself; separate normal item/tool scenes check geometry and tint assembly.
 */
public final class TextureLightingClientScene {
 public static void run(ClientGameTestContext context){
  context.runOnClient(mc->{mc.getWindow().setWindowed(1600,1000);mc.options.guiScale().set(2);mc.options.fov().set(55);if(!mc.gui.hud.isHidden())mc.gui.hud.toggle();mc.resizeGui();});
  var selected=new HashSet<Integer>();for(String page:System.getProperty("smog.textureReviewPages","").split(","))if(!page.isBlank())selected.add(Integer.parseInt(page));
  var manifest=new ArrayList<String>();manifest.add("page,cell,texture,swatch,tint");
  try(var world=context.worldBuilder().create()){
   var server=world.getServer();server.runCommand("fill -16 99 -16 24 99 16 minecraft:stone");server.runCommand("fill -2 100 -1 9 106 -1 minecraft:stone");server.runCommand("tp @a 3.5 101.7 9 180 0");server.runCommand("weather clear");
   context.waitTicks(15);context.getInput().lookAt(new BlockPos(3,103,0));
   var frames=new ArrayList<ItemFrame>();server.runOnServer(s->{for(int i=0;i<32;i++){var f=new ItemFrame(s.overworld(),new BlockPos(i%8,104-i/8,0),Direction.SOUTH);s.overworld().addFreshEntity(f);frames.add(f);}});context.waitTicks(12);
   for(int page=0;page<(TextureReviewCards.ALL.size()+31)/32;page++){
    if(!selected.isEmpty()&&!selected.contains(page))continue;
    final int start=page*32;
    server.runOnServer(s->{for(int i=0;i<32;i++){if(start+i<TextureReviewCards.ALL.size()){var card=TextureReviewCards.ALL.get(start+i);var stack=new ItemStack(card.item());int tint=card.texture().contains("tool_forms/")||card.texture().contains("tool_overlays/")?io.github.kpaakeh.smog.copper.ToolDefinitions.find("copper","pickaxe_head",false).color():0xFFFFFF;stack.set(DataComponents.CUSTOM_MODEL_DATA,new CustomModelData(List.of(),List.of(),List.of(),List.of(tint)));frames.get(i).setItem(stack,false);manifest.add((start/32)+","+i+","+card.texture()+","+card.id()+","+Integer.toHexString(tint));}else frames.get(i).setItem(ItemStack.EMPTY,false);}});
    for(String lighting:List.of("day","block7")){
     if(lighting.equals("day")){server.runCommand("fill -16 107 -16 24 107 16 minecraft:air");server.runCommand("time set noon");}else{server.runCommand("fill -16 107 -16 24 107 16 minecraft:stone");server.runCommand("time set midnight");}
     server.runOnServer(s->{for(int i=0;i<32;i++)s.overworld().setBlockAndUpdate(new BlockPos(i%8,104-i/8,0),lighting.equals("day")?Blocks.AIR.defaultBlockState():Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL,7));});
     context.waitTicks(35);if(lighting.equals("block7"))server.runOnServer(a->{for(int i=0;i<32;i++)if(a.overworld().getBrightness(LightLayer.BLOCK,new BlockPos(i%8,104-i/8,0))!=7)throw new IllegalStateException("Swatch block light differs from 7");});context.takeScreenshot("textures-page-"+page+"-"+lighting);
    }
   }
   server.runCommand("fill -16 107 -16 24 107 16 minecraft:air");server.runCommand("time set noon");
   for(var schema:io.github.kpaakeh.smog.copper.ToolDefinitions.templates()){
    server.runOnServer(s->{var p=s.getPlayerList().getPlayers().getFirst();p.getInventory().setSelectedSlot(0);p.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,io.github.kpaakeh.smog.copper.ToolParts.example(schema,"copper"));p.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND,io.github.kpaakeh.smog.copper.ToolParts.example(schema,"iron"));p.inventoryMenu.broadcastChanges();});
    context.waitFor(mc->!mc.player.getOffhandItem().isEmpty());context.runOnClient(mc->{mc.setCameraEntity(mc.player);mc.options.setCameraType(net.minecraft.client.CameraType.THIRD_PERSON_FRONT);});context.waitTicks(30);context.takeScreenshot("tools-thirdperson-"+schema.id());
   }
   context.runOnClient(mc->{mc.options.setCameraType(net.minecraft.client.CameraType.FIRST_PERSON);if(mc.gui.hud.isHidden())mc.gui.hud.toggle();});
  }
  try{var out=Path.of("..","..","review","2026-11-03",selected.isEmpty()?"texture-lighting":"texture-lighting-final");Files.createDirectories(out);Files.write(out.resolve("manifest.csv"),manifest);try(var files=Files.list(Path.of("screenshots"))){for(var p:files.toList())Files.copy(p,out.resolve(p.getFileName()),StandardCopyOption.REPLACE_EXISTING);}}catch(Exception e){throw new RuntimeException(e);}
 }
}

