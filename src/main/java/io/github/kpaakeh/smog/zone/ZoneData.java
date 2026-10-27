package io.github.kpaakeh.smog.zone;

import java.util.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import io.github.kpaakeh.smog.registry.ModBlocks;

/** One saved collection per dimension. Access on the server thread only. */
public final class ZoneData extends SavedData {
    public static final SavedDataType<ZoneData> TYPE = new SavedDataType<>(ModBlocks.id("zones"),
        ZoneData::new, Zone.CODEC.listOf().xmap(ZoneData::new, d -> List.copyOf(d.zones.values())), null);
    private final Map<String, Zone> zones = new LinkedHashMap<>();
    public ZoneData() {}
    private ZoneData(List<Zone> values) { values.forEach(z -> zones.put(z.id(), z)); }
    public static ZoneData get(ServerLevel level) { return level.getDataStorage().computeIfAbsent(TYPE); }
    public Collection<Zone> all() { return List.copyOf(zones.values()); }
    public void put(Zone zone) { zones.put(zone.id(), zone); setDirty(); }
    public boolean remove(String id) { boolean removed = zones.remove(id) != null; if (removed) setDirty(); return removed; }
    public void clear() { zones.clear(); setDirty(); }
    public void tick(long time) {
        boolean changed = false;
        for (var it = zones.entrySet().iterator(); it.hasNext();) {
            var entry = it.next(); Zone zone = entry.getValue();
            if (zone.expired(time)) { it.remove(); changed = true; }
            else if (zone.dynamic()) {
                Zone next = zone.advance();
                if (next == null) it.remove(); else entry.setValue(next);
                changed = true;
            }
        }
        if (changed) setDirty();
    }
}
