package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;

public final class ToolActionTests {
    @GameTest public void sawFellsAnActualGeneratedTreeWithAutomaticProvenance(GameTestHelper h){
        var level=h.getLevel();var base=h.absolutePos(new BlockPos(4,20,4));
        for(var clear:BlockPos.betweenClosed(base.offset(-4,0,-4),base.offset(4,14,4)))level.setBlock(clear,Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);
        for(var soil:BlockPos.betweenClosed(base.offset(-2,-1,-2),base.offset(2,-1,2)))level.setBlock(soil,Blocks.DIRT.defaultBlockState(),Block.UPDATE_ALL);
        var sapling=Blocks.OAK_SAPLING.defaultBlockState().setValue(SaplingBlock.STAGE,1);level.setBlock(base,sapling,Block.UPDATE_ALL);
        ((SaplingBlock)Blocks.OAK_SAPLING).performBonemeal(level,net.minecraft.util.RandomSource.create(7),base,sapling,BonemealSource.INTERACTION);
        var logs=new java.util.ArrayList<BlockPos>();
        for(var pos:BlockPos.betweenClosed(base.offset(-3,0,-3),base.offset(3,12,3)))if(level.getBlockState(pos).is(Blocks.OAK_LOG))logs.add(pos.immutable());
        h.assertTrue(logs.size()>=4,"The real tree feature generated a trunk");
        for(var pos:logs)h.assertTrue(NaturalTreeLogs.natural(level,pos),"TreeFeature automatically marks generated logs");
        var p=player(h,"saw",base);var tool=p.getMainHandItem();h.assertTrue(p.gameMode.destroyBlock(base),"Native first break succeeds on the generated tree");
        for(var pos:logs)h.assertTrue(level.getBlockState(pos).isAir(),"The entire generated trunk is felled");
        h.assertValueEqual(tool.getDamageValue(),logs.size(),"Every generated log costs durability");h.succeed();
    }
    @GameTest public void sawRecognizesLegacyTreeButRejectsPlayerLogsAndPersistentLeafBuildings(GameTestHelper h){
        var level=h.getLevel();var base=h.absolutePos(new BlockPos(4,20,4));
        level.setBlock(base.below(),Blocks.DIRT.defaultBlockState(),Block.UPDATE_ALL);
        for(int y=0;y<4;y++)level.setBlock(base.above(y),Blocks.OAK_LOG.defaultBlockState(),Block.UPDATE_ALL);
        for(int x=-1;x<=1;x++)for(int z=-1;z<=1;z++)if(x!=0||z!=0)level.setBlock(base.offset(x,3,z),Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT,false).setValue(LeavesBlock.DISTANCE,1),Block.UPDATE_ALL);
        var attached=base.offset(1,2,0);level.setBlock(attached,Blocks.OAK_LOG.defaultBlockState(),Block.UPDATE_ALL);NaturalTreeLogs.placed(level,attached);
        h.assertFalse(NaturalTreeLogs.natural(level,base),"Legacy tree has no positive metadata");h.assertTrue(NaturalTreeLogs.playerPlaced(level,attached),"Manual log records negative provenance");
        var p=player(h,"saw",base);h.assertTrue(p.gameMode.destroyBlock(base),"Initial legacy log breaks");
        for(int y=0;y<4;y++)h.assertTrue(level.getBlockState(base.above(y)).isAir(),"Unmarked legacy tree is felled from structure evidence");
        h.assertTrue(level.getBlockState(attached).is(Blocks.OAK_LOG),"Attached player log survives even beneath natural leaves");
        var building=base.offset(8,0,0);level.setBlock(building.below(),Blocks.DIRT.defaultBlockState(),Block.UPDATE_ALL);
        for(int y=0;y<4;y++){level.setBlock(building.above(y),Blocks.OAK_LOG.defaultBlockState(),Block.UPDATE_ALL);NaturalTreeLogs.placed(level,building.above(y));}
        for(int x=-1;x<=1;x++)for(int z=-1;z<=1;z++)level.setBlock(building.offset(x,4,z),Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT,true),Block.UPDATE_ALL);
        h.assertTrue(NaturalTreeLogs.tree(level,building).isEmpty(),"Player-placed pillar and persistent decoration never qualify as a tree");h.succeed();
    }
    @GameTest public void hammerStopsAtBrokenHeadInsteadOfCompletingAFreeArea(GameTestHelper h){
        var level=h.getLevel();var center=h.absolutePos(new BlockPos(4,3,4));for(int x=-1;x<=1;x++)for(int y=-1;y<=1;y++)level.setBlock(center.offset(x,y,0),Blocks.STONE.defaultBlockState(),Block.UPDATE_ALL);
        var p=player(h,"hammer",center);var tool=p.getMainHandItem();tool.setDamageValue(tool.getMaxDamage()-1);h.assertTrue(p.gameMode.destroyBlock(center),"Last paid initial strike breaks the target");
        int remaining=0;for(int x=-1;x<=1;x++)for(int y=-1;y<=1;y++)if(level.getBlockState(center.offset(x,y,0)).is(Blocks.STONE))remaining++;
        h.assertValueEqual(remaining,8,"A broken head cannot supply eight free secondary breaks");h.succeed();
    }
    private static ServerPlayer player(GameTestHelper h,String type,BlockPos pos){var p=(ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL);p.connection=h.makeMockServerPlayerInLevel().connection;p.setPos(pos.getX()+.5,pos.getY(),pos.getZ()-3);p.setYRot(0);p.setXRot(0);p.setItemInHand(InteractionHand.MAIN_HAND,ToolParts.example(ToolDefinitions.template(type),"copper"));return p;}
    @GameTest public void hammerBreaksOnePlaneAndPaysWearForEveryBlock(GameTestHelper h){
        var level=h.getLevel();var center=h.absolutePos(new BlockPos(4,3,4));
        for(int x=-1;x<=1;x++)for(int y=-1;y<=1;y++)level.setBlock(center.offset(x,y,0),Blocks.STONE.defaultBlockState(),Block.UPDATE_ALL);
        level.setBlock(center.offset(0,0,1),Blocks.STONE.defaultBlockState(),Block.UPDATE_ALL);
        var p=player(h,"hammer",center);var tool=p.getMainHandItem();
        h.assertTrue(p.gameMode.destroyBlock(center),"The initial native player break succeeds");
        for(int x=-1;x<=1;x++)for(int y=-1;y<=1;y++)h.assertTrue(level.getBlockState(center.offset(x,y,0)).isAir(),"Hammer clears every block in the selected3x3plane");
        h.assertTrue(level.getBlockState(center.offset(0,0,1)).is(Blocks.STONE),"The second depth layer is untouched");
        h.assertValueEqual(tool.getDamageValue(),9,"All nine native breaks consume durability");h.succeed();
    }
    @GameTest public void sawUsesPositiveProvenanceAndNeverCutsAttachedPlacedLogs(GameTestHelper h){
        var level=h.getLevel();var base=h.absolutePos(new BlockPos(4,2,4));
        for(int y=0;y<4;y++){var pos=base.above(y);level.setBlock(pos,Blocks.OAK_LOG.defaultBlockState(),Block.UPDATE_ALL);NaturalTreeLogs.generated(level,pos,Blocks.OAK_LOG.defaultBlockState());}
        var placed=base.offset(1,2,0);level.setBlock(placed,Blocks.OAK_LOG.defaultBlockState(),Block.UPDATE_ALL);
        var p=player(h,"saw",base);var tool=p.getMainHandItem();h.assertTrue(p.gameMode.destroyBlock(base),"Native initial saw break succeeds");
        for(int y=0;y<4;y++)h.assertTrue(level.getBlockState(base.above(y)).isAir(),"All positively marked connected logs are felled");
        h.assertTrue(level.getBlockState(placed).is(Blocks.OAK_LOG),"A connected player-built log is not included");h.assertValueEqual(tool.getDamageValue(),4,"Saw pays for every harvested log");
        var replacement=base.offset(0,0,2);level.setBlock(replacement,Blocks.OAK_LOG.defaultBlockState(),Block.UPDATE_ALL);NaturalTreeLogs.generated(level,replacement,Blocks.OAK_LOG.defaultBlockState());NaturalTreeLogs.placed(level,replacement);
        h.assertFalse(NaturalTreeLogs.natural(level,replacement),"Manual replacement clears generation provenance");h.succeed();
    }
    @GameTest public void sickleHarvestsOnlyMaturePlantsAndReservesPlantingMaterial(GameTestHelper h){
        var level=h.getLevel();var center=h.absolutePos(new BlockPos(4,2,4));
        for(int x=-1;x<=1;x++)for(int z=-1;z<=1;z++){var pos=center.offset(x,0,z);level.setBlock(pos.below(),Blocks.FARMLAND.defaultBlockState(),Block.UPDATE_ALL);level.setBlock(pos,Blocks.CARROTS.defaultBlockState().setValue(CropBlock.AGE,x==1&&z==1?2:7),Block.UPDATE_ALL);}
        var p=player(h,"sickle",center);var tool=p.getMainHandItem();var context=new UseOnContext(p,InteractionHand.MAIN_HAND,new BlockHitResult(Vec3.atCenterOf(center),Direction.UP,center,false));
        h.assertTrue(tool.useOn(context).consumesAction(),"Right-click harvests the mature crop area");
        for(int x=-1;x<=1;x++)for(int z=-1;z<=1;z++)h.assertValueEqual(level.getBlockState(center.offset(x,0,z)).getValue(CropBlock.AGE),x==1&&z==1?2:0,"Mature carrots are replanted and young carrots preserved");
        h.assertValueEqual(tool.getDamageValue(),8,"Only the eight mature plants consume durability");
        int before=level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class,new AABB(center).inflate(3)).stream().mapToInt(e->e.getItem().getCount()).sum();
        h.assertTrue(before>0,"Harvest leaves actual produce after reserving one carrot per planted crop");tool.useOn(context);
        h.assertValueEqual(tool.getDamageValue(),8,"Clicking immature replanted crops again creates no extra harvest or wear");h.succeed();
    }
}
