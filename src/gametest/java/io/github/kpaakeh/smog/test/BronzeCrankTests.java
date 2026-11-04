package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.GameTestHelper;

public final class BronzeCrankTests {
    @GameTest public void fixedMountAndCollisionMatchTheRenderedAxleOnAllSixFaces(GameTestHelper h){
        var block=BronzeContent.entry("bronze_hand_crank").block();var p=h.absolutePos(new BlockPos(4,4,4));
        // Outer rim at the rear: it must touch the shaft side, not the handle side.
        for(var facing:Direction.values()){
            var shape=block.defaultBlockState().setValue(BronzeBlock.FACING,facing).getShape(h.getLevel(),p);
            for(var radial:Direction.values())if(radial.getAxis()!=facing.getAxis()){
                double x=.5-facing.getStepX()*.453125+radial.getStepX()*.21875,y=.5-facing.getStepY()*.453125+radial.getStepY()*.21875,z=.5-facing.getStepZ()*.453125+radial.getStepZ()*.21875;
                h.assertTrue(shape.toAabbs().stream().anyMatch(b->b.contains(x,y,z)),"The fixed mounting ring reaches the actual rear bearing on "+facing);
            }
        }h.succeed();
    }
    @GameTest public void twoOppositeHandlesDoNotBrakeEachOther(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,4,4));var handle=BronzeContent.entry("bronze_hand_crank").block();
        level.setBlockAndUpdate(p,BronzeContent.entry("bronze_shaft").block().defaultBlockState());level.setBlockAndUpdate(p.north(),handle.defaultBlockState());level.setBlockAndUpdate(p.south(),handle.defaultBlockState().setValue(BronzeBlock.FACING,Direction.SOUTH));
        var a=(BronzeEntity)level.getBlockEntity(p.north());var b=(BronzeEntity)level.getBlockEntity(p.south());
        a.crank();var first=BronzeNetwork.query(level,p);h.assertTrue(first.running()&&first.available()==4&&first.rate().equals(RotationRate.NOMINAL),"An idle second handle never brakes the one in use");
        b.crank();var both=BronzeNetwork.query(level,p);h.assertTrue(both.running()&&both.available()==8&&both.rate().equals(RotationRate.NOMINAL),"Forward input from either end agrees and has only paid power");
        b.crank(true);var opposing=BronzeNetwork.query(level,p);h.assertTrue(opposing.running()&&opposing.available()==4,"Contrary manual input freewheels instead of locking the network");h.succeed();
    }
    @GameTest public void shiftReversesTheWholeLineOnEveryPlacementFace(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,4,4));var handle=BronzeContent.entry("bronze_hand_crank").block();
        for(var facing:Direction.values()){
            var shaft=p.relative(facing.getOpposite());level.setBlockAndUpdate(p,handle.defaultBlockState().setValue(BronzeBlock.FACING,facing));level.setBlockAndUpdate(shaft,BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,BronzeShaft.canonical(facing)));
            var crank=(BronzeEntity)level.getBlockEntity(p);crank.crank();h.assertTrue(BronzeNetwork.query(level,shaft).rate().equals(RotationRate.NOMINAL),"Normal use means positive canonical shaft speed on "+facing);
            crank.crank(true);h.assertTrue(BronzeNetwork.query(level,shaft).rate().equals(RotationRate.NOMINAL.negate()),"Shift means the same magnitude in reverse on "+facing);
            level.setBlockAndUpdate(shaft,net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());level.setBlockAndUpdate(p,net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
        }h.succeed();
    }
    @GameTest(maxTicks=40) public void releaseHasOnlyAShortGracePeriodAndRefreshIsContinuous(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,4,4));level.setBlockAndUpdate(p,BronzeContent.entry("bronze_hand_crank").block().defaultBlockState());var crank=(BronzeEntity)level.getBlockEntity(p);crank.crank();
        h.runAfterDelay(4,()->{h.assertTrue(BronzeNetwork.query(level,p).running(),"Held input remains live between ordinary four-tick use repeats");crank.crank();});
        h.runAfterDelay(8,()->h.assertTrue(BronzeNetwork.query(level,p).running(),"Refresh keeps the handle turning continuously"));
        h.runAfterDelay(11,()->{var result=BronzeNetwork.query(level,p);h.assertTrue(!crank.cranking()&&!result.running()&&result.rate().equals(RotationRate.ZERO),"Released input stops within six ticks, not four seconds");h.succeed();});
    }
}
