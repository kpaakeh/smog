package io.github.kpaakeh.smog.gui;

import io.github.kpaakeh.smog.copper.CopperWorkshops;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/** Vanilla stonecutter texture, sprites, slot geometry and scrolling; pottery recipes. */
public final class PotteryGui {
    private float offset;
    private boolean dragging;
    private net.minecraft.world.item.Item input;
    private int rows(MachineMenu menu){return Math.max(0,(menu.potteryChoices().size()+3)/4-3);}
    private int start(MachineMenu menu){return Math.round(offset*rows(menu))*4;}
    private static Identifier sprite(String name){return Identifier.withDefaultNamespace("container/stonecutter/"+name);}
    public void draw(MachineMenu menu,Font font,GuiGraphicsExtractor g,int x,int y,int mouseX,int mouseY){
        if(input!=menu.container.getItem(0).getItem()){input=menu.container.getItem(0).getItem();offset=0;dragging=false;}
        g.blit(RenderPipelines.GUI_TEXTURED,Identifier.withDefaultNamespace("textures/gui/container/stonecutter.png"),x,y,0,0,176,166,256,256);
        g.blitSprite(RenderPipelines.GUI_TEXTURED,sprite(rows(menu)>0&&!menu.container.getItem(0).isEmpty()?"scroller":"scroller_disabled"),x+119,y+15+(int)(41*offset),12,15);
        if(menu.container.getItem(0).isEmpty())return;
        var choices=menu.potteryChoices();var recipes=CopperWorkshops.potteryRecipes();int start=start(menu);
        for(int cell=0;cell<12&&start+cell<choices.size();cell++){
            int choice=choices.get(start+cell),px=x+52+(cell%4)*16,py=y+14+(cell/4)*18;
            boolean hovered=mouseX>=px&&mouseX<px+16&&mouseY>=py&&mouseY<py+18;
            g.blitSprite(RenderPipelines.GUI_TEXTURED,sprite(choice==menu.potterySelection()?"recipe_selected":hovered?"recipe_highlighted":"recipe"),px,py,16,18);
            var recipe=recipes.get(choice);var stack=new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(recipe.output())),recipe.outputCount());g.fakeItem(stack,px,py+1);
            if(hovered)g.setTooltipForNextFrame(stack.getHoverName(),mouseX,mouseY);
        }
    }
    public int click(MachineMenu menu,double mx,double my,int x,int y,int button){
        dragging=false;if(button!=com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_LEFT||menu.container.getItem(0).isEmpty())return -1;
        if(rows(menu)>0&&mx>=x+119&&mx<x+131&&my>=y+9&&my<y+65){dragging=true;drag(menu,my,y);return -1;}
        var choices=menu.potteryChoices();int start=start(menu);
        for(int cell=0;cell<12&&start+cell<choices.size();cell++){int px=x+52+(cell%4)*16,py=y+14+(cell/4)*18;if(mx>=px&&mx<px+16&&my>=py&&my<py+18)return MachineMenu.POTTERY_SELECT+choices.get(start+cell);}
        return -1;
    }
    public boolean scroll(MachineMenu menu,double amount){int rows=rows(menu);if(rows==0||menu.container.getItem(0).isEmpty())return false;offset=Math.clamp(offset-(float)amount/rows,0,1);return true;}
    public boolean drag(MachineMenu menu,double y,int top){if(!dragging||rows(menu)==0)return false;offset=Math.clamp(((float)y-top-15-7.5f)/41,0,1);return true;}
    public void release(){dragging=false;}
}
