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
    private static final Map<Integer,Blueprint> HOISTS=new HashMap<>();
    public static Blueprint get(String id){
        var known=ALL.get(id);if(known!=null)return known;
        if(id.startsWith("bronze_mine_hoist_"))try{int height=Integer.parseInt(id.substring("bronze_mine_hoist_".length()));return height>=1&&height<=4096?hoist(height):null;}catch(NumberFormatException ignored){}
        return null;
    }
    /** Height is discovered from the lower landing; no pre-enumerated height limit. */
    public static List<Blueprint> hoistCandidates(Level level,BlockPos controller,Direction facing){
        var found=new ArrayList<Blueprint>();var rotation=rotation(facing);
        for(int h=1;controller.getY()-h-1>=level.getMinY();h++){
            var landing=controller.offset(new BlockPos(-1,-h-1,-1).rotate(rotation));
            if(!level.hasChunkAt(landing)){if(found.isEmpty())found.add(hoist(h));break;}
            if(level.getBlockState(landing).is(BronzeContent.entry("bronze_hoist_landing").block()))found.add(hoist(h));
        }return found.isEmpty()?List.of(hoist(1)):List.copyOf(found);
    }
    public static Blueprint hoist(int h){
        if(h<1||h>4096)throw new IllegalArgumentException("Height outside Minecraft dimension bounds");
        return HOISTS.computeIfAbsent(h,height->{
            var cells=new LinkedHashMap<BlockPos,List<String>>();String air="minecraft:air",frame="smog:bronze_machine_frame";
            for(int y=0;y<=height+1;y++)for(int x=1;x<=2;x++)for(int z=1;z<=2;z++)cells.put(new BlockPos(x,y,z),List.of(y==0?"smog:bronze_hoist_platform":air));
            for(int y=0;y<=height;y++){
                for(int x:new int[]{0,3})cells.put(new BlockPos(x,y,1),List.of("smog:bronze_hoist_guide"));
                cells.put(new BlockPos(3,y,2),List.of(y==height?"smog:bronze_hoist_counterweight":air));
            }
            for(int x:new int[]{0,3})for(int z:new int[]{0,3})for(int y=0;y<=height+1;y++)cells.put(new BlockPos(x,y,z),y==0||y==height+1?List.of(frame):List.of(air,frame));
            for(int x=0;x<4;x++)cells.put(new BlockPos(x,height+1,0),List.of(frame));
            cells.put(new BlockPos(1,height+1,0),List.of("smog:bronze_hoist_drum"));cells.put(new BlockPos(2,height+1,0),List.of("smog:bronze_bearing_block"));
            cells.put(new BlockPos(0,0,2),List.of("smog:bronze_hoist_landing"));cells.put(new BlockPos(0,height,2),List.of("smog:bronze_hoist_landing"));
            for(int x=1;x<=2;x++)for(int z=1;z<=2;z++)cells.put(new BlockPos(x,height+2,z),List.of(air));
            var origin=new BlockPos(1,height+1,3);var parts=new ArrayList<Cell>();cells.forEach((p,choices)->{if(!p.equals(origin))parts.add(new Cell(p.subtract(origin),choices,false));});
            return new Blueprint("bronze_mine_hoist_"+height,"bronze_mine_hoist",List.copyOf(parts));
        });
    }
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
        for(var cell:blueprint.cells()){var p=origin.offset(cell.offset().rotate(rotation));if(p.getY()<level.getMinY()||p.getY()>=level.getMinY()+level.getHeight())return new Inspection(false,true,p,"dimension bounds");if(!level.hasChunkAt(p))return new Inspection(false,false,p,"");}
        for(var cell:blueprint.cells()){var p=origin.offset(cell.offset().rotate(rotation));var state=level.getBlockState(p);var id=BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();boolean matches=cell.floor()?state.isFaceSturdy(level,p,Direction.UP):cell.alternatives().contains(id);if(!matches)return new Inspection(false,true,p,cell.floor()?"sturdy floor":String.join(" / ",cell.alternatives()));}
        return new Inspection(true,true,origin,"");
    }
}
