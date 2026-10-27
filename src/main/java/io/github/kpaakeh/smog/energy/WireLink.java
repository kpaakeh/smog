package io.github.kpaakeh.smog.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.world.phys.Vec3;

public record WireLink(BlockPos a,BlockPos b,ConductorMaterial material,Direction aFace,Direction bFace) {
    private static final Codec<WireLink> MATERIAL_CODEC=RecordCodecBuilder.create(i->i.group(
        BlockPos.CODEC.fieldOf("a").forGetter(WireLink::a),BlockPos.CODEC.fieldOf("b").forGetter(WireLink::b),
        Codec.STRING.xmap(v->ConductorMaterial.valueOf(v.toUpperCase(java.util.Locale.ROOT)),ConductorMaterial::serializedName).fieldOf("material").forGetter(WireLink::material),
        Direction.CODEC.optionalFieldOf("a_face",Direction.UP).forGetter(WireLink::aFace),
        Direction.CODEC.optionalFieldOf("b_face",Direction.UP).forGetter(WireLink::bFace)).apply(i,WireLink::new));
    private static final Codec<WireLink> LEGACY_CODEC=RecordCodecBuilder.create(i->i.group(
        BlockPos.CODEC.fieldOf("a").forGetter(WireLink::a),BlockPos.CODEC.fieldOf("b").forGetter(WireLink::b),
        Codec.STRING.xmap(ConductorMaterial::legacy,Enum::name).fieldOf("voltage").forGetter(WireLink::material),
        Direction.CODEC.optionalFieldOf("a_face",Direction.UP).forGetter(WireLink::aFace),
        Direction.CODEC.optionalFieldOf("b_face",Direction.UP).forGetter(WireLink::bFace)).apply(i,WireLink::new));
    public static final Codec<WireLink> CODEC=Codec.withAlternative(MATERIAL_CODEC,LEGACY_CODEC);
    public WireLink(BlockPos a,BlockPos b,ConductorMaterial material){this(a,b,material,Direction.UP,Direction.UP);}
    public WireLink {
        a=a.immutable();b=b.immutable();
        if(a.compareTo(b)>0){BlockPos t=a;a=b;b=t;Direction face=aFace;aFace=bFace;bFace=face;}
        if(a.equals(b) || a.distSqr(b)>material.reach*material.reach)throw new IllegalArgumentException("Invalid wire span");
    }
    public static Vec3 anchor(BlockPos pos,Direction face){return Vec3.atCenterOf(pos).add(face.getStepX()*.52,face.getStepY()*.52,face.getStepZ()*.52);}
    public Vec3 point(double t) {
        var start=anchor(a,aFace);var end=anchor(b,bFace);double sag=Math.max(.15,start.distanceTo(end)*.055);
        return start.lerp(end,t).add(0,-4*sag*t*(1-t),0);
    }
    public boolean touches(BlockPos pos){return a.equals(pos)||b.equals(pos);}
}
