package io.github.kpaakeh.smog.render;

import java.util.*;
import io.github.kpaakeh.smog.machine.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.Rotation;

/** Immutable snapshots are safe to read from chunk meshing workers. Only visuals are changed. */
public final class ClientFormations {
    private static final Set<MachineEntity> LOADED = Collections.newSetFromMap(new IdentityHashMap<>());
    private static volatile Set<BlockPos> hidden = Set.of();
    private static Set<BlockPos> controllers = Set.of();
    private static ClientLevel currentLevel;
    private ClientFormations() {}
    public static java.util.List<MachineEntity> machines(){return java.util.List.copyOf(LOADED);}
    public static boolean hidden(BlockPos pos) { return hidden.contains(pos); }
    public static boolean controller(BlockPos pos) { return controllers.contains(pos); }
    public static Rotation rotation(MachineEntity machine) {
        return switch (machine.getBlockState().getValue(MachineBlock.FACING)) {
            case EAST -> Rotation.CLOCKWISE_90; case SOUTH -> Rotation.CLOCKWISE_180;
            case WEST -> Rotation.COUNTERCLOCKWISE_90; default -> Rotation.NONE;
        };
    }
    public static void initialize() {
        ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((entity, level) -> { if (entity instanceof MachineEntity machine) LOADED.add(machine); });
        ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((entity, level) -> { if (entity instanceof MachineEntity machine) LOADED.remove(machine); });
        ClientTickEvents.END_CLIENT_TICK.register(client -> update(client.level));
    }
    private static void update(ClientLevel level) {
        if (currentLevel != level) {
            currentLevel = level; hidden = Set.of(); controllers = Set.of();
            LOADED.removeIf(entity -> entity.getLevel() != level);
        }
        if (level == null) { LOADED.clear(); return; }
        Set<BlockPos> next = new HashSet<>(), centers = new HashSet<>();
        LOADED.removeIf(MachineEntity::isRemoved);
        for (MachineEntity machine : LOADED) {
            var structure = machine.structure();
            if (machine.getLevel() != level || structure == null || !machine.assembled()) continue;
            BlockPos origin = machine.getBlockPos(); Rotation rotation = rotation(machine);
            // Wait for all constituent chunks before hiding their ordinary meshes.
            if (structure.parts().keySet().stream().anyMatch(p -> !level.hasChunkAt(origin.offset(p.rotate(rotation))))) continue;
            centers.add(origin); next.add(origin);
            for (var part : structure.parts().entrySet())
                if (!part.getValue().value().equals("minecraft:air")) next.add(origin.offset(part.getKey().rotate(rotation)).immutable());
        }
        if (!next.equals(hidden)) {
            Set<BlockPos> dirty = new HashSet<>(hidden); dirty.addAll(next);
            hidden = Set.copyOf(next); controllers = Set.copyOf(centers);
            // Equal BlockStates do not request a rebuild: visibility changed outside the state.
            Set<SectionPos> sections=new HashSet<>();
            for(BlockPos pos:dirty)sections.add(SectionPos.of(pos));
            for(SectionPos section:sections)level.setSectionDirtyWithNeighbors(section.x(),section.y(),section.z());
        } else controllers = Set.copyOf(centers);
    }
}
