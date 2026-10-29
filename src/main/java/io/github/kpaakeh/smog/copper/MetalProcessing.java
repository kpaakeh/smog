package io.github.kpaakeh.smog.copper;

import com.google.gson.*;
import java.util.*;
import io.github.kpaakeh.smog.machine.MachineEntity;

/** Working heat is an explicit game parameter, not a claimed Celsius value. */
public final class MetalProcessing {
    public record Conditions(int heat,boolean lining,boolean closed,boolean atmosphere,int openMax){}
    private static final Map<String,Conditions> VALUES=new LinkedHashMap<>();
    public static void initialize(JsonObject root){
        for(var value:root.getAsJsonArray("casting_metals")){var o=value.getAsJsonObject();VALUES.put(o.get("id").getAsString(),new Conditions(o.get("working_heat").getAsInt(),o.get("lining").getAsBoolean(),o.get("closed").getAsBoolean(),o.get("controlled_atmosphere").getAsBoolean(),o.get("open_max_heat").getAsInt()));}
        if(VALUES.size()>128)throw new IllegalArgumentException("At most 128 molten material kinds fit the synchronized tank layout");
        MoltenMetalStorage.NAMES=VALUES.keySet().toArray(String[]::new);
    }
    public static Conditions conditions(int kind){return VALUES.get(MoltenMetalStorage.NAMES[kind]);}
    public static boolean compatible(MachineEntity m,int kind){var c=conditions(kind);return c.heat()<=Smeltery.maximumHeat(m)&&(!c.lining()||m.complexRoles().getOrDefault("lining",0)>0)&&(!c.closed()||CopperWorkshops.closed(m))&&(!c.atmosphere()||m.complexRoles().getOrDefault("controlled_atmosphere",0)>0);}
    public static int problem(MachineEntity m,int kind){var c=conditions(kind);if(c.heat()>Smeltery.maximumHeat(m))return 12;if(c.lining()&&m.complexRoles().getOrDefault("lining",0)==0)return 13;if(c.closed()&&!CopperWorkshops.closed(m))return 14;if(c.atmosphere()&&m.complexRoles().getOrDefault("controlled_atmosphere",0)==0)return 15;if(!CopperWorkshops.closed(m)&&m.copper.heat>c.openMax())return 16;return 0;}
    public static boolean safe(MachineEntity m,int kind){return problem(m,kind)==0;}
    public static java.util.List<net.minecraft.network.chat.Component> requirements(int kind){var c=conditions(kind);var result=new java.util.ArrayList<net.minecraft.network.chat.Component>();result.add(net.minecraft.network.chat.Component.translatable("gui.smog.process.heat",c.heat()));if(c.lining())result.add(net.minecraft.network.chat.Component.translatable("gui.smog.copper.status_13"));if(c.closed())result.add(net.minecraft.network.chat.Component.translatable("gui.smog.copper.status_14"));if(c.atmosphere())result.add(net.minecraft.network.chat.Component.translatable("gui.smog.copper.status_15"));if(c.openMax()<1350)result.add(net.minecraft.network.chat.Component.translatable("gui.smog.process.open_max",c.openMax()));return java.util.List.copyOf(result);}

}
