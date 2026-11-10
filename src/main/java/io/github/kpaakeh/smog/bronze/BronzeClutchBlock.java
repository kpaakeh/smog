package io.github.kpaakeh.smog.bronze;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;

/** Redstone overrides, but never erases, the player's manual setting. */
public final class BronzeClutchBlock extends BronzeBlock {
    public static final BooleanProperty POWERED=BlockStateProperties.POWERED;
    public BronzeClutchBlock(Properties properties){this(properties,"bronze_clutch");}
    public BronzeClutchBlock(Properties properties,String id){super(properties,id);registerDefaultState(defaultBlockState().setValue(POWERED,false));}
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){super.createBlockStateDefinition(builder);builder.add(POWERED);}
    public static boolean engaged(BlockState state){return state.getValue(OPEN)&&!state.getValue(POWERED);}
    @Override public BlockState getStateForPlacement(BlockPlaceContext context){var state=super.getStateForPlacement(context);return state==null?null:state.setValue(POWERED,context.getLevel().hasNeighborSignal(context.getClickedPos()));}
    @Override protected void neighborChanged(BlockState state,Level level,BlockPos pos,Block neighbor,net.minecraft.world.level.redstone.Orientation orientation,boolean moved){
        super.neighborChanged(state,level,pos,neighbor,orientation,moved);
        if(!level.isClientSide()){boolean powered=level.hasNeighborSignal(pos);if(state.getValue(POWERED)!=powered)level.setBlock(pos,state.setValue(POWERED,powered),Block.UPDATE_ALL);}
    }
}
