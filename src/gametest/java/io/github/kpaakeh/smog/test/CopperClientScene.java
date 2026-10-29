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
                int hotbar=0;
                for(String metal:java.util.List.of("copper","bronze"))for(String handle:java.util.List.of("wooden_handle","reinforced_handle","hollow_copper_handle")){
                    var tool=io.github.kpaakeh.smog.copper.ToolParts.assemble(new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item(metal+"_pickaxe_head")),new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item(handle)),new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item(metal+"_clamp")),new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item(handle.equals("wooden_handle")?"fiber_wrap":handle.equals("reinforced_handle")?"leather_wrap":"wool_wrap")));
                    player.getInventory().setItem(hotbar++,tool);
                }
                var mattock=io.github.kpaakeh.smog.copper.ToolParts.assemble(io.github.kpaakeh.smog.copper.ToolDefinitions.template("mattock"),java.util.List.of(new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("iron_pickaxe_head")),new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("bronze_axe_head")),new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("wooden_handle")),new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("cord_binding")),ItemStack.EMPTY));player.getInventory().setItem(6,mattock);
                player.getInventory().setItem(7,new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("handcart")));
                player.getInventory().setSelectedSlot(0);
                var cart=io.github.kpaakeh.smog.copper.CopperTransport.HANDCART.create(level,net.minecraft.world.entity.EntitySpawnReason.COMMAND);cart.setInitialPos(-3.5,100,-2.5);cart.setItem(53,new ItemStack(Items.BRICKS,64));level.addFreshEntity(cart);
                var craneOrigin=new BlockPos(8,102,4);Multiblocks.get(ModBlocks.id("loading_crane")).parts().forEach((p,ingredient)->level.setBlockAndUpdate(craneOrigin.offset(p),ingredient.block().defaultBlockState()));level.setBlockAndUpdate(craneOrigin,block("loading_crane").defaultBlockState());var crane=(MachineEntity)level.getBlockEntity(craneOrigin);if(!crane.tryAssemble())throw new IllegalStateException("Crane failed");level.setBlockAndUpdate(new BlockPos(8,100,4),Blocks.BARREL.defaultBlockState());level.setBlockAndUpdate(new BlockPos(8,100,6),Blocks.BARREL.defaultBlockState());
                var anchorA=new BlockPos(-9,104,5);var anchorB=new BlockPos(9,104,5);level.setBlockAndUpdate(anchorA,block("rope_anchor").defaultBlockState());level.setBlockAndUpdate(anchorB,block("rope_anchor").defaultBlockState());var firstAnchor=(MachineEntity)level.getBlockEntity(anchorA);var lastAnchor=(MachineEntity)level.getBlockEntity(anchorB);if(!io.github.kpaakeh.smog.copper.Ropeway.link(level,firstAnchor,lastAnchor))throw new IllegalStateException("Rope span failed");firstAnchor.setItem(0,new ItemStack(Items.BRICKS,32));firstAnchor.copper.queued=1;firstAnchor.sync();
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
                var benchPos=new BlockPos(0,100,-3);level.setBlockAndUpdate(benchPos,block("tool_workbench").defaultBlockState());var bench=(MachineEntity)level.getBlockEntity(benchPos);bench.copper.selection=io.github.kpaakeh.smog.copper.ToolDefinitions.index("hammer");
                bench.setItem(0,new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("bronze_hammer_head")));bench.setItem(1,new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("hollow_copper_handle")));bench.setItem(2,new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("bronze_clamp")));bench.setItem(4,new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("fiber_wrap")));bench.sync();
                level.setBlockAndUpdate(new BlockPos(-5,100,-1),block("water_tank").defaultBlockState());var tank=(MachineEntity)level.getBlockEntity(new BlockPos(-5,100,-1));tank.copper.processFluid=4*81000;tank.sync();
            });
            world.getServer().runCommand("tp @a 0.5 104 -5.5 0 35");context.waitTicks(40);context.waitFor(mc->io.github.kpaakeh.smog.render.ClientFormations.hidden(new BlockPos(-1,100,1)));
            context.getInput().lookAt(new BlockPos(0,101,1));context.waitTicks(20);context.takeScreenshot("copper-workshop-world");
            world.getServer().runCommand("tp @a 0.5 105 1.5 0 90");context.waitTicks(12);context.takeScreenshot("copper-hearth-liquid-overhead");
            world.getServer().runCommand("tp @a 0.5 104 -5.5 0 35");context.waitTicks(8);
            world.getServer().runCommand("tp @a 0.5 107 -9.5 0 20");context.waitTicks(8);context.getInput().lookAt(new BlockPos(0,103,4));context.waitTicks(10);context.takeScreenshot("copper-logistics-world");
            context.waitFor(mc->TestJeiProbe.ready());
            for(String machine:java.util.List.of("copper_hearth","casting_table","potter_table","ceramic_kiln","washing_trough","lever_press","tool_workbench")){
                context.runOnClient(mc->TestJeiProbe.showCopper(machine));context.waitTicks(6);context.takeScreenshot("copper-jei-"+machine);context.setScreen(()->null);
            }
            world.getServer().runOnServer(server->{var player=server.getPlayerList().getPlayers().getFirst();player.openMenu((MachineEntity)server.overworld().getBlockEntity(new BlockPos(5,100,0)));});
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(10);context.takeScreenshot("copper-press-gui");context.setScreen(()->null);
            world.getServer().runOnServer(server->{server.getPlayerList().getPlayers().getFirst().openMenu((MachineEntity)server.overworld().getBlockEntity(new BlockPos(0,100,-3)));});
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(10);context.takeScreenshot("copper-tool-workbench-gui");context.setScreen(()->null);
            world.getServer().runCommand("tp @a 0.5 102 -5.5 0 45");context.getInput().lookAt(new BlockPos(0,100,-3));context.waitTicks(12);context.takeScreenshot("copper-tool-workbench-world");
            context.setScreen(()->{var mc=net.minecraft.client.Minecraft.getInstance();return new net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen(mc.player,mc.level.enabledFeatures(),true);});
            context.runOnClient(mc->{var screen=(net.fabricmc.fabric.api.client.creativetab.v1.FabricCreativeModeInventoryScreen)mc.gui.screen();var tab=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id("copper_age"));screen.switchToPage(screen.getPage(tab));screen.setSelectedTab(tab);});context.waitTicks(10);context.takeScreenshot("copper-tools-inventory");context.setScreen(()->null);
            world.getServer().runCommand("tp @a -4.5 102 -3.5 0 35");context.getInput().lookAt(new BlockPos(-5,100,0));context.waitTicks(20);context.takeScreenshot("copper-washing-and-water");
        }
    }
}

