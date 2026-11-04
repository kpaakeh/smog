package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.world.level.block.*;

public final class BronzeAngleClientScene {
    public static void run(ClientGameTestContext context){
        var previous=CopperClientScene.language(context,"ru_ru");context.runOnClient(mc->{mc.getWindow().setWindowed(1280,960);mc.options.guiScale().set(2);mc.options.fov().set(60);mc.resizeGui();});
        try(var world=context.worldBuilder().create()){
            var server=world.getServer();server.runCommand("fill -8 96 -8 62 96 32 minecraft:stone");server.runCommand("time set noon");server.runCommand("weather clear");var cells=new java.util.ArrayList<BlockPos>();var labels=new java.util.ArrayList<String>();
            server.runOnServer(s->{var level=s.overworld();var player=s.getPlayerList().getPlayers().getFirst();player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);player.setNoGravity(true);player.getAbilities().flying=true;player.onUpdateAbilities();int i=0;
                for(var facing:Direction.values())for(var output:Direction.values())if(facing.getAxis()!=output.getAxis()){
                    var p=new BlockPos((i%8)*7,101,(i/8)*9);cells.add(p);labels.add(facing.getName()+"-to-"+output.getName());i++;var input=facing.getOpposite();
                    level.setBlockAndUpdate(p,BronzeContent.entry("bronze_angle_gearbox").block().defaultBlockState().setValue(BronzeBlock.FACING,facing).setValue(BronzeAngleBlock.OUTPUT,output));
                    for(var side:new Direction[]{input,output})level.setBlockAndUpdate(p.relative(side),BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,BronzeShaft.canonical(side)));
                    level.setBlockAndUpdate(p.relative(input,2),ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,input).setValue(ShaftRotationFixture.REVERSE,true).setValue(ShaftRotationFixture.TEMPO,i%ShaftRotationFixture.SPEEDS.length));
                }BronzeNetwork.invalidate(level);
            });context.waitTicks(30);
            for(int i=0;i<cells.size();i++){
                var p=cells.get(i);final int index=i;var sides=new Direction[2];server.runOnServer(s->{var state=s.overworld().getBlockState(p);sides[0]=state.getValue(BronzeBlock.FACING).getOpposite();sides[1]=BronzeAngleBlock.output(state);});
                var camera=net.minecraft.world.phys.Vec3.atCenterOf(p).add(-2.7*(sides[0].getStepX()+sides[1].getStepX()),-2.7*(sides[0].getStepY()+sides[1].getStepY())+.5,-2.7*(sides[0].getStepZ()+sides[1].getStepZ()));
                server.runCommand("tp @a "+camera.x+" "+(camera.y-1.62)+" "+camera.z+" 0 0");context.waitTicks(20);context.getInput().lookAt(p);context.waitTicks(12);context.takeScreenshot("angle-"+labels.get(i));
                if(index==0){context.waitTicks(7);context.takeScreenshot("angle-motion-second-frame");}
            }
            server.runOnServer(s->{for(var p:cells){var result=BronzeNetwork.query(s.overworld(),p);if(!result.running()||!((BronzeEntity)s.overworld().getBlockEntity(p)).moving())throw new IllegalStateException("Corner not turning: "+p);}});
        }if(!previous.equals("ru_ru"))CopperClientScene.language(context,previous);
    }
}
