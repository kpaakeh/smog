package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;

public final class FinishingTests {
    private static MachineEntity bench(GameTestHelper h){var pos=h.absolutePos(new BlockPos(3,2,3));h.getLevel().setBlock(pos,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("tool_workbench")).defaultBlockState(),Block.UPDATE_ALL);return (MachineEntity)h.getLevel().getBlockEntity(pos);}
    private static void run(GameTestHelper h,MachineEntity bench,int ticks){for(int n=0;n<ticks;n++)ToolAssembly.tick(h.getLevel(),bench);}
    @GameTest public void oneStrikeFinishesFiniteBatchAndChargesEachHead(GameTestHelper h){
        var bench=bench(h);var cast=ToolDefinitions.find("copper","pickaxe_head",true);h.assertTrue(cast!=null,"Casting fixture is registered");bench.setItem(0,new ItemStack(ToolParts.item(cast.item().toString()),10));
        var player=h.makeMockServerPlayer(GameType.SURVIVAL);((net.minecraft.server.level.ServerPlayer)player).connection=h.makeMockServerPlayerInLevel().connection;var hammer=ToolParts.example(ToolDefinitions.template("hammer"),"stone");player.setItemInHand(InteractionHand.MAIN_HAND,hammer);
        ToolAssembly.use(h.getLevel(),bench,player,InteractionHand.MAIN_HAND);h.assertValueEqual(bench.copper.queued,8,"One strike queues at most eight cast heads");h.assertValueEqual(hammer.getDamageValue(),8,"Each head reserves one hammer use");
        ToolAssembly.use(h.getLevel(),bench,player,InteractionHand.MAIN_HAND);h.assertValueEqual(hammer.getDamageValue(),8,"Repeated strike neither accelerates nor charges again");
        run(h,bench,320);h.assertValueEqual(bench.getItem(3).getCount(),8,"Exactly the finite batch is finished");h.assertValueEqual(bench.getItem(0).getCount(),2,"Unordered input remains on the table");h.assertValueEqual(bench.copper.queued,0,"Batch completes without automatic repetition");run(h,bench,100);h.assertValueEqual(bench.getItem(3).getCount(),8,"Idle table does not process the rest");h.succeed();
    }
    @GameTest public void finishingRespectsOutputRoomAndLastHammerUse(GameTestHelper h){
        var bench=bench(h);var cast=ToolDefinitions.find("copper","pickaxe_head",true);var input=new ItemStack(ToolParts.item(cast.item().toString()),8);bench.setItem(0,input);var finished=ToolParts.finish(input);int maximum=finished.getMaxStackSize();bench.setItem(3,finished.copyWithCount(maximum-1));
        var player=h.makeMockServerPlayer(GameType.SURVIVAL);((net.minecraft.server.level.ServerPlayer)player).connection=h.makeMockServerPlayerInLevel().connection;var hammer=ToolParts.example(ToolDefinitions.template("hammer"),"stone");hammer.setDamageValue(hammer.getMaxDamage()-1);player.setItemInHand(InteractionHand.MAIN_HAND,hammer);
        ToolAssembly.use(h.getLevel(),bench,player,InteractionHand.MAIN_HAND);h.assertValueEqual(bench.copper.queued,1,"Output room and remaining hammer life cap the order");h.assertTrue(ModularToolItem.broken(player.getMainHandItem())&&!player.getMainHandItem().isEmpty(),"Last hammer use is spent but the repairable assembly remains");run(h,bench,40);h.assertValueEqual(bench.getItem(3).getCount(),maximum,"Output never exceeds its stack limit");h.assertValueEqual(bench.getItem(0).getCount(),7,"Only one ordered cast is consumed");h.succeed();
    }
}
