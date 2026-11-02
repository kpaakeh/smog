package io.github.kpaakeh.smog.mixin;

import io.github.kpaakeh.smog.copper.NaturalTreeLogs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TreeFeature.class)
public abstract class NaturalTreeFeatureMixin {
    @Inject(method="setBlockKnownShape",at=@At("TAIL"))
    private static void smog$recordGeneratedLog(LevelWriter level,BlockPos pos,BlockState state,CallbackInfo ci){if(level instanceof LevelAccessor accessor&&accessor.getBlockState(pos).is(state.getBlock()))NaturalTreeLogs.generated(accessor,pos,state);}
}
