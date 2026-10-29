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
    private Button rateButton,powerButton,previous,next,craft,containerMode;
    public MachineScreen(MachineMenu menu,Inventory inventory,Component title) {
        super(menu,inventory,title,menu.layout.width,menu.layout.height);inventoryLabelY=menu.layout.height-94;if(menu.layout.id.equals("smeltery")){inventoryLabelX=80;titleLabelX=80;}
    }
    @Override protected void init() {
        super.init();
        if(menu.layout.id.equals("casting_table")||menu.layout.id.equals("casting_basin"))return;
        if(menu.layout.id.equals("smeltery")){
            addRenderableWidget(Button.builder(Component.literal("<"),button->send(io.github.kpaakeh.smog.copper.Smeltery.PREVIOUS)).bounds(leftPos+7,topPos+219,20,18).build());
            addRenderableWidget(Button.builder(Component.literal(">"),button->send(io.github.kpaakeh.smog.copper.Smeltery.NEXT)).bounds(leftPos+43,topPos+219,20,18).build());
            return;
        }
        if((menu.layout.id.equals("casting_table")||menu.layout.id.equals("casting_basin")||menu.layout.id.equals("copper_workshop")||menu.layout.id.equals("manual_workshop")||menu.layout.id.equals("water_supply")||menu.layout.id.equals("tool_workbench"))) {
            if(menu.layout.id.equals("tool_workbench")){
                previous=addRenderableWidget(Button.builder(Component.literal("<"),button->send(20)).bounds(leftPos+7,topPos+imageHeight-109,16,14).build());
                next=addRenderableWidget(Button.builder(Component.literal(">"),button->send(21)).bounds(leftPos+26,topPos+imageHeight-109,16,14).build());return;
            }
            previous=addRenderableWidget(Button.builder(Component.literal("<"),button->send(20)).bounds(leftPos+7,topPos+83,20,16).build());
            next=addRenderableWidget(Button.builder(Component.literal(">"),button->send(21)).bounds(leftPos+30,topPos+83,20,16).build());
            craft=addRenderableWidget(Button.builder(Component.translatable("gui.smog.copper.craft"),button->send(22)).bounds(leftPos+55,topPos+83,59,16).build());
            return;
        }
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
    private void send(int id){if(minecraft.gameMode!=null)minecraft.gameMode.handleInventoryButtonClick(menu.containerId,id);}
    private void refreshButtons(){
        if(containerMode!=null){containerMode.setMessage(Component.literal(new String[]{"A","↓","↑"}[Math.floorMod(menu.data.get(29),3)]));containerMode.setTooltip(Tooltip.create(Component.translatable("gui.smog.smeltery.container_mode_"+Math.floorMod(menu.data.get(29),3))));}
        if(previous!=null){int kind=menu.data.get(14);previous.visible=next.visible=kind==2||kind==7;previous.active=next.active=menu.data.get(13)==0;if(craft!=null)craft.visible=kind==2||kind==5;}
        if(powerButton!=null)powerButton.setMessage(Component.translatable(menu.data.get(8)==1?"gui.smog.on":"gui.smog.off"));
        if(rateButton!=null)rateButton.visible=menu.data.get(11)==MachineSpec.Kind.TRANSFORMER.ordinal();
    }
    @Override public void containerTick(){super.containerTick();refreshButtons();}
    @Override public void extractBackground(GuiGraphicsExtractor g,int mouseX,int mouseY,float partial) {
        int x=leftPos,y=topPos;if(menu.layout.id.equals("smeltery")){SmelteryGui.draw(menu,font,g,x,y,mouseX,mouseY);return;}
        if(menu.layout.id.equals("tool_workbench")&&imageHeight>190){
            g.blit(RenderPipelines.GUI_TEXTURED,menu.layout.texture,x,y,0,0,176,94,176,190);
            for(int extra=0;extra<imageHeight-190;extra+=18)g.blit(RenderPipelines.GUI_TEXTURED,menu.layout.texture,x,y+94+extra,0,76,176,18,176,190);
            g.blit(RenderPipelines.GUI_TEXTURED,menu.layout.texture,x,y+imageHeight-96,0,94,176,96,176,190);
        }else g.blit(RenderPipelines.GUI_TEXTURED,menu.layout.texture,x,y,0,0,imageWidth,imageHeight,176,190);
        for(var slot:menu.layout.slots)GuiTextures.draw(g,"slot",x+slot.x()-1,y+slot.y()-1,18,18);
        for(int row=0;row<3;row++)for(int col=0;col<9;col++)GuiTextures.draw(g,"slot",x+7+col*18,y+imageHeight-83+row*18,18,18);
        for(int col=0;col<9;col++)GuiTextures.draw(g,"slot",x+7+col*18,y+imageHeight-25,18,18);
        if((menu.layout.id.equals("casting_table")||menu.layout.id.equals("casting_basin")||menu.layout.id.equals("copper_workshop")||menu.layout.id.equals("manual_workshop")||menu.layout.id.equals("water_supply")||menu.layout.id.equals("tool_workbench"))) {
            int kind=menu.data.get(14);
            if(kind==7){var preview=io.github.kpaakeh.smog.copper.ToolAssembly.preview(menu.container,io.github.kpaakeh.smog.copper.ToolDefinitions.at(menu.data.get(6)),menu.data.get(15));
                g.text(font,preview.isEmpty()?io.github.kpaakeh.smog.copper.ToolAssembly.problem(menu.container,io.github.kpaakeh.smog.copper.ToolDefinitions.at(menu.data.get(6))):preview.getMaxDamage()>0?Component.translatable("tool.smog.durability",Math.max(0,preview.getMaxDamage()-preview.getDamageValue()),preview.getMaxDamage()):Component.translatable("tool.smog.shaping_cost",Math.max(1,menu.data.get(17)),preview.getCount()),x+7,y+20,0xFF404040,false);
                if(mouseX>=x+7&&mouseX<x+169&&mouseY>=y+19&&mouseY<y+30){var mounted=menu.container.getItem(5);g.setTooltipForNextFrame(preview.isEmpty()?io.github.kpaakeh.smog.copper.ToolAssembly.problem(menu.container,io.github.kpaakeh.smog.copper.ToolDefinitions.at(menu.data.get(6))):!mounted.isEmpty()?Component.translatable("tool.smog.rebuild_compare",mounted.getMaxDamage(),preview.getMaxDamage()):preview.getHoverName(),mouseX,mouseY);}
                if(!preview.isEmpty()){g.fakeItem(preview,x+79,y+imageHeight-129);if(mouseX>=x+79&&mouseX<x+95&&mouseY>=y+imageHeight-129&&mouseY<y+imageHeight-113)g.setTooltipForNextFrame(font,preview,mouseX,mouseY);}
                GuiTextures.draw(g,"copper_progress_empty",x+106,y+imageHeight-129,24,16);GuiTextures.crop(g,"copper_progress_full",x+106,y+imageHeight-129,0,0,24*Math.clamp(menu.data.get(0),0,40)/40,16,24,16);
                g.text(font,Component.literal(font.plainSubstrByWidth((menu.data.get(13)>0?Component.translatable("tool.smog.working_short"):(io.github.kpaakeh.smog.copper.ToolAssembly.shapingMode(menu.container)||!io.github.kpaakeh.smog.copper.MoldMaking.preview(menu.container).isEmpty())&&!preview.isEmpty()?preview.getHoverName():Component.translatable("tool.smog.template."+io.github.kpaakeh.smog.copper.ToolDefinitions.at(menu.data.get(6)).id())).getString(),120)),x+47,y+imageHeight-106,0xFF404040,false);return;}
            if(kind==6){g.text(font,Component.literal(menu.data.get(19)+" / 8000 mB"),x+7,y+20,0xFF404040,false);return;}
            GuiTextures.draw(g,"copper_progress_empty",x+99,y+35,24,16);
            int work=24*Math.clamp(menu.data.get(0),0,Math.max(1,menu.data.get(1)))/Math.max(1,menu.data.get(1));
            GuiTextures.crop(g,"copper_progress_full",x+99,y+35,0,0,work,16,24,16);
            if(menu.data.get(2)>0)GuiTextures.draw(g,"copper_lit_progress",x+49,y+36,14,14);
            if(kind==5||kind==6)g.text(font,Component.literal(menu.data.get(19)+" / 8000 mB"),x+7,y+20,0xFF404040,false);
            else if(kind==2)g.text(font,net.minecraft.world.item.Item.byId(menu.data.get(12)).getDefaultInstance().getHoverName(),x+7,y+20,0xFF404040,false);
            else if(kind==1)g.text(font,Component.translatable("conductor.smog."+io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES[Math.floorMod(menu.data.get(6),io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES.length)]),x+7,y+20,0xFF404040,false);
            else g.text(font,Component.translatable("gui.smog.copper.heat",menu.data.get(2),menu.data.get(3)),x+7,y+20,0xFF404040,false);
            g.text(font,Component.translatable("gui.smog.copper.status_"+menu.data.get(7)),x+7,y+55,0xFF404040,false);
            if(kind==0){
                g.text(font,Component.literal("Cu "+menu.data.get(9)/4.0+" | Sn "+menu.data.get(10)/4.0),x+50,y+65,0xFF404040,false);
                g.text(font,Component.literal("Br "+menu.data.get(11)/4.0+" | "+menu.data.get(4)/4.0+"/"+menu.data.get(5)/4.0),x+50,y+75,0xFF404040,false);
            }else {
                g.text(font,Component.literal(menu.data.get(0)+" / "+menu.data.get(1)),x+75,y+70,0xFF404040,false);
            }
            return;
        }
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
    @Override public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event,boolean doubleClick){if(menu.layout.id.equals("smeltery")&&(event.button()==0||event.button()==1)){int action=SmelteryGui.click(menu,event.x(),event.y(),leftPos,topPos,event.button());if(action>=0){send(action);return true;}}return super.mouseClicked(event,doubleClick);}
    @Override protected void extractLabels(GuiGraphicsExtractor g,int mouseX,int mouseY){
        g.text(font,title,titleLabelX,titleLabelY,0xFF404040,false);g.text(font,playerInventoryTitle,inventoryLabelX,inventoryLabelY,0xFF404040,false);
    }
}
