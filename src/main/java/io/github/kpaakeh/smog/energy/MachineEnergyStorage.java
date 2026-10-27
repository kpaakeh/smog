package io.github.kpaakeh.smog.energy;

import io.github.kpaakeh.smog.machine.*;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

/** A transactional view of the actual machine buffer. No duplicate energy store or unit conversion. */
public final class MachineEnergyStorage extends SnapshotParticipant<MachineEnergyStorage.Snapshot> implements EnergyStorage {
    record Snapshot(int amount,long tick,long inserted,long extracted){}
    private final MachineEntity machine;
    private long tick=Long.MIN_VALUE,inserted,extracted;
    public MachineEnergyStorage(MachineEntity machine){this.machine=machine;}
    private void refresh(){
        long now=machine.getLevel()==null?Long.MIN_VALUE:machine.getLevel().getGameTime();
        if(now!=tick){tick=now;inserted=extracted=0;}
    }
    private boolean ready(){return machine.getLevel()!=null&&!machine.getLevel().isClientSide()&&!machine.isRemoved()&&machine.enabled()&&machine.assembled();}
    @Override public boolean supportsInsertion(){return !machine.spec().source()&&machine.spec().kind()!=MachineSpec.Kind.DECORATIVE;}
    @Override public boolean supportsExtraction(){return machine.spec().source()||machine.spec().relay();}
    @Override public long insert(long maxAmount,TransactionContext transaction){
        StoragePreconditions.notNegative(maxAmount);if(!ready()||!supportsInsertion())return 0;refresh();
        long amount=Math.min(maxAmount,Math.min(machine.capacity()-machine.energy(),machine.inputRate()-inserted));
        if(amount<=0)return 0;updateSnapshots(transaction);machine.energy(machine.energy()+(int)amount);inserted+=amount;return amount;
    }
    @Override public long extract(long maxAmount,TransactionContext transaction){
        StoragePreconditions.notNegative(maxAmount);if(!ready()||!supportsExtraction())return 0;refresh();
        long amount=Math.min(maxAmount,Math.min(machine.energy(),machine.outputRate()-extracted));
        if(amount<=0)return 0;updateSnapshots(transaction);machine.energy(machine.energy()-(int)amount);extracted+=amount;return amount;
    }
    @Override public long getAmount(){return machine.energy();}
    @Override public long getCapacity(){return machine.capacity();}
    @Override protected Snapshot createSnapshot(){return new Snapshot(machine.energy(),tick,inserted,extracted);}
    @Override protected void readSnapshot(Snapshot snapshot){machine.energy(snapshot.amount);tick=snapshot.tick;inserted=snapshot.inserted;extracted=snapshot.extracted;}
    @Override protected void onFinalCommit(){machine.setChanged();}
}
