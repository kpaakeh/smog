package io.github.kpaakeh.smog.gui;

import io.github.kpaakeh.smog.copper.CopperWorkshops;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/** Recipe picker; its icons never represent stored or extractable items. */
public final class PotteryGui {
    private int page;
    private net.minecraft.world.item.Item input;
    private static void panel(GuiGraphicsExtractor g,int x,int y,int w,int h){
        g.fill(x,y,x+w,y+h,0xFF373737);g.fill(x+1,y+1,x+w-1,y+h-1,0xFFFFFFFF);g.fill(x+3,y+3,x+w-3,y+h-3,0xFFC6C6C6);g.fill(x+w-3,y+3,x+w-1,y+h-1,0xFF555555);g.fill(x+3,y+h-3,x+w-1,y+h-1,0xFF555555);
    }
    public void draw(MachineMenu menu,Font font,GuiGraphicsExtractor g,int x,int y,int mouseX,int mouseY){
        var choices=menu.potteryChoices();if(input!=menu.container.getItem(0).getItem()){input=menu.container.getItem(0).getItem();page=0;}
        int pages=Math.max(1,(choices.size()+8)/9);page=Math.clamp(page,0,pages-1);
        panel(g,x,y,248,210);
        g.text(font,Component.translatable("gui.smog.pottery.input"),x+16,y+32,0xFF404040,false);
        g.text(font,Component.translatable("gui.smog.pottery.choose"),x+92,y+18,0xFF404040,false);
        g.text(font,Component.translatable("gui.smog.pottery.output"),x+202,y+32,0xFF404040,false);
        for(var slot:menu.layout.slots)GuiTextures.draw(g,"slot",x+slot.x()-1,y+slot.y()-1,18,18);
        for(int row=0;row<3;row++)for(int col=0;col<9;col++)GuiTextures.draw(g,"slot",x+43+col*18,y+127+row*18,18,18);
        for(int col=0;col<9;col++)GuiTextures.draw(g,"slot",x+43+col*18,y+185,18,18);
        g.text(font,Component.literal("→"),x+62,y+57,0xFF404040,false);g.text(font,Component.literal("→"),x+181,y+57,0xFF404040,false);
        var recipes=CopperWorkshops.potteryRecipes();
        for(int cell=0;cell<9&&page*9+cell<choices.size();cell++){
            int choice=choices.get(page*9+cell),px=x+96+(cell%3)*20,py=y+32+(cell/3)*20;var recipe=recipes.get(choice);
            boolean selected=choice==menu.potterySelection(),ready=CopperWorkshops.is(menu.container.getItem(0),recipe.input())&&menu.container.getItem(0).getCount()>=recipe.count();
            g.fill(px-2,py-2,px+18,py+18,selected?0xFF6D854A:0xFF8B8B8B);GuiTextures.draw(g,"slot",px-1,py-1,18,18);
            var item=new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(recipe.output())),recipe.outputCount());g.fakeItem(item,px,py);
            if(!ready)g.fill(px,py,px+16,py+16,0x55777777);
            if(mouseX>=px-1&&mouseX<px+17&&mouseY>=py-1&&mouseY<py+17)g.setTooltipForNextFrame(item.getHoverName().copy().append(" · ").append(Component.translatable("gui.smog.pottery.cost",recipe.count(),BuiltInRegistries.ITEM.getValue(Identifier.parse(recipe.input())).getDefaultInstance().getHoverName())),mouseX,mouseY);
        }
        panel(g,x+96,y+94,16,14);panel(g,x+136,y+94,16,14);
        g.text(font,Component.literal("<"),x+101,y+97,0xFF404040,false);g.text(font,Component.literal(">"),x+141,y+97,0xFF404040,false);
        var pageText=Component.literal((page+1)+"/"+pages);g.text(font,pageText,x+124-font.width(pageText)/2,y+97,0xFF404040,false);
        if(mouseX>=x+211&&mouseX<x+237&&mouseY>=y+49&&mouseY<y+75&&menu.slots.get(1).getItem().isEmpty())g.setTooltipForNextFrame(Component.translatable("gui.smog.pottery.take"),mouseX,mouseY);
    }
    public int click(MachineMenu menu,double mouseX,double mouseY,int x,int y,int button){
        if(button!=com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_LEFT)return -1;
        var choices=menu.potteryChoices();int pages=Math.max(1,(choices.size()+8)/9);
        if(mouseY>=y+94&&mouseY<y+108){if(mouseX>=x+96&&mouseX<x+112)page=Math.floorMod(page-1,pages);if(mouseX>=x+136&&mouseX<x+152)page=(page+1)%pages;return -1;}
        for(int cell=0;cell<9&&page*9+cell<choices.size();cell++){int px=x+96+(cell%3)*20,py=y+32+(cell/3)*20;if(mouseX>=px-1&&mouseX<px+17&&mouseY>=py-1&&mouseY<py+17)return MachineMenu.POTTERY_SELECT+choices.get(page*9+cell);}
        return -1;
    }
}
