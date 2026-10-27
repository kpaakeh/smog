package io.github.kpaakeh.smog.render;

import java.util.List;
import io.github.kpaakeh.smog.energy.WireLink;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.phys.Vec3;

public final class WireRendering {
    private WireRendering(){}
    public static void submit(List<WireLink> wires,BlockPos origin,PoseStack pose,SubmitNodeCollector collector,int light){
        collector.submitCustomGeometry(pose,RenderTypes.leash(),(matrix,vertices)->{
            for(var wire:wires)for(int segment=0;segment<32;segment++){
                Vec3 a=wire.point(segment/32.0).subtract(origin.getX(),origin.getY(),origin.getZ());
                Vec3 b=wire.point((segment+1)/32.0).subtract(origin.getX(),origin.getY(),origin.getZ());
                Vec3 along=b.subtract(a).normalize(),side=along.cross(new Vec3(0,1,0));if(side.lengthSqr()<.001)side=new Vec3(1,0,0);
                side=side.normalize().scale(.018+.008*wire.voltage().ordinal());Vec3 up=along.cross(side).normalize().scale(.018+.008*wire.voltage().ordinal());
                Vec3[] offsets={side.add(up),side.subtract(up),side.scale(-1).subtract(up),up.subtract(side)};
                for(int face=0;face<4;face++){
                    int color=wire.voltage().color|0xFF000000;
                    vertex(vertices,matrix,a.add(offsets[face]),color,light);vertex(vertices,matrix,b.add(offsets[face]),color,light);
                    vertex(vertices,matrix,b.add(offsets[(face+1)%4]),color,light);vertex(vertices,matrix,a.add(offsets[(face+1)%4]),color,light);
                }
            }
        });
    }
    private static void vertex(VertexConsumer out,PoseStack.Pose pose,Vec3 p,int color,int light){out.addVertex(pose,(float)p.x,(float)p.y,(float)p.z).setColor(color).setLight(light);}
}
