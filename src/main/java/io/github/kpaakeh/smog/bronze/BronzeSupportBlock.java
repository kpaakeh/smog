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

/** The bearing's foot faces its mounting surface; the independent shaft has only three axes. */
public final class BronzeSupportBlock extends BronzeBlock {
    public static final EnumProperty<Direction> MOUNT=EnumProperty.create("mount",Direction.class);
    public BronzeSupportBlock(Properties properties){super(properties,"bronze_shaft_support");registerDefaultState(defaultBlockState().setValue(MOUNT,Direction.DOWN));}
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){super.createBlockStateDefinition(builder);builder.add(MOUNT);}
    public static boolean mounted(BlockState state,LevelReader level,BlockPos pos){
        var mount=state.getValue(MOUNT);if(mount.getAxis()==state.getValue(FACING).getAxis())return false;
        var foot=pos.relative(mount);return level.hasChunkAt(foot)&&level.getBlockState(foot).isFaceSturdy(level,foot,mount.getOpposite());
    }
    @Override protected boolean canSurvive(BlockState state,LevelReader level,BlockPos pos){return mounted(state,level,pos);}
    public static Direction placementAxis(Level level,BlockPos pos,Direction mount,Direction[] looking){
        for(var look:looking){if(look.getAxis()==mount.getAxis())continue;var axis=BronzeShaft.canonical(look);
            for(var end:new Direction[]{axis,axis.getOpposite()}){var next=pos.relative(end);if(level.hasChunkAt(next)&&BronzeNetwork.ports(level,next,level.getBlockState(next)).contains(end.getOpposite()))return axis;}
        }
        for(var look:looking)if(look.getAxis()!=mount.getAxis())return BronzeShaft.canonical(look);
        throw new IllegalArgumentException("No perpendicular shaft axis");
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context){
        var mount=context.getClickedFace().getOpposite();var axis=placementAxis(context.getLevel(),context.getClickedPos(),mount,context.getNearestLookingDirections());
        var state=defaultBlockState().setValue(MOUNT,mount).setValue(FACING,axis);return canSurvive(state,context.getLevel(),context.getClickedPos())?state:null;
    }
    @Override protected VoxelShape getShape(BlockState state,BlockGetter level,BlockPos pos,CollisionContext context){
        if(state.getValue(FACING).getAxis()==state.getValue(MOUNT).getAxis())return Shapes.empty();
        return WorkshopModelShapes.get("bronze_shaft_support_"+state.getValue(FACING).getAxis().getName()+"_"+state.getValue(MOUNT).getName()+"_inventory",Direction.NORTH);
    }
    @Override protected InteractionResult useItemOn(ItemStack stack,BlockState state,Level level,BlockPos pos,Player player,InteractionHand hand,BlockHitResult hit){
        if(stack.getItem() instanceof ConfiguratorItem&&player.isShiftKeyDown()){
            if(!level.isClientSide()){
                var axis=BronzeShaft.nextAxis(state.getValue(FACING));if(axis.getAxis()==state.getValue(MOUNT).getAxis())axis=BronzeShaft.nextAxis(axis);
                if(level.getBlockEntity(pos) instanceof BronzeEntity entity)entity.disassemble();
                level.setBlock(pos,state.setValue(FACING,axis).setValue(ASSEMBLED,false),Block.UPDATE_ALL);BronzeNetwork.invalidate(level);
            }return InteractionResult.SUCCESS;
        }return super.useItemOn(stack,state,level,pos,player,hand,hit);
    }
    @Override protected BlockState rotate(BlockState state,Rotation rotation){return state.setValue(FACING,BronzeShaft.canonical(rotation.rotate(state.getValue(FACING)))).setValue(MOUNT,rotation.rotate(state.getValue(MOUNT)));}
    @Override protected BlockState mirror(BlockState state,Mirror mirror){return state.setValue(FACING,BronzeShaft.canonical(mirror.mirror(state.getValue(FACING)))).setValue(MOUNT,mirror.mirror(state.getValue(MOUNT)));}
}
