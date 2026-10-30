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
        transfer(level,m,8100);m.sync();
    }
    public static boolean connects(net.minecraft.world.level.LevelReader level,net.minecraft.core.BlockPos pos){
        if(!level.hasChunkAt(pos))return false;var state=level.getBlockState(pos);String name=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
        if(state.getBlock() instanceof WaterPipeBlock||name.equals("smog:water_valve")||name.equals("smog:water_outlet"))return true;
        if(level.getBlockEntity(pos) instanceof MachineEntity machine)return handles(machine)||CopperWorkshops.workshop(machine,"washing_trough");
        return level instanceof ServerLevel server&&FluidStorage.SIDED.find(server,pos,null)!=null;
    }
    private record Sink(net.fabricmc.fabric.api.transfer.v1.storage.Storage<net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant> storage,net.minecraft.core.BlockPos outlet){}
    private static java.util.List<Sink> sinks(ServerLevel level,MachineEntity source){
        var result=new java.util.ArrayList<Sink>();var queue=new java.util.ArrayDeque<net.minecraft.core.BlockPos>();var seen=new java.util.HashSet<net.minecraft.core.BlockPos>();var start=source.getBlockPos();queue.add(start);seen.add(start);
        while(!queue.isEmpty()&&seen.size()<=256){
            var current=queue.removeFirst();var state=level.getBlockState(current);String currentName=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
            if(currentName.equals("smog:water_outlet")){
                if(state.getValue(WorkshopPartBlock.OPEN)&&level.hasChunkAt(current.below())){var storage=FluidStorage.SIDED.find(level,current.below(),Direction.UP);if(storage!=null&&storage!=source.processFluid)result.add(new Sink(storage,current));}continue;
            }
            if(currentName.equals("smog:water_valve")&&!state.getValue(WorkshopPartBlock.OPEN))continue;
            for(var side:Direction.values()){
                if(currentName.equals("smog:water_valve")&&side.getAxis()!=state.getValue(WorkshopPartBlock.FACING).getAxis())continue;
                var pos=current.relative(side);if(!level.hasChunkAt(pos)||Math.abs(pos.getX()-start.getX())>16||Math.abs(pos.getY()-start.getY())>16||Math.abs(pos.getZ()-start.getZ())>16||seen.contains(pos))continue;
                var next=level.getBlockState(pos);String name=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(next.getBlock()).toString();
                if(name.equals("smog:water_valve")&&side.getAxis()!=next.getValue(WorkshopPartBlock.FACING).getAxis())continue;
                if(name.equals("smog:water_outlet")&&side!=next.getValue(WorkshopPartBlock.FACING))continue;
                // A rejected side must not hide a later, valid route into the same valve.
                seen.add(pos);
                if(next.getBlock() instanceof WaterPipeBlock||name.equals("smog:water_valve")||name.equals("smog:water_outlet")){queue.add(pos);continue;}
                if(level.getBlockEntity(pos) instanceof MachineEntity other&&(CopperWorkshops.workshop(other,"hand_water_pump")||CopperWorkshops.workshop(other,"water_tank")&&!CopperWorkshops.workshop(source,"hand_water_pump")))continue;
                var storage=FluidStorage.SIDED.find(level,pos,side.getOpposite());if(storage!=null&&storage!=source.processFluid)result.add(new Sink(storage,null));
            }
        }return result;
    }
    public static int transfer(ServerLevel level,MachineEntity source,int limit){
        int budget=Math.min(limit,source.copper.processFluid),total=0;if(budget<=0)return 0;
        for(var sink:sinks(level,source)){
            if(budget<=0)break;
            try(var tx=Transaction.openOuter()){
                long accepted=sink.storage.insert(source.processFluid.resource(),budget,tx);long removed=source.processFluid.extract(source.processFluid.resource(),accepted,tx);
                if(accepted==removed&&accepted>0){tx.commit();budget-=accepted;total+=accepted;if(sink.outlet!=null)level.sendParticles(net.minecraft.core.particles.ParticleTypes.SPLASH,sink.outlet.getX()+.5,sink.outlet.getY()+.1,sink.outlet.getZ()+.5,3,.04,.2,.04,0);}
            }
        }return total;
    }
}
