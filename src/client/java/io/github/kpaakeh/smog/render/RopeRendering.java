package io.github.kpaakeh.smog.render;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.phys.Vec3;
public final class RopeRendering {
    public static Vec3 point(Vec3 end,double t){return io.github.kpaakeh.smog.copper.Ropeway.point(end,t);}
    public static void submit(Vec3 end,PoseStack pose,SubmitNodeCollector collector,int light){collector.submitCustomGeometry(pose,RenderTypes.leash(),(matrix,out)->{for(int i=0;i<32;i++){var a=point(end,i/32.0);var b=point(end,(i+1)/32.0);for(int axis=0;axis<2;axis++){Vec3 width=axis==0?new Vec3(.025,0,0):new Vec3(0,.025,.025);for(var vertex:new Vec3[]{a.subtract(width),b.subtract(width),b.add(width),a.add(width)})out.addVertex(matrix,(float)vertex.x,(float)vertex.y,(float)vertex.z).setColor(0xFFAA8651).setLight(light);}}});}
}
