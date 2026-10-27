package io.github.kpaakeh.techmod.zone;

import java.util.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.attachment.v1.*;
import io.github.kpaakeh.techmod.registry.ModBlocks;

public final class ZoneSystem {
    private record Active(Zone zone, ZoneType type, net.minecraft.server.level.ServerLevel level) {}
    private static final Map<UUID, Map<String, Active>> ACTIVE = new HashMap<>();
    private static final AttachmentType<List<Identifier>> OWNED_POST_EFFECTS = AttachmentRegistry.<List<Identifier>>create(
        ModBlocks.id("zone_post_effects"), builder -> builder.initializer(List::of).persistent(Identifier.CODEC.listOf()).copyOnDeath());
    private ZoneSystem() {}
    public static void initialize() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> ACTIVE.clear());
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (var level : server.getAllLevels()) ZoneData.get(level).tick(level.getGameTime());
            Set<UUID> online = new HashSet<>();
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                online.add(player.getUUID()); tickPlayer(player);
            }
            ACTIVE.keySet().retainAll(online);
        });
    }
    private static void tickPlayer(ServerPlayer player) {
        var level = (net.minecraft.server.level.ServerLevel) player.level();
        Map<String, Active> previous = ACTIVE.getOrDefault(player.getUUID(), Map.of());
        Map<String, Active> current = new LinkedHashMap<>();
        Set<Identifier> requested = new HashSet<>();
        for (Zone zone : ZoneData.get(level).all()) {
            ZoneType type = ZoneType.get(zone.type());
            if (type == null || !zone.contains(player.position()) || zone.intensity() == 0 || !player.isAlive() || player.isSpectator()) continue;
            String key = level.dimension().identifier() + "/" + zone.id() + "/" + zone.type();
            current.put(key, new Active(zone, type, level));
            var context = new ZoneType.Context(level, player, zone, level.getGameTime());
            for (var effect : type.effects()) {
                if (!previous.containsKey(key)) effect.enter(context);
                effect.tick(context);
            }
            requested.addAll(type.postEffects());
        }
        previous.forEach((key, active) -> {
            if (!current.containsKey(key)) for (var effect : active.type.effects())
                effect.exit(new ZoneType.Context(active.level, player, active.zone, active.level.getGameTime()));
        });
        AttachmentTarget target = (AttachmentTarget) player;
        List<Identifier> savedOwned = target.getAttachedOrCreate(OWNED_POST_EFFECTS);
        Set<Identifier> owned = new HashSet<>(savedOwned);
        for (var it = owned.iterator(); it.hasNext();) {
            Identifier id = it.next();
            if (!requested.contains(id)) { player.removePostEffect(id); it.remove(); }
        }
        for (Identifier id : requested) if (player.addPostEffect(id)) owned.add(id);
        if (!owned.equals(new HashSet<>(savedOwned))) target.setAttached(OWNED_POST_EFFECTS, List.copyOf(owned));
        ACTIVE.put(player.getUUID(), current);
    }
}
