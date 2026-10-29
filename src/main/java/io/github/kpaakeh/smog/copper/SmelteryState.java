package io.github.kpaakeh.smog.copper;

import java.util.*;
import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.*;

/** Stable, independent input jobs and a physical casting vessel survive every interruption. */
public final class SmelteryState {
    public final int[] progress=new int[Smeltery.MAX_WORKERS];
    public final String[] inputs=new String[Smeltery.MAX_WORKERS];
    public int page,containerMode,fuelAmount,fuelHeat,fuelSpeed=1;
    public int fuelViewAmount,fuelViewType,burnTotal;
    public long fuelViewTarget=Long.MIN_VALUE;
    public boolean fuelActive,consumeMold;
    public String fuel="",selected="",pourMetal="";
    public int filled,required,cooling;
    public ItemStack castingResult=ItemStack.EMPTY,castingMold=ItemStack.EMPTY;
    public long lastFlow=-20;
    public SmelteryState(){Arrays.fill(inputs,"");}
    public boolean casting(){return filled>0;}
    public void clearCast(){filled=required=cooling=0;consumeMold=false;pourMetal="";castingResult=castingMold=ItemStack.EMPTY;}
    public void save(ValueOutput o){
        o.putInt("smeltery_burn_total",burnTotal);o.putLong("last_cast_flow",lastFlow);o.putInt("smeltery_page",page);o.putInt("container_mode",containerMode);o.putInt("fuel_amount",fuelAmount);o.putString("fuel_fluid",fuel);o.putString("selected_metal",selected);
        o.store("melt_progress",Codec.INT.listOf(),Arrays.stream(progress).boxed().toList());o.store("melt_inputs",Codec.STRING.listOf(),List.of(inputs));
        o.putBoolean("casting_consumes_mold",consumeMold);o.putString("casting_metal",pourMetal);o.putInt("casting_filled",filled);o.putInt("casting_required",required);o.putInt("casting_cooling",cooling);
        if(!castingResult.isEmpty())o.store("casting_result",ItemStack.CODEC,castingResult);if(!castingMold.isEmpty())o.store("casting_mold",ItemStack.CODEC,castingMold);
    }
    public void load(ValueInput i){
        burnTotal=Math.max(0,i.getIntOr("smeltery_burn_total",0));lastFlow=i.getLongOr("last_cast_flow",-20);page=Math.clamp(i.getIntOr("smeltery_page",0),0,8);containerMode=Math.floorMod(i.getIntOr("container_mode",0),3);fuelAmount=Math.clamp(i.getIntOr("fuel_amount",0),0,SmelteryFuelStorage.CAPACITY);fuel=i.getStringOr("fuel_fluid","");selected=i.getStringOr("selected_metal","");
        Arrays.fill(progress,0);Arrays.fill(inputs,"");var jobs=i.read("melt_progress",Codec.INT.listOf()).orElse(List.of());for(int n=0;n<Math.min(jobs.size(),progress.length);n++)progress[n]=Math.clamp(jobs.get(n),0,20000);
        var keys=i.read("melt_inputs",Codec.STRING.listOf()).orElse(List.of());for(int n=0;n<Math.min(keys.size(),inputs.length);n++)inputs[n]=keys.get(n);
        consumeMold=i.getBooleanOr("casting_consumes_mold",false);pourMetal=i.getStringOr("casting_metal","");filled=Math.max(0,i.getIntOr("casting_filled",0));required=Math.max(filled,i.getIntOr("casting_required",0));cooling=Math.max(0,i.getIntOr("casting_cooling",0));castingResult=i.read("casting_result",ItemStack.CODEC).orElse(ItemStack.EMPTY);castingMold=i.read("casting_mold",ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }
}
