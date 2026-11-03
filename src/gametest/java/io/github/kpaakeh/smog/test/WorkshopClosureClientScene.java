package io.github.kpaakeh.smog.test;
import java.nio.file.*;
import java.util.*;
import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.phys.Vec3;
/** Final acceptance fixtures use normal containers, recipes, shapes and world lighting. */
public final class WorkshopClosureClientScene {
 private static Block block(String id){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(id));}
 private static MachineEntity make(net.minecraft.server.level.ServerLevel level,BlockPos pos,String id,boolean assembled){
  if(assembled)Multiblocks.get(ModBlocks.id(id)).parts().forEach((offset,part)->level.setBlockAndUpdate(pos.offset(offset),part.block().defaultBlockState()));
  level.setBlockAndUpdate(pos,block(id).defaultBlockState());var m=(MachineEntity)level.getBlockEntity(pos);if(assembled&&!m.tryAssemble())throw new IllegalStateException("Cannot assemble "+id);if(!assembled)m.serverTick();return m;
 }
 private static void view(ClientGameTestContext context,net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext server,BlockPos pos,String name,int index,int expected){
  server.runCommand("tp @a "+(pos.getX()+.5)+" "+pos.getY()+" "+(pos.getZ()-2.5));context.waitTicks(6);
  server.runOnServer(s->{var p=s.getPlayerList().getPlayers().getFirst();var m=(MachineEntity)s.overworld().getBlockEntity(pos);m.sync();if(CopperWorkshops.data(m,index)!=expected)throw new IllegalStateException(name+" backend state "+CopperWorkshops.data(m,index)+" expected "+expected);p.openMenu(m);});
  context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen screen&&screen.getMenu().data.get(index)==expected);context.waitTicks(3);context.takeScreenshot(name);context.setScreen(()->null);
 }
 public static void run(ClientGameTestContext context){
  CopperClientScene.language(context,"ru_ru");context.runOnClient(mc->{mc.getWindow().setWindowed(1280,960);mc.options.guiScale().set(2);mc.options.fov().set(60);mc.resizeGui();});
  try(var world=context.worldBuilder().create()){
   var server=world.getServer();server.runCommand("fill -16 98 -16 34 98 34 minecraft:stone");server.runCommand("time set noon");server.runCommand("weather clear");
   var pressPos=new BlockPos(0,100,0);var washPos=new BlockPos(8,100,0);var tankPos=new BlockPos(0,100,8);var pumpPos=new BlockPos(4,100,8);var ropePos=new BlockPos(12,104,8);
   server.runOnServer(s->{var l=s.overworld();make(l,pressPos,"lever_press",true);make(l,washPos,"washing_trough",true);make(l,tankPos,"water_tank",false);make(l,pumpPos,"hand_water_pump",false);make(l,ropePos,"rope_anchor",false);var viewer=s.getPlayerList().getPlayers().getFirst();viewer.setGameMode(net.minecraft.world.level.GameType.CREATIVE);viewer.getAbilities().flying=true;viewer.onUpdateAbilities();});context.waitTicks(20);
   if(!Boolean.getBoolean("smog.closureWorldOnly"))for(int scale:new int[]{1,2,3,4}){
    final int actual=scale;context.runOnClient(mc->{mc.options.guiScale().set(actual);mc.resizeGui();});
    context.runOnClient(mc->{for(var schema:ToolDefinitions.templates())for(String metal:List.of("wood","stone","copper","iron","tin","lead","gold","bronze"))for(String wrap:List.of("none","fiber","wool","leather")){
     var stack=ToolParts.example(schema,metal);if(!wrap.equals("none")){var parts=new ArrayList<>(ToolParts.components(stack));parts.set(3,new ItemStack(ToolParts.item(wrap+"_wrap")));stack=ToolParts.assemble(schema,parts);}
     for(int mode:new int[]{ToolTooltip.PROPERTIES,ToolTooltip.CONSTRUCTION}){var lines=new ArrayList<net.minecraft.network.chat.Component>();lines.add(stack.getHoverName());lines.addAll(ToolTooltip.lines(stack,mode));var fitted=io.github.kpaakeh.smog.gui.ToolTooltipLayout.fit(lines,mc.font,Math.max(120,Math.min(260,mc.getWindow().getGuiScaledWidth()-24)),mc.getWindow().getGuiScaledHeight());int rows=fitted.size();if(rows*(mc.font.lineHeight+1)>mc.getWindow().getGuiScaledHeight()-16)throw new IllegalStateException("Tooltip exceeds viewport: "+schema.id()+"/"+metal+"/"+wrap+" mode "+mode+" scale "+actual+" rows "+rows);}
    }});
    for(int reason:new int[]{3,4,5,7,8,9,10,13,1}){
     server.runOnServer(s->{var m=(MachineEntity)s.overworld().getBlockEntity(pressPos);m.enabled(true);m.clearContent();m.copper.queued=0;m.copper.work=0;m.copper.autoWork=false;m.copper.processFluid=0;
      if(reason!=3&&reason!=13)m.setItem(0,new ItemStack(reason==4?Items.STICK:Items.WHEAT_SEEDS,reason==5?3:64));
      if(reason==7)m.copper.processFluid=WorkshopFluidStorage.CAPACITY;
      if(reason==8)m.setItem(3,new ItemStack(Items.STICK,64));
      if(reason==10)m.copper.queued=8;
      if(reason==13)m.setItem(3,new ItemStack(ToolParts.item("seed_cake"),3));
      if(reason==1)m.enabled(false);m.sync();});
     view(context,server,pressPos,"closure-scale-"+scale+"-press-"+reason,31,reason);
    }
    server.runOnServer(s->{var m=(MachineEntity)s.overworld().getBlockEntity(washPos);m.setItem(0,new ItemStack(Items.RAW_COPPER,16));m.copper.processFluid=1;m.sync();});view(context,server,washPos,"closure-scale-"+scale+"-wash-water",31,6);
    for(int reason:new int[]{5,6,4,0}){
     server.runOnServer(s->{var m=(MachineEntity)s.overworld().getBlockEntity(tankPos);m.enabled(reason!=0);m.copper.processFluid=reason==5?0:reason==4?WorkshopFluidStorage.CAPACITY:81001;m.sync();});view(context,server,tankPos,"closure-scale-"+scale+"-tank-"+reason,36,reason);
    }
    server.runOnServer(s->{var m=(MachineEntity)s.overworld().getBlockEntity(pumpPos);m.copper.pumpBudget=0;s.overworld().setBlockAndUpdate(pumpPos.below(),Blocks.AIR.defaultBlockState());m.sync();});view(context,server,pumpPos,"closure-scale-"+scale+"-pump-source",36,1);
    server.runOnServer(s->{s.overworld().setBlockAndUpdate(pumpPos.below(),Blocks.WATER.defaultBlockState());});view(context,server,pumpPos,"closure-scale-"+scale+"-pump-prime",36,2);
    server.runOnServer(s->{var m=(MachineEntity)s.overworld().getBlockEntity(pumpPos);WaterSupply.prime(s.overworld(),m);});view(context,server,pumpPos,"closure-scale-"+scale+"-pump-running",36,3);
    view(context,server,ropePos,"closure-scale-"+scale+"-rope-link",36,1);
   }
   context.runOnClient(mc->{mc.options.guiScale().set(2);mc.resizeGui();if(!mc.gui.hud.isHidden())mc.gui.hud.toggle();});
   if(!Boolean.getBoolean("smog.closureDecorOnly")){
   // GUI sprites live in a separate atlas in production; the test copies use item atlas paths.
   server.runCommand("fill -2 100 19 11 106 19 minecraft:stone");
   var guiFrames=new ArrayList<ItemFrame>();server.runOnServer(a->{int i=0;for(var card:TextureReviewCards.ALL)if(card.texture().startsWith("smog:gui/")){var f=new ItemFrame(a.overworld(),new BlockPos(i++,104,20),Direction.SOUTH);f.setItem(new ItemStack(card.item()),false);a.overworld().addFreshEntity(f);guiFrames.add(f);}});
   server.runCommand("tp @a 3.5 102 28 180 0");context.waitTicks(15);context.getInput().lookAt(new BlockPos(3,104,20));context.waitTicks(5);context.takeScreenshot("closure-gui-textures-day");
   server.runCommand("time set midnight");server.runCommand("fill -4 107 17 14 107 31 minecraft:stone");server.runOnServer(a->{for(int i=0;i<guiFrames.size();i++)a.overworld().setBlockAndUpdate(new BlockPos(i,104,20),Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL,7));});context.waitTicks(30);context.takeScreenshot("closure-gui-textures-block7");
   server.runOnServer(a->{for(var f:guiFrames)f.discard();for(int i=0;i<guiFrames.size();i++)a.overworld().setBlockAndUpdate(new BlockPos(i,104,20),Blocks.AIR.defaultBlockState());});server.runCommand("fill -4 107 17 14 107 31 minecraft:air");server.runCommand("time set noon");
   // Finished, normally tinted assemblies, face-on in native non-glowing frames.
   server.runCommand("fill -2 100 19 11 106 19 minecraft:stone");
   server.runOnServer(s->{int i=0;for(var schema:ToolDefinitions.templates())for(String metal:List.of("copper","iron")){var f=new ItemFrame(s.overworld(),new BlockPos(i%10,104-i/10,20),Direction.SOUTH);f.setItem(ToolParts.example(schema,metal),false);s.overworld().addFreshEntity(f);i++;}});
   server.runCommand("tp @a 4.5 102 29 180 0");context.waitTicks(15);context.getInput().lookAt(new BlockPos(4,103,20));context.waitTicks(10);context.takeScreenshot("closure-assembled-tools-day");
   server.runCommand("time set midnight");server.runCommand("fill -4 107 17 14 107 31 minecraft:stone");server.runOnServer(s->{for(int i=0;i<20;i++)s.overworld().setBlockAndUpdate(new BlockPos(i%10,104-i/10,20),Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL,7));});context.waitTicks(30);context.takeScreenshot("closure-assembled-tools-block7");
   server.runCommand("time set noon");
   }
   // Native outer/inner stairs, single/double slabs, connected and waterlogged walls.
   int family=0;for(String material:List.of("firebrick","limestone","fluorite","phosphorite","kaolin","dolomite","rock_salt","graphite")){
    final int z=38+family++*5;server.runOnServer(s->{var l=s.overworld();String base=material.equals("firebrick")?"firebrick":"polished_"+material;
     for(int x=0;x<=10;x++)for(int dz=-2;dz<=2;dz++)l.setBlockAndUpdate(new BlockPos(x,99,z+dz),Blocks.STONE.defaultBlockState());
     for(int x=4;x<=10;x++)for(int dz=-2;dz<=2;dz++)if((x==4||x==10||Math.abs(dz)==2)&&!(x==4&&dz==0))l.setBlockAndUpdate(new BlockPos(x,100,z+dz),Blocks.GLASS.defaultBlockState());
     var stair=block(base+"_stairs");var slab=block(base+"_slab");var wall=block(base+"_wall");
     l.setBlockAndUpdate(new BlockPos(0,100,z),stair.defaultBlockState().setValue(StairBlock.FACING,Direction.NORTH));l.setBlockAndUpdate(new BlockPos(0,100,z-1),stair.defaultBlockState().setValue(StairBlock.FACING,Direction.EAST));
     l.setBlockAndUpdate(new BlockPos(2,100,z),stair.defaultBlockState().setValue(StairBlock.FACING,Direction.NORTH).setValue(StairBlock.HALF,Half.TOP));l.setBlockAndUpdate(new BlockPos(2,100,z+1),stair.defaultBlockState().setValue(StairBlock.FACING,Direction.EAST).setValue(StairBlock.HALF,Half.TOP));
     l.setBlockAndUpdate(new BlockPos(4,100,z),slab.defaultBlockState().setValue(SlabBlock.TYPE,SlabType.DOUBLE));l.setBlockAndUpdate(new BlockPos(5,100,z),slab.defaultBlockState().setValue(SlabBlock.WATERLOGGED,true));
     for(int dx=6;dx<=8;dx++)l.setBlockAndUpdate(new BlockPos(dx,100,z),wall.defaultBlockState().setValue(WallBlock.WATERLOGGED,true));l.setBlockAndUpdate(new BlockPos(7,100,z+1),wall.defaultBlockState());
    });server.runCommand("tp @a 5.5 103 "+(z-11)+" 0 25");context.waitTicks(12);context.getInput().lookAt(new BlockPos(5,100,z));context.waitTicks(4);context.takeScreenshot("closure-decor-connections-"+material);server.runCommand("tp @a 5.5 103 "+(z+11)+" 180 25");context.waitTicks(8);context.getInput().lookAt(new BlockPos(5,100,z));context.waitTicks(3);context.takeScreenshot("closure-decor-connections-reverse-"+material);
   }
   context.runOnClient(mc->{if(mc.gui.hud.isHidden())mc.gui.hud.toggle();});
  }
  if(!Boolean.getBoolean("smog.closureDecorOnly")){PlumbingVisualClientScene.run(context);VisualReviewClientScene.run(context);}
  try{var out=Path.of("..","..","review","2026-11-03",Boolean.getBoolean("smog.closureDecorOnly")?"closure-decor-final":Boolean.getBoolean("smog.closureWorldOnly")?"closure-final":"closure");Files.createDirectories(out);try(var stream=Files.list(Path.of("screenshots"))){for(var p:stream.toList())Files.copy(p,out.resolve(p.getFileName()),StandardCopyOption.REPLACE_EXISTING);}}catch(Exception e){throw new RuntimeException(e);}
 }
}

