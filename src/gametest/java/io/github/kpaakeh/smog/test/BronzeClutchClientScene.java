package io.github.kpaakeh.smog.test;
import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.world.level.block.*;
public final class BronzeClutchClientScene {
 public static void run(ClientGameTestContext context){
  context.runOnClient(mc->{mc.getWindow().setWindowed(1280,960);mc.options.guiScale().set(2);mc.resizeGui();});
  try(var world=context.worldBuilder().create()){
   var server=world.getServer();server.runCommand("time set noon");server.runCommand("weather clear");server.runCommand("fill -8 94 -8 48 94 16 minecraft:stone");
   server.runOnServer(s->{var level=s.overworld();var player=s.getPlayerList().getPlayers().getFirst();player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);player.setNoGravity(true);player.getAbilities().flying=true;player.onUpdateAbilities();
    for(var face:Direction.values()){var p=new BlockPos(face.ordinal()*7,102,0);level.setBlockAndUpdate(p,BronzeContent.entry("bronze_clutch").block().defaultBlockState().setValue(BronzeBlock.FACING,face));level.setBlockAndUpdate(p.relative(face),BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,BronzeShaft.canonical(face)));level.setBlockAndUpdate(p.relative(face.getOpposite()),ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,face.getOpposite()));}BronzeNetwork.invalidate(level);
   });context.waitTicks(60);context.getInput().pressKey(com.mojang.blaze3d.platform.InputConstants.KEY_F1);
   for(int pass=0;pass<2;pass++)for(var face:Direction.values())for(boolean enabled:new boolean[]{true,false}){
    var p=new BlockPos(face.ordinal()*7,102,0);server.runOnServer(s->{var level=s.overworld();level.setBlockAndUpdate(p,level.getBlockState(p).setValue(BronzeBlock.OPEN,enabled));BronzeNetwork.invalidate(level);});
    server.runCommand("tp @a "+(p.getX()+(face.getAxis()==Direction.Axis.X?0.5:2.8))+" 102.1 "+(face.getAxis()==Direction.Axis.Z?0.5:2.8)+" 0 0");context.waitTicks(30);context.getInput().lookAt(p);context.waitTicks(8);context.takeScreenshot("clutch-pass"+pass+"-"+face.getName()+"-"+(enabled?"engaged":"disengaged"));
   }context.getInput().pressKey(com.mojang.blaze3d.platform.InputConstants.KEY_F1);
  }
 }
}
