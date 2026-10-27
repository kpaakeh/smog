package io.github.kpaakeh.smog.render;

import java.util.List;
import io.github.kpaakeh.smog.multiblock.MultiblockDefinition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.core.BlockPos;
import org.joml.Matrix3x2fc;

public record StructurePreviewState(List<Part> parts, MultiblockDefinition.Bounds size, boolean formed,
    float yaw, float pitch, int x0, int y0, int x1, int y1, float scale, Matrix3x2fc pose,
    ScreenRectangle scissorArea, ScreenRectangle bounds) implements PictureInPictureRenderState {
    public record Part(BlockPos pos, BlockModelRenderState model) {}
}
