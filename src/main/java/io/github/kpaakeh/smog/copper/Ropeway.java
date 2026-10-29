package io.github.kpaakeh.smog.copper;

import io.github.kpaakeh.smog.machine.MachineEntity;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

/** Two loaded anchors, a physical clear span, and a persistent travelling load. */
public final class Ropeway {
    public static boolean handles(MachineEntity m){return CopperWorkshops.workshop(m,"rope_anchor");}
    public static boolean clear(ServerLevel level,BlockPos a,BlockPos b){
        if(a.equals(b)||a.distSqr(b)>24*24)return false;
        var start=Vec3.atCenterOf(a);var end=Vec3.atCenterOf(b);var direction=end.subtract(start).normalize();
        int steps=(int)Math.ceil(start.distanceTo(end)*2);for(int i=0;i<=steps;i++){var pos=BlockPos.containing(start.lerp(end,i/(double)steps));if(!level.hasChunkAt(pos))return false;}
        return level.clip(new ClipContext(start.add(direction.scale(.9)),end.subtract(direction.scale(.9)),ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,net.minecraft.world.phys.shapes.CollisionContext.empty())).getType()==HitResult.Type.MISS;
    }
    public static MachineEntity destination(ServerLevel level,MachineEntity m){
        if(!m.copper.ropeLinked)return null;var pos=BlockPos.of(m.copper.ropeTarget);if(!clear(level,m.getBlockPos(),pos)||!(level.getBlockEntity(pos) instanceof MachineEntity other)||!handles(other)||!other.copper.ropeLinked||other.copper.ropeTarget!=m.getBlockPos().asLong())return null;return other;
    }
    public static boolean link(ServerLevel level,MachineEntity a,MachineEntity b){
        if(!handles(a)||!handles(b)||a.copper.ropeLinked||b.copper.ropeLinked||!clear(level,a.getBlockPos(),b.getBlockPos()))return false;
        a.copper.ropeLinked=b.copper.ropeLinked=true;a.copper.ropeTarget=b.getBlockPos().asLong();b.copper.ropeTarget=a.getBlockPos().asLong();a.sync();b.sync();return true;
    }
    public static void unlink(ServerLevel level,MachineEntity m){
        if(m.copper.ropeLinked&&level.hasChunkAt(BlockPos.of(m.copper.ropeTarget))&&level.getBlockEntity(BlockPos.of(m.copper.ropeTarget)) instanceof MachineEntity other&&handles(other)&&other.copper.ropeTarget==m.getBlockPos().asLong()){other.copper.ropeLinked=false;other.sync();}
        m.copper.ropeLinked=false;m.sync();
    }
    public static int duration(MachineEntity m){return Math.max(40,(int)Math.ceil(Math.sqrt(m.getBlockPos().distSqr(BlockPos.of(m.copper.ropeTarget))))*10);}
    public static void tick(ServerLevel level,MachineEntity m){
        var other=destination(level,m);if(other==null){m.copper.status=8;return;}
        if(m.getItem(1).isEmpty()&&m.copper.queued>0&&!m.getItem(0).isEmpty()){m.setItem(1,m.removeItem(0,64));m.copper.work=0;m.copper.queued=0;m.sync();}
        if(m.getItem(1).isEmpty())return;
        if(m.copper.work<duration(m)){m.copper.work+=BronzeDrive.connected(m)?2:1;m.setChanged();return;}
        var load=m.getItem(1);if(!other.canOutput(load)){m.copper.status=5;return;}
        if(other.getItem(3).isEmpty())other.setItem(3,load);else other.getItem(3).grow(load.getCount());m.setItem(1,ItemStack.EMPTY);m.copper.work=0;m.copper.status=0;other.sync();m.sync();
    }
}
