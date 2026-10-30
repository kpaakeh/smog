package io.github.kpaakeh.smog.registry;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class ModBlocks {
    private ModBlocks() {}
    public static Identifier id(String path) { return Identifier.fromNamespaceAndPath("smog", path); }

    public static Block register(String name, BlockBehaviour.Properties properties) {
        return register(name, Block::new, properties, true);
    }

    public static <B extends Block> B register(String name, Function<BlockBehaviour.Properties, B> factory,
                                              BlockBehaviour.Properties properties, boolean withItem) {
        Identifier id = id(name);
        B block = factory.apply(properties.setId(ResourceKey.create(Registries.BLOCK, id)));
        Registry.register(BuiltInRegistries.BLOCK, id, block);
        if(withItem){var itemProperties=new Item.Properties().setId(ResourceKey.create(Registries.ITEM,id)).useBlockDescriptionPrefix();Registry.register(BuiltInRegistries.ITEM,id,name.equals("rope_anchor")||name.equals("loading_crane")?new WorkshopControlItem(block,itemProperties):new BlockItem(block,itemProperties));}
        return block;
    }
}
