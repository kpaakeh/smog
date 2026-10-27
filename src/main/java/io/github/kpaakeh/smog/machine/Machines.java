package io.github.kpaakeh.smog.machine;

import java.util.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class Machines {
    public record Entry(MachineBlock block, BlockEntityType<MachineEntity> entityType, MachineDefinition definition) {}
    private static final Map<Identifier, Entry> ENTRIES = new LinkedHashMap<>();
    private Machines() {}
    public static MachineBlock register(String name, BlockBehaviour.Properties properties, MachineDefinition definition) {
        Identifier id = ModBlocks.id(name);
        MachineBlock block = ModBlocks.register(name, p -> new MachineBlock(p, id), properties, true);
        BlockEntityType<MachineEntity> type = new BlockEntityType<>((pos, state) -> new MachineEntity(id, pos, state), Set.of(block));
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type);
        ENTRIES.put(id, new Entry(block, type, definition));
        return block;
    }
    public static Entry get(Identifier id) { return Objects.requireNonNull(ENTRIES.get(id), "Unknown machine " + id); }
    public static Collection<Entry> all() { return List.copyOf(ENTRIES.values()); }
}
