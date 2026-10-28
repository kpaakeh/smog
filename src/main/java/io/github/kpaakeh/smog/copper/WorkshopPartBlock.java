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
    @Override protected net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state,net.minecraft.world.level.BlockGetter level,BlockPos pos,net.minecraft.world.phys.shapes.CollisionContext context){
        var name=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(this).getPath();
        if(name.equals("ceramic_crucible")||name.equals("large_ceramic_crucible")){double lo=name.startsWith("large")?0:2,hi=16-lo;return net.minecraft.world.phys.shapes.Shapes.or(box(lo,0,lo,hi,3,hi),box(lo,3,lo,lo+2,14,hi),box(hi-2,3,lo,hi,14,hi),box(lo+2,3,lo,hi-2,14,lo+2),box(lo+2,3,hi-2,hi-2,14,hi));}
        return super.getShape(state,level,pos,context);
    }
    @Override protected InteractionResult useItemOn(ItemStack stack,BlockState state,Level level,BlockPos pos,net.minecraft.world.entity.player.Player player,InteractionHand hand,net.minecraft.world.phys.BlockHitResult hit){
        if(stack.getItem() instanceof ConfiguratorItem){if(!level.isClientSide())level.setBlock(pos,state.setValue(FACING,state.getValue(FACING).getClockWise()),Block.UPDATE_ALL);return InteractionResult.SUCCESS;}
        return super.useItemOn(stack,state,level,pos,player,hand,hit);
    }
}
