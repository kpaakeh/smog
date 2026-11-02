package io.github.kpaakeh.smog.mixin;

import io.github.kpaakeh.smog.copper.NaturalTreeLogs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class PlacedTreeLogMixin {
    @Inject(method="place",at=@At("RETURN"))
    private void smog$clearGeneratedMark(BlockPlaceContext context,CallbackInfoReturnable<InteractionResult> cir){if(!context.getLevel().isClientSide()&&cir.getReturnValue().consumesAction())NaturalTreeLogs.placed(context.getLevel(),context.getClickedPos());}
}
