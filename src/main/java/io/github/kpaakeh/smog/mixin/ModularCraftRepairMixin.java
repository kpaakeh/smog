package io.github.kpaakeh.smog.mixin;

import io.github.kpaakeh.smog.copper.ModularToolItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Vanilla combination creates a default item and loses modular parts; only our tools use their workbench. */
@Mixin(RepairItemRecipe.class)
public abstract class ModularCraftRepairMixin {
    @Inject(method="canCombine",at=@At("HEAD"),cancellable=true)
    private static void smog$keepParts(ItemStack first,ItemStack second,CallbackInfoReturnable<Boolean> cir){if(first.getItem() instanceof ModularToolItem||second.getItem() instanceof ModularToolItem)cir.setReturnValue(false);}
}
