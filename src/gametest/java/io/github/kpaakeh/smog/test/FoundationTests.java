package io.github.kpaakeh.smog.test;

import com.mojang.serialization.JsonOps;
import java.util.*;
import io.github.kpaakeh.smog.fluid.ModFluids;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.multiblock.*;
import io.github.kpaakeh.smog.registry.*;
import io.github.kpaakeh.smog.zone.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

public class FoundationTests {
    @GameTest public void materialFamiliesRecipesAndWorldgen(GameTestHelper h) {
        var level=h.getLevel();
        h.assertValueEqual(ModOres.all().size(),13,"Thirteen ore families");
        for (var ore:ModOres.all()) {
            h.assertValueEqual(ore.contents().size(),ore.metallic()?11:7,"Complete material family");
            for (var item:ore.contents()) h.assertTrue(item.asItem()!=net.minecraft.world.item.Items.AIR,"Material form has an item");
            var recipe=level.recipeAccess().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING,
                net.minecraft.world.item.crafting.CraftingInput.of(3,3,java.util.stream.IntStream.range(0,9).mapToObj(i->ore.raw().getDefaultInstance()).toList()),level).orElseThrow();
            h.assertTrue(recipe.value().assemble(net.minecraft.world.item.crafting.CraftingInput.of(3,3,
                java.util.stream.IntStream.range(0,9).mapToObj(i->ore.raw().getDefaultInstance()).toList())).is(ore.rawBlock().asItem()),"Raw packing recipe");
            var unpackInput=net.minecraft.world.item.crafting.CraftingInput.of(1,1,List.of(ore.rawBlock().asItem().getDefaultInstance()));
            var unpack=level.recipeAccess().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING,unpackInput,level).orElseThrow().value().assemble(unpackInput);
            h.assertTrue(unpack.is(ore.raw()) && unpack.getCount()==9,"Raw unpacking conserves material");
            var cookInput=new net.minecraft.world.item.crafting.SingleRecipeInput(ore.dust().getDefaultInstance());
            var smelting=level.recipeAccess().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.SMELTING,cookInput,level);
            if(ore.metallic()) {var smelt=smelting.orElseThrow().value().assemble(cookInput);h.assertTrue(smelt.is(ore.product())&&smelt.getCount()==1,"Dust smelts to its product");}
            else h.assertTrue(smelting.isEmpty(),"Mineral ore has no invented direct ingot smelting");
            var placed=level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.PLACED_FEATURE)
                .getOrThrow(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.PLACED_FEATURE,ModBlocks.id(ore.name()+"_ore"))).value();
            h.assertTrue(level.getBiome(h.absolutePos(new BlockPos(3,3,3))).value().getGenerationSettings().hasFeature(placed),"Ore is injected into overworld biome");
            for(int x=0;x<8;x++)for(int y=0;y<8;y++)for(int z=0;z<8;z++)h.setBlock(x,y,z,Blocks.STONE);
            for(int seed=0;seed<20;seed++)placed.feature().value().place(level,level.getChunkSource().getGenerator(),net.minecraft.util.RandomSource.create(seed),h.absolutePos(new BlockPos(4,4,4)));
            boolean found=false;
            for(int x=0;x<8;x++)for(int y=0;y<8;y++)for(int z=0;z<8;z++)if(h.getBlockState(new BlockPos(x,y,z)).is(ore.stone()))found=true;
            h.assertTrue(found,"Ore feature really creates blocks for "+ore.name());
        }
        net.minecraft.world.item.CreativeModeTabs.tryRebuildTabContents(level.enabledFeatures(),true,level.registryAccess());
        for(String tab:List.of("ores","blocks"))h.assertTrue(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id(tab)).hasAnyItems(),"Creative tab has contents: "+tab);
        h.assertValueEqual(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id("ores")).getDisplayItems().size(),249,"All forms are in ore tab");
        h.succeed();
    }
    @GameTest public void zoneGeometryMovementAndPersistence(GameTestHelper h) {
        Zone sphere = Zone.sphere(ModBlocks.id("freezing"), Vec3.ZERO, 4);
        h.assertTrue(sphere.contains(new Vec3(4,0,0)), "Sphere includes boundary");
        h.assertFalse(sphere.contains(new Vec3(4,4,0)), "Sphere excludes bounding box corners");
        Zone dynamic = new Zone("moving", sphere.type(), Zone.Shape.BOX, Vec3.ZERO, new Vec3(2,3,4),
            new Vec3(1,0,0), new Vec3(0.5,0,0), 200, 0.5f);
        Zone moved = dynamic.advance();
        h.assertValueEqual(moved.center(), new Vec3(1,0,0), "Movement");
        h.assertValueEqual(moved.halfSize(), new Vec3(2.5,3,4), "Growth");
        h.assertTrue(moved.contains(new Vec3(3.5,3,4)), "Box boundary");
        var encoded = Zone.CODEC.encodeStart(JsonOps.INSTANCE, moved).getOrThrow();
        h.assertValueEqual(Zone.CODEC.parse(JsonOps.INSTANCE, encoded).getOrThrow(), moved, "Zone serialization roundtrip");
        ZoneData data = new ZoneData(); data.put(dynamic);
        var saved = ZoneData.TYPE.codec().encodeStart(JsonOps.INSTANCE, data).getOrThrow();
        h.assertValueEqual(ZoneData.TYPE.codec().parse(JsonOps.INSTANCE, saved).getOrThrow().all(), data.all(), "SavedData roundtrip");
        data.tick(200);
        h.assertTrue(data.all().isEmpty(), "Expired zones removed");
        Zone shrinking = new Zone("shrinking", sphere.type(), sphere.shape(), Vec3.ZERO, new Vec3(1,1,1), Vec3.ZERO, new Vec3(-1,0,0), -1, 1);
        data.put(shrinking); data.tick(1);
        h.assertTrue(data.all().isEmpty(), "Collapsed zones removed");
        h.succeed();
    }

    @GameTest public void registryTagsAndFluids(GameTestHelper h) {
        var casing = BuiltInRegistries.BLOCK.getValue(ModBlocks.id("machine_casing"));
        h.assertTrue(casing.defaultBlockState().is(ModTags.MACHINE_CASINGS), "Generated block tag loaded");
        h.assertTrue(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("nickel_ore")).defaultBlockState().is(ModTags.ORES), "Ore registered and tagged");
        h.assertTrue(BuiltInRegistries.ITEM.getValue(ModBlocks.id("nickel_ore")).getDefaultInstance().is(ModTags.ORE_ITEMS), "Item tag loaded");
        h.getLevel().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.PLACED_FEATURE)
            .getOrThrow(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.PLACED_FEATURE, ModBlocks.id("nickel_ore")));
        var ore = BuiltInRegistries.BLOCK.getValue(ModBlocks.id("nickel_ore"));
        h.assertTrue(Block.getDrops(ore.defaultBlockState(), h.getLevel(), h.absolutePos(new BlockPos(0,1,0)), null)
            .stream().anyMatch(stack -> stack.is(BuiltInRegistries.ITEM.getValue(ModBlocks.id("raw_nickel")))), "Generated ore loot table drops the ore");
        var fluid = ModFluids.all().getFirst();
        h.assertTrue(fluid.source.isSame(fluid.flowing), "Fluid variants share identity");
        h.assertFalse(fluid.source.isSame(net.minecraft.world.level.material.Fluids.WATER), "Fluid identity remains distinct");
        h.assertTrue(fluid.source.defaultFluidState().is(ModTags.COOLANTS), "Fluid tag loaded");
        h.assertValueEqual(fluid.source.getBucket(), fluid.bucket, "Custom bucket");
        h.assertTrue(fluid.source.defaultFluidState().createLegacyBlock().is(fluid.block), "Fluid creates its own block");
        h.assertValueEqual(fluid.flowing.defaultFluidState().setValue(FlowingFluid.LEVEL, 5).getAmount(), 5, "Flowing level");
        h.succeed();
    }

    @GameTest public void structureRotationsAndBreakage(GameTestHelper h) {
        BlockPos origin = h.absolutePos(new BlockPos(2,2,2));
        var level = h.getLevel();
        Block casing = BuiltInRegistries.BLOCK.getValue(ModBlocks.id("machine_casing"));
        Block controller = BuiltInRegistries.BLOCK.getValue(ModBlocks.id("test_controller"));
        var definition = Multiblocks.get(ModBlocks.id("test_structure"));
        for (Rotation rotation : Rotation.values()) {
            level.setBlockAndUpdate(origin, controller.defaultBlockState().setValue(MachineBlock.FACING, rotation.rotate(Direction.NORTH)));
            definition.parts().forEach((p,ingredient) -> level.setBlockAndUpdate(origin.offset(p.rotate(rotation)),
                ingredient.value().equals("minecraft:air") ? Blocks.AIR.defaultBlockState() : casing.defaultBlockState()));
            h.assertValueEqual(definition.check(level, origin, rotation), MultiblockDefinition.Result.COMPLETE, "Rotation matches " + rotation);
            MachineEntity entity = (MachineEntity) level.getBlockEntity(origin);
            entity.serverTick();
            h.assertFalse(entity.assembled(), "Complete pattern waits for the configurator");
            h.assertTrue(entity.tryAssemble(), "Explicit assembly succeeds");
            h.assertTrue(level.getBlockState(origin).getValue(MachineBlock.ASSEMBLED), "Controller switches model state");
            BlockPos broken = origin.offset(new BlockPos(1,0,0).rotate(rotation));
            level.setBlockAndUpdate(broken, Blocks.AIR.defaultBlockState());
            int progress = entity.progress(); entity.serverTick();
            h.assertFalse(level.getBlockState(origin).getValue(MachineBlock.ASSEMBLED), "Dismantling reverts state");
            h.assertValueEqual(entity.progress(), progress, "Dismantled machine stops");
            definition.parts().keySet().forEach(p -> level.setBlockAndUpdate(origin.offset(p.rotate(rotation)), Blocks.AIR.defaultBlockState()));
        }
        h.succeed();
    }
}
