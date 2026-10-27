package io.github.kpaakeh.techmod.fluid;

import java.util.*;
import io.github.kpaakeh.techmod.registry.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.*;

/** Water-like fluids with finite/infinite source conversion, configurable speed and tint. */
public final class ModFluids {
    public static final class Family {
        public final String name;
        public final int color, tickDelay;
        public final boolean infinite;
        public FlowingFluid source, flowing;
        public LiquidBlock block;
        public Item bucket;
        private Family(String name, int color, int delay, boolean infinite) {
            if (delay < 1) throw new IllegalArgumentException("Fluid tick delay must be positive");
            this.name = name; this.color = color; this.tickDelay = delay; this.infinite = infinite;
        }
    }
    private static final List<Family> VALUES = new ArrayList<>();
    private ModFluids() {}
    public static List<Family> all() { return List.copyOf(VALUES); }
    public static Family register(String name, int color, int tickDelay, boolean infinite) {
        Family f = new Family(name, color, tickDelay, infinite);
        f.source = Registry.register(BuiltInRegistries.FLUID, ModBlocks.id(name), new ModFluid.Source(f));
        f.flowing = Registry.register(BuiltInRegistries.FLUID, ModBlocks.id("flowing_" + name), new ModFluid.Flowing(f));
        f.block = ModBlocks.register(name, p -> new FluidBlock(f.source, p),
            BlockBehaviour.Properties.of().noCollision().strength(100).noLootTable().liquid().replaceable(), false);
        var id = ModBlocks.id(name + "_bucket");
        f.bucket = Registry.register(BuiltInRegistries.ITEM, id, new BucketItem(f.source,
            new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id)).stacksTo(1).craftRemainder(Items.BUCKET)));
        VALUES.add(f); return f;
    }
    private static final class FluidBlock extends LiquidBlock {
        private FluidBlock(FlowingFluid fluid, BlockBehaviour.Properties properties) { super(fluid, properties); }
    }
}
