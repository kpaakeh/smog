package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

/** Isolated copper workshop scene; no desktop input, no user world or saves. */
public final class CopperClientScene {
    private static Block block(String name){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name));}
    public static void run(ClientGameTestContext context){
        try(var world=context.worldBuilder().create()){
            world.getServer().runCommand("fill -12 99 -8 12 99 8 minecraft:stone");
            world.getServer().runCommand("time set noon");world.getServer().runCommand("weather clear");
            world.getServer().runCommand("tp @a 0.5 104 -7.5 0 25");
            world.getServer().runOnServer(server->{
                var level=server.overworld();var player=server.getPlayerList().getPlayers().getFirst();player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);player.getAbilities().flying=true;player.onUpdateAbilities();
                var origin=new BlockPos(0,100,0);
                var definition=Multiblocks.get(ModBlocks.id("copper_hearth"));
                definition.parts().forEach((p,ingredient)->level.setBlockAndUpdate(origin.offset(p),ingredient.block().defaultBlockState()));
                level.setBlockAndUpdate(origin,block("copper_hearth").defaultBlockState().setValue(MachineBlock.FACING,Direction.SOUTH));
                var hearth=(MachineEntity)level.getBlockEntity(origin);if(!hearth.tryAssemble())throw new IllegalStateException("Client scene hearth assembly failed");hearth.copper.copper=8*9000;hearth.copper.heat=1000;hearth.sync();
                for(String name:java.util.List.of("washing_trough","lever_press")){
                    var pos=new BlockPos(name.equals("washing_trough")?-5:5,100,0);
                    Multiblocks.get(ModBlocks.id(name)).parts().forEach((p,ingredient)->level.setBlockAndUpdate(pos.offset(p),ingredient.block().defaultBlockState()));level.setBlockAndUpdate(pos,block(name).defaultBlockState());
                    var machine=(MachineEntity)level.getBlockEntity(pos);if(!machine.tryAssemble())throw new IllegalStateException("Manual scene assembly failed");
                    machine.setItem(0,new ItemStack(name.equals("washing_trough")?Items.RAW_COPPER:Items.WHEAT_SEEDS,16));machine.copper.processFluid=name.equals("washing_trough")?162000:20250;machine.sync();
                }
                level.setBlockAndUpdate(new BlockPos(-5,100,-1),block("water_tank").defaultBlockState());var tank=(MachineEntity)level.getBlockEntity(new BlockPos(-5,100,-1));tank.copper.processFluid=4*81000;tank.sync();
            });
            world.getServer().runCommand("tp @a 0.5 104 -5.5 0 35");context.waitTicks(40);context.waitFor(mc->io.github.kpaakeh.smog.render.ClientFormations.hidden(new BlockPos(-1,100,1)));
            context.getInput().lookAt(new BlockPos(0,101,1));context.waitTicks(20);context.takeScreenshot("copper-workshop-world");
            world.getServer().runCommand("tp @a 0.5 105 1.5 0 90");context.waitTicks(12);context.takeScreenshot("copper-hearth-liquid-overhead");
            world.getServer().runCommand("tp @a 0.5 104 -5.5 0 35");context.waitTicks(8);
            context.waitFor(mc->TestJeiProbe.ready());
            for(String machine:java.util.List.of("copper_hearth","casting_table","potter_table","ceramic_kiln","washing_trough","lever_press")){
                context.runOnClient(mc->TestJeiProbe.showCopper(machine));context.waitTicks(6);context.takeScreenshot("copper-jei-"+machine);context.setScreen(()->null);
            }
            world.getServer().runOnServer(server->{var player=server.getPlayerList().getPlayers().getFirst();player.openMenu((MachineEntity)server.overworld().getBlockEntity(new BlockPos(5,100,0)));});
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(10);context.takeScreenshot("copper-press-gui");context.setScreen(()->null);
            world.getServer().runCommand("tp @a -4.5 102 -3.5 0 35");context.getInput().lookAt(new BlockPos(-5,100,0));context.waitTicks(20);context.takeScreenshot("copper-washing-and-water");
        }
    }
}

