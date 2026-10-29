package io.github.kpaakeh.smog.render;

import io.github.kpaakeh.smog.copper.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.item.ItemDisplayContext;

/** Editable JSON body, shared with the placed item; no hardcoded model geometry. */
public final class HandcartRenderer extends EntityRenderer<HandcartEntity,HandcartRenderer.State> {
    public static final class State extends EntityRenderState {public final ItemStackRenderState body=new ItemStackRenderState();public float angle;public int light;}
    private final ItemModelResolver items;
    public HandcartRenderer(EntityRendererProvider.Context context){super(context);items=context.getItemModelResolver();shadowRadius=.7f;}
    @Override public State createRenderState(){return new State();}
    @Override public void extractRenderState(HandcartEntity cart,State state,float tick){super.extractRenderState(cart,state,tick);items.updateForNonLiving(state.body,cart.getPickResult(),ItemDisplayContext.NONE,cart);state.angle=cart.getYRot();state.light=getPackedLightCoords(cart,tick);}
    @Override public void submit(State state,PoseStack pose,SubmitNodeCollector collector,CameraRenderState camera){pose.pushPose();pose.translate(0,.5,0);pose.rotate(Axis.YP.rotationDegrees(180-state.angle));state.body.submit(pose,collector,state.light,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,0);pose.popPose();super.submit(state,pose,collector,camera);}
}
