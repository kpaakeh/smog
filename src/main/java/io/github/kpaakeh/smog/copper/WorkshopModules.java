package io.github.kpaakeh.smog.copper;

import java.util.*;
import com.google.gson.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

/** Functional module slots for non-shell structures: the envelope stays stable while selected parts change work. */
public final class WorkshopModules {
    private record Slot(String role,List<BlockPos> positions,Map<Identifier,String> modes){}
    private static final Map<Identifier,List<Slot>> PROFILES=new LinkedHashMap<>();
    public static void initialize(JsonArray definitions){
        for(var value:definitions){var o=value.getAsJsonObject();List<Slot> slots=new ArrayList<>();for(var role:o.getAsJsonArray("roles")){var r=role.getAsJsonObject();List<BlockPos> positions=new ArrayList<>();for(var p:r.getAsJsonArray("positions")){var a=p.getAsJsonArray();positions.add(new BlockPos(a.get(0).getAsInt(),a.get(1).getAsInt(),a.get(2).getAsInt()));}Map<Identifier,String> modes=new LinkedHashMap<>();r.getAsJsonObject("modes").entrySet().forEach(e->modes.put(Identifier.parse(e.getKey()),e.getValue().getAsString()));slots.add(new Slot(r.get("name").getAsString(),List.copyOf(positions),Map.copyOf(modes)));}PROFILES.put(Identifier.parse(o.get("controller").getAsString()),List.copyOf(slots));}
    }
    public static String mode(MachineEntity machine,String role,String fallback){
        if(machine.getLevel()==null)return fallback;
        for(var slot:PROFILES.getOrDefault(machine.machineId(),List.of()))if(slot.role.equals(role))for(var offset:slot.positions){var pos=machine.getBlockPos().offset(offset.rotate(machine.rotation()));if(!machine.getLevel().hasChunkAt(pos))continue;String mode=slot.modes.get(BuiltInRegistries.BLOCK.getKey(machine.getLevel().getBlockState(pos).getBlock()));if(mode!=null&&!mode.equals(fallback))return mode;}
        return fallback;
    }
}
