package io.github.kpaakeh.smog.gui;

public final class FluidAmountText {
    private FluidAmountText(){}
    public static net.minecraft.network.chat.Component format(long units){return io.github.kpaakeh.smog.copper.FluidAmounts.format(units,com.mojang.blaze3d.platform.InputConstants.isKeyDown(com.mojang.blaze3d.platform.InputConstants.KEY_LSHIFT)||com.mojang.blaze3d.platform.InputConstants.isKeyDown(com.mojang.blaze3d.platform.InputConstants.KEY_RSHIFT));}
}
