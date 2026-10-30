package io.github.kpaakeh.smog.copper;

import net.minecraft.core.*;
import net.minecraft.network.syncher.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;
import io.github.kpaakeh.smog.machine.MachineEntity;

/** Server-owned passenger hoist, with native vehicle interpolation and no chunk tickets. */
public final class RopewayRideEntity extends Entity {
    private static final EntityDataAccessor<Long> START=SynchedEntityData.defineId(RopewayRideEntity.class,EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Long> END=SynchedEntityData.defineId(RopewayRideEntity.class,EntityDataSerializers.LONG);
    private int progress,lastWait;
    public RopewayRideEntity(EntityType<?> type,Level level){super(type,level);setNoGravity(true);}
    @Override protected InterpolationHandler createInterpolationHandler(){return LinearInterpolationHandler.create(this,3);}
    @Override protected void defineSynchedData(SynchedEntityData.Builder b){b.define(START,0L);b.define(END,0L);}
    public BlockPos start(){return BlockPos.of(entityData.get(START));}
    public BlockPos end(){return BlockPos.of(entityData.get(END));}
    public boolean uses(BlockPos anchor){return start().equals(anchor)||end().equals(anchor);}
    private int duration(){return Math.max(40,(int)Math.ceil(Math.sqrt(start().distSqr(end())))*10);}
    private Vec3 at(double t){return Ropeway.point(Vec3.atLowerCornerOf(end().subtract(start())),t).add(start().getX(),start().getY()-1.5,start().getZ());}
    public static Vec3 landing(ServerLevel level,BlockPos anchor,Entity passenger){
        // Search a small platform around the anchor, accounting for real floor height and hazards.
        for(int radius=0;radius<=3;radius++)for(int dy=1;dy>=-4;dy--)for(int dx=-radius;dx<=radius;dx++)for(int dz=-radius;dz<=radius;dz++){
            if(Math.max(Math.abs(dx),Math.abs(dz))!=radius)continue;
            var pos=anchor.offset(dx,dy,dz);
            if(!level.hasChunkAt(pos)||!level.hasChunkAt(pos.above())||!level.hasChunkAt(pos.below()))continue;
            var safe=net.minecraft.world.entity.vehicle.DismountHelper.findSafeDismountLocation(passenger.getType(),level,pos,true);
            if(safe!=null&&level.noCollision(passenger,passenger.getDimensions(passenger.getPose()).makeBoundingBox(safe)))return safe;
        }return null;
    }
    private boolean canRelease(ServerLevel level){
        for(var passenger:getPassengers())if(landing(level,start(),passenger)==null&&landing(level,end(),passenger)==null)return false;
        return true;
    }
    private void waiting(int reason){
        if(lastWait==reason)return;lastWait=reason;
        for(var passenger:getPassengers())if(passenger instanceof Player player)
            player.sendOverlayMessage(Component.translatable(reason==1?"message.smog.ride_blocked":"message.smog.ride_landing"));
    }

    private static boolean clear(ServerLevel level,BlockPos start,BlockPos end){
        if(start.equals(end)||start.distSqr(end)>576)return false;
        var delta=Vec3.atLowerCornerOf(end.subtract(start));int steps=Math.max(1,(int)Math.ceil(delta.length()*4));
        for(int n=0;n<=steps;n++){var p=Ropeway.point(delta,n/(double)steps).add(start.getX(),start.getY()-1.5,start.getZ());var box=new AABB(p.x-.3,p.y,p.z-.3,p.x+.3,p.y+1.8,p.z+.3);
            for(int x=(int)Math.floor(box.minX);x<=(int)Math.floor(box.maxX);x++)for(int y=(int)Math.floor(box.minY);y<=(int)Math.floor(box.maxY);y++)for(int z=(int)Math.floor(box.minZ);z<=(int)Math.floor(box.maxZ);z++){
                var pos=new BlockPos(x,y,z);if(!level.hasChunkAt(pos))return false;if(pos.equals(start)||pos.equals(end))continue;
                var shape=level.getBlockState(pos).getCollisionShape(level,pos);if(net.minecraft.world.phys.shapes.Shapes.joinIsNotEmpty(shape.move(x,y,z),net.minecraft.world.phys.shapes.Shapes.create(box),net.minecraft.world.phys.shapes.BooleanOp.AND))return false;
            }
        }return true;
    }
    public static Component mount(ServerLevel level,MachineEntity anchor,Player player){
        if(anchor.copper.ropeLinked&&!level.hasChunkAt(BlockPos.of(anchor.copper.ropeTarget)))return Component.translatable("message.smog.ride_blocked");
        var other=Ropeway.linkedDestination(level,anchor);if(other==null)return Component.translatable("message.smog.ride_link");
        if(player.isPassenger()||!anchor.getItem(1).isEmpty()||!other.getItem(1).isEmpty()||anchor.copper.queued>0||other.copper.queued>0||busy(level,anchor.getBlockPos()))return Component.translatable("message.smog.ride_busy");
        if(!clear(level,anchor.getBlockPos(),other.getBlockPos())||landing(level,anchor.getBlockPos(),player)==null||landing(level,other.getBlockPos(),player)==null)return Component.translatable("message.smog.ride_clear");
        var ride=CopperTransport.ROPEWAY_RIDE.create(level,EntitySpawnReason.SPAWN_ITEM_USE);if(ride==null)return Component.translatable("message.smog.ride_busy");
        ride.entityData.set(START,anchor.getBlockPos().asLong());ride.entityData.set(END,other.getBlockPos().asLong());var at=ride.at(0);ride.setPos(at.x,at.y,at.z);level.addFreshEntity(ride);
        if(!player.startRiding(ride)){ride.discard();return Component.translatable("message.smog.ride_busy");}return Component.translatable("message.smog.ride_started");
    }
    public static boolean busy(ServerLevel level,BlockPos anchor){return !level.getEntitiesOfClass(RopewayRideEntity.class,new AABB(anchor).inflate(26),r->!r.isRemoved()&&r.uses(anchor)).isEmpty();}
    @Override protected boolean canAddPassenger(Entity passenger){return passenger instanceof Player&&getPassengers().isEmpty();}
    @Override public Vec3 getPassengerRidingPosition(Entity passenger){return position();}
    @Override public Vec3 getDismountLocationForPassenger(LivingEntity passenger){
        passenger.fallDistance=0;passenger.setDeltaMovement(Vec3.ZERO);
        if(level() instanceof ServerLevel level){
            var first=progress*2>=duration()?end():start();var second=first.equals(start())?end():start();
            var safe=landing(level,first,passenger);if(safe==null)safe=landing(level,second,passenger);
            if(safe==null)safe=landing(level,blockPosition(),passenger);
            if(safe!=null)return safe;
        }
        // A deliberate emergency dismount remains possible even if the platforms were destroyed.
        return position();
    }
    @Override public boolean hurtServer(ServerLevel level,net.minecraft.world.damagesource.DamageSource source,float damage){return false;}
    @Override public void tick(){
        super.tick();if(level().isClientSide()){getInterpolation().interpolate();return;}
        if(!(level() instanceof ServerLevel level))return;if(getPassengers().isEmpty()){discard();return;}
        if(!level.hasChunkAt(start())||!level.hasChunkAt(end())){waiting(1);return;}
        boolean linked=level.getBlockEntity(start()) instanceof MachineEntity a&&Ropeway.handles(a)&&a.copper.ropeLinked&&a.copper.ropeTarget==end().asLong()&&level.getBlockEntity(end()) instanceof MachineEntity b&&b.copper.ropeLinked&&b.copper.ropeTarget==start().asLong();
        if(!linked){if(canRelease(level)){ejectPassengers();discard();}else waiting(2);return;}
        if(!clear(level,start(),end())){waiting(1);return;}
        if(progress>=duration()){if(canRelease(level)){ejectPassengers();discard();}else waiting(2);return;}
        lastWait=0;
        var next=at(++progress/(double)duration());setPos(next.x,next.y,next.z);setDeltaMovement(Vec3.ZERO);for(var passenger:getPassengers())passenger.fallDistance=0;
    }
    @Override protected void readAdditionalSaveData(ValueInput i){entityData.set(START,i.getLongOr("start",0));entityData.set(END,i.getLongOr("end",0));progress=Math.clamp(i.getIntOr("progress",0),0,duration());}
    @Override protected void addAdditionalSaveData(ValueOutput o){o.putLong("start",entityData.get(START));o.putLong("end",entityData.get(END));o.putInt("progress",progress);}
}
