package io.github.kpaakeh.smog.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.OverlayTexture;

/** One depth-tested offscreen scene for every block: real 3D, not projected item icons. */
public final class StructurePreviewRenderer extends PictureInPictureRenderer<StructurePreviewState> {
    @Override public Class<StructurePreviewState> getRenderStateClass() { return StructurePreviewState.class; }
    @Override protected String getTextureLabel() { return "smog structure preview"; }
    @Override protected float getTranslateY(int height, int guiScale) { return height / 2f; }
    @Override protected void renderToTexture(StructurePreviewState state, PoseStack pose, SubmitNodeCollector collector) {
        Minecraft.getInstance().gameRenderer.lighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
        var b = state.size();
        pose.scale(1,-1,1);
        pose.rotate(Axis.XP.rotationDegrees(state.pitch())); pose.rotate(Axis.YP.rotationDegrees(state.yaw()));
        pose.translate(-(b.minX()+b.maxX()+1)/2f, -(b.minY()+b.maxY()+1)/2f, -(b.minZ()+b.maxZ()+1)/2f);
        for (var part : state.parts()) {
            pose.pushPose();
            if (state.formed()) { pose.translate(b.minX(),b.minY(),b.minZ()); pose.scale(b.width(),b.height(),b.depth()); }
            else pose.translate(part.pos().getX(),part.pos().getY(),part.pos().getZ());
            part.model().submit(pose,collector,15728880,OverlayTexture.NO_OVERLAY,0);
            pose.popPose();
        }
    }
}
