package io.github.kpaakeh.smog.machine;

import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;

public enum GeneratorKind implements StringRepresentable {
    NONE(32),SOLAR(32),LIGHTNING(512),THERMAL(32),FLOW(32),WIND(128),STEAM(128),GEOTHERMAL(128);
    public final int transferRate;
    GeneratorKind(int transferRate) { this.transferRate=transferRate; }
    @Override public String getSerializedName() { return name().toLowerCase(java.util.Locale.ROOT); }
    public Identifier structureId() { return ModBlocks.id("generator_"+getSerializedName()); }
}
