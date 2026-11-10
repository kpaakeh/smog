package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;

public final class BronzeSplitterTests {
    @GameTest public void allOrientationsAndBranchMasksMatchTheirPortsAndCollisions(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,4,4));var block=BronzeContent.entry("bronze_splitter").block();
        for(var facing:Direction.values())for(var side:Direction.values())if(facing.getAxis()!=side.getAxis()){
            var base=block.defaultBlockState().setValue(BronzeBlock.FACING,facing).setValue(BronzeAngleBlock.OUTPUT,side);var outlets=new Direction[]{facing,side,side.getOpposite()};var controls=new net.minecraft.world.level.block.state.properties.BooleanProperty[]{BronzeSplitterBlock.FRONT,BronzeSplitterBlock.RIGHT,BronzeSplitterBlock.LEFT};
            var input=facing.getOpposite();level.setBlockAndUpdate(p.relative(input),BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,BronzeShaft.canonical(facing)));level.setBlockAndUpdate(p.relative(input,2),ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,input).setValue(ShaftRotationFixture.REVERSE,true));
            for(var out:outlets)level.setBlockAndUpdate(p.relative(out),BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,BronzeShaft.canonical(out)));
            for(int mask=0;mask<8;mask++){
                var state=base;for(int i=0;i<3;i++)state=state.setValue(controls[i],(mask&(1<<i))!=0);level.setBlockAndUpdate(p,state);BronzeNetwork.invalidate(level);
                h.assertTrue(BronzeNetwork.query(level,p).running(),"Disengaged outputs never brake the input");var shape=state.getShape(level,p);
                if(facing==Direction.NORTH&&side==Direction.EAST&&mask==0)shape.forAllBoxes((a,b,c,d,e,f)->{for(double value:new double[]{a,b,c,d,e,f})h.assertTrue(Math.abs(value*64-Math.rint(value*64))<1e-6,"Collision coordinates stay on the small native voxel grid");});
                for(int i=0;i<3;i++){
                    var out=outlets[i];boolean enabled=(mask&(1<<i))!=0;h.assertTrue(BronzeNetwork.ports(level,p,state).contains(out)==enabled,"Port follows its own setting");var result=BronzeNetwork.query(level,p.relative(out));
                    h.assertTrue(enabled?result.rate().equals(RotationRate.of(16*BronzeShaft.handedness(out),1)):!result.running(),"Every output follows its visible rotor direction");
                    double x=.5+out.getStepX()*5./16,y=.5+out.getStepY()*5./16,z=.5+out.getStepZ()*5./16;h.assertTrue(shape.toAabbs().stream().anyMatch(b->b.contains(x,y,z))==enabled,"Retracting a shaft removes its real collision, not just its model");
                }
            }
            for(var out:outlets)level.setBlockAndUpdate(p.relative(out),Blocks.AIR.defaultBlockState());for(var pos:java.util.List.of(p,p.relative(input),p.relative(input,2)))level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
        }h.succeed();
    }
    private static void configure(GameTestHelper h,BlockPos pos,Direction face){
        var player=h.makeMockServerPlayerInLevel();var stack=new ItemStack(BuiltInRegistries.ITEM.getValue(ModBlocks.id("configurator")));player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,stack);player.setShiftKeyDown(true);
        player.gameMode.useItemOn(player,h.getLevel(),stack,net.minecraft.world.InteractionHand.MAIN_HAND,new BlockHitResult(Vec3.atCenterOf(pos).relative(face,.5),face,pos,false));
    }
    @GameTest public void realConfiguratorClicksToggleBranchesAndFreeSharedPower(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,4,4));level.setBlockAndUpdate(p,BronzeContent.entry("bronze_splitter").block().defaultBlockState());level.setBlockAndUpdate(p.south(),BronzeContent.entry("bronze_hand_crank").block().defaultBlockState().setValue(BronzeBlock.FACING,Direction.SOUTH));
        for(var out:java.util.List.of(Direction.NORTH,Direction.EAST,Direction.WEST)){
            level.setBlockAndUpdate(p.relative(out),BronzeContent.entry("bronze_drive_coupler").block().defaultBlockState().setValue(BronzeBlock.FACING,out));level.setBlockAndUpdate(p.relative(out,2),BuiltInRegistries.BLOCK.getValue(ModBlocks.id("hand_bellows")).defaultBlockState());
        }
        ((BronzeEntity)level.getBlockEntity(p.south())).crank();h.assertTrue(BronzeNetwork.query(level,p).status().equals("overload"),"Three 2P loads cannot use one 4P handle");
        configure(h,p,Direction.WEST);var state=level.getBlockState(p);h.assertTrue(!state.getValue(BronzeSplitterBlock.LEFT)&&state.getValue(BronzeSplitterBlock.RIGHT)&&state.getValue(BronzeSplitterBlock.FRONT),"A real crouched item click changes only the clicked branch");
        var result=BronzeNetwork.query(level,p);h.assertTrue(result.running()&&result.required()==4&&result.available()==4,"Isolating one load restores the remaining branches without extra power");
        configure(h,p,Direction.WEST);h.assertTrue(level.getBlockState(p).getValue(BronzeSplitterBlock.LEFT)&&BronzeNetwork.query(level,p).status().equals("overload"),"The same click reconnects the load");h.succeed();
    }
    @GameTest public void crouchedConfiguratorStillRotatesShaftsAndAngleOutputs(GameTestHelper h){
        var p=h.absolutePos(new BlockPos(4,4,4));var level=h.getLevel();level.setBlockAndUpdate(p,BronzeContent.entry("bronze_shaft").block().defaultBlockState());configure(h,p,Direction.NORTH);h.assertTrue(level.getBlockState(p).getValue(BronzeBlock.FACING)==Direction.EAST,"Native item use reaches the shaft's three-axis control");
        level.setBlockAndUpdate(p,BronzeContent.entry("bronze_angle_gearbox").block().defaultBlockState());configure(h,p,Direction.NORTH);h.assertTrue(BronzeAngleBlock.output(level.getBlockState(p))!=Direction.EAST,"Native item use reaches angle-plane configuration");h.succeed();
    }
    @GameTest public void transformsPreserveTheSelectedPhysicalBranches(GameTestHelper h){
        var block=BronzeContent.entry("bronze_splitter").block();
        for(var facing:Direction.values())for(var side:Direction.values())if(facing.getAxis()!=side.getAxis())for(int mask=0;mask<8;mask++){
            var state=block.defaultBlockState().setValue(BronzeBlock.FACING,facing).setValue(BronzeAngleBlock.OUTPUT,side).setValue(BronzeSplitterBlock.FRONT,(mask&1)!=0).setValue(BronzeSplitterBlock.RIGHT,(mask&2)!=0).setValue(BronzeSplitterBlock.LEFT,(mask&4)!=0);
            var rotated=state;for(int i=0;i<4;i++)rotated=rotated.rotate(Rotation.CLOCKWISE_90);h.assertTrue(rotated.equals(state),"Four turns retain orientation and branch settings");
            for(var mirror:Mirror.values())h.assertTrue(state.mirror(mirror).mirror(mirror).equals(state),"Mirroring twice retains branch settings");
        }h.succeed();
    }
}
