package io.github.kpaakeh.smog.compat.jei;

import io.github.kpaakeh.smog.copper.Smeltery;
import io.github.kpaakeh.smog.registry.ModBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Physical tank fuels, plus the actual in-tank blazing fuel preparation. */
public final class SmelteryFuelCategory implements IRecipeCategory<SmelteryFuelCategory.Page> {
    public record Page(Smeltery.Fuel fuel,boolean preparation){}
    public static final IRecipeType<Page> TYPE=IRecipeType.create("smog","smeltery_fuels",Page.class);
    private final IDrawable icon;
    public SmelteryFuelCategory(IGuiHelper gui){icon=gui.createDrawableItemLike(Items.LAVA_BUCKET);}
    @Override public IRecipeType<Page> getRecipeType(){return TYPE;}
    @Override public Component getTitle(){return Component.translatable("jei.smog.smeltery_fuels");}
    @Override public int getWidth(){return 174;}
    @Override public int getHeight(){return 48;}
    @Override public IDrawable getIcon(){return icon;}
    @Override public void setRecipe(IRecipeLayoutBuilder b,Page page,IFocusGroup focuses){
        var fuel=BuiltInRegistries.FLUID.getValue(Identifier.parse(page.fuel().fluid()));
        b.addInputSlot(8,5).setBackground(new SmelteryTankDrawable(),-1,-1).addFluidStack(page.preparation()?net.minecraft.world.level.material.Fluids.LAVA:fuel,81000).setFluidRenderer(648000,false,16,30);
        if(page.preparation()){
            b.addInputSlot(40,12).setStandardSlotBackground().add(new ItemStack(Items.BLAZE_POWDER,2));
            b.addOutputSlot(140,5).setBackground(new SmelteryTankDrawable(),-1,-1).addFluidStack(fuel,81000).setFluidRenderer(648000,false,16,30);
        }
    }
    @Override public void createRecipeExtras(IRecipeExtrasBuilder b,Page page,IFocusGroup focuses){
        if(page.preparation()){b.addRecipeArrow().setPosition(82,12);b.addText(Component.translatable("jei.smog.fuel_preparation"),174,10).setPosition(2,37);}
        else b.addText(Component.translatable("jei.smog.fuel_heat",page.fuel().heat(),page.fuel().speed()),132,28).setPosition(36,9);
    }
}
