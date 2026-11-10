package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;

public final class BronzeClutchTests {
    @GameTest public void everyAxisAndSettingMatchesThePhysicalGap(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,4,4));
        for(var face:Direction.values())for(boolean opened:new boolean[]{true,false})for(boolean powered:new boolean[]{true,false}){
            var state=BronzeContent.entry("bronze_clutch").block().defaultBlockState().setValue(BronzeBlock.FACING,face).setValue(BronzeBlock.OPEN,opened).setValue(BronzeClutchBlock.POWERED,powered);level.setBlockAndUpdate(p,state);
            h.assertTrue(BronzeKinematics.port(state,face).equals(RotationRate.of(-BronzeShaft.handedness(face),1)),"Legacy opposite facings use the same physical rotor as canonical placements");
            boolean engaged=opened&&!powered;h.assertTrue(BronzeClutchBlock.engaged(state)==engaged,"Redstone overrides the manual setting");
            h.assertTrue(state.getShape(level,p).toAabbs().stream().anyMatch(box->box.contains(.5,.5,.5))==engaged,"The centre is physically empty exactly when disconnected");
            if(!powered)h.assertTrue(BronzeNetwork.ports(level,p,state).size()==(opened?2:0),"Manual opening disconnects both physical ports");
        }h.succeed();
    }
    @GameTest public void realRedstonePreservesManualChoice(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,4,4));var block=BronzeContent.entry("bronze_clutch").block();level.setBlockAndUpdate(p,block.defaultBlockState());
        level.setBlockAndUpdate(p.east(),Blocks.REDSTONE_BLOCK.defaultBlockState());h.assertTrue(level.getBlockState(p).getValue(BronzeClutchBlock.POWERED)&&BronzeNetwork.ports(level,p,level.getBlockState(p)).isEmpty(),"Actual redstone disconnects and updates the visible state");
        level.setBlockAndUpdate(p.east(),Blocks.AIR.defaultBlockState());h.assertTrue(BronzeClutchBlock.engaged(level.getBlockState(p)),"Removing signal restores an enabled manual setting");
        level.setBlockAndUpdate(p,level.getBlockState(p).setValue(BronzeBlock.OPEN,false));level.setBlockAndUpdate(p.east(),Blocks.REDSTONE_BLOCK.defaultBlockState());level.setBlockAndUpdate(p.east(),Blocks.AIR.defaultBlockState());h.assertTrue(!BronzeClutchBlock.engaged(level.getBlockState(p)),"Removing signal cannot erase manual disconnection");h.succeed();
    }
    @GameTest public void couplingActuallySeparatesTheDrivenNetwork(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,4,4));level.setBlockAndUpdate(p,BronzeContent.entry("bronze_clutch").block().defaultBlockState());level.setBlockAndUpdate(p.south(),BronzeContent.entry("bronze_hand_crank").block().defaultBlockState().setValue(BronzeBlock.FACING,Direction.SOUTH));level.setBlockAndUpdate(p.north(),BronzeContent.entry("bronze_shaft").block().defaultBlockState());
        ((BronzeEntity)level.getBlockEntity(p.south())).crank();h.assertTrue(BronzeNetwork.query(level,p.north()).running(),"Closed coupling transmits the actual source");
        level.setBlockAndUpdate(p,level.getBlockState(p).setValue(BronzeBlock.OPEN,false));BronzeNetwork.invalidate(level);h.assertTrue(!BronzeNetwork.query(level,p.north()).running()&&BronzeNetwork.query(level,p.south()).running(),"Disconnecting frees the input and stops the isolated output");h.succeed();
    }
    @GameTest public void safetyTripsOnOverloadAndRequiresARealManualReset(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,4,4));var splitter=p.north();level.setBlockAndUpdate(p,BronzeContent.entry("bronze_safety_clutch").block().defaultBlockState());level.setBlockAndUpdate(splitter,BronzeContent.entry("bronze_splitter").block().defaultBlockState());level.setBlockAndUpdate(p.south(),BronzeContent.entry("bronze_hand_crank").block().defaultBlockState().setValue(BronzeBlock.FACING,Direction.SOUTH));
        for(var out:java.util.List.of(Direction.NORTH,Direction.EAST,Direction.WEST)){
            level.setBlockAndUpdate(splitter.relative(out),BronzeContent.entry("bronze_drive_coupler").block().defaultBlockState().setValue(BronzeBlock.FACING,out));level.setBlockAndUpdate(splitter.relative(out,2),net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(io.github.kpaakeh.smog.registry.ModBlocks.id("hand_bellows")).defaultBlockState());
        }
        var handle=(BronzeEntity)level.getBlockEntity(p.south());handle.crank();h.assertTrue(BronzeNetwork.query(level,p).status().equals("overload"),"The real 6P load overloads a 4P handle");((BronzeEntity)level.getBlockEntity(p)).tick();h.assertTrue(!level.getBlockState(p).getValue(BronzeBlock.OPEN),"Protection latches open instead of braking the source forever");h.assertTrue(BronzeNetwork.query(level,p.south()).running(),"Tripping frees the source");
        level.setBlockAndUpdate(splitter.west(2),Blocks.AIR.defaultBlockState());((BronzeEntity)level.getBlockEntity(p)).tick();h.assertTrue(!level.getBlockState(p).getValue(BronzeBlock.OPEN),"Removing overload alone cannot reset protection");
        var player=h.makeMockServerPlayerInLevel();player.gameMode.useItemOn(player,level,net.minecraft.world.item.ItemStack.EMPTY,net.minecraft.world.InteractionHand.MAIN_HAND,new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(p),Direction.UP,p,false));
        h.assertTrue(level.getBlockState(p).getValue(BronzeBlock.OPEN)&&BronzeNetwork.query(level,p).running(),"A real hand click restores the now-safe 4P circuit");h.succeed();
    }
    @GameTest public void couplerFeedsOnlyItsFrontMachineInEveryOrientation(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,4,4));
        for(var face:Direction.values()){
            level.setBlockAndUpdate(p,BronzeContent.entry("bronze_drive_coupler").block().defaultBlockState().setValue(BronzeBlock.FACING,face));level.setBlockAndUpdate(p.relative(face.getOpposite()),BronzeContent.entry("bronze_hand_crank").block().defaultBlockState().setValue(BronzeBlock.FACING,face.getOpposite()));
            level.setBlockAndUpdate(p.relative(face),net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(io.github.kpaakeh.smog.registry.ModBlocks.id("hand_bellows")).defaultBlockState());((BronzeEntity)level.getBlockEntity(p.relative(face.getOpposite()))).crank();
            var clicked=p.relative(face);var coupler=BronzeContent.entry("bronze_drive_coupler").block();var player=h.makeMockServerPlayerInLevel();var context=new net.minecraft.world.item.context.BlockPlaceContext(level,player,net.minecraft.world.InteractionHand.MAIN_HAND,new net.minecraft.world.item.ItemStack(coupler),new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(clicked).relative(face.getOpposite(),.5),face.getOpposite(),clicked,false));h.assertTrue(coupler.getStateForPlacement(context).getValue(BronzeBlock.FACING)==face,"Placement puts the flange against the clicked mechanism on every face");
            var result=BronzeNetwork.query(level,p);h.assertTrue(result.running()&&result.required()==2&&result.consumers().containsKey(p.relative(face)),"The front accepts a real mechanism at its actual 2P load");h.assertTrue(BronzeNetwork.ports(level,p,level.getBlockState(p)).equals(java.util.Set.of(face.getOpposite())),"Only the rear is a shaft socket");
            for(var pos:java.util.List.of(p,p.relative(face),p.relative(face.getOpposite())))level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());BronzeNetwork.invalidate(level);
        }h.succeed();
    }
}
