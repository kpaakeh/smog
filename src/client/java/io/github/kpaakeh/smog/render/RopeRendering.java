package io.github.kpaakeh.smog.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.phys.Vec3;

/** Leash geometry uses triangle strips, with degenerate ends to isolate each ribbon. */
public final class RopeRendering {
    private static Vec3 side(Vec3 direction,double radius){
        var cross=direction.cross(new Vec3(0,1,0));
        return cross.lengthSqr()<1e-8?new Vec3(radius,0,0):cross.normalize().scale(radius);
    }
    private static void ribbon(Vec3[] points,Vec3 width,PoseStack pose,SubmitNodeCollector collector,int light){
        collector.submitCustomGeometry(pose,RenderTypes.leash(),(matrix,out)->{
            var first=points[0].subtract(width);
            out.addVertex(matrix,(float)first.x,(float)first.y,(float)first.z).setColor(0xFFAA8651).setLight(light);
            for(var point:points)for(var vertex:new Vec3[]{point.subtract(width),point.add(width)})
                out.addVertex(matrix,(float)vertex.x,(float)vertex.y,(float)vertex.z).setColor(0xFFAA8651).setLight(light);
            var last=points[points.length-1].add(width);
            out.addVertex(matrix,(float)last.x,(float)last.y,(float)last.z).setColor(0xFFAA8651).setLight(light);
        });
    }
    public static void straight(Vec3 start,Vec3 end,PoseStack pose,SubmitNodeCollector collector,int light){
        var direction=end.subtract(start);if(direction.lengthSqr()<1e-8)return;
        var width=side(direction,.018);var points=new Vec3[]{start,end};
        ribbon(points,width,pose,collector,light);
        ribbon(points,direction.normalize().cross(width),pose,collector,light);
    }
    public static Vec3 point(Vec3 end,double t){return io.github.kpaakeh.smog.copper.Ropeway.point(end,t);}
    public static void submit(Vec3 end,PoseStack pose,SubmitNodeCollector collector,int light){
        if(end.lengthSqr()<1e-8)return;
        var points=new Vec3[33];for(int i=0;i<points.length;i++)points[i]=point(end,i/32.0);
        var width=side(end,.025);
        ribbon(points,width,pose,collector,light);
        ribbon(points,end.normalize().cross(width),pose,collector,light);
    }
}
