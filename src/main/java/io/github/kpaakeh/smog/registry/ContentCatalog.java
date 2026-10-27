package io.github.kpaakeh.smog.registry;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import io.github.kpaakeh.smog.fluid.ModFluids;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.multiblock.*;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** One declarative catalog drives runtime registrations and build-time assets/data generation. */
public final class ContentCatalog {
    private ContentCatalog() {}
    private static float hardness(JsonObject o) { return o.has("hardness") ? o.get("hardness").getAsFloat() : 3; }
    public static void initialize() {
        try (var stream = ContentCatalog.class.getResourceAsStream("/smog.content.json")) {
            if (stream == null) throw new IllegalStateException("Missing content catalog");
            JsonObject root = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            for (var value : root.getAsJsonArray("blocks")) {
                var o = value.getAsJsonObject(); ModBlocks.register(o.get("name").getAsString(), BlockBehaviour.Properties.of().strength(hardness(o)).noOcclusion().requiresCorrectToolForDrops());
            }
            for (var value : root.getAsJsonArray("ores")) {
                var o = value.getAsJsonObject(); String name = o.get("name").getAsString();
                ModOres.register(name, o.has("product") ? o.get("product").getAsString() : "ingot", hardness(o), hardness(o));
                var biomes = switch (o.get("biomes").getAsString()) {
                    case "overworld" -> BiomeSelectors.foundInOverworld();
                    case "nether" -> BiomeSelectors.foundInTheNether();
                    case "end" -> BiomeSelectors.foundInTheEnd();
                    default -> throw new IllegalArgumentException("Unknown biome selection");
                };
                ModOres.generation(name, biomes);
            }
            if(root.has("alloys"))for(var value:root.getAsJsonArray("alloys"))ModAlloys.register(value.getAsJsonObject().get("name").getAsString());
            for (var value : root.getAsJsonArray("multiblocks")) {
                var o = value.getAsJsonObject(); Map<BlockPos, BlockIngredient> parts = new LinkedHashMap<>();
                for (var partValue : o.getAsJsonArray("parts")) {
                    var p = partValue.getAsJsonObject(); var pos = new BlockPos(p.get("x").getAsInt(), p.get("y").getAsInt(), p.get("z").getAsInt());
                    if (parts.put(pos, new BlockIngredient(p.get("block").getAsString())) != null) throw new IllegalArgumentException("Duplicate structure position");
                }
                Multiblocks.register(new MultiblockDefinition(ModBlocks.id(o.get("name").getAsString()), parts));
            }
            if(root.has("items"))for(var value:root.getAsJsonArray("items")){
                var o=value.getAsJsonObject();ModItems.register(o.get("name").getAsString(),o.has("kind")?o.get("kind").getAsString():"item",
                    o.has("conductor")?o.get("conductor").getAsString():"copper",o.has("tab")?o.get("tab").getAsString():"ores");
            }
            for (var value : root.getAsJsonArray("machines")) {
                var o = value.getAsJsonObject(); Identifier structure = o.has("multiblock") ? Identifier.parse(o.get("multiblock").getAsString()) : null;
                if (structure != null && Multiblocks.get(structure) == null) throw new IllegalArgumentException("Unknown multiblock " + structure);
                var kind=o.has("kind")?MachineSpec.Kind.valueOf(o.get("kind").getAsString()):MachineSpec.Kind.DECORATIVE;
                var spec=new MachineSpec(kind,o.has("transfer_rate")?o.get("transfer_rate").getAsInt():32,
                    o.has("capacity")?o.get("capacity").getAsInt():64000,o.has("work_ticks")?o.get("work_ticks").getAsInt():100,o.has("cost")?o.get("cost").getAsInt():4,o.has("conductor")?io.github.kpaakeh.smog.energy.ConductorMaterial.valueOf(o.get("conductor").getAsString().toUpperCase(java.util.Locale.ROOT)):null);
                Machines.register(o.get("name").getAsString(), BlockBehaviour.Properties.of().strength(hardness(o)).noOcclusion().requiresCorrectToolForDrops(),
                    new MachineDefinition(structure, 20, (level, machine) -> machine.progress(machine.progress() == Integer.MAX_VALUE ? 0 : machine.progress() + 1)),spec);
            }
            for (var value : root.getAsJsonArray("fluids")) {
                var o = value.getAsJsonObject(); ModFluids.register(o.get("name").getAsString(), Integer.parseInt(o.get("color").getAsString(), 16),
                    o.get("tick_delay").getAsInt(), o.get("infinite").getAsBoolean());
            }
            for (var structure : Multiblocks.all()) for (var ingredient : structure.parts().values()) {
                if (!ingredient.value().startsWith("#") && !net.minecraft.core.registries.BuiltInRegistries.BLOCK.containsKey(Identifier.parse(ingredient.value())))
                    throw new IllegalArgumentException("Unknown multiblock block: " + ingredient.value());
            }
            ModCreativeTabs.initialize();
        } catch (IOException e) { throw new IllegalStateException("Unable to read content catalog", e); }
    }
}
