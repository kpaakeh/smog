package io.github.kpaakeh.smog.machine;

import io.github.kpaakeh.smog.energy.Voltage;

public record MachineSpec(Kind kind, Voltage voltage, int capacity, int workTicks, int cost) {
    public enum Kind {
        DECORATIVE, ELECTRIC_FURNACE, CRUSHER, PRESS, BLOCK_BREAKER, BLOCK_PLACER,
        FLUID_PUMP, FLUID_PLACER, SOLID_GENERATOR, GENERATOR_CONTROLLER, CONNECTOR, TRANSFORMER
    }
    public MachineSpec { if(capacity<1 || workTicks<1 || cost<0)throw new IllegalArgumentException("Invalid machine specification"); }
    public static MachineSpec decorative() { return new MachineSpec(Kind.DECORATIVE,Voltage.LV,32000,100,0); }
    public boolean source() { return kind==Kind.SOLID_GENERATOR || kind==Kind.GENERATOR_CONTROLLER; }
    public boolean relay() { return kind==Kind.CONNECTOR || kind==Kind.TRANSFORMER; }
    public boolean gui() { return kind!=Kind.CONNECTOR; }
}
