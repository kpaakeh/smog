package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.multiblock.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import io.github.kpaakeh.smog.render.ClientFormations;
import net.fabricmc.fabric.api.client.gametest.v1.context.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import com.mojang.blaze3d.vertex.VertexSorting;

/** Compile a real isolated section through Fabric/Indigo, not merely a visibility-set assertion. */
public final class FormationRenderingTest {
    private static final BlockPos ORIGIN=new BlockPos(8,148,8);
    private static void checkMesh(Minecraft mc,boolean formed) {
        var section=SectionPos.of(ORIGIN);var models=mc.getModelManager();
        var region=new RenderRegionCache().createRegion(mc.level,section.asLong());
        var compiler=new SectionCompiler(true,false,models.getBlockStateModelSet(),models.getFluidStateModelSet(),mc.getBlockColors());
        try(var buffers=new SectionBufferBuilderPack()) {
            var result=compiler.compile(section,region,VertexSorting.DISTANCE_TO_ORIGIN,buffers);
            try {
                if(result.renderedLayers.isEmpty()!=formed)throw new IllegalStateException("Terrain mesh visibility incorrect: formed="+formed+", layers="+result.renderedLayers.keySet());
                if(result.blockEntities.stream().noneMatch(be->be.getBlockPos().equals(ORIGIN)))throw new IllegalStateException("Controller renderer removed with terrain");
            } finally {result.release();}
        }
    }
    private static void configure(TestSingleplayerContext world) {
        world.getServer().runOnServer(server->{
            var player=server.getPlayerList().getPlayers().getFirst();
            var stack=new ItemStack(BuiltInRegistries.ITEM.getValue(ModBlocks.id("wrench")));
            player.setItemInHand(InteractionHand.MAIN_HAND,stack);
            player.gameMode.useItemOn(player,server.overworld(),stack,InteractionHand.MAIN_HAND,new BlockHitResult(Vec3.atCenterOf(ORIGIN),Direction.UP,ORIGIN,false));
        });
    }
    public static void run(ClientGameTestContext context,TestSingleplayerContext world) {
        var definition=Multiblocks.get(GeneratorKind.SOLAR.structureId());
        world.getServer().runOnServer(server->{
            var level=server.overworld();
            level.setBlockAndUpdate(ORIGIN,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("generator_controller")).defaultBlockState());
            definition.parts().forEach((offset,part)->level.setBlockAndUpdate(ORIGIN.offset(offset),part.block().defaultBlockState()));
            var player=server.getPlayerList().getPlayers().getFirst();player.getAbilities().flying=true;player.onUpdateAbilities();
        });
        world.getServer().runCommand("tp @a 8.5 148 2.5 0 0");
        context.getInput().lookAt(ORIGIN.above());context.waitTicks(30);
        context.waitFor(mc->mc.level.getBlockEntity(ORIGIN)!=null);
        context.runOnClient(mc->{if(mc.level.getBlockState(ORIGIN).getValue(MachineBlock.ASSEMBLED))throw new IllegalStateException("Pattern auto-assembled");checkMesh(mc,false);});
        context.takeScreenshot("solar-before-configurator");
        configure(world);
        context.waitFor(mc->ClientFormations.controller(ORIGIN));context.waitTicks(40);
        context.runOnClient(mc->checkMesh(mc,true));context.takeScreenshot("solar-only-unified-model");
        var offset=definition.parts().keySet().iterator().next();
        world.getServer().runOnServer(server->server.overworld().setBlockAndUpdate(ORIGIN.offset(offset),Blocks.AIR.defaultBlockState()));
        context.waitFor(mc->!ClientFormations.hidden(ORIGIN));context.waitTicks(20);
        context.runOnClient(mc->checkMesh(mc,false));context.takeScreenshot("solar-dismantled-parts-restored");
        world.getServer().runOnServer(server->server.overworld().setBlockAndUpdate(ORIGIN.offset(offset),definition.parts().get(offset).block().defaultBlockState()));
        context.waitTicks(20);
        context.runOnClient(mc->{if(ClientFormations.hidden(ORIGIN))throw new IllegalStateException("Repair auto-assembled");});
        configure(world);context.waitFor(mc->ClientFormations.controller(ORIGIN));context.waitTicks(30);
        context.runOnClient(mc->checkMesh(mc,true));
    }
}
