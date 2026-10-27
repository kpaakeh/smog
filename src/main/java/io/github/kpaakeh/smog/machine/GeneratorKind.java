package io.github.kpaakeh.smog.machine;

import io.github.kpaakeh.smog.energy.Voltage;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

public enum GeneratorKind implements StringRepresentable {
    NONE(Voltage.LV),SOLAR(Voltage.LV),LIGHTNING(Voltage.HV),THERMAL(Voltage.LV),FLOW(Voltage.LV),WIND(Voltage.MV),STEAM(Voltage.MV),GEOTHERMAL(Voltage.MV);
    public final Voltage voltage;
    GeneratorKind(Voltage voltage) { this.voltage=voltage; }
    @Override public String getSerializedName() { return name().toLowerCase(java.util.Locale.ROOT); }
    public Identifier structureId() { return ModBlocks.id("generator_"+getSerializedName()); }
}
