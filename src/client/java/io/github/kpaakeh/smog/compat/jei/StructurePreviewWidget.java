package io.github.kpaakeh.smog.compat.jei;

import java.util.*;
import io.github.kpaakeh.smog.machine.MachineBlock;
import io.github.kpaakeh.smog.render.*;
import mezz.jei.api.gui.inputs.*;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.*;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.core.*;
import net.minecraft.world.level.block.Block;
import org.joml.Matrix3x2f;
import com.mojang.blaze3d.platform.InputConstants;

public final class StructurePreviewWidget implements IRecipeWidget, IJeiInputHandler {
    private final SmogJeiPlugin.Page page;
    private final ScreenRectangle area = new ScreenRectangle(4,28,162,76);
    private float yaw=135, pitch=25, zoom=1;
    private boolean formed;
    public StructurePreviewWidget(SmogJeiPlugin.Page page) { this.page=page; }
    @Override public ScreenPosition getPosition() { return new ScreenPosition(0,0); }
    @Override public ScreenRectangle getScreenRectangle() { return area; }
    @Override public ScreenRectangle getArea() { return area; }
    @Override public boolean handleMouseDragged(double x,double y,InputConstants.Key key,double dx,double dy) {
        if (!area.containsPoint((int)x,(int)y) || !key.equals(InputConstants.Type.MOUSE.getOrCreate(InputConstants.MOUSE_BUTTON_LEFT))) return false;
        yaw=(yaw+(float)dx*1.2f)%360; pitch=Math.clamp(pitch+(float)dy, -80, 80); return true;
    }
    @Override public boolean handleMouseScrolled(double x,double y,double dx,double dy) {
        if (!area.containsPoint((int)x,(int)y)) return false;
        zoom=Math.clamp(zoom*(float)Math.pow(1.12,dy),.4f,2.5f);return true;
    }
    @Override public boolean handleInput(double x,double y,IJeiUserInput input) {
        if (!area.containsPoint((int)x,(int)y)) return false;
        if (input.getKey().equals(InputConstants.Type.MOUSE.getOrCreate(InputConstants.MOUSE_BUTTON_RIGHT))) {
            if (!input.isSimulate()) formed=!formed;return true;
        }
        if (input.getKey().equals(InputConstants.Type.MOUSE.getOrCreate(InputConstants.MOUSE_BUTTON_MIDDLE))) {
            if (!input.isSimulate()) { yaw=135;pitch=25;zoom=1; }return true;
        }
        return input.getKey().equals(InputConstants.Type.MOUSE.getOrCreate(InputConstants.MOUSE_BUTTON_LEFT));
    }
    @Override public void drawWidget(GuiGraphicsExtractor graphics,double mouseX,double mouseY) {
        io.github.kpaakeh.smog.gui.GuiTextures.draw(graphics,"structure_preview",4,28,162,76);
        var minecraft=Minecraft.getInstance();var resolver=new BlockModelResolver(minecraft.getModelManager());
        List<StructurePreviewState.Part> parts=new ArrayList<>();
        var display=BlockDisplayContext.create();
        boolean normalized=formed&&io.github.kpaakeh.smog.multiblock.complex.ComplexMultiblocks.get(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(page.controller()))==null;
        if (normalized) {
            var model=new BlockModelRenderState();resolver.update(model,page.previewState(true),display);
            parts.add(new StructurePreviewState.Part(BlockPos.ZERO,model));
        } else {
            var model=new BlockModelRenderState();resolver.update(model,page.controller().defaultBlockState(),display);
            parts.add(new StructurePreviewState.Part(BlockPos.ZERO,model));
            page.structure().parts().forEach((pos,ingredient)-> {
                Block block;
                if (ingredient.value().startsWith("#")) {
                    var iterator=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getTagOrEmpty(ingredient.tag()).iterator();
                    if (!iterator.hasNext()) return; block=iterator.next().value();
                } else block=ingredient.block();
                if (block.defaultBlockState().isAir()) return;
                var part=new BlockModelRenderState();resolver.update(part,block.defaultBlockState(),display);
                parts.add(new StructurePreviewState.Part(pos,part));
            });
        }
        var b=page.structure().bounds();float size=Math.max(b.width(),Math.max(b.height(),b.depth()));
        var pose=new Matrix3x2f(graphics.pose());var scissor=graphics.scissorStack.peek();
        graphics.guiRenderState.addPicturesInPictureState(new StructurePreviewState(List.copyOf(parts),b,normalized,yaw,pitch,4,28,166,104,
            54f/size*zoom,pose,scissor,PictureInPictureRenderState.getBounds(4,28,166,104,pose,scissor)));
        graphics.text(minecraft.font,net.minecraft.network.chat.Component.translatable(formed?"jei.smog.formed":"jei.smog.blueprint"),8,32,0xFFBDD9E5,false);
    }
}
