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
    public static void submit(List<Surface> surfaces,PoseStack pose,SubmitNodeCollector collector,int light){
        for(var s:surfaces)collector.submitCustomGeometry(pose,RenderTypes.entitySolid(ModBlocks.id("textures/block/molten_"+new String[]{"copper","tin","bronze"}[s.metal]+"_surface.png")),(matrix,vertices)->{
            int color=s.hot?0xFFFFFFFF:0xFF777777,brightness=s.hot?net.minecraft.util.LightCoordsUtil.FULL_BRIGHT:light;
            vertex(vertices,matrix,s.x,s.y,s.z+s.depth,0,s.depth,color,brightness);vertex(vertices,matrix,s.x+s.width,s.y,s.z+s.depth,s.width,s.depth,color,brightness);
            vertex(vertices,matrix,s.x+s.width,s.y,s.z,s.width,0,color,brightness);vertex(vertices,matrix,s.x,s.y,s.z,0,0,color,brightness);
        });
    }
    private static void vertex(VertexConsumer out,PoseStack.Pose matrix,float x,float y,float z,float u,float v,int color,int light){out.addVertex(matrix,x,y,z).setColor(color).setUv(u,v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(matrix,0,1,0);}
}
