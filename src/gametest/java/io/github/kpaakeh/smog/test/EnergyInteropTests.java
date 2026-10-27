package io.github.kpaakeh.smog.test;

import java.util.*;
import io.github.kpaakeh.smog.energy.*;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

/** Third-party test storages expose only the public Energy API, never Smog's machine internals. */
public final class EnergyInteropTests {
    private static final Map<BlockPos,EnergyStorage> EXTERNAL=new HashMap<>();
    private static boolean registered;
    private static MachineEntity machine(GameTestHelper h,String name,int x,int y,int z) {
        var pos=h.absolutePos(new BlockPos(x,y,z));
        h.getLevel().setBlockAndUpdate(pos,BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name)).defaultBlockState());
        var machine=(MachineEntity)h.getLevel().getBlockEntity(pos);machine.serverTick();return machine;
    }
    private static SimpleEnergyStorage sink(GameTestHelper h,BlockPos pos,int capacity) {
        var storage=new SimpleEnergyStorage(capacity,Long.MAX_VALUE,0);
        EXTERNAL.put(pos,storage);h.getLevel().setBlockAndUpdate(pos,Blocks.IRON_BLOCK.defaultBlockState());return storage;
    }
    @GameTest public void eachConductorTransfersItsRatedAmount(GameTestHelper h) {
        if(!registered){EnergyStorage.SIDED.registerForBlocks((level,pos,state,entity,side)->EXTERNAL.get(pos),Blocks.IRON_BLOCK);registered=true;}
        try {
            for(var material:ConductorMaterial.values()) {
                int y=2+material.ordinal()*3;
                var source=machine(h,material.connectorId(),0,y,1);var end=machine(h,material.connectorId(),5,y,1);
                var receiver=sink(h,end.getBlockPos().east(),10000);
                WireData.get(h.getLevel()).add(new WireLink(source.getBlockPos(),end.getBlockPos(),material));
                try(var tx=Transaction.openOuter()) {h.assertValueEqual(source.energyStorage().insert(material.transfer,tx),(long)material.transfer,"Input accepts material rating "+material);tx.commit();}
                EnergyNetwork.tick(h.getLevel());
                h.assertValueEqual(receiver.getAmount(),(long)material.transfer,"Wire reaches rated throughput "+material);
                h.assertValueEqual(source.energy(),0,"No energy left or duplicated for "+material);
            }
            h.succeed();
        } finally {EXTERNAL.clear();}
    }
    @GameTest public void connectorSixFacePlacement(GameTestHelper h) {
        var level=h.getLevel();var support=h.absolutePos(new BlockPos(3,3,3));level.setBlockAndUpdate(support,Blocks.STONE.defaultBlockState());
        for(var material:ConductorMaterial.values())for(var face:Direction.values()) {
            var pos=support.relative(face);level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
            var item=(net.minecraft.world.item.BlockItem)BuiltInRegistries.ITEM.getValue(ModBlocks.id(material.connectorId()));
            var stack=new net.minecraft.world.item.ItemStack(item);
            var result=item.place(new net.minecraft.world.item.context.DirectionalPlaceContext(level,pos,Direction.NORTH,stack,face));
            h.assertTrue(result.consumesAction(),"Connector places on "+face+" "+material);
            h.assertValueEqual(level.getBlockState(pos).getValue(ConnectorBlock.ATTACHMENT),face,"Placement follows clicked support face");
            var anchor=WireLink.anchor(pos,face);var offset=anchor.subtract(net.minecraft.world.phys.Vec3.atCenterOf(pos));
            h.assertTrue(Math.abs(offset.dot(new net.minecraft.world.phys.Vec3(face.getStepX(),face.getStepY(),face.getStepZ()))-.52)<.00001,"Cable anchor is on rotated connector tip");
            level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
        }
        var first=support.west(2);var last=support.east(2);
        var wire=new WireLink(last,first,ConductorMaterial.COPPER,Direction.DOWN,Direction.NORTH);
        h.assertTrue(wire.point(0).distanceTo(WireLink.anchor(wire.a(),wire.aFace()))<.00001,"First wire end follows attachment");
        h.assertTrue(wire.point(1).distanceTo(WireLink.anchor(wire.b(),wire.bFace()))<.00001,"Second wire end follows attachment");
        var encoded=WireLink.CODEC.encodeStart(com.mojang.serialization.JsonOps.INSTANCE,wire).getOrThrow();
        h.assertValueEqual(WireLink.CODEC.parse(com.mojang.serialization.JsonOps.INSTANCE,encoded).getOrThrow(),wire,"Attachment directions persist through codec");
        var legacy=WireLink.CODEC.encodeStart(com.mojang.serialization.JsonOps.INSTANCE,new WireLink(first,last,ConductorMaterial.COPPER)).getOrThrow().getAsJsonObject();
        legacy.remove("a_face");legacy.remove("b_face");legacy.remove("material");legacy.addProperty("voltage","LV");
        var loaded=WireLink.CODEC.parse(com.mojang.serialization.JsonOps.INSTANCE,legacy).getOrThrow();
        h.assertValueEqual(loaded.aFace(),Direction.UP,"Legacy wire first tip remains upright");h.assertValueEqual(loaded.bFace(),Direction.UP,"Legacy wire second tip remains upright");
        h.succeed();
    }
    @GameTest public void publicApiTransactionsAndWireBridge(GameTestHelper h) {
        if(!registered){EnergyStorage.SIDED.registerForBlocks((level,pos,state,entity,side)->EXTERNAL.get(pos),Blocks.IRON_BLOCK);registered=true;}
        try {
            var source=machine(h,"solid_generator",0,1,1);source.energy(100);
            var tiny=sink(h,source.getBlockPos().east(),5);EnergyNetwork.tick(h.getLevel());
            h.assertValueEqual(tiny.getAmount(),5L,"Native generator pushes into a partial third-party sink");
            h.assertValueEqual(source.energy(),95,"Only accepted FE are deducted");
            EnergyNetwork.tick(h.getLevel());h.assertValueEqual(source.energy(),95,"Full third-party target loses no FE");

            var wired=machine(h,"solid_generator",0,1,4);var a=machine(h,"connector_lv",1,1,4);var b=machine(h,"connector_lv",6,1,4);
            var remote=sink(h,b.getBlockPos().east(),1000);
            WireData.get(h.getLevel()).add(new WireLink(a.getBlockPos(),b.getBlockPos(),ConductorMaterial.COPPER));wired.energy(100);
            EnergyNetwork.tick(h.getLevel());h.assertValueEqual(remote.getAmount(),32L,"Wires push to third-party sink at the remote connector");
            EnergyNetwork.tick(h.getLevel());h.assertValueEqual(remote.getAmount(),32L,"Multiple updates cannot bypass per-tick source limit");

            var furnace=machine(h,"electric_furnace",0,1,7);
            var north=EnergyStorage.SIDED.find(h.getLevel(),furnace.getBlockPos(),Direction.NORTH);
            var south=EnergyStorage.SIDED.find(h.getLevel(),furnace.getBlockPos(),Direction.SOUTH);
            h.assertTrue(north!=null&&south!=null,"Machines register sided Energy API lookup");
            try(var transaction=Transaction.openOuter()){h.assertValueEqual(north.insert(32,transaction),32L,"Simulation accepts FE");}
            h.assertValueEqual(furnace.energy(),0,"Aborted transaction restores the real buffer");
            var foreignGenerator=new SimpleEnergyStorage(1000,0,1000);foreignGenerator.amount=100;
            try(var transaction=Transaction.openOuter()){
                long sent=north.insert(32,transaction);h.assertValueEqual(foreignGenerator.extract(sent,transaction),sent,"External source pays for insertion");transaction.commit();
            }
            h.assertValueEqual(furnace.energy(),32,"Third-party source powers native consumer");
            h.assertValueEqual(foreignGenerator.getAmount(),68L,"Interop conserves FE");
            try(var transaction=Transaction.openOuter()){
                h.assertValueEqual(south.insert(32,transaction),0L,"All sides share a per-tick input limit");
                h.assertValueEqual(north.extract(32,transaction),0L,"Consumer cannot output energy");transaction.commit();
            }

            var ingress=machine(h,"connector_lv",0,6,1);var exit=machine(h,"connector_lv",5,6,1);var target=machine(h,"electric_furnace",6,6,1);
            WireData.get(h.getLevel()).add(new WireLink(ingress.getBlockPos(),exit.getBlockPos(),ConductorMaterial.COPPER));
            try(var transaction=Transaction.openOuter()) {
                var port=EnergyStorage.SIDED.find(h.getLevel(),ingress.getBlockPos(),Direction.WEST);
                h.assertValueEqual(port.insert(32,transaction),32L,"Connector accepts an external push");transaction.commit();
            }
            EnergyNetwork.tick(h.getLevel());h.assertValueEqual(target.energy(),32,"External input crosses native wire into a machine");
            h.assertValueEqual(ingress.energy(),0,"Relay does not duplicate incoming FE");
            h.succeed();
        } finally {EXTERNAL.clear();}
    }
}
