package io.github.kpaakeh.smog.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.kpaakeh.smog.copper.RopewayRideEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomModelData;
import java.util.List;

/** A visible, replaceable roller and hand grip shared by the passenger and freight hoists. */
public final class RopewayRideRenderer extends EntityRenderer<RopewayRideEntity,RopewayRideRenderer.State> {
    public static final class State extends EntityRenderState {
        public final ItemStackRenderState carriage=new ItemStackRenderState();
        public float angle;public int light;
    }
    public static ItemStack carriage(){
        var stack=new ItemStack(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(ModBlocks.id("rope_anchor")));
        stack.set(DataComponents.CUSTOM_MODEL_DATA,new CustomModelData(List.of(),List.of(),List.of("carriage"),List.of()));
        return stack;
    }
    private final ItemModelResolver items;
    public RopewayRideRenderer(EntityRendererProvider.Context context){super(context);items=context.getItemModelResolver();}
    @Override public State createRenderState(){return new State();}
    @Override public void extractRenderState(RopewayRideEntity ride,State state,float tick){
        super.extractRenderState(ride,state,tick);items.updateForNonLiving(state.carriage,carriage(),ItemDisplayContext.NONE,ride);
        var direction=ride.end().subtract(ride.start());state.angle=(float)Math.toDegrees(Math.atan2(direction.getX(),direction.getZ()));state.light=getPackedLightCoords(ride,tick);
    }
    @Override public void submit(State state,PoseStack pose,SubmitNodeCollector collector,CameraRenderState camera){
        pose.pushPose();pose.translate(0,1.5,0);pose.rotate(Axis.YP.rotationDegrees(state.angle));
        state.carriage.submit(pose,collector,state.light,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,0);pose.popPose();
        super.submit(state,pose,collector,camera);
    }
}
