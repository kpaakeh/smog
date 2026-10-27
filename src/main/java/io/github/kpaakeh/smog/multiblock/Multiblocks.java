package io.github.kpaakeh.smog.multiblock;

import java.util.*;
import net.minecraft.resources.Identifier;

public final class Multiblocks {
    private static final Map<Identifier, MultiblockDefinition> DEFINITIONS = new LinkedHashMap<>();
    private Multiblocks() {}
    public static MultiblockDefinition register(MultiblockDefinition definition) {
        if (DEFINITIONS.putIfAbsent(definition.id(), definition) != null) throw new IllegalArgumentException("Duplicate multiblock " + definition.id());
        return definition;
    }
    public static MultiblockDefinition get(Identifier id) { return DEFINITIONS.get(id); }
    public static Collection<MultiblockDefinition> all() { return List.copyOf(DEFINITIONS.values()); }
}
