package io.github.kpaakeh.smog.energy;

import io.github.kpaakeh.smog.machine.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;

public final class WireCoilItem extends Item {
    private final Voltage voltage;
    public WireCoilItem(Properties properties,Voltage voltage){super(properties);this.voltage=voltage;}
    @Override public InteractionResult useOn(UseOnContext context){
        if(!(context.getLevel() instanceof ServerLevel level))return InteractionResult.SUCCESS;
        var player=context.getPlayer();if(player==null)return InteractionResult.PASS;
        var pos=context.getClickedPos();var stack=context.getItemInHand();
        if(!EnergyNetwork.connector(level,pos,voltage)){player.sendSystemMessage(Component.translatable("message.smog.connector_required",voltage.name()));return InteractionResult.FAIL;}
        var data=stack.getOrDefault(DataComponents.CUSTOM_DATA,CustomData.EMPTY).copyTag();
        if(player.isShiftKeyDown()){
            WireData.get(level).disconnect(pos).forEach(link->EnergyNetwork.refund(level,pos,link.voltage()));
            data.remove("smog_wire_dimension");stack.set(DataComponents.CUSTOM_DATA,CustomData.of(data));return InteractionResult.SUCCESS;
        }
        String dimension=level.dimension().identifier().toString();
        if(!data.getStringOr("smog_wire_dimension","").equals(dimension)){
            data.putString("smog_wire_dimension",dimension);data.putInt("smog_wire_x",pos.getX());data.putInt("smog_wire_y",pos.getY());data.putInt("smog_wire_z",pos.getZ());
            stack.set(DataComponents.CUSTOM_DATA,CustomData.of(data));player.sendSystemMessage(Component.translatable("message.smog.wire_start"));return InteractionResult.SUCCESS;
        }
        BlockPos start=new BlockPos(data.getIntOr("smog_wire_x",0),data.getIntOr("smog_wire_y",0),data.getIntOr("smog_wire_z",0));
        if(!EnergyNetwork.connector(level,start,voltage)||start.equals(pos)||start.distSqr(pos)>voltage.reach*voltage.reach){player.sendSystemMessage(Component.translatable("message.smog.wire_invalid",voltage.reach));return InteractionResult.FAIL;}
        WireLink link=new WireLink(start,pos,voltage);
        for(int i=1;i<32;i++){
            BlockPos test=BlockPos.containing(link.point(i/32.0));
            if(!level.hasChunkAt(test)||!test.equals(start)&&!test.equals(pos)&&level.getBlockState(test).isCollisionShapeFullBlock(level,test)){
                player.sendSystemMessage(Component.translatable("message.smog.wire_obstructed"));return InteractionResult.FAIL;
            }
        }
        if(!WireData.get(level).add(link))return InteractionResult.FAIL;
        data.remove("smog_wire_dimension");stack.set(DataComponents.CUSTOM_DATA,CustomData.of(data));
        if(!player.getAbilities().instabuild)stack.shrink(1);return InteractionResult.SUCCESS;
    }
}
