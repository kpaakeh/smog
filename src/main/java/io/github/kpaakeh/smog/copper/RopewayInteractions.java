package io.github.kpaakeh.smog.copper;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;

/** Rope picking is server-owned and uses normal reach and block occlusion. */
public final class RopewayInteractions {
    public static void initialize(){
        UseItemCallback.EVENT.register(RopewayInteractions::use);
        UseBlockCallback.EVENT.register((player,level,hand,hit)->use(player,level,hand));
    }
    private static InteractionResult use(net.minecraft.world.entity.player.Player player,net.minecraft.world.level.Level level,net.minecraft.world.InteractionHand hand){
        if(!(level instanceof ServerLevel server)||!player.getItemInHand(hand).is(Items.STICK)||player.isSpectator()||player.isPassenger())return InteractionResult.PASS;
        var result=RopewayRideEntity.mountLookedAt(server,player);if(result==null)return InteractionResult.PASS;
        player.sendOverlayMessage(result);return InteractionResult.SUCCESS;
    }
}
