package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import java.util.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.world.level.block.*;

/** Focused native-world review of one block: three axes, four tempos, joins and stop/reverse. */
public final class BronzeShaftClientScene {
    private static void capture(ClientGameTestContext context,net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext server,String name,double x,double y,double z,BlockPos target){
        server.runCommand("tp @a "+x+" "+y+" "+z+" 0 20");context.waitTicks(10);context.getInput().lookAt(target);context.waitTicks(8);context.takeScreenshot(name);
    }
    public static void run(ClientGameTestContext context){
        String previous=CopperClientScene.language(context,"ru_ru");
        context.runOnClient(mc->{mc.getWindow().setWindowed(1280,960);mc.options.guiScale().set(2);mc.options.fov().set(60);mc.resizeGui();});
        try(var world=context.worldBuilder().create()){
            var server=world.getServer();server.runCommand("fill -8 99 -8 40 99 26 minecraft:stone");server.runCommand("time set noon");server.runCommand("weather clear");
            var sources=new ArrayList<BlockPos>();var shafts=new ArrayList<BlockPos>();
            server.runOnServer(s->{var level=s.overworld();var player=s.getPlayerList().getPlayers().getFirst();player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);player.getAbilities().flying=true;player.onUpdateAbilities();
                for(int axis=0;axis<3;axis++)for(int tempo=0;tempo<ShaftRotationFixture.SPEEDS.length;tempo++){
                    var facing=axis==0?Direction.NORTH:axis==1?Direction.EAST:Direction.UP;var out=facing.getOpposite();var origin=new BlockPos(tempo*6+(axis==1?3:0),axis==2?105:101,axis*10);sources.add(origin);
                    level.setBlockAndUpdate(origin,ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,facing).setValue(ShaftRotationFixture.TEMPO,tempo));
                    for(int i=1;i<=3;i++){var p=origin.relative(out,i);shafts.add(p);level.setBlockAndUpdate(p,BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,i%2==0?facing:facing.getOpposite()));}
                }
                BronzeNetwork.invalidate(level);
            });
            context.waitTicks(30);
            capture(context,server,"shaft-three-axes-speed-1-to-64",36,121,-19,new BlockPos(16,102,10));
            capture(context,server,"shaft-z-close-opposite-end-joins",3.5,101,2,new BlockPos(0,101,2));
            capture(context,server,"shaft-x-close-opposite-end-joins",1.5,101,7,new BlockPos(1,101,10));
            capture(context,server,"shaft-y-close-opposite-end-joins",5,102,20.5,new BlockPos(0,103,20));
            for(int tempo=0;tempo<ShaftRotationFixture.SPEEDS.length;tempo++){
                capture(context,server,"shaft-speed-"+ShaftRotationFixture.SPEEDS[tempo]+"-a",tempo*6+3.5,101,2,new BlockPos(tempo*6,101,2));context.waitTicks(7);context.takeScreenshot("shaft-speed-"+ShaftRotationFixture.SPEEDS[tempo]+"-b");
            }
            server.runCommand("fill -8 108 -8 40 108 26 minecraft:stone");server.runCommand("time set midnight");
            var lamps=new ArrayList<BlockPos>();
            server.runOnServer(s->{var level=s.overworld();for(var p:shafts){var state=level.getBlockState(p);var side=state.getValue(BronzeBlock.FACING).getAxis()==Direction.Axis.X?Direction.NORTH:Direction.EAST;var lamp=p.relative(side);if(level.getBlockState(lamp).isAir()){lamps.add(lamp);level.setBlockAndUpdate(lamp,Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL,8));}}});context.waitTicks(35);
            server.runOnServer(s->{for(var p:shafts)if(s.overworld().getBrightness(net.minecraft.world.level.LightLayer.BLOCK,p)!=7)throw new IllegalStateException("Shaft review must use actual block light 7: "+p);});
            capture(context,server,"shaft-z-light-7",3.5,101,2,new BlockPos(0,101,2));capture(context,server,"shaft-x-light-7",1.5,101,7,new BlockPos(1,101,10));capture(context,server,"shaft-y-light-7",5,102,20.5,new BlockPos(0,103,20));
            server.runCommand("fill -8 108 -8 40 108 26 minecraft:air");server.runCommand("time set noon");server.runOnServer(s->lamps.forEach(p->s.overworld().setBlockAndUpdate(p,Blocks.AIR.defaultBlockState())));context.waitTicks(15);
            server.runOnServer(s->{for(var p:sources)s.overworld().setBlockAndUpdate(p,s.overworld().getBlockState(p).setValue(ShaftRotationFixture.REVERSE,true));BronzeNetwork.invalidate(s.overworld());});context.waitTicks(5);
            capture(context,server,"shaft-reverse",3.5,101,2,new BlockPos(0,101,2));
            server.runOnServer(s->{for(var p:sources)s.overworld().setBlockAndUpdate(p,s.overworld().getBlockState(p).setValue(ShaftRotationFixture.ACTIVE,false));BronzeNetwork.invalidate(s.overworld());});context.waitTicks(4);context.takeScreenshot("shaft-stopped-a");context.waitTicks(25);context.takeScreenshot("shaft-stopped-b");
            server.runOnServer(s->{var level=s.overworld();for(var p:shafts){var entity=(BronzeEntity)level.getBlockEntity(p);if(entity.moving()||Math.abs(entity.angle(1)-entity.angle(0))>.001)throw new IllegalStateException("Stopped shaft still animates: "+p);}});
            // Add a new, reversed-placement section to an already rotating circuit.
            server.runOnServer(s->{var level=s.overworld();var source=sources.get(2);level.setBlockAndUpdate(source,level.getBlockState(source).setValue(ShaftRotationFixture.ACTIVE,true));var p=source.south(4);level.setBlockAndUpdate(p,BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,Direction.SOUTH));BronzeNetwork.invalidate(level);});context.waitTicks(5);
            capture(context,server,"shaft-live-join",15.5,101,2.5,new BlockPos(12,101,3));
            server.runOnServer(s->{var entity=(BronzeEntity)s.overworld().getBlockEntity(new BlockPos(12,101,2));entity.inspect(s.getPlayerList().getPlayers().getFirst());});context.waitTicks(2);context.takeScreenshot("shaft-status-simple");
            server.runOnServer(s->{var player=s.getPlayerList().getPlayers().getFirst();player.setShiftKeyDown(true);((BronzeEntity)s.overworld().getBlockEntity(new BlockPos(12,101,2))).inspect(player);player.setShiftKeyDown(false);});context.waitTicks(2);context.takeScreenshot("shaft-status-details");
        }
        if(!previous.equals("ru_ru"))CopperClientScene.language(context,previous);
    }
}
