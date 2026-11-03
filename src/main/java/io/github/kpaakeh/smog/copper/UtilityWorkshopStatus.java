package io.github.kpaakeh.smog.copper;

import java.util.HashSet;
import io.github.kpaakeh.smog.machine.MachineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

/** Read-only utility status. Codes are scoped by the menu kind, not the legacy process status.
 * No extraction, insertion probes, queue changes or chunk loading take place here.
 */
public final class UtilityWorkshopStatus {
    public static final class Water {
        public static final int DISABLED=0,NEED_SOURCE=1,NEED_PRIME=2,PUMPING=3,FULL=4,EMPTY=5,READY=6;
        private Water(){}
    }
    public static final class Rope {
        public static final int DISABLED=0,UNLINKED=1,UNLOADED=2,INVALID_LINK=3,BLOCKED=4,
            PASSENGER_BUSY=5,MOVING=6,DESTINATION_FULL=7,OUTPUT_READY=8,NO_INPUT=9,WAITING_DISPATCH=10,QUEUED=11;
        private Rope(){}
    }
    public static final class Crane {
        public static final int ASSEMBLE=0,DISABLED=1,IDLE=2,QUEUED=3,MOVING=4,SOURCE=5,EMPTY=6,
            DESTINATION=7,BLOCKED=8,UNLOADED=9,DESTINATION_FULL=10;
        private Crane(){}
    }
    private UtilityWorkshopStatus(){}
    public static int inspect(MachineEntity machine){
        if(WaterSupply.handles(machine))return water(machine);
        if(Ropeway.handles(machine))return rope(machine);
        if(LoadingCrane.handles(machine))return crane(machine);
        return 0;
    }
    private static int water(MachineEntity machine){
        if(!machine.enabled())return Water.DISABLED;
        if(machine.copper.processFluid>=WorkshopFluidStorage.CAPACITY)return Water.FULL;
        if(CopperWorkshops.workshop(machine,"hand_water_pump")){
            var level=machine.getLevel();var below=machine.getBlockPos().below();
            if(level==null||!level.hasChunkAt(below))return Water.NEED_SOURCE;
            var source=level.getFluidState(below);if(!source.is(Fluids.WATER)||!source.isSource())return Water.NEED_SOURCE;
            return machine.copper.pumpBudget>0?Water.PUMPING:Water.NEED_PRIME;
        }
        return machine.copper.processFluid<=0?Water.EMPTY:Water.READY;
    }
    private static int rope(MachineEntity machine){
        if(!machine.enabled())return Rope.DISABLED;
        if(!machine.copper.ropeLinked)return Rope.UNLINKED;
        if(!(machine.getLevel() instanceof ServerLevel level))return Rope.WAITING_DISPATCH;
        if(RopewayRideEntity.busy(level,machine.getBlockPos()))return Rope.PASSENGER_BUSY;
        var target=BlockPos.of(machine.copper.ropeTarget);var origin=machine.getBlockPos();
        if(origin.equals(target)||origin.distSqr(target)>24*24)return Rope.INVALID_LINK;
        if(!level.hasChunkAt(target))return Rope.UNLOADED;
        var other=Ropeway.linkedDestination(level,machine);if(other==null)return Rope.INVALID_LINK;
        if(!spanLoaded(level,origin,target))return Rope.UNLOADED;
        if(!Ropeway.clear(level,origin,target))return Rope.BLOCKED;
        var load=machine.getItem(1);
        if(!load.isEmpty())return machine.copper.work>=Ropeway.duration(machine)&&!other.canOutput(load)?Rope.DESTINATION_FULL:Rope.MOVING;
        if(!machine.getItem(3).isEmpty())return Rope.OUTPUT_READY;
        if(machine.getItem(0).isEmpty())return Rope.NO_INPUT;
        return machine.copper.queued>0?Rope.QUEUED:Rope.WAITING_DISPATCH;
    }
    private static boolean spanLoaded(ServerLevel level,BlockPos origin,BlockPos target){
        var delta=Vec3.atLowerCornerOf(target.subtract(origin));int steps=Math.max(1,(int)Math.ceil(delta.length()*4));var seen=new HashSet<Long>();
        for(int n=0;n<=steps;n++){
            var point=Ropeway.point(delta,n/(double)steps).add(origin.getX(),origin.getY(),origin.getZ());
            for(int x=(int)Math.floor(point.x-.3);x<=(int)Math.floor(point.x+.3);x++)for(int z=(int)Math.floor(point.z-.3);z<=(int)Math.floor(point.z+.3);z++){
                long chunk=((long)(x>>4)<<32)^((z>>4)&0xffffffffL);
                if(seen.add(chunk)&&!level.hasChunkAt(new BlockPos(x,origin.getY(),z)))return false;
            }
        }return true;
    }
    private static int crane(MachineEntity machine){
        if(!machine.assembled())return Crane.ASSEMBLE;
        if(!machine.enabled())return Crane.DISABLED;
        if(!(machine.getLevel() instanceof ServerLevel level))return Crane.IDLE;
        boolean reverse=machine.copper.selection%2==1;var source=LoadingCrane.bay(machine,reverse);var target=LoadingCrane.bay(machine,!reverse);
        if(!level.hasChunkAt(source)||!level.hasChunkAt(target))return Crane.UNLOADED;
        var obstruction=LoadingCrane.obstruction(machine);if(obstruction!=null)return level.hasChunkAt(obstruction)?Crane.BLOCKED:Crane.UNLOADED;
        var destination=LoadingCrane.cargo(level,target);if(destination==null)return Crane.DESTINATION;
        if(!destination.supportsInsertion())return Crane.DESTINATION_FULL;
        int pickup=LoadingCrane.duration(machine)*3/20;
        if(machine.getItem(1).isEmpty()&&(machine.copper.queued==0||machine.copper.work<=pickup)){
            var from=LoadingCrane.cargo(level,source);if(from==null)return Crane.SOURCE;if(from==destination)return Crane.DESTINATION;
            boolean hasInput=false;for(var view:from)if(!view.isResourceBlank()&&view.getAmount()>0){hasInput=true;break;}
            if(!hasInput)return Crane.EMPTY;
        }
        // Arbitrary foreign storage filters cannot be predicted by reading slot capacity alone.
        // Preserve the production path's real rejected-capacity result; never mutate a store to probe it.
        if(machine.copper.status==LoadingCrane.DESTINATION)return Crane.DESTINATION_FULL;
        if(machine.copper.queued==0)return Crane.IDLE;
        return machine.copper.work==0?Crane.QUEUED:Crane.MOVING;
    }
}
