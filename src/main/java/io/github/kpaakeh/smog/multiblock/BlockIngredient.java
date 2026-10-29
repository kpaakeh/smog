package io.github.kpaakeh.smog.multiblock;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import io.github.kpaakeh.smog.registry.ModTags;

/** A concrete block, #block_tag, or minecraft:air (required empty space). */
public record BlockIngredient(String value) {
    public BlockIngredient { Identifier.parse(value.startsWith("#") ? value.substring(1) : value); }
    public boolean matches(BlockState state) {
        return value.startsWith("#") ? state.is(tag()) : state.is(block());
    }
    public TagKey<Block> tag() { return ModTags.block(value.substring(1)); }
    public Block block() { if(value.startsWith("#")){var choices=BuiltInRegistries.BLOCK.getTagOrEmpty(tag()).iterator();if(!choices.hasNext())throw new IllegalStateException("Empty block tag: "+value);return choices.next().value();}return BuiltInRegistries.BLOCK.getValue(Identifier.parse(value)); }
    public List<ItemStack> displayItems() {
        if (!value.startsWith("#")) return block().asItem().getDefaultInstance().isEmpty() ? List.of() : List.of(block().asItem().getDefaultInstance());
        return java.util.stream.StreamSupport.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(tag()).spliterator(), false)
            .map(h -> h.value().asItem().getDefaultInstance()).filter(s -> !s.isEmpty()).toList();
    }
}
