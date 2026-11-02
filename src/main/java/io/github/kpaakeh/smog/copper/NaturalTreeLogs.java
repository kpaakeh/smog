package io.github.kpaakeh.smog.copper;

import java.util.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.*;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import io.github.kpaakeh.smog.registry.ModBlocks;

/** Persisted positive generation and negative player-placement provenance. */
public final class NaturalTreeLogs {
    private static final Codec<Set<Long>> POSITIONS=Codec.LONG.listOf().xmap(Set::copyOf,ArrayList::new);
    public static final AttachmentType<Set<Long>> GENERATED=AttachmentRegistry.createPersistent(ModBlocks.id("natural_tree_logs"),POSITIONS);
    public static final AttachmentType<Set<Long>> PLAYER_PLACED=AttachmentRegistry.createPersistent(ModBlocks.id("player_placed_tree_logs"),POSITIONS);
    public static final int MAX_LOGS=1024,HORIZONTAL_RANGE=24,VERTICAL_RANGE=96;
    private NaturalTreeLogs(){}
    public static void initialize(){}
    public static void generated(LevelAccessor level,BlockPos pos,BlockState state){if(state.is(BlockTags.LOGS)){change(level,pos,GENERATED,true);change(level,pos,PLAYER_PLACED,false);}}
    public static void placed(LevelAccessor level,BlockPos pos){change(level,pos,GENERATED,false);change(level,pos,PLAYER_PLACED,level.getBlockState(pos).is(BlockTags.LOGS));}
    public static boolean natural(LevelAccessor level,BlockPos pos){return contains(level,pos,GENERATED)&&!playerPlaced(level,pos);}
    public static boolean playerPlaced(LevelAccessor level,BlockPos pos){return contains(level,pos,PLAYER_PLACED);}
    private static boolean contains(LevelAccessor level,BlockPos pos,AttachmentType<Set<Long>> type){var chunk=level.getChunk(pos);synchronized(chunk){return ((AttachmentTarget)chunk).getAttachedOrElse(type,Set.of()).contains(pos.asLong());}}
    private static void change(LevelAccessor level,BlockPos pos,AttachmentType<Set<Long>> type,boolean value){
        var chunk=level.getChunk(pos);synchronized(chunk){var target=(AttachmentTarget)chunk;var old=target.getAttachedOrElse(type,Set.of());if(old.contains(pos.asLong())==value)return;var next=new HashSet<Long>(old);if(value)next.add(pos.asLong());else next.remove(pos.asLong());target.setAttached(type,Set.copyOf(next));}
    }
    /** Resolve before the first log is removed. Older trees need roots and non-player foliage. */
    public static Set<BlockPos> tree(LevelAccessor level,BlockPos origin){
        var initial=level.getBlockState(origin);if(!initial.is(BlockTags.LOGS)||playerPlaced(level,origin))return Set.of();
        var pending=new ArrayDeque<BlockPos>();var logs=new LinkedHashSet<BlockPos>();pending.add(origin.immutable());logs.add(origin.immutable());
        while(!pending.isEmpty()){
            var current=pending.removeFirst();
            for(int x=-1;x<=1;x++)for(int y=-1;y<=1;y++)for(int z=-1;z<=1;z++){
                var next=current.offset(x,y,z);if(logs.contains(next)||Math.abs(next.getX()-origin.getX())>HORIZONTAL_RANGE||Math.abs(next.getZ()-origin.getZ())>HORIZONTAL_RANGE||Math.abs(next.getY()-origin.getY())>VERTICAL_RANGE||!level.hasChunkAt(next))continue;
                if(!level.getBlockState(next).is(initial.getBlock())||playerPlaced(level,next))continue;
                if(logs.size()>=MAX_LOGS)return Set.of();logs.add(next.immutable());pending.addLast(next.immutable());
            }
        }
        boolean recorded=natural(level,origin);
        if(!recorded){
            if(logs.size()<3)return Set.of();
            boolean root=false;var leaves=new HashSet<BlockPos>();int minimum=logs.stream().mapToInt(BlockPos::getY).min().orElse(origin.getY()),maximum=logs.stream().mapToInt(BlockPos::getY).max().orElse(origin.getY());
            if(maximum-minimum<2)return Set.of();
            for(var log:logs){
                var below=log.below();if(level.hasChunkAt(below)){var soil=level.getBlockState(below);if(soil.is(BlockTags.DIRT)||soil.is(Blocks.MUD)||soil.is(Blocks.MANGROVE_ROOTS)||soil.is(Blocks.MUDDY_MANGROVE_ROOTS))root=true;}
                if(log.getY()<minimum+2)continue;
                for(var leaf:BlockPos.betweenClosed(log.offset(-2,-1,-2),log.offset(2,2,2))){if(!level.hasChunkAt(leaf))continue;var state=level.getBlockState(leaf);if(state.is(BlockTags.LEAVES)&&state.hasProperty(LeavesBlock.PERSISTENT)&&state.hasProperty(LeavesBlock.DISTANCE)&&!state.getValue(LeavesBlock.PERSISTENT)&&state.getValue(LeavesBlock.DISTANCE)<7)leaves.add(leaf.immutable());}
            }
            if(!root||leaves.size()<4)return Set.of();
        }
        // Generated origins may touch unrecorded building logs. Only a legacy tree proof admits unmarked logs.
        if(recorded)logs.removeIf(pos->!natural(level,pos));
        return Collections.unmodifiableSet(logs);
    }
}
