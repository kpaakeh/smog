package io.github.kpaakeh.smog.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.kpaakeh.smog.bronze.*;
import java.util.List;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.phys.Vec3;

/** Moving JSON sub-models share the static model's palette, scale, origin and lighting. */
public final class BronzeRenderer implements BlockEntityRenderer<BronzeEntity,BronzeRenderer.State> {
    public static final class State extends BlockEntityRenderState {
        public ItemStackRenderState moving=new ItemStackRenderState();
        public String animation="none";
        public Direction facing=Direction.NORTH;
        public float angle;
    }
    private final ItemModelResolver items;
    public BronzeRenderer(BlockEntityRendererProvider.Context context){items=context.itemModelResolver();}
    @Override public State createRenderState(){return new State();}
    @Override public void extractRenderState(BronzeEntity entity,State state,float partial,Vec3 camera,ModelFeatureRenderer.CrumblingOverlay overlay){
        BlockEntityRenderer.super.extractRenderState(entity,state,partial,camera,overlay);
        state.animation=BronzeContent.entry(entity.id()).animation();state.facing=entity.getBlockState().getValue(BronzeBlock.FACING);state.angle=entity.angle(partial);
        if(entity.id().equals("bronze_shaft")||entity.id().equals("bronze_shaft_support"))state.facing=BronzeShaft.canonical(state.facing);
        if(entity.getBlockState().getValue(BronzeBlock.ASSEMBLED))state.animation="none";
        if(state.animation.equals("none"))return;
        var stack=new ItemStack(entity.getBlockState().getBlock());stack.set(DataComponents.CUSTOM_MODEL_DATA,new CustomModelData(List.of(),List.of(),List.of("moving"),List.of()));
        items.updateForTopItem(state.moving,stack,ItemDisplayContext.NONE,entity.getLevel(),null,0);
    }
    @Override public void submit(State state,PoseStack pose,SubmitNodeCollector collector,CameraRenderState camera){
        if(state.animation.equals("none"))return;
        pose.pushPose();pose.translate(.5,.5,.5);
        switch(state.facing){case EAST->pose.rotate(Axis.YP.rotationDegrees(-90));case SOUTH->pose.rotate(Axis.YP.rotationDegrees(-180));case WEST->pose.rotate(Axis.YP.rotationDegrees(-270));case UP->pose.rotate(Axis.XP.rotationDegrees(90));case DOWN->pose.rotate(Axis.XP.rotationDegrees(270));default->{}}
        switch(state.animation){
            case "rotate_x"->pose.rotate(Axis.XP.rotationDegrees(state.angle));
            case "rotate_y"->pose.rotate(Axis.YP.rotationDegrees(state.angle));
            case "rotate_z"->pose.rotate(Axis.ZP.rotationDegrees(state.angle));
            case "reciprocate"->pose.translate(0,.09*Math.sin(Math.toRadians(state.angle)),0);
            case "rock_z"->{pose.translate(-.375,.1875,0);pose.rotate(Axis.ZP.rotationDegrees(18*(float)Math.sin(Math.toRadians(state.angle))));pose.translate(.375,-.1875,0);}
            default->{}
        }
        state.moving.submit(pose,collector,state.lightCoords,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,0);pose.popPose();
    }
}
