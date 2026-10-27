package io.github.kpaakeh.smog.energy;

import java.util.*;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/** FE network with Team Reborn Energy interoperability. Per-edge and per-source limits prevent multiplication through loops. */
public final class EnergyNetwork {
    private record Edge(BlockPos from,BlockPos to){}
    private record Sent(String dimension,long revision,long tick){}
    private static final Map<ServerLevel,Set<MachineEntity>> LOADED=new IdentityHashMap<>();
    private static final Map<UUID,Sent> SENT=new HashMap<>();
    private EnergyNetwork(){}
    public static void track(MachineEntity machine){if(machine.getLevel() instanceof ServerLevel level)LOADED.computeIfAbsent(level,l->new HashSet<>()).add(machine);}
    public static void initialize(){
        PayloadTypeRegistry.clientboundPlay().register(WireSnapshot.TYPE,WireSnapshot.CODEC);
        ServerLifecycleEvents.SERVER_STOPPED.register(server->{LOADED.clear();SENT.clear();});
        ServerTickEvents.END_SERVER_TICK.register(server->{for(var level:server.getAllLevels())tick(level);SENT.keySet().retainAll(server.getPlayerList().getPlayers().stream().map(ServerPlayer::getUUID).toList());});
        ServerPlayConnectionEvents.JOIN.register((handler,sender,server)->SENT.remove(handler.player.getUUID()));
    }
    public static boolean connector(ServerLevel level,BlockPos pos,ConductorMaterial material){
        return level.hasChunkAt(pos) && level.getBlockEntity(pos) instanceof MachineEntity m && m.spec().kind()==MachineSpec.Kind.CONNECTOR && m.spec().conductor()==material;
    }
    public static void tick(ServerLevel level){
        var loaded=LOADED.computeIfAbsent(level,l->new HashSet<>());loaded.removeIf(m->m.isRemoved()||m.getLevel()!=level||!level.hasChunkAt(m.getBlockPos())||level.getBlockEntity(m.getBlockPos())!=m);
        Map<BlockPos,MachineEntity> nodes=new HashMap<>();loaded.forEach(m->{if(m.enabled()&&m.assembled())nodes.put(m.getBlockPos(),m);});
        WireData wires=WireData.get(level);
        for(var wire:wires.all())if(level.hasChunkAt(wire.a())&&level.hasChunkAt(wire.b())&&(!connector(level,wire.a(),wire.material())||!connector(level,wire.b(),wire.material()))) {
            wires.remove(wire);refund(level,wire.a(),wire.material());
        }
        Map<Edge,Integer> capacity=new HashMap<>();Map<BlockPos,List<BlockPos>> adjacent=new HashMap<>();
        for(var node:nodes.values())for(var direction:Direction.values()) {
            var other=nodes.get(node.getBlockPos().relative(direction));
            if(other!=null)edge(adjacent,capacity,node.getBlockPos(),other.getBlockPos(),Math.min(node.outputRate(),other.inputRate()));
        }
        for(var wire:wires.all())if(nodes.containsKey(wire.a())&&nodes.containsKey(wire.b())) {
            edge(adjacent,capacity,wire.a(),wire.b(),wire.material().transfer);edge(adjacent,capacity,wire.b(),wire.a(),wire.material().transfer);
        }
        var sources=nodes.values().stream().filter(m->(m.spec().source()||m.spec().relay())&&m.energy()>0).sorted(Comparator.comparing(MachineEntity::getBlockPos)).toList();
        for(var source:sources) {
            int budget=Math.min(source.energy(),source.outputRate());
            var targets=nodes.values().stream().filter(m->!m.spec().relay()&&!m.spec().source()&&m.spec().kind()!=MachineSpec.Kind.DECORATIVE&&m.energy()<m.capacity()).sorted(Comparator.comparing(MachineEntity::getBlockPos)).toList();
            for(var target:targets) {
                if(budget==0)break;
                List<Edge> path=path(source.getBlockPos(),target.getBlockPos(),nodes,adjacent,capacity);
                if(path.isEmpty())continue;
                int amount=Math.min(budget,target.capacity()-target.energy());for(var e:path)amount=Math.min(amount,capacity.get(e));
                if(amount<=0)continue;
                amount=transfer(source,target.energyStorage(),amount);if(amount<=0)continue;
                budget-=amount;
                for(var e:path)capacity.put(e,capacity.get(e)-amount);
            }
            // Push remaining energy out through any reachable connector/transformer or the source itself.
            for(var terminal:nodes.values().stream().filter(m->m==source||m.spec().relay()).sorted(Comparator.comparing(MachineEntity::getBlockPos)).toList()) {
                if(budget<=0)break;
                var route=path(source.getBlockPos(),terminal.getBlockPos(),nodes,adjacent,capacity);
                if(terminal!=source&&route.isEmpty())continue;
                for(var direction:Direction.values()) {
                    var neighbor=terminal.getBlockPos().relative(direction);
                    if(!level.hasChunkAt(neighbor)||level.getBlockEntity(neighbor) instanceof MachineEntity)continue;
                    var storage=team.reborn.energy.api.EnergyStorage.SIDED.find(level,neighbor,direction.getOpposite());
                    if(storage==null||!storage.supportsInsertion())continue;
                    int amount=Math.min(budget,terminal.outputRate());
                    for(var edge:route)amount=Math.min(amount,capacity.get(edge));
                    amount=transfer(source,storage,amount);budget-=amount;
                    for(var edge:route)capacity.put(edge,capacity.get(edge)-amount);
                }
            }
        }
        broadcast(level,wires);
    }
    /** Both buffers and their rate counters commit together, or roll back together. */
    private static int transfer(MachineEntity source,team.reborn.energy.api.EnergyStorage target,int maximum) {
        if(maximum<=0)return 0;
        try(var transaction=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
            int offered=(int)source.energyStorage().extract(maximum,transaction);
            if(offered<=0)return 0;
            int accepted=(int)target.insert(offered,transaction);
            if(accepted==offered){transaction.commit();return offered;}
            // Retry a partial insertion after this transaction has rolled both buffers back.
        }
        // Probe the target without consuming source energy; a partial target must never lose energy.
        long accepted;
        try(var simulation=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {accepted=target.insert(maximum,simulation);}
        if(accepted<=0)return 0;
        try(var transaction=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()) {
            int offered=(int)source.energyStorage().extract(Math.min(maximum,accepted),transaction);
            if(offered>0&&target.insert(offered,transaction)==offered){transaction.commit();return offered;}
        }
        return 0;
    }
    private static void edge(Map<BlockPos,List<BlockPos>> graph,Map<Edge,Integer> capacity,BlockPos a,BlockPos b,int rate){
        graph.computeIfAbsent(a,p->new ArrayList<>()).add(b);capacity.put(new Edge(a,b),rate);
    }
    private static List<Edge> path(BlockPos start,BlockPos end,Map<BlockPos,MachineEntity> nodes,Map<BlockPos,List<BlockPos>> graph,Map<Edge,Integer> capacity){
        ArrayDeque<BlockPos> queue=new ArrayDeque<>();Map<BlockPos,BlockPos> parents=new HashMap<>();queue.add(start);parents.put(start,start);
        while(!queue.isEmpty()){
            var current=queue.remove();if(current.equals(end))break;
            if(!current.equals(start)&&!nodes.get(current).spec().relay())continue;
            for(var next:graph.getOrDefault(current,List.of()))if(!parents.containsKey(next)&&capacity.getOrDefault(new Edge(current,next),0)>0){parents.put(next,current);queue.add(next);}
        }
        if(!parents.containsKey(end))return List.of();List<Edge> result=new ArrayList<>();
        for(var at=end;!at.equals(start);at=parents.get(at))result.add(new Edge(parents.get(at),at));return result;
    }
    public static void refund(ServerLevel level,BlockPos pos,ConductorMaterial material){
        Block.popResource(level,pos,new ItemStack(BuiltInRegistries.ITEM.getValue(ModBlocks.id(material.coilId()))));
    }
    private static void broadcast(ServerLevel level,WireData wires){
        for(var player:level.players()){
            String dimension=level.dimension().identifier().toString();Sent previous=SENT.get(player.getUUID());
            if(previous!=null&&previous.dimension.equals(dimension)&&previous.revision==wires.revision()&&level.getGameTime()-previous.tick<40)continue;
            JsonObject object=new JsonObject();object.addProperty("dimension",dimension);object.add("links",WireLink.CODEC.listOf().encodeStart(JsonOps.INSTANCE,wires.all()).getOrThrow());
            if(ServerPlayNetworking.canSend(player,WireSnapshot.TYPE))ServerPlayNetworking.send(player,new WireSnapshot(object.toString()));
            SENT.put(player.getUUID(),new Sent(dimension,wires.revision(),level.getGameTime()));
        }
    }
}
