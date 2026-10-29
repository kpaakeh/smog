package io.github.kpaakeh.smog.copper;

import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.network.chat.Component;
import io.github.kpaakeh.smog.machine.MachineEntity;

public final class RopeLinkItem extends Item {
    public RopeLinkItem(Properties p){super(p);}
    @Override public InteractionResult useOn(UseOnContext context){
        if(!(context.getLevel() instanceof ServerLevel level))return InteractionResult.SUCCESS;
        if(!(level.getBlockEntity(context.getClickedPos()) instanceof MachineEntity anchor)||!Ropeway.handles(anchor))return InteractionResult.PASS;
        var stack=context.getItemInHand();var data=stack.getOrDefault(DataComponents.CUSTOM_DATA,CustomData.EMPTY).copyTag();var player=context.getPlayer();
        if(player!=null&&player.isShiftKeyDown()){Ropeway.unlink(level,anchor);stack.remove(DataComponents.CUSTOM_DATA);player.sendOverlayMessage(Component.translatable("message.smog.rope_unlinked"));return InteractionResult.SUCCESS;}
        String dimension=level.dimension().identifier().toString();
        if(data.contains("rope_start")&&data.getStringOr("rope_dimension","").equals(dimension)){
            var start=BlockPos.of(data.getLongOr("rope_start",0));boolean linked=level.hasChunkAt(start)&&level.getBlockEntity(start) instanceof MachineEntity first&&Ropeway.link(level,first,anchor);
            stack.remove(DataComponents.CUSTOM_DATA);if(linked&&player!=null&&!player.isCreative())stack.shrink(1);if(player!=null)player.sendOverlayMessage(Component.translatable(linked?"message.smog.rope_linked":"message.smog.rope_invalid"));
        }else{var saved=new CompoundTag();saved.putLong("rope_start",anchor.getBlockPos().asLong());saved.putString("rope_dimension",dimension);stack.set(DataComponents.CUSTOM_DATA,CustomData.of(saved));if(player!=null)player.sendOverlayMessage(Component.translatable("message.smog.rope_first"));}
        return InteractionResult.SUCCESS;
    }
}
