package io.github.kpaakeh.smog.copper;

import java.util.function.Consumer;
import io.github.kpaakeh.smog.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

/** Records only the block directly inspected by the player. No generation data, scans or hidden ore lookup. */
public final class FieldJournalItem extends Item {
    public static final int MAX_RECORDS=32;
    public FieldJournalItem(Properties properties){super(properties.stacksTo(1));}
    public static boolean sample(net.minecraft.world.level.block.state.BlockState state){
        if(state.is(Blocks.COPPER_ORE)||state.is(Blocks.DEEPSLATE_COPPER_ORE))return true;
        return ModOres.all().stream().anyMatch(o->state.is(o.stone())||state.is(o.deepslate()))||ModDeposits.all().stream().anyMatch(d->state.is(d.block()));
    }
    private static CompoundTag data(ItemStack stack){return stack.getOrDefault(DataComponents.CUSTOM_DATA,CustomData.EMPTY).copyTag();}
    public static boolean record(ItemStack stack,Level level,BlockPos pos){
        if(!sample(level.getBlockState(pos)))return false;
        var data=data(stack);var records=data.getListOrEmpty("survey_records");String dimension=level.dimension().identifier().toString();
        for(var entry:records)if(entry instanceof CompoundTag record&&record.getStringOr("dimension","").equals(dimension)&&record.getIntOr("x",0)==pos.getX()&&record.getIntOr("y",0)==pos.getY()&&record.getIntOr("z",0)==pos.getZ())return false;
        if(records.size()>=MAX_RECORDS)return false;
        var record=new CompoundTag();record.putString("dimension",dimension);record.putString("block",BuiltInRegistries.BLOCK.getKey(level.getBlockState(pos).getBlock()).toString());record.putInt("x",pos.getX());record.putInt("y",pos.getY());record.putInt("z",pos.getZ());records.add(record);data.put("survey_records",records);data.putInt("survey_selected",records.size()-1);stack.set(DataComponents.CUSTOM_DATA,CustomData.of(data));return true;
    }
    public static Component selected(ItemStack stack){
        var data=data(stack);var records=data.getListOrEmpty("survey_records");if(records.isEmpty())return Component.translatable("message.smog.journal_empty");
        int index=Math.floorMod(data.getIntOr("survey_selected",0),records.size());var entry=records.getCompoundOrEmpty(index);Identifier id=Identifier.tryParse(entry.getStringOr("block","minecraft:air"));
        var name=id==null?Component.literal("?"):BuiltInRegistries.BLOCK.getValue(id).getName();
        return Component.translatable("message.smog.journal_entry",index+1,records.size(),name,entry.getIntOr("x",0),entry.getIntOr("y",0),entry.getIntOr("z",0),entry.getStringOr("dimension",""));
    }
    @Override public InteractionResult useOn(UseOnContext context){
        var player=context.getPlayer();if(player==null)return InteractionResult.PASS;
        if(!context.getLevel().isClientSide()){
            boolean recorded=record(context.getItemInHand(),context.getLevel(),context.getClickedPos());
            player.sendOverlayMessage(recorded?selected(context.getItemInHand()):Component.translatable("message.smog.journal_rejected"));
        }
        return InteractionResult.SUCCESS;
    }
    @Override public InteractionResult use(Level level,Player player,InteractionHand hand){
        if(!level.isClientSide()){
            var stack=player.getItemInHand(hand);var data=data(stack);var records=data.getListOrEmpty("survey_records");
            if(!records.isEmpty()){data.putInt("survey_selected",Math.floorMod(data.getIntOr("survey_selected",0)+(player.isShiftKeyDown()?-1:1),records.size()));stack.set(DataComponents.CUSTOM_DATA,CustomData.of(data));}
            player.sendOverlayMessage(selected(stack));
        }
        return InteractionResult.SUCCESS;
    }
    @Override public void appendHoverText(ItemStack stack,TooltipContext context,TooltipDisplay display,Consumer<Component> lines,TooltipFlag flag){
        super.appendHoverText(stack,context,display,lines,flag);lines.accept(Component.translatable("item.smog.field_journal.help"));lines.accept(selected(stack));
    }
}
