package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class BronzeRegistrationTests {
    @GameTest public void verticalStaticModelsAndCollisionUseTheSameFrontAsTheirRenderer(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(4,4,4));
        for(var entry:BronzeContent.blocks()){
            if(entry.block() instanceof BronzeAngleBlock||entry.block() instanceof BronzeSupportBlock)continue;
            var source=entry.block().defaultBlockState().getShape(h.getLevel(),pos);
            for(var facing:java.util.List.of(Direction.UP,Direction.DOWN)){
                var boxes=entry.block().defaultBlockState().setValue(BronzeBlock.FACING,facing).getShape(h.getLevel(),pos).toAabbs();
                source.forAllBoxes((x,y,z,xx,yy,zz)->{
                    for(double a:new double[]{x,xx})for(double b:new double[]{y,yy})for(double c:new double[]{z,zz}){
                        double v=facing==Direction.UP?1-c:c,w=facing==Direction.UP?b:1-b;
                        h.assertTrue(boxes.stream().anyMatch(box->a>=box.minX-1e-6&&a<=box.maxX+1e-6&&v>=box.minY-1e-6&&v<=box.maxY+1e-6&&w>=box.minZ-1e-6&&w<=box.maxZ+1e-6),"Vertical world model and rendered front disagree: "+entry.id()+"/"+facing);
                    }
                });
            }
        }h.succeed();
    }
    public static void place(net.minecraft.server.level.ServerLevel level,BlockPos origin,Direction facing,BronzeBlueprints.Blueprint drawing){
        var rotation=BronzeBlueprints.rotation(facing);
        for(var cell:drawing.cells()){
            var block=cell.floor()?Blocks.STONE:BuiltInRegistries.BLOCK.getValue(net.minecraft.resources.Identifier.parse(cell.alternatives().getFirst()));
            var state=block.defaultBlockState();if(state.hasProperty(BronzeBlock.FACING))state=state.setValue(BronzeBlock.FACING,facing);
            level.setBlock(origin.offset(cell.offset().rotate(rotation)),state,Block.UPDATE_CLIENTS);
        }
        level.setBlock(origin,BronzeContent.entry(drawing.controller()).block().defaultBlockState().setValue(BronzeBlock.FACING,facing),Block.UPDATE_CLIENTS);
    }
    private static void clear(net.minecraft.server.level.ServerLevel level,BlockPos origin,Direction facing,BronzeBlueprints.Blueprint drawing){
        var rotation=BronzeBlueprints.rotation(facing);for(var cell:drawing.cells())level.setBlock(origin.offset(cell.offset().rotate(rotation)),Blocks.AIR.defaultBlockState(),Block.UPDATE_CLIENTS);level.setBlock(origin,Blocks.AIR.defaultBlockState(),Block.UPDATE_CLIENTS);
    }
    @GameTest public void chapterRegistersEverySpecifiedBlockAndItemWithoutToolPollution(GameTestHelper h){
        h.assertValueEqual(BronzeContent.blocks().size(),72,"56 functional and 16 decorative blocks");h.assertValueEqual(BronzeContent.items().size(),50,"Mechanical castings, patterns, molds and supplies");
        CreativeModeTabs.tryRebuildTabContents(h.getLevel().enabledFeatures(),true,h.getLevel().registryAccess());
        var bronze=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id("bronze_age")).getDisplayItems();h.assertValueEqual(bronze.size(),122,"Every new entry is visible in its chapter");
        var copper=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id("copper_age")).getDisplayItems();
        for(var entry:BronzeContent.blocks()){
            h.assertTrue(entry.block().asItem()!=Items.AIR,"Normal block item: "+entry.id());h.assertTrue(bronze.stream().anyMatch(s->s.is(entry.block().asItem())),"Chapter includes "+entry.id());h.assertFalse(copper.stream().anyMatch(s->s.is(entry.block().asItem())),"Bronze does not leak into copper");
            String category=switch(entry.group()){case "decor"->"blocks";case "controller"->"controllers";default->"mechanisms";};
            h.assertTrue(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id(category)).getDisplayItems().stream().anyMatch(s->s.is(entry.block().asItem())),"Correct functional category: "+entry.id());
        }h.succeed();
    }
    @GameTest public void allBlueprintHeightsAndHorizontalRotationsMatchPhysicalCells(GameTestHelper h){
        h.assertValueEqual(BronzeBlueprints.all().size(),47,"All exact variants are registered");var level=h.getLevel();var origin=h.absolutePos(new BlockPos(40,35,40));
        for(var drawing:BronzeBlueprints.all())for(var facing:java.util.List.of(Direction.NORTH,Direction.EAST,Direction.SOUTH,Direction.WEST)){
            place(level,origin,facing,drawing);h.assertTrue(BronzeBlueprints.inspect(level,origin,facing,drawing).complete(),"Valid drawing: "+drawing.id()+" "+facing);
            var required=drawing.cells().stream().filter(c->!c.floor()&&!c.alternatives().contains("minecraft:air")).findFirst().orElseThrow();var part=origin.offset(required.offset().rotate(BronzeBlueprints.rotation(facing)));
            level.setBlock(part,Blocks.AIR.defaultBlockState(),Block.UPDATE_CLIENTS);h.assertFalse(BronzeBlueprints.inspect(level,origin,facing,drawing).complete(),"Missing paid component blocks assembly");clear(level,origin,facing,drawing);
        }h.succeed();
    }
    @GameTest public void structureAirAlternativesAndUnloadedChunksAreNotIgnored(GameTestHelper h){
        var level=h.getLevel();var origin=h.absolutePos(new BlockPos(5,3,5));var drawing=BronzeBlueprints.get("bronze_stamper");place(level,origin,Direction.NORTH,drawing);
        var air=drawing.cells().stream().filter(c->c.alternatives().equals(java.util.List.of("minecraft:air"))).findFirst().orElseThrow();level.setBlockAndUpdate(origin.offset(air.offset()),Blocks.DIRT.defaultBlockState());h.assertFalse(BronzeBlueprints.inspect(level,origin,Direction.NORTH,drawing).complete(),"Hollow spaces must remain free");
        var far=new BlockPos(29000000,100,29000000);h.assertFalse(level.hasChunkAt(far),"Fixture starts unloaded");h.assertFalse(BronzeBlueprints.inspect(level,far,Direction.NORTH,drawing).loaded(),"Inspection pauses without loading chunks");h.assertFalse(level.hasChunkAt(far),"Inspection does not generate far terrain");clear(level,origin,Direction.NORTH,drawing);h.succeed();
    }
    @GameTest public void existingAirNozzleHasPhysicalMountsInBothNewFurnaces(GameTestHelper h){
        var level=h.getLevel();var origin=h.absolutePos(new BlockPos(5,5,5));
        for(String id:java.util.List.of("bloomery_controller","forge_hearth"))for(var facing:java.util.List.of(Direction.NORTH,Direction.EAST,Direction.SOUTH,Direction.WEST)){
            var drawing=BronzeBlueprints.get(id);place(level,origin,facing,drawing);
            var cell=drawing.cells().stream().filter(c->c.alternatives().contains("smog:air_nozzle")).findFirst().orElseThrow();var pos=origin.offset(cell.offset().rotate(BronzeBlueprints.rotation(facing)));level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
            h.assertValueEqual(BronzeBlueprints.nozzleFacing(level,pos),facing.getOpposite(),"Wall nozzle can mount without an impossible extra intake in the hollow furnace");h.assertTrue(BronzeBlueprints.nozzleFacing(level,pos.above(3))==null,"The mounting exception is restricted to the drawing's real nozzle cell");clear(level,origin,facing,drawing);
        }h.succeed();
    }
    @GameTest public void sharedStructuralBlocksHaveOneOwnerAndBreakageReleasesAssembly(GameTestHelper h){
        var level=h.getLevel();var first=h.absolutePos(new BlockPos(5,3,5));var second=first.east(2);var drawing=BronzeBlueprints.get("bronze_mill");place(level,first,Direction.NORTH,drawing);place(level,second,Direction.NORTH,drawing);
        var player=h.makeMockServerPlayerInLevel();var a=(BronzeEntity)level.getBlockEntity(first);var b=(BronzeEntity)level.getBlockEntity(second);a.inspect(player);h.assertFalse(a.blueprint().isEmpty(),"First mill owns its frame");b.inspect(player);h.assertTrue(b.blueprint().isEmpty(),"Second mill cannot reuse the same structural frame");a.disassemble();b.inspect(player);h.assertFalse(b.blueprint().isEmpty(),"Explicit disassembly releases shared parts");b.disassemble();clear(level,first,Direction.NORTH,drawing);clear(level,second,Direction.NORTH,drawing);h.succeed();
    }
    @GameTest(maxTicks=100) public void crankMotionExpiresAndShapesFollowTheModel(GameTestHelper h){
        var level=h.getLevel();var pos=h.absolutePos(new BlockPos(3,3,3));level.setBlockAndUpdate(pos,BronzeContent.entry("bronze_hand_crank").block().defaultBlockState());var crank=(BronzeEntity)level.getBlockEntity(pos);crank.crank();h.assertTrue(crank.moving(),"Finite crank stroke starts");
        var shaft=BronzeContent.entry("bronze_shaft").block().defaultBlockState();var shape=shaft.getShape(level,pos);h.assertTrue(shape.bounds().getYsize()<1,"A narrow shaft does not have a full-cube collision");
        h.runAfterDelay(81,()->{h.assertFalse(crank.moving(),"Crank does not animate forever or generate offline motion");h.succeed();});
    }
}
