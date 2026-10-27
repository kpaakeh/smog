package io.github.kpaakeh.smog.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;

public final class MachineBlock extends Block implements EntityBlock {
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<GeneratorKind> GENERATOR = EnumProperty.create("generator",GeneratorKind.class);
    private final Identifier machineId;
    public MachineBlock(BlockBehaviour.Properties properties, Identifier id) {
        super(properties); machineId = id;
        registerDefaultState(stateDefinition.any().setValue(ASSEMBLED, false).setValue(FACING, Direction.NORTH).setValue(GENERATOR,GeneratorKind.NONE));
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(ASSEMBLED,FACING,GENERATOR); }
    @Override protected net.minecraft.world.InteractionResult useWithoutItem(BlockState state,Level level,BlockPos pos,net.minecraft.world.entity.player.Player player,net.minecraft.world.phys.BlockHitResult hit) {
        if(level.getBlockEntity(pos) instanceof MachineEntity machine && machine.spec().gui()) {
            if(!level.isClientSide())player.openMenu(machine);
            return net.minecraft.world.InteractionResult.SUCCESS;
        }
        return net.minecraft.world.InteractionResult.PASS;
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) { return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()); }
    @Override protected BlockState rotate(BlockState state, Rotation rotation) { return state.setValue(FACING, rotation.rotate(state.getValue(FACING))); }
    @Override protected BlockState mirror(BlockState state, Mirror mirror) { return rotate(state, mirror.getRotation(state.getValue(FACING))); }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new MachineEntity(machineId, pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide() || type != Machines.get(machineId).entityType()) return null;
        return (world, pos, blockState, entity) -> ((MachineEntity) entity).serverTick();
    }
}
