package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;

public final class BronzeSupportTests {
    @GameTest public void fixedBearingLeavesClearanceForEveryShaftAngle(GameTestHelper h) throws Exception {
        try(var stream=BronzeSupportTests.class.getResourceAsStream("/assets/smog/models/block/bronze_shaft_support.json")){
            var root=com.google.gson.JsonParser.parseReader(new java.io.InputStreamReader(stream,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            for(var element:root.getAsJsonArray("elements")){
                var e=element.getAsJsonObject();var a=e.getAsJsonArray("from");var b=e.getAsJsonArray("to");
                double nearX=Math.max(0,Math.max(a.get(0).getAsDouble()-8,8-b.get(0).getAsDouble()));
                double nearY=Math.max(0,Math.max(a.get(1).getAsDouble()-8,8-b.get(1).getAsDouble()));
                h.assertTrue(Math.hypot(nearX,nearY)>Math.sqrt(8)+.1,"Stationary bearing faces cannot scrape the corners of the rotating square shaft");
                h.assertTrue(a.get(2).getAsDouble()>1.5&&b.get(2).getAsDouble()<14.5,"Bearing leaves the rotating end collars unobstructed");
            }
        }h.succeed();
    }
    @GameTest public void twelveMountedAxesCarryActualRotationAndKeepTheShaftCentred(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(5,5,5));var support=BronzeContent.entry("bronze_shaft_support").block();
        for(var axis:java.util.List.of(Direction.EAST,Direction.UP,Direction.NORTH))for(var mount:Direction.values()){
            if(axis.getAxis()==mount.getAxis())continue;
            var anchor=p.relative(mount);level.setBlockAndUpdate(anchor,Blocks.STONE.defaultBlockState());
            var state=support.defaultBlockState().setValue(BronzeBlock.FACING,axis).setValue(BronzeSupportBlock.MOUNT,mount);level.setBlockAndUpdate(p,state);
            var source=p.relative(axis);level.setBlockAndUpdate(source,ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,axis));
            var next=p.relative(axis.getOpposite());level.setBlockAndUpdate(next,BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,axis));
            BronzeNetwork.invalidate(level);h.assertTrue(BronzeSupportBlock.mounted(state,level,p),"Foot really touches its mount "+axis+"/"+mount);
            var result=BronzeNetwork.query(level,next);h.assertTrue(result.running()&&Math.abs(result.rate().value())==16,"Bearing transmits without a tempo or power loss");
            var shape=state.getShape(level,p);h.assertTrue(shape.bounds().getSize()<1,"Bearing remains a narrow shape");
            for(double offset:new double[]{-.49,0,.49}){double x=.5,y=.5,z=.5;switch(axis.getAxis()){case X->x+=offset;case Y->y+=offset;case Z->z+=offset;}
                final double xx=x,yy=y,zz=z;h.assertTrue(shape.toAabbs().stream().anyMatch(b->b.contains(xx,yy,zz)),"Shaft centre is continuous through every mount");}
            level.setBlockAndUpdate(source,Blocks.AIR.defaultBlockState());level.setBlockAndUpdate(next,Blocks.AIR.defaultBlockState());level.setBlockAndUpdate(p,Blocks.AIR.defaultBlockState());level.setBlockAndUpdate(anchor,Blocks.AIR.defaultBlockState());
        }h.succeed();
    }
    @GameTest public void placementUsesTheClickedMountAndFindsAnAdjacentShaft(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(5,5,5));var support=BronzeContent.entry("bronze_shaft_support").block();var player=h.makeMockServerPlayerInLevel();
        for(var mount:Direction.values())for(var axis:java.util.List.of(Direction.EAST,Direction.UP,Direction.NORTH)){
            if(axis.getAxis()==mount.getAxis())continue;var foot=p.relative(mount);level.setBlockAndUpdate(foot,Blocks.STONE.defaultBlockState());var neighbor=p.relative(axis);
            level.setBlockAndUpdate(neighbor,BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,axis));
            var context=new BlockPlaceContext(level,player,net.minecraft.world.InteractionHand.MAIN_HAND,new ItemStack(support),new BlockHitResult(Vec3.atCenterOf(foot).relative(mount.getOpposite(),.5),mount.getOpposite(),foot,false));
            var placed=support.getStateForPlacement(context);h.assertTrue(placed!=null&&placed.getValue(BronzeSupportBlock.MOUNT)==mount&&placed.getValue(BronzeBlock.FACING).getAxis()==axis.getAxis(),"Clicking a surface mounts the foot and aligns the existing shaft");
            level.setBlockAndUpdate(neighbor,Blocks.AIR.defaultBlockState());level.setBlockAndUpdate(foot,Blocks.AIR.defaultBlockState());
        }h.succeed();
    }
    @GameTest public void floatingBearingCannotRelayAndLosingItsMountRemovesOnlyTheBearing(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,5,4));var support=BronzeContent.entry("bronze_shaft_support").block();var state=support.defaultBlockState();
        level.setBlockAndUpdate(p,state);h.assertTrue(BronzeNetwork.ports(level,p,state).isEmpty(),"A command-placed floating bearing cannot cheat the support rule");
        level.setBlockAndUpdate(p.below(),Blocks.STONE.defaultBlockState());level.setBlockAndUpdate(p,state);var shaft=p.south();level.setBlockAndUpdate(shaft,BronzeContent.entry("bronze_shaft").block().defaultBlockState());
        level.setBlockAndUpdate(p.below(),Blocks.AIR.defaultBlockState());h.assertTrue(level.getBlockState(p).isAir(),"Removing the foot's support drops the bearing");h.assertTrue(level.getBlockState(shaft).is(BronzeContent.entry("bronze_shaft").block()),"The neighboring paid shaft is preserved");h.succeed();
    }
    @GameTest public void rotatedAndMirroredMountsPreservePerpendicularAxes(GameTestHelper h){
        var block=BronzeContent.entry("bronze_shaft_support").block();
        for(var axis:java.util.List.of(Direction.EAST,Direction.UP,Direction.NORTH))for(var mount:Direction.values())if(axis.getAxis()!=mount.getAxis()){
            var original=block.defaultBlockState().setValue(BronzeBlock.FACING,axis).setValue(BronzeSupportBlock.MOUNT,mount);
            var rotated=original;for(int i=0;i<4;i++)rotated=rotated.rotate(Rotation.CLOCKWISE_90);h.assertTrue(rotated.equals(original),"Four structure turns restore the exact mount");
            for(var mirror:Mirror.values()){var mirrored=original.mirror(mirror);h.assertTrue(mirrored.getValue(BronzeBlock.FACING).getAxis()!=mirrored.getValue(BronzeSupportBlock.MOUNT).getAxis(),"Structure mirroring preserves a real perpendicular foot");h.assertTrue(mirrored.mirror(mirror).equals(original),"Mirroring twice restores the state");}
        }h.succeed();
    }
}
