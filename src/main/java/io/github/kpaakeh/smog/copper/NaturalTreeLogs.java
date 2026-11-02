package io.github.kpaakeh.smog.copper;

import java.util.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import io.github.kpaakeh.smog.registry.ModBlocks;

/** Positive provenance, not a guess from nearby leaves. Stored separately in each chunk. */
public final class NaturalTreeLogs {
    public static final AttachmentType<Set<Long>> GENERATED=AttachmentRegistry.createPersistent(ModBlocks.id("natural_tree_logs"),Codec.LONG.listOf().xmap(Set::copyOf,ArrayList::new));
    private NaturalTreeLogs(){}
    public static void initialize(){}
    public static void generated(LevelAccessor level,BlockPos pos,BlockState state){if(state.is(BlockTags.LOGS))change(level,pos,true);}
    public static void placed(LevelAccessor level,BlockPos pos){change(level,pos,false);}
    public static boolean natural(LevelAccessor level,BlockPos pos){var chunk=level.getChunk(pos);synchronized(chunk){return ((AttachmentTarget)chunk).getAttachedOrElse(GENERATED,Set.of()).contains(pos.asLong());}}
    private static void change(LevelAccessor level,BlockPos pos,boolean value){
        var chunk=level.getChunk(pos);synchronized(chunk){var target=(AttachmentTarget)chunk;var old=target.getAttachedOrElse(GENERATED,Set.of());if(old.contains(pos.asLong())==value)return;var next=new HashSet<Long>(old);if(value)next.add(pos.asLong());else next.remove(pos.asLong());target.setAttached(GENERATED,Set.copyOf(next));}
    }
}
