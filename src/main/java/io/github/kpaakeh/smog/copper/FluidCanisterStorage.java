package io.github.kpaakeh.smog.copper;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantItemStorage;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/** Standard item fluid capability; arbitrary partial amounts are never rounded to buckets. */
public final class FluidCanisterStorage extends SingleVariantItemStorage<FluidVariant> {
    public static final int CAPACITY=8*81000;
    public FluidCanisterStorage(ContainerItemContext context){super(context);}
    private static net.minecraft.nbt.CompoundTag data(ItemVariant item){return item.toStack().getOrDefault(DataComponents.CUSTOM_DATA,CustomData.EMPTY).copyTag();}
    @Override protected FluidVariant getBlankResource(){return FluidVariant.blank();}
    @Override protected FluidVariant getResource(ItemVariant item){var data=data(item);if(data.getIntOr("smog_fluid_units",0)<=0)return FluidVariant.blank();var id=Identifier.tryParse(data.getStringOr("smog_fluid",""));return id!=null&&BuiltInRegistries.FLUID.containsKey(id)?FluidVariant.of(BuiltInRegistries.FLUID.getValue(id)):FluidVariant.blank();}
    @Override protected long getAmount(ItemVariant item){return Math.clamp(data(item).getIntOr("smog_fluid_units",0),0,CAPACITY);}
    @Override protected boolean canInsert(FluidVariant fluid){return fluid.equals(FluidVariant.of(fluid.getFluid()));}
    @Override protected long getCapacity(FluidVariant fluid){return CAPACITY;}
    @Override protected ItemVariant getUpdatedVariant(ItemVariant item,FluidVariant fluid,long amount){var stack=item.toStack();var data=data(item);data.remove("smog_fluid");data.remove("smog_fluid_units");if(amount>0&&!fluid.isBlank()){data.putString("smog_fluid",BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());data.putInt("smog_fluid_units",(int)amount);}if(data.isEmpty())stack.remove(DataComponents.CUSTOM_DATA);else stack.set(DataComponents.CUSTOM_DATA,CustomData.of(data));return ItemVariant.of(stack);}
    public static ItemStack filled(FluidVariant fluid,int amount){var stack=new ItemStack(BuiltInRegistries.ITEM.getValue(io.github.kpaakeh.smog.registry.ModBlocks.id("fluid_canister")));var data=new net.minecraft.nbt.CompoundTag();data.putString("smog_fluid",BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());data.putInt("smog_fluid_units",amount);stack.set(DataComponents.CUSTOM_DATA,CustomData.of(data));return stack;}
}
