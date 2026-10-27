package io.github.kpaakeh.smog.multiblock;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;

/** Positions are relative to the controller; NORTH is the reference orientation. */
public record MultiblockDefinition(Identifier id, Map<BlockPos, BlockIngredient> parts) {
    public record Bounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        public int width() { return maxX - minX + 1; }
        public int height() { return maxY - minY + 1; }
        public int depth() { return maxZ - minZ + 1; }
    }
    public Bounds bounds() {
        int x0=0,y0=0,z0=0,x1=0,y1=0,z1=0;
        for (BlockPos p : parts.keySet()) {
            x0=Math.min(x0,p.getX());y0=Math.min(y0,p.getY());z0=Math.min(z0,p.getZ());
            x1=Math.max(x1,p.getX());y1=Math.max(y1,p.getY());z1=Math.max(z1,p.getZ());
        }
        return new Bounds(x0,y0,z0,x1,y1,z1);
    }
    public MultiblockDefinition {
        if (parts.isEmpty() || parts.size() > 4096) throw new IllegalArgumentException("Multiblock must contain 1..4096 parts");
        Map<BlockPos, BlockIngredient> copy = new LinkedHashMap<>();
        parts.forEach((p, ingredient) -> {
            if (p.equals(BlockPos.ZERO) || Math.abs((long)p.getX()) > 32 || Math.abs((long)p.getY()) > 32 || Math.abs((long)p.getZ()) > 32)
                throw new IllegalArgumentException("Part must be within 32 blocks and outside controller position");
            copy.put(p.immutable(), ingredient);
        });
        parts = Collections.unmodifiableMap(copy);
    }
    public enum Result { COMPLETE, INCOMPLETE, UNLOADED }
    public Result check(Level level, BlockPos origin, Rotation rotation) {
        for (BlockPos offset : parts.keySet()) if (!level.hasChunkAt(origin.offset(offset.rotate(rotation)))) return Result.UNLOADED;
        for (var part : parts.entrySet()) {
            if (!part.getValue().matches(level.getBlockState(origin.offset(part.getKey().rotate(rotation))))) return Result.INCOMPLETE;
        }
        return Result.COMPLETE;
    }
}
