package io.github.kpaakeh.smog.test.mixin;

import com.mojang.blaze3d.platform.Window;
import org.lwjgl.sdl.SDLVideo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

/** Test-only framebuffer capture: never show, focus or capture the user's desktop input. */
@Mixin(Window.class)
public abstract class HiddenReviewWindowMixin {
    @ModifyArg(method="createWindow",at=@At(value="INVOKE",target="Lcom/mojang/renderpearl/api/device/GpuBackend;createWindow(Ljava/lang/String;IIJ)J"),index=3)
    private long smog$hiddenAndUnfocusable(long flags){return flags|SDLVideo.SDL_WINDOW_HIDDEN|SDLVideo.SDL_WINDOW_NOT_FOCUSABLE;}
}
