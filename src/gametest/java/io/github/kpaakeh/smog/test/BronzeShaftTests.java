package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class BronzeShaftTests {
    private static void shaft(GameTestHelper h,BlockPos p,Direction face){h.getLevel().setBlockAndUpdate(p,BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,face));}
    private static void near(GameTestHelper h,double actual,double expected,String message){h.assertTrue(Math.abs(actual-expected)<.001,message+": "+actual+" != "+expected);}
    @GameTest public void ratesAreExactAndTempoChangesPreservePhase(GameTestHelper h){
        h.assertTrue(RotationRate.of(1,4).multiply(RotationRate.of(2,1)).equals(RotationRate.of(1,2)),"A quarter tempo is never rounded to zero");
        var timeline=new RotationTimeline(0,17);near(h,timeline.update(0,RotationRate.ONE),17,"Start keeps its phase");near(h,timeline.update(1,RotationRate.ONE),17.25,"Normal advance");near(h,timeline.update(2,RotationRate.of(2,1)),17.5,"A speed change has no phase jump");near(h,timeline.update(3,RotationRate.ZERO),18,"Stop keeps the last turning step");near(h,timeline.update(4,RotationRate.ZERO),18,"Stopped means stationary");near(h,timeline.update(1000,RotationRate.ONE),18,"Unloaded time produces no hidden turns");near(h,timeline.update(1001,RotationRate.of(-1,1)),18.25,"Reverse changes velocity, not position");near(h,timeline.update(1002,RotationRate.ZERO),18,"Reverse really moves in the opposite direction");h.succeed();
    }
    @GameTest public void sixConnectionDirectionsUseOnlyThreeCanonicalAxes(GameTestHelper h){
        var level=h.getLevel();var origin=h.absolutePos(new BlockPos(5,5,5));
        for(var face:Direction.values()){
            var out=face.getOpposite();level.getChunk(origin.relative(out,4));
            level.setBlockAndUpdate(origin,ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,face));
            for(int i=1;i<=3;i++)shaft(h,origin.relative(out,i),i%2==0?face:face.getOpposite());
            BronzeNetwork.invalidate(level);var expected=RotationRate.of(BronzeShaft.handedness(face)*16,1);
            for(int i=1;i<=3;i++){
                var pos=origin.relative(out,i);var result=BronzeNetwork.query(level,pos);h.assertTrue(result.running()&&result.rate().equals(expected),"A connected shaft transmits through "+out);
                var entity=(BronzeEntity)level.getBlockEntity(pos);entity.tick();near(h,entity.angle(0),result.phase(),"Every section starts on the same circuit phase");
                var canonical=BronzeShaft.canonical(face);h.assertTrue(canonical==BronzeShaft.canonical(face.getOpposite()),"Both ends share one model orientation");
                var shape=level.getBlockState(pos).getShape(level,pos);h.assertTrue(!shape.isEmpty(),"A shaft has a real narrow collision");
            }
            for(int i=0;i<=3;i++)level.setBlockAndUpdate(origin.relative(out,i),Blocks.AIR.defaultBlockState());BronzeNetwork.invalidate(level);
        }h.succeed();
    }
    @GameTest public void fullSpeedRangeAndReverseReachTheShaft(GameTestHelper h){
        var level=h.getLevel();var origin=h.absolutePos(new BlockPos(4,4,4));var pos=origin.south();shaft(h,pos,Direction.SOUTH);
        for(int tempo=0;tempo<ShaftRotationFixture.SPEEDS.length;tempo++)for(boolean reverse:new boolean[]{false,true}){
            level.setBlockAndUpdate(origin,ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.TEMPO,tempo).setValue(ShaftRotationFixture.REVERSE,reverse));BronzeNetwork.invalidate(level);
            var result=BronzeNetwork.query(level,pos);h.assertTrue(result.rate().equals(RotationRate.of((reverse?-1:1)*ShaftRotationFixture.SPEEDS[tempo],1)),"Actual physical source tempo reaches the shaft");
            var entity=(BronzeEntity)level.getBlockEntity(pos);entity.tick();near(h,Math.IEEEremainder(entity.angle(1)-entity.angle(0),360),result.rate().value()*RotationTimeline.DEGREES_PER_TICK,"Renderer interpolation uses actual signed speed");
        }
        level.setBlockAndUpdate(origin,level.getBlockState(origin).setValue(ShaftRotationFixture.ACTIVE,false));BronzeNetwork.invalidate(level);var entity=(BronzeEntity)level.getBlockEntity(pos);entity.tick();near(h,entity.angle(1),entity.angle(0),"Removing rotation freezes the model");h.succeed();
    }
    @GameTest public void joiningSectionSharesPhaseAndShaftHasOneNativeDrop(GameTestHelper h){
        var level=h.getLevel();var origin=h.absolutePos(new BlockPos(4,4,4));level.setBlockAndUpdate(origin,ShaftRotationFixture.SOURCE.defaultBlockState());shaft(h,origin.south(),Direction.NORTH);var first=(BronzeEntity)level.getBlockEntity(origin.south());first.tick();
        shaft(h,origin.south(2),Direction.SOUTH);BronzeNetwork.invalidate(level);var second=(BronzeEntity)level.getBlockEntity(origin.south(2));first.tick();second.tick();near(h,first.angle(.5f),second.angle(.5f),"An opposite-placed section joins with the same phase");
        var player=h.makeMockServerPlayerInLevel();var block=BronzeContent.entry("bronze_shaft").block();var drops=Block.getDrops(block.defaultBlockState(),level,origin.south(),first,player,new ItemStack(Items.IRON_PICKAXE));h.assertValueEqual(drops.stream().filter(s->s.is(block.asItem())).mapToInt(ItemStack::getCount).sum(),1,"A shaft drops its one actual block item");h.succeed();
    }
    @GameTest public void speedsOutsideOneToSixtyFourStopAtTheCircuitBoundary(GameTestHelper h){
        var level=h.getLevel();var source=h.absolutePos(new BlockPos(4,4,4));shaft(h,source.south(),Direction.NORTH);
        for(int fault=1;fault<=2;fault++){
            level.setBlockAndUpdate(source,ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FAULT,fault));BronzeNetwork.invalidate(level);var result=BronzeNetwork.query(level,source.south());
            h.assertTrue(result.status().equals(fault==1?"slow":"fast")&&!result.running(),"Out-of-range input stops without rounding or clamping into free work");
            var entity=(BronzeEntity)level.getBlockEntity(source.south());entity.tick();near(h,entity.angle(1),entity.angle(0),"Rejected speed cannot keep a shaft turning");
        }h.succeed();
    }
    @GameTest public void sweptCollisionContainsTheActualRotatingModel(GameTestHelper h) throws Exception {
        var state=BronzeContent.entry("bronze_shaft").block().defaultBlockState();var boxes=state.getShape(h.getLevel(),h.absolutePos(new BlockPos(3,3,3))).toAabbs();
        try(var stream=BronzeShaftTests.class.getResourceAsStream("/assets/smog/models/block/bronze_shaft_moving.json")){
            var model=com.google.gson.JsonParser.parseReader(new java.io.InputStreamReader(stream,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            for(var element:model.getAsJsonArray("elements")){
                var e=element.getAsJsonObject();var a=e.getAsJsonArray("from");var b=e.getAsJsonArray("to");
                for(int angle=0;angle<360;angle+=5)for(int ix=0;ix<2;ix++)for(int iy=0;iy<2;iy++)for(int iz=0;iz<2;iz++){
                    double vx=(ix==0?a:b).get(0).getAsDouble()-8,vy=(iy==0?a:b).get(1).getAsDouble()-8;
                    double radians=Math.toRadians(angle),x=(8+vx*Math.cos(radians)-vy*Math.sin(radians))/16,y=(8+vx*Math.sin(radians)+vy*Math.cos(radians))/16,z=(iz==0?a:b).get(2).getAsDouble()/16;
                    boolean inside=false;for(var box:boxes)if(x>=box.minX-1e-6&&x<=box.maxX+1e-6&&y>=box.minY-1e-6&&y<=box.maxY+1e-6&&z>=box.minZ-1e-6&&z<=box.maxZ+1e-6){inside=true;break;}
                    h.assertTrue(inside,"A rotating model vertex is outside its collision at "+angle);
                }
            }
        }
        h.assertTrue(state.getShape(h.getLevel(),h.absolutePos(new BlockPos(3,3,3))).bounds().getXsize()<1,"The safe motion envelope is still narrower than a cube");h.succeed();
    }
    @GameTest public void loadedShaftDoesNotInventOfflineMotion(GameTestHelper h){
        var level=h.getLevel();var pos=h.absolutePos(new BlockPos(4,4,4));shaft(h,pos,Direction.NORTH);var entity=(BronzeEntity)level.getBlockEntity(pos);
        var tag=entity.saveWithFullMetadata(level.registryAccess());tag.putFloat("phase",137);tag.putFloat("speed",64);tag.putLong("phase_tick",level.getGameTime()-500000);
        var copy=(BronzeEntity)net.minecraft.world.level.block.entity.BlockEntity.loadStatic(pos,entity.getBlockState(),tag,level.registryAccess());copy.setLevel(level);
        near(h,copy.angle(0),137,"Loading keeps the stored physical position instead of replaying unloaded time");h.assertFalse(copy.moving(),"A persisted speed is not proof that the newly loaded circuit is powered");h.succeed();
    }
}
