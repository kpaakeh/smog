package io.github.kpaakeh.smog.compat.jei;

import io.github.kpaakeh.smog.copper.CopperWorkshops;
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

public final class CopperCategory implements IRecipeCategory<CopperWorkshops.RecipePreview> {
    public static final IRecipeType<CopperWorkshops.RecipePreview> TYPE=IRecipeType.create("smog","copper_workshop",CopperWorkshops.RecipePreview.class);
    private final IDrawable icon;
    public CopperCategory(IGuiHelper gui){icon=gui.createDrawableItemLike(net.minecraft.world.item.Items.COPPER_INGOT);}
    @Override public IRecipeType<CopperWorkshops.RecipePreview> getRecipeType(){return TYPE;}
    @Override public Component getTitle(){return Component.translatable("jei.smog.copper.title");}
    @Override public int getWidth(){return 174;}
    @Override public int getHeight(){return 76;}
    @Override public IDrawable getIcon(){return icon;}
    @Override public void setRecipe(IRecipeLayoutBuilder builder,CopperWorkshops.RecipePreview recipe,IFocusGroup focuses){
        for(int i=0;i<recipe.inputs().size();i++){
            var stack=recipe.inputs().get(i);var slot=builder.addInputSlot(6+i*20,23).setStandardSlotBackground();
            var metal=CopperWorkshops.melt(stack);
            if(metal!=null&&(recipe.description().endsWith("casting")||recipe.description().endsWith("alloy")))slot.addFluidStack(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(metal[0]).getFluid(),(long)metal[1]*stack.getCount()).setFluidRenderer((long)metal[1]*stack.getCount(),false,16,16);
            else slot.add(stack);
        }
        if(!recipe.tool().isEmpty())builder.addInputSlot(74,23).setStandardSlotBackground().add(recipe.tool());
        var output=builder.addOutputSlot(140,23).setOutputSlotBackground();
        if(recipe.description().endsWith("alloy"))output.addFluidStack(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(2).getFluid(),36000).setFluidRenderer(36000,false,16,16);
        else if(recipe.description().endsWith("melting")){int kind=CopperWorkshops.melt(recipe.inputs().getFirst())[0];output.addFluidStack(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(kind).getFluid(),(long)CopperWorkshops.melt(recipe.inputs().getFirst())[1]*recipe.inputs().getFirst().getCount()).setFluidRenderer((long)CopperWorkshops.melt(recipe.inputs().getFirst())[1]*recipe.inputs().getFirst().getCount(),false,16,16);}
        else output.add(recipe.result());
    }
    @Override public void createRecipeExtras(IRecipeExtrasBuilder builder,CopperWorkshops.RecipePreview recipe,IFocusGroup focuses){
        builder.addText(Component.translatable("block.smog."+recipe.machine()),170,12).setPosition(2,2);
        builder.addText(Component.translatable(recipe.description()),170,25).setPosition(2,49);
    }
}
