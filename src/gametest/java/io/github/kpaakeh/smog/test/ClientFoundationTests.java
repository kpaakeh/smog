package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.fluid.ModFluids;
import io.github.kpaakeh.smog.machine.MachineBlock;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import io.github.kpaakeh.smog.registry.ModBlocks;
import io.github.kpaakeh.smog.zone.*;
import net.fabricmc.fabric.api.client.gametest.v1.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/** Uses the game's test input layer; does not take over the desktop mouse or keyboard. */
public final class ClientFoundationTests implements FabricClientGameTest {
    @Override public void runTest(ClientGameTestContext context) {
        try (var world = context.worldBuilder().create()) {
            world.getServer().runCommand("fill -12 99 -12 12 99 12 minecraft:stone");
            world.getServer().runCommand("tp @a 0.5 100 -6.5 0 15");
            world.getServer().runOnServer(server -> {
                var level = server.overworld();
                server.getPlayerList().getPlayers().getFirst().setGameMode(GameType.CREATIVE);
                var origin = new BlockPos(0,100,0);
                level.setBlockAndUpdate(origin, BuiltInRegistries.BLOCK.getValue(ModBlocks.id("test_controller")).defaultBlockState());
                var casing = BuiltInRegistries.BLOCK.getValue(ModBlocks.id("machine_casing"));
                Multiblocks.get(ModBlocks.id("test_structure")).parts().forEach((p, ingredient) ->
                    level.setBlockAndUpdate(origin.offset(p), ingredient.value().equals("minecraft:air") ? Blocks.AIR.defaultBlockState() : casing.defaultBlockState()));
                level.setBlockAndUpdate(new BlockPos(-3,100,0), BuiltInRegistries.BLOCK.getValue(ModBlocks.id("beryllium_ore")).defaultBlockState());
                level.setBlockAndUpdate(new BlockPos(-4,100,0), BuiltInRegistries.BLOCK.getValue(ModBlocks.id("deepslate_beryllium_ore")).defaultBlockState());
                level.setBlockAndUpdate(new BlockPos(3,100,0), BuiltInRegistries.BLOCK.getValue(ModBlocks.id("test_machine")).defaultBlockState());
                int column=-6;
                for(var ore:io.github.kpaakeh.smog.registry.ModOres.all()) {
                    level.setBlockAndUpdate(new BlockPos(column,100,6),ore.stone().defaultBlockState());
                    level.setBlockAndUpdate(new BlockPos(column,101,6),ore.deepslate().defaultBlockState());
                    level.setBlockAndUpdate(new BlockPos(column,102,6),ore.rawBlock().defaultBlockState());
                    level.setBlockAndUpdate(new BlockPos(column,103,6),ore.storageBlock().defaultBlockState());
                    column+=2;
                }
                for (int x = 3; x <= 5; x++) for (int z = 2; z <= 4; z++)
                    if (x == 4 && z == 3) level.setBlockAndUpdate(new BlockPos(x,100,z), ModFluids.all().getFirst().block.defaultBlockState());
                    else level.setBlockAndUpdate(new BlockPos(x,100,z), Blocks.STONE.defaultBlockState());
            });
            context.waitFor(mc -> mc.level != null && mc.level.getBlockState(new BlockPos(0,100,0)).hasProperty(MachineBlock.ASSEMBLED)
                && mc.level.getBlockState(new BlockPos(0,100,0)).getValue(MachineBlock.ASSEMBLED));
            context.waitTicks(40);
            context.waitFor(mc -> io.github.kpaakeh.smog.render.ClientFormations.hidden(new BlockPos(1,100,0)));
            context.takeScreenshot("foundation-blocks-and-fluid");
            world.getServer().runCommand("tp @a 0.5 100 11.5 180 -12");
            context.getInput().lookAt(new BlockPos(0,102,6));context.waitTicks(20);
            context.takeScreenshot("material-gallery");
            world.getServer().runCommand("tp @a 4.5 101 1.5 0 45");
            context.waitFor(mc -> mc.level.getFluidState(new BlockPos(4,100,3)).getType() == ModFluids.all().getFirst().source);
            context.getInput().lookAt(new BlockPos(4,100,3));
            context.waitTicks(20);
            context.takeScreenshot("foundation-fluid");
            context.setScreen(() -> {
                var mc=net.minecraft.client.Minecraft.getInstance();
                return new net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen(mc.player,mc.level.enabledFeatures(),true);
            });
            for(String tab:java.util.List.of("machines","ores","blocks")) {
                context.runOnClient(mc -> {
                    var screen=(net.fabricmc.fabric.api.client.creativetab.v1.FabricCreativeModeInventoryScreen)mc.gui.screen();
                    var target=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id(tab));
                    screen.switchToPage(screen.getPage(target));
                    screen.setSelectedTab(target); // false also means this tab is already selected.
                });
                context.waitTicks(5);
                context.runOnClient(mc -> {
                    var screen=(net.fabricmc.fabric.api.client.creativetab.v1.FabricCreativeModeInventoryScreen)mc.gui.screen();
                    if(screen.getSelectedTab()!=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id(tab)))throw new IllegalStateException("Creative tab changed unexpectedly");
                });
                context.takeScreenshot("creative-"+tab);
            }
            context.setScreen(() -> null);
            if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("jei")) {
                context.waitFor(mc -> TestJeiProbe.ready());
                context.runOnClient(mc -> TestJeiProbe.showMultiblocks());
                context.waitTicks(10);
                context.takeScreenshot("foundation-jei-multiblock");
                context.setScreen(() -> null);
                context.waitTicks(5);
            }
            world.getServer().runOnServer(server -> {
                var player = server.getPlayerList().getPlayers().getFirst();
                ZoneData.get(server.overworld()).put(Zone.sphere(ModBlocks.id("infection"), player.position(), 5));
            });
            context.waitFor(mc -> mc.gameRenderer.getRequestedPostEffects().contains(net.minecraft.resources.Identifier.withDefaultNamespace("creeper")));
            context.waitTicks(20);
            context.takeScreenshot("foundation-zone-shader");
            world.getServer().runOnServer(server -> ZoneData.get(server.overworld()).clear());
            context.waitFor(mc -> !mc.gameRenderer.getRequestedPostEffects().contains(net.minecraft.resources.Identifier.withDefaultNamespace("creeper")));
            // A rebuilt structure must also revert on the client after a casing is removed.
            world.getServer().runOnServer(server -> server.overworld().setBlockAndUpdate(new BlockPos(1,100,0), Blocks.AIR.defaultBlockState()));
            context.waitFor(mc -> !mc.level.getBlockState(new BlockPos(0,100,0)).getValue(MachineBlock.ASSEMBLED));
            context.waitFor(mc -> !io.github.kpaakeh.smog.render.ClientFormations.hidden(new BlockPos(-1,100,0)));
            world.getServer().runCommand("tp @a 0.5 100 -6.5 0 15");
            context.waitTicks(20);context.takeScreenshot("multiblock-dismantled");
            IndustryClientScene.run(context,world);
        }
    }
}
