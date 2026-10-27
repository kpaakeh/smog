package io.github.kpaakeh.techmod.zone;

import java.util.*;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/** Custom effects run on the server. Vanilla handles particle, sound and post-effect networking. */
public record ZoneType(List<Effect> effects, List<Identifier> postEffects) {
    public ZoneType { effects = List.copyOf(effects); postEffects = List.copyOf(postEffects); }
    public record Context(ServerLevel level, ServerPlayer player, Zone zone, long tick) {}
    @FunctionalInterface public interface Effect {
        void tick(Context context);
        default void enter(Context context) {}
        default void exit(Context context) {}
    }
    private static final Map<Identifier, ZoneType> TYPES = new LinkedHashMap<>();
    public static ZoneType register(Identifier id, ZoneType type) {
        if (TYPES.putIfAbsent(id, type) != null) throw new IllegalArgumentException("Duplicate zone type: " + id);
        return type;
    }
    public static ZoneType get(Identifier id) { return TYPES.get(id); }
    public static Set<Identifier> ids() { return Set.copyOf(TYPES.keySet()); }
}
