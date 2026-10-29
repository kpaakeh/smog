package io.github.kpaakeh.smog.gui;

import java.util.*;
import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/** Native pixel panel, a real common tank, paged input jobs and separate fuel indicator. */
public final class SmelteryGui {
    public static final int TX=84,TY=24,TW=96,TH=104;
    public record Layer(int metal,int y,int height,int amount){}
    public static int value(MachineMenu m,int index){return (m.data.get(index)&65535)|((m.data.get(index+1)&65535)<<16);}
    public static int amount(MachineMenu m,int kind){return value(m,Smeltery.METALS+kind*2);}
    public static List<Layer> layers(MachineMenu m){
        var order=new ArrayList<Integer>();int selected=m.data.get(24);if(selected>=0&&selected<MoltenMetalStorage.NAMES.length&&amount(m,selected)>0)order.add(selected);
        for(int kind=0;kind<MoltenMetalStorage.NAMES.length;kind++)if(kind!=selected&&amount(m,kind)>0)order.add(kind);
        long total=order.stream().mapToLong(k->amount(m,k)).sum();long denominator=Math.max(total,value(m,20));int filled=denominator==0?0:(int)Math.min(TH,Math.max(order.size(),TH*total/denominator));int bottom=TY+TH;long cumulative=0;
        var result=new ArrayList<Layer>();int allocated=0;
        for(int i=0;i<order.size();i++){int k=order.get(i);cumulative+=amount(m,k);int next=(i+1)+(total==0?0:(int)((filled-order.size())*cumulative/total));int h=next-allocated;bottom-=h;result.add(new Layer(k,bottom,h,amount(m,k)));allocated=next;}
        return result;
    }
    private static Component capacity(MachineMenu menu){long total=0;for(int k=0;k<MoltenMetalStorage.NAMES.length;k++)total+=amount(menu,k);return Component.translatable("gui.smog.smeltery.capacity",String.format(Locale.ROOT,"%.2f",total/9000.0),String.format(Locale.ROOT,"%.2f",value(menu,20)/9000.0));}
    public static String volume(int units){return String.format(Locale.ROOT,"%.2f mB · %.3f",units/81.0,units/9000.0);}
    private static String fuelName(MachineMenu menu){int id=menu.data.get(27);if(id==0)return "";for(var fluid:io.github.kpaakeh.smog.fluid.ModFluids.all())if(net.minecraft.core.registries.BuiltInRegistries.FLUID.getId(fluid.source)==id)return fluid.name;return "lava";}
    private static void tile(GuiGraphicsExtractor g,Identifier texture,int x,int y,int width,int height,int stripHeight){for(int yy=0;yy<height;yy+=16)for(int xx=0;xx<width;xx+=16)g.blit(RenderPipelines.GUI_TEXTURED,texture,x+xx,y+yy,0,0,Math.min(16,width-xx),Math.min(16,height-yy),16,stripHeight);}
    public static void draw(MachineMenu menu,Font font,GuiGraphicsExtractor g,int x,int y,int mouseX,int mouseY){
        g.blit(RenderPipelines.GUI_TEXTURED,ModBlocks.id("textures/gui/smeltery.png"),x,y,0,0,248,244,248,244);
        for(var slot:menu.slots)if(slot.container==menu.container&&slot.isActive())GuiTextures.draw(g,"slot",x+slot.x-1,y+slot.y-1,18,18);
        for(int row=0;row<3;row++)for(int col=0;col<9;col++)GuiTextures.draw(g,"slot",x+79+col*18,y+161+row*18,18,18);
        for(int col=0;col<9;col++)GuiTextures.draw(g,"slot",x+79+col*18,y+219,18,18);
        g.fill(x+TX-1,y+TY-1,x+TX+TW+1,y+TY+TH+1,0xFF373737);g.fill(x+TX,y+TY,x+TX+TW,y+TY+TH,0xFF20252A);
        for(var layer:layers(menu)){tile(g,ModBlocks.id("textures/block/molten_"+MoltenMetalStorage.NAMES[layer.metal()]+"_surface.png"),x+TX,y+layer.y(),TW,layer.height(),16);if(layer.metal()==menu.data.get(24))g.fill(x+TX-3,y+layer.y(),x+TX-1,y+layer.y()+layer.height(),0xFFF2E5AF);
            if(mouseX>=x+TX&&mouseX<x+TX+TW&&mouseY>=y+layer.y()&&mouseY<y+layer.y()+layer.height())g.setTooltipForNextFrame(font,List.of(Component.translatable("conductor.smog."+MoltenMetalStorage.NAMES[layer.metal()]),Component.translatable("gui.smog.smeltery.quantity",volume(layer.amount())),capacity(menu),Component.translatable("gui.smog.smeltery.select")),Optional.empty(),mouseX,mouseY);
        }
        if(mouseX>=x+TX&&mouseX<x+TX+TW&&mouseY>=y+TY&&mouseY<y+TY+TH&&layers(menu).stream().noneMatch(layer->mouseY>=y+layer.y()&&mouseY<y+layer.y()+layer.height()))g.setTooltipForNextFrame(capacity(menu),mouseX,mouseY);
        g.fill(x+223,y+23,x+237,y+100,0xFF373737);g.fill(x+224,y+24,x+236,y+99,0xFF20252A);int fuel=value(menu,25),height=(int)(75L*fuel/SmelteryFuelStorage.CAPACITY);
        if(fuel==0&&menu.data.get(35)>0)GuiTextures.draw(g,"copper_lit_progress",x+222,y+97,14,14);
        if(height>0)tile(g,fuelName(menu).equals("lava")?ModBlocks.id("textures/block/smeltery_fuel_surface.png"):ModBlocks.id("textures/block/blazing_fuel_surface.png"),x+224,y+99-height,12,height,16);
        if(mouseX>=x+223&&mouseX<x+237&&mouseY>=y+23&&mouseY<y+100)g.setTooltipForNextFrame(font,List.of(Component.translatable(fuelName(menu).isEmpty()?"gui.smog.smeltery.solid_fuel":fuelName(menu).equals("lava")?"block.minecraft.lava":"block.smog."+fuelName(menu)),Component.translatable("gui.smog.smeltery.fuel",fuel/81),Component.translatable("gui.smog.smeltery.temperature",menu.data.get(2),menu.data.get(28)),Component.translatable("gui.smog.smeltery.fuel_cursor")),Optional.empty(),mouseX,mouseY);
        g.text(font,Component.literal((menu.data.get(23)+1)+" / "+Math.max(1,(menu.data.get(33)+8)/9)),x+16,y+95,0xFF404040,false);
        g.text(font,Component.translatable("gui.smog.smeltery.inputs",menu.data.get(22)),x+7,y+16,0xFF404040,false);
        int page=menu.data.get(23);
        for(int n=page*9;n<Math.min(page*9+9,Smeltery.MAX_WORKERS);n++){int slot=Smeltery.slot(n);if(n>=menu.data.get(22)&&menu.container.getItem(slot).isEmpty())continue;int px=x+7+(n%3)*18,py=y+46+((n%9)/3)*22;g.fill(px,py,px+18,py+2,0xFF373737);g.fill(px,py,px+18*menu.data.get(Smeltery.PROGRESS+n)/1000,py+2,0xFF84AA55);int status=menu.data.get(Smeltery.STATUS+n);if(status!=0&&!menu.container.getItem(slot).isEmpty())g.fill(px+15,py,px+18,py+2,0xFFB05D49);if(mouseX>=px&&mouseX<px+18&&mouseY>=py&&mouseY<py+3)g.setTooltipForNextFrame(Component.translatable("gui.smog.copper.status_"+status),mouseX,mouseY);if(n>=menu.data.get(22))g.fill(px,py-18,px+18,py,0x66888888);}
        g.text(font,Component.literal(menu.data.get(2)+" / "+menu.data.get(28)),x+82,y+138,0xFF404040,false);
        if(mouseX>=x+79&&mouseX<x+181&&mouseY>=y+133&&mouseY<y+148)g.setTooltipForNextFrame(Component.translatable("gui.smog.copper.status_"+menu.data.get(7)),mouseX,mouseY);
    }
    public static int click(MachineMenu menu,double mouseX,double mouseY,int x,int y,int button){
        if(mouseX>=x+TX&&mouseX<x+TX+TW&&mouseY>=y+TY&&mouseY<y+TY+TH){for(var layer:layers(menu))if(mouseY>=y+layer.y()&&mouseY<y+layer.y()+layer.height())return (button==1?Smeltery.CURSOR_DRAIN:Smeltery.SELECT)+layer.metal();if(!menu.getCarried().isEmpty())return (button==1?Smeltery.CURSOR_DRAIN:Smeltery.SELECT)+Math.clamp(menu.data.get(24),0,MoltenMetalStorage.NAMES.length-1);}
        if(mouseX>=x+223&&mouseX<x+237&&mouseY>=y+23&&mouseY<y+100&&!menu.getCarried().isEmpty())return button==1?Smeltery.CURSOR_FUEL_OUT:Smeltery.CURSOR_FUEL_IN;
        return -1;
    }
}
