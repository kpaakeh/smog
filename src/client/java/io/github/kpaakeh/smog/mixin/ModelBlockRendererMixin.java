package io.github.kpaakeh.smog.mixin;

import io.github.kpaakeh.smog.render.ClientFormations;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModelBlockRenderer.class, priority = 1100)
public abstract class ModelBlockRendererMixin {
    @Inject(method = "tesselateBlock", at = @At("HEAD"), cancellable = true)
    private void smog$hideFormedParts(BlockQuadOutput output, float x, float y, float z,
        BlockAndTintGetter level, BlockPos pos, BlockState state, BlockStateModel model, long seed, CallbackInfo ci) {
        if (ClientFormations.hidden(pos)) ci.cancel();
    }
}
