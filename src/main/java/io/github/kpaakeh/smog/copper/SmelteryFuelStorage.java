package io.github.kpaakeh.smog.copper;

import java.util.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import io.github.kpaakeh.smog.machine.MachineEntity;

/** Fuel lives only in the physical tank, never duplicated in its controller. */
public final class SmelteryFuelStorage extends SnapshotParticipant<SmelteryFuelStorage.Snapshot> implements Storage<FluidVariant> {
    public static final int CAPACITY=8*81000;
    public record Snapshot(String fluid,int amount){}
    private final MachineEntity machine;
    private long version;
    public SmelteryFuelStorage(MachineEntity m){machine=m;}
    private boolean ready(){return !machine.isRemoved()&&machine.getLevel()!=null&&!machine.getLevel().isClientSide()&&Smeltery.fuelTank(machine);}
    public FluidVariant resource(){return machine.smeltery.fuel.isEmpty()?FluidVariant.blank():FluidVariant.of(BuiltInRegistries.FLUID.getValue(Identifier.parse(machine.smeltery.fuel)));}
    @Override public long insert(FluidVariant fluid,long amount,TransactionContext tx){StoragePreconditions.notNegative(amount);if(!ready())return 0;if(Smeltery.fuel(fluid)==null||(!resource().isBlank()&&!resource().equals(fluid)))return 0;int accepted=(int)Math.min(amount,CAPACITY-machine.smeltery.fuelAmount);if(accepted<=0)return 0;updateSnapshots(tx);machine.smeltery.fuel=BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString();machine.smeltery.fuelAmount+=accepted;return accepted;}
    @Override public long extract(FluidVariant fluid,long amount,TransactionContext tx){StoragePreconditions.notNegative(amount);if(!ready())return 0;if(!resource().equals(fluid))return 0;int extracted=(int)Math.min(amount,machine.smeltery.fuelAmount);if(extracted<=0)return 0;updateSnapshots(tx);machine.smeltery.fuelAmount-=extracted;if(machine.smeltery.fuelAmount==0)machine.smeltery.fuel="";return extracted;}
    @Override public Iterator<StorageView<FluidVariant>> iterator(){return List.<StorageView<FluidVariant>>of(new StorageView<>(){public boolean isResourceBlank(){return resource().isBlank();}public FluidVariant getResource(){return resource();}public long getAmount(){return machine.smeltery.fuelAmount;}public long getCapacity(){return CAPACITY;}public long extract(FluidVariant fluid,long amount,TransactionContext tx){return SmelteryFuelStorage.this.extract(fluid,amount,tx);}}).iterator();}
    @Override public long getVersion(){return version;}
    @Override protected Snapshot createSnapshot(){return new Snapshot(machine.smeltery.fuel,machine.smeltery.fuelAmount);}
    @Override protected void readSnapshot(Snapshot snapshot){machine.smeltery.fuel=snapshot.fluid();machine.smeltery.fuelAmount=snapshot.amount();}
    @Override protected void onFinalCommit(){version++;machine.sync();}
}
