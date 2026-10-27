package io.github.kpaakeh.techmod;

import net.fabricmc.api.ClientModInitializer;

public class TechModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (var fluid : io.github.kpaakeh.techmod.fluid.ModFluids.all()) {
            var model = new net.minecraft.client.renderer.block.FluidModel.Unbaked(
                new net.minecraft.client.resources.model.sprite.Material(net.minecraft.resources.Identifier.withDefaultNamespace("block/water_still")),
                new net.minecraft.client.resources.model.sprite.Material(net.minecraft.resources.Identifier.withDefaultNamespace("block/water_flow")),
                new net.minecraft.client.resources.model.sprite.Material(net.minecraft.resources.Identifier.withDefaultNamespace("block/water_overlay")),
                net.minecraft.client.color.block.BlockTintSources.constant(0xFF000000 | fluid.color));
            net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry.register(fluid.source, fluid.flowing, model);
        }
    }
}
