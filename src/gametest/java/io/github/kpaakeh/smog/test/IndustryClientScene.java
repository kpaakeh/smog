package io.github.kpaakeh.smog.test;

import java.util.List;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.energy.*;
import io.github.kpaakeh.smog.registry.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;

public final class IndustryClientScene {
    private static final BlockPos GENERATOR=new BlockPos(-9,100,-6),A=new BlockPos(-8,100,-6),B=new BlockPos(-3,100,-6),FURNACE=new BlockPos(-2,100,-6);
    public static void run(ClientGameTestContext context,TestSingleplayerContext world){
        world.getServer().runCommand("fill -20 99 -20 28 99 23 minecraft:stone");
        world.getServer().runOnServer(server->{
            var level=server.overworld();
            var names=List.of("electric_furnace","crusher","press","block_breaker","block_placer","fluid_pump","fluid_placer","solid_generator","transformer");
            for(int i=0;i<names.size();i++)level.setBlockAndUpdate(new BlockPos(-8+i*2,100,-2),BuiltInRegistries.BLOCK.getValue(ModBlocks.id(names.get(i))).defaultBlockState());
            level.setBlockAndUpdate(GENERATOR,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("solid_generator")).defaultBlockState());
            level.setBlockAndUpdate(A,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("connector_lv")).defaultBlockState());
            level.setBlockAndUpdate(B,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("connector_lv")).defaultBlockState());
            level.setBlockAndUpdate(FURNACE,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("electric_furnace")).defaultBlockState());
            ((MachineEntity)level.getBlockEntity(GENERATOR)).setItem(2,new ItemStack(Items.COAL,3));
            ((MachineEntity)level.getBlockEntity(FURNACE)).setItem(0,new ItemStack(ModOres.all().getFirst().raw(),10));
            WireData.get(level).add(new WireLink(A,B,ConductorMaterial.COPPER));
            for(var kind:GeneratorKind.values())if(kind!=GeneratorKind.NONE){
                var origin=new BlockPos(-14+(kind.ordinal()-1)*6,100,18);
                level.setBlockAndUpdate(origin,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("generator_controller")).defaultBlockState());
                io.github.kpaakeh.smog.multiblock.Multiblocks.get(kind.structureId()).parts().forEach((offset,ingredient)->level.setBlockAndUpdate(origin.offset(offset),ingredient.block().defaultBlockState()));
                if(!((MachineEntity)level.getBlockEntity(origin)).tryAssemble())throw new IllegalStateException("Generator fixture assembly failed");
            }
        });
        world.getServer().runCommand("tp @a -5.5 100 -12.5 0 12");
        context.getInput().lookAt(new BlockPos(-5,100,-6));
        context.waitFor(mc->!io.github.kpaakeh.smog.render.ClientWires.from(A).isEmpty());context.waitTicks(30);
        context.takeScreenshot("industry-catenary-wire");
        world.getServer().waitFor(server->((MachineEntity)server.overworld().getBlockEntity(FURNACE)).getItem(3).is(ModOres.all().getFirst().product()));
        world.getServer().runOnServer(server->server.getPlayerList().getPlayers().getFirst().openMenu((MachineEntity)server.overworld().getBlockEntity(FURNACE)));
        context.waitForScreen(io.github.kpaakeh.smog.gui.MachineScreen.class);context.waitTicks(10);
        context.runOnClient(mc->{
            var menu=(io.github.kpaakeh.smog.gui.MachineMenu)mc.player.containerMenu;
            if(menu.energy()<=0||menu.capacity()!=64000)throw new IllegalStateException("Energy GUI sync failed");
        });
        context.takeScreenshot("industry-machine-gui");
        world.getServer().runOnServer(server->server.getPlayerList().getPlayers().getFirst().closeContainer());
        context.waitFor(mc->mc.gui.screen()==null);
        world.getServer().runCommand("tp @a 4.5 108 -16.5 0 12");
        world.getServer().runOnServer(server->{var player=server.getPlayerList().getPlayers().getFirst();player.getAbilities().flying=true;player.onUpdateAbilities();});
        context.getInput().lookAt(new BlockPos(4,102,18));context.waitTicks(40);context.takeScreenshot("industry-generator-models");
    }
}
