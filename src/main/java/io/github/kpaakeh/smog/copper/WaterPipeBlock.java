package io.github.kpaakeh.smog.copper;

import net.minecraft.core.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.*;

/** Six-way workshop pipe; connectivity and its collision are synchronized block states. No per-pipe tank. */
public final class WaterPipeBlock extends Block {
    public static BooleanProperty property(Direction direction){return switch(direction){case NORTH->BlockStateProperties.NORTH;case SOUTH->BlockStateProperties.SOUTH;case EAST->BlockStateProperties.EAST;case WEST->BlockStateProperties.WEST;case UP->BlockStateProperties.UP;case DOWN->BlockStateProperties.DOWN;};}
    public WaterPipeBlock(Properties properties){super(properties);var state=stateDefinition.any();for(var side:Direction.values())state=state.setValue(property(side),false);registerDefaultState(state);}
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){for(var side:Direction.values())builder.add(property(side));}
    private BlockState connections(BlockState state,LevelReader level,BlockPos pos){for(var side:Direction.values())state=state.setValue(property(side),WaterSupply.connects(level,pos.relative(side)));return state;}
    @Override public BlockState getStateForPlacement(BlockPlaceContext context){return connections(defaultBlockState(),context.getLevel(),context.getClickedPos());}
    @Override protected BlockState updateShape(BlockState state,LevelReader level,ScheduledTickAccess ticks,BlockPos pos,Direction direction,BlockPos neighbor,BlockState neighborState,RandomSource random){return connections(state,level,pos);}
    @Override protected VoxelShape getShape(BlockState state,BlockGetter level,BlockPos pos,CollisionContext context){
        var shape=box(5,5,5,11,11,11);
        for(var side:Direction.values())if(state.getValue(property(side)))shape=Shapes.or(shape,switch(side){case NORTH->box(5,5,0,11,11,5);case SOUTH->box(5,5,11,11,11,16);case WEST->box(0,5,5,5,11,11);case EAST->box(11,5,5,16,11,11);case UP->box(5,11,5,11,16,11);case DOWN->box(5,0,5,11,5,11);});return shape;
    }
}
