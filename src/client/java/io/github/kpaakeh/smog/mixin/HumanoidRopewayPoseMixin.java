package io.github.kpaakeh.smog.mixin;

import io.github.kpaakeh.smog.render.RopewayRiderState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidRopewayPoseMixin {
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart rightLeg;
    @Shadow @Final public ModelPart leftLeg;

    // Apply after item-use, attack and idle animation; armor models use this same method.
    @Inject(method="setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",at=@At("TAIL"))
    private void smog$hangFromGrip(HumanoidRenderState state,CallbackInfo ci){
        if(!((RopewayRiderState)state).smog$isRopewayRider())return;
        rightArm.xRot=leftArm.xRot=-3.0F;
        rightArm.yRot=leftArm.yRot=0;
        rightArm.zRot=.12F;
        leftArm.zRot=-.12F;
        rightLeg.xRot=leftLeg.xRot=.03F;
        rightLeg.yRot=leftLeg.yRot=0;
        rightLeg.zRot=.02F;
        leftLeg.zRot=-.02F;
    }
}
