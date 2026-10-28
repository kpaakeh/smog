package io.github.kpaakeh.smog.multiblock.complex;

import java.util.*;
import com.google.gson.*;
import io.github.kpaakeh.smog.multiblock.BlockIngredient;
import net.minecraft.resources.Identifier;

/** Register a reusable shell rule set and bind it to a controller; machines own no geometry logic. */
public final class ComplexMultiblocks {
    private static final Map<Identifier,ComplexMultiblockDefinition> CONTROLLERS=new LinkedHashMap<>();
    public static void register(ComplexMultiblockDefinition definition){if(CONTROLLERS.putIfAbsent(definition.controller(),definition)!=null)throw new IllegalArgumentException("Duplicate complex controller");}
    public static ComplexMultiblockDefinition get(Identifier controller){return CONTROLLERS.get(controller);}
    public static Collection<ComplexMultiblockDefinition> all(){return List.copyOf(CONTROLLERS.values());}
    public static io.github.kpaakeh.smog.machine.MachineEntity owner(net.minecraft.server.level.ServerLevel level,net.minecraft.core.BlockPos part){
        var pos=ComplexFormationData.get(level).owner(part);
        return pos!=null&&level.hasChunkAt(pos)&&level.getBlockEntity(pos) instanceof io.github.kpaakeh.smog.machine.MachineEntity m&&m.assembled()?m:null;
    }
    /** Search loaded block entities only; using the configurator must not load world chunks. */
    public static List<io.github.kpaakeh.smog.machine.MachineEntity> nearbyControllers(net.minecraft.server.level.ServerLevel level,net.minecraft.core.BlockPos part){
        List<io.github.kpaakeh.smog.machine.MachineEntity> result=new ArrayList<>();
        for(int x=(part.getX()-10)>>4;x<=((part.getX()+10)>>4);x++)for(int z=(part.getZ()-10)>>4;z<=((part.getZ()+10)>>4);z++){
            var chunk=level.getChunkSource().getChunkNow(x,z);if(chunk==null)continue;
            for(var entity:chunk.getBlockEntities().values())if(entity instanceof io.github.kpaakeh.smog.machine.MachineEntity m&&(m.complexDefinition()!=null||io.github.kpaakeh.smog.copper.ManualWorkshops.handles(m))&&!m.assembled()&&Math.abs(m.getBlockPos().getX()-part.getX())<=10&&Math.abs(m.getBlockPos().getY()-part.getY())<=10&&Math.abs(m.getBlockPos().getZ()-part.getZ())<=10)result.add(m);
        }
        result.sort(Comparator.comparingDouble(m->m.getBlockPos().distSqr(part)));return result;
    }
    public static void initializeInteractions(){
        net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage.SIDED.registerForBlocks((level,pos,state,entity,side)->{
            if(!(level instanceof net.minecraft.server.level.ServerLevel server))return null;var m=owner(server,pos);return m!=null&&io.github.kpaakeh.smog.copper.CopperWorkshops.workshop(m,"copper_hearth")?m.molten:null;
        },net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(io.github.kpaakeh.smog.registry.ModBlocks.id("casting_spout")));
        net.fabricmc.fabric.api.event.player.UseBlockCallback.EVENT.register((player,level,hand,hit)->{
            if(!(level instanceof net.minecraft.server.level.ServerLevel server)||player.isSpectator()||hand!=net.minecraft.world.InteractionHand.MAIN_HAND)return net.minecraft.world.InteractionResult.PASS;
            var machine=owner(server,hit.getBlockPos());if(machine==null||machine.getBlockPos().equals(hit.getBlockPos()))return net.minecraft.world.InteractionResult.PASS;
            if(player.getItemInHand(hand).getItem() instanceof io.github.kpaakeh.smog.registry.ConfiguratorItem tool)return tool.useOn(new net.minecraft.world.item.context.UseOnContext(player,hand,hit));
            if(machine.spec().kind()==io.github.kpaakeh.smog.machine.MachineSpec.Kind.COPPER_WORKSHOP)return io.github.kpaakeh.smog.copper.CopperInteractions.use(server,machine,player,hand,hit.getBlockPos());
            if(player.isShiftKeyDown())return net.minecraft.world.InteractionResult.PASS;
            player.openMenu(machine);return net.minecraft.world.InteractionResult.SUCCESS;
        });
    }
    public static void load(JsonArray definitions){
        for(var value:definitions){var o=value.getAsJsonObject();List<ComplexMultiblockDefinition.Role> roles=new ArrayList<>();
            for(var entry:o.getAsJsonArray("roles")){var r=entry.getAsJsonObject();Set<ComplexMultiblockDefinition.Zone> zones=new HashSet<>();for(var zone:r.getAsJsonArray("zones"))zones.add(ComplexMultiblockDefinition.Zone.valueOf(zone.getAsString().toUpperCase(Locale.ROOT)));
                roles.add(new ComplexMultiblockDefinition.Role(r.get("name").getAsString(),new BlockIngredient(r.get("block").getAsString()),zones,r.get("min").getAsInt(),r.get("max").getAsInt()));}
            List<ComplexMultiblockDefinition.RoleGroup> groups=new ArrayList<>();
            if(o.has("required_any"))for(var group:o.getAsJsonArray("required_any")){var g=group.getAsJsonObject();Set<String> names=new HashSet<>();for(var n:g.getAsJsonArray("roles"))names.add(n.getAsString());groups.add(new ComplexMultiblockDefinition.RoleGroup(names,g.get("min").getAsInt()));}
            register(new ComplexMultiblockDefinition(Identifier.parse(o.get("id").getAsString()),Identifier.parse(o.get("controller").getAsString()),o.get("closed").getAsBoolean(),o.get("min_width").getAsInt(),o.get("max_width").getAsInt(),o.get("min_depth").getAsInt(),o.get("max_depth").getAsInt(),o.get("min_height").getAsInt(),o.get("max_height").getAsInt(),new BlockIngredient(o.get("floor").getAsString()),new BlockIngredient(o.get("casing").getAsString()),roles,groups));
        }
    }
}
