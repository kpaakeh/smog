package io.github.kpaakeh.smog.test;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.multiblock.*;
import io.github.kpaakeh.smog.energy.*;
import io.github.kpaakeh.smog.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Deliberately test-only. None of these registrations or resources enter the release JAR. */
public final class TestIndustryContent implements ModInitializer {
    private static final Set<Item> ITEMS=Collections.newSetFromMap(new IdentityHashMap<>());
    public static boolean fixture(Item item){return ITEMS.contains(item);}
    @Override public void onInitialize() {
        try(var stream=TestIndustryContent.class.getResourceAsStream("/smog-test-industry.json")) {
            var root=JsonParser.parseReader(new InputStreamReader(Objects.requireNonNull(stream),StandardCharsets.UTF_8)).getAsJsonObject();
            for(var value:root.getAsJsonArray("blocks")) {
                var entry=value.getAsJsonObject();ITEMS.add(ModBlocks.register(entry.get("name").getAsString(),BlockBehaviour.Properties.of().strength(3).noOcclusion().requiresCorrectToolForDrops()).asItem());
            }
            for(var value:root.getAsJsonArray("multiblocks")) {
                var entry=value.getAsJsonObject();Map<BlockPos,BlockIngredient> parts=new LinkedHashMap<>();
                for(var part:entry.getAsJsonArray("parts")){var p=part.getAsJsonObject();parts.put(new BlockPos(p.get("x").getAsInt(),p.get("y").getAsInt(),p.get("z").getAsInt()),new BlockIngredient(p.get("block").getAsString()));}
                Multiblocks.register(new MultiblockDefinition(ModBlocks.id(entry.get("name").getAsString()),parts));
            }
            for(var value:root.getAsJsonArray("items")) {
                var entry=value.getAsJsonObject();ITEMS.add(ModItems.register(entry.get("name").getAsString(),entry.get("kind").getAsString(),entry.has("conductor")?entry.get("conductor").getAsString():"copper","test_only"));
            }
            for(var value:root.getAsJsonArray("machines")) {
                var e=value.getAsJsonObject();var structure=e.has("multiblock")?Identifier.parse(e.get("multiblock").getAsString()):null;
                var spec=new MachineSpec(e.has("kind")?MachineSpec.Kind.valueOf(e.get("kind").getAsString()):MachineSpec.Kind.DECORATIVE,
                    e.has("transfer_rate")?e.get("transfer_rate").getAsInt():32,e.has("capacity")?e.get("capacity").getAsInt():64000,
                    e.has("work_ticks")?e.get("work_ticks").getAsInt():100,e.has("cost")?e.get("cost").getAsInt():4,
                    e.has("conductor")?ConductorMaterial.valueOf(e.get("conductor").getAsString().toUpperCase(Locale.ROOT)):null);
                var block=Machines.register(e.get("name").getAsString(),BlockBehaviour.Properties.of().strength(3).noOcclusion().requiresCorrectToolForDrops(),
                    new MachineDefinition(structure,20,(level,machine)->machine.progress(machine.progress()==Integer.MAX_VALUE?0:machine.progress()+1)),spec);
                ITEMS.add(block.asItem());
            }
        } catch(IOException exception){throw new IllegalStateException(exception);}
    }
}
