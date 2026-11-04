package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.world.level.block.*;

public final class BronzeCrankClientScene {
    public static void run(ClientGameTestContext context){
        var previous=CopperClientScene.language(context,"ru_ru");context.runOnClient(mc->{mc.getWindow().setWindowed(1280,960);mc.options.guiScale().set(2);mc.options.fov().set(60);mc.resizeGui();});
        try(var world=context.worldBuilder().create()){
            var server=world.getServer();server.runCommand("fill -6 96 -8 30 96 8 minecraft:stone");server.runCommand("time set noon");server.runCommand("weather clear");
            server.runOnServer(s->{var level=s.overworld();var player=s.getPlayerList().getPlayers().getFirst();player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);player.setNoGravity(true);player.getAbilities().flying=true;player.onUpdateAbilities();int i=0;
                for(var facing:Direction.values()){
                    var p=new BlockPos(i++*5,101,0);level.setBlockAndUpdate(p,BronzeContent.entry("bronze_hand_crank").block().defaultBlockState().setValue(BronzeBlock.FACING,facing));level.setBlockAndUpdate(p.relative(facing.getOpposite()),BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,BronzeShaft.canonical(facing)));
                }
            });context.waitTicks(30);
            for(int i=0;i<Direction.values().length;i++){
                var face=Direction.values()[i];var p=new BlockPos(i*5,101,0);double x=p.getX()+.5+face.getStepX()*2.4+.8,z=.5+face.getStepZ()*2.4+.6,y=101.5+face.getStepY()*2.4;
                server.runCommand("tp @a "+x+" "+(y-1.62)+" "+z+" 0 0");context.waitTicks(20);context.getInput().lookAt(p);context.waitTicks(10);context.takeScreenshot("handle-"+face.getName());
            }
            server.runCommand("tp @a 1.3 97.48 1.1 0 0");context.waitTicks(30);context.getInput().lookAt(new BlockPos(0,101,0));context.waitTicks(20);context.takeScreenshot("handle-down-revisit");
            server.runCommand("tp @a 3.5 98.8 2.5 0 0");context.waitTicks(20);context.getInput().lookAt(new BlockPos(0,101,0));context.waitTicks(15);context.takeScreenshot("handle-down-side");
            // Real held mouse input, not a direct call to crank().
            var p=new BlockPos(10,101,0);server.runCommand("tp @a 10.5 100 -2.5 0 0");context.waitTicks(20);context.getInput().lookAt(p);context.waitTicks(12);
            context.getInput().holdMouse(com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_RIGHT);context.waitTicks(18);
            server.runOnServer(s->{var result=BronzeNetwork.query(s.overworld(),p.south());if(!result.running()||result.rate().value()!=16)throw new IllegalStateException("Holding right mouse did not drive the shaft: "+result);});context.takeScreenshot("handle-held-forward");
            context.getInput().releaseMouse(com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_RIGHT);context.waitTicks(8);
            server.runOnServer(s->{if(BronzeNetwork.query(s.overworld(),p.south()).running())throw new IllegalStateException("Released mouse kept driving");});context.takeScreenshot("handle-released");
            context.getInput().holdShift();context.getInput().holdMouse(com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_RIGHT);context.waitTicks(18);
            server.runOnServer(s->{var result=BronzeNetwork.query(s.overworld(),p.south());if(!result.running()||result.rate().value()!=-16)throw new IllegalStateException("Shift plus held mouse did not reverse the shaft: "+result);});context.takeScreenshot("handle-held-reverse");
            context.getInput().releaseMouse(com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_RIGHT);context.getInput().releaseShift();context.waitTicks(8);
        }if(!previous.equals("ru_ru"))CopperClientScene.language(context,previous);
    }
}
