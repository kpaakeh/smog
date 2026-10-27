package io.github.kpaakeh.smog.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.multiblock.MultiblockDefinition;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

/** One normalized model stretched over the recipe's exact bounds, anchored and rotated at its controller. */
public final class MachineRenderer implements BlockEntityRenderer<MachineEntity, MachineRenderer.State> {
    public static final class State extends BlockEntityRenderState {
        public BlockModelRenderState model = new BlockModelRenderState();
        public MultiblockDefinition.Bounds bounds;
        public float angle;
        public boolean formed;
        public java.util.List<io.github.kpaakeh.smog.energy.WireLink> wires=java.util.List.of();
    }
    private final BlockModelResolver models;
    private static final BlockDisplayContext DISPLAY = BlockDisplayContext.create();
    public MachineRenderer(BlockEntityRendererProvider.Context context) { models = context.blockModelResolver(); }
    @Override public State createRenderState() { return new State(); }
    @Override public void extractRenderState(MachineEntity machine, State state, float tick, Vec3 camera, ModelFeatureRenderer.CrumblingOverlay overlay) {
        BlockEntityRenderer.super.extractRenderState(machine, state, tick, camera, overlay);
        state.wires=machine.spec().kind()==MachineSpec.Kind.CONNECTOR?ClientWires.from(machine.getBlockPos()):java.util.List.of();
        if(!state.wires.isEmpty())state.lightCoords=net.minecraft.util.LightCoordsUtil.pack(machine.getLevel().getBrightness(net.minecraft.world.level.LightLayer.BLOCK,machine.getBlockPos().relative(ConnectorBlock.attachment(machine.getBlockState()))),machine.getLevel().getBrightness(net.minecraft.world.level.LightLayer.SKY,machine.getBlockPos().relative(ConnectorBlock.attachment(machine.getBlockState()))));
        state.formed = machine.structure() != null && machine.assembled() && ClientFormations.controller(machine.getBlockPos());
        if (!state.formed) return;
        state.bounds = machine.structure().bounds();
        var exterior = machine.getBlockPos().above(state.bounds.maxY()+1);
        var level = machine.getLevel();
        state.lightCoords = net.minecraft.util.LightCoordsUtil.pack(
            level.getBrightness(net.minecraft.world.level.LightLayer.BLOCK,exterior),
            level.getBrightness(net.minecraft.world.level.LightLayer.SKY,exterior));
        state.angle = switch (machine.getBlockState().getValue(MachineBlock.FACING)) {
            case EAST -> -90; case SOUTH -> -180; case WEST -> -270; default -> 0;
        };
        models.update(state.model, machine.getBlockState().setValue(MachineBlock.FACING, Direction.NORTH), DISPLAY);
        ModelRendering.enableDepth(state.model);
    }
    @Override public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        if(!state.wires.isEmpty())WireRendering.submit(state.wires,state.blockPos,pose,collector,state.lightCoords);
        if (!state.formed) return;
        var b = state.bounds;
        pose.pushPose(); pose.translate(.5, 0, .5); pose.rotate(Axis.YP.rotationDegrees(state.angle)); pose.translate(-.5, 0, -.5);
        pose.translate(b.minX(), b.minY(), b.minZ()); pose.scale(b.width(), b.height(), b.depth());
        state.model.submit(pose, collector, state.lightCoords, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, 0);
        pose.popPose();
    }
    @Override public boolean shouldRenderOffScreen() { return true; }
    @Override public int getViewDistance() { return 160; }
}
