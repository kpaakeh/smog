package io.github.kpaakeh.techmod.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

/** Tag declarations. Membership lives in data/<namespace>/tags/<registry>/<path>.json. */
public final class ModTags {
    private ModTags() {}
    public static <T> TagKey<T> of(net.minecraft.resources.ResourceKey<? extends net.minecraft.core.Registry<T>> registry, String id) {
        return TagKey.create(registry, Identifier.parse(id));
    }
    public static TagKey<Block> block(String id) { return TagKey.create(Registries.BLOCK, Identifier.parse(id)); }
    public static TagKey<Item> item(String id) { return TagKey.create(Registries.ITEM, Identifier.parse(id)); }
    public static TagKey<Fluid> fluid(String id) { return TagKey.create(Registries.FLUID, Identifier.parse(id)); }
    public static final TagKey<Block> MACHINE_CASINGS = block("tech_mod:machine_casings");
    public static final TagKey<Block> ORES = block("tech_mod:ores");
    public static final TagKey<Item> ORE_ITEMS = item("tech_mod:ores");
    public static final TagKey<Fluid> COOLANTS = fluid("tech_mod:coolants");
}
