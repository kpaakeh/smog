package io.github.kpaakeh.smog.copper;

import java.util.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

/** The controller's fuel stack and one-item work cells have independent transfer capacities. */
public final class SmelteryItemStorage {
    private SmelteryItemStorage(){}
    public static Storage<ItemVariant> sided(MachineEntity machine,Direction side){
        List<Storage<ItemVariant>> slots=new ArrayList<>();
        for(int slot:side==null?java.util.stream.IntStream.concat(java.util.stream.IntStream.range(0,Smeltery.MAX_WORKERS).map(Smeltery::slot),java.util.stream.IntStream.of(2)).toArray():side==Direction.UP?java.util.stream.IntStream.range(0,Smeltery.MAX_WORKERS).map(Smeltery::slot).toArray():side==Direction.DOWN?new int[]{}:new int[]{2}){
            slots.add(new SingleStackStorage(){
                @Override protected ItemStack getStack(){return machine.getItem(slot);}
                @Override protected void setStack(ItemStack stack){machine.setItem(slot,stack);}
                @Override protected int getCapacity(ItemVariant variant){return slot==2?Math.min(64,variant.toStack().getMaxStackSize()):Smeltery.worker(slot)<Smeltery.workers(machine)?1:0;}
                @Override protected boolean canInsert(ItemVariant variant){return !machine.isRemoved()&&machine.getLevel()!=null&&!machine.getLevel().isClientSide()&&machine.assembled()&&machine.canPlaceItem(slot,variant.toStack());}
                @Override protected boolean canExtract(ItemVariant variant){return false;}
                @Override protected void onFinalCommit(){machine.sync();}
            });
        }
        return new CombinedStorage<>(slots);
    }
}
