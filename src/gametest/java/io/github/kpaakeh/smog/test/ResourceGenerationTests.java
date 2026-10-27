package io.github.kpaakeh.smog.test;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import io.github.kpaakeh.smog.registry.*;
import io.github.kpaakeh.smog.fluid.ModFluids;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;

public final class ResourceGenerationTests {
    @GameTest public void depositsAndUndergroundFluidsReallyGenerate(GameTestHelper h) {
        h.assertFalse(BuiltInRegistries.BLOCK.containsKey(ModBlocks.id("sulfur")),"No duplicate Smog sulfur");
        h.assertTrue(BuiltInRegistries.BLOCK.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("sulfur"))==Blocks.SULFUR,"Native sulfur available");
        h.assertValueEqual(ModDeposits.all().size(),7,"Seven mod deposits; sulfur uses vanilla");
        for(var deposit:ModDeposits.all())placeFeature(h,deposit.name()+"_deposit",deposit.block());
        var oil=ModFluids.all().stream().filter(f->f.name.equals("crude_oil")).findFirst().orElseThrow();
        var brine=ModFluids.all().stream().filter(f->f.name.equals("mineral_brine")).findFirst().orElseThrow();
        placeFeature(h,"crude_oil_reservoir",oil.block);placeFeature(h,"mineral_brine_reservoir",brine.block);
        h.assertFalse(oil.source.defaultFluidState().is(FluidTags.WATER),"Oil is not tagged as water");
        h.assertTrue(brine.source.defaultFluidState().is(FluidTags.WATER),"Brine is an aqueous fluid");
        h.assertTrue(oil.source.getBucket()==oil.bucket&&brine.source.getBucket()==brine.bucket,"Both resources have usable buckets");
        h.succeed();
    }
    private static void placeFeature(GameTestHelper h,String name,Block expected) {
        var level=h.getLevel();var placed=level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE).getOrThrow(ResourceKey.create(Registries.PLACED_FEATURE,ModBlocks.id(name))).value();
        h.assertTrue(level.getBiome(h.absolutePos(new BlockPos(3,3,3))).value().getGenerationSettings().hasFeature(placed),"Resource injected in overworld biome: "+name);
        for(int x=0;x<12;x++)for(int y=0;y<12;y++)for(int z=0;z<12;z++)level.setBlock(h.absolutePos(new BlockPos(x,y,z)),Blocks.STONE.defaultBlockState(),Block.UPDATE_CLIENTS);
        placed.feature().value().place(level,level.getChunkSource().getGenerator(),RandomSource.create(42),h.absolutePos(new BlockPos(6,6,6)));
        int found=0;
        for(int x=0;x<12;x++)for(int y=0;y<12;y++)for(int z=0;z<12;z++)if(level.getBlockState(h.absolutePos(new BlockPos(x,y,z))).is(expected))found++;
        h.assertTrue(found<= (name.endsWith("reservoir")?1728:600),"Natural deposit stays bounded: "+name+" blocks="+found);
        h.assertTrue(found>16,"Deposit generates a usable amount: "+name+" blocks="+found);
    }
    @GameTest public void placersUseSedimentsAndBodiesUseRock(GameTestHelper h) {
        var level=h.getLevel();var pos=h.absolutePos(new BlockPos(4,4,4));
        var registry=level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
        var placer=registry.getOrThrow(ResourceKey.create(Registries.PLACED_FEATURE,ModBlocks.id("titanium_ore"))).value().feature().value();
        var body=registry.getOrThrow(ResourceKey.create(Registries.PLACED_FEATURE,ModBlocks.id("nickel_ore"))).value().feature().value();
        for(int x=0;x<9;x++)for(int y=0;y<9;y++)for(int z=0;z<9;z++)level.setBlock(h.absolutePos(new BlockPos(x,y,z)),Blocks.SAND.defaultBlockState(),Block.UPDATE_CLIENTS);
        h.assertFalse(body.place(level,level.getChunkSource().getGenerator(),RandomSource.create(7),pos),"Deep ore body does not replace sand");
        h.assertTrue(placer.place(level,level.getChunkSource().getGenerator(),RandomSource.create(7),pos),"Heavy-mineral placer forms in sediments");
        int titanium=0;
        for(int x=0;x<9;x++)for(int y=0;y<9;y++)for(int z=0;z<9;z++)if(level.getBlockState(h.absolutePos(new BlockPos(x,y,z))).is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("titanium_ore"))))titanium++;
        h.assertTrue(titanium>0&&titanium<=25,"Titanium placer is a small find, not a massive ore slab: "+titanium);
        h.succeed();
    }
    @GameTest public void activeCatalogContainsNoIndustry(GameTestHelper h) throws IOException {
        try(var stream=ResourceGenerationTests.class.getResourceAsStream("/smog.content.json")) {
            var root=JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
            h.assertTrue(root.getAsJsonObject("resource_policy").get("uranium_required").getAsBoolean(),"Uranium is mandatory for planned nuclear power");
            h.assertValueEqual(root.getAsJsonArray("ores").size(),13,"Requested ore count");h.assertValueEqual(root.getAsJsonArray("deposits").size(),7,"Requested deposit count");h.assertValueEqual(root.getAsJsonArray("underground_resources").size(),2,"Requested underground resource count");
            h.assertTrue(root.getAsJsonArray("machines").isEmpty()&&root.getAsJsonArray("multiblocks").isEmpty(),"Release catalog has no legacy machines or generators");
            for(var value:root.getAsJsonArray("items"))h.assertFalse(value.getAsJsonObject().has("kind"),"No old wire coils or configurator registered in release");
        }
        net.minecraft.world.item.CreativeModeTabs.tryRebuildTabContents(h.getLevel().enabledFeatures(),true,h.getLevel().registryAccess());
        h.assertValueEqual(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id("blocks")).getDisplayItems().size(),10,"Only seven natural blocks and three material buckets in release tab");
        for(var stack:BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id("blocks")).getDisplayItems())h.assertFalse(TestIndustryContent.fixture(stack.getItem()),"No test content in release tab");
        h.assertFalse(BuiltInRegistries.CREATIVE_MODE_TAB.containsKey(ModBlocks.id("machines")),"No empty industry tab");
        h.assertTrue(TestIndustryContent.fixture(BuiltInRegistries.ITEM.getValue(ModBlocks.id("electric_furnace"))),"System test fixture remains separate");
        h.succeed();
    }
}
