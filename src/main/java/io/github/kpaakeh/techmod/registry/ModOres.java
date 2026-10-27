package io.github.kpaakeh.techmod.registry;

import java.util.function.Predicate;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;

/** Worldgen JSON is generated from the matching content catalog entry during build. */
public final class ModOres {
    public record Ore(Block stone, Block deepslate) {}
    private ModOres() {}
    public static Ore register(String name, float hardness, float resistance) {
        Block stone = ModBlocks.register(name + "_ore", BlockBehaviour.Properties.of().strength(hardness, resistance).requiresCorrectToolForDrops());
        Block deep = ModBlocks.register("deepslate_" + name + "_ore", BlockBehaviour.Properties.of().strength(hardness * 1.5f, resistance).requiresCorrectToolForDrops());
        return new Ore(stone, deep);
    }
    public static void generation(String name, Predicate<BiomeSelectionContext> biomes) {
        BiomeModifications.addFeature(biomes, GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, ModBlocks.id(name + "_ore")));
    }
}
