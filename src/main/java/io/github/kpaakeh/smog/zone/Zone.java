package io.github.kpaakeh.smog.zone;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.UUID;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

/** Immutable, persistent geometry. Velocities are blocks per server tick. */
public record Zone(String id, Identifier type, Shape shape, Vec3 center, Vec3 halfSize,
                   Vec3 velocity, Vec3 growth, long expiresAt, float intensity) {
    public enum Shape { ELLIPSOID, BOX }
    public static final Codec<Zone> CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.STRING.fieldOf("id").forGetter(Zone::id),
        Identifier.CODEC.fieldOf("type").forGetter(Zone::type),
        Codec.STRING.xmap(Shape::valueOf, Shape::name).fieldOf("shape").forGetter(Zone::shape),
        Vec3.CODEC.fieldOf("center").forGetter(Zone::center),
        Vec3.CODEC.fieldOf("half_size").forGetter(Zone::halfSize),
        Vec3.CODEC.fieldOf("velocity").forGetter(Zone::velocity),
        Vec3.CODEC.fieldOf("growth").forGetter(Zone::growth),
        Codec.LONG.fieldOf("expires_at").forGetter(Zone::expiresAt),
        Codec.FLOAT.fieldOf("intensity").forGetter(Zone::intensity)
    ).apply(i, Zone::new));

    public Zone {
        if (id == null || id.isBlank() || type == null || shape == null) throw new IllegalArgumentException("Invalid zone identity");
        if (!finite(center) || !finite(halfSize) || !finite(velocity) || !finite(growth)
            || halfSize.x <= 0 || halfSize.y <= 0 || halfSize.z <= 0 || !Float.isFinite(intensity) || intensity < 0)
            throw new IllegalArgumentException("Zone geometry must be finite, with positive size and nonnegative intensity");
    }
    private static boolean finite(Vec3 v) { return v != null && Double.isFinite(v.x) && Double.isFinite(v.y) && Double.isFinite(v.z); }
    public static Zone sphere(Identifier type, Vec3 center, double radius) {
        return new Zone(UUID.randomUUID().toString(), type, Shape.ELLIPSOID, center,
            new Vec3(radius, radius, radius), Vec3.ZERO, Vec3.ZERO, -1, 1);
    }
    public static Zone box(Identifier type, Vec3 center, Vec3 halfSize) {
        return new Zone(UUID.randomUUID().toString(), type, Shape.BOX, center, halfSize, Vec3.ZERO, Vec3.ZERO, -1, 1);
    }
    public Zone moving(Vec3 blocksPerTick) { return new Zone(id, type, shape, center, halfSize, blocksPerTick, growth, expiresAt, intensity); }
    public Zone growing(Vec3 blocksPerTick) { return new Zone(id, type, shape, center, halfSize, velocity, blocksPerTick, expiresAt, intensity); }
    public Zone until(long gameTime) { return new Zone(id, type, shape, center, halfSize, velocity, growth, gameTime, intensity); }
    public Zone at(Vec3 position) { return new Zone(id, type, shape, position, halfSize, velocity, growth, expiresAt, intensity); }
    public Zone scaled(float strength) { return new Zone(id, type, shape, center, halfSize, velocity, growth, expiresAt, strength); }
    public boolean contains(Vec3 point) {
        Vec3 d = point.subtract(center);
        double x = Math.abs(d.x / halfSize.x), y = Math.abs(d.y / halfSize.y), z = Math.abs(d.z / halfSize.z);
        return shape == Shape.BOX ? x <= 1 && y <= 1 && z <= 1 : x*x + y*y + z*z <= 1;
    }
    public boolean expired(long time) { return expiresAt >= 0 && time >= expiresAt; }
    public boolean dynamic() { return !velocity.equals(Vec3.ZERO) || !growth.equals(Vec3.ZERO); }
    public Zone advance() {
        Vec3 size = halfSize.add(growth);
        if (size.x <= 0 || size.y <= 0 || size.z <= 0) return null;
        return new Zone(id, type, shape, center.add(velocity), size, velocity, growth, expiresAt, intensity);
    }
}
