package io.github.kpaakeh.smog.registry;

import java.util.*;
import java.util.function.Predicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;

/** Worldgen JSON is generated from the matching content catalog entry during build. */
public final class ModOres {
    public record Ore(String name, Block stone, Block deepslate, Item raw, Block rawBlock,
                      Item product, Block storageBlock, Item dust, Item crushed, Item shard, Item nugget, Item plate) {
        public boolean metallic(){return product!=null;}
        public List<ItemLike> contents() { return java.util.stream.Stream.<ItemLike>of(stone,deepslate,raw,rawBlock,product,storageBlock,dust,crushed,shard,nugget,plate).filter(Objects::nonNull).toList(); }
    }
    private static final List<Ore> VALUES = new ArrayList<>();
    private ModOres() {}
    public static List<Ore> all() { return List.copyOf(VALUES); }
    private static Item item(String name) {
        var id = ModBlocks.id(name);
        return Registry.register(BuiltInRegistries.ITEM, id, new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    }
    public static Ore register(String name, String product, float hardness, float resistance) {
        if (!Set.of("ingot", "gem", "none").contains(product)) throw new IllegalArgumentException("Material product must be ingot or gem");
        Block stone = ModBlocks.register(name + "_ore", BlockBehaviour.Properties.of().strength(hardness, resistance).requiresCorrectToolForDrops());
        Block deep = ModBlocks.register("deepslate_" + name + "_ore", BlockBehaviour.Properties.of().strength(hardness * 1.5f, resistance).requiresCorrectToolForDrops());
        var ore = new Ore(name, stone, deep, item("raw_" + name),
            ModBlocks.register("raw_" + name + "_block", BlockBehaviour.Properties.of().strength(5, 6).requiresCorrectToolForDrops()),
            product.equals("none")?null:item(name + "_" + product), product.equals("none")?null:ModBlocks.register(name + "_block", BlockBehaviour.Properties.of().strength(5, 6).requiresCorrectToolForDrops()),
            item(name + "_dust"), item("crushed_" + name + "_ore"), item(name + "_shard"), product.equals("none")?null:item(name + "_nugget"), product.equals("none")?null:item(name + "_plate"));
        VALUES.add(ore); return ore;
    }
    public static void generation(String name, Predicate<BiomeSelectionContext> biomes) {
        BiomeModifications.addFeature(biomes, GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, ModBlocks.id(name + "_ore")));
    }
}
