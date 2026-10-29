package io.github.kpaakeh.smog.gui;

import io.github.kpaakeh.smog.machine.MachineEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public final class MachineMenu extends AbstractContainerMenu {
    public static final int DATA_SIZE=512;
    public final GuiLayouts.Layout layout;
    public final Container container;
    public final ContainerData data;
    private final int openingWorkers;
    private int viewPage;
    public MachineMenu(GuiLayouts.Layout layout,int id,Inventory inventory,Container container,ContainerData data) {
        super(layout.type,id);this.layout=layout;this.container=container;this.data=layout.id.equals("smeltery")?new ContainerData(){public int getCount(){return data.getCount();}public int get(int index){return index==23?viewPage:data.get(index);}public void set(int index,int value){if(index==23)viewPage=value;else data.set(index,value);}}:data;openingWorkers=container instanceof MachineEntity machine&&io.github.kpaakeh.smog.copper.Smeltery.handles(machine)?io.github.kpaakeh.smog.copper.Smeltery.workers(machine):-1;
        checkContainerSize(container,layout.inventorySize);checkContainerDataCount(data,DATA_SIZE);
        for(var position:layout.slots)addSlot(new Slot(container,position.index(),position.x(),position.y()) {
            @Override public int getMaxStackSize(){return layout.id.equals("smeltery")?1:super.getMaxStackSize();}
            @Override public int getMaxStackSize(ItemStack stack){return layout.id.equals("smeltery")?1:super.getMaxStackSize(stack);}
            @Override public boolean isActive(){if(!layout.id.equals("smeltery"))return true;int worker=io.github.kpaakeh.smog.copper.Smeltery.worker(position.index());return worker<0||worker/io.github.kpaakeh.smog.copper.Smeltery.PAGE_SIZE==MachineMenu.this.data.get(23)&&(worker<MachineMenu.this.data.get(22)||!container.getItem(position.index()).isEmpty());}
            @Override public boolean mayPickup(Player player){return !(container instanceof MachineEntity machine&&(io.github.kpaakeh.smog.copper.ToolAssembly.handles(machine)&&machine.copper.queued>0||position.index()==1&&io.github.kpaakeh.smog.copper.CastingProcesses.locked(machine)));}
            @Override public boolean mayPlace(ItemStack stack) { return isActive()&&!position.output() && container.canPlaceItem(position.index(),stack); }
        });
        addStandardInventorySlots(inventory,layout.id.equals("smeltery")?80:8,layout.height-82);addDataSlots(this.data);
    }
    public int energy() { return (data.get(2)&65535)|((data.get(3)&65535)<<16); }
    public int capacity() { return (data.get(4)&65535)|((data.get(5)&65535)<<16); }
    @Override public boolean stillValid(Player player) { return container.stillValid(player)&&(openingWorkers<0||container instanceof MachineEntity machine&&machine.assembled()&&io.github.kpaakeh.smog.copper.Smeltery.workers(machine)==openingWorkers); }
    @Override public boolean clickMenuButton(Player player,int id) {
        if(!stillValid(player) || !(container instanceof MachineEntity machine))return false;
        if(io.github.kpaakeh.smog.copper.Smeltery.handles(machine)){
            var state=machine.smeltery;int count=io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES.length;
            if(id==io.github.kpaakeh.smog.copper.Smeltery.PREVIOUS||id==io.github.kpaakeh.smog.copper.Smeltery.NEXT)viewPage=Math.floorMod(viewPage+(id==io.github.kpaakeh.smog.copper.Smeltery.NEXT?1:-1),Math.max(1,(io.github.kpaakeh.smog.copper.Smeltery.visibleWorkers(machine)+io.github.kpaakeh.smog.copper.Smeltery.PAGE_SIZE-1)/io.github.kpaakeh.smog.copper.Smeltery.PAGE_SIZE));
            else if(id==io.github.kpaakeh.smog.copper.Smeltery.MODE)state.containerMode=(state.containerMode+1)%3;
            else if(id>=io.github.kpaakeh.smog.copper.Smeltery.SELECT&&id<io.github.kpaakeh.smog.copper.Smeltery.SELECT+count){int kind=id-io.github.kpaakeh.smog.copper.Smeltery.SELECT;if(!io.github.kpaakeh.smog.copper.Smeltery.portable(getCarried())){if(machine.copper.metal(kind)<=0)return false;state.selected=io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES[kind];}else {boolean moved=state.containerMode!=2&&io.github.kpaakeh.smog.copper.Smeltery.cursor(machine,player,this,kind,true,false);}}
            else if(id==io.github.kpaakeh.smog.copper.Smeltery.CURSOR_FUEL_IN)io.github.kpaakeh.smog.copper.Smeltery.cursor(machine,player,this,0,true,true);
            else return false;machine.sync();broadcastChanges();return true;
        }
        if(machine.spec().kind()==io.github.kpaakeh.smog.machine.MachineSpec.Kind.COPPER_WORKSHOP && id>=20)return io.github.kpaakeh.smog.copper.CopperWorkshops.button(machine,id);
        if(id==0)machine.enabled(!machine.enabled());else if(id==1)machine.cycleTransferRate();else return false;
        broadcastChanges();return true;
    }
    @Override public ItemStack quickMoveStack(Player player,int index) {
        if(index<0 || index>=slots.size())return ItemStack.EMPTY;
        Slot slot=slots.get(index);if(!slot.isActive()||!slot.mayPickup(player)||!slot.hasItem())return ItemStack.EMPTY;
        ItemStack stack=slot.getItem(),original=stack.copy();int count=layout.slots.size();
        if(index<count) { if(!moveItemStackTo(stack,count,slots.size(),true))return ItemStack.EMPTY; }
        else if(!moveItemStackTo(stack,0,count,false))return ItemStack.EMPTY;
        if(stack.isEmpty())slot.set(ItemStack.EMPTY);else slot.setChanged();
        if(stack.getCount()==original.getCount())return ItemStack.EMPTY;
        slot.onTake(player,stack);return original;
    }
}
