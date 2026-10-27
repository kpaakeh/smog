package io.github.kpaakeh.smog.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public final class TextureButton extends Button {
    private final String texture;
    public TextureButton(int x,int y,int width,int height,String texture,Component text,OnPress press){
        super(x,y,width,height,text,press,DEFAULT_NARRATION);this.texture=texture;
    }
    @Override protected void extractContents(GuiGraphicsExtractor graphics,int mouseX,int mouseY,float partial){
        GuiTextures.draw(graphics,texture+(!active?"_disabled":isHoveredOrFocused()?"_hover":""),getX(),getY(),getWidth(),getHeight());
        var font=Minecraft.getInstance().font;
        graphics.text(font,getMessage(),getX()+(getWidth()-font.width(getMessage()))/2,getY()+(getHeight()-8)/2,active?0xFF303030:0xFF686868,false);
    }
}
