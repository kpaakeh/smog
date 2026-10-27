package io.github.kpaakeh.smog.machine;

import io.github.kpaakeh.smog.energy.ConductorMaterial;

public record MachineSpec(Kind kind, int transferRate, int capacity, int workTicks, int cost,ConductorMaterial conductor) {
    public enum Kind {
        DECORATIVE, ELECTRIC_FURNACE, CRUSHER, PRESS, BLOCK_BREAKER, BLOCK_PLACER,
        FLUID_PUMP, FLUID_PLACER, SOLID_GENERATOR, GENERATOR_CONTROLLER, CONNECTOR, TRANSFORMER
    }
    public MachineSpec { if(transferRate<1 || capacity<1 || workTicks<1 || cost<0)throw new IllegalArgumentException("Invalid machine specification"); }
    public static MachineSpec decorative() { return new MachineSpec(Kind.DECORATIVE,32,32000,100,0,null); }
    public boolean source() { return kind==Kind.SOLID_GENERATOR || kind==Kind.GENERATOR_CONTROLLER; }
    public boolean relay() { return kind==Kind.CONNECTOR || kind==Kind.TRANSFORMER; }
    public boolean gui() { return kind!=Kind.CONNECTOR; }
}
