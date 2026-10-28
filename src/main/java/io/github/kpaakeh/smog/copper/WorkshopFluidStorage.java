package io.github.kpaakeh.smog.copper;

import java.util.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluids;

/** Exact finite water supply or oil receiver. Inventory and pipes share the same persisted tank. */
public final class WorkshopFluidStorage extends SnapshotParticipant<Integer> implements Storage<FluidVariant> {
    public static final int BUCKET=81000,CAPACITY=8*BUCKET;
    private final MachineEntity machine;
    public WorkshopFluidStorage(MachineEntity machine){this.machine=machine;}
    public boolean washing(){return !CopperWorkshops.workshop(machine,"lever_press");}
    public FluidVariant resource(){return FluidVariant.of(washing()?Fluids.WATER:BuiltInRegistries.FLUID.getValue(ModBlocks.id("plant_oil")));}
    private boolean ready(){return machine.getLevel()!=null&&!machine.getLevel().isClientSide()&&!machine.isRemoved()&&machine.assembled();}
    @Override public long insert(FluidVariant resource,long amount,TransactionContext tx){StoragePreconditions.notNegative(amount);if(!ready()||!washing()||!resource.equals(resource()))return 0;int n=(int)Math.min(amount,CAPACITY-machine.copper.processFluid);if(n<=0)return 0;updateSnapshots(tx);machine.copper.processFluid+=n;return n;}
    @Override public long extract(FluidVariant resource,long amount,TransactionContext tx){StoragePreconditions.notNegative(amount);if(!ready()||!resource.equals(resource()))return 0;int n=(int)Math.min(amount,machine.copper.processFluid);if(n<=0)return 0;updateSnapshots(tx);machine.copper.processFluid-=n;return n;}
    @Override public Iterator<StorageView<FluidVariant>> iterator(){return Collections.<StorageView<FluidVariant>>singletonList(new StorageView<>(){
        public boolean isResourceBlank(){return false;}public FluidVariant getResource(){return resource();}public long getAmount(){return machine.copper.processFluid;}public long getCapacity(){return CAPACITY;}
        public long extract(FluidVariant r,long amount,TransactionContext tx){return WorkshopFluidStorage.this.extract(r,amount,tx);}
    }).iterator();}
    @Override public long getVersion(){return ((long)machine.copper.processFluid<<1)|(machine.assembled()?1:0);}
    @Override protected Integer createSnapshot(){return machine.copper.processFluid;}
    @Override protected void readSnapshot(Integer amount){machine.copper.processFluid=amount;}
    @Override protected void onFinalCommit(){machine.sync();}
}
