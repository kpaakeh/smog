package io.github.kpaakeh.smog.copper;

import java.util.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

/** Pouring consumes each actual fluid portion. Cooling is a separate persisted phase. */
public final class CastingProcesses {
    public static final int PORT_FLOW=750;
    private static final Map<ServerLevel,Flow> FLOWS=new WeakHashMap<>();
    private static final class Flow{long tick=Long.MIN_VALUE;final Map<BlockPos,Integer> used=new HashMap<>();}
    private static Flow flow(ServerLevel level){var f=FLOWS.computeIfAbsent(level,k->new Flow());if(f.tick!=level.getGameTime()){f.tick=level.getGameTime();f.used.clear();}return f;}
    public record Recipe(ItemStack result,int units,int cooling,boolean consumeMold){}
    public static Recipe recipe(MachineEntity m,int metal){
        if(CopperWorkshops.workshop(m,"casting_basin")){String name=MoltenMetalStorage.NAMES[metal];var id=net.minecraft.resources.Identifier.parse((Set.of("copper","iron","gold","netherite").contains(name)?"minecraft:":"smog:")+name+"_block");return BuiltInRegistries.ITEM.containsKey(id)?new Recipe(new ItemStack(BuiltInRegistries.ITEM.getValue(id)),9*MoltenMetalStorage.INGOT,160,false):null;}
        var pattern=MoldMaking.pattern(m.getItem(1));if(pattern!=null){int gold=Arrays.asList(MoltenMetalStorage.NAMES).indexOf("gold");if(metal!=gold)return null;var result=MoldMaking.metalMold(pattern);return result.isEmpty()?null:new Recipe(result,2*MoltenMetalStorage.INGOT,80,false);}
        int mold=CopperWorkshops.mold(m.getItem(1));if(mold<0)return null;var result=CopperWorkshops.castResult(metal,mold);return result.isEmpty()?null:new Recipe(result,CopperWorkshops.castCost(metal,mold),Math.max(40,CopperWorkshops.castCost(metal,mold)/225),CopperWorkshops.singleUse(m.getItem(1)));
    }
    public static boolean locked(MachineEntity m){return Smeltery.vessel(m)&&m.smeltery.casting();}
    public static void tick(ServerLevel level,MachineEntity m){
        var s=m.smeltery;
        if(s.casting()){
            m.copper.work=s.required==0?0:100*s.filled/s.required;
            if(!ItemStack.isSameItemSameComponents(m.getItem(1),s.castingMold)){m.copper.status=9;return;}
            if(s.filled>=s.required){
                m.copper.status=10;if(s.cooling>0){s.cooling--;m.setChanged();return;}
                if(s.castingResult.isEmpty()){m.copper.status=9;return;}
                if(!m.canOutput(s.castingResult)){m.copper.status=5;return;}
                if(m.getItem(3).isEmpty())m.setItem(3,s.castingResult.copy());else m.getItem(3).grow(s.castingResult.getCount());
                if(s.consumeMold)m.getItem(1).shrink(1);s.clearCast();m.copper.work=0;m.copper.status=0;m.sync();return;
            }
        }
        var route=CopperWorkshops.route(level,m.getBlockPos());var source=route==null?null:route.source();if(source==null){m.copper.status=s.casting()?8:6;return;}
        int metal=Smeltery.selected(source);if(!s.casting()&&recipe(m,metal)==null){m.copper.status=6;return;}
        var budget=flow(level);int remaining=PORT_FLOW-budget.used.getOrDefault(route.port(),0);if(remaining<=0){m.copper.status=7;return;}
        try(var tx=Transaction.openOuter()){var fluid=MoltenMetalStorage.variant(metal);long available=Math.min(remaining,source.copper.metal(metal));long inserted=m.castingFluid.insert(fluid,available,tx);if(inserted<=0){m.copper.status=8;return;}if(source.molten.extract(fluid,inserted,tx)!=inserted)return;tx.commit();budget.used.merge(route.port(),(int)inserted,Integer::sum);}
    }
    public static int stage(MachineEntity m){var s=m.smeltery;if(!m.getItem(3).isEmpty()&&!s.casting())return 4;if(s.casting())return s.filled<s.required?2:s.cooling>0?3:m.canOutput(s.castingResult)?4:5;return CopperWorkshops.workshop(m,"casting_basin")||!m.getItem(1).isEmpty()?1:0;}
    public static int data(MachineEntity m,int index){var s=m.smeltery;return switch(index){case 20->s.filled&65535;case 21->s.filled>>>16;case 22->s.required&65535;case 23->s.required>>>16;case 24->s.cooling;case 25->s.coolingTotal;case 26->Arrays.asList(MoltenMetalStorage.NAMES).indexOf(s.pourMetal)+1;case 27->stage(m);default->0;};}
    public static void drop(ServerLevel level,MachineEntity m){
        if(m.copper.processFluid>0&&(WaterSupply.handles(m)||ManualWorkshops.handles(m))){dropFluid(level,m,m.processFluid.resource(),m.copper.processFluid);m.copper.processFluid=0;}

        if(Smeltery.vessel(m)&&m.smeltery.filled>0){int kind=Arrays.asList(MoltenMetalStorage.NAMES).indexOf(m.smeltery.pourMetal);if(kind>=0)dropFluid(level,m,MoltenMetalStorage.variant(kind),m.smeltery.filled);m.smeltery.clearCast();}
        if(Smeltery.fuelTank(m)&&m.smeltery.fuelAmount>0){dropFluid(level,m,m.smelteryFuel.resource(),m.smeltery.fuelAmount);m.smeltery.fuelAmount=0;m.smeltery.fuel="";}
    }
    private static void dropFluid(ServerLevel level,MachineEntity m,net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant fluid,int units){while(units>0){int portion=Math.min(units,FluidCanisterStorage.CAPACITY);Containers.dropItemStack(level,m.getBlockPos().getX()+.5,m.getBlockPos().getY()+.5,m.getBlockPos().getZ()+.5,FluidCanisterStorage.filled(fluid,portion));units-=portion;}}
}
