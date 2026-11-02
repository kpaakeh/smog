package io.github.kpaakeh.smog;

import net.fabricmc.api.ClientModInitializer;

public class SmogClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        io.github.kpaakeh.smog.copper.ToolTooltip.viewerMode(()->{
            int mode=0;
            if(com.mojang.blaze3d.platform.InputConstants.isKeyDown(com.mojang.blaze3d.platform.InputConstants.KEY_LCONTROL)||com.mojang.blaze3d.platform.InputConstants.isKeyDown(com.mojang.blaze3d.platform.InputConstants.KEY_RCONTROL))mode|=io.github.kpaakeh.smog.copper.ToolTooltip.PROPERTIES;
            if(com.mojang.blaze3d.platform.InputConstants.isKeyDown(com.mojang.blaze3d.platform.InputConstants.KEY_LALT)||com.mojang.blaze3d.platform.InputConstants.isKeyDown(com.mojang.blaze3d.platform.InputConstants.KEY_RALT))mode|=io.github.kpaakeh.smog.copper.ToolTooltip.CONSTRUCTION;
            return mode;
        });
        net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback.EVENT.register((stack,context,flag,lines)->{
            if(stack.getItem() instanceof io.github.kpaakeh.smog.copper.ModularToolItem||stack.getItem() instanceof io.github.kpaakeh.smog.copper.ToolPartItem){
                var mc=net.minecraft.client.Minecraft.getInstance();var wrapped=io.github.kpaakeh.smog.gui.ToolTooltipLayout.wrap(lines,mc.font,Math.max(120,Math.min(260,mc.getWindow().getGuiScaledWidth()-24)));
                lines.clear();lines.addAll(wrapped);
            }
            if(stack.getItem() instanceof io.github.kpaakeh.smog.copper.WorkshopCanisterItem&&!lines.isEmpty()){
                var data=stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
                var ordinary=io.github.kpaakeh.smog.copper.FluidAmounts.format(data.getIntOr("smog_fluid_units",0),false).copy().append(" / ").append(io.github.kpaakeh.smog.copper.FluidAmounts.format(648000,false)).getString();
                for(int i=0;i<lines.size();i++)if(lines.get(i).getString().equals(ordinary))lines.set(i,io.github.kpaakeh.smog.gui.FluidAmountText.format(data.getIntOr("smog_fluid_units",0)).copy().append(" / ").append(io.github.kpaakeh.smog.gui.FluidAmountText.format(648000)));
            }
        });
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(io.github.kpaakeh.smog.copper.CopperTransport.HANDCART,io.github.kpaakeh.smog.render.HandcartRenderer::new);
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(io.github.kpaakeh.smog.copper.CopperTransport.ROPEWAY_RIDE,io.github.kpaakeh.smog.render.RopewayRideRenderer::new);
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
