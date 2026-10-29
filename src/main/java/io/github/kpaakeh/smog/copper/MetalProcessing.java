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
        MoltenMetalStorage.NAMES=VALUES.keySet().toArray(String[]::new);
    }
    public static Conditions conditions(int kind){return VALUES.get(MoltenMetalStorage.NAMES[kind]);}
    public static boolean compatible(MachineEntity m,int kind){var c=conditions(kind);return c.heat()<=1100&&(!c.lining()||m.complexRoles().getOrDefault("lining",0)>0)&&(!c.closed()||CopperWorkshops.closed(m))&&(!c.atmosphere()||m.complexRoles().getOrDefault("controlled_atmosphere",0)>0);}
    public static boolean safe(MachineEntity m,int kind){return compatible(m,kind)&&(CopperWorkshops.closed(m)||m.copper.heat<=conditions(kind).openMax());}
}
