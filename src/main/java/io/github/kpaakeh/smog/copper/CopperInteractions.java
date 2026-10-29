package io.github.kpaakeh.smog.copper;

import io.github.kpaakeh.smog.machine.*;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil;

/** Primary world interactions. Sneak on controller still opens the optional detailed menu. */
public final class CopperInteractions {
    private static String ingots(int amount){return String.format(java.util.Locale.ROOT,"%.2f",amount/9000.0);}
    private static boolean put(MachineEntity m,int slot,ItemStack held){
        if(held.isEmpty()||!m.canPlaceItem(slot,held))return false;var old=m.getItem(slot);
        if(!old.isEmpty()&&!ItemStack.isSameItemSameComponents(old,held))return false;
        int count=Math.min(held.getCount(),held.getMaxStackSize()-(old.isEmpty()?0:old.getCount()));if(count==0)return false;
        if(old.isEmpty())m.setItem(slot,held.split(count));else {old.grow(count);held.shrink(count);}m.sync();return true;
    }
    private static boolean take(MachineEntity m,Player player,int slot){if(m.getItem(slot).isEmpty())return false;var stack=m.removeItemNoUpdate(slot);player.getInventory().add(stack);if(!stack.isEmpty())Containers.dropItemStack(player.level(),player.getX(),player.getY(),player.getZ(),stack);m.sync();return true;}
    public static InteractionResult use(ServerLevel level,MachineEntity m,Player player,InteractionHand hand,BlockPos clicked){
        var held=player.getItemInHand(hand);
        if(Smeltery.fuelTank(m)){
            if(held.is(Items.BLAZE_POWDER)&&m.smelteryFuel.resource().equals(net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(net.minecraft.world.level.material.Fluids.LAVA))&&m.smeltery.fuelAmount>0){int powder=2*((m.smeltery.fuelAmount+80999)/81000);if(!player.isCreative()&&held.getCount()<powder){player.sendOverlayMessage(Component.translatable("message.smog.fuel_powder",powder));return InteractionResult.SUCCESS;}int amount=m.smeltery.fuelAmount;var target=net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(net.minecraft.core.registries.BuiltInRegistries.FLUID.getValue(io.github.kpaakeh.smog.registry.ModBlocks.id("blazing_fuel")));try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){if(m.smelteryFuel.extract(m.smelteryFuel.resource(),amount,tx)!=amount||m.smelteryFuel.insert(target,amount,tx)!=amount)return InteractionResult.FAIL;tx.commit();}if(!player.isCreative())held.shrink(powder);player.sendOverlayMessage(Component.translatable("message.smog.fuel_prepared"));return InteractionResult.SUCCESS;}

            if(held.isEmpty()){player.sendOverlayMessage(Component.translatable("message.smog.fuel_tank",m.smeltery.fuelAmount/81));return InteractionResult.SUCCESS;}
            return FluidStorageUtil.interactWithFluidStorage(m.smelteryFuel,player,hand)?InteractionResult.SUCCESS:InteractionResult.PASS;
        }
        if(Smeltery.handles(m)){
            if(!m.assembled()){player.sendOverlayMessage(Component.translatable("message.smog.multiblock_incomplete"));return InteractionResult.SUCCESS;}
            if(held.isEmpty()){player.openMenu(m);return InteractionResult.SUCCESS;}
            if(FluidStorageUtil.interactWithFluidStorage(m.molten,player,hand))return InteractionResult.SUCCESS;
            if(held.has(DataComponents.COOKING_FUEL)&&put(m,2,held))return InteractionResult.SUCCESS;
            if(CopperWorkshops.melt(held)!=null)for(int n=0;n<Smeltery.workers(m);n++)if(put(m,Smeltery.slot(n),held))return InteractionResult.SUCCESS;
            return InteractionResult.PASS;
        }
        if(Ropeway.handles(m)){
            if(held.getItem() instanceof RopeLinkItem)return InteractionResult.PASS;
            if(player.isShiftKeyDown()&&!held.isEmpty()){player.openMenu(m);return InteractionResult.SUCCESS;}
            if(!held.isEmpty()&&put(m,0,held))return InteractionResult.SUCCESS;
            if(held.isEmpty()){if(take(m,player,3))return InteractionResult.SUCCESS;if(player.isShiftKeyDown()){take(m,player,0);return InteractionResult.SUCCESS;}m.copper.queued=1;m.sync();player.sendOverlayMessage(Component.translatable("message.smog.rope_queued"));return InteractionResult.SUCCESS;}
        }
        if(LoadingCrane.handles(m)){
            if(!held.isEmpty())return InteractionResult.PASS;
            if(!m.assembled()){player.sendOverlayMessage(Component.translatable("message.smog.multiblock_incomplete"));return InteractionResult.SUCCESS;}
            if(player.isShiftKeyDown()){m.copper.selection=1-Math.floorMod(m.copper.selection,2);m.copper.work=0;m.copper.queued=0;m.sync();}
            else LoadingCrane.queue(m);
            player.sendOverlayMessage(Component.translatable(m.copper.selection%2==0?"message.smog.crane_forward":"message.smog.crane_reverse"));return InteractionResult.SUCCESS;
        }
        if(ToolAssembly.handles(m))return ToolAssembly.use(level,m,player,hand);
        if(player.isShiftKeyDown()&&!held.isEmpty()&&(WaterSupply.handles(m)||ManualWorkshops.handles(m))){player.openMenu(m);return InteractionResult.SUCCESS;}
        if(WaterSupply.handles(m)){
            if(FluidStorageUtil.interactWithFluidStorage(m.processFluid,player,hand))return InteractionResult.SUCCESS;
            if(CopperWorkshops.workshop(m,"hand_water_pump")&&held.isEmpty())player.sendOverlayMessage(Component.translatable(WaterSupply.prime(level,m)?"message.smog.pump_ready":"message.smog.pump_missing_water"));
            else player.sendOverlayMessage(Component.translatable("message.smog.water_supply",m.copper.processFluid/81));
            return InteractionResult.SUCCESS;
        }
        if(ManualWorkshops.handles(m)){
            if(!m.assembled()){player.sendOverlayMessage(Component.translatable("message.smog.multiblock_incomplete"));return InteractionResult.SUCCESS;}
            if(FluidStorageUtil.interactWithFluidStorage(m.processFluid,player,hand))return InteractionResult.SUCCESS;
            if(!m.processFluid.washing()&&CopperWorkshops.is(held,"smog:lubricating_compound")&&m.copper.air==0){if(!player.isCreative())held.shrink(1);m.copper.air=8;m.sync();player.sendOverlayMessage(Component.translatable("message.smog.lubricated"));return InteractionResult.SUCCESS;}
            if(!m.processFluid.washing()&&held.is(Items.GLASS_BOTTLE)&&m.copper.processFluid>=WorkshopFluidStorage.BUCKET/4){
                m.copper.processFluid-=WorkshopFluidStorage.BUCKET/4;if(!player.isCreative())held.shrink(1);
                var bottle=new ItemStack(net.minecraft.core.registries.BuiltInRegistries.ITEM.getValue(io.github.kpaakeh.smog.registry.ModBlocks.id("plant_oil_bottle")));player.getInventory().add(bottle);if(!bottle.isEmpty())Containers.dropItemStack(level,player.getX(),player.getY(),player.getZ(),bottle);m.sync();return InteractionResult.SUCCESS;
            }
            if(player.isShiftKeyDown()&&held.isEmpty()){take(m,player,0);m.copper.queued=0;m.copper.work=0;m.sync();return InteractionResult.SUCCESS;}
            if(!held.isEmpty()&&put(m,0,held))return InteractionResult.SUCCESS;
            if(held.isEmpty()){
                if(take(m,player,3))return InteractionResult.SUCCESS;
                int batches=ManualWorkshops.queue(m);player.sendOverlayMessage(batches>0?Component.translatable("message.smog.manual_queued",batches):Component.translatable("message.smog.manual_workshop",m.copper.processFluid/81,0));return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        if(player.isShiftKeyDown()&&!held.isEmpty()&&CopperWorkshops.workshop(m,"potter_table")){player.openMenu(m);return InteractionResult.SUCCESS;}
        if(player.isShiftKeyDown()&&held.isEmpty()){
            if(Smeltery.vessel(m)&&!CastingProcesses.locked(m)&&take(m,player,1)){m.copper.work=0;return InteractionResult.SUCCESS;}
            if(CopperWorkshops.workshop(m,"potter_table")){CopperWorkshops.button(m,21);player.sendOverlayMessage(Component.translatable("message.smog.mold_selected",Item.byId(CopperWorkshops.data(m,12)).getDefaultInstance().getHoverName()));m.sync();return InteractionResult.SUCCESS;}
            player.openMenu(m);return InteractionResult.SUCCESS;
        }
        if(CopperWorkshops.workshop(m,"hand_bellows")){boolean worked=CopperWorkshops.pump(level,m);player.sendOverlayMessage(Component.translatable(worked?"message.smog.bellows":"message.smog.bellows_disconnected"));return InteractionResult.SUCCESS;}
        if(CopperWorkshops.workshop(m,"copper_hearth")){
            if(FluidStorageUtil.interactWithFluidStorage(m.molten,player,hand))return InteractionResult.SUCCESS;
            if(held.has(DataComponents.COOKING_FUEL)&&put(m,2,held))return InteractionResult.SUCCESS;
            if(CopperWorkshops.melt(held)!=null&&put(m,0,held))return InteractionResult.SUCCESS;
            if(held.isEmpty()){var contents=Component.empty();for(int k=0;k<MoltenMetalStorage.NAMES.length;k++)if(m.copper.metal(k)>0){if(!contents.getString().isEmpty())contents.append(" · ");contents.append(Component.translatable("conductor.smog."+MoltenMetalStorage.NAMES[k])).append(": "+ingots(m.copper.metal(k)));}player.sendOverlayMessage(contents.append(" | "+m.copper.heat));return InteractionResult.SUCCESS;}
        }else if(Smeltery.vessel(m)){
            if(held.isEmpty()&&take(m,player,3))return InteractionResult.SUCCESS;
            if(CastingProcesses.locked(m)){player.sendOverlayMessage(Component.translatable("message.smog.casting_locked",m.smeltery.filled/81,m.smeltery.required/81));return InteractionResult.SUCCESS;}
            if(!CopperWorkshops.workshop(m,"casting_basin")&&(CopperWorkshops.mold(held)>=0||MoldMaking.pattern(held)!=null)){take(m,player,1);m.setItem(1,held.split(1));m.copper.work=0;m.sync();return InteractionResult.SUCCESS;}
            if(held.isEmpty()){take(m,player,3);return InteractionResult.SUCCESS;}
        }else if(CopperWorkshops.workshop(m,"potter_table")){
            if(!held.isEmpty()&&put(m,0,held)){CopperWorkshops.button(m,22);m.sync();return InteractionResult.SUCCESS;}
            if(held.isEmpty()){if(!take(m,player,3))player.sendOverlayMessage(Component.translatable("message.smog.mold_selected",Item.byId(CopperWorkshops.data(m,12)).getDefaultInstance().getHoverName()));return InteractionResult.SUCCESS;}
        }else if(CopperWorkshops.workshop(m,"ceramic_kiln")){
            if(held.has(DataComponents.COOKING_FUEL)&&put(m,2,held))return InteractionResult.SUCCESS;
            if(!held.isEmpty()&&put(m,0,held))return InteractionResult.SUCCESS;
            if(held.isEmpty()){if(!take(m,player,3))player.sendOverlayMessage(Component.translatable("gui.smog.copper.status_"+CopperWorkshops.data(m,7)));return InteractionResult.SUCCESS;}
        }
        return InteractionResult.PASS;
    }
}
