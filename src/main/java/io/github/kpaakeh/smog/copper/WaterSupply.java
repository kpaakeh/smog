package io.github.kpaakeh.smog.copper;

import io.github.kpaakeh.smog.machine.MachineEntity;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.level.material.Fluids;

/** Small loaded-chunk supply. Hand priming grants a finite job, never a permanent free-running pump. */
public final class WaterSupply {
    public static boolean handles(MachineEntity m){return CopperWorkshops.workshop(m,"water_tank")||CopperWorkshops.workshop(m,"hand_water_pump");}
    public static boolean prime(ServerLevel level,MachineEntity m){
        var below=m.getBlockPos().below();var fluid=level.getFluidState(below);
        if(!fluid.is(Fluids.WATER)||!fluid.isSource())return false;
        if(m.copper.pumpBudget==0)m.copper.pumpBudget=4*WorkshopFluidStorage.BUCKET;m.sync();return true;
    }
    public static void tick(ServerLevel level,MachineEntity m){
        if(level.getGameTime()%10!=0)return;
        if(CopperWorkshops.workshop(m,"hand_water_pump")&&m.copper.pumpBudget>0){
            var water=level.getFluidState(m.getBlockPos().below());
            if(water.is(Fluids.WATER)&&water.isSource()){
                int amount=Math.min(8100,Math.min(m.copper.pumpBudget,WorkshopFluidStorage.CAPACITY-m.copper.processFluid));
                m.copper.processFluid+=amount;m.copper.pumpBudget-=amount;
            }
        }
        int budget=Math.min(8100,m.copper.processFluid);
        for(var side:Direction.values()){
            if(budget<=0)break;var pos=m.getBlockPos().relative(side);if(!level.hasChunkAt(pos))continue;
            if(level.getBlockEntity(pos) instanceof MachineEntity other){
                if(CopperWorkshops.workshop(other,"hand_water_pump")||CopperWorkshops.workshop(other,"water_tank")&&!CopperWorkshops.workshop(m,"hand_water_pump"))continue;
            }
            var sink=FluidStorage.SIDED.find(level,pos,side.getOpposite());if(sink==null||sink==m.processFluid)continue;
            try(var tx=Transaction.openOuter()){
                long accepted=sink.insert(m.processFluid.resource(),budget,tx);
                long removed=m.processFluid.extract(m.processFluid.resource(),accepted,tx);
                if(accepted==removed){tx.commit();budget-=accepted;}
            }
        }
        m.sync();
    }
}
