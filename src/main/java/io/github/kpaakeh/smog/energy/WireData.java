package io.github.kpaakeh.smog.energy;

import java.util.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.*;

public final class WireData extends SavedData {
    public static final SavedDataType<WireData> TYPE=new SavedDataType<>(ModBlocks.id("wires"),WireData::new,
        WireLink.CODEC.listOf().xmap(WireData::new,d->List.copyOf(d.links)),null);
    private final Set<WireLink> links=new LinkedHashSet<>();
    private long revision;
    public WireData(){}
    private WireData(List<WireLink> list){links.addAll(list);}
    public static WireData get(ServerLevel level){return level.getDataStorage().computeIfAbsent(TYPE);}
    public List<WireLink> all(){return List.copyOf(links);}
    public long revision(){return revision;}
    public boolean add(WireLink link){if(links.size()>=512 || !links.add(link))return false;revision++;setDirty();return true;}
    public boolean remove(WireLink link){if(!links.remove(link))return false;revision++;setDirty();return true;}
    public List<WireLink> disconnect(BlockPos pos){var removed=links.stream().filter(l->l.touches(pos)).toList();removed.forEach(this::remove);return removed;}
}
