package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;

/** Isolated, real-client logistics review. Uses only the game's test API, never desktop input. */
public final class LogisticsClientScene {
    private static final BlockPos CRANE=new BlockPos(0,104,0),FIRST=new BlockPos(-6,104,0),LAST=new BlockPos(-6,104,12);
    private static Block block(String name){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name));}
    private static Item item(String name){return BuiltInRegistries.ITEM.getValue(ModBlocks.id(name));}
    private static void useRope(net.minecraft.server.level.ServerPlayer player,BlockPos pos){((RopeLinkItem)player.getMainHandItem().getItem()).useOn(new UseOnContext(player,InteractionHand.MAIN_HAND,new BlockHitResult(Vec3.atCenterOf(pos),Direction.UP,pos,false)));}
    public static void run(ClientGameTestContext context){
        try(var world=context.worldBuilder().create()){
            world.getServer().runCommand("fill -12 99 -8 12 99 18 minecraft:stone");world.getServer().runCommand("time set noon");world.getServer().runCommand("weather clear");
            world.getServer().runOnServer(server->{
                var level=server.overworld();var player=server.getPlayerList().getPlayers().getFirst();player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);player.getAbilities().flying=true;player.onUpdateAbilities();
                Multiblocks.get(ModBlocks.id("loading_crane")).parts().forEach((offset,part)->level.setBlockAndUpdate(CRANE.offset(offset),part.block().defaultBlockState()));level.setBlockAndUpdate(CRANE,block("loading_crane").defaultBlockState());var crane=(MachineEntity)level.getBlockEntity(CRANE);if(!crane.tryAssemble())throw new IllegalStateException("Current five-block crane did not assemble");crane.enabled(false);
                for(boolean second:new boolean[]{false,true})level.setBlockAndUpdate(LoadingCrane.bay(crane,second),Blocks.BARREL.defaultBlockState());var source=(net.minecraft.world.Container)level.getBlockEntity(LoadingCrane.bay(crane,false));source.setItem(0,new ItemStack(Items.COBBLESTONE,64));source.setItem(1,new ItemStack(Items.DIAMOND,11));
                level.setBlockAndUpdate(FIRST,block("rope_anchor").defaultBlockState());level.setBlockAndUpdate(LAST,block("rope_anchor").defaultBlockState());
                player.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(item("freight_rope"),2));useRope(player,FIRST);useRope(player,LAST);var a=(MachineEntity)level.getBlockEntity(FIRST);if(!a.copper.ropeLinked)throw new IllegalStateException("Native two-anchor rope interaction did not link");player.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);
            });
            world.getServer().runCommand("tp @a 6.5 105 -5.5 45 15");context.waitTicks(30);context.getInput().lookAt(new BlockPos(0,102,2));context.waitTicks(10);context.takeScreenshot("review-crane-overview-markers-redstone");
            world.getServer().runOnServer(server->{var m=(MachineEntity)server.overworld().getBlockEntity(CRANE);LoadingCrane.queue(m);for(int tick=0;tick<25;tick++)LoadingCrane.tick(server.overworld(),m);m.sync();if(m.getItem(1).isEmpty())throw new IllegalStateException("Hoist never picked up cargo");});
            context.waitTicks(10);context.takeScreenshot("review-crane-low-pickup");
            world.getServer().runOnServer(server->{var m=(MachineEntity)server.overworld().getBlockEntity(CRANE);for(int tick=0;tick<45;tick++)LoadingCrane.tick(server.overworld(),m);m.sync();});
            world.getServer().runCommand("tp @a 4.5 103 2.5 90 0");context.waitTicks(10);context.getInput().lookAt(new BlockPos(0,102,2));context.waitTicks(5);context.takeScreenshot("review-crane-high-side-cargo-drum");
            world.getServer().runOnServer(server->{var m=(MachineEntity)server.overworld().getBlockEntity(CRANE);for(int tick=0;tick<50;tick++)LoadingCrane.tick(server.overworld(),m);m.sync();});context.waitTicks(10);context.takeScreenshot("review-crane-low-delivery");
            world.getServer().runOnServer(server->{var m=(MachineEntity)server.overworld().getBlockEntity(CRANE);for(int tick=0;tick<50;tick++)LoadingCrane.tick(server.overworld(),m);m.sync();var target=(net.minecraft.world.Container)server.overworld().getBlockEntity(LoadingCrane.bay(m,true));if(target.getItem(0).getCount()!=64)throw new IllegalStateException("Hoist did not deliver exactly one stack");});
            world.getServer().runCommand("tp @a -1.5 104 6.5 90 10");context.waitTicks(10);context.getInput().lookAt(new BlockPos(-6,104,6));context.waitTicks(10);context.takeScreenshot("review-ropeway-before-cargo");
            world.getServer().runOnServer(server->{var level=server.overworld();var a=(MachineEntity)level.getBlockEntity(FIRST);var player=server.getPlayerList().getPlayers().getFirst();player.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.OAK_PLANKS,16));CopperInteractions.use(level,a,player,InteractionHand.MAIN_HAND,FIRST);player.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);CopperInteractions.use(level,a,player,InteractionHand.MAIN_HAND,FIRST);});
            for(int frame=0;frame<6;frame++){context.waitTicks(12);context.takeScreenshot("review-ropeway-cargo-"+frame);}
            context.waitTicks(70);world.getServer().runOnServer(server->{var target=(MachineEntity)server.overworld().getBlockEntity(LAST);if(!target.getItem(3).is(Items.OAK_PLANKS)||target.getItem(3).getCount()!=16)throw new IllegalStateException("Native ropeway delivery did not arrive");var player=server.getPlayerList().getPlayers().getFirst();player.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.STICK));var source=(MachineEntity)server.overworld().getBlockEntity(FIRST);CopperInteractions.use(server.overworld(),source,player,InteractionHand.MAIN_HAND,FIRST);if(!player.isPassenger())throw new IllegalStateException("Using a stick did not board the ropeway");});
            context.runOnClient(mc->mc.options.setCameraType(net.minecraft.client.CameraType.THIRD_PERSON_BACK));
            context.waitTicks(5);context.takeScreenshot("review-ropeway-stick-boarded");context.waitTicks(45);context.takeScreenshot("review-ropeway-stick-midspan");context.waitTicks(100);
            world.getServer().runOnServer(server->{var player=server.getPlayerList().getPlayers().getFirst();if(player.isPassenger())throw new IllegalStateException("Passenger did not dismount at destination");if(player.position().distanceToSqr(Vec3.atCenterOf(LAST))>36)throw new IllegalStateException("Passenger did not reach the destination platform");});context.takeScreenshot("review-ropeway-stick-landed");context.runOnClient(mc->mc.options.setCameraType(net.minecraft.client.CameraType.FIRST_PERSON));
        }
    }
}
