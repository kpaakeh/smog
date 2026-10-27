package io.github.kpaakeh.techmod.zone;

import java.util.List;
import io.github.kpaakeh.techmod.registry.ModBlocks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.Identifier;

public final class BuiltinZones {
    private BuiltinZones() {}
    public static void initialize() {
        ZoneType.register(ModBlocks.id("infection"), new ZoneType(List.of(
            ZoneEffects.status(MobEffects.POISON, 0), ZoneEffects.particles(ParticleTypes.SPORE_BLOSSOM_AIR, 4, 10),
            ZoneEffects.sound(SoundEvents.AMBIENT_CAVE.value(), 0.4f, 0.8f, 200)), List.of(Identifier.withDefaultNamespace("creeper"))));
        ZoneType.register(ModBlocks.id("warming"), new ZoneType(List.of(
            ZoneEffects.particles(ParticleTypes.SMOKE, 3, 10), c -> { if (c.tick() % 20 == 0) c.player().setRemainingFireTicks(40); }), List.of()));
        ZoneType.register(ModBlocks.id("freezing"), new ZoneType(List.of(
            ZoneEffects.status(MobEffects.SLOWNESS, 0), ZoneEffects.particles(ParticleTypes.SNOWFLAKE, 4, 10)), List.of()));
    }
}
