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
    public static final java.util.Map<String,IRecipeType<CopperWorkshops.RecipePreview>> TYPES=new java.util.LinkedHashMap<>();
    static { for(String machine:java.util.List.of("copper_hearth","casting_table","casting_basin","potter_table","ceramic_kiln","washing_trough","lever_press","tool_workbench"))TYPES.put(machine,IRecipeType.create("smog",machine,CopperWorkshops.RecipePreview.class)); }
    private final String machine;
    private final IDrawable icon;
    public CopperCategory(IGuiHelper gui,String machine){this.machine=machine;icon=gui.createDrawableItemLike(BuiltInRegistries.BLOCK.getValue(ModBlocks.id(machine)));}
    @Override public IRecipeType<CopperWorkshops.RecipePreview> getRecipeType(){return TYPES.get(machine);}
    @Override public Component getTitle(){return Component.translatable("block.smog."+machine);}
    @Override public int getWidth(){return 174;}
    @Override public int getHeight(){return machine.equals("tool_workbench")?60+20*((io.github.kpaakeh.smog.copper.ToolDefinitions.maxParts()+3)/4):machine.equals("copper_hearth")?96:76;}
    @Override public IDrawable getIcon(){return icon;}
    @Override public void setRecipe(IRecipeLayoutBuilder builder,CopperWorkshops.RecipePreview recipe,IFocusGroup focuses){
        for(int i=0;i<recipe.inputs().size();i++){
            var stack=recipe.inputs().get(i);var slot=builder.addInputSlot(6+(machine.equals("tool_workbench")||machine.equals("copper_hearth")?i%4:i)*20,23+(machine.equals("tool_workbench")||machine.equals("copper_hearth")?i/4:0)*20).setStandardSlotBackground();
            var metal=CopperWorkshops.melt(stack);
            if(metal!=null&&(recipe.description().endsWith("casting")||recipe.description().endsWith("alloy")||recipe.description().endsWith("mold_making")))slot.addFluidStack(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(metal[0]).getFluid(),recipe.fluidUnits()>0?recipe.fluidUnits():(long)metal[1]*stack.getCount()).setFluidRenderer(recipe.fluidUnits()>0?recipe.fluidUnits():(long)metal[1]*stack.getCount(),false,16,16);
            else if(recipe.machine().equals("washing_trough")&&stack.is(net.minecraft.world.item.Items.WATER_BUCKET))slot.addFluidStack(net.minecraft.world.level.material.Fluids.WATER,81000).setFluidRenderer(81000,false,16,16);
            else slot.add(stack);
        }
        if(recipe.machine().equals("lever_press")&&!recipe.description().endsWith("fiber_pressing"))builder.addOutputSlot(116,23).setStandardSlotBackground().addFluidStack(BuiltInRegistries.FLUID.getValue(ModBlocks.id("plant_oil")),20250).setFluidRenderer(20250,false,16,16);
        if(!recipe.tool().isEmpty())builder.addInputSlot(machine.equals("tool_workbench")?104:74,23).setStandardSlotBackground().add(recipe.tool());
        var output=builder.addOutputSlot(140,23).setOutputSlotBackground();
        if(recipe.description().endsWith("alloy"))output.addFluidStack(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(CopperWorkshops.melt(recipe.result())[0]).getFluid(),9000L*recipe.result().getCount()).setFluidRenderer(9000L*recipe.result().getCount(),false,16,16);
        else if(recipe.description().endsWith("melting")){int kind=CopperWorkshops.melt(recipe.inputs().getFirst())[0];output.addFluidStack(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(kind).getFluid(),(long)CopperWorkshops.melt(recipe.inputs().getFirst())[1]*recipe.inputs().getFirst().getCount()).setFluidRenderer((long)CopperWorkshops.melt(recipe.inputs().getFirst())[1]*recipe.inputs().getFirst().getCount(),false,16,16);}
        else output.add(recipe.result());
    }
    @Override public void createRecipeExtras(IRecipeExtrasBuilder builder,CopperWorkshops.RecipePreview recipe,IFocusGroup focuses){
        builder.addText(Component.translatable("block.smog."+recipe.machine()),170,12).setPosition(2,2);
        Component description=Component.translatable(recipe.description());
        if(recipe.description().endsWith("melting")){var melt=CopperWorkshops.melt(recipe.inputs().getFirst());if(melt!=null)description=description.copy().append(" · ").append(Component.translatable("jei.smog.copper.required_heat",CopperWorkshops.meltingHeat(melt[0])));}
        builder.addText(description,170,25).setPosition(2,getHeight()-27);
    }
}
