package io.github.kpaakeh.smog.copper;

import io.github.kpaakeh.smog.machine.MachineEntity;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.phys.AABB;

/** A fixed gantry moves a finite batch between its two marked loading bays. */
public final class LoadingCrane {
    public static boolean handles(MachineEntity m){return CopperWorkshops.workshop(m,"loading_crane");}
    public static BlockPos bay(MachineEntity m,boolean second){return m.getBlockPos().offset(new BlockPos(0,-2,second?2:0).rotate(m.rotation()));}
    public static Storage<ItemVariant> cargo(ServerLevel level,BlockPos pos){
        if(!level.hasChunkAt(pos))return null;
        var storage=ItemStorage.SIDED.find(level,pos,Direction.UP);if(storage!=null)return storage;
        var carts=level.getEntitiesOfClass(HandcartEntity.class,new AABB(pos).inflate(.7),cart->cart.blockPosition().equals(pos));return carts.size()==1?ContainerStorage.of(carts.getFirst(),null):null;
    }
    public static long transfer(ServerLevel level,MachineEntity m){
        boolean reverse=m.copper.selection%2==1;var from=cargo(level,bay(m,reverse));var to=cargo(level,bay(m,!reverse));
        if(from==null||to==null||from==to)return 0;
        try(var transaction=Transaction.openOuter()){long moved=StorageUtil.move(from,to,resource->true,64,transaction);transaction.commit();return moved;}
    }
    public static void queue(MachineEntity m){if(m.copper.queued==0){m.copper.queued=1;m.copper.work=0;}m.sync();}
    public static void tick(ServerLevel level,MachineEntity m){
        if(!m.assembled())return;boolean powered=BronzeDrive.connected(m);if(powered&&m.copper.queued==0)queue(m);
        if(m.copper.queued==0)return;
        if(++m.copper.work<(powered?40:80)){m.setChanged();return;}
        long moved=transfer(level,m);m.copper.work=0;m.copper.queued=0;m.copper.status=moved>0?0:5;m.sync();
    }
}
