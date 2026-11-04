package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.world.level.block.*;

public final class BronzeSupportClientScene {
    public static void run(ClientGameTestContext context){
        var previous=CopperClientScene.language(context,"ru_ru");context.runOnClient(mc->{mc.getWindow().setWindowed(1280,960);mc.options.guiScale().set(2);mc.options.fov().set(60);mc.resizeGui();});
        try(var world=context.worldBuilder().create()){
            var server=world.getServer();server.runCommand("fill -8 96 -8 60 96 20 minecraft:stone");server.runCommand("time set noon");server.runCommand("weather clear");
            var cells=new java.util.ArrayList<BlockPos>();var labels=new java.util.ArrayList<String>();
            server.runOnServer(s->{var level=s.overworld();var player=s.getPlayerList().getPlayers().getFirst();player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);player.setNoGravity(true);player.getAbilities().flying=true;player.onUpdateAbilities();int i=0;
                for(var axis:java.util.List.of(Direction.NORTH,Direction.EAST,Direction.UP))for(var mount:Direction.values())if(axis.getAxis()!=mount.getAxis()){
                    var p=new BlockPos(i++*5,101,0);cells.add(p);labels.add(axis.getAxis().getName()+"-foot-"+mount.getName());level.setBlockAndUpdate(p.relative(mount),Blocks.STONE.defaultBlockState());
                    level.setBlockAndUpdate(p,BronzeContent.entry("bronze_shaft_support").block().defaultBlockState().setValue(BronzeBlock.FACING,axis).setValue(BronzeSupportBlock.MOUNT,mount));
                    for(var side:new Direction[]{axis,axis.getOpposite()})level.setBlockAndUpdate(p.relative(side),BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,axis));
                    level.setBlockAndUpdate(p.relative(axis,2),ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,axis).setValue(ShaftRotationFixture.TEMPO,i%6));
                }BronzeNetwork.invalidate(level);
            });context.waitTicks(30);
            for(int i=0;i<cells.size();i++){
                var p=cells.get(i);var label=labels.get(i);var axis=label.startsWith("z")?Direction.NORTH:label.startsWith("x")?Direction.EAST:Direction.UP;
                double dx=axis.getAxis()==Direction.Axis.X?1.5:3,dz=axis.getAxis()==Direction.Axis.X?-3:1.8;
                var mount=Direction.byName(label.substring(label.indexOf("-foot-")+6));
                if(mount.getAxis()==Direction.Axis.X)dx=-mount.getStepX()*Math.abs(dx);
                if(mount.getAxis()==Direction.Axis.Z)dz=-mount.getStepZ()*Math.abs(dz);
                server.runCommand("tp @a "+(p.getX()+dx)+" "+(p.getY()+.5)+" "+(p.getZ()+dz)+" 0 0");context.waitTicks(20);context.getInput().lookAt(p);context.waitTicks(15);context.takeScreenshot("support-"+label);
            }
            server.runOnServer(s->{for(var p:cells){var entity=(BronzeEntity)s.overworld().getBlockEntity(p);if(!entity.moving())throw new IllegalStateException("Mounted support did not rotate: "+p);}});
            var p=cells.getFirst();server.runOnServer(s->{var level=s.overworld();var source=p.north(2);level.setBlockAndUpdate(source,level.getBlockState(source).setValue(ShaftRotationFixture.ACTIVE,false));BronzeNetwork.invalidate(level);});context.waitTicks(5);
            server.runCommand("tp @a 3 101.5 1.8 0 0");context.waitTicks(20);context.getInput().lookAt(p);context.waitTicks(15);context.takeScreenshot("support-stopped");context.waitTicks(15);context.takeScreenshot("support-stopped-later");
        }if(!previous.equals("ru_ru"))CopperClientScene.language(context,previous);
    }
}
