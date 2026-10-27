package io.github.kpaakeh.techmod.machine;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

/** Register callbacks during mod initialization, before worlds load. */
public record MachineDefinition(Identifier multiblock, int interval, Tick action) {
    @FunctionalInterface public interface Tick { void tick(ServerLevel level, MachineEntity machine); }
    public MachineDefinition { if (interval < 1) throw new IllegalArgumentException("Tick interval must be positive"); }
    public static MachineDefinition simple() { return new MachineDefinition(null, 20, (level, machine) -> {}); }
}
