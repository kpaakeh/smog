package io.github.kpaakeh.smog.mixin;

import io.github.kpaakeh.smog.copper.ModularToolItem;
import io.github.kpaakeh.smog.copper.ToolTraits;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Player supplies the world clock which Item.getDestroySpeed cannot receive. */
@Mixin(Player.class)
public abstract class ModularMiningContextMixin {
    @Inject(method="getDestroySpeed",at=@At("HEAD"))
    private void smog$expireWorkHardening(BlockState state,CallbackInfoReturnable<Float> cir){
        var player=(Player)(Object)this;var stack=player.getMainHandItem();
        if(stack.getItem() instanceof ModularToolItem)ToolTraits.tick(stack,player.level());
    }
}
