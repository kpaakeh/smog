package io.github.kpaakeh.smog.render;

import java.util.*;
import com.mojang.blaze3d.vertex.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

/** Textured, tile-sized liquid surfaces; no coincident planes with vessel walls or molds. */
public final class MoltenRendering {
    public record Surface(float x,float y,float z,float width,float depth,int metal,boolean hot){}
    public record Stream(float x,float y,float z,float height,int metal){}
    private static net.minecraft.resources.Identifier surface(int kind){return ModBlocks.id("textures/block/"+(kind==-1?"workshop_water":kind==-2?"plant_oil":"molten_"+io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES[kind])+"_surface.png");}
    public static void submit(List<Surface> surfaces,PoseStack pose,SubmitNodeCollector collector,int light){
        for(var s:surfaces)collector.submitCustomGeometry(pose,RenderTypes.entitySolid(surface(s.metal)),(matrix,vertices)->{
            int color=s.hot?0xFFFFFFFF:0xFF777777,brightness=s.hot?net.minecraft.util.LightCoordsUtil.FULL_BRIGHT:light;
            vertex(vertices,matrix,s.x,s.y,s.z+s.depth,0,s.depth,color,brightness);vertex(vertices,matrix,s.x+s.width,s.y,s.z+s.depth,s.width,s.depth,color,brightness);
            vertex(vertices,matrix,s.x+s.width,s.y,s.z,s.width,0,color,brightness);vertex(vertices,matrix,s.x,s.y,s.z,0,0,color,brightness);
        });
    }
    public static void submitStreams(List<Stream> streams,PoseStack pose,SubmitNodeCollector collector){
        for(var s:streams)collector.submitCustomGeometry(pose,RenderTypes.entitySolid(surface(s.metal)),(matrix,out)->{
            float r=.0625f,bottom=s.y-s.height;int light=net.minecraft.util.LightCoordsUtil.FULL_BRIGHT;
            float[][] corners={{s.x-r,s.z-r},{s.x+r,s.z-r},{s.x+r,s.z+r},{s.x-r,s.z+r}};
            for(int side=0;side<4;side++){var a=corners[side];var b=corners[(side+1)%4];vertex(out,matrix,a[0],bottom,a[1],0,s.height,-1,light);vertex(out,matrix,b[0],bottom,b[1],.125f,s.height,-1,light);vertex(out,matrix,b[0],s.y,b[1],.125f,0,-1,light);vertex(out,matrix,a[0],s.y,a[1],0,0,-1,light);}
        });
    }
    private static void vertex(VertexConsumer out,PoseStack.Pose matrix,float x,float y,float z,float u,float v,int color,int light){out.addVertex(matrix,x,y,z).setColor(color).setUv(u,v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(matrix,0,1,0);}
}
