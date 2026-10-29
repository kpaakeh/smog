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
    public record Layer(float x,float y,float z,float width,float height,float depth,int metal){}
    public record Stream(float x,float y,float z,float height,int metal){}
    private static net.minecraft.resources.Identifier surface(int kind){if(kind==-3||kind==-4)return ModBlocks.id("textures/block/"+(kind==-4?"blazing_fuel_surface":"smeltery_fuel_surface")+".png");return ModBlocks.id("textures/block/"+(kind==-1?"workshop_water":kind==-2?"plant_oil":"molten_"+io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES[kind])+"_surface.png");}
    public static void submit(List<Surface> surfaces,PoseStack pose,SubmitNodeCollector collector,int light){
        for(var s:surfaces)collector.submitCustomGeometry(pose,RenderTypes.entitySolid(surface(s.metal)),(matrix,vertices)->{
            int color=s.hot?0xFFFFFFFF:0xFF777777,brightness=s.hot?net.minecraft.util.LightCoordsUtil.FULL_BRIGHT:light;
            vertex(vertices,matrix,s.x,s.y,s.z+s.depth,0,s.depth,color,brightness);vertex(vertices,matrix,s.x+s.width,s.y,s.z+s.depth,s.width,s.depth,color,brightness);
            vertex(vertices,matrix,s.x+s.width,s.y,s.z,s.width,0,color,brightness);vertex(vertices,matrix,s.x,s.y,s.z,0,0,color,brightness);
        });
    }
    public static void submitLayers(List<Layer> layers,PoseStack pose,SubmitNodeCollector collector){
        for(var layer:layers)collector.submitCustomGeometry(pose,RenderTypes.entitySolid(surface(layer.metal())),(matrix,out)->{
            int light=net.minecraft.util.LightCoordsUtil.FULL_BRIGHT;float x=layer.x(),y=layer.y(),z=layer.z(),xx=x+layer.width(),yy=y+layer.height(),zz=z+layer.depth();
            for(float left=x;left<xx;left+=1)for(float front=z;front<zz;front+=1){float right=Math.min(xx,left+1),back=Math.min(zz,front+1);quad(out,matrix,new float[][]{{left,yy,front},{right,yy,front},{right,yy,back},{left,yy,back}},right-left,back-front,light);}
            for(float base=y;base<yy;base+=1){float top=Math.min(yy,base+1),h=top-base;
                for(float front=z;front<zz;front+=1){float back=Math.min(zz,front+1);quad(out,matrix,new float[][]{{x,base,front},{x,top,front},{x,top,back},{x,base,back}},h,back-front,light);quad(out,matrix,new float[][]{{xx,base,back},{xx,top,back},{xx,top,front},{xx,base,front}},h,back-front,light);}
                for(float left=x;left<xx;left+=1){float right=Math.min(xx,left+1);quad(out,matrix,new float[][]{{right,base,z},{right,top,z},{left,top,z},{left,base,z}},h,right-left,light);quad(out,matrix,new float[][]{{left,base,zz},{left,top,zz},{right,top,zz},{right,base,zz}},h,right-left,light);}
            }
        });
    }
    private static void quad(VertexConsumer out,PoseStack.Pose pose,float[][] p,float width,float height,int light){float[][] uv={{0,0},{0,height},{width,height},{width,0}};float ax=p[3][0]-p[0][0],ay=p[3][1]-p[0][1],az=p[3][2]-p[0][2],bx=p[2][0]-p[3][0],by=p[2][1]-p[3][1],bz=p[2][2]-p[3][2];float nx=ay*bz-az*by,ny=az*bx-ax*bz,nz=ax*by-ay*bx,length=(float)Math.sqrt(nx*nx+ny*ny+nz*nz);if(length==0)return;for(int n:new int[]{0,3,2,1})out.addVertex(pose,p[n][0],p[n][1],p[n][2]).setColor(-1).setUv(uv[n][0],uv[n][1]).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,nx/length,ny/length,nz/length);}
    public static void submitStreams(List<Stream> streams,PoseStack pose,SubmitNodeCollector collector){
        for(var s:streams)collector.submitCustomGeometry(pose,RenderTypes.entitySolid(surface(s.metal)),(matrix,out)->{
            float r=.0625f,bottom=s.y-s.height;int light=net.minecraft.util.LightCoordsUtil.FULL_BRIGHT;
            float[][] corners={{s.x-r,s.z-r},{s.x+r,s.z-r},{s.x+r,s.z+r},{s.x-r,s.z+r}};
            for(int side=0;side<4;side++){var a=corners[side];var b=corners[(side+1)%4];vertex(out,matrix,a[0],bottom,a[1],0,s.height,-1,light);vertex(out,matrix,b[0],bottom,b[1],.125f,s.height,-1,light);vertex(out,matrix,b[0],s.y,b[1],.125f,0,-1,light);vertex(out,matrix,a[0],s.y,a[1],0,0,-1,light);}
        });
    }
    private static void vertex(VertexConsumer out,PoseStack.Pose matrix,float x,float y,float z,float u,float v,int color,int light){out.addVertex(matrix,x,y,z).setColor(color).setUv(u,v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(matrix,0,1,0);}
}
