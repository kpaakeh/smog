package io.github.kpaakeh.smog.mixin;

import io.github.kpaakeh.smog.copper.ModularToolItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class ModularAnvilMixin {
    @Shadow @Final private DataSlot cost;
    @Inject(method="createResult",at=@At("HEAD"),cancellable=true)
    private void smog$keepParts(CallbackInfo ci){var menu=(AnvilMenu)(Object)this;if(menu.getSlot(0).getItem().getItem() instanceof ModularToolItem&&menu.getSlot(1).getItem().getItem() instanceof ModularToolItem){menu.getSlot(2).set(ItemStack.EMPTY);cost.set(0);ci.cancel();}}
}
