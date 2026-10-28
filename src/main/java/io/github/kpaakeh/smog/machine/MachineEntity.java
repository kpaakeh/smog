package io.github.kpaakeh.smog.machine;

import java.util.*;
import io.github.kpaakeh.smog.energy.*;
import io.github.kpaakeh.smog.gui.*;
import io.github.kpaakeh.smog.multiblock.*;
import io.github.kpaakeh.smog.multiblock.complex.*;
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
    private ComplexMultiblockDefinition.Shape complexShape;
    private ComplexMultiblockDefinition.Inspection complexInspection;
    private String assemblyDetail="";
    public final io.github.kpaakeh.smog.copper.CopperState copper=new io.github.kpaakeh.smog.copper.CopperState();
    public ComplexMultiblockDefinition complexDefinition(){return ComplexMultiblocks.get(machineId);}
    public ComplexMultiblockDefinition.Shape complexShape(){return complexShape;}
    public java.util.Map<String,Integer> complexRoles(){return complexInspection==null?java.util.Map.of():complexInspection.roles();}
    public String assemblyDetail(){return assemblyDetail;}
    public void sync(){setChanged();if(level instanceof ServerLevel)level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),Block.UPDATE_CLIENTS);}

    private final MachineEnergyStorage energyStorage=new MachineEnergyStorage(this);
    public MachineEnergyStorage energyStorage(){return energyStorage;}
    private NonNullList<ItemStack> items=NonNullList.withSize(4,ItemStack.EMPTY);
    private int progress,energy,burnTicks,burnTotal;
    private boolean enabled=true;
    private int outputLimit=32;
    public void cycleTransferRate(){if(spec().kind()==MachineSpec.Kind.TRANSFORMER){outputLimit=outputLimit>=spec().transferRate()?32:Math.min(spec().transferRate(),outputLimit*2);setChanged();}}
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
    public int inputRate() { return spec().transferRate(); }
    public int outputRate() { return spec().kind()==MachineSpec.Kind.TRANSFORMER?outputLimit:spec().kind()==MachineSpec.Kind.GENERATOR_CONTROLLER?generator().transferRate:spec().transferRate(); }
    public MultiblockDefinition structure() {
        var complex=complexDefinition();
        if(complex!=null)return complexShape==null||level==null?null:complex.visual(level,worldPosition,rotation(),complexShape);
        Identifier id=spec().kind()==MachineSpec.Kind.GENERATOR_CONTROLLER && generator()!=GeneratorKind.NONE?generator().structureId():Machines.get(machineId).definition().multiblock();
        return id==null?null:Multiblocks.get(id);
    }
    public Rotation rotation() {
        return switch(getBlockState().getValue(MachineBlock.FACING)) {
            case EAST->Rotation.CLOCKWISE_90;case SOUTH->Rotation.CLOCKWISE_180;case WEST->Rotation.COUNTERCLOCKWISE_90;default->Rotation.NONE;
        };
    }
    public boolean multiblockController() {
        return complexDefinition()!=null || spec().kind()==MachineSpec.Kind.GENERATOR_CONTROLLER || Machines.get(machineId).definition().multiblock()!=null;
    }
    /** Assembly is an explicit player action; ticks only validate the selected structure. */
    public boolean tryAssemble() {
        if(!(level instanceof ServerLevel server) || !multiblockController())return false;
        if(assembled())return validate(server);
        if(complexDefinition()!=null) {
            var found=complexDefinition().discover(server,worldPosition,rotation());
            assemblyDetail=found.detail();
            if(found.result()!=MultiblockDefinition.Result.COMPLETE)return false;
            if(!claim(server,found)){assemblyDetail="overlap";return false;}
            complexShape=found.shape();complexInspection=found;
            server.setBlock(worldPosition,getBlockState().setValue(MachineBlock.ASSEMBLED,true),Block.UPDATE_ALL);sync();return true;
        }
        GeneratorKind chosen=GeneratorKind.NONE;
        if(spec().kind()==MachineSpec.Kind.GENERATOR_CONTROLLER) {
            for(var candidate:GeneratorKind.values()) {
                if(candidate==GeneratorKind.NONE)continue;
                var definition=Multiblocks.get(candidate.structureId());
                if(definition!=null && definition.check(server,worldPosition,rotation())==MultiblockDefinition.Result.COMPLETE){chosen=candidate;break;}
            }
            if(chosen==GeneratorKind.NONE)return false;
        } else if(structure().check(server,worldPosition,rotation())!=MultiblockDefinition.Result.COMPLETE)return false;
        server.setBlock(worldPosition,getBlockState().setValue(MachineBlock.ASSEMBLED,true).setValue(MachineBlock.GENERATOR,chosen),Block.UPDATE_ALL);
        setChanged();return true;
    }
    private boolean claim(ServerLevel server,ComplexMultiblockDefinition.Inspection inspection) {
        java.util.List<BlockPos> parts=new java.util.ArrayList<>();parts.add(worldPosition);
        inspection.snapshot().parts().forEach((offset,ingredient)->{if(!ingredient.value().equals("minecraft:air"))parts.add(worldPosition.offset(offset.rotate(rotation())));});
        return ComplexFormationData.get(server).claim(worldPosition,parts);
    }
    public void disassemble() {
        if(!(level instanceof ServerLevel server) || !multiblockController())return;
        ComplexFormationData.get(server).release(worldPosition);complexShape=null;complexInspection=null;
        server.setBlock(worldPosition,getBlockState().setValue(MachineBlock.ASSEMBLED,false).setValue(MachineBlock.GENERATOR,GeneratorKind.NONE),Block.UPDATE_ALL);
        progress(0);sync();
    }
    private boolean validate(ServerLevel server) {
        if(!multiblockController()) {
            if(!assembled())server.setBlock(worldPosition,getBlockState().setValue(MachineBlock.ASSEMBLED,true),Block.UPDATE_CLIENTS);
            return true;
        }
        if(!assembled())return false;
        if(complexDefinition()!=null) {
            if(complexShape==null){disassemble();return false;}
            var bounds=complexShape.bounds();
            for(int x=bounds.minX();x<=bounds.maxX();x++)for(int z=0;z<complexShape.depth();z++)if(!server.hasChunkAt(worldPosition.offset(new BlockPos(x,0,z).rotate(rotation()))))return false;
            if(complexInspection!=null&&server.getGameTime()%20!=0)return true;
            var inspection=complexDefinition().inspect(server,worldPosition,rotation(),complexShape);
            assemblyDetail=inspection.detail();
            if(inspection.result()==MultiblockDefinition.Result.UNLOADED)return false;
            if(inspection.result()!=MultiblockDefinition.Result.COMPLETE||!claim(server,inspection)){disassemble();return false;}
            complexInspection=inspection;return true;
        }
        var definition=structure();
        // An unloaded structure pauses; a missing part dismantles it until configured again.
        var result=definition==null?MultiblockDefinition.Result.INCOMPLETE:definition.check(server,worldPosition,rotation());
        if(result==MultiblockDefinition.Result.INCOMPLETE)disassemble();
        return result==MultiblockDefinition.Result.COMPLETE;
    }
    public void serverTick() {
        if(!(level instanceof ServerLevel server))return;
        if(spec().kind()!=MachineSpec.Kind.COPPER_WORKSHOP)EnergyNetwork.track(this);
        if(!validate(server) || !enabled)return;
        switch(spec().kind()) {
            case ELECTRIC_FURNACE,CRUSHER,PRESS -> tickProcessing(server);
            case SOLID_GENERATOR -> tickFuel(server);
            case GENERATOR_CONTROLLER -> GeneratorLogic.tick(server,this);
            case BLOCK_BREAKER,BLOCK_PLACER,FLUID_PUMP,FLUID_PLACER -> tickWorld(server);
            case COPPER_WORKSHOP -> io.github.kpaakeh.smog.copper.CopperWorkshops.tick(server,this);
            case DECORATIVE -> {var def=Machines.get(machineId).definition();if(server.getGameTime()%def.interval()==0)def.action().tick(server,this);}
            default -> {}
        }
    }
    public int takeFuel() {
        if(!(level instanceof ServerLevel server))return 0;
        var fuel=items.get(2);
        int duration=net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt.getFromItem(fuel,net.minecraft.core.component.DataComponents.COOKING_FUEL,net.minecraft.world.item.component.CookingFuel::burnTime,getLootContext(server),0);
        if(duration<=0)return 0;
        var remainder=fuel.getItem().getCraftingRemainder();ItemStack rest=remainder==null?ItemStack.EMPTY:remainder.create();
        if(!rest.isEmpty()&&!canOutput(rest))return 0;
        fuel.shrink(1);if(!rest.isEmpty())output(rest);setChanged();return duration;
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
        if(spec().kind()==MachineSpec.Kind.COPPER_WORKSHOP)return io.github.kpaakeh.smog.copper.CopperWorkshops.accepts(this,slot,stack);
        return spec().kind()==MachineSpec.Kind.SOLID_GENERATOR?slot==2&&stack.has(net.minecraft.core.component.DataComponents.COOKING_FUEL)
            :!spec().source()&&!spec().relay()&&slot==0;
    }
    @Override public int[] getSlotsForFace(Direction side) {return spec().kind()==MachineSpec.Kind.COPPER_WORKSHOP?(side==Direction.DOWN?new int[]{3}:side==Direction.UP?new int[]{0}:new int[]{2}):side==Direction.DOWN?new int[]{3}:spec().kind()==MachineSpec.Kind.SOLID_GENERATOR?new int[]{2}:new int[]{0};}
    @Override public boolean canPlaceItemThroughFace(int slot,ItemStack stack,Direction side) {return canPlaceItem(slot,stack);}
    @Override public boolean canTakeItemThroughFace(int slot,ItemStack stack,Direction side) {return slot==3;}
    public final ContainerData data=new ContainerData() {
        @Override public int getCount() {return MachineMenu.DATA_SIZE;}
        @Override public int get(int index) {if(spec().kind()==MachineSpec.Kind.COPPER_WORKSHOP)return io.github.kpaakeh.smog.copper.CopperWorkshops.data(MachineEntity.this,index);return switch(index) {
            case 0->progress;case 1->spec().workTicks();case 2->energy&65535;case 3->energy>>>16;
            case 4->capacity()&65535;case 5->capacity()>>>16;case 6->burnTicks;case 7->burnTotal;
            case 8->enabled?1:0;case 9->outputRate();case 10->generator().ordinal();case 11->spec().kind().ordinal();default->0;
        };}
        @Override public void set(int index,int value) {}
    };
    @Override protected AbstractContainerMenu createMenu(int id,Inventory inventory) {
        var layout=GuiLayouts.get(spec().kind()==MachineSpec.Kind.COPPER_WORKSHOP?"copper_workshop":spec().kind()==MachineSpec.Kind.GENERATOR_CONTROLLER||spec().kind()==MachineSpec.Kind.TRANSFORMER?"environment":spec().source()?"generator":"processor");return new MachineMenu(layout,id,inventory,this,data);
    }
    @Override public void preRemoveSideEffects(BlockPos pos,BlockState state) {
        super.preRemoveSideEffects(pos,state);
        if(level instanceof ServerLevel server){ComplexFormationData.get(server).release(worldPosition);io.github.kpaakeh.smog.copper.CopperWorkshops.dropMetal(server,this);Containers.dropContents(server,worldPosition,this);}
    }
    @Override protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);ContainerHelper.saveAllItems(output,items);
        output.putInt("progress",progress);output.putInt("energy",energy);output.putInt("burn_ticks",burnTicks);output.putInt("burn_total",burnTotal);output.putBoolean("enabled",enabled);
        output.store("strikes",com.mojang.serialization.Codec.STRING.listOf(),List.copyOf(strikes));
        output.putInt("output_limit",outputLimit);copper.save(output);
        if(complexShape!=null){output.putInt("complex_width",complexShape.width());output.putInt("complex_depth",complexShape.depth());output.putInt("complex_height",complexShape.height());output.putInt("complex_left",complexShape.left());}
    }
    @Override public net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket getUpdatePacket(){return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);}
    @Override public net.minecraft.nbt.CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries){return saveCustomOnly(registries);}
    @Override protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);items=NonNullList.withSize(4,ItemStack.EMPTY);ContainerHelper.loadAllItems(input,items);
        progress=Math.max(0,input.getIntOr("progress",0));energy=Math.clamp(input.getIntOr("energy",0),0,capacity());
        burnTicks=Math.max(0,input.getIntOr("burn_ticks",0));burnTotal=Math.max(burnTicks,input.getIntOr("burn_total",0));enabled=input.getBooleanOr("enabled",true);
        processingItem=items.get(0).getItem();copper.load(input);
        complexInspection=null;complexShape=null;
        int width=input.getIntOr("complex_width",0);
        if(width>=3&&width<=9)try{complexShape=new ComplexMultiblockDefinition.Shape(width,input.getIntOr("complex_depth",3),input.getIntOr("complex_height",2),input.getIntOr("complex_left",1));}catch(IllegalArgumentException ignored){}
        strikes.clear();input.read("strikes",com.mojang.serialization.Codec.STRING.listOf()).ifPresent(strikes::addAll);
        outputLimit=Math.clamp(input.getIntOr("output_limit",new int[]{32,128,512}[Math.clamp(input.getIntOr("voltage",0),0,2)]),1,spec().transferRate());
    }
}
