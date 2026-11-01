package io.github.kpaakeh.smog.mixin;

import io.github.kpaakeh.smog.copper.RopewayRideEntity;
import io.github.kpaakeh.smog.render.RopewayRiderState;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidRiderExtractionMixin {
    // AvatarRenderer uses this helper too. TAIL follows the native passenger assignment.
    @Inject(method="extractHumanoidRenderState",at=@At("TAIL"))
    private static void smog$extractRopewayRider(LivingEntity entity,HumanoidRenderState state,float partialTicks,ItemModelResolver items,CallbackInfo ci){
        boolean riding=entity.getVehicle() instanceof RopewayRideEntity;
        ((RopewayRiderState)state).smog$setRopewayRider(riding);
        if(riding){
            state.isPassenger=false;
            state.isCrouching=false;
            state.isFallFlying=false;
            state.isVisuallySwimming=false;
            state.swimAmount=0;
        }
    }
}
