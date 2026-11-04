package io.github.kpaakeh.smog.bronze;

import io.github.kpaakeh.smog.copper.WorkshopModelShapes;
import io.github.kpaakeh.smog.registry.ConfiguratorItem;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;

/** A pair of perpendicular bevel gears: rear input and one visible side output. */
public final class BronzeAngleBlock extends BronzeBlock {
    public static final EnumProperty<Direction> OUTPUT=EnumProperty.create("output",Direction.class);
    public BronzeAngleBlock(Properties properties){super(properties,"bronze_angle_gearbox");registerDefaultState(defaultBlockState().setValue(OUTPUT,Direction.EAST));}
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){super.createBlockStateDefinition(builder);builder.add(OUTPUT);}
    public static Direction output(BlockState state){var facing=state.getValue(FACING);var output=state.getValue(OUTPUT);return output.getAxis()!=facing.getAxis()?output:facing.getAxis()==Direction.Axis.Y?Direction.EAST:facing.getClockWise();}
    public static String model(BlockState state){return "bronze_angle_gearbox_"+state.getValue(FACING).getName()+"_"+output(state).getName();}
    public static Direction nextOutput(Direction facing,Direction current){var options=java.util.Arrays.stream(Direction.values()).filter(d->d.getAxis()!=facing.getAxis()).toList();return options.get((options.indexOf(current)+1)%options.size());}
    @Override public BlockState getStateForPlacement(BlockPlaceContext context){
        var facing=context.getClickedFace();Direction output=null;
        for(var look:context.getNearestLookingDirections())if(look.getAxis()!=facing.getAxis()){
            if(output==null)output=look;var next=context.getClickedPos().relative(look);
            if(context.getLevel().hasChunkAt(next)&&BronzeNetwork.ports(context.getLevel(),next,context.getLevel().getBlockState(next)).contains(look.getOpposite())){output=look;break;}
        }
        return defaultBlockState().setValue(FACING,facing).setValue(OUTPUT,output);
    }
    @Override protected VoxelShape getShape(BlockState state,BlockGetter level,BlockPos pos,CollisionContext context){return WorkshopModelShapes.get(model(state)+"_inventory",Direction.NORTH);}
    @Override protected InteractionResult useItemOn(ItemStack stack,BlockState state,Level level,BlockPos pos,Player player,InteractionHand hand,BlockHitResult hit){
        if(stack.getItem() instanceof ConfiguratorItem&&player.isShiftKeyDown()){
            if(!level.isClientSide()){
                if(level.getBlockEntity(pos) instanceof BronzeEntity entity)entity.disassemble();
                level.setBlock(pos,state.setValue(OUTPUT,nextOutput(state.getValue(FACING),output(state))).setValue(ASSEMBLED,false),Block.UPDATE_ALL);BronzeNetwork.invalidate(level);
            }return InteractionResult.SUCCESS;
        }return super.useItemOn(stack,state,level,pos,player,hand,hit);
    }
    @Override protected BlockState rotate(BlockState state,Rotation rotation){return state.setValue(FACING,rotation.rotate(state.getValue(FACING))).setValue(OUTPUT,rotation.rotate(output(state)));}
    @Override protected BlockState mirror(BlockState state,Mirror mirror){return state.setValue(FACING,mirror.mirror(state.getValue(FACING))).setValue(OUTPUT,mirror.mirror(output(state)));}
}
