package io.github.kpaakeh.smog.machine;

import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/** Attachment points out from the clicked support face. Old horizontal facing remains compatible. */
public final class ConnectorBlock extends MachineBlock {
    public static final EnumProperty<Direction> ATTACHMENT=EnumProperty.create("attachment",Direction.class);
    public ConnectorBlock(BlockBehaviour.Properties properties,Identifier id){
        super(properties,id);registerDefaultState(defaultBlockState().setValue(ATTACHMENT,Direction.UP));
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder){super.createBlockStateDefinition(builder);builder.add(ATTACHMENT);}
    @Override public BlockState getStateForPlacement(BlockPlaceContext context){return super.getStateForPlacement(context).setValue(ATTACHMENT,context.getClickedFace());}
    @Override protected BlockState rotate(BlockState state,Rotation rotation){return super.rotate(state,rotation).setValue(ATTACHMENT,rotation.rotate(state.getValue(ATTACHMENT)));}
    @Override protected BlockState mirror(BlockState state,Mirror mirror){return super.mirror(state,mirror).setValue(ATTACHMENT,mirror.mirror(state.getValue(ATTACHMENT)));}
    public static Direction attachment(BlockState state){return state.hasProperty(ATTACHMENT)?state.getValue(ATTACHMENT):Direction.UP;}
}
