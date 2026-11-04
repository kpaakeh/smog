package io.github.kpaakeh.smog.bronze;

import com.google.gson.*;
import java.util.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;

/** Exact cell-by-cell drawings; alternatives are explicit, not any solid block. */
public final class BronzeBlueprints {
    public record Cell(BlockPos offset,List<String> alternatives,boolean floor) {}
    public record Blueprint(String id,String controller,List<Cell> cells) {}
    public record Inspection(boolean complete,boolean loaded,BlockPos problem,String expected) {}
    private static final Map<String,Blueprint> ALL=new LinkedHashMap<>();
    private BronzeBlueprints() {}
    public static Collection<Blueprint> all(){return List.copyOf(ALL.values());}
    public static Blueprint get(String id){return ALL.get(id);}
    public static io.github.kpaakeh.smog.multiblock.MultiblockDefinition preview(Blueprint blueprint){
        var parts=new LinkedHashMap<BlockPos,io.github.kpaakeh.smog.multiblock.BlockIngredient>();
        for(var cell:blueprint.cells())parts.put(cell.offset(),new io.github.kpaakeh.smog.multiblock.BlockIngredient(cell.floor()?"minecraft:stone":cell.alternatives().getFirst()));
        return new io.github.kpaakeh.smog.multiblock.MultiblockDefinition(io.github.kpaakeh.smog.registry.ModBlocks.id("bronze_blueprint_"+blueprint.id()),parts);
    }
    public static List<Blueprint> forController(String id){return ALL.values().stream().filter(b->b.controller().equals(id)).toList();}
    public static Rotation rotation(Direction facing){return switch(facing){case EAST->Rotation.CLOCKWISE_90;case SOUTH->Rotation.CLOCKWISE_180;case WEST->Rotation.COUNTERCLOCKWISE_90;default->Rotation.NONE;};}
    /** The new furnace walls contain the nozzle itself, not a copper-smeltery intake behind it. */
    public static Direction nozzleFacing(Level level,BlockPos target){
        for(String id:List.of("bloomery_controller","forge_hearth"))for(var drawing:forController(id))for(var cell:drawing.cells()){
            if(!cell.alternatives().contains("smog:air_nozzle"))continue;
            for(var facing:List.of(Direction.NORTH,Direction.EAST,Direction.SOUTH,Direction.WEST)){
                var origin=target.subtract(cell.offset().rotate(rotation(facing)));if(!level.hasChunkAt(origin))continue;var state=level.getBlockState(origin);
                if(state.getBlock() instanceof BronzeBlock block&&block.id().equals(id)&&state.getValue(BronzeBlock.FACING)==facing)return facing.getOpposite();
            }
        }return null;
    }
    public static void load(JsonArray definitions){
        for(var value:definitions){var o=value.getAsJsonObject();var cells=new ArrayList<Cell>();var offsets=new HashSet<BlockPos>();
            for(var v:o.getAsJsonArray("cells")){var c=v.getAsJsonObject();var p=new BlockPos(c.get("x").getAsInt(),c.get("y").getAsInt(),c.get("z").getAsInt());
                if(p.equals(BlockPos.ZERO)||!offsets.add(p))throw new IllegalArgumentException("Invalid blueprint cell "+p);
                var choices=new ArrayList<String>();for(var option:c.getAsJsonArray("blocks")){String id=option.getAsString();if(!BuiltInRegistries.BLOCK.containsKey(Identifier.parse(id)))throw new IllegalArgumentException("Unknown blueprint block "+id);choices.add(id);}
                boolean floor=c.has("floor")&&c.get("floor").getAsBoolean();
                if(choices.isEmpty()&&!floor)throw new IllegalArgumentException("Empty blueprint alternative");cells.add(new Cell(p,List.copyOf(choices),floor));
            }
            var blueprint=new Blueprint(o.get("id").getAsString(),o.get("controller").getAsString(),List.copyOf(cells));
            if(ALL.putIfAbsent(blueprint.id(),blueprint)!=null)throw new IllegalArgumentException("Duplicate blueprint "+blueprint.id());
        }
    }
    public static Inspection inspect(Level level,BlockPos origin,Direction facing,Blueprint blueprint){
        var rotation=rotation(facing);
        if(!level.hasChunkAt(origin))return new Inspection(false,false,origin,"");
        if(!BuiltInRegistries.BLOCK.getKey(level.getBlockState(origin).getBlock()).toString().equals("smog:"+blueprint.controller()))return new Inspection(false,true,origin,"smog:"+blueprint.controller());
        // All required chunks must be present before reporting a missing physical part.
        for(var cell:blueprint.cells()){var p=origin.offset(cell.offset().rotate(rotation));if(!level.hasChunkAt(p))return new Inspection(false,false,p,"");}
        for(var cell:blueprint.cells()){var p=origin.offset(cell.offset().rotate(rotation));var state=level.getBlockState(p);var id=BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();boolean matches=cell.floor()?state.isFaceSturdy(level,p,Direction.UP):cell.alternatives().contains(id);if(!matches)return new Inspection(false,true,p,cell.floor()?"sturdy floor":String.join(" / ",cell.alternatives()));}
        return new Inspection(true,true,origin,"");
    }
}
