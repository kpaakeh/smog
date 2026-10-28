package io.github.kpaakeh.smog.multiblock.complex;

import java.util.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.*;

/** Persistent exclusive ownership survives chunk unloads and server restarts. Server thread only. */
public final class ComplexFormationData extends SavedData {
    public record Claim(BlockPos controller,List<BlockPos> parts) {
        public static final Codec<Claim> CODEC=RecordCodecBuilder.create(i->i.group(BlockPos.CODEC.fieldOf("controller").forGetter(Claim::controller),BlockPos.CODEC.listOf().fieldOf("parts").forGetter(Claim::parts)).apply(i,Claim::new));
        public Claim {controller=controller.immutable();parts=parts.stream().map(BlockPos::immutable).toList();}
    }
    public static final SavedDataType<ComplexFormationData> TYPE=new SavedDataType<>(ModBlocks.id("complex_formations"),ComplexFormationData::new,Claim.CODEC.listOf().xmap(ComplexFormationData::new,d->List.copyOf(d.claims.values())),null);
    private final Map<BlockPos,Claim> claims=new LinkedHashMap<>();
    private final Map<BlockPos,BlockPos> owners=new HashMap<>();
    public ComplexFormationData(){}
    private ComplexFormationData(List<Claim> values){for(var claim:values){claims.put(claim.controller,claim);for(var part:claim.parts)owners.put(part,claim.controller);}}
    public static ComplexFormationData get(ServerLevel level){return level.getDataStorage().computeIfAbsent(TYPE);}
    public BlockPos owner(BlockPos part){return owners.get(part);}
    public boolean claim(BlockPos controller,Collection<BlockPos> parts){
        for(var part:parts){var owner=owners.get(part);if(owner!=null&&!owner.equals(controller))return false;}
        var prior=claims.get(controller);var next=new Claim(controller,List.copyOf(parts));if(next.equals(prior))return true;
        release(controller);claims.put(controller.immutable(),next);for(var part:next.parts)owners.put(part,controller.immutable());setDirty();return true;
    }
    public void release(BlockPos controller){var claim=claims.remove(controller);if(claim!=null){for(var part:claim.parts)owners.remove(part,controller);setDirty();}}
}
