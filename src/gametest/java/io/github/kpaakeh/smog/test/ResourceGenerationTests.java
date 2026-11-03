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
    @GameTest public void veinsGenerateInBothHostRocks(GameTestHelper h) {
        var level=h.getLevel();var pos=h.absolutePos(new BlockPos(4,4,4));
        var registry=level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
        for(var ore:ModOres.all())for(boolean deep:new boolean[]{false,true}) {
            var feature=registry.getOrThrow(ResourceKey.create(Registries.PLACED_FEATURE,ModBlocks.id(ore.name()+"_ore"))).value().feature().value();
            h.assertFalse(feature instanceof io.github.kpaakeh.smog.worldgen.ResourceDepositFeature,"Ores use vanilla veins, never solid masses");
            for(int x=0;x<9;x++)for(int y=0;y<9;y++)for(int z=0;z<9;z++)level.setBlock(h.absolutePos(new BlockPos(x,y,z)),(deep?Blocks.DEEPSLATE:Blocks.STONE).defaultBlockState(),Block.UPDATE_CLIENTS);
            for(int seed=0;seed<20;seed++)feature.place(level,level.getChunkSource().getGenerator(),RandomSource.create(seed),pos);
            int found=0;
            for(int x=0;x<9;x++)for(int y=0;y<9;y++)for(int z=0;z<9;z++)if(level.getBlockState(h.absolutePos(new BlockPos(x,y,z))).is(deep?ore.deepslate():ore.stone()))found++;
            h.assertTrue(found>0,"Vein generates in its host rock: "+ore.name()+" deep="+deep);
        }
        h.succeed();
    }
    @GameTest public void copperCatalogContainsNoLegacyIndustry(GameTestHelper h) throws IOException {
        try(var stream=ResourceGenerationTests.class.getResourceAsStream("/smog.content.json")) {
            var root=JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
            h.assertTrue(root.getAsJsonObject("resource_policy").get("uranium_required").getAsBoolean(),"Uranium is mandatory for planned nuclear power");
            h.assertValueEqual(root.getAsJsonArray("ores").size(),13,"Requested ore count");h.assertValueEqual(root.getAsJsonArray("deposits").size(),7,"Requested deposit count");h.assertValueEqual(root.getAsJsonArray("underground_resources").size(),2,"Requested underground resource count");
            h.assertTrue(root.getAsJsonArray("machines").asList().stream().allMatch(v->v.getAsJsonObject().get("kind").getAsString().equals("COPPER_WORKSHOP")),"Only copper-era workshops are active");
            for(var value:root.getAsJsonArray("items"))h.assertFalse(value.getAsJsonObject().has("kind")&&value.getAsJsonObject().get("kind").getAsString().equals("wire"),"No electrical wire coils in copper release");
        }
        net.minecraft.world.item.CreativeModeTabs.tryRebuildTabContents(h.getLevel().enabledFeatures(),true,h.getLevel().registryAccess());
        var displayed=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id("blocks")).getDisplayItems();
        var ids=displayed.stream().map(stack->BuiltInRegistries.ITEM.getKey(stack.getItem())).collect(java.util.stream.Collectors.toSet());
        for(String required:java.util.List.of("limestone","kaolin","firebrick_casing","smeltery_glass"))h.assertTrue(ids.contains(ModBlocks.id(required)),"Required resource/workshop building part is discoverable: "+required);
        for(String forbidden:java.util.List.of("electric_furnace","crusher","solid_generator","generator_controller"))h.assertFalse(ids.contains(ModBlocks.id(forbidden)),"Legacy electrical industry stays outside the release building tab: "+forbidden);
        for(var stack:BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id("blocks")).getDisplayItems())h.assertFalse(TestIndustryContent.fixture(stack.getItem()),"No test content in release tab");
        h.assertFalse(BuiltInRegistries.CREATIVE_MODE_TAB.containsKey(ModBlocks.id("machines")),"Obsolete mixed industry tab replaced by mechanisms/controllers");
        h.assertTrue(TestIndustryContent.fixture(BuiltInRegistries.ITEM.getValue(ModBlocks.id("electric_furnace"))),"System test fixture remains separate");
        h.succeed();
    }
    @GameTest public void creativeCatalogIsCompleteAndToolsExistOnlyInTheirTabs(GameTestHelper h) throws IOException {
        net.minecraft.world.item.CreativeModeTabs.tryRebuildTabContents(h.getLevel().enabledFeatures(),true,h.getLevel().registryAccess());
        var expected=java.util.Set.of("copper_age","materials","blocks","mechanisms","controllers","tool_parts","tools");
        var actual=BuiltInRegistries.CREATIVE_MODE_TAB.keySet().stream().filter(id->id.getNamespace().equals("smog")).map(net.minecraft.resources.Identifier::getPath).collect(java.util.stream.Collectors.toSet());
        h.assertTrue(actual.equals(expected),"Functional tabs and the chapter overview exist");
        var owner=new java.util.HashMap<net.minecraft.world.item.Item,String>();int examples=0;
        for(String id:expected){var stacks=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id(id)).getDisplayItems();h.assertFalse(stacks.isEmpty(),"Category is useful: "+id);
            var seen=new java.util.ArrayList<net.minecraft.world.item.ItemStack>();
            for(var stack:stacks){
                h.assertFalse(TestIndustryContent.fixture(stack.getItem()),"Fixtures never leak into "+id+": "+BuiltInRegistries.ITEM.getKey(stack.getItem()));
                String previous=id.equals("copper_age")?null:owner.putIfAbsent(stack.getItem(),id);h.assertTrue(previous==null||previous.equals(id),"One category owns each item: "+BuiltInRegistries.ITEM.getKey(stack.getItem()));
                h.assertFalse(seen.stream().anyMatch(s->net.minecraft.world.item.ItemStack.isSameItemSameComponents(s,stack)),"No duplicate entry within "+id);seen.add(stack);
                if(stack.getItem() instanceof io.github.kpaakeh.smog.copper.ToolPartItem)h.assertTrue(id.equals("tool_parts"),"Final and cast parts belong only to Tool Parts");
                if(stack.getItem() instanceof io.github.kpaakeh.smog.copper.ModularToolItem){examples++;h.assertTrue(id.equals("tools"),"Modular tools belong only to Tools");h.assertTrue(io.github.kpaakeh.smog.copper.ModularToolItem.configured(stack),"No empty modular tool shells");}
            }
        }
        h.assertTrue(examples>0&&examples<=io.github.kpaakeh.smog.copper.ToolDefinitions.templates().size()*4,"Configured tools are a bounded selection, not thousands of combinations");
        for(var part:io.github.kpaakeh.smog.copper.ToolDefinitions.parts())h.assertTrue("tool_parts".equals(owner.get(BuiltInRegistries.ITEM.getValue(part.item()))),"Every registered cast/final tool part is discoverable in its own category");
        for(String name:java.util.List.of("configurator","freight_rope"))h.assertTrue("tools".equals(owner.get(BuiltInRegistries.ITEM.getValue(ModBlocks.id(name)))),"Hand tool belongs to Tools: "+name);
        for(String name:java.util.List.of("casting_faucet","water_valve","handcart","casting_table"))h.assertTrue("mechanisms".equals(owner.get(BuiltInRegistries.ITEM.getValue(ModBlocks.id(name)))),"Working mechanism is not mixed into building blocks: "+name);
        try(var stream=ResourceGenerationTests.class.getResourceAsStream("/smog.content.json")){
            var root=JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
            for(var value:root.getAsJsonArray("machines")){var machine=value.getAsJsonObject();var item=BuiltInRegistries.ITEM.getValue(ModBlocks.id(machine.get("name").getAsString()));h.assertTrue((machine.has("multiblock")?"controllers":"mechanisms").equals(owner.get(item)),"Only actual multiblock controllers enter Controllers");}
        }
        var audit=new com.google.gson.JsonObject();
        for(String category:expected){var entries=new com.google.gson.JsonArray();for(var stack:BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id(category)).getDisplayItems())entries.add(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());audit.add(category,entries);}
        var report=java.nio.file.Path.of("..","..","review","creative-catalog.json");java.nio.file.Files.createDirectories(report.getParent());java.nio.file.Files.writeString(report,new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(audit));
        var chapter=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id("copper_age")).getDisplayItems();
        for(String name:java.util.List.of("copper_hearth","ceramic_kiln","washing_trough","lever_press","loading_crane","copper_water_pipe","water_tank"))h.assertTrue(chapter.stream().anyMatch(stack->stack.is(BuiltInRegistries.ITEM.getValue(ModBlocks.id(name)))),"Chapter includes its controller or utility: "+name);
        for(var item:BuiltInRegistries.ITEM)if(BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("smog")&&!TestIndustryContent.fixture(item)&&!(item instanceof io.github.kpaakeh.smog.copper.ModularToolItem))h.assertTrue(owner.containsKey(item),"Every production item is discoverable: "+BuiltInRegistries.ITEM.getKey(item));
        h.succeed();
    }
    @GameTest public void russianNamesUseVanillaGrammarAndNoCatalogSeparators(GameTestHelper h) throws IOException {
        var production=net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer("smog").orElseThrow().findPath("assets/smog/lang/ru_ru.json").orElseThrow();
        try(var stream=java.nio.file.Files.newInputStream(production)){
            var names=JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
            var expected=java.util.Map.of("block.smog.limestone_bricks","Известняковые кирпичи","block.smog.limestone_bricks_stairs","Ступеньки из известняковых кирпичей","item.smog.copper_cast_saw_head","Отлитое медное полотно пилы","item.smog.cord_binding","Верёвочное крепление","item.smog.raw_tin","Рудное олово","item.smog.molten_copper_bucket","Ведро расплавленной меди","tool.smog.named.pickaxe.copper","Медная кирка");
            expected.forEach((key,value)->h.assertTrue(names.has(key)&&names.get(key).getAsString().equals(value),"Natural Russian name: "+key+" actual="+names.get(key)+" expected="+value+" resource="+production));
            for(var entry:names.entrySet())if((entry.getKey().startsWith("block.smog.")||entry.getKey().startsWith("item.smog."))&&!entry.getKey().contains(".help")&&!entry.getKey().contains(".unlink"))h.assertFalse(entry.getValue().getAsString().contains(":")||entry.getValue().getAsString().contains(" — "),"No catalog-style separator in item names: "+entry.getKey());
        }h.succeed();
    }
}
