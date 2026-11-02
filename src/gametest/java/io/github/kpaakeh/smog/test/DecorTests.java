package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.registry.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;

public final class DecorTests {
    @GameTest public void decorativeNativeGeometryConnectsWaterlogsAndDropsWithoutDuplication(GameTestHelper h){
        var level=h.getLevel();var pos=h.absolutePos(new BlockPos(3,2,3));
        var slab=BuiltInRegistries.BLOCK.getValue(ModBlocks.id("limestone_slab"));var wall=BuiltInRegistries.BLOCK.getValue(ModBlocks.id("mossy_limestone_bricks_wall"));var stairs=BuiltInRegistries.BLOCK.getValue(ModBlocks.id("limestone_stairs"));
        h.assertTrue(slab.defaultBlockState().setValue(SlabBlock.WATERLOGGED,true).getFluidState().isSource(),"Native slabs retain source water");
        h.assertTrue(stairs.defaultBlockState().setValue(StairBlock.WATERLOGGED,true).getFluidState().isSource(),"Native stairs retain source water");
        h.assertTrue(wall.defaultBlockState().setValue(WallBlock.WATERLOGGED,true).getFluidState().isSource(),"Native walls retain source water");
        level.setBlockAndUpdate(pos.below(),Blocks.STONE.defaultBlockState());level.setBlockAndUpdate(pos,wall.defaultBlockState());level.setBlockAndUpdate(pos.north(),wall.defaultBlockState());
        h.assertTrue(level.getBlockState(pos).getValue(WallBlock.NORTH)!=net.minecraft.world.level.block.state.properties.WallSide.NONE,"Adjacent native walls connect");
        level.setBlockAndUpdate(pos,stairs.defaultBlockState().setValue(StairBlock.FACING,Direction.NORTH));level.setBlockAndUpdate(pos.north(),stairs.defaultBlockState().setValue(StairBlock.FACING,Direction.WEST));
        h.assertTrue(level.getBlockState(pos).getValue(StairBlock.SHAPE)!=net.minecraft.world.level.block.state.properties.StairsShape.STRAIGHT,"Native stairs form their outer corner");
        var player=h.makeMockServerPlayerInLevel();var pickaxe=new ItemStack(Items.IRON_PICKAXE);
        var doubleSlab=slab.defaultBlockState().setValue(SlabBlock.TYPE,net.minecraft.world.level.block.state.properties.SlabType.DOUBLE);
        var slabDrops=Block.getDrops(doubleSlab,level,pos,null,player,pickaxe);
        h.assertValueEqual(slabDrops.stream().filter(s->s.is(slab.asItem())).mapToInt(ItemStack::getCount).sum(),2,"A double slab returns exactly its two paid slabs");
        var wallDrops=Block.getDrops(wall.defaultBlockState(),level,pos,null,player,pickaxe);
        h.assertValueEqual(wallDrops.stream().filter(s->s.is(wall.asItem())).mapToInt(ItemStack::getCount).sum(),1,"A decorative wall returns one item");
        CreativeModeTabs.tryRebuildTabContents(level.enabledFeatures(),true,level.registryAccess());
        for(var block:DecorBlocks.all()){
            int tabs=0;for(String tab:ModCreativeTabs.IDS)if(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id(tab)).getDisplayItems().stream().anyMatch(s->s.is(block.asItem()))){tabs++;h.assertTrue(tab.equals("blocks"),"Decorations appear only among building blocks");}
            h.assertValueEqual(tabs,1,"Each decorative block is discoverable exactly once");
        }
        h.succeed();
    }
    @GameTest public void decorativeFamiliesUseNativeShapesAndDoNotDuplicateTheirItems(GameTestHelper h){
        h.assertValueEqual(DecorBlocks.all().size(),210,"Eight complete families reuse existing refractory cubes");
        var names=new java.util.HashSet<net.minecraft.resources.Identifier>();
        for(var block:DecorBlocks.all()){
            var id=BuiltInRegistries.BLOCK.getKey(block);h.assertTrue(names.add(id),"Every decorative block has one registration");
            h.assertTrue(block.asItem()!=Items.AIR,"Every decorative block has a normal item");
            if(id.getPath().endsWith("_stairs"))h.assertTrue(block instanceof StairBlock&&block.defaultBlockState().hasProperty(StairBlock.WATERLOGGED),"Stairs use native waterlogging and shape rules");
            if(id.getPath().endsWith("_slab"))h.assertTrue(block instanceof SlabBlock,"Slabs support the native double-slab state");
            if(id.getPath().endsWith("_wall"))h.assertTrue(block instanceof WallBlock,"Walls use native neighbour connections");
        }
        h.succeed();
    }
    @GameTest public void newDecorativeCraftsAndStonecuttingHaveUsableMaterialInputs(GameTestHelper h){
        var stone=BuiltInRegistries.ITEM.getValue(ModBlocks.id("limestone"));var slabs=BuiltInRegistries.ITEM.getValue(ModBlocks.id("limestone_slab"));
        var slabInput=CraftingInput.of(3,1,java.util.List.of(new ItemStack(stone),new ItemStack(stone),new ItemStack(stone)));
        var result=h.getLevel().recipeAccess().getRecipeFor(RecipeType.CRAFTING,slabInput,h.getLevel()).orElseThrow().value().assemble(slabInput);
        h.assertTrue(result.is(slabs),"Three base blocks craft their native slabs");h.assertValueEqual(result.getCount(),6,"Slabs retain vanilla yield");
        var input=new SingleRecipeInput(new ItemStack(stone));
        h.assertTrue(h.getLevel().recipeAccess().getRecipeFor(RecipeType.STONECUTTING,input,h.getLevel()).isPresent(),"The vanilla stonecutter accepts the base decorative material");h.succeed();
    }
}
