package io.github.kpaakeh.smog.gui;

import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/** Native-size PNG drawing; progress indicators crop their texture rather than stretch it. */
public final class GuiTextures {
    private GuiTextures(){}
    public static Identifier id(String name){return ModBlocks.id("textures/gui/"+name+".png");}
    public static void draw(GuiGraphicsExtractor g,String name,int x,int y,int width,int height){
        g.blit(RenderPipelines.GUI_TEXTURED,id(name),x,y,0,0,width,height,width,height);
    }
    public static void crop(GuiGraphicsExtractor g,String name,int x,int y,int u,int v,int width,int height,int textureWidth,int textureHeight){
        if(width>0&&height>0)g.blit(RenderPipelines.GUI_TEXTURED,id(name),x,y,u,v,width,height,textureWidth,textureHeight);
    }
}
