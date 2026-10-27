package io.github.kpaakeh.techmod.multiblock;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;

/** Positions are relative to the controller; NORTH is the reference orientation. */
public record MultiblockDefinition(Identifier id, Map<BlockPos, BlockIngredient> parts) {
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
