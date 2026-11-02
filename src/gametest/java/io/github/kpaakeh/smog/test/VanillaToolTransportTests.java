package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.AABB;

/** Native loot and redstone contracts of existing workshop mechanics. */
public final class VanillaToolTransportTests {
    private static Block block(String id){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(id));}
    private static int count(net.minecraft.world.Container container,Item item){int total=0;for(int slot=0;slot<container.getContainerSize();slot++)if(container.getItem(slot).is(item))total+=container.getItem(slot).getCount();return total;}
    @GameTest public void hammerSecondaryBreaksHonorNativeSilkTouchAndSkipContainers(GameTestHelper h){
        var level=h.getLevel();var center=h.absolutePos(new BlockPos(4,3,4));
        for(int x=-1;x<=1;x++)for(int y=-1;y<=1;y++)level.setBlock(center.offset(x,y,0),Blocks.DIAMOND_ORE.defaultBlockState(),Block.UPDATE_ALL);
        var protectedPos=center.offset(1,1,0);level.setBlock(protectedPos,Blocks.FURNACE.defaultBlockState(),Block.UPDATE_ALL);var furnace=(FurnaceBlockEntity)level.getBlockEntity(protectedPos);furnace.setItem(0,new ItemStack(Items.DIAMOND,3));
        var player=(ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL);player.connection=h.makeMockServerPlayerInLevel().connection;player.setPos(center.getX()+.5,center.getY(),center.getZ()-3);player.setYRot(0);player.setXRot(0);
        var hammer=ToolParts.example(ToolDefinitions.template("hammer"),"iron");hammer.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH),1);player.setItemInHand(InteractionHand.MAIN_HAND,hammer);
        h.assertTrue(player.gameMode.destroyBlock(center),"Native primary break succeeds with a silk-touch assembled hammer");
        int ores=0,diamonds=0;for(var entity:level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class,new AABB(center).inflate(4))){if(entity.getItem().is(Items.DIAMOND_ORE))ores+=entity.getItem().getCount();if(entity.getItem().is(Items.DIAMOND))diamonds+=entity.getItem().getCount();}
        h.assertValueEqual(ores,8,"All eight mined blocks use vanilla Silk Touch loot, including secondary breaks");h.assertValueEqual(diamonds,0,"Secondary breaks cannot bypass Silk Touch into raw diamond drops");
        h.assertTrue(level.getBlockState(protectedPos).is(Blocks.FURNACE),"Secondary area does not dismantle a vanilla container");h.assertValueEqual(furnace.getItem(0).getCount(),3,"The protected furnace inventory is unchanged");h.assertValueEqual(hammer.getDamageValue(),8,"Only actually mined ore blocks consume durability");h.succeed();
    }
    @GameTest public void craneRedstoneUsesRisingEdgesAndKeepsLatchAcrossCurrentSave(GameTestHelper h){
        var level=h.getLevel();var origin=h.absolutePos(new BlockPos(3,5,1));Multiblocks.get(ModBlocks.id("loading_crane")).parts().forEach((offset,part)->level.setBlock(origin.offset(offset),part.block().defaultBlockState(),Block.UPDATE_ALL));level.setBlock(origin,block("loading_crane").defaultBlockState(),Block.UPDATE_ALL);var crane=(MachineEntity)level.getBlockEntity(origin);h.assertTrue(crane.tryAssemble(),"Crane assembles");
        var sourcePos=LoadingCrane.bay(crane,false);var targetPos=LoadingCrane.bay(crane,true);level.setBlock(sourcePos,Blocks.BARREL.defaultBlockState(),Block.UPDATE_ALL);level.setBlock(targetPos,Blocks.BARREL.defaultBlockState(),Block.UPDATE_ALL);var source=(BarrelBlockEntity)level.getBlockEntity(sourcePos);var target=(BarrelBlockEntity)level.getBlockEntity(targetPos);source.setItem(0,new ItemStack(Items.COBBLESTONE,64));source.setItem(1,new ItemStack(Items.COBBLESTONE,64));
        level.setBlock(origin.above(),Blocks.REDSTONE_BLOCK.defaultBlockState(),Block.UPDATE_ALL);for(int tick=0;tick<LoadingCrane.duration(crane)*2;tick++)LoadingCrane.tick(level,crane);
        h.assertValueEqual(count(target,Items.COBBLESTONE),64,"A held redstone signal starts exactly one finite load");h.assertValueEqual(count(source,Items.COBBLESTONE),64,"Continuous power never silently orders the next stack");
        var saved=crane.saveCustomOnly(level.registryAccess());crane.loadCustomOnly(TagValueInput.create(ProblemReporter.DISCARDING,level.registryAccess(),saved));for(int tick=0;tick<20;tick++)LoadingCrane.tick(level,crane);
        h.assertValueEqual(count(target,Items.COBBLESTONE),64,"Restoring a powered controller does not invent a new rising edge");
        level.setBlock(origin.above(),Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);LoadingCrane.tick(level,crane);level.setBlock(origin.above(),Blocks.REDSTONE_BLOCK.defaultBlockState(),Block.UPDATE_ALL);for(int tick=0;tick<LoadingCrane.duration(crane)+2;tick++)LoadingCrane.tick(level,crane);
        h.assertValueEqual(count(target,Items.COBBLESTONE),128,"A real second rising edge orders the remaining stack");h.assertTrue(source.isEmpty(),"Both finite loads preserve the exact source total");h.succeed();
    }
}
