package io.github.kpaakeh.smog.copper;

import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecartContainer;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;

/** Native persistent container; the player supplies motion, never fuel or chunk tickets. */
public final class HandcartEntity extends AbstractMinecartContainer {
    private UUID puller;
    public HandcartEntity(EntityType<?> type,Level level){super(type,level);clearItemStacks();}
    @Override public int getContainerSize(){return 54;}
    @Override protected AbstractContainerMenu createMenu(int id,Inventory inventory){return ChestMenu.sixRows(id,inventory,this);}
    @Override protected Item getDropItem(){return BuiltInRegistries.ITEM.getValue(ModBlocks.id("handcart"));}
    @Override public ItemStack getPickResult(){return new ItemStack(getDropItem());}
    @Override public boolean isRideable(){return false;}
    @Override protected double getMaxSpeed(ServerLevel level){return .24;}
    public boolean pulling(){return puller!=null;}
    public void pull(Player player){puller=player.getUUID();}
    @Override public InteractionResult interact(Player player,InteractionHand hand,Vec3 location){
        if(player.isShiftKeyDown()){
            if(!level().isClientSide()){
                if(puller!=null&&!puller.equals(player.getUUID())){player.sendOverlayMessage(Component.translatable("message.smog.cart_busy"));return InteractionResult.SUCCESS;}
                puller=puller==null?player.getUUID():null;
                player.sendOverlayMessage(Component.translatable(puller==null?"message.smog.cart_released":"message.smog.cart_pulling"));
            }return InteractionResult.SUCCESS;
        }
        if(!level().isClientSide())player.openMenu(this);return InteractionResult.SUCCESS;
    }
    @Override protected void comeOffTrack(ServerLevel level){
        if(!pulling()){super.comeOffTrack(level);return;}
        var velocity=getDeltaMovement();double speed=velocity.horizontalDistance(),maximum=getMaxSpeed(level);if(speed>maximum)velocity=new Vec3(velocity.x*maximum/speed,velocity.y,velocity.z*maximum/speed);setDeltaMovement(velocity);move(MoverType.SELF,velocity);if(!onGround())setDeltaMovement(getDeltaMovement().scale(getAirDrag()));
    }
    @Override public void tick(){
        if(level() instanceof ServerLevel server&&puller!=null){
            var player=server.getPlayerByUUID(puller);
            if(player==null||!player.isAlive()||player.isSpectator()||player.distanceToSqr(this)>36)puller=null;
            else {
                var delta=player.position().subtract(position());double distance=delta.horizontalDistance();
                var velocity=distance>1.65?delta.multiply(1,0,1).normalize().scale(Math.min(.24,(distance-1.65)*.15)):Vec3.ZERO;
                setDeltaMovement(velocity.x,getDeltaMovement().y,velocity.z);
            }
        }
        super.tick();
    }
}
