package io.github.kpaakeh.smog.gui;

import io.github.kpaakeh.smog.copper.MoltenMetalStorage;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.Component;

/** A form's actual saved fill and cooling phases, never the unrelated furnace heat UI. */
public final class CastingGui {
    private CastingGui(){}
    public static void draw(MachineMenu menu,Font font,GuiGraphicsExtractor g,int x,int y,int mouseX,int mouseY){
        int filled=SmelteryGui.value(menu,20),required=SmelteryGui.value(menu,22),stage=menu.data.get(27),metal=menu.data.get(26)-1;
        var label=Component.translatable("gui.smog.casting.stage_"+stage);
        g.text(font,Component.literal(font.plainSubstrByWidth(label.getString(),162)),x+7,y+20,0xFF404040,false);
        g.fill(x+25,y+34,x+43,y+66,0xFF373737);g.fill(x+26,y+35,x+43,y+66,0xFFFFFFFF);g.fill(x+26,y+35,x+42,y+65,0xFF8B8B8B);
        if(filled>0&&required>0&&metal>=0&&metal<MoltenMetalStorage.NAMES.length){int height=Math.clamp((int)(30L*filled/required),1,30);SmelteryGui.tile(g,ModBlocks.id("textures/block/molten_"+MoltenMetalStorage.NAMES[metal]+"_surface.png"),x+26,y+65-height,16,height,16);}
        SmelteryGui.marks(g,x+26,y+35,16,30);
        GuiTextures.draw(g,"copper_progress_empty",x+99,y+35,24,16);
        int total=stage==3?Math.max(1,menu.data.get(25)):Math.max(1,required);
        int progress=stage==3?total-menu.data.get(24):filled;
        GuiTextures.crop(g,"copper_progress_full",x+99,y+35,0,0,stage==4?24:Math.clamp((int)(24L*progress/total),0,24),16,24,16);
        if(required>0)g.text(font,Component.translatable("gui.smog.casting.volume",filled/81,required/81),x+7,y+70,0xFF404040,false);
        if(stage==3)g.text(font,Component.translatable("gui.smog.casting.cooling",(menu.data.get(24)+19)/20),x+7,y+82,0xFF404040,false);
        if(mouseX>=x+25&&mouseX<x+43&&mouseY>=y+34&&mouseY<y+66){var tooltip=metal>=0&&metal<MoltenMetalStorage.NAMES.length?Component.translatable("block.smog.molten_"+MoltenMetalStorage.NAMES[metal]).append(" / "+(filled>0&&filled<81?"<1":Integer.toString(filled/81))+" mB"):label;g.setTooltipForNextFrame(tooltip,mouseX,mouseY);}
    }
}
