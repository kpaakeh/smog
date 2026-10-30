package io.github.kpaakeh.smog.copper;

import io.github.kpaakeh.smog.machine.MachineEntity;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;

/** A real, persistent hoist: cargo leaves its source at pickup, enters its destination at delivery. */
public final class LoadingCrane {
    public static final int SOURCE=1,EMPTY=2,DESTINATION=3,BLOCKED=4,UNLOADED=5;
    public static boolean handles(MachineEntity m){return CopperWorkshops.workshop(m,"loading_crane");}
    public static BlockPos bay(MachineEntity m,boolean second){return m.getBlockPos().offset(new BlockPos(0,-4,second?3:1).rotate(m.rotation()));}
    public static int duration(MachineEntity m){return m.copper.workKey.equals("crane:powered")?80:160;}
    public static Storage<ItemVariant> cargo(ServerLevel level,BlockPos pos){
        if(!level.hasChunkAt(pos))return null;
        var storage=ItemStorage.SIDED.find(level,pos,Direction.UP);if(storage!=null)return storage;
        var carts=level.getEntitiesOfClass(HandcartEntity.class,new AABB(pos).inflate(.6),cart->!cart.pulling()&&cart.blockPosition().equals(pos));return carts.size()==1?ContainerStorage.of(carts.getFirst(),null):null;
    }
    public static BlockPos obstruction(MachineEntity m){
        var level=m.getLevel();for(int z=1;z<=3;z++)for(int y=-3;y<=-1;y++){
            var pos=m.getBlockPos().offset(new BlockPos(0,y,z).rotate(m.rotation()));
            if(!level.hasChunkAt(pos)||!level.getBlockState(pos).getCollisionShape(level,pos).isEmpty())return pos;
        }return null;
    }
    public static String coordinates(BlockPos pos){return pos.getX()+", "+pos.getY()+", "+pos.getZ();}
    public static Component status(MachineEntity m){
        boolean reverse=m.copper.selection%2==1;var source=bay(m,reverse);var target=bay(m,!reverse);
        return switch(m.copper.status){case SOURCE->Component.translatable("message.smog.crane_source",coordinates(source));case EMPTY->Component.translatable("message.smog.crane_empty",coordinates(source));case DESTINATION->Component.translatable(m.getItem(1).isEmpty()?"message.smog.crane_destination":"message.smog.crane_wait",coordinates(target));case BLOCKED->Component.translatable("message.smog.crane_blocked",coordinates(obstruction(m)==null?target:obstruction(m)));case UNLOADED->Component.translatable("message.smog.crane_unloaded");default->Component.translatable("message.smog.crane_started",coordinates(source),coordinates(target));};
    }
    private static int readiness(ServerLevel level,MachineEntity m,boolean pickup){
        boolean reverse=m.copper.selection%2==1;var source=bay(m,reverse);var target=bay(m,!reverse);
        if(!level.hasChunkAt(source)||!level.hasChunkAt(target))return UNLOADED;
        if(obstruction(m)!=null)return BLOCKED;
        var to=cargo(level,target);if(to==null)return DESTINATION;
        if(pickup){var from=cargo(level,source);if(from==null)return SOURCE;if(from==to)return DESTINATION;
            for(var view:from)if(!view.isResourceBlank()&&view.getAmount()>0)try(var tx=Transaction.openOuter()){if(to.insert(view.getResource(),Math.min(64,view.getAmount()),tx)>0)return 0;}
            for(var view:from)if(!view.isResourceBlank()&&view.getAmount()>0)return DESTINATION;return EMPTY;
        }return 0;
    }
    public static Component queue(MachineEntity m){
        if(m.copper.queued!=0)return m.copper.status==0?Component.translatable("message.smog.crane_busy"):status(m);
        if(!(m.getLevel() instanceof ServerLevel level))return Component.empty();
        int problem=readiness(level,m,m.getItem(1).isEmpty());m.copper.status=problem;if(problem!=0){m.sync();return status(m);}
        m.copper.workKey=BronzeDrive.connected(m)?"crane:powered":"crane:manual";
        m.copper.queued=1;m.copper.work=m.getItem(1).isEmpty()?0:duration(m)*3/4;m.sync();return status(m);
    }
    private static boolean pickup(ServerLevel level,MachineEntity m){
        boolean reverse=m.copper.selection%2==1;var from=cargo(level,bay(m,reverse));var to=cargo(level,bay(m,!reverse));if(from==null||to==null)return false;
        for(var view:from){if(view.isResourceBlank()||view.getAmount()==0)continue;var resource=view.getResource();long count;
            try(var probe=Transaction.openOuter()){count=to.insert(resource,Math.min(Math.min(64,resource.toStack().getMaxStackSize()),view.getAmount()),probe);}
            if(count==0)continue;
            try(var tx=Transaction.openOuter()){long taken=from.extract(resource,count,tx);if(taken==0)continue;tx.commit();m.setItem(1,resource.toStack((int)taken));m.setChanged();return true;}
        }return false;
    }
    private static boolean deliver(ServerLevel level,MachineEntity m){
        var held=m.getItem(1);if(held.isEmpty())return true;var to=cargo(level,bay(m,m.copper.selection%2==0));if(to==null)return false;
        try(var tx=Transaction.openOuter()){long inserted=to.insert(ItemVariant.of(held),held.getCount(),tx);if(inserted<=0)return false;tx.commit();held.shrink((int)inserted);m.setChanged();return held.isEmpty();}
    }
    public static void tick(ServerLevel level,MachineEntity m){
        if(!m.assembled())return;
        boolean signal=level.hasNeighborSignal(m.getBlockPos());
        if(signal!=m.copper.craneRedstoneHigh){m.copper.craneRedstoneHigh=signal;m.setChanged();if(signal)queue(m);}
        if(m.copper.queued==0){if(BronzeDrive.connected(m)&&level.getGameTime()%20==0)queue(m);return;}
        int duration=duration(m),pickup=duration*3/20,delivery=duration*3/4;
        int problem=readiness(level,m,m.copper.work<=pickup&&m.getItem(1).isEmpty());
        if(problem!=0){if(m.copper.status!=problem){m.copper.status=problem;m.sync();}return;}
        if(m.copper.work==pickup&&m.getItem(1).isEmpty()&&!pickup(level,m)){m.copper.status=EMPTY;m.sync();return;}
        if(m.copper.work==delivery&&!deliver(level,m)){m.copper.status=DESTINATION;m.sync();return;}
        m.copper.status=0;
        if(m.copper.work%20==0)level.playSound(null,m.getBlockPos(),net.minecraft.sounds.SoundEvents.WOOD_STEP,net.minecraft.sounds.SoundSource.BLOCKS,.35f,.75f);
        if(++m.copper.work>=duration){m.copper.work=0;m.copper.queued=0;}
        m.sync();
    }
}
