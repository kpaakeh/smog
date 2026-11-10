package io.github.kpaakeh.smog.bronze;

import io.github.kpaakeh.smog.registry.ConfiguratorItem;
import java.util.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import io.github.kpaakeh.smog.copper.WorkshopModelShapes;

public final class BronzeSplitterBlock extends BronzeAngleBlock {
    private static final Map<BlockState,VoxelShape> SHAPES=new java.util.concurrent.ConcurrentHashMap<>();
    public static final BooleanProperty FRONT=BooleanProperty.create("front"),RIGHT=BooleanProperty.create("right"),LEFT=BooleanProperty.create("left");
    public BronzeSplitterBlock(Properties properties){super(properties,"bronze_splitter");registerDefaultState(defaultBlockState().setValue(FRONT,true).setValue(RIGHT,true).setValue(LEFT,true));}
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){super.createBlockStateDefinition(builder);builder.add(FRONT,RIGHT,LEFT);}
    public static BooleanProperty branch(BlockState state,Direction side){return side==state.getValue(FACING)?FRONT:side==output(state)?RIGHT:side==output(state).getOpposite()?LEFT:null;}
    public static Set<Direction> ports(BlockState state){
        var result=EnumSet.of(state.getValue(FACING).getOpposite());for(var side:Direction.values()){var branch=branch(state,side);if(branch!=null&&state.getValue(branch))result.add(side);}return result;
    }
    @Override protected VoxelShape getShape(BlockState state,BlockGetter level,BlockPos pos,CollisionContext context){
        var geometry=state.setValue(ASSEMBLED,false).setValue(OPEN,true).setValue(OUTPUT,output(state));
        return SHAPES.computeIfAbsent(geometry,s->{String model=model(s);var shape=WorkshopModelShapes.get(model+"_inventory",Direction.NORTH);
            for(var entry:Map.of("front",FRONT,"right",RIGHT,"left",LEFT).entrySet())shape=Shapes.or(shape,WorkshopModelShapes.get(model+(s.getValue(entry.getValue())?"_shaft_":"_cap_")+entry.getKey(),Direction.NORTH));return shape.optimize();});
    }
    @Override protected InteractionResult useItemOn(ItemStack stack,BlockState state,Level level,BlockPos pos,Player player,InteractionHand hand,BlockHitResult hit){
        var branch=branch(state,hit.getDirection());
        if(stack.getItem() instanceof ConfiguratorItem&&player.isShiftKeyDown()&&branch!=null){
            if(!level.isClientSide()){var next=state.cycle(branch);level.setBlock(pos,next,Block.UPDATE_ALL);BronzeNetwork.invalidate(level);player.sendOverlayMessage(Component.translatable(next.getValue(branch)?"message.smog.branch_engaged":"message.smog.branch_disengaged"));}return InteractionResult.SUCCESS;
        }return super.useItemOn(stack,state,level,pos,player,hand,hit);
    }
}
