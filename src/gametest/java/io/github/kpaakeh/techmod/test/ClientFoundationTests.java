package io.github.kpaakeh.techmod.test;

import io.github.kpaakeh.techmod.fluid.ModFluids;
import io.github.kpaakeh.techmod.machine.MachineBlock;
import io.github.kpaakeh.techmod.multiblock.Multiblocks;
import io.github.kpaakeh.techmod.registry.ModBlocks;
import io.github.kpaakeh.techmod.zone.*;
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
            world.getServer().runCommand("fill -8 99 -8 8 99 8 minecraft:stone");
            world.getServer().runCommand("tp @a 0.5 100 -6.5 0 15");
            world.getServer().runOnServer(server -> {
                var level = server.overworld();
                server.getPlayerList().getPlayers().getFirst().setGameMode(GameType.CREATIVE);
                var origin = new BlockPos(0,100,0);
                level.setBlockAndUpdate(origin, BuiltInRegistries.BLOCK.getValue(ModBlocks.id("test_controller")).defaultBlockState());
                var casing = BuiltInRegistries.BLOCK.getValue(ModBlocks.id("machine_casing"));
                Multiblocks.get(ModBlocks.id("test_structure")).parts().forEach((p, ingredient) ->
                    level.setBlockAndUpdate(origin.offset(p), ingredient.value().equals("minecraft:air") ? Blocks.AIR.defaultBlockState() : casing.defaultBlockState()));
                level.setBlockAndUpdate(new BlockPos(-3,100,0), BuiltInRegistries.BLOCK.getValue(ModBlocks.id("test_ore")).defaultBlockState());
                level.setBlockAndUpdate(new BlockPos(-4,100,0), BuiltInRegistries.BLOCK.getValue(ModBlocks.id("deepslate_test_ore")).defaultBlockState());
                level.setBlockAndUpdate(new BlockPos(3,100,0), BuiltInRegistries.BLOCK.getValue(ModBlocks.id("test_machine")).defaultBlockState());
                for (int x = 3; x <= 5; x++) for (int z = 2; z <= 4; z++)
                    if (x == 4 && z == 3) level.setBlockAndUpdate(new BlockPos(x,100,z), ModFluids.all().getFirst().block.defaultBlockState());
                    else level.setBlockAndUpdate(new BlockPos(x,100,z), Blocks.STONE.defaultBlockState());
            });
            context.waitFor(mc -> mc.level != null && mc.level.getBlockState(new BlockPos(0,100,0)).hasProperty(MachineBlock.ASSEMBLED)
                && mc.level.getBlockState(new BlockPos(0,100,0)).getValue(MachineBlock.ASSEMBLED));
            context.waitTicks(40);
            context.takeScreenshot("foundation-blocks-and-fluid");
            world.getServer().runCommand("tp @a 4.5 101 1.5 0 45");
            context.waitFor(mc -> mc.level.getFluidState(new BlockPos(4,100,3)).getType() == ModFluids.all().getFirst().source);
            context.getInput().lookAt(new BlockPos(4,100,3));
            context.waitTicks(20);
            context.takeScreenshot("foundation-fluid");
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
        }
    }
}
