package io.github.kpaakeh.smog.bronze;

import java.util.*;
import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/** First mechanical circuit: reciprocal physical ports, shared finite power, no FE.
 * Ratio-changing transmissions, belts and stored flywheel energy are not relays yet.
 */
public final class BronzeNetwork {
    private static final Set<String> NODES=Set.of("bronze_drive","bronze_hand_crank","bronze_shaft","bronze_shaft_support","bronze_angle_gearbox","bronze_splitter","bronze_clutch","bronze_drive_coupler");
    public record Result(int available,int required,String status,Map<BlockPos,Integer> consumers){public boolean running(){return status.equals("ready");}}
    private static final class Cache {long tick=Long.MIN_VALUE;final Map<BlockPos,Result> results=new HashMap<>();}
    private static final Map<Level,Cache> CACHES=new WeakHashMap<>();
    private BronzeNetwork() {}
    public static boolean node(String name){return NODES.contains(name);}
    private static String id(BlockState state){var key=BuiltInRegistries.BLOCK.getKey(state.getBlock());return key.getNamespace().equals("smog")?key.getPath():"";}
    public static void invalidate(Level level){var cache=CACHES.get(level);if(cache!=null)cache.results.clear();}
    private static Direction facing(BlockState state){return state.hasProperty(BronzeBlock.FACING)?state.getValue(BronzeBlock.FACING):state.getValue(WorkshopPartBlock.FACING);}
    public static Set<Direction> ports(Level level,BlockPos pos,BlockState state){
        String n=id(state);if(!node(n))return Set.of();var f=facing(state);
        if(n.equals("bronze_clutch")&&(!state.getValue(BronzeBlock.OPEN)||level.hasNeighborSignal(pos)))return Set.of();
        if(n.equals("bronze_drive")||n.equals("bronze_hand_crank")||n.equals("bronze_drive_coupler"))return Set.of(f.getOpposite());
        if(n.equals("bronze_angle_gearbox"))return f.getAxis()==Direction.Axis.Y?Set.of():Set.of(f.getOpposite(),f.getClockWise());
        if(n.equals("bronze_splitter"))return f.getAxis()==Direction.Axis.Y?Set.of():Set.of(f,f.getOpposite(),f.getClockWise(),f.getCounterClockWise());
        return Set.of(f,f.getOpposite());
    }
    private static boolean consumer(MachineEntity m){return CopperWorkshops.workshop(m,"lever_press")||CopperWorkshops.workshop(m,"hand_bellows")||LoadingCrane.handles(m)||Ropeway.handles(m);}
    public static int demand(MachineEntity m){
        if(CopperWorkshops.workshop(m,"hand_bellows"))return m.enabled()?2:0;
        if(CopperWorkshops.workshop(m,"lever_press"))return m.enabled()&&m.assembled()&&ManualWorkshops.accepts(m,m.getItem(0))&&m.getItem(0).getCount()>=8&&m.canOutput(ManualWorkshops.result(m))&&(ManualWorkshops.fiber(m)||m.copper.processFluid<=WorkshopFluidStorage.CAPACITY-WorkshopFluidStorage.BUCKET/4)?4:0;
        return m.copper.queued>0||!m.getItem(1).isEmpty()?LoadingCrane.handles(m)?8:Ropeway.handles(m)?4:0:0;
    }
    private static void add(Map<BlockPos,Integer> consumers,MachineEntity m){if(consumer(m))consumers.put(m.getBlockPos(),demand(m));}
    public static Result query(Level level,BlockPos start){
        if(level.isClientSide()||!level.hasChunkAt(start))return new Result(0,0,"unloaded",Map.of());
        var cache=CACHES.computeIfAbsent(level,l->new Cache());if(cache.tick!=level.getGameTime()){cache.tick=level.getGameTime();cache.results.clear();}
        var existing=cache.results.get(start);if(existing!=null)return existing;
        var nodes=new LinkedHashMap<BlockPos,BlockState>();var queue=new ArrayDeque<BlockPos>();queue.add(start.immutable());String problem="";
        while(!queue.isEmpty()){
            var pos=queue.removeFirst();if(nodes.containsKey(pos))continue;
            if(nodes.size()>=256){problem="large";break;}
            var state=level.getBlockState(pos);if(!node(id(state)))continue;nodes.put(pos,state);
            for(var side:ports(level,pos,state)){var next=pos.relative(side);if(!level.hasChunkAt(next)){problem="unloaded";continue;}if(ports(level,next,level.getBlockState(next)).contains(side.getOpposite())&&!nodes.containsKey(next))queue.add(next);}
        }
        var consumers=new LinkedHashMap<BlockPos,Integer>();int power=0;Direction sourceDirection=null;
        for(var entry:nodes.entrySet()){
            var pos=entry.getKey();var state=entry.getValue();String n=id(state);
            if(n.equals("bronze_drive_coupler")){var target=pos.relative(facing(state));if(level.hasChunkAt(target)&&level.getBlockEntity(target) instanceof MachineEntity machine)add(consumers,machine);}
            boolean source=n.equals("bronze_drive")&&BronzeDrive.working(level,pos)||n.equals("bronze_hand_crank")&&level.getBlockEntity(pos) instanceof BronzeEntity crank&&crank.cranking();
            if(source){var direction=facing(state).getOpposite();if(sourceDirection!=null&&sourceDirection!=direction)problem="direction";sourceDirection=direction;power+=n.equals("bronze_drive")?16:4;}
            if(n.equals("bronze_drive")){
                // Account for old direct loads as well: the second output is not free power.
                for(int x=-3;x<=3;x++)for(int y=-3;y<=3;y++)for(int z=-3;z<=3;z++){
                    var target=pos.offset(x,y,z);if(level.hasChunkAt(target)&&level.getBlockEntity(target) instanceof MachineEntity machine&&consumer(machine)&&BronzeDrive.directSources(machine).contains(pos))add(consumers,machine);
                }
            }
            if(n.equals("bronze_shaft")){
                int length=1;var axis=facing(state).getAxis();
                for(var side:List.of(facing(state),facing(state).getOpposite()))for(int step=1;step<=8;step++){
                    var next=pos.relative(side,step);var other=nodes.get(next);if(other==null||!id(other).equals("bronze_shaft")||facing(other).getAxis()!=axis)break;length++;
                }if(length>8)problem="support";
            }
        }
        int required=consumers.values().stream().mapToInt(Integer::intValue).sum();
        var result=new Result(power,required,!problem.isEmpty()?problem:power==0?"source":required>power?"overload":"ready",Map.copyOf(consumers));
        nodes.keySet().forEach(pos->cache.results.put(pos,result));return result;
    }
    public static boolean powered(MachineEntity machine){
        var level=machine.getLevel();if(level==null||level.isClientSide())return false;
        var roots=new LinkedHashSet<>(BronzeDrive.directSources(machine));
        for(var side:Direction.values()){var pos=machine.getBlockPos().relative(side);if(!level.hasChunkAt(pos))continue;var state=level.getBlockState(pos);if(id(state).equals("bronze_drive_coupler")&&pos.relative(facing(state)).equals(machine.getBlockPos()))roots.add(pos);}
        for(var root:roots){var result=query(level,root);
            if(result.consumers().getOrDefault(machine.getBlockPos(),-1)<demand(machine)){invalidate(level);result=query(level,root);}
            if(result.running()&&result.consumers().containsKey(machine.getBlockPos()))return true;
        }return false;
    }
}
