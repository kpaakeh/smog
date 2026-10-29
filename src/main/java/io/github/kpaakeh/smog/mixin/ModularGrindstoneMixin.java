package io.github.kpaakeh.smog.mixin;

import io.github.kpaakeh.smog.copper.ModularToolItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.GrindstoneMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneMenu.class)
public abstract class ModularGrindstoneMixin {
    @Inject(method="mergeItems",at=@At("HEAD"),cancellable=true)
    private void smog$keepParts(ItemStack first,ItemStack second,CallbackInfoReturnable<ItemStack> cir){if(!first.isEmpty()&&!second.isEmpty()&&(first.getItem() instanceof ModularToolItem||second.getItem() instanceof ModularToolItem))cir.setReturnValue(ItemStack.EMPTY);}
}
