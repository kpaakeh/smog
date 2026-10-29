package io.github.kpaakeh.smog.copper;

import java.util.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;

/** A form is a real insertion-only FluidStorage, including for pipes from other mods. */
public final class CastingStorage extends SnapshotParticipant<CastingStorage.Snapshot> implements Storage<FluidVariant> {
    public record Snapshot(int filled,int required,int cooling,String metal,ItemStack result,ItemStack mold,long lastFlow,boolean consumeMold){}
    private final MachineEntity machine;private long version;private int signature=Integer.MIN_VALUE;
    public CastingStorage(MachineEntity m){machine=m;}
    @Override public long insert(FluidVariant fluid,long amount,TransactionContext tx){
        StoragePreconditions.notNegative(amount);int metal=MoltenMetalStorage.kind(fluid);if(!Smeltery.vessel(machine)||metal<0||machine.isRemoved()||machine.getLevel()==null||machine.getLevel().isClientSide())return 0;
        var s=machine.smeltery;var recipe=s.casting()?new CastingProcesses.Recipe(s.castingResult,s.required,s.cooling,s.consumeMold):CastingProcesses.recipe(machine,metal);if(recipe==null||!machine.canOutput(s.casting()?s.castingResult:recipe.result()))return 0;
        if(s.casting()&&(!s.pourMetal.equals(MoltenMetalStorage.NAMES[metal])||!ItemStack.isSameItemSameComponents(machine.getItem(1),s.castingMold)))return 0;
        int cost=s.casting()?s.required:recipe.units();long accepted=Math.min(amount,Math.max(0,cost-s.filled));if(accepted<=0)return 0;
        updateSnapshots(tx);if(!s.casting()){s.consumeMold=recipe.consumeMold();s.required=recipe.units();s.cooling=recipe.cooling();s.pourMetal=MoltenMetalStorage.NAMES[metal];s.castingResult=recipe.result().copy();s.castingMold=machine.getItem(1).copyWithCount(1);}s.filled+=(int)accepted;s.lastFlow=machine.getLevel().getGameTime();return accepted;
    }
    @Override public long extract(FluidVariant fluid,long amount,TransactionContext tx){StoragePreconditions.notNegative(amount);return 0;}
    @Override public Iterator<StorageView<FluidVariant>> iterator(){return List.<StorageView<FluidVariant>>of(new StorageView<>(){public boolean isResourceBlank(){return !machine.smeltery.casting();}public FluidVariant getResource(){int k=Arrays.asList(MoltenMetalStorage.NAMES).indexOf(machine.smeltery.pourMetal);return k<0?FluidVariant.blank():MoltenMetalStorage.variant(k);}public long getAmount(){return machine.smeltery.filled;}public long getCapacity(){return machine.smeltery.required;}public long extract(FluidVariant fluid,long amount,TransactionContext tx){return 0;}}).iterator();}
    @Override public long getVersion(){var state=machine.smeltery;int next=java.util.Objects.hash(state.filled,state.required,state.cooling>0,machine.getItem(1).getItem(),machine.getItem(1).getComponents(),machine.getItem(1).getCount(),machine.getItem(3).getItem(),machine.getItem(3).getComponents(),machine.getItem(3).getCount());if(next!=signature){signature=next;version++;}return version;}
    @Override protected Snapshot createSnapshot(){var s=machine.smeltery;return new Snapshot(s.filled,s.required,s.cooling,s.pourMetal,s.castingResult.copy(),s.castingMold.copy(),s.lastFlow,s.consumeMold);}
    @Override protected void readSnapshot(Snapshot v){var s=machine.smeltery;s.filled=v.filled();s.required=v.required();s.cooling=v.cooling();s.pourMetal=v.metal();s.castingResult=v.result();s.castingMold=v.mold();s.lastFlow=v.lastFlow();s.consumeMold=v.consumeMold();}
    @Override protected void onFinalCommit(){version++;machine.copper.selection=Arrays.asList(MoltenMetalStorage.NAMES).indexOf(machine.smeltery.pourMetal);machine.copper.status=7;machine.copper.work=machine.smeltery.required==0?0:100*machine.smeltery.filled/machine.smeltery.required;machine.sync();}
}
