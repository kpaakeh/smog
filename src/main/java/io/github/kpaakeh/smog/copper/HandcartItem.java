package io.github.kpaakeh.smog.copper;

import net.minecraft.world.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.EntitySpawnReason;

public final class HandcartItem extends Item {
    public HandcartItem(Properties properties){super(properties.stacksTo(1));}
    @Override public InteractionResult useOn(UseOnContext context){
        var level=context.getLevel();var pos=context.getClickedPos().relative(context.getClickedFace());
        if(!level.getWorldBorder().isWithinBounds(pos))return InteractionResult.FAIL;
        var cart=CopperTransport.HANDCART.create(level,EntitySpawnReason.SPAWN_ITEM_USE);if(cart==null)return InteractionResult.FAIL;
        cart.setInitialPos(pos.getX()+.5,pos.getY()+.0625,pos.getZ()+.5);cart.setYRot(context.getRotation());
        if(!level.noCollision(cart,cart.getBoundingBox()))return InteractionResult.FAIL;
        if(!level.isClientSide()){level.addFreshEntity(cart);if(context.getPlayer()==null||!context.getPlayer().isCreative())context.getItemInHand().shrink(1);}
        return InteractionResult.SUCCESS;
    }
}
