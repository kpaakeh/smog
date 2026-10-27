package io.github.kpaakeh.smog.gui;

import io.github.kpaakeh.smog.machine.*;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class MachineScreen extends AbstractContainerScreen<MachineMenu> {
    private Button rateButton,powerButton;
    public MachineScreen(MachineMenu menu,Inventory inventory,Component title) {
        super(menu,inventory,title,menu.layout.width,menu.layout.height);inventoryLabelY=96;
    }
    @Override protected void init() {
        super.init();
        powerButton=addRenderableWidget(new TextureButton(leftPos+134,topPos+77,34,16,"button",Component.translatable("gui.smog.on"),button->{
            if(minecraft.gameMode!=null)minecraft.gameMode.handleInventoryButtonClick(menu.containerId,0);
        }));
        powerButton.setTooltip(Tooltip.create(Component.translatable("gui.smog.toggle_power")));
        rateButton=addRenderableWidget(new TextureButton(leftPos+146,topPos+95,22,12,"voltage_button",Component.literal("+"),button->{
            if(minecraft.gameMode!=null)minecraft.gameMode.handleInventoryButtonClick(menu.containerId,1);
        }));
        rateButton.setTooltip(Tooltip.create(Component.translatable("gui.smog.change_rate")));
        refreshButtons();
    }
    private void refreshButtons(){
        if(powerButton!=null)powerButton.setMessage(Component.translatable(menu.data.get(8)==1?"gui.smog.on":"gui.smog.off"));
        if(rateButton!=null)rateButton.visible=menu.data.get(11)==MachineSpec.Kind.TRANSFORMER.ordinal();
    }
    @Override public void containerTick(){super.containerTick();refreshButtons();}
    @Override public void extractBackground(GuiGraphicsExtractor g,int mouseX,int mouseY,float partial) {
        int x=leftPos,y=topPos;
        g.blit(RenderPipelines.GUI_TEXTURED,menu.layout.texture,x,y,0,0,imageWidth,imageHeight,imageWidth,imageHeight);
        for(var slot:menu.layout.slots)GuiTextures.draw(g,"slot",x+slot.x()-1,y+slot.y()-1,18,18);
        for(int row=0;row<3;row++)for(int col=0;col<9;col++)GuiTextures.draw(g,"slot",x+7+col*18,y+107+row*18,18,18);
        for(int col=0;col<9;col++)GuiTextures.draw(g,"slot",x+7+col*18,y+165,18,18);
        if(menu.layout.id.equals("processor")) {
            GuiTextures.draw(g,"progress_empty",x+60,y+35,41,14);
            int duration=Math.max(1,menu.data.get(1));int filled=41*Math.clamp(menu.data.get(0),0,duration)/duration;
            GuiTextures.crop(g,"progress_full",x+60,y+35,0,0,filled,14,41,14);
        } else if(menu.layout.id.equals("generator")) {
            GuiTextures.draw(g,"fuel_empty",x+74,y+35,14,14);
            int total=Math.max(1,menu.data.get(7));int filled=14*Math.clamp(menu.data.get(6),0,total)/total;
            GuiTextures.crop(g,"fuel_full",x+74,y+49-filled,0,14-filled,14,filled,14,14);
        }
        int capacity=Math.max(1,menu.capacity()),filled=(int)(48L*menu.energy()/capacity);
        GuiTextures.draw(g,"energy_empty",x+146,y+20,12,52);
        GuiTextures.crop(g,"energy_full",x+148,y+70-filled,2,50-filled,8,filled,12,52);
        g.text(font,Component.literal(menu.energy()+" / "+menu.capacity()+" FE"),x+8,y+82,0xFF404040,false);
        int rate=menu.data.get(9);
        g.text(font,Component.literal(rate+" FE/t"),x+99,y+20,0xFF404040,false);
        if(menu.data.get(11)==MachineSpec.Kind.GENERATOR_CONTROLLER.ordinal())
            g.text(font,Component.translatable("generator.smog."+GeneratorKind.values()[Math.clamp(menu.data.get(10),0,7)].getSerializedName()),x+8,y+22,0xFF404040,false);
        if(mouseX>=x+142 && mouseX<x+162 && mouseY>=y+18 && mouseY<y+74)
            g.setTooltipForNextFrame(Component.literal(menu.energy()+" FE | "+rate+" FE/t"),mouseX,mouseY);
    }
    @Override protected void extractLabels(GuiGraphicsExtractor g,int mouseX,int mouseY){
        g.text(font,title,titleLabelX,titleLabelY,0xFF404040,false);g.text(font,playerInventoryTitle,inventoryLabelX,inventoryLabelY,0xFF404040,false);
    }
}
