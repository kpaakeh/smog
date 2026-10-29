package io.github.kpaakeh.smog.compat.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Inset vanilla slot palette; no stretched sprite edges. */
public final class SmelteryTankDrawable implements IDrawable {
    @Override public int getWidth(){return 24;}
    @Override public int getHeight(){return 34;}
    @Override public void draw(GuiGraphicsExtractor g,int x,int y){g.fill(x,y,x+24,y+34,0xFF373737);g.fill(x+1,y+1,x+24,y+34,0xFFFFFFFF);g.fill(x+1,y+1,x+23,y+33,0xFF8B8B8B);io.github.kpaakeh.smog.gui.SmelteryGui.marks(g,x+1,y+1,22,32);}
}
