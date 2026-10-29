package io.github.kpaakeh.smog.copper;

import java.util.*;
import com.google.gson.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.*;

/** A hollow alloying furnace with independent jobs, source-owned fluid selection and physical fuel tanks. */
public final class Smeltery {
    public static final int MAX_WORKERS=81,PAGE_SIZE=27,INVENTORY_SIZE=88;
    public static final int META=20,METALS=40,PROGRESS=296,STATUS=377;
    public static final int PREVIOUS=30,NEXT=31,MODE=32,SELECT=100,CURSOR_DRAIN=400,CURSOR_FUEL_IN=700,CURSOR_FUEL_OUT=701;
    public record Fuel(String fluid,int heat,int speed,int cost){}
    private static final Map<String,Fuel> FUELS=new LinkedHashMap<>();
    public static void initialize(JsonObject root){if(root.has("smeltery_fuels"))for(var v:root.getAsJsonArray("smeltery_fuels")){var o=v.getAsJsonObject();FUELS.put(o.get("fluid").getAsString(),new Fuel(o.get("fluid").getAsString(),o.get("heat").getAsInt(),o.get("speed").getAsInt(),o.get("cost").getAsInt()));}}
    public static java.util.List<Fuel> fuels(){return java.util.List.copyOf(FUELS.values());}
    public static Fuel fuel(FluidVariant fluid){return fluid.isBlank()||!fluid.equals(FluidVariant.of(fluid.getFluid()))?null:FUELS.get(BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());}
    public static boolean handles(MachineEntity m){return CopperWorkshops.workshop(m,"copper_hearth");}
    public static boolean fuelTank(MachineEntity m){return CopperWorkshops.workshop(m,"smeltery_fuel_tank");}
    public static boolean vessel(MachineEntity m){return CopperWorkshops.workshop(m,"casting_table")||CopperWorkshops.workshop(m,"casting_basin");}
    public static boolean related(MachineEntity m){return handles(m)||fuelTank(m)||vessel(m);}
    public static int slot(int worker){return worker==0?0:worker+7;}
    public static int worker(int slot){return slot==0?0:slot>=8&&slot<INVENTORY_SIZE?slot-7:-1;}
    public static int workers(MachineEntity m){return m.complexShape()==null?0:Math.clamp(m.complexShape().internalVolume(false),1,MAX_WORKERS);}
    public static int visibleWorkers(MachineEntity m){int count=workers(m);for(int n=count;n<MAX_WORKERS;n++)if(!m.getItem(slot(n)).isEmpty())count=n+1;return Math.max(1,count);}
    public static int selected(MachineEntity m){int index=Arrays.asList(MoltenMetalStorage.NAMES).indexOf(m.smeltery.selected);if(index>=0)return index;for(int k=0;k<MoltenMetalStorage.NAMES.length;k++)if(m.copper.metal(k)>0)return k;return 0;}
    public static int maximumHeat(MachineEntity m){var shape=m.complexShape();int shell=shape==null?12:shape.width()*shape.depth()+2*(shape.height()-1)*(shape.width()+shape.depth()-2);return m.complexRoles().getOrDefault("lining",0)>=Math.max(6,shell/2)?1350:1100;}
    public static List<MachineEntity> tanks(MachineEntity m){
        if(!handles(m)||!m.assembled()||m.formationParts()==null||!(m.getLevel() instanceof ServerLevel level))return List.of();
        var result=new ArrayList<MachineEntity>();
        for(var offset:m.formationParts().keySet()){var pos=m.getBlockPos().offset(offset.rotate(m.rotation()));if(level.hasChunkAt(pos)&&level.getBlockEntity(pos) instanceof MachineEntity tank&&fuelTank(tank)&&m.getBlockPos().equals(io.github.kpaakeh.smog.multiblock.complex.ComplexFormationData.get(level).owner(pos)))result.add(tank);}
        return result;
    }
    public static MachineEntity shownTank(MachineEntity m){var tanks=tanks(m);return tanks.stream().filter(t->t.getBlockPos().asLong()==m.smeltery.fuelViewTarget).findFirst().or(()->tanks.stream().filter(t->t.smeltery.fuelAmount>0).findFirst()).orElse(tanks.isEmpty()?null:tanks.getFirst());}
    public static boolean portable(ItemStack stack){return !stack.isEmpty()&&ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM)!=null;}
    public static void containers(MachineEntity m){
        if(!(m.getLevel() instanceof net.minecraft.server.level.ServerLevel level))return;
        for(int slot:new int[]{4,5})if(!m.getItem(slot).isEmpty()){var old=m.removeItemNoUpdate(slot);net.minecraft.world.Containers.dropItemStack(level,m.getBlockPos().getX()+.5,m.getBlockPos().getY()+.5,m.getBlockPos().getZ()+.5,old);m.sync();}
    }
    public static boolean cursor(MachineEntity m,net.minecraft.world.entity.player.Player player,net.minecraft.world.inventory.AbstractContainerMenu menu,int kind,boolean empty,boolean fuel){
        var item=ContainerItemContext.ofPlayerCursor(player,menu).find(FluidStorage.ITEM);if(item==null)return false;var tank=shownTank(m);Storage<FluidVariant> storage=fuel?(tank==null?null:tank.smelteryFuel):m.molten;if(storage==null)return false;
        try(var tx=Transaction.openOuter()){long moved=empty?StorageUtil.move(item,storage,v->true,Long.MAX_VALUE,tx):StorageUtil.move(storage,item,v->fuel||MoltenMetalStorage.kind(v)==kind,Long.MAX_VALUE,tx);if(moved==0)return false;tx.commit();menu.broadcastChanges();return true;}
    }
    private static void heat(ServerLevel level,MachineEntity m,int active){
        var state=m.smeltery;state.fuelActive=false;state.fuelHeat=state.fuelViewAmount=state.fuelViewType=0;state.fuelSpeed=1;
        var tanks=tanks(m);var shown=tanks.stream().filter(t->t.smeltery.fuelAmount>0).findFirst().orElse(null);if(shown!=null){state.fuelViewTarget=shown.getBlockPos().asLong();state.fuelViewAmount=shown.smeltery.fuelAmount;state.fuelViewType=BuiltInRegistries.FLUID.getId(shown.smelteryFuel.resource().getFluid());var info=fuel(shown.smelteryFuel.resource());if(info!=null)state.fuelHeat=Math.min(maximumHeat(m),info.heat());}
        boolean demand=active>0||CopperWorkshops.canAlloy(m);int required=Alloying.requiredHeat(m);boolean lowDemand=Alloying.possible(m,1100);
        for(int n=0;n<workers(m);n++){var recipe=CopperWorkshops.melt(m.getItem(slot(n)));if(recipe!=null&&MetalProcessing.compatible(m,recipe[0])){int need=CopperWorkshops.meltingHeat(recipe[0]);required=Math.max(required,need);if(need<=1100)lowDemand=true;}}
        boolean firebox=m.formationParts()!=null&&m.formationParts().keySet().stream().anyMatch(offset->level.getBlockState(m.getBlockPos().offset(offset.rotate(m.rotation()))).is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("solid_firebox"))));
        if(!firebox)m.copper.burn=0;
        if(firebox&&m.copper.burn>0){m.copper.burn=Math.max(0,m.copper.burn-(1+Math.max(0,workers(m)-1)/9));state.fuelActive=true;state.fuelHeat=1100;}
        else if(firebox&&demand&&required<=1100){m.copper.burn=m.takeFuel();state.burnTotal=m.copper.burn;state.fuelActive=m.copper.burn>0;if(state.fuelActive)state.fuelHeat=1100;}
        if(demand&&(!state.fuelActive||state.fuelHeat<required)){
            var ordered=new ArrayList<>(tanks);final int minimum=required;ordered.sort(Comparator.comparingInt(t->{var info=fuel(t.smelteryFuel.resource());return info==null?2:Math.min(maximumHeat(m),info.heat())>=minimum?0:1;}));
            for(var tank:ordered){var info=fuel(tank.smelteryFuel.resource());if(info==null||state.fuelActive&&Math.min(maximumHeat(m),info.heat())<=state.fuelHeat)continue;int cost=info.cost()*(1+(workers(m)+8)/9)+active*2;if(tank.smeltery.fuelAmount<cost)continue;int type=BuiltInRegistries.FLUID.getId(tank.smelteryFuel.resource().getFluid());try(var tx=Transaction.openOuter()){if(tank.smelteryFuel.extract(tank.smelteryFuel.resource(),cost,tx)!=cost)continue;tx.commit();state.fuelActive=true;state.fuelHeat=Math.min(maximumHeat(m),info.heat());state.fuelSpeed=info.speed();state.fuelViewTarget=tank.getBlockPos().asLong();state.fuelViewAmount=tank.smeltery.fuelAmount;state.fuelViewType=type;break;}}
        }
        if(!state.fuelActive&&firebox&&demand&&lowDemand){m.copper.burn=m.takeFuel();state.burnTotal=m.copper.burn;if(m.copper.burn>0){state.fuelActive=true;state.fuelHeat=1100;}}
        if(!state.fuelActive&&firebox&&m.getItem(2).has(net.minecraft.core.component.DataComponents.COOKING_FUEL)&&state.fuelHeat==0)state.fuelHeat=1100;
        int target=state.fuelHeat;
        boolean onlyZinc=active>0;for(int n=0;n<workers(m);n++){var recipe=CopperWorkshops.melt(m.getItem(slot(n)));if(recipe!=null&&!MoltenMetalStorage.NAMES[recipe[0]].equals("zinc"))onlyZinc=false;}
        if(onlyZinc&&!CopperWorkshops.closed(m))target=Math.min(target,500);
        if(state.fuelActive)m.copper.heat=Math.min(maximumHeat(m),m.copper.heat<target?m.copper.heat+1+(m.copper.air>0?1:0):m.copper.heat>target?m.copper.heat-1:m.copper.heat);
        else if(level.getGameTime()%(20+Math.min(40,m.complexRoles().getOrDefault("lining",0)*2))==0)m.copper.heat=Math.max(0,m.copper.heat-1);
    }
    public static int duration(int units){return Math.max(40,(int)Math.min(20000,80L*Math.max(1,units)/MoltenMetalStorage.INGOT));}
    private static void worldInputs(ServerLevel level,MachineEntity m){
        if(level.getGameTime()%5!=0||m.complexShape()==null)return;var shape=m.complexShape();var bounds=shape.bounds();var inverse=switch(m.rotation()){case CLOCKWISE_90->net.minecraft.world.level.block.Rotation.COUNTERCLOCKWISE_90;case COUNTERCLOCKWISE_90->net.minecraft.world.level.block.Rotation.CLOCKWISE_90;default->m.rotation();};
        for(var entity:level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class,new net.minecraft.world.phys.AABB(m.getBlockPos()).inflate(11))){var local=entity.blockPosition().subtract(m.getBlockPos()).rotate(inverse);var stack=entity.getItem();if(local.getX()<=bounds.minX()||local.getX()>=bounds.maxX()||local.getZ()<1||local.getZ()>=shape.depth()-1||local.getY()<0||local.getY()>=shape.height()-1||CopperWorkshops.melt(stack)==null)continue;
            for(int n=0;n<workers(m)&&!stack.isEmpty();n++){int slot=slot(n);var old=m.getItem(slot);if(!old.isEmpty()&&!ItemStack.isSameItemSameComponents(old,stack))continue;int amount=Math.min(stack.getCount(),1-old.getCount());if(amount<=0)continue;if(old.isEmpty())m.setItem(slot,stack.copyWithCount(amount));else old.grow(amount);stack.shrink(amount);m.setChanged();}if(stack.isEmpty())entity.discard();}
    }
    public static void tick(ServerLevel level,MachineEntity m){
        worldInputs(level,m);
        int count=workers(m),active=0;for(int n=0;n<count;n++){var recipe=CopperWorkshops.melt(m.getItem(slot(n)));if(recipe!=null&&MetalProcessing.compatible(m,recipe[0])&&m.copper.total()+recipe[1]<=CopperWorkshops.capacity(m))active++;}
        heat(level,m,active);containers(m);m.copper.status=active==0?2:0;
        for(int n=0;n<count;n++){
            var input=m.getItem(slot(n));var recipe=CopperWorkshops.melt(input);String key=BuiltInRegistries.ITEM.getKey(input.getItem())+":"+input.getComponents().hashCode();if(!key.equals(m.smeltery.inputs[n])){m.smeltery.progress[n]=0;m.smeltery.inputs[n]=key;}
            if(recipe==null)continue;if(m.copper.total()+recipe[1]>CopperWorkshops.capacity(m)){m.copper.status=5;continue;}if(!MetalProcessing.safe(m,recipe[0])){m.copper.status=MetalProcessing.problem(m,recipe[0]);continue;}if(!m.smeltery.fuelActive){m.copper.status=m.smeltery.fuelHeat>0&&m.smeltery.fuelHeat<CopperWorkshops.meltingHeat(recipe[0])?4:3;continue;}if(m.copper.heat<CopperWorkshops.meltingHeat(recipe[0])||m.smeltery.fuelHeat<CopperWorkshops.meltingHeat(recipe[0])){m.copper.status=4;continue;}
            int total=duration(recipe[1]);m.smeltery.progress[n]=Math.min(total,m.smeltery.progress[n]+m.smeltery.fuelSpeed*(m.copper.air>0?2:1));
            if(m.smeltery.progress[n]>=total){if(m.copper.total()+recipe[1]>CopperWorkshops.capacity(m)){m.copper.status=5;continue;}m.copper.metal(recipe[0],m.copper.metal(recipe[0])+recipe[1]);input.shrink(1);m.smeltery.progress[n]=0;if(m.copper.air>0)m.copper.air--;}
        }
        m.copper.work=m.smeltery.progress[0];CopperWorkshops.alloy(m);m.setChanged();
    }
    private static int workerStatus(MachineEntity m,int worker){if(worker>=workers(m))return 11;var recipe=CopperWorkshops.melt(m.getItem(slot(worker)));if(recipe==null)return 2;if(!MetalProcessing.safe(m,recipe[0]))return MetalProcessing.problem(m,recipe[0]);if(m.copper.total()+recipe[1]>CopperWorkshops.capacity(m))return 5;if(!m.smeltery.fuelActive)return m.smeltery.fuelHeat>0&&m.smeltery.fuelHeat<CopperWorkshops.meltingHeat(recipe[0])?4:3;if(m.copper.heat<CopperWorkshops.meltingHeat(recipe[0])||m.smeltery.fuelHeat<CopperWorkshops.meltingHeat(recipe[0]))return 4;return 0;}
    public static int data(MachineEntity m,int index){
        if(index>=STATUS&&index<STATUS+MAX_WORKERS)return workerStatus(m,index-STATUS);
        if(index>=METALS&&index<METALS+MoltenMetalStorage.NAMES.length*2){int kind=(index-METALS)/2;return (index-METALS)%2==0?m.copper.metal(kind)&65535:m.copper.metal(kind)>>>16;}
        if(index>=PROGRESS&&index<PROGRESS+MAX_WORKERS){int n=index-PROGRESS;var recipe=CopperWorkshops.melt(m.getItem(slot(n)));return recipe==null?0:1000*m.smeltery.progress[n]/duration(recipe[1]);}
        return switch(index){case 20->CopperWorkshops.capacity(m)&65535;case 21->CopperWorkshops.capacity(m)>>>16;case 22->workers(m);case 23->m.smeltery.page;case 24->selected(m);case 25->m.smeltery.fuelViewAmount&65535;case 26->m.smeltery.fuelViewAmount>>>16;case 27->m.smeltery.fuelViewType;case 28->m.smeltery.fuelHeat;case 29->m.smeltery.containerMode;case 30->m.smeltery.fuelActive?1:0;case 33->visibleWorkers(m);case 34->m.smeltery.fuelSpeed;case 35->m.copper.burn;case 36->Math.max(m.copper.burn,m.smeltery.burnTotal);case 38->maximumHeat(m);default->0;};
    }
}
