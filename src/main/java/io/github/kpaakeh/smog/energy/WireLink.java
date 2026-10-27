package io.github.kpaakeh.smog.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public record WireLink(BlockPos a,BlockPos b,Voltage voltage) {
    public static final Codec<WireLink> CODEC=RecordCodecBuilder.create(i->i.group(
        BlockPos.CODEC.fieldOf("a").forGetter(WireLink::a),BlockPos.CODEC.fieldOf("b").forGetter(WireLink::b),
        Codec.STRING.xmap(Voltage::valueOf,Voltage::name).fieldOf("voltage").forGetter(WireLink::voltage)).apply(i,WireLink::new));
    public WireLink {
        a=a.immutable();b=b.immutable();if(a.compareTo(b)>0){BlockPos t=a;a=b;b=t;}
        if(a.equals(b) || a.distSqr(b)>voltage.reach*voltage.reach)throw new IllegalArgumentException("Invalid wire span");
    }
    public Vec3 point(double t) {
        double length=Math.sqrt(a.distSqr(b)),sag=Math.max(.15,length*.055);
        return new Vec3(a.getX()+.5+(b.getX()-a.getX())*t,a.getY()+1.02+(b.getY()-a.getY())*t-4*sag*t*(1-t),a.getZ()+.5+(b.getZ()-a.getZ())*t);
    }
    public boolean touches(BlockPos pos){return a.equals(pos)||b.equals(pos);}
}
