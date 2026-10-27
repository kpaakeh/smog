package io.github.kpaakeh.smog.compat.jei;

import io.github.kpaakeh.smog.machine.*;
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
import net.minecraft.world.item.ItemStack;

public final class ProcessingCategory implements IRecipeCategory<ProcessingRecipes.Recipe> {
    public static final IRecipeType<ProcessingRecipes.Recipe> CRUSHING=IRecipeType.create("smog","crushing",ProcessingRecipes.Recipe.class);
    public static final IRecipeType<ProcessingRecipes.Recipe> PRESSING=IRecipeType.create("smog","pressing",ProcessingRecipes.Recipe.class);
    private final MachineSpec.Kind kind;
    private final IDrawable icon;
    public ProcessingCategory(IGuiHelper gui,MachineSpec.Kind kind){this.kind=kind;icon=gui.createDrawableItemLike(BuiltInRegistries.BLOCK.getValue(ModBlocks.id(kind==MachineSpec.Kind.CRUSHER?"crusher":"press")));}
    @Override public IRecipeType<ProcessingRecipes.Recipe> getRecipeType(){return kind==MachineSpec.Kind.CRUSHER?CRUSHING:PRESSING;}
    @Override public Component getTitle(){return Component.translatable("block.smog."+(kind==MachineSpec.Kind.CRUSHER?"crusher":"press"));}
    @Override public int getWidth(){return 150;}
    @Override public int getHeight(){return 58;}
    @Override public IDrawable getIcon(){return icon;}
    @Override public void setRecipe(IRecipeLayoutBuilder builder,ProcessingRecipes.Recipe recipe,IFocusGroup focuses){
        builder.addInputSlot(18,18).setStandardSlotBackground().add(new ItemStack(recipe.input(),recipe.inputCount()));
        builder.addOutputSlot(110,18).setOutputSlotBackground().add(recipe.result());
    }
    @Override public void createRecipeExtras(IRecipeExtrasBuilder builder,ProcessingRecipes.Recipe recipe,IFocusGroup focuses){
        builder.addRecipeArrow().setPosition(62,18);
        var spec=Machines.all().stream().filter(m->m.spec().kind()==kind).findFirst().orElseThrow().spec();
        builder.addText(Component.literal(spec.workTicks()+" ticks | "+(spec.workTicks()*spec.cost())+" FE"),145,10).setPosition(3,43);
    }
}
