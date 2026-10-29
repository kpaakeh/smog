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
public class WorkshopPartBlock extends Block {
    public static final BooleanProperty OPEN=BlockStateProperties.OPEN;
    public static final EnumProperty<Direction> FACING=BlockStateProperties.HORIZONTAL_FACING;
    public WorkshopPartBlock(Properties properties){super(properties);registerDefaultState(stateDefinition.any().setValue(FACING,Direction.NORTH).setValue(OPEN,false));}
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){builder.add(FACING,OPEN);}
    private String partName(){return net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(this).getPath();}
    private boolean attachment(){return partName().equals("casting_faucet")||partName().equals("air_nozzle");}
    @Override public BlockState getStateForPlacement(BlockPlaceContext context){
        if(attachment()){
            var side=context.getClickedFace();if(side.getAxis()==Direction.Axis.Y)return null;
            var behind=context.getClickedPos().relative(side.getOpposite());String expected=partName().equals("casting_faucet")?"casting_spout":"air_intake";
            if(!context.getLevel().getBlockState(behind).is(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(io.github.kpaakeh.smog.registry.ModBlocks.id(expected))))return null;
            return defaultBlockState().setValue(FACING,side);
        }
        return defaultBlockState().setValue(FACING,context.getHorizontalDirection().getOpposite());
    }
    @Override protected InteractionResult useWithoutItem(BlockState state,Level level,BlockPos pos,net.minecraft.world.entity.player.Player player,net.minecraft.world.phys.BlockHitResult hit){
        if(!attachment()&&!partName().equals("water_valve")&&!partName().equals("water_outlet"))return InteractionResult.PASS;
        if(!level.isClientSide()){boolean open=!state.getValue(OPEN);level.setBlock(pos,state.setValue(OPEN,open),Block.UPDATE_ALL);player.sendOverlayMessage(net.minecraft.network.chat.Component.translatable(open?"message.smog.valve_open":"message.smog.valve_closed"));}
        return InteractionResult.SUCCESS;
    }
    @Override protected BlockState rotate(BlockState state,Rotation rotation){return state.setValue(FACING,rotation.rotate(state.getValue(FACING)));}
    @Override protected BlockState mirror(BlockState state,Mirror mirror){return rotate(state,mirror.getRotation(state.getValue(FACING)));}
    @Override protected net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state,net.minecraft.world.level.BlockGetter level,BlockPos pos,net.minecraft.world.phys.shapes.CollisionContext context){
        var name=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(this).getPath();
        if(name.equals("ceramic_crucible")||name.equals("large_ceramic_crucible")){double lo=name.startsWith("large")?0:2,hi=16-lo;return net.minecraft.world.phys.shapes.Shapes.or(box(lo,0,lo,hi,3,hi),box(lo,3,lo,lo+2,14,hi),box(hi-2,3,lo,hi,14,hi),box(lo+2,3,lo,hi-2,14,lo+2),box(lo+2,3,hi-2,hi-2,14,hi));}
        if(name.equals("casting_faucet"))return WorkshopModelShapes.get(state.getValue(OPEN)?"casting_faucet_open":"casting_faucet",state.getValue(FACING));
        var shape=WorkshopShapes.shape(name,state.getValue(FACING));return shape==null?super.getShape(state,level,pos,context):shape;
    }
    @Override protected InteractionResult useItemOn(ItemStack stack,BlockState state,Level level,BlockPos pos,net.minecraft.world.entity.player.Player player,InteractionHand hand,net.minecraft.world.phys.BlockHitResult hit){
        if(stack.getItem() instanceof ConfiguratorItem tool)return tool.useOn(new net.minecraft.world.item.context.UseOnContext(player,hand,hit));
        return super.useItemOn(stack,state,level,pos,player,hand,hit);
    }
}
