package io.github.kpaakeh.smog.bronze;

import java.util.*;
import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/** Reciprocal shafts and visible gear teeth share finite mechanical power, without FE.
 * Belts and stored flywheel energy are not relays yet.
 */
public final class BronzeNetwork {
    private static final Set<String> NODES=Set.of("bronze_drive","bronze_hand_crank","bronze_shaft","bronze_shaft_support","bronze_angle_gearbox","bronze_reduction_gearbox","bronze_cogwheel","bronze_splitter","bronze_clutch","bronze_drive_coupler");
    public record Source(int power,RotationRate rate){public Source {if(power<0||!Double.isFinite(rate.value()))throw new IllegalArgumentException("Invalid mechanical source");}}
    public record Result(int available,int required,String status,Map<BlockPos,Integer> consumers,RotationRate rate,double phase,ExactFraction availableExact,ExactFraction requiredExact){
        public Result(int available,int required,String status,Map<BlockPos,Integer> consumers,RotationRate rate,double phase){this(available,required,status,consumers,rate,phase,ExactFraction.of(available),ExactFraction.of(required));}
        public boolean running(){return status.equals("ready");}
    }
    private static final class Cache {long tick=Long.MIN_VALUE;final Map<BlockPos,Result> results=new HashMap<>();final Map<BlockPos,RotationTimeline> phases=new HashMap<>();}
    private static final Map<Level,Cache> CACHES=new WeakHashMap<>();
    private static final Map<net.minecraft.world.level.block.Block,java.util.function.BiFunction<Level,BlockPos,Source>> SOURCES=new HashMap<>();
    private BronzeNetwork() {}
    public static boolean node(String name){return NODES.contains(name);}
    public static void registerSource(net.minecraft.world.level.block.Block block,java.util.function.BiFunction<Level,BlockPos,Source> source){if(SOURCES.putIfAbsent(block,source)!=null)throw new IllegalArgumentException("Source already registered");}
    private static boolean sourceBlock(BlockState state){return SOURCES.containsKey(state.getBlock())||Set.of("bronze_drive","bronze_hand_crank").contains(id(state));}
    private static boolean node(BlockState state){return sourceBlock(state)||node(id(state));}
    private static boolean unsupportedAxle(BlockState state){return id(state).equals("bronze_shaft")||state.getBlock() instanceof BronzeGearBlock;}
    private static Source source(Level level,BlockPos pos,BlockState state){
        var external=SOURCES.get(state.getBlock());if(external!=null)return external.apply(level,pos);
        if(id(state).equals("bronze_drive")&&BronzeDrive.working(level,pos))return new Source(16,RotationRate.NOMINAL);
        if(id(state).equals("bronze_hand_crank")&&level.getBlockEntity(pos) instanceof BronzeEntity crank&&crank.cranking())return new Source(4,crank.crankRate());
        return null;
    }
    private static String id(BlockState state){var key=BuiltInRegistries.BLOCK.getKey(state.getBlock());return key.getNamespace().equals("smog")?key.getPath():"";}
    public static void invalidate(Level level){var cache=CACHES.get(level);if(cache!=null)cache.results.clear();}
    private static Direction facing(BlockState state){return state.hasProperty(BronzeBlock.FACING)?state.getValue(BronzeBlock.FACING):state.getValue(WorkshopPartBlock.FACING);}
    public static Set<Direction> ports(Level level,BlockPos pos,BlockState state){
        String n=id(state);if(!node(state))return Set.of();var f=facing(state);
        if(state.getBlock() instanceof BronzeSupportBlock&&!BronzeSupportBlock.mounted(state,level,pos))return Set.of();
        if(n.equals("bronze_clutch")&&(!state.getValue(BronzeBlock.OPEN)||level.hasNeighborSignal(pos)))return Set.of();
        if(sourceBlock(state)||n.equals("bronze_drive_coupler"))return Set.of(f.getOpposite());
        if(state.getBlock() instanceof BronzeSplitterBlock)return BronzeSplitterBlock.ports(state);
        if(state.getBlock() instanceof BronzeGearBlock)return BronzeGearBlock.ports(level,pos,state);
        if(state.getBlock() instanceof BronzeAngleBlock)return Set.of(f.getOpposite(),BronzeAngleBlock.output(state));
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
        if(level.isClientSide()||!level.hasChunkAt(start))return new Result(0,0,"unloaded",Map.of(),RotationRate.ZERO,0);
        var cache=CACHES.computeIfAbsent(level,l->new Cache());if(cache.tick!=level.getGameTime()){cache.tick=level.getGameTime();cache.results.clear();}
        var existing=cache.results.get(start);if(existing!=null)return existing;
        var nodes=new LinkedHashMap<BlockPos,BlockState>();var queue=new ArrayDeque<BlockPos>();queue.add(start.immutable());String problem="";
        while(!queue.isEmpty()){
            var pos=queue.removeFirst();if(nodes.containsKey(pos))continue;
            if(nodes.size()>=256){problem="large";break;}
            var state=level.getBlockState(pos);if(!node(state))continue;nodes.put(pos,state);
            for(var side:ports(level,pos,state)){var next=pos.relative(side);if(!level.hasChunkAt(next)){problem="unloaded";continue;}if(ports(level,next,level.getBlockState(next)).contains(side.getOpposite())&&!nodes.containsKey(next))queue.add(next);}
        }
        var roots=nodes.entrySet().stream().filter(e->sourceBlock(e.getValue())).map(Map.Entry::getKey).toList();
        var root=(roots.isEmpty()?nodes.keySet():roots).stream().min(java.util.Comparator.comparingLong(BlockPos::asLong)).orElse(start);
        var ratios=new LinkedHashMap<BlockPos,RotationRate>();ratios.put(root,RotationRate.ONE);queue.clear();queue.add(root);
        while(!queue.isEmpty()){
            var pos=queue.removeFirst();var state=nodes.get(pos);if(state==null)continue;
            for(var side:ports(level,pos,state)){
                var next=pos.relative(side);var other=nodes.get(next);if(other==null||!ports(level,next,other).contains(side.getOpposite()))continue;
                var ratio=ratios.get(pos).multiply(BronzeKinematics.across(state,side,other));
                var previous=ratios.putIfAbsent(next,ratio);if(previous==null)queue.add(next);else if(!previous.equals(ratio))problem="direction";
            }
        }
        var consumers=new LinkedHashMap<BlockPos,Integer>();int power=0;RotationRate rate=null;
        var manual=new ArrayList<Map.Entry<BlockPos,Source>>();
        for(var entry:nodes.entrySet()){
            var pos=entry.getKey();var state=entry.getValue();String n=id(state);
            if(n.equals("bronze_drive_coupler")){var target=pos.relative(facing(state));if(level.hasChunkAt(target)&&level.getBlockEntity(target) instanceof MachineEntity machine)add(consumers,machine);}
            var source=source(level,pos,state);
            if(n.equals("bronze_hand_crank")&&source!=null){manual.add(Map.entry(pos,source));source=null;}
            if(source!=null&&source.power()>0&&source.rate().numerator().signum()!=0){
                if(source.rate().belowMinimum())problem="slow";else if(source.rate().aboveMaximum())problem="fast";
                var signed=source.rate().divide(ratios.get(pos));
                if(rate!=null&&!rate.equals(signed))problem="direction";
                rate=signed;power+=source.power();
            }
            if(state.getBlock() instanceof BronzeGearBlock gear){var clearance=gear.clearance(level,pos,state);if(!clearance.isEmpty())problem=clearance;}
            if(n.equals("bronze_drive")){
                // Account for old direct loads as well: the second output is not free power.
                for(int x=-3;x<=3;x++)for(int y=-3;y<=3;y++)for(int z=-3;z<=3;z++){
                    var target=pos.offset(x,y,z);if(level.hasChunkAt(target)&&level.getBlockEntity(target) instanceof MachineEntity machine&&consumer(machine)&&BronzeDrive.directSources(machine).contains(pos))add(consumers,machine);
                }
            }
            if(unsupportedAxle(state)){
                int length=1;var axis=facing(state).getAxis();
                for(var side:List.of(facing(state),facing(state).getOpposite()))for(int step=1;step<=8;step++){
                    var next=pos.relative(side,step);var other=nodes.get(next);if(other==null||!unsupportedAxle(other)||facing(other).getAxis()!=axis)break;length++;
                }if(length>8)problem="support";
            }
        }
        // An unused handle is back-driven, not a brake. A released/contrary handle
        // cannot override a running water drive or stop another player's input.
        manual.sort(java.util.Comparator.<Map.Entry<BlockPos,Source>>comparingLong(e->((BronzeEntity)level.getBlockEntity(e.getKey())).crankStarted()).reversed().thenComparingLong(e->e.getKey().asLong()));
        for(var entry:manual){var candidate=entry.getValue().rate().divide(ratios.get(entry.getKey()));if(rate==null)rate=candidate;if(rate.equals(candidate))power+=entry.getValue().power();}
        int required=consumers.values().stream().mapToInt(Integer::intValue).sum();
        var prospective=new LinkedHashMap<BlockPos,RotationRate>();
        if(rate!=null)for(var entry:nodes.entrySet()){
            var value=rate.multiply(ratios.get(entry.getKey()));prospective.put(entry.getKey(),value);
            for(var side:ports(level,entry.getKey(),entry.getValue())){var portRate=value.multiply(BronzeKinematics.port(entry.getValue(),side));if(portRate.belowMinimum())problem="slow";else if(portRate.aboveMaximum())problem="fast";}
        }
        var exactRequired=ExactFraction.ZERO;
        for(var consumer:consumers.entrySet()){
            ExactFraction tempo=null;var machine=level.getBlockEntity(consumer.getKey()) instanceof MachineEntity m?m:null;var direct=machine==null?List.<BlockPos>of():BronzeDrive.directSources(machine);
            for(var entry:nodes.entrySet())if(prospective.containsKey(entry.getKey())&&(direct.contains(entry.getKey())||id(entry.getValue()).equals("bronze_drive_coupler")&&entry.getKey().relative(facing(entry.getValue())).equals(consumer.getKey()))){
                var next=ExactFraction.of(prospective.get(entry.getKey())).abs().divide(ExactFraction.of(16));if(tempo!=null&&!tempo.equals(next))problem="direction";tempo=next;
            }
            exactRequired=exactRequired.add(ExactFraction.of(consumer.getValue()).multiply(tempo==null?ExactFraction.ONE:tempo));
        }
        String status=!problem.isEmpty()?problem:power==0?"source":exactRequired.compareTo(ExactFraction.of(power))>0?"overload":"ready";
        var actual=status.equals("ready")&&rate!=null?rate:RotationRate.ZERO;
        var timeline=cache.phases.get(root);
        var divisor=java.math.BigInteger.ONE;for(var ratio:ratios.values())divisor=divisor.divide(divisor.gcd(ratio.denominator())).multiply(ratio.denominator());double period=360*divisor.doubleValue();
        if(timeline==null){var slowest=nodes.keySet().stream().filter(p->level.getBlockEntity(p) instanceof BronzeEntity).max(java.util.Comparator.comparing(p->ratios.get(p).denominator()));double seed=slowest.map(p->((BronzeEntity)level.getBlockEntity(p)).angle(0)/ratios.get(p).value()).orElse(0.);timeline=new RotationTimeline(level.getGameTime(),seed,period);cache.phases.put(root,timeline);}
        timeline.period(period);
        double phase=timeline.update(level.getGameTime(),actual);
        var immutableConsumers=Map.copyOf(consumers);
        for(var pos:nodes.keySet()){var ratio=ratios.get(pos);var available=ExactFraction.of(power);cache.results.put(pos,new Result(available.floorInt(),required,status,immutableConsumers,actual.multiply(ratio),RotationTimeline.wrap(phase*ratio.value()),available,exactRequired));}
        return cache.results.getOrDefault(start,new Result(0,0,"source",Map.of(),RotationRate.ZERO,0));
    }
    public static boolean powered(MachineEntity machine){return connection(machine)!=null;}
    public static RotationRate tempo(MachineEntity machine){var result=connection(machine);return result==null?RotationRate.ZERO:result.rate().multiply(RotationRate.of(result.rate().numerator().signum()<0?-1:1,16));}
    private static Result connection(MachineEntity machine){
        var level=machine.getLevel();if(level==null||level.isClientSide())return null;
        var roots=new LinkedHashSet<>(BronzeDrive.directSources(machine));
        for(var side:Direction.values()){var pos=machine.getBlockPos().relative(side);if(!level.hasChunkAt(pos))continue;var state=level.getBlockState(pos);if(id(state).equals("bronze_drive_coupler")&&pos.relative(facing(state)).equals(machine.getBlockPos()))roots.add(pos);}
        for(var root:roots){var result=query(level,root);
            if(result.consumers().getOrDefault(machine.getBlockPos(),-1)<demand(machine)){invalidate(level);result=query(level,root);}
            if(result.running()&&result.consumers().containsKey(machine.getBlockPos()))return result;
        }return null;
    }
}
