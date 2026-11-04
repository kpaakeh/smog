package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.*;

public final class BronzeAngleTests {
    @GameTest public void aCornerCircuitPerformsOnePaidPressBatch(GameTestHelper h){
        var level=h.getLevel();var pressPos=h.absolutePos(new BlockPos(7,4,7));
        io.github.kpaakeh.smog.multiblock.Multiblocks.get(io.github.kpaakeh.smog.registry.ModBlocks.id("lever_press")).parts().forEach((offset,part)->level.setBlockAndUpdate(pressPos.offset(offset),part.block().defaultBlockState()));
        level.setBlockAndUpdate(pressPos,net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(io.github.kpaakeh.smog.registry.ModBlocks.id("lever_press")).defaultBlockState());
        var press=(io.github.kpaakeh.smog.machine.MachineEntity)level.getBlockEntity(pressPos);h.assertTrue(press.tryAssemble(),"Existing copper press is physically assembled");press.setItem(0,new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WHEAT_SEEDS,8));
        level.setBlockAndUpdate(pressPos.north(),BronzeContent.entry("bronze_drive_coupler").block().defaultBlockState().setValue(BronzeBlock.FACING,Direction.SOUTH));var gear=pressPos.north(2);
        level.setBlockAndUpdate(gear,BronzeContent.entry("bronze_angle_gearbox").block().defaultBlockState().setValue(BronzeBlock.FACING,Direction.EAST).setValue(BronzeAngleBlock.OUTPUT,Direction.SOUTH));shaft(h,gear.west(),Direction.EAST);
        level.setBlockAndUpdate(gear.west(2),BronzeContent.entry("bronze_hand_crank").block().defaultBlockState().setValue(BronzeBlock.FACING,Direction.WEST));((BronzeEntity)level.getBlockEntity(gear.west(2))).crank();
        for(int i=0;i<100;i++)io.github.kpaakeh.smog.copper.ManualWorkshops.tick(level,press);
        h.assertTrue(press.getItem(0).isEmpty()&&press.copper.processFluid==20250&&press.getItem(3).getCount()==1,"The real press consumes exactly one batch through a perpendicular physical circuit");h.succeed();
    }
    private static void shaft(GameTestHelper h,BlockPos p,Direction direction){h.getLevel().setBlockAndUpdate(p,BronzeContent.entry("bronze_shaft").block().defaultBlockState().setValue(BronzeBlock.FACING,BronzeShaft.canonical(direction)));}
    @GameTest public void allTwentyFourCornersTransmitPhysicalDirectionWithoutLoss(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(5,5,5));var gear=BronzeContent.entry("bronze_angle_gearbox").block();
        for(var facing:Direction.values())for(var output:Direction.values())if(facing.getAxis()!=output.getAxis()){
            var input=facing.getOpposite();var state=gear.defaultBlockState().setValue(BronzeBlock.FACING,facing).setValue(BronzeAngleBlock.OUTPUT,output);level.setBlockAndUpdate(p,state);
            shaft(h,p.relative(input),facing);shaft(h,p.relative(output),output);var source=p.relative(input,2);level.setBlockAndUpdate(source,ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,input).setValue(ShaftRotationFixture.REVERSE,true));
            BronzeNetwork.invalidate(level);var out=BronzeNetwork.query(level,p.relative(output));var in=BronzeNetwork.query(level,p.relative(input));var result=BronzeNetwork.query(level,p);
            h.assertTrue(out.running()&&out.available()==16&&out.rate().equals(RotationRate.of(16*BronzeShaft.handedness(output),1)),"Output shaft follows the bevel gear's real angular direction: "+facing+" -> "+output);
            h.assertTrue(in.rate().equals(RotationRate.of(16*BronzeShaft.handedness(facing),1))&&result.rate().equals(RotationRate.NOMINAL),"Input and output are a genuine 1:1 pair");
            var shape=state.getShape(level,p);for(var side:new Direction[]{input,output}){double x=.5+side.getStepX()*.49,y=.5+side.getStepY()*.49,z=.5+side.getStepZ()*.49;h.assertTrue(shape.toAabbs().stream().anyMatch(b->b.contains(x,y,z)),"Visible shafts and collision reach exactly the declared ports");}
            for(var side:Direction.values())if(side!=input&&side!=output)h.assertFalse(BronzeNetwork.ports(level,p,state).contains(side),"The housing has no invisible extra connections");
            for(var pos:java.util.List.of(p,p.relative(input),p.relative(output),source))level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
        }h.succeed();
    }
    @GameTest public void perpendicularSourcesCombineOnlyWhenTheirPhysicalRotationAgrees(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,5,4));var gear=BronzeContent.entry("bronze_angle_gearbox").block();level.setBlockAndUpdate(p,gear.defaultBlockState());
        shaft(h,p.south(),Direction.NORTH);shaft(h,p.east(),Direction.EAST);level.setBlockAndUpdate(p.south(2),ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,Direction.SOUTH).setValue(ShaftRotationFixture.REVERSE,true));var other=p.east(2);
        level.setBlockAndUpdate(other,ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,Direction.EAST));BronzeNetwork.invalidate(level);
        var compatible=BronzeNetwork.query(level,p);h.assertTrue(compatible.running()&&compatible.available()==32,"Two sources can face different directions yet physically agree through the bevel pair");
        level.setBlockAndUpdate(other,level.getBlockState(other).setValue(ShaftRotationFixture.REVERSE,true));BronzeNetwork.invalidate(level);var conflict=BronzeNetwork.query(level,p);
        h.assertTrue(conflict.status().equals("direction")&&!conflict.running()&&conflict.rate().equals(RotationRate.ZERO),"A truly opposing second source safely stops the circuit");h.succeed();
    }
    @GameTest public void speedAndPhaseAcrossCornersStayExactForBothDirections(GameTestHelper h){
        var level=h.getLevel();var p=h.absolutePos(new BlockPos(4,5,4));var gear=BronzeContent.entry("bronze_angle_gearbox").block();level.setBlockAndUpdate(p,gear.defaultBlockState().setValue(BronzeAngleBlock.OUTPUT,Direction.WEST));shaft(h,p.south(),Direction.NORTH);shaft(h,p.west(),Direction.EAST);
        for(int tempo=0;tempo<ShaftRotationFixture.SPEEDS.length;tempo++)for(boolean reverse:new boolean[]{false,true}){
            level.setBlockAndUpdate(p.south(2),ShaftRotationFixture.SOURCE.defaultBlockState().setValue(ShaftRotationFixture.FACING,Direction.SOUTH).setValue(ShaftRotationFixture.TEMPO,tempo).setValue(ShaftRotationFixture.REVERSE,!reverse));BronzeNetwork.invalidate(level);
            var input=BronzeNetwork.query(level,p);var output=BronzeNetwork.query(level,p.west());h.assertTrue(input.running()&&Math.abs(input.rate().value())==ShaftRotationFixture.SPEEDS[tempo]&&input.rate().equals(output.rate().negate()),"Westward output changes canonical handedness without changing absolute tempo");
            var a=(BronzeEntity)level.getBlockEntity(p);var b=(BronzeEntity)level.getBlockEntity(p.west());a.tick();b.tick();
            h.assertTrue(Math.abs(Math.IEEEremainder(a.angle(.5f)+b.angle(.5f),360))<.001,"The connected rotor and output shaft have the same physical phase");
        }h.succeed();
    }
    @GameTest public void structureTransformsAndOutputSelectionKeepTheSameTwoPorts(GameTestHelper h){
        var block=BronzeContent.entry("bronze_angle_gearbox").block();
        for(var facing:Direction.values())for(var output:Direction.values())if(facing.getAxis()!=output.getAxis()){
            var original=block.defaultBlockState().setValue(BronzeBlock.FACING,facing).setValue(BronzeAngleBlock.OUTPUT,output);var rotated=original;
            for(int i=0;i<4;i++)rotated=rotated.rotate(Rotation.CLOCKWISE_90);h.assertTrue(rotated.equals(original),"Four turns restore both physical ports");
            var next=output;var visited=java.util.EnumSet.noneOf(Direction.class);for(int i=0;i<4;i++){visited.add(next);next=BronzeAngleBlock.nextOutput(facing,next);}h.assertTrue(next==output&&visited.size()==4&&visited.stream().noneMatch(d->d.getAxis()==facing.getAxis()),"Configuration offers exactly four perpendicular outputs");
            for(var mirror:Mirror.values())h.assertTrue(original.mirror(mirror).mirror(mirror).equals(original),"Mirroring twice restores both ports");
        }h.succeed();
    }
}
