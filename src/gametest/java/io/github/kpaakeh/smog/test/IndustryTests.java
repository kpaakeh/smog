package io.github.kpaakeh.smog.test;

import java.util.*;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.energy.*;
import io.github.kpaakeh.smog.registry.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class IndustryTests {
    private static MachineEntity place(GameTestHelper h,String name,BlockPos relative){
        var pos=h.absolutePos(relative);h.getLevel().setBlockAndUpdate(pos,BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name)).defaultBlockState());
        return (MachineEntity)h.getLevel().getBlockEntity(pos);
    }
    private static void ticks(MachineEntity machine,int ticks){for(int i=0;i<ticks;i++)machine.serverTick();}
    @GameTest public void materialsAndPoweredProcessing(GameTestHelper h){
        h.assertFalse(BuiltInRegistries.BLOCK.containsKey(ModBlocks.id("apatite_ore")),"Apatite removed");
        var family=ModOres.all().getFirst();
        h.assertTrue(family.shard()!=family.nugget(),"Ore shards and metal nuggets are distinct");
        h.assertFalse(h.getLevel().recipeAccess().byKey(ResourceKey.create(Registries.RECIPE,ModBlocks.id("crushed_"+family.name()+"_ore"))).isPresent(),"No hand crafting crushed ore");
        h.assertFalse(h.getLevel().recipeAccess().byKey(ResourceKey.create(Registries.RECIPE,ModBlocks.id(family.name()+"_shard"))).isPresent(),"No hand crafting shards");
        var furnace=place(h,"electric_furnace",new BlockPos(1,1,1));furnace.energy(10000);furnace.setItem(0,family.raw().getDefaultInstance());ticks(furnace,100);
        h.assertTrue(furnace.getItem(3).is(family.product()),"Electric furnace produces ingot");h.assertValueEqual(furnace.energy(),9600,"Furnace consumes exact energy");
        var crusher=place(h,"crusher",new BlockPos(3,1,1));crusher.energy(10000);crusher.setItem(0,family.raw().getDefaultInstance());ticks(crusher,60);
        h.assertTrue(crusher.getItem(3).is(family.crushed()),"Crusher produces crushed ore");
        crusher.setItem(0,crusher.removeItemNoUpdate(3));ticks(crusher,60);h.assertTrue(crusher.getItem(3).is(family.shard()),"Crusher produces ore shards");
        crusher.setItem(0,crusher.removeItemNoUpdate(3));ticks(crusher,60);h.assertTrue(crusher.getItem(3).is(family.dust()),"Crusher produces dust");
        var press=place(h,"press",new BlockPos(5,1,1));press.energy(10000);press.setItem(0,family.product().getDefaultInstance());ticks(press,80);
        h.assertTrue(press.getItem(3).is(family.plate()),"Press produces plate");
        furnace.setItem(0,family.raw().getDefaultInstance());furnace.setItem(3,new ItemStack(Items.DIAMOND,64));int energy=furnace.energy();ticks(furnace,110);
        h.assertValueEqual(furnace.energy(),energy,"Blocked output spends no energy");h.assertTrue(furnace.getItem(0).is(family.raw()),"Blocked output preserves input");
        var tag=crusher.saveWithFullMetadata(h.getLevel().registryAccess());var copy=(MachineEntity)BlockEntity.loadStatic(crusher.getBlockPos(),crusher.getBlockState(),tag,h.getLevel().registryAccess());
        h.assertValueEqual(copy.energy(),crusher.energy(),"Energy survives save/load");h.assertTrue(copy.getItem(3).is(family.dust()),"Inventory survives save/load");
        h.succeed();
    }
    @GameTest public void worldMachinesAndFuel(GameTestHelper h){
        var breaker=place(h,"block_breaker",new BlockPos(1,1,3));var target=breaker.getBlockPos().north();
        h.getLevel().setBlockAndUpdate(target,Blocks.STONE.defaultBlockState());breaker.energy(1000);ticks(breaker,20);
        h.assertTrue(h.getLevel().getBlockState(target).isAir()&&breaker.getItem(3).is(Items.COBBLESTONE),"Breaker collects block drop");
        h.getLevel().setBlockAndUpdate(target,Blocks.BEDROCK.defaultBlockState());int before=breaker.energy();ticks(breaker,20);
        h.assertTrue(h.getLevel().getBlockState(target).is(Blocks.BEDROCK)&&breaker.energy()==before,"Breaker respects unbreakable blocks");
        var placer=place(h,"block_placer",new BlockPos(3,1,3));placer.energy(1000);placer.setItem(0,new ItemStack(Items.DIRT,2));ticks(placer,20);
        h.assertTrue(h.getLevel().getBlockState(placer.getBlockPos().north()).is(Blocks.DIRT)&&placer.getItem(0).getCount()==1,"Placer consumes exactly one block");
        var pump=place(h,"fluid_pump",new BlockPos(5,1,3));pump.energy(1000);pump.setItem(0,new ItemStack(Items.BUCKET,2));h.getLevel().setBlockAndUpdate(pump.getBlockPos().north(),Blocks.WATER.defaultBlockState());ticks(pump,20);
        h.assertTrue(pump.getItem(3).is(Items.WATER_BUCKET)&&pump.getItem(0).getCount()==1,"Pump fills one bucket");h.assertTrue(h.getLevel().getFluidState(pump.getBlockPos().north()).isEmpty(),"Pump removes source");
        var fluid=place(h,"fluid_placer",new BlockPos(7,1,3));fluid.energy(1000);fluid.setItem(0,Items.WATER_BUCKET.getDefaultInstance());ticks(fluid,20);
        h.assertTrue(h.getLevel().getFluidState(fluid.getBlockPos().north()).isSource()&&fluid.getItem(3).is(Items.BUCKET),"Fluid placer returns empty bucket");
        var generator=place(h,"solid_generator",new BlockPos(1,1,6));generator.setItem(2,new ItemStack(Items.COAL,2));ticks(generator,10);
        h.assertValueEqual(generator.energy(),160,"Fuel creates energy");h.assertValueEqual(generator.getItem(2).getCount(),1,"One fuel consumed");
        generator.energy(generator.capacity());ticks(generator,10);h.assertValueEqual(generator.getItem(2).getCount(),1,"Full generator does not consume extra fuel");
        h.succeed();
    }
    @GameTest public void wiresRoutingAndVoltage(GameTestHelper h){
        var source=place(h,"solid_generator",new BlockPos(0,1,1));var a=place(h,"connector_lv",new BlockPos(1,1,1));
        var b=place(h,"connector_lv",new BlockPos(6,1,1));var sink=place(h,"electric_furnace",new BlockPos(7,1,1));
        for(var node:List.of(source,a,b,sink))node.serverTick();source.energy(1000);
        var link=new WireLink(a.getBlockPos(),b.getBlockPos(),Voltage.LV);var wires=WireData.get(h.getLevel());h.assertTrue(wires.add(link),"Wire added");
        h.assertFalse(wires.add(new WireLink(b.getBlockPos(),a.getBlockPos(),Voltage.LV)),"Reverse duplicate rejected");
        EnergyNetwork.tick(h.getLevel());h.assertValueEqual(sink.energy(),32,"LV wire rate");h.assertValueEqual(source.energy(),968,"Energy conserved");
        var encoded=WireLink.CODEC.encodeStart(com.mojang.serialization.JsonOps.INSTANCE,link).getOrThrow();h.assertValueEqual(WireLink.CODEC.parse(com.mojang.serialization.JsonOps.INSTANCE,encoded).getOrThrow(),link,"Wire persistence codec");
        var p=link.point(.5);h.assertTrue(p.y<(a.getBlockPos().getY()+1.02),"Wire sags smoothly");
        h.getLevel().destroyBlock(b.getBlockPos(),false);EnergyNetwork.tick(h.getLevel());h.assertFalse(wires.all().contains(link),"Broken endpoint disconnects wire");
        var low=place(h,"solid_generator",new BlockPos(1,1,5));var transformer=place(h,"transformer",new BlockPos(2,1,5));var high=place(h,"press",new BlockPos(3,1,5));
        for(var node:List.of(low,transformer,high))node.serverTick();low.energy(1000);EnergyNetwork.tick(h.getLevel());h.assertValueEqual(high.energy(),0,"Wrong voltage cannot power press");
        transformer.cycleVoltage();EnergyNetwork.tick(h.getLevel());h.assertValueEqual(high.energy(),32,"Transformer routes converted voltage without multiplying EU");
        h.succeed();
    }
    @GameTest public void sharedGeneratorController(GameTestHelper h){
        var level=h.getLevel();var origin=new BlockPos(h.absolutePos(new BlockPos(3,1,2)).getX(),100,h.absolutePos(new BlockPos(3,1,2)).getZ());
        for(var kind:GeneratorKind.values()){
            if(kind==GeneratorKind.NONE)continue;
            for(int x=-2;x<=2;x++)for(int y=-1;y<=6;y++)for(int z=-1;z<=4;z++)level.setBlockAndUpdate(origin.offset(x,y,z),Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(origin,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("generator_controller")).defaultBlockState());
            var machine=(MachineEntity)level.getBlockEntity(origin);var definition=io.github.kpaakeh.smog.multiblock.Multiblocks.get(kind.structureId());
            definition.parts().forEach((p,ingredient)->level.setBlockAndUpdate(origin.offset(p),ingredient.block().defaultBlockState()));machine.serverTick();
            h.assertTrue(machine.assembled()&&machine.generator()==kind,"Shared controller detects "+kind);
            machine.energy(0);
            if(kind==GeneratorKind.THERMAL || kind==GeneratorKind.STEAM)level.setBlockAndUpdate(origin.below(),Blocks.MAGMA_BLOCK.defaultBlockState());
            if(kind==GeneratorKind.STEAM)level.setBlockAndUpdate(origin.north(),Blocks.WATER.defaultBlockState());
            if(kind==GeneratorKind.GEOTHERMAL)level.setBlockAndUpdate(origin.below(),Blocks.LAVA.defaultBlockState());
            if(kind==GeneratorKind.FLOW)level.setBlockAndUpdate(origin.north(),Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL,4));
            if(kind!=GeneratorKind.LIGHTNING){machine.serverTick();h.assertTrue(machine.energy()>0,"Environmental production works for "+kind);}
            else {h.assertTrue(machine.recordStrike("test-bolt"),"New lightning accepted");h.assertFalse(machine.recordStrike("test-bolt"),"Same lightning not credited twice");}
            var broken=origin.offset(definition.parts().keySet().iterator().next());level.setBlockAndUpdate(broken,Blocks.AIR.defaultBlockState());machine.serverTick();
            h.assertFalse(machine.assembled(),"Dismantling stops "+kind);
        }
        h.succeed();
    }
}
