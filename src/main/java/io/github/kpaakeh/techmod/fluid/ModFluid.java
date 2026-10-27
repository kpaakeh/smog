package io.github.kpaakeh.techmod.fluid;

import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;

public abstract class ModFluid extends WaterFluid {
    protected final ModFluids.Family family;
    protected ModFluid(ModFluids.Family family) { this.family = family; }
    @Override public Fluid getSource() { return family.source; }
    @Override public Fluid getFlowing() { return family.flowing; }
    @Override public Item getBucket() { return family.bucket; }
    @Override public boolean isSame(Fluid other) { return other == family.source || other == family.flowing; }
    @Override protected boolean canConvertToSource(ServerLevel level) { return family.infinite; }
    @Override public int getTickDelay(LevelReader level) { return family.tickDelay; }
    @Override public BlockState createLegacyBlock(FluidState state) { return family.block.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state)); }
    @Override public boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos, Fluid other, Direction direction) {
        return direction == Direction.DOWN && !isSame(other);
    }
    public static final class Source extends ModFluid {
        public Source(ModFluids.Family family) { super(family); }
        @Override public int getAmount(FluidState state) { return 8; }
        @Override public boolean isSource(FluidState state) { return true; }
    }
    public static final class Flowing extends ModFluid {
        public Flowing(ModFluids.Family family) { super(family); }
        @Override protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) { super.createFluidStateDefinition(builder); builder.add(LEVEL); }
        @Override public int getAmount(FluidState state) { return state.getValue(LEVEL); }
        @Override public boolean isSource(FluidState state) { return false; }
    }
}
