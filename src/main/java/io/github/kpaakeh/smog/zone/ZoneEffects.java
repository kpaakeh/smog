package io.github.kpaakeh.smog.zone;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public final class ZoneEffects {
    private ZoneEffects() {}
    public static ZoneType.Effect status(Holder<MobEffect> effect, int amplifier) {
        if (amplifier < 0) throw new IllegalArgumentException("Negative amplifier");
        return c -> { if (c.tick() % 20 == 0) c.player().addEffect(new MobEffectInstance(effect, 40, amplifier, true, false)); };
    }
    public static ZoneType.Effect particles(ParticleOptions particle, int count, int interval) {
        if (interval < 1 || count < 0 || count > 256) throw new IllegalArgumentException("Invalid particle budget");
        return c -> { if (c.tick() % interval == 0) c.level().sendParticles(c.player(), particle, false, false,
            c.player().getX(), c.player().getY() + 1, c.player().getZ(), count, 2, 1, 2, 0.01); };
    }
    public static ZoneType.Effect sound(SoundEvent sound, float volume, float pitch, int interval) {
        if (interval < 1) throw new IllegalArgumentException("Invalid sound interval");
        return c -> { if (c.tick() % interval == 0) c.player().connection.send(
            new net.minecraft.network.protocol.game.ClientboundSoundPacket(Holder.direct(sound), SoundSource.AMBIENT,
                c.player().getX(), c.player().getY(), c.player().getZ(), volume, pitch, c.level().getRandom().nextLong())); };
    }
}
