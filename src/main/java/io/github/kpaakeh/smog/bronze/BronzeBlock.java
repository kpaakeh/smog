package io.github.kpaakeh.smog.bronze;

import io.github.kpaakeh.smog.copper.WorkshopModelShapes;
import io.github.kpaakeh.smog.registry.ConfiguratorItem;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;

/** Physical parts stay in their own world cells, including after assembly. */
public final class BronzeBlock extends Block implements EntityBlock {
    public static final EnumProperty<Direction> FACING=BlockStateProperties.FACING;
    public static final BooleanProperty ASSEMBLED=BooleanProperty.create("assembled");
    public static final BooleanProperty OPEN=net.minecraft.world.level.block.state.properties.BlockStateProperties.OPEN;
    private final String id;
    public BronzeBlock(Properties properties,String id){super(properties);this.id=id;registerDefaultState(stateDefinition.any().setValue(FACING,Direction.NORTH).setValue(ASSEMBLED,false).setValue(OPEN,true));}
    public String id(){return id;}
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){builder.add(FACING,ASSEMBLED,OPEN);}
    @Override public BlockState getStateForPlacement(BlockPlaceContext context){
        var spec=BronzeContent.entry(id);Direction facing=spec.axis()?context.getClickedFace():context.getHorizontalDirection().getOpposite();
        if(spec.attachment())facing=context.getClickedFace();
        var state=defaultBlockState().setValue(FACING,facing);return canSurvive(state,context.getLevel(),context.getClickedPos())?state:null;
    }
    @Override protected boolean canSurvive(BlockState state,LevelReader level,BlockPos pos){
        if(!BronzeContent.entry(id).attachment())return true;
        var back=state.getValue(FACING).getOpposite();var support=pos.relative(back);
        return level.getBlockState(support).isFaceSturdy(level,support,back.getOpposite());
    }
    @Override protected void neighborChanged(BlockState state,Level level,BlockPos pos,Block neighbor,net.minecraft.world.level.redstone.Orientation orientation,boolean moved){
        if(!level.isClientSide())BronzeNetwork.invalidate(level);
        if(!level.isClientSide()&&!canSurvive(state,level,pos))level.destroyBlock(pos,true);
    }
    @Override protected VoxelShape getShape(BlockState state,BlockGetter level,BlockPos pos,CollisionContext context){
        var facing=state.getValue(FACING);var shape=WorkshopModelShapes.get(id+"_inventory",facing.getAxis()==Direction.Axis.Y?Direction.NORTH:facing);
        if(facing.getAxis()!=Direction.Axis.Y)return shape;
        var boxes=new java.util.ArrayList<VoxelShape>();
        shape.forAllBoxes((x,y,z,xx,yy,zz)->boxes.add(facing==Direction.UP?Shapes.box(x,z,1-yy,xx,zz,1-y):Shapes.box(x,1-zz,y,xx,1-z,yy)));
        return boxes.stream().reduce(Shapes.empty(),Shapes::or);
    }
    @Override protected InteractionResult useItemOn(ItemStack stack,BlockState state,Level level,BlockPos pos,net.minecraft.world.entity.player.Player player,InteractionHand hand,BlockHitResult hit){
        if(stack.getItem() instanceof ConfiguratorItem){
            if(!level.isClientSide()){
                var entity=level.getBlockEntity(pos) instanceof BronzeEntity bronze?bronze:null;
                if(player.isShiftKeyDown()){
                    var face=state.getValue(FACING);var next=BronzeContent.entry(id).axis()?Direction.values()[(face.ordinal()+1)%Direction.values().length]:face.getAxis()==Direction.Axis.Y?Direction.NORTH:face.getClockWise();
                    var rotated=state.setValue(FACING,next).setValue(ASSEMBLED,false);
                    if(canSurvive(rotated,level,pos)){if(entity!=null)entity.disassemble();level.setBlock(pos,rotated,Block.UPDATE_ALL);}
                }else if(entity!=null)entity.inspect(player);
            }return InteractionResult.SUCCESS;
        }return super.useItemOn(stack,state,level,pos,player,hand,hit);
    }
    @Override protected InteractionResult useWithoutItem(BlockState state,Level level,BlockPos pos,net.minecraft.world.entity.player.Player player,BlockHitResult hit){
        if(!level.isClientSide()&&level.getBlockEntity(pos) instanceof BronzeEntity entity){
            if(id.equals("bronze_clutch")){level.setBlock(pos,state.cycle(OPEN),Block.UPDATE_ALL);BronzeNetwork.invalidate(level);entity.inspect(player);return InteractionResult.SUCCESS;}
            if(id.equals("bronze_hand_crank"))entity.crank();else entity.inspect(player);
        }return InteractionResult.SUCCESS;
    }
    @Override protected BlockState rotate(BlockState state,Rotation rotation){return state.setValue(FACING,rotation.rotate(state.getValue(FACING)));}
    @Override protected BlockState mirror(BlockState state,Mirror mirror){return rotate(state,mirror.getRotation(state.getValue(FACING)));}
    @Override public BlockEntity newBlockEntity(BlockPos pos,BlockState state){var entry=BronzeContent.entry(id);return entry.group().equals("controller")||!entry.animation().equals("none")?new BronzeEntity(pos,state):null;}
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level,BlockState state,BlockEntityType<T> type){
        if(level.isClientSide()||type!=BronzeContent.ENTITY_TYPE||!BronzeContent.entry(id).group().equals("controller")&&!BronzeNetwork.node(id))return null;
        return (world,pos,block,entity)->((BronzeEntity)entity).tick();
    }
}
