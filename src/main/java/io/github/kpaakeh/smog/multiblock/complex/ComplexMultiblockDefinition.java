package io.github.kpaakeh.smog.multiblock.complex;

import java.util.*;
import io.github.kpaakeh.smog.multiblock.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;

/** A family of variable-size structures. The controller anchors the front wall, one block above the floor. */
public record ComplexMultiblockDefinition(Identifier id,Identifier controller,boolean closed,
        int minWidth,int maxWidth,int minDepth,int maxDepth,int minHeight,int maxHeight,
        BlockIngredient floor,BlockIngredient casing,List<Role> roles,List<RoleGroup> groups) {
    public enum Zone { FLOOR,WALL,ROOF,INTERIOR }
    public record Role(String name,BlockIngredient ingredient,Set<Zone> zones,int minimum,int maximum) {
        public Role {zones=Set.copyOf(zones);if(minimum<0||maximum<minimum)throw new IllegalArgumentException("Invalid role limits");}
    }
    public record RoleGroup(Set<String> roles,int minimum){public RoleGroup{roles=Set.copyOf(roles);if(minimum<1)throw new IllegalArgumentException("Invalid role group");}}
    public record Shape(int width,int depth,int height,int left) {
        public Shape {if(width<3||width>11||depth<3||depth>11||height<2||height>11||left<1||left>width-2)throw new IllegalArgumentException("Invalid shape");}
        public MultiblockDefinition.Bounds bounds(){return new MultiblockDefinition.Bounds(-left,-1,0,width-left-1,height-2,depth-1);}
        public int internalVolume(boolean closed){return (width-2)*(depth-2)*(height-(closed?2:1));}
    }
    public record Inspection(MultiblockDefinition.Result result,Shape shape,MultiblockDefinition snapshot,Map<String,Integer> roles,String detail,int matched) {}
    public ComplexMultiblockDefinition {
        roles=List.copyOf(roles);groups=List.copyOf(groups);
        if(minWidth<3||minDepth<3||minHeight<(closed?3:2)||maxWidth>11||maxDepth>11||maxHeight>11||minWidth>maxWidth||minDepth>maxDepth||minHeight>maxHeight)throw new IllegalArgumentException("Invalid complex structure size range");
        if(roles.stream().map(Role::name).distinct().count()!=roles.size())throw new IllegalArgumentException("Duplicate roles");
    }
    public Inspection discover(Level level,BlockPos origin,Rotation rotation) {
        Inspection best=null;
        for(int w=minWidth;w<=maxWidth;w++)for(int d=minDepth;d<=maxDepth;d++)for(int h=minHeight;h<=maxHeight;h++)for(int left=1;left<w-1;left++) {
            var inspection=inspect(level,origin,rotation,new Shape(w,d,h,left));
            if(inspection.result()==MultiblockDefinition.Result.COMPLETE)return inspection;
            if(best==null||inspection.matched()>best.matched())best=inspection;
        }
        return best;
    }
    public Inspection inspect(Level level,BlockPos origin,Rotation rotation,Shape shape) {
        if(shape.width<minWidth||shape.width>maxWidth||shape.depth<minDepth||shape.depth>maxDepth||shape.height<minHeight||shape.height>maxHeight)return failure(shape,"size",0);
        var bounds=shape.bounds();
        // Reject undersized open-shell prefixes: the rim must be the actual top of the walls.
        for(int x=bounds.minX();x<=bounds.maxX();x++)for(int z=0;z<shape.depth;z++)if(x==bounds.minX()||x==bounds.maxX()||z==0||z==shape.depth-1){
            var above=origin.offset(new BlockPos(x,bounds.maxY()+1,z).rotate(rotation));
            if(!level.hasChunkAt(above))return new Inspection(MultiblockDefinition.Result.UNLOADED,shape,null,Map.of(),"unloaded",0);
            if(!closed&&casing.matches(level.getBlockState(above)))return failure(shape,"wall continues above rim",0);
        }
        Map<BlockPos,BlockIngredient> parts=new LinkedHashMap<>();Map<String,Integer> counts=new LinkedHashMap<>();int matched=0;
        for(int x=bounds.minX();x<=bounds.maxX();x++)for(int y=-1;y<=bounds.maxY();y++)for(int z=0;z<shape.depth;z++) {
            var local=new BlockPos(x,y,z);var world=origin.offset(local.rotate(rotation));
            if(!level.hasChunkAt(world))return new Inspection(MultiblockDefinition.Result.UNLOADED,shape,null,Map.of(),"unloaded",matched);
            if(local.equals(BlockPos.ZERO))continue;
            var state=level.getBlockState(world);
            Zone zone=y==-1?Zone.FLOOR:x==bounds.minX()||x==bounds.maxX()||z==0||z==shape.depth-1?Zone.WALL:closed&&y==bounds.maxY()?Zone.ROOF:Zone.INTERIOR;
            Role role=null;
            for(var candidate:roles)if(candidate.zones.contains(zone)&&candidate.ingredient.matches(state)){role=candidate;break;}
            BlockIngredient required;
            if(role!=null){
                int boundaries=(x==bounds.minX()||x==bounds.maxX()?1:0)+(z==0||z==shape.depth-1?1:0)+(y==-1||y==bounds.maxY()?1:0);
                if(!role.name.equals("lining")&&boundaries>1)return failure(shape,"edge|"+world.getX()+"|"+world.getY()+"|"+world.getZ(),matched);
                if(role.name.equals("fuel")&&level.getBlockEntity(world) instanceof net.minecraft.world.Container container&&!container.isEmpty())return failure(shape,"empty fuel container @ "+local.toShortString(),matched);
                counts.merge(role.name,1,Integer::sum);required=role.ingredient;
            }
            else {required=switch(zone){case FLOOR->floor;case WALL,ROOF->casing;case INTERIOR->new BlockIngredient("minecraft:air");};}
            if(!required.matches(state))return failure(shape,"block|"+world.getX()+"|"+world.getY()+"|"+world.getZ()+"|"+zone.name().toLowerCase(),matched);
            parts.put(local,required);matched++;
        }
        for(var role:roles){int count=counts.getOrDefault(role.name,0);if(count<role.minimum||count>role.maximum)return failure(shape,"role|"+role.name+"|"+count+"|"+role.minimum+"|"+role.maximum,matched);}
        for(var group:groups)if(group.roles.stream().mapToInt(name->counts.getOrDefault(name,0)).sum()<group.minimum)return failure(shape,String.join(" / ",group.roles),matched);
        return new Inspection(MultiblockDefinition.Result.COMPLETE,shape,new MultiblockDefinition(id,parts),Map.copyOf(counts),"complete",matched);
    }
    /** Snapshot for rendering/JEI: valid assembled blocks and required air only. */
    public MultiblockDefinition visual(Level level,BlockPos origin,Rotation rotation,Shape shape) {
        var bounds=shape.bounds();Map<BlockPos,BlockIngredient> parts=new LinkedHashMap<>();
        for(int x=bounds.minX();x<=bounds.maxX();x++)for(int y=-1;y<=bounds.maxY();y++)for(int z=0;z<shape.depth;z++) {
            var local=new BlockPos(x,y,z);if(local.equals(BlockPos.ZERO))continue;
            boolean interior=x>bounds.minX()&&x<bounds.maxX()&&z>0&&z<shape.depth-1&&y>=0&&(!closed||y<bounds.maxY());
            if(interior&&roles.stream().noneMatch(r->r.zones.contains(Zone.INTERIOR)))continue;
            var world=origin.offset(local.rotate(rotation));
            var state=level.hasChunkAt(world)?level.getBlockState(world):net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
            if(state.isAir())continue;
            parts.put(local,new BlockIngredient(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString()));
        }
        return new MultiblockDefinition(id,parts);
    }
    private Inspection failure(Shape shape,String detail,int matched){return new Inspection(MultiblockDefinition.Result.INCOMPLETE,shape,null,Map.of(),detail,matched);}
}
