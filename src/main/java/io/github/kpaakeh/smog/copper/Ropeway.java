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
    public static Vec3 point(Vec3 end,double t){return new Vec3(.5,.8,.5).add(end.scale(t)).add(0,-Math.sin(t*Math.PI)*Math.min(.7,end.length()*.025),0);}
    public static boolean clear(ServerLevel level,BlockPos a,BlockPos b){
        if(a.equals(b)||a.distSqr(b)>24*24)return false;var delta=Vec3.atLowerCornerOf(b.subtract(a));int steps=(int)Math.ceil(delta.length()*4);
        for(int n=0;n<=steps;n++){
            var at=point(delta,n/(double)steps).add(a.getX(),a.getY(),a.getZ());var box=new AABB(at.x-.3,at.y-.65,at.z-.3,at.x+.3,at.y+.025,at.z+.3);
            for(int x=(int)Math.floor(box.minX);x<=(int)Math.floor(box.maxX);x++)for(int y=(int)Math.floor(box.minY);y<=(int)Math.floor(box.maxY);y++)for(int z=(int)Math.floor(box.minZ);z<=(int)Math.floor(box.maxZ);z++){
                var pos=new BlockPos(x,y,z);if(!level.hasChunkAt(pos))return false;if(pos.equals(a)||pos.equals(b))continue;var shape=level.getBlockState(pos).getCollisionShape(level,pos);if(net.minecraft.world.phys.shapes.Shapes.joinIsNotEmpty(shape.move(x,y,z),net.minecraft.world.phys.shapes.Shapes.create(box),net.minecraft.world.phys.shapes.BooleanOp.AND))return false;
            }
        }return true;
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
