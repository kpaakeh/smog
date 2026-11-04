package io.github.kpaakeh.smog.test;
import java.nio.file.*;
import io.github.kpaakeh.smog.copper.CopperTransport;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.block.*;
/** Texture-only cart acceptance: existing model/UV, actual world-lit entity from three sides. */
public final class CartTextureReviewScene {
 public static void run(ClientGameTestContext context){
  context.runOnClient(mc->{mc.getWindow().setWindowed(1600,1000);mc.options.fov().set(55);if(!mc.gui.hud.isHidden())mc.gui.hud.toggle();mc.resizeGui();});
  try(var world=context.worldBuilder().create()){
   var server=world.getServer();server.runCommand("fill -16 99 -16 16 99 16 minecraft:stone");server.runCommand("weather clear");server.runOnServer(s->{var player=s.getPlayerList().getPlayers().getFirst();player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);player.getAbilities().flying=true;player.onUpdateAbilities();var cart=CopperTransport.HANDCART.create(s.overworld(),EntitySpawnReason.COMMAND);cart.setInitialPos(.5,100,.5);s.overworld().addFreshEntity(cart);});context.waitTicks(20);
   for(String light:new String[]{"day","block7"}){
    server.runCommand("time set "+(light.equals("day")?"noon":"midnight"));server.runCommand("fill -16 106 -16 16 106 16 minecraft:"+(light.equals("day")?"air":"stone"));
    server.runOnServer(s->{for(int x=-1;x<=1;x++)for(int y=100;y<=102;y++)for(int z=-1;z<=2;z++)s.overworld().setBlockAndUpdate(new BlockPos(x,y,z),light.equals("day")?Blocks.AIR.defaultBlockState():Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL,7));});context.waitTicks(30);
    double[][] views={{3.5,100.2,3.5},{-3.5,100.2,1},{.5,100.5,-3.5}};int i=0;
    for(var eye:views){server.runCommand("tp @a "+eye[0]+" "+eye[1]+" "+eye[2]);context.waitTicks(8);context.getInput().lookAt(new BlockPos(0,100,0));context.waitTicks(6);context.takeScreenshot("cart-texture-"+light+"-view-"+(i++));}
   }
  }
  context.runOnClient(mc->{if(mc.gui.hud.isHidden())mc.gui.hud.toggle();});
  try{var out=Path.of("..","..","review","2026-11-04","texture-polish",System.getProperty("smog.cartReviewFolder","game"));Files.createDirectories(out);try(var files=Files.list(Path.of("screenshots"))){for(var p:files.toList())Files.copy(p,out.resolve(p.getFileName()),StandardCopyOption.REPLACE_EXISTING);}}catch(Exception e){throw new RuntimeException(e);}
 }
}
