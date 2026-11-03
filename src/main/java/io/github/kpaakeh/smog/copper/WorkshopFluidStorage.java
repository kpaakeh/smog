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

/** Exact finite workshop supply or oil receiver. Inventory and pipes share the same persisted tank. */
public final class WorkshopFluidStorage extends SnapshotParticipant<WorkshopFluidStorage.Snapshot> implements Storage<FluidVariant> {
    public static final int BUCKET=81000,CAPACITY=8*BUCKET;
    private final MachineEntity machine;
    public WorkshopFluidStorage(MachineEntity machine){this.machine=machine;}
    protected record Snapshot(int amount,FluidVariant fluid){}
    public boolean reservoir(){return CopperWorkshops.workshop(machine,"water_tank");}
    public static boolean pipeCompatible(FluidVariant fluid){return !fluid.isBlank()&&fluid.equals(FluidVariant.of(fluid.getFluid()))&&fluid.typeHolder().is(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.FLUID,ModBlocks.id("copper_pipe_fluids")));}
    public boolean washing(){return !CopperWorkshops.workshop(machine,"lever_press");}
    public FluidVariant resource(){return reservoir()?machine.copper.supplyFluid:FluidVariant.of(washing()?Fluids.WATER:BuiltInRegistries.FLUID.getValue(ModBlocks.id("plant_oil")));}
    private boolean ready(){return machine.getLevel()!=null&&!machine.getLevel().isClientSide()&&!machine.isRemoved()&&machine.assembled();}
    @Override public long insert(FluidVariant resource,long amount,TransactionContext tx){StoragePreconditions.notNegative(amount);if(!ready()||(reservoir()? !pipeCompatible(resource)||(machine.copper.processFluid>0&&!resource.equals(resource())):!resource.equals(resource())))return 0;int n=(int)Math.min(amount,CAPACITY-machine.copper.processFluid);if(n<=0)return 0;updateSnapshots(tx);if(reservoir())machine.copper.supplyFluid=resource;machine.copper.processFluid+=n;return n;}
    @Override public long extract(FluidVariant resource,long amount,TransactionContext tx){StoragePreconditions.notNegative(amount);if(!ready()||!resource.equals(resource()))return 0;int n=(int)Math.min(amount,machine.copper.processFluid);if(n<=0)return 0;updateSnapshots(tx);machine.copper.processFluid-=n;return n;}
    @Override public Iterator<StorageView<FluidVariant>> iterator(){return Collections.<StorageView<FluidVariant>>singletonList(new StorageView<>(){
        public boolean isResourceBlank(){return machine.copper.processFluid==0;}public FluidVariant getResource(){return machine.copper.processFluid==0?FluidVariant.blank():resource();}public long getAmount(){return machine.copper.processFluid;}public long getCapacity(){return CAPACITY;}
        public long extract(FluidVariant r,long amount,TransactionContext tx){return WorkshopFluidStorage.this.extract(r,amount,tx);}
    }).iterator();}
    @Override public long getVersion(){return ((long)machine.copper.processFluid<<33)^((long)resource().hashCode()<<1)^(machine.assembled()?1:0);}
    @Override protected Snapshot createSnapshot(){return new Snapshot(machine.copper.processFluid,machine.copper.supplyFluid);}
    @Override protected void readSnapshot(Snapshot snapshot){machine.copper.processFluid=snapshot.amount();machine.copper.supplyFluid=snapshot.fluid();}
    @Override protected void onFinalCommit(){machine.sync();}
}
