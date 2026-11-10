package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.world.level.block.*;

public final class BronzeGearClientScene {
    public static void run(ClientGameTestContext context){
        var previous=CopperClientScene.language(context,"ru_ru");context.runOnClient(mc->{mc.getWindow().setWindowed(1280,960);mc.options.guiScale().set(2);mc.options.fov().set(60);mc.resizeGui();});
        try(var world=context.worldBuilder().create()){
            var server=world.getServer();server.runCommand("fill -8 97 -8 48 97 16 minecraft:stone");server.runCommand("time set noon");server.runCommand("weather clear");var positions=new java.util.ArrayList<BlockPos>();var sources=new java.util.ArrayList<BlockPos>();
            server.runOnServer(s->{var level=s.overworld();var player=s.getPlayerList().getPlayers().getFirst();player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);player.setNoGravity(true);player.getAbilities().flying=true;player.onUpdateAbilities();int i=0;
                for(var axis:java.util.List.of(Direction.NORTH,Direction.EAST,Direction.UP))for(boolean fast:new boolean[]{true,false}){
                    var large=new BlockPos(i++*7,102,0);var side=axis.getAxis()==Direction.Axis.X?Direction.SOUTH:Direction.EAST;var small=large.relative(side);positions.add(large);
                    level.setBlockAndUpdate(large,BronzeContent.entry("bronze_reduction_gearbox").block().defaultBlockState().setValue(BronzeBlock.FACING,axis));level.setBlockAndUpdate(small,BronzeContent.entry("bronze_cogwheel").block().defaultBlockState().setValue(BronzeBlock.FACING,axis));
                    var driver=fast?large:small;level.setBlockAndUpdate(driver.relative(axis),BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,axis));var source=driver.relative(axis,2);sources.add(source);level.setBlockAndUpdate(source,ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,axis));
                }BronzeNetwork.invalidate(level);
            });context.waitTicks(80);context.getInput().pressKey(com.mojang.blaze3d.platform.InputConstants.KEY_F1);
            for(int i=0;i<positions.size();i++){
                var p=positions.get(i);int axis=i/2;double x=p.getX()+(axis==1?-4:2.5),y=axis==2?98.5:102.5,z=axis==0?4:3.5;
                server.runCommand("tp @a "+x+" "+y+" "+z+" 0 0");context.waitTicks(20);context.getInput().lookAt(p);context.waitTicks(15);context.takeScreenshot("cogwheel-"+axis+"-"+(i%2==0?"24-to-12":"12-to-24"));context.waitTicks(5);context.takeScreenshot("cogwheel-"+axis+"-motion-"+i);
            }
            server.runCommand("tp @a 4.2 101 4.5 180 0");context.waitTicks(20);context.getInput().lookAt(180,0);context.waitTicks(15);context.takeScreenshot("cogwheel-outer-teeth-at-screen-edge");
            server.runOnServer(s->{for(var p:positions)if(!BronzeNetwork.query(s.overworld(),p).running())throw new IllegalStateException("Gear review is not actually powered: "+p);for(var p:sources)s.overworld().setBlockAndUpdate(p,s.overworld().getBlockState(p).setValue(ShaftRotationFixture.REVERSE,true));BronzeNetwork.invalidate(s.overworld());});context.waitTicks(8);context.takeScreenshot("cogwheel-reverse");
            server.runOnServer(s->{for(var p:sources)s.overworld().setBlockAndUpdate(p,s.overworld().getBlockState(p).setValue(ShaftRotationFixture.ACTIVE,false));BronzeNetwork.invalidate(s.overworld());});context.waitTicks(5);context.takeScreenshot("cogwheel-stopped");
        }context.getInput().pressKey(com.mojang.blaze3d.platform.InputConstants.KEY_F1);if(!previous.equals("ru_ru"))CopperClientScene.language(context,previous);
    }
}
