package io.github.kpaakeh.smog.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class MachineScreen extends AbstractContainerScreen<MachineMenu> {
    private Button voltageButton;
    public MachineScreen(MachineMenu menu,Inventory inventory,Component title) {
        super(menu,inventory,title,menu.layout.width,menu.layout.height);inventoryLabelY=96;
    }
    @Override protected void init() {
        super.init();
        addRenderableWidget(Button.builder(Component.literal("I/O"),button -> {
            if(minecraft.gameMode!=null)minecraft.gameMode.handleInventoryButtonClick(menu.containerId,0);
        }).bounds(leftPos+134,topPos+77,34,16).build());
        voltageButton=addRenderableWidget(Button.builder(Component.literal("V"),button->{if(minecraft.gameMode!=null)minecraft.gameMode.handleInventoryButtonClick(menu.containerId,1);})
            .bounds(leftPos+146,topPos+95,22,12).build());
    }
    @Override public void containerTick(){super.containerTick();if(voltageButton!=null)voltageButton.visible=menu.data.get(11)==io.github.kpaakeh.smog.machine.MachineSpec.Kind.TRANSFORMER.ordinal();}
    @Override public void extractBackground(GuiGraphicsExtractor g,int mouseX,int mouseY,float partial) {
        int x=leftPos,y=topPos;
        g.fill(x,y,x+imageWidth,y+imageHeight,0xFF28323C);g.outline(x,y,imageWidth,imageHeight,0xFF82919B);
        for(var slot:menu.layout.slots)slot(g,x+slot.x(),y+slot.y());
        for(int row=0;row<3;row++)for(int col=0;col<9;col++)slot(g,x+8+col*18,y+108+row*18);
        for(int col=0;col<9;col++)slot(g,x+8+col*18,y+166);
        g.fill(x+60,y+39,x+101,y+45,0xFF172028);
        int duration=Math.max(1,menu.data.get(1));g.fill(x+60,y+39,x+60+41*Math.min(duration,menu.data.get(0))/duration,y+45,0xFF74BBA1);
        int capacity=Math.max(1,menu.capacity());int filled=(int)(52L*menu.energy()/capacity);
        g.fill(x+146,y+20,x+158,y+72,0xFF101820);g.fill(x+146,y+72-filled,x+158,y+72,0xFFE2B65D);
        g.text(font,Component.literal(menu.energy()+" / "+menu.capacity()+" EU"),x+8,y+82,0xFFD0DDE3,false);
        g.text(font,Component.literal(menu.data.get(8)==1?"ON":"OFF"),x+136,y+65,0xFFE7EDF0,false);
        g.text(font,Component.literal(io.github.kpaakeh.smog.energy.Voltage.values()[Math.clamp(menu.data.get(9),0,2)].name()),x+128,y+20,0xFFE7EDF0,false);
        if(menu.data.get(11)==io.github.kpaakeh.smog.machine.MachineSpec.Kind.GENERATOR_CONTROLLER.ordinal())
            g.text(font,Component.translatable("generator.smog."+io.github.kpaakeh.smog.machine.GeneratorKind.values()[Math.clamp(menu.data.get(10),0,7)].getSerializedName()),x+8,y+22,0xFFD0DDE3,false);
        if(mouseX>=x+142 && mouseX<x+162 && mouseY>=y+18 && mouseY<y+74)
            g.setTooltipForNextFrame(Component.literal(menu.energy()+" EU | "+io.github.kpaakeh.smog.energy.Voltage.values()[Math.clamp(menu.data.get(9),0,2)]),mouseX,mouseY);
    }
    @Override protected void extractLabels(GuiGraphicsExtractor g,int mouseX,int mouseY){
        g.text(font,title,titleLabelX,titleLabelY,0xFFE7EDF0,false);g.text(font,playerInventoryTitle,inventoryLabelX,inventoryLabelY,0xFFD0DDE3,false);
    }
    private void slot(GuiGraphicsExtractor g,int x,int y) {
        g.fill(x-1,y-1,x+17,y+17,0xFF121920);g.outline(x-1,y-1,18,18,0xFF667681);
    }
}
