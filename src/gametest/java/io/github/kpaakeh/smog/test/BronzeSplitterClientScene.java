package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.Vec3;

public final class BronzeSplitterClientScene {
    private static void capture(ClientGameTestContext context,net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext server,BlockPos pos,Direction facing,Direction output,String name){
        var x=new Vec3(output.getStepX(),output.getStepY(),output.getStepZ());var z=new Vec3(-facing.getStepX(),-facing.getStepY(),-facing.getStepZ());var camera=Vec3.atCenterOf(pos).add(z.scale(-2.8)).add(x.scale(2.8)).add(z.cross(x).scale(1.7));
        server.runCommand("tp @a "+camera.x+" "+(camera.y-1.62)+" "+camera.z+" 0 0");context.waitTicks(20);context.getInput().lookAt(pos);context.waitTicks(12);context.takeScreenshot(name);
    }
    public static void run(ClientGameTestContext context){
        var previous=CopperClientScene.language(context,"ru_ru");context.runOnClient(mc->{mc.getWindow().setWindowed(1280,960);mc.options.guiScale().set(2);mc.options.fov().set(60);mc.resizeGui();});
        try(var world=context.worldBuilder().create()){
            var server=world.getServer();server.runCommand("fill -8 94 -8 64 94 32 minecraft:stone");server.runCommand("time set noon");server.runCommand("weather clear");var positions=new java.util.ArrayList<BlockPos>();
            server.runOnServer(s->{var level=s.overworld();var player=s.getPlayerList().getPlayers().getFirst();player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);player.setNoGravity(true);player.getAbilities().flying=true;player.onUpdateAbilities();int index=0;
                for(var facing:Direction.values())for(var output:Direction.values())if(facing.getAxis()!=output.getAxis()){
                    var p=new BlockPos((index%8)*7,102,(index/8)*9);positions.add(p);index++;var state=BronzeContent.entry("bronze_splitter").block().defaultBlockState().setValue(BronzeBlock.FACING,facing).setValue(BronzeAngleBlock.OUTPUT,output);level.setBlockAndUpdate(p,state);
                    for(var side:Direction.values())if(BronzeSplitterBlock.ports(state).contains(side))level.setBlockAndUpdate(p.relative(side),BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,BronzeShaft.canonical(side)));
                    var input=facing.getOpposite();level.setBlockAndUpdate(p.relative(input,2),ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,input).setValue(ShaftRotationFixture.REVERSE,true));
                }BronzeNetwork.invalidate(level);
            });context.waitTicks(70);context.getInput().pressKey(com.mojang.blaze3d.platform.InputConstants.KEY_F1);
            for(var p:positions){var directions=new Direction[2];server.runOnServer(s->{var state=s.overworld().getBlockState(p);directions[0]=state.getValue(BronzeBlock.FACING);directions[1]=BronzeAngleBlock.output(state);if(!BronzeNetwork.query(s.overworld(),p).running())throw new IllegalStateException("Review splitter is not powered: "+p);});capture(context,server,p,directions[0],directions[1],"splitter-"+directions[0].getName()+"-"+directions[1].getName());}
            var p=positions.getFirst();var facing=Direction.DOWN;var output=Direction.NORTH;
            for(int mask=0;mask<8;mask++){
                final int selected=mask;server.runOnServer(s->{var level=s.overworld();level.setBlockAndUpdate(p,level.getBlockState(p).setValue(BronzeSplitterBlock.FRONT,(selected&1)!=0).setValue(BronzeSplitterBlock.RIGHT,(selected&2)!=0).setValue(BronzeSplitterBlock.LEFT,(selected&4)!=0));BronzeNetwork.invalidate(level);});context.waitTicks(10);
                capture(context,server,p,facing,output,"splitter-mask-"+mask);capture(context,server,p,facing,output.getOpposite(),"splitter-mask-back-"+mask);
            }
            context.getInput().pressKey(com.mojang.blaze3d.platform.InputConstants.KEY_F1);
            server.runOnServer(s->{var player=s.getPlayerList().getPlayers().getFirst();((BronzeEntity)s.overworld().getBlockEntity(p)).inspect(player);});context.waitTicks(3);context.takeScreenshot("splitter-inspection");
        }if(!previous.equals("ru_ru"))CopperClientScene.language(context,previous);
    }
}
