package io.github.kpaakeh.smog.mixin;

import io.github.kpaakeh.smog.render.ClientFormations;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
    @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true)
    private void smog$hidePartEntities(BlockEntity entity, float tick, ModelFeatureRenderer.CrumblingOverlay overlay,
        boolean visible, CallbackInfoReturnable<BlockEntityRenderState> cir) {
        if (ClientFormations.hidden(entity.getBlockPos()) && !ClientFormations.controller(entity.getBlockPos())) cir.setReturnValue(null);
    }
}
