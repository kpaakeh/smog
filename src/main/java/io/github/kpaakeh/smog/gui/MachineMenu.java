package io.github.kpaakeh.smog.gui;

import io.github.kpaakeh.smog.machine.MachineEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public final class MachineMenu extends AbstractContainerMenu {
    public static final int DATA_SIZE=12;
    public final GuiLayouts.Layout layout;
    public final Container container;
    public final ContainerData data;
    public MachineMenu(GuiLayouts.Layout layout,int id,Inventory inventory,Container container,ContainerData data) {
        super(layout.type,id);this.layout=layout;this.container=container;this.data=data;
        checkContainerSize(container,layout.inventorySize);checkContainerDataCount(data,DATA_SIZE);
        for(var position:layout.slots)addSlot(new Slot(container,position.index(),position.x(),position.y()) {
            @Override public boolean mayPlace(ItemStack stack) { return !position.output() && container.canPlaceItem(position.index(),stack); }
        });
        addStandardInventorySlots(inventory,8,108);addDataSlots(data);
    }
    public int energy() { return (data.get(2)&65535)|((data.get(3)&65535)<<16); }
    public int capacity() { return (data.get(4)&65535)|((data.get(5)&65535)<<16); }
    @Override public boolean stillValid(Player player) { return container.stillValid(player); }
    @Override public boolean clickMenuButton(Player player,int id) {
        if(!stillValid(player) || !(container instanceof MachineEntity machine))return false;
        if(id==0)machine.enabled(!machine.enabled());else if(id==1)machine.cycleVoltage();else return false;
        broadcastChanges();return true;
    }
    @Override public ItemStack quickMoveStack(Player player,int index) {
        if(index<0 || index>=slots.size())return ItemStack.EMPTY;
        Slot slot=slots.get(index);if(!slot.hasItem())return ItemStack.EMPTY;
        ItemStack stack=slot.getItem(),original=stack.copy();int count=layout.slots.size();
        if(index<count) { if(!moveItemStackTo(stack,count,slots.size(),true))return ItemStack.EMPTY; }
        else if(!moveItemStackTo(stack,0,count,false))return ItemStack.EMPTY;
        if(stack.isEmpty())slot.set(ItemStack.EMPTY);else slot.setChanged();
        if(stack.getCount()==original.getCount())return ItemStack.EMPTY;
        slot.onTake(player,stack);return original;
    }
}
