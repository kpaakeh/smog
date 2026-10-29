package io.github.kpaakeh.smog.copper;

import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.multiblock.complex.ComplexMultiblocks;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

/** Native hopper-facing insertion view; every slot maps to the current controller, with no local buffer. */
public final class SmelteryItemPortBlock extends WorkshopPartBlock implements WorldlyContainerHolder {
    public SmelteryItemPortBlock(Properties properties){super(properties);}
    @Override public WorldlyContainer getContainer(BlockState state,LevelAccessor level,BlockPos pos){return new Input(level,pos.immutable());}
    private record Input(LevelAccessor level,BlockPos pos) implements WorldlyContainer {
        private MachineEntity owner(){return level instanceof ServerLevel server&&server.hasChunkAt(pos)&&server.getBlockState(pos).getBlock() instanceof SmelteryItemPortBlock?ComplexMultiblocks.owner(server,pos):null;}
        private boolean valid(MachineEntity m,int slot){return m!=null&&!m.isRemoved()&&Smeltery.handles(m)&&m.assembled()&&slot>=0&&slot<Smeltery.workers(m);}
        @Override public int getContainerSize(){var m=owner();return m!=null&&Smeltery.handles(m)?Smeltery.workers(m):0;}
        @Override public int getMaxStackSize(){return 1;}
        @Override public ItemStack getItem(int slot){var m=owner();return valid(m,slot)?m.getItem(Smeltery.slot(slot)):ItemStack.EMPTY;}
        @Override public boolean isEmpty(){for(int n=0;n<getContainerSize();n++)if(!getItem(n).isEmpty())return false;return true;}
        @Override public boolean canPlaceItem(int slot,ItemStack stack){var m=owner();return valid(m,slot)&&m.canPlaceItem(Smeltery.slot(slot),stack);}
        @Override public void setItem(int slot,ItemStack stack){var m=owner();if(valid(m,slot))m.setItem(Smeltery.slot(slot),stack);}
        @Override public ItemStack removeItem(int slot,int count){return ItemStack.EMPTY;}
        @Override public ItemStack removeItemNoUpdate(int slot){return ItemStack.EMPTY;}
        @Override public void clearContent(){}
        @Override public void setChanged(){var m=owner();if(m!=null)m.sync();}
        @Override public boolean stillValid(Player player){return owner()!=null&&player.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(pos))<64;}
        @Override public int[] getSlotsForFace(Direction side){return java.util.stream.IntStream.range(0,getContainerSize()).toArray();}
        @Override public boolean canPlaceItemThroughFace(int slot,ItemStack stack,Direction side){return canPlaceItem(slot,stack);}
        @Override public boolean canTakeItemThroughFace(int slot,ItemStack stack,Direction side){return false;}
    }
}
