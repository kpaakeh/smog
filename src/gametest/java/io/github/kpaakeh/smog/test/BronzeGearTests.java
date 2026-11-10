package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class BronzeGearTests {
    @GameTest public void placingBesideTeethAutomaticallyAlignsTheWheelAxis(GameTestHelper h){
        var p=h.absolutePos(new BlockPos(4,4,3));var player=h.makeMockServerPlayerInLevel();
        for(var axis:java.util.List.of(Direction.NORTH,Direction.UP)){
            block(h,p,"bronze_reduction_gearbox",axis);var item=new ItemStack(BronzeContent.entry("bronze_cogwheel").block());
            var context=new net.minecraft.world.item.context.BlockPlaceContext(h.getLevel(),player,net.minecraft.world.InteractionHand.MAIN_HAND,item,new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(p).relative(Direction.EAST,.5),Direction.EAST,p,false));
            var state=BronzeContent.entry("bronze_cogwheel").block().getStateForPlacement(context);h.assertTrue(state!=null&&state.getValue(BronzeBlock.FACING).getAxis()==axis.getAxis(),"Clicking teeth aligns their axes instead of pointing a shaft at the rim");
        }h.succeed();
    }
    @GameTest public void slowGearsDoNotJumpWhenTheDriverCompletesARevolution(GameTestHelper h){
        var timeline=new RotationTimeline(0,0,5760);timeline.update(0,RotationRate.NOMINAL);for(int i=1;i<=91;i++)timeline.update(i,RotationRate.NOMINAL);
        h.assertTrue(Math.abs(timeline.update(91,RotationRate.NOMINAL)/16-22.75)<.001,"A 1:16 follower retains its fraction of a revolution across a full driver turn");h.succeed();
    }
    private static void block(GameTestHelper h,BlockPos p,String id,Direction axis){h.getLevel().setBlockAndUpdate(p,BronzeContent.entry(id).block().defaultBlockState().setValue(BronzeBlock.FACING,axis));}
    private static void source(GameTestHelper h,BlockPos p,Direction axis){block(h,p.relative(axis),"bronze_shaft",axis);h.getLevel().setBlockAndUpdate(p.relative(axis,2),ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,axis));BronzeNetwork.invalidate(h.getLevel());}
    @GameTest public void teethControlBothRatiosAndReverseOnEveryAxis(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(6,6,6));
        for(var axis:java.util.List.of(Direction.NORTH,Direction.EAST,Direction.UP)){
            var side=axis.getAxis()==Direction.Axis.X?Direction.SOUTH:Direction.EAST;var small=p.relative(side);
            block(h,p,"bronze_reduction_gearbox",axis);block(h,small,"bronze_cogwheel",axis);source(h,p,axis);
            var out=BronzeNetwork.query(level,small);h.assertTrue(out.running()&&out.rate().equals(RotationRate.of(-32,1)),"24 driving 12 teeth doubles speed and reverses on "+axis);
            level.setBlockAndUpdate(p.relative(axis,2),Blocks.AIR.defaultBlockState());level.setBlockAndUpdate(p.relative(axis),Blocks.AIR.defaultBlockState());source(h,small,axis);
            out=BronzeNetwork.query(level,p);h.assertTrue(out.running()&&out.rate().equals(RotationRate.of(-8,1)),"12 driving 24 teeth halves speed and reverses on "+axis);
            for(var pos:java.util.List.of(p,small,small.relative(axis),small.relative(axis,2)))level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());BronzeNetwork.invalidate(level);
        }h.succeed();
    }
    @GameTest public void obstructionStopsWithoutDestroyingPaidParts(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(6,6,6));block(h,p,"bronze_reduction_gearbox",Direction.NORTH);source(h,p,Direction.NORTH);
        level.setBlockAndUpdate(p.east(),Blocks.STONE.defaultBlockState());BronzeNetwork.invalidate(level);h.assertTrue(BronzeNetwork.query(level,p).status().equals("blocked"),"The large wheel needs physical room outside its cell");
        level.setBlockAndUpdate(p.east(),Blocks.AIR.defaultBlockState());BronzeNetwork.invalidate(level);h.assertTrue(BronzeNetwork.query(level,p).running(),"Clearing the obstruction restores rotation");h.succeed();
    }
    @GameTest public void aRotatingWheelDoesNotReplaceAMountedSupport(GameTestHelper h){
        var p=h.absolutePos(new BlockPos(5,4,1));var level=h.getLevel();level.setBlockAndUpdate(p.north(),ShaftRotationFixture.SOURCE.defaultBlockState());
        for(int i=0;i<9;i++)block(h,p.south(i),i==3?"bronze_reduction_gearbox":"bronze_shaft",Direction.NORTH);
        BronzeNetwork.invalidate(level);h.assertTrue(BronzeNetwork.query(level,p).status().equals("support"),"A bare wheel cannot anchor nine unsupported shaft cells");
        level.setBlockAndUpdate(p.south(5).below(),Blocks.STONE.defaultBlockState());block(h,p.south(5),"bronze_shaft_support",Direction.NORTH);BronzeNetwork.invalidate(level);h.assertTrue(BronzeNetwork.query(level,p).running(),"A real mounted bearing restores the line");h.succeed();
    }
    @GameTest public void closedGearLoopHasOneSourceBudget(GameTestHelper h){
        var p=h.absolutePos(new BlockPos(5,4,3));for(int z=0;z<2;z++){block(h,p.south(z),"bronze_reduction_gearbox",Direction.NORTH);block(h,p.east().south(z),"bronze_cogwheel",Direction.NORTH);}source(h,p,Direction.NORTH);
        for(var pos:java.util.List.of(p,p.east(),p.south(),p.east().south())){var result=BronzeNetwork.query(h.getLevel(),pos);h.assertTrue(result.running()&&result.available()==16,"A physically consistent closed line never duplicates source power");}h.succeed();
    }
    @GameTest public void gearsRespectBothSpeedBoundsAndCompatibleSources(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(6,6,6));block(h,p,"bronze_reduction_gearbox",Direction.NORTH);block(h,p.east(),"bronze_cogwheel",Direction.NORTH);source(h,p,Direction.NORTH);
        var inlet=p.north(2);level.setBlockAndUpdate(inlet,level.getBlockState(inlet).setValue(ShaftRotationFixture.TEMPO,ShaftRotationFixture.SPEEDS.length-1));BronzeNetwork.invalidate(level);h.assertTrue(BronzeNetwork.query(level,p).status().equals("fast"),"A large gear cannot accelerate past 512");
        level.setBlockAndUpdate(inlet,Blocks.AIR.defaultBlockState());level.setBlockAndUpdate(p.north(),Blocks.AIR.defaultBlockState());source(h,p.east(),Direction.NORTH);level.setBlockAndUpdate(p.east().north(2),level.getBlockState(p.east().north(2)).setValue(ShaftRotationFixture.TEMPO,0));BronzeNetwork.invalidate(level);h.assertTrue(BronzeNetwork.query(level,p).status().equals("slow"),"A small gear cannot reduce below 1");
        source(h,p,Direction.NORTH);var other=p.east().north(2);level.setBlockAndUpdate(other,level.getBlockState(other).setValue(ShaftRotationFixture.TEMPO,4).setValue(ShaftRotationFixture.REVERSE,true));BronzeNetwork.invalidate(level);var result=BronzeNetwork.query(level,p);h.assertTrue(result.running()&&result.available()==32,"Compatible sources combine across the tooth ratio without duplicating power");
        level.setBlockAndUpdate(other,level.getBlockState(other).setValue(ShaftRotationFixture.REVERSE,false));BronzeNetwork.invalidate(level);h.assertTrue(BronzeNetwork.query(level,p).status().equals("direction"),"Opposing sources cannot perform free work");h.succeed();
    }
    private static MachineEntity press(GameTestHelper h,BlockPos pos){
        var level=h.getLevel();io.github.kpaakeh.smog.multiblock.Multiblocks.get(ModBlocks.id("lever_press")).parts().forEach((offset,part)->level.setBlockAndUpdate(pos.offset(offset),part.block().defaultBlockState()));
        level.setBlockAndUpdate(pos,net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(ModBlocks.id("lever_press")).defaultBlockState());var m=(MachineEntity)level.getBlockEntity(pos);h.assertTrue(m.tryAssemble(),"Physical press assembled");m.setItem(0,new ItemStack(Items.WHEAT_SEEDS,8));return m;
    }
    @GameTest public void slowerPressTakesTwiceTheTimeAndHalfThePower(GameTestHelper h){
        var p=h.absolutePos(new BlockPos(5,4,3));var m=press(h,p.south(4));block(h,p,"bronze_reduction_gearbox",Direction.NORTH);block(h,p.east(),"bronze_cogwheel",Direction.NORTH);block(h,p.south(),"bronze_shaft",Direction.NORTH);block(h,p.south(2),"bronze_shaft",Direction.NORTH);block(h,p.south(3),"bronze_drive_coupler",Direction.SOUTH);source(h,p.east(),Direction.NORTH);
        var result=BronzeNetwork.query(h.getLevel(),p);h.assertTrue(result.running()&&result.requiredExact().equals(ExactFraction.of(2)),"Half-speed press asks for 2P: "+result);
        for(int i=0;i<199;i++)ManualWorkshops.tick(h.getLevel(),m);h.assertTrue(m.getItem(3).isEmpty(),"Slow gearing cannot finish in normal time");ManualWorkshops.tick(h.getLevel(),m);h.assertTrue(m.getItem(0).isEmpty()&&m.getItem(3).getCount()==1,"Exactly one paid batch finishes at 200 ticks");h.succeed();
    }
    @GameTest public void fasterPressUsesTwiceThePowerAndStopsWhenUnderpowered(GameTestHelper h){
        var p=h.absolutePos(new BlockPos(5,4,3));var m=press(h,p.east().south(4));block(h,p,"bronze_reduction_gearbox",Direction.NORTH);block(h,p.east(),"bronze_cogwheel",Direction.NORTH);block(h,p.east().south(),"bronze_shaft",Direction.NORTH);block(h,p.east().south(2),"bronze_shaft",Direction.NORTH);block(h,p.east().south(3),"bronze_drive_coupler",Direction.SOUTH);source(h,p,Direction.NORTH);
        var result=BronzeNetwork.query(h.getLevel(),p);h.assertTrue(result.running()&&result.requiredExact().equals(ExactFraction.of(8)),"Double-speed press asks for 8P: "+result);
        h.getLevel().setBlockAndUpdate(p.north(2),BronzeContent.entry("bronze_hand_crank").block().defaultBlockState());((BronzeEntity)h.getLevel().getBlockEntity(p.north(2))).crank();BronzeNetwork.invalidate(h.getLevel());
        for(int i=0;i<60;i++)ManualWorkshops.tick(h.getLevel(),m);h.assertTrue(m.copper.work==0&&m.getItem(0).getCount()==8,"A 4P handle cannot perform an 8P job");source(h,p,Direction.NORTH);
        for(int i=0;i<49;i++)ManualWorkshops.tick(h.getLevel(),m);h.assertTrue(m.getItem(3).isEmpty(),"Fast gearing still needs its full paid cycle");ManualWorkshops.tick(h.getLevel(),m);h.assertTrue(m.getItem(3).getCount()==1,"One batch finishes in 50 powered ticks");h.succeed();
    }
    @GameTest public void fractionalWorkSurvivesSavingAndInvalidDataIsIgnored(GameTestHelper h){
        var m=press(h,h.absolutePos(new BlockPos(5,4,3)));h.assertTrue(m.copper.mechanicalSteps(RotationRate.of(1,2),1)==0,"Half a tick is retained");
        var tag=m.saveWithFullMetadata(h.getLevel().registryAccess());var copy=(MachineEntity)net.minecraft.world.level.block.entity.BlockEntity.loadStatic(m.getBlockPos(),m.getBlockState(),tag,h.getLevel().registryAccess());
        h.assertTrue(copy.copper.mechanicalSteps(RotationRate.of(1,2),1)==1,"The saved half completes exactly one tick after reload");
        tag.putString("mechanical_fraction","1/0");copy=(MachineEntity)net.minecraft.world.level.block.entity.BlockEntity.loadStatic(m.getBlockPos(),m.getBlockState(),tag,h.getLevel().registryAccess());h.assertTrue(copy.copper.workFraction.equals(ExactFraction.ZERO),"Invalid saved fractions cannot crash or grant free progress");h.succeed();
    }
}
