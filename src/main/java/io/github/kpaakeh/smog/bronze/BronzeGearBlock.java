package io.github.kpaakeh.smog.bronze;

import java.util.*;
import net.minecraft.core.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;

/** Visible 24/12-tooth wheels. Only teeth that physically reach each other transmit rotation. */
public final class BronzeGearBlock extends BronzeBlock {
    private final boolean large;
    public BronzeGearBlock(Properties properties,String id){super(properties,id);large=id.equals("bronze_reduction_gearbox");}
    public boolean large(){return large;}
    public int teeth(){return large?24:12;}
    public static boolean meshes(BlockState a,Direction side,BlockState b){
        return a.getBlock() instanceof BronzeGearBlock first&&b.getBlock() instanceof BronzeGearBlock second&&first.large!=second.large&&a.getValue(FACING).getAxis()==b.getValue(FACING).getAxis()&&side.getAxis()!=a.getValue(FACING).getAxis();
    }
    public static Set<Direction> ports(Level level,BlockPos pos,BlockState state){
        var axis=state.getValue(FACING);var result=EnumSet.of(axis,axis.getOpposite());
        for(var side:Direction.values())if(side.getAxis()!=axis.getAxis()&&level.hasChunkAt(pos.relative(side))&&meshes(state,side,level.getBlockState(pos.relative(side))))result.add(side);
        return result;
    }
    public String clearance(LevelReader level,BlockPos pos,BlockState state){
        if(!large)return "";var axis=state.getValue(FACING).getAxis();var shape=state.getCollisionShape(level,pos);
        for(int a=-1;a<=1;a++)for(int b=-1;b<=1;b++){
            if(a==0&&b==0)continue;int dx=axis==Direction.Axis.X?0:a,dy=axis==Direction.Axis.Y?0:axis==Direction.Axis.X?a:b,dz=axis==Direction.Axis.Z?0:b;
            var neighbor=pos.offset(dx,dy,dz);if(!level.hasChunkAt(neighbor))return "unloaded";var other=level.getBlockState(neighbor);
            var side=dx>0?Direction.EAST:dx<0?Direction.WEST:dy>0?Direction.UP:dy<0?Direction.DOWN:dz>0?Direction.SOUTH:Direction.NORTH;
            if(Math.abs(a)+Math.abs(b)==1&&meshes(state,side,other))continue;
            var obstruction=other.getCollisionShape(level,neighbor).move(dx,dy,dz);if(Shapes.joinIsNotEmpty(shape,obstruction,BooleanOp.AND))return "blocked";
        }return "";
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context){var state=super.getStateForPlacement(context);return state!=null&&clearance(context.getLevel(),context.getClickedPos(),state).isEmpty()?state:null;}
}
