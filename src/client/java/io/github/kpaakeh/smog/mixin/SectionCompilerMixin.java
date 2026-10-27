package io.github.kpaakeh.smog.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.kpaakeh.smog.render.ClientFormations;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.material.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Skip terrain geometry before Fabric redirects tessellation into Indigo. Keep block entities. */
@Mixin(SectionCompiler.class)
public abstract class SectionCompilerMixin {
    @ModifyExpressionValue(method="compile",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/block/state/BlockState;getRenderShape()Lnet/minecraft/world/level/block/RenderShape;"))
    private RenderShape smog$hideModel(RenderShape original,@Local(ordinal=2) BlockPos pos) {
        return ClientFormations.hidden(pos)?RenderShape.INVISIBLE:original;
    }
    @ModifyExpressionValue(method="compile",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/block/state/BlockState;getFluidState()Lnet/minecraft/world/level/material/FluidState;"))
    private FluidState smog$hideFluid(FluidState original,@Local(ordinal=2) BlockPos pos) {
        return ClientFormations.hidden(pos)?Fluids.EMPTY.defaultFluidState():original;
    }
}
