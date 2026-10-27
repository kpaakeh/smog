package io.github.kpaakeh.smog.render;

import io.github.kpaakeh.smog.mixin.BlockModelRenderStateAccessor;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;

public final class ModelRendering {
    private ModelRendering() {}
    public static void enableDepth(BlockModelRenderState state) {
        // The default block-item sheet disables depth writes; it is unsuitable for a shared 3D scene.
        ((BlockModelRenderStateAccessor)state).smog$renderType(RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS));
    }
}
