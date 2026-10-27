package io.github.kpaakeh.smog.machine;

import java.util.*;
import io.github.kpaakeh.smog.energy.*;
import io.github.kpaakeh.smog.gui.*;
import io.github.kpaakeh.smog.multiblock.*;
import net.minecraft.core.*;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.*;

/** Common persistent machine: inventory, energy, processing, menu and multiblock validation. */
public final class MachineEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private final Identifier machineId;
    private NonNullList<ItemStack> items=NonNullList.withSize(4,ItemStack.EMPTY);
    private int progress,energy,burnTicks,burnTotal;
    private boolean enabled=true;
    private Voltage transformedVoltage=Voltage.LV;
    public void cycleVoltage(){if(spec().kind()==MachineSpec.Kind.TRANSFORMER){transformedVoltage=Voltage.values()[(transformedVoltage.ordinal()+1)%Voltage.values().length];setChanged();}}
    private Item processingItem=Items.AIR;
    private final LinkedHashSet<String> strikes=new LinkedHashSet<>();
    public boolean recordStrike(String id) {
        if(!strikes.add(id))return false;
        if(strikes.size()>128)strikes.remove(strikes.iterator().next());setChanged();return true;
    }
    public MachineEntity(Identifier id,BlockPos pos,BlockState state) { super(Machines.get(id).entityType(),pos,state);machineId=id; }
    public Identifier machineId() { return machineId; }
    public MachineSpec spec() { return Machines.get(machineId).spec(); }
    public int progress() { return progress; }
    public void progress(int value) { progress=Math.max(0,value);setChanged(); }
    public int energy() { return energy; }
    public int capacity() { return spec().capacity(); }
    public void energy(int value) { energy=Math.clamp(value,0,capacity());setChanged(); }
    public int receive(int value) { int accepted=Math.min(Math.max(0,value),capacity()-energy);if(accepted>0)energy(energy+accepted);return accepted; }
    public boolean spend(int amount) { if(amount<0 || energy<amount)return false;energy(energy-amount);return true; }
    public boolean enabled() { return enabled; }
    public void enabled(boolean value) { enabled=value;setChanged(); }
    public boolean assembled() { return getBlockState().getValue(MachineBlock.ASSEMBLED); }
    public GeneratorKind generator() { return getBlockState().getValue(MachineBlock.GENERATOR); }
    public Voltage inputVoltage() { return spec().voltage(); }
    public Voltage outputVoltage() { return spec().kind()==MachineSpec.Kind.TRANSFORMER?transformedVoltage:spec().kind()==MachineSpec.Kind.GENERATOR_CONTROLLER?generator().voltage:spec().voltage(); }
    public MultiblockDefinition structure() {
        Identifier id=spec().kind()==MachineSpec.Kind.GENERATOR_CONTROLLER && generator()!=GeneratorKind.NONE?generator().structureId():Machines.get(machineId).definition().multiblock();
        return id==null?null:Multiblocks.get(id);
    }
    public Rotation rotation() {
        return switch(getBlockState().getValue(MachineBlock.FACING)) {
            case EAST->Rotation.CLOCKWISE_90;case SOUTH->Rotation.CLOCKWISE_180;case WEST->Rotation.COUNTERCLOCKWISE_90;default->Rotation.NONE;
        };
    }
    private boolean validate(ServerLevel server) {
        GeneratorKind chosen=GeneratorKind.NONE;
        boolean complete;
        if(spec().kind()==MachineSpec.Kind.GENERATOR_CONTROLLER) {
            for(var candidate:GeneratorKind.values()) {
                var structure=Multiblocks.get(candidate.structureId());
                if(candidate!=GeneratorKind.NONE && structure!=null && structure.check(server,worldPosition,rotation())==MultiblockDefinition.Result.COMPLETE) {chosen=candidate;break;}
            }
            complete=chosen!=GeneratorKind.NONE;
        } else {
            var structure=structure();complete=structure==null || structure.check(server,worldPosition,rotation())==MultiblockDefinition.Result.COMPLETE;
        }
        if(assembled()!=complete || generator()!=chosen)server.setBlock(worldPosition,getBlockState().setValue(MachineBlock.ASSEMBLED,complete).setValue(MachineBlock.GENERATOR,chosen),Block.UPDATE_CLIENTS);
        return complete;
    }
    public void serverTick() {
        if(!(level instanceof ServerLevel server))return;
        EnergyNetwork.track(this);
        if(!validate(server) || !enabled)return;
        switch(spec().kind()) {
            case ELECTRIC_FURNACE,CRUSHER,PRESS -> tickProcessing(server);
            case SOLID_GENERATOR -> tickFuel(server);
            case GENERATOR_CONTROLLER -> GeneratorLogic.tick(server,this);
            case BLOCK_BREAKER,BLOCK_PLACER,FLUID_PUMP,FLUID_PLACER -> tickWorld(server);
            case DECORATIVE -> {var def=Machines.get(machineId).definition();if(server.getGameTime()%def.interval()==0)def.action().tick(server,this);}
            default -> {}
        }
    }
    private void tickFuel(ServerLevel server) {
        if(energy>=capacity())return;
        if(burnTicks==0) {
            int duration=net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt.getFromItem(items.get(2),net.minecraft.core.component.DataComponents.COOKING_FUEL,
                net.minecraft.world.item.component.CookingFuel::burnTime,getLootContext(server),0);
            if(duration<=0)return;
            var remainder=items.get(2).getItem().getCraftingRemainder();
            // Containers with remainders must fit before fuel is consumed.
            ItemStack rest=remainder==null?ItemStack.EMPTY:remainder.create();
            if(!rest.isEmpty() && !canOutput(rest))return;
            items.get(2).shrink(1);if(!rest.isEmpty())output(rest);
            burnTicks=burnTotal=duration;setChanged();
        }
        burnTicks--;receive(16);setChanged();
    }
    private void tickProcessing(ServerLevel server) {
        ItemStack input=items.get(0),result=ItemStack.EMPTY;int inputCount=1;
        if(processingItem!=input.getItem()) { progress=0;processingItem=input.getItem();setChanged(); }
        if(spec().kind()==MachineSpec.Kind.ELECTRIC_FURNACE) {
            var recipe=server.recipeAccess().getRecipeFor(RecipeType.SMELTING,new SingleRecipeInput(input),server);
            if(recipe.isPresent())result=recipe.get().value().assemble(new SingleRecipeInput(input));
        } else {
            var recipe=ProcessingRecipes.find(spec().kind(),input);
            if(recipe!=null) {result=recipe.result();inputCount=recipe.inputCount();}
        }
        if(result.isEmpty()) {if(progress!=0)progress(0);return;}
        if(!canOutput(result) || !spend(spec().cost()))return;
        progress++;
        if(progress>=spec().workTicks()) {input.shrink(inputCount);output(result);progress=0;}
        setChanged();
    }
    private void tickWorld(ServerLevel server) {
        if(energy<spec().cost())return;
        if(++progress<spec().workTicks()) {setChanged();return;}
        progress=0;
        BlockPos target=worldPosition.relative(getBlockState().getValue(MachineBlock.FACING));
        if(!server.hasChunkAt(target) || server.isOutsideBuildHeight(target))return;
        boolean success=switch(spec().kind()) {
            case BLOCK_BREAKER -> breakBlock(server,target);
            case BLOCK_PLACER -> placeBlock(server,target);
            case FLUID_PUMP -> pump(server,target);
            case FLUID_PLACER -> placeFluid(server,target);
            default -> false;
        };
        if(success)spend(spec().cost());setChanged();
    }
    private boolean breakBlock(ServerLevel server,BlockPos target) {
        var state=server.getBlockState(target);
        if(state.isAir() || state.getDestroySpeed(server,target)<0 || !state.getFluidState().isEmpty())return false;
        var drops=Block.getDrops(state,server,target,server.getBlockEntity(target),null,Items.IRON_PICKAXE.getDefaultInstance());
        ItemStack combined=ItemStack.EMPTY;
        for(var drop:drops) {
            if(combined.isEmpty())combined=drop.copy();
            else if(ItemStack.isSameItemSameComponents(combined,drop))combined.grow(drop.getCount());else return false;
        }
        if(!combined.isEmpty() && !canOutput(combined))return false;
        if(!server.destroyBlock(target,false))return false;
        if(!combined.isEmpty())output(combined);return true;
    }
    private boolean placeBlock(ServerLevel server,BlockPos target) {
        if(!(items.get(0).getItem() instanceof BlockItem item) || !server.getBlockState(target).canBeReplaced())return false;
        return item.place(new DirectionalPlaceContext(server,target,getBlockState().getValue(MachineBlock.FACING),items.get(0),Direction.UP)).consumesAction();
    }
    private boolean pump(ServerLevel server,BlockPos target) {
        if(!items.get(0).is(Items.BUCKET))return false;
        var state=server.getBlockState(target);var fluid=state.getFluidState();
        if(!fluid.isSource() || !(state.getBlock() instanceof BucketPickup pickup))return false;
        var expected=fluid.getType().getBucket().getDefaultInstance();if(expected.isEmpty() || !canOutput(expected))return false;
        ItemStack filled=pickup.pickupBlock(null,server,target,state);if(filled.isEmpty())return false;
        items.get(0).shrink(1);output(filled);return true;
    }
    private boolean placeFluid(ServerLevel server,BlockPos target) {
        if(!(items.get(0).getItem() instanceof BucketItem bucket) || items.get(0).getItem() instanceof MobBucketItem)return false;
        if(!server.getBlockState(target).getFluidState().isEmpty() || !server.getBlockState(target).canBeReplaced())return false;
        if(!canOutput(Items.BUCKET.getDefaultInstance()) || !bucket.emptyContents(null,server,target,null))return false;
        items.get(0).shrink(1);output(Items.BUCKET.getDefaultInstance());return true;
    }
    public boolean canOutput(ItemStack result) {
        var output=items.get(3);
        return result.getCount()<=result.getMaxStackSize() && (output.isEmpty() || ItemStack.isSameItemSameComponents(output,result) && output.getCount()+result.getCount()<=output.getMaxStackSize());
    }
    private void output(ItemStack result) {if(items.get(3).isEmpty())items.set(3,result.copy());else items.get(3).grow(result.getCount());setChanged();}
    @Override protected Component getDefaultName() { return getBlockState().getBlock().getName(); }
    @Override protected NonNullList<ItemStack> getItems() { return items; }
    @Override protected void setItems(NonNullList<ItemStack> value) { items=value; }
    @Override public int getContainerSize() { return items.size(); }
    @Override public boolean canPlaceItem(int slot,ItemStack stack) {
        return spec().kind()==MachineSpec.Kind.SOLID_GENERATOR?slot==2&&stack.has(net.minecraft.core.component.DataComponents.COOKING_FUEL)
            :!spec().source()&&!spec().relay()&&slot==0;
    }
    @Override public int[] getSlotsForFace(Direction side) {return side==Direction.DOWN?new int[]{3}:spec().kind()==MachineSpec.Kind.SOLID_GENERATOR?new int[]{2}:new int[]{0};}
    @Override public boolean canPlaceItemThroughFace(int slot,ItemStack stack,Direction side) {return canPlaceItem(slot,stack);}
    @Override public boolean canTakeItemThroughFace(int slot,ItemStack stack,Direction side) {return slot==3;}
    public final ContainerData data=new ContainerData() {
        @Override public int getCount() {return MachineMenu.DATA_SIZE;}
        @Override public int get(int index) {return switch(index) {
            case 0->progress;case 1->spec().workTicks();case 2->energy&65535;case 3->energy>>>16;
            case 4->capacity()&65535;case 5->capacity()>>>16;case 6->burnTicks;case 7->burnTotal;
            case 8->enabled?1:0;case 9->outputVoltage().ordinal();case 10->generator().ordinal();case 11->spec().kind().ordinal();default->0;
        };}
        @Override public void set(int index,int value) {}
    };
    @Override protected AbstractContainerMenu createMenu(int id,Inventory inventory) {
        var layout=GuiLayouts.get(spec().kind()==MachineSpec.Kind.GENERATOR_CONTROLLER||spec().kind()==MachineSpec.Kind.TRANSFORMER?"environment":spec().source()?"generator":"processor");return new MachineMenu(layout,id,inventory,this,data);
    }
    @Override public void preRemoveSideEffects(BlockPos pos,BlockState state) {
        super.preRemoveSideEffects(pos,state);
        if(level instanceof ServerLevel server)Containers.dropContents(server,worldPosition,this);
    }
    @Override protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);ContainerHelper.saveAllItems(output,items);
        output.putInt("progress",progress);output.putInt("energy",energy);output.putInt("burn_ticks",burnTicks);output.putInt("burn_total",burnTotal);output.putBoolean("enabled",enabled);
        output.store("strikes",com.mojang.serialization.Codec.STRING.listOf(),List.copyOf(strikes));
        output.putInt("voltage",transformedVoltage.ordinal());
    }
    @Override protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);items=NonNullList.withSize(4,ItemStack.EMPTY);ContainerHelper.loadAllItems(input,items);
        progress=Math.max(0,input.getIntOr("progress",0));energy=Math.clamp(input.getIntOr("energy",0),0,capacity());
        burnTicks=Math.max(0,input.getIntOr("burn_ticks",0));burnTotal=Math.max(burnTicks,input.getIntOr("burn_total",0));enabled=input.getBooleanOr("enabled",true);
        processingItem=items.get(0).getItem();
        strikes.clear();input.read("strikes",com.mojang.serialization.Codec.STRING.listOf()).ifPresent(strikes::addAll);
        transformedVoltage=Voltage.values()[Math.clamp(input.getIntOr("voltage",0),0,Voltage.values().length-1)];
    }
}
