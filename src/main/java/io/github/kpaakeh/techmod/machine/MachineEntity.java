package io.github.kpaakeh.techmod.machine;

import io.github.kpaakeh.techmod.multiblock.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/** Generic controller. Persistent progress is available to simple machine callbacks. */
public final class MachineEntity extends BlockEntity {
    private final Identifier machineId;
    private int progress;
    public MachineEntity(Identifier id, BlockPos pos, BlockState state) { super(Machines.get(id).entityType(), pos, state); machineId = id; }
    public int progress() { return progress; }
    public void progress(int value) { if (value < 0) throw new IllegalArgumentException("Negative progress"); progress = value; setChanged(); }
    public boolean assembled() { return getBlockState().getValue(MachineBlock.ASSEMBLED); }
    public void serverTick() {
        if (!(level instanceof ServerLevel server)) return;
        MachineDefinition definition = Machines.get(machineId).definition();
        // Revalidate every tick: dismantling or unloading a part stops processing immediately.
        boolean complete = definition.multiblock() == null;
        if (!complete) {
            MultiblockDefinition structure = Multiblocks.get(definition.multiblock());
            Rotation rotation = switch (getBlockState().getValue(MachineBlock.FACING)) {
                case EAST -> Rotation.CLOCKWISE_90; case SOUTH -> Rotation.CLOCKWISE_180;
                case WEST -> Rotation.COUNTERCLOCKWISE_90; default -> Rotation.NONE;
            };
            complete = structure != null && structure.check(server, worldPosition, rotation) == MultiblockDefinition.Result.COMPLETE;
        }
        if (assembled() != complete) server.setBlock(worldPosition, getBlockState().setValue(MachineBlock.ASSEMBLED, complete), Block.UPDATE_CLIENTS);
        if (complete && server.getGameTime() % definition.interval() == 0) definition.action().tick(server, this);
    }
    @Override protected void saveAdditional(ValueOutput output) { super.saveAdditional(output); output.putInt("progress", progress); }
    @Override protected void loadAdditional(ValueInput input) { super.loadAdditional(input); progress = Math.max(0, input.getIntOr("progress", 0)); }
}
