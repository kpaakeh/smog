package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class WorkshopFinaleTests {
    private static Block block(String name){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name));}
    private static void set(GameTestHelper h,BlockPos pos,Block block){h.getLevel().setBlock(pos,block.defaultBlockState(),Block.UPDATE_ALL);}
    private static MachineEntity press(GameTestHelper h){var pos=h.absolutePos(new BlockPos(5,3,4));Multiblocks.get(ModBlocks.id("lever_press")).parts().forEach((p,part)->set(h,pos.offset(p),part.block()));set(h,pos,block("lever_press"));var machine=(MachineEntity)h.getLevel().getBlockEntity(pos);set(h,pos.above(),block("bronze_drive"));h.assertTrue(machine.tryAssemble(),"Drive replaces a real frame in an assembled press");return machine;}
    private static void water(GameTestHelper h,BlockPos drive){h.getLevel().setBlock(drive.north(),Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL,8),Block.UPDATE_ALL);}
    private static boolean done(GameTestHelper h,ServerPlayer player){return player.getAdvancements().getOrStartProgress(h.getLevel().getServer().getAdvancements().get(ModBlocks.id("copper/mechanized"))).isDone();}
    @GameTest public void pressCommissioningRequiresTwoActualAutomaticBatchesAndSurvivesReload(GameTestHelper h){
        var machine=press(h);var drive=machine.getBlockPos().above();water(h,drive);var player=h.makeMockServerPlayerInLevel();player.setPos(drive.getX(),drive.getY(),drive.getZ());
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,ItemStack.EMPTY);player.gameMode.useItemOn(player,h.getLevel(),ItemStack.EMPTY,net.minecraft.world.InteractionHand.MAIN_HAND,new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(drive),Direction.UP,drive,false));h.assertTrue(machine.copper.commissioningOwner.equals(player.getUUID().toString()),"Native use of the claimed drive starts commissioning rather than a manual press queue");h.assertFalse(done(h,player),"Installing a drive alone cannot finish the chapter");
        machine.setItem(0,new ItemStack(Items.WHEAT_SEEDS,16));for(int i=0;i<100;i++)ManualWorkshops.tick(h.getLevel(),machine);
        h.assertValueEqual(machine.copper.commissioningWork,1,"The first real batch counts once");h.assertFalse(done(h,player),"One batch is not yet a functioning series");
        var saved=machine.saveCustomOnly(h.getLevel().registryAccess());machine.loadCustomOnly(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING,h.getLevel().registryAccess(),saved));
        h.assertValueEqual(machine.copper.commissioningWork,1,"Commissioning progress survives reload");for(int i=0;i<100;i++)ManualWorkshops.tick(h.getLevel(),machine);
        h.assertTrue(done(h,player),"Two productive automatic batches complete the chapter");h.assertValueEqual(machine.getItem(3).getCount(),2,"The trial produces useful seed cake rather than consuming a quest token");h.assertValueEqual(machine.copper.processFluid,40500,"Both real oil batches remain available");
        int xp=player.totalExperience;WorkshopFinale.use(h.getLevel(),drive,player);h.assertValueEqual(player.totalExperience,xp,"Repeating the interaction cannot farm the reward");h.succeed();
    }
    @GameTest public void stalledWaterBlockedOutputAndManualQueueCannotFakeCommissioning(GameTestHelper h){
        var machine=press(h);var drive=machine.getBlockPos().above();var player=h.makeMockServerPlayerInLevel();player.setPos(drive.getX(),drive.getY(),drive.getZ());
        set(h,drive.north(),Blocks.WATER);h.assertFalse(WorkshopFinale.use(h.getLevel(),drive,player),"Standing source water is not mechanical power");water(h,drive);WorkshopFinale.use(h.getLevel(),drive,player);
        machine.setItem(0,new ItemStack(Items.WHEAT_SEEDS,16));machine.setItem(3,new ItemStack(Items.STICK,64));for(int i=0;i<120;i++)ManualWorkshops.tick(h.getLevel(),machine);h.assertValueEqual(machine.copper.commissioningWork,0,"A blocked output does not count as production");
        machine.setItem(3,ItemStack.EMPTY);set(h,drive.north(),Blocks.AIR);for(int i=0;i<120;i++)ManualWorkshops.tick(h.getLevel(),machine);h.assertValueEqual(machine.getItem(0).getCount(),16,"A stopped drive cannot spend trial input");
        ManualWorkshops.queue(machine);for(int i=0;i<200;i++)ManualWorkshops.tick(h.getLevel(),machine);h.assertValueEqual(machine.copper.commissioningWork,0,"Manual fallback remains usable without being passed off as automation");h.assertFalse(done(h,player),"None of the invalid cases completes the chapter");h.succeed();
    }
    @GameTest public void bellowsRouteCountsRealMeltsAndRejectsIdleOrManualAir(GameTestHelper h){
        var level=h.getLevel();var origin=h.absolutePos(new BlockPos(5,3,4));
        for(int x=-1;x<=1;x++)for(int y=-1;y<=1;y++)for(int z=0;z<3;z++)set(h,origin.offset(x,y,z),y==-1?Blocks.COBBLESTONE:x==-1||x==1||z==0||z==2?Blocks.BRICKS:Blocks.AIR);
        set(h,origin,block("copper_hearth"));set(h,origin.offset(0,-1,1),block("solid_firebox"));set(h,origin.offset(-1,0,1),block("casting_spout"));set(h,origin.offset(1,0,1),block("air_intake"));
        var smeltery=(MachineEntity)level.getBlockEntity(origin);h.assertTrue(smeltery.tryAssemble(),"Smeltery assembled");var nozzle=origin.offset(2,0,1);level.setBlock(nozzle,block("air_nozzle").defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.EAST).setValue(WorkshopPartBlock.OPEN,true),Block.UPDATE_ALL);
        var bellowsPos=nozzle.east();set(h,bellowsPos,block("hand_bellows"));var bellows=(MachineEntity)level.getBlockEntity(bellowsPos);bellows.serverTick();var drive=bellowsPos.east();set(h,drive,block("bronze_drive"));water(h,drive);
        var player=h.makeMockServerPlayerInLevel();player.setPos(drive.getX(),drive.getY(),drive.getZ());h.assertTrue(WorkshopFinale.use(level,drive,player),"Bellows are an alternative to the press, not a mandatory second project");
        for(int i=0;i<5;i++)CopperWorkshops.pump(level,bellows,true);h.assertValueEqual(bellows.copper.commissioningWork,0,"Idle automatic pumping cannot complete the chapter");
        smeltery.copper.heat=600;smeltery.copper.burn=10000;
        for(int job=0;job<3;job++){
            h.assertTrue(CopperWorkshops.pump(level,bellows,job!=1),"Air actually reaches the claimed smeltery");smeltery.setItem(0,new ItemStack(Items.COPPER_INGOT));
            for(int i=0;i<200&&!smeltery.getItem(0).isEmpty();i++)Smeltery.tick(level,smeltery);
            h.assertTrue(smeltery.getItem(0).isEmpty(),"The metal really finishes melting");h.assertValueEqual(bellows.copper.commissioningWork,job==2?2:1,"Only completed work using automatic air counts");
        }
        h.assertTrue(done(h,player),"Mechanical bellows can finish the chapter without a press or water-pipe branch");h.assertValueEqual(smeltery.copper.copper,27000,"The project retains all three real ingots of metal");h.succeed();
    }
}
