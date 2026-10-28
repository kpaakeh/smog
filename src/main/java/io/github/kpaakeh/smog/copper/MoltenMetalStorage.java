package io.github.kpaakeh.smog.copper;

import java.util.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.registries.*;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;

/** One shared-capacity, transactional molten-metal tank. Fabric units, no rounding during pipe transfers. */
public final class MoltenMetalStorage extends SnapshotParticipant<int[]> implements Storage<FluidVariant> {
    public static final int QUARTER=2250,INGOT=9000;
    public static final String[] NAMES={"copper","tin","bronze"};
    private final MachineEntity machine;
    private long version;
    private int lastCopper=-1,lastTin,lastBronze,lastCapacity,lastHot;
    public MoltenMetalStorage(MachineEntity machine){this.machine=machine;}
    public static FluidVariant variant(int metal){return FluidVariant.of(BuiltInRegistries.FLUID.getValue(ModBlocks.id("molten_"+NAMES[metal])));}
    public static int kind(FluidVariant resource){
        if(resource.isBlank()||!resource.equals(FluidVariant.of(resource.getFluid())))return -1;
        for(int i=0;i<3;i++)if(resource.equals(variant(i))||resource.typeHolder().is(TagKey.create(Registries.FLUID,Identifier.fromNamespaceAndPath("c","molten_"+NAMES[i]))))return i;
        return -1;
    }
    private boolean ready(){return machine.getLevel()!=null&&!machine.getLevel().isClientSide()&&!machine.isRemoved()&&machine.assembled()&&CopperWorkshops.workshop(machine,"copper_hearth");}
    @Override public long insert(FluidVariant resource,long maxAmount,TransactionContext transaction){
        StoragePreconditions.notNegative(maxAmount);int k=kind(resource);if(k<0||!ready())return 0;
        long accepted=Math.min(maxAmount,Math.max(0,CopperWorkshops.capacity(machine)-machine.copper.total()));if(accepted==0)return 0;
        updateSnapshots(transaction);int previous=machine.copper.total();machine.copper.heat=(int)(((long)machine.copper.heat*previous+1000L*accepted)/(previous+accepted));machine.copper.metal(k,machine.copper.metal(k)+(int)accepted);return accepted;
    }
    @Override public long extract(FluidVariant resource,long maxAmount,TransactionContext transaction){
        StoragePreconditions.notNegative(maxAmount);int k=kind(resource);if(k<0||!ready()||machine.copper.heat<CopperWorkshops.threshold(machine))return 0;
        int extracted=(int)Math.min(maxAmount,machine.copper.metal(k));if(extracted==0)return 0;
        updateSnapshots(transaction);machine.copper.metal(k,machine.copper.metal(k)-extracted);return extracted;
    }
    @Override public Iterator<StorageView<FluidVariant>> iterator(){
        List<StorageView<FluidVariant>> views=new ArrayList<>();
        for(int metal=0;metal<3;metal++){final int k=metal;views.add(new StorageView<>(){
            public boolean isResourceBlank(){return false;}
            public FluidVariant getResource(){return variant(k);}
            public long getAmount(){return machine.copper.metal(k);}
            public long getCapacity(){return CopperWorkshops.capacity(machine);}
            public long extract(FluidVariant resource,long amount,TransactionContext transaction){return kind(resource)==k?MoltenMetalStorage.this.extract(resource,amount,transaction):0;}
        });}return views.iterator();
    }
    @Override public long getVersion(){int cap=CopperWorkshops.capacity(machine),hot=machine.copper.heat>=CopperWorkshops.threshold(machine)?1:0;if(lastCopper!=machine.copper.copper||lastTin!=machine.copper.tin||lastBronze!=machine.copper.bronze||lastCapacity!=cap||lastHot!=hot){lastCopper=machine.copper.copper;lastTin=machine.copper.tin;lastBronze=machine.copper.bronze;lastCapacity=cap;lastHot=hot;version++;}return version;}
    @Override protected int[] createSnapshot(){return new int[]{machine.copper.copper,machine.copper.tin,machine.copper.bronze,machine.copper.heat};}
    @Override protected void readSnapshot(int[] s){machine.copper.copper=s[0];machine.copper.tin=s[1];machine.copper.bronze=s[2];machine.copper.heat=s[3];}
    @Override protected void onFinalCommit(){version++;CopperWorkshops.alloy(machine);machine.sync();}
}
