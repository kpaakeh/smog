package io.github.kpaakeh.smog.mixin;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockModelRenderState.class)
public interface BlockModelRenderStateAccessor {
    @Accessor("renderType") void smog$renderType(RenderType renderType);
}
