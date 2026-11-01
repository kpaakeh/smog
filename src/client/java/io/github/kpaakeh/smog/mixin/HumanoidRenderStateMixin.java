package io.github.kpaakeh.smog.mixin;

import io.github.kpaakeh.smog.render.RopewayRiderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HumanoidRenderState.class)
public abstract class HumanoidRenderStateMixin implements RopewayRiderState {
    @Unique private boolean smog$ropewayRider;

    @Override public boolean smog$isRopewayRider(){return smog$ropewayRider;}
    @Override public void smog$setRopewayRider(boolean riding){smog$ropewayRider=riding;}
}
