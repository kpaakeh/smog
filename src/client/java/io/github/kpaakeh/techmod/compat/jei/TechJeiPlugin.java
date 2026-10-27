package io.github.kpaakeh.techmod.compat.jei;

import java.util.*;
import io.github.kpaakeh.techmod.machine.Machines;
import io.github.kpaakeh.techmod.multiblock.*;
import io.github.kpaakeh.techmod.registry.ModBlocks;
import mezz.jei.api.*;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

/** Optional JEI plugin. Large layers are paged into 7x7 tiles; no client classes leak to servers. */
@JeiPlugin
public final class TechJeiPlugin implements IModPlugin {
    public record Page(MultiblockDefinition structure, Block controller, int y, int x, int z) {}
    public static final IRecipeType<Page> TYPE = IRecipeType.create("tech_mod", "multiblocks", Page.class);
    @Override public Identifier getPluginUid() { return ModBlocks.id("jei"); }
    @Override public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new Category(registration.getJeiHelpers().getGuiHelper()));
    }
    @Override public void registerRecipes(IRecipeRegistration registration) {
        List<Page> pages = new ArrayList<>();
        for (var entry : Machines.all()) {
            var definition = entry.definition().multiblock();
            if (definition == null) continue;
            var structure = Multiblocks.get(definition);
            Set<BlockPos> positions = new HashSet<>(structure.parts().keySet()); positions.add(BlockPos.ZERO);
            int minX = positions.stream().mapToInt(BlockPos::getX).min().orElse(0);
            int maxX = positions.stream().mapToInt(BlockPos::getX).max().orElse(0);
            int minZ = positions.stream().mapToInt(BlockPos::getZ).min().orElse(0);
            int maxZ = positions.stream().mapToInt(BlockPos::getZ).max().orElse(0);
            for (int y : positions.stream().map(BlockPos::getY).distinct().sorted().toList())
                for (int z = minZ; z <= maxZ; z += 7) for (int x = minX; x <= maxX; x += 7)
                    pages.add(new Page(structure, entry.block(), y, x, z));
        }
        registration.addRecipes(TYPE, pages);
    }
    private static final class Category implements IRecipeCategory<Page> {
        private final IDrawable icon;
        private Category(IGuiHelper gui) { icon = gui.createDrawableItemLike(net.minecraft.world.level.block.Blocks.IRON_BLOCK); }
        @Override public IRecipeType<Page> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("jei.tech_mod.multiblocks"); }
        @Override public int getWidth() { return 170; }
        @Override public int getHeight() { return 176; }
        @Override public IDrawable getIcon() { return icon; }
        @Override public void setRecipe(IRecipeLayoutBuilder builder, Page page, IFocusGroup focuses) {
            builder.addOutputSlot(150, 2).add(page.controller());
            for (int z = 0; z < 7; z++) for (int x = 0; x < 7; x++) {
                BlockPos pos = new BlockPos(page.x + x, page.y, page.z + z);
                var ingredient = page.structure.parts().get(pos);
                if (pos.equals(BlockPos.ZERO)) builder.addInputSlot(x * 18 + 2, z * 18 + 46).setStandardSlotBackground().add(page.controller());
                else if (ingredient != null) builder.addInputSlot(x * 18 + 2, z * 18 + 46).setStandardSlotBackground()
                    .setSlotName(pos.toShortString()).addItemStacks(ingredient.displayItems());
            }
        }
        @Override public void createRecipeExtras(IRecipeExtrasBuilder builder, Page page, IFocusGroup focuses) {
            builder.addText(Component.literal(page.structure.id().getPath()), 140, 10);
            builder.addText(Component.literal("Y=" + page.y + "  X=" + page.x + ".." + (page.x+6)), 160, 10).setPosition(0, 14);
            builder.addText(Component.literal("Z=" + page.z + ".." + (page.z+6) + "  North ↑"), 160, 10).setPosition(0, 28);
        }
    }
}
