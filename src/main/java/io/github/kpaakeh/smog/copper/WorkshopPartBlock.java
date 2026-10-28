package io.github.kpaakeh.smog.copper;

import io.github.kpaakeh.smog.registry.ConfiguratorItem;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;

/** Movable ports and channels have an editable direction, independent of controller location. */
public final class WorkshopPartBlock extends Block {
    public static final EnumProperty<Direction> FACING=BlockStateProperties.HORIZONTAL_FACING;
    public WorkshopPartBlock(Properties properties){super(properties);registerDefaultState(stateDefinition.any().setValue(FACING,Direction.NORTH));}
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){builder.add(FACING);}
    @Override public BlockState getStateForPlacement(BlockPlaceContext context){return defaultBlockState().setValue(FACING,context.getHorizontalDirection().getOpposite());}
    @Override protected BlockState rotate(BlockState state,Rotation rotation){return state.setValue(FACING,rotation.rotate(state.getValue(FACING)));}
    @Override protected BlockState mirror(BlockState state,Mirror mirror){return rotate(state,mirror.getRotation(state.getValue(FACING)));}
    @Override protected InteractionResult useItemOn(ItemStack stack,BlockState state,Level level,BlockPos pos,net.minecraft.world.entity.player.Player player,InteractionHand hand,net.minecraft.world.phys.BlockHitResult hit){
        if(stack.getItem() instanceof ConfiguratorItem){if(!level.isClientSide())level.setBlock(pos,state.setValue(FACING,state.getValue(FACING).getClockWise()),Block.UPDATE_ALL);return InteractionResult.SUCCESS;}
        return super.useItemOn(stack,state,level,pos,player,hand,hit);
    }
}
