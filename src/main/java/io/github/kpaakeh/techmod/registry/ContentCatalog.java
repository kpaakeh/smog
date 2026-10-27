package io.github.kpaakeh.techmod.registry;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import io.github.kpaakeh.techmod.fluid.ModFluids;
import io.github.kpaakeh.techmod.machine.*;
import io.github.kpaakeh.techmod.multiblock.*;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** One declarative catalog drives runtime registrations and build-time assets/data generation. */
public final class ContentCatalog {
    private ContentCatalog() {}
    private static float hardness(JsonObject o) { return o.has("hardness") ? o.get("hardness").getAsFloat() : 3; }
    public static void initialize() {
        try (var stream = ContentCatalog.class.getResourceAsStream("/tech-mod.content.json")) {
            if (stream == null) throw new IllegalStateException("Missing content catalog");
            JsonObject root = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            for (var value : root.getAsJsonArray("blocks")) {
                var o = value.getAsJsonObject(); ModBlocks.register(o.get("name").getAsString(), BlockBehaviour.Properties.of().strength(hardness(o)).requiresCorrectToolForDrops());
            }
            for (var value : root.getAsJsonArray("ores")) {
                var o = value.getAsJsonObject(); String name = o.get("name").getAsString();
                ModOres.register(name, hardness(o), hardness(o));
                var biomes = switch (o.get("biomes").getAsString()) {
                    case "overworld" -> BiomeSelectors.foundInOverworld();
                    case "nether" -> BiomeSelectors.foundInTheNether();
                    case "end" -> BiomeSelectors.foundInTheEnd();
                    default -> throw new IllegalArgumentException("Unknown biome selection");
                };
                ModOres.generation(name, biomes);
            }
            for (var value : root.getAsJsonArray("multiblocks")) {
                var o = value.getAsJsonObject(); Map<BlockPos, BlockIngredient> parts = new LinkedHashMap<>();
                for (var partValue : o.getAsJsonArray("parts")) {
                    var p = partValue.getAsJsonObject(); var pos = new BlockPos(p.get("x").getAsInt(), p.get("y").getAsInt(), p.get("z").getAsInt());
                    if (parts.put(pos, new BlockIngredient(p.get("block").getAsString())) != null) throw new IllegalArgumentException("Duplicate structure position");
                }
                Multiblocks.register(new MultiblockDefinition(ModBlocks.id(o.get("name").getAsString()), parts));
            }
            for (var value : root.getAsJsonArray("machines")) {
                var o = value.getAsJsonObject(); Identifier structure = o.has("multiblock") ? Identifier.parse(o.get("multiblock").getAsString()) : null;
                if (structure != null && Multiblocks.get(structure) == null) throw new IllegalArgumentException("Unknown multiblock " + structure);
                Machines.register(o.get("name").getAsString(), BlockBehaviour.Properties.of().strength(hardness(o)).requiresCorrectToolForDrops(),
                    new MachineDefinition(structure, 20, (level, machine) -> machine.progress(machine.progress() == Integer.MAX_VALUE ? 0 : machine.progress() + 1)));
            }
            for (var value : root.getAsJsonArray("fluids")) {
                var o = value.getAsJsonObject(); ModFluids.register(o.get("name").getAsString(), Integer.parseInt(o.get("color").getAsString(), 16),
                    o.get("tick_delay").getAsInt(), o.get("infinite").getAsBoolean());
            }
            for (var structure : Multiblocks.all()) for (var ingredient : structure.parts().values()) {
                if (!ingredient.value().startsWith("#") && !net.minecraft.core.registries.BuiltInRegistries.BLOCK.containsKey(Identifier.parse(ingredient.value())))
                    throw new IllegalArgumentException("Unknown multiblock block: " + ingredient.value());
            }
            CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> {
                for (var block : net.minecraft.core.registries.BuiltInRegistries.BLOCK)
                    if (net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals("tech_mod") && !block.asItem().getDefaultInstance().isEmpty()) output.accept(block);
                ModFluids.all().forEach(f -> output.accept(f.bucket));
            });
        } catch (IOException e) { throw new IllegalStateException("Unable to read content catalog", e); }
    }
}
