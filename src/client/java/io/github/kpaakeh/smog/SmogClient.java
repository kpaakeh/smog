package io.github.kpaakeh.smog;

import net.fabricmc.api.ClientModInitializer;

public class SmogClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(io.github.kpaakeh.smog.copper.CopperTransport.HANDCART,io.github.kpaakeh.smog.render.HandcartRenderer::new);
        for(var layout:io.github.kpaakeh.smog.gui.GuiLayouts.all())
            net.minecraft.client.gui.screens.MenuScreens.register(layout.type,io.github.kpaakeh.smog.gui.MachineScreen::new);
        io.github.kpaakeh.smog.render.ClientWires.initialize();
        io.github.kpaakeh.smog.render.ClientFormations.initialize();
        for (var machine : io.github.kpaakeh.smog.machine.Machines.all())
            net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry.register(machine.entityType(), io.github.kpaakeh.smog.render.MachineRenderer::new);
        net.fabricmc.fabric.api.client.rendering.v1.PictureInPictureRendererRegistry.register(context -> new io.github.kpaakeh.smog.render.StructurePreviewRenderer());
        for (var fluid : io.github.kpaakeh.smog.fluid.ModFluids.all()) {
            var model = new net.minecraft.client.renderer.block.FluidModel.Unbaked(
                new net.minecraft.client.resources.model.sprite.Material(io.github.kpaakeh.smog.registry.ModBlocks.id("block/" + fluid.name + "_still")),
                new net.minecraft.client.resources.model.sprite.Material(io.github.kpaakeh.smog.registry.ModBlocks.id("block/" + fluid.name + "_flow")),
                new net.minecraft.client.resources.model.sprite.Material(io.github.kpaakeh.smog.registry.ModBlocks.id("block/" + fluid.name + "_overlay")),
                net.minecraft.client.color.block.BlockTintSources.constant(0xFFFFFFFF));
            net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry.register(fluid.source, fluid.flowing, model);
        }
    }
}
