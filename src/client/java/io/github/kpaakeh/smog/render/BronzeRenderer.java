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
    private static final String[] BEVEL_PARTS={"moving","moving_output","moving_left","shaft_front","shaft_right","shaft_left"};
    private static final org.joml.Quaternionf[][] BEVEL_ORIENTATIONS=new org.joml.Quaternionf[6][6];
    static {
        for(var facing:Direction.values())for(var output:Direction.values())if(facing.getAxis()!=output.getAxis()){
            var x=new org.joml.Vector3f(output.getStepX(),output.getStepY(),output.getStepZ());var z=new org.joml.Vector3f(-facing.getStepX(),-facing.getStepY(),-facing.getStepZ());var y=new org.joml.Vector3f(z).cross(x);
            BEVEL_ORIENTATIONS[facing.ordinal()][output.ordinal()]=new org.joml.Quaternionf().setFromNormalized(new org.joml.Matrix3f().setColumn(0,x).setColumn(1,y).setColumn(2,z));
        }
    }
    public static final class State extends BlockEntityRenderState {
        public final ItemStackRenderState[] parts=new ItemStackRenderState[6];
        public final boolean[] branches=new boolean[3];
        public State(){parts[0]=new ItemStackRenderState();}
        public Direction output=Direction.EAST;
        public boolean bevel;
        public boolean splitter;
        public String animation="none";
        public Direction facing=Direction.NORTH;
        public float angle;
    }
    private final ItemModelResolver items;
    public BronzeRenderer(BlockEntityRendererProvider.Context context){items=context.itemModelResolver();}
    // Large wheels extend beyond their owning cell.
    @Override public boolean shouldRenderOffScreen(){return true;}
    @Override public State createRenderState(){return new State();}
    @Override public void extractRenderState(BronzeEntity entity,State state,float partial,Vec3 camera,ModelFeatureRenderer.CrumblingOverlay overlay){
        BlockEntityRenderer.super.extractRenderState(entity,state,partial,camera,overlay);
        state.animation=BronzeContent.entry(entity.id()).animation();state.facing=entity.getBlockState().getValue(BronzeBlock.FACING);state.angle=entity.angle(partial);
        state.bevel=entity.getBlockState().getBlock() instanceof BronzeAngleBlock;
        state.splitter=entity.getBlockState().getBlock() instanceof BronzeSplitterBlock;
        if(state.splitter){state.branches[0]=entity.getBlockState().getValue(BronzeSplitterBlock.FRONT);state.branches[1]=entity.getBlockState().getValue(BronzeSplitterBlock.RIGHT);state.branches[2]=entity.getBlockState().getValue(BronzeSplitterBlock.LEFT);}
        if(state.bevel)state.output=BronzeAngleBlock.output(entity.getBlockState());
        if(entity.id().equals("bronze_shaft")||entity.id().equals("bronze_shaft_support")||entity.getBlockState().getBlock() instanceof BronzeGearBlock||entity.getBlockState().getBlock() instanceof BronzeClutchBlock)state.facing=BronzeShaft.canonical(state.facing);
        if(entity.getBlockState().getValue(BronzeBlock.ASSEMBLED))state.animation="none";
        if(entity.getBlockState().getBlock() instanceof BronzeClutchBlock&&!BronzeClutchBlock.engaged(entity.getBlockState()))state.animation="none";
        if(state.animation.equals("none"))return;
        var stack=new ItemStack(entity.getBlockState().getBlock());
        for(int i=0;i<(state.splitter?6:state.bevel?2:1);i++){if(state.parts[i]==null)state.parts[i]=new ItemStackRenderState();stack.set(DataComponents.CUSTOM_MODEL_DATA,new CustomModelData(List.of(),List.of(),List.of(BEVEL_PARTS[i]),List.of()));items.updateForTopItem(state.parts[i],stack,ItemDisplayContext.NONE,entity.getLevel(),null,0);}
    }
    @Override public void submit(State state,PoseStack pose,SubmitNodeCollector collector,CameraRenderState camera){
        if(state.animation.equals("none"))return;
        pose.pushPose();pose.translate(.5,.5,.5);
        if(state.bevel){
            pose.rotate(BEVEL_ORIENTATIONS[state.facing.ordinal()][state.output.ordinal()]);
            for(int i=0;i<(state.splitter?6:2);i++){
                if(i>=3&&!state.branches[i-3])continue;
                pose.pushPose();pose.rotate((i==0||i==3?Axis.ZP:Axis.XP).rotationDegrees((i==1||i==4?-1:1)*state.angle));state.parts[i].submit(pose,collector,state.lightCoords,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,0);pose.popPose();
            }pose.popPose();return;
        }
        switch(state.facing){case EAST->pose.rotate(Axis.YP.rotationDegrees(-90));case SOUTH->pose.rotate(Axis.YP.rotationDegrees(-180));case WEST->pose.rotate(Axis.YP.rotationDegrees(-270));case UP->pose.rotate(Axis.XP.rotationDegrees(90));case DOWN->pose.rotate(Axis.XP.rotationDegrees(270));default->{}}
        switch(state.animation){
            case "rotate_x"->pose.rotate(Axis.XP.rotationDegrees(state.angle));
            case "rotate_y"->pose.rotate(Axis.YP.rotationDegrees(state.angle));
            case "rotate_z"->pose.rotate(Axis.ZP.rotationDegrees(state.angle));
            case "reciprocate"->pose.translate(0,.09*Math.sin(Math.toRadians(state.angle)),0);
            case "rock_z"->{pose.translate(-.375,.1875,0);pose.rotate(Axis.ZP.rotationDegrees(18*(float)Math.sin(Math.toRadians(state.angle))));pose.translate(.375,-.1875,0);}
            default->{}
        }
        state.parts[0].submit(pose,collector,state.lightCoords,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,0);pose.popPose();
    }
}
