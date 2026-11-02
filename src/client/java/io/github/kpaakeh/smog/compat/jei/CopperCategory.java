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
    @Override public int getHeight(){return machine.equals("tool_workbench")?Math.max(46,12+20*((io.github.kpaakeh.smog.copper.ToolDefinitions.maxParts()+3)/4)):40;}
    @Override public IDrawable getIcon(){return icon;}
    @Override public void setRecipe(IRecipeLayoutBuilder builder,CopperWorkshops.RecipePreview recipe,IFocusGroup focuses){
        for(int i=0;i<recipe.inputs().size();i++){
            var stack=recipe.inputs().get(i);var slot=builder.addInputSlot(6+(machine.equals("tool_workbench")||machine.equals("copper_hearth")?i%4:i)*20,7+(machine.equals("tool_workbench")||machine.equals("copper_hearth")?i/4:0)*20).setStandardSlotBackground();
            var metal=CopperWorkshops.melt(stack);
            if(metal!=null&&recipe.description().endsWith("melting"))slot.addRichTooltipCallback((view,tooltip)->io.github.kpaakeh.smog.copper.MetalProcessing.requirements(metal[0]).forEach(tooltip::add));
            if(metal!=null&&(recipe.description().endsWith("casting")||recipe.description().endsWith("alloy")||recipe.description().endsWith("mold_making")))slot.addFluidStack(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(metal[0]).getFluid(),recipe.fluidUnits()>0?recipe.fluidUnits():(long)metal[1]*stack.getCount()).setFluidRenderer(recipe.fluidUnits()>0?recipe.fluidUnits():(long)metal[1]*stack.getCount(),false,16,16);
            else if(recipe.machine().equals("washing_trough")&&stack.is(net.minecraft.world.item.Items.WATER_BUCKET)){long water=recipe.fluidUnits()>0?recipe.fluidUnits():81000;slot.addFluidStack(net.minecraft.world.level.material.Fluids.WATER,water).setFluidRenderer(81000,false,16,16);fluidTooltip(slot,water);}
            else slot.add(stack);
            if(metal!=null&&(recipe.description().endsWith("casting")||recipe.description().endsWith("alloy")||recipe.description().endsWith("mold_making")))fluidTooltip(slot,recipe.fluidUnits()>0?recipe.fluidUnits():(long)metal[1]*stack.getCount());
        }
        if(recipe.machine().equals("lever_press")&&!recipe.description().endsWith("fiber_pressing"))builder.addOutputSlot(116,7).setStandardSlotBackground().addFluidStack(BuiltInRegistries.FLUID.getValue(ModBlocks.id("plant_oil")),20250).setFluidRenderer(20250,false,16,16);
        if(!recipe.tool().isEmpty()){
            var toolSlot=builder.addInputSlot(machine.equals("tool_workbench")?90:74,7).setStandardSlotBackground();
            if(machine.equals("casting_table")&&recipe.description().endsWith("casting"))toolSlot.addItemStacks(moldAlternatives(recipe.tool()));else toolSlot.add(recipe.tool());
        }
        boolean tank=recipe.description().endsWith("melting")||recipe.description().endsWith("alloy");var output=builder.addOutputSlot(140,tank?3:7);if(tank)output.setBackground(new SmelteryTankDrawable(),-1,-1).setOverlay(SmelteryTankDrawable.MARKS,0,0);else output.setOutputSlotBackground();
        if(recipe.description().endsWith("alloy"))output.addFluidStack(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(CopperWorkshops.melt(recipe.result())[0]).getFluid(),9000L*recipe.result().getCount()).setFluidRenderer(tankCapacity(),false,16,30);
        else if(recipe.description().endsWith("melting")){int kind=CopperWorkshops.melt(recipe.inputs().getFirst())[0];output.addFluidStack(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(kind).getFluid(),(long)CopperWorkshops.melt(recipe.inputs().getFirst())[1]*recipe.inputs().getFirst().getCount()).setFluidRenderer(tankCapacity(),false,16,30);}
        else output.add(recipe.result());
        if(machine.equals("tool_workbench")||io.github.kpaakeh.smog.copper.ToolDefinitions.part(recipe.result())!=null)output.addRichTooltipCallback((view,tooltip)->tooltip.add(Component.translatable("jei.smog.example_materials")));
        if(recipe.description().endsWith("alloy"))fluidTooltip(output,9000L*recipe.result().getCount());
        else if(recipe.description().endsWith("melting"))fluidTooltip(output,(long)CopperWorkshops.melt(recipe.inputs().getFirst())[1]*recipe.inputs().getFirst().getCount());
    }
    private static java.util.List<net.minecraft.world.item.ItemStack> moldAlternatives(net.minecraft.world.item.ItemStack mold){
        String base=BuiltInRegistries.ITEM.getKey(mold.getItem()).getPath().replaceFirst("^(sand_|metal_)","");
        var choices=new java.util.ArrayList<net.minecraft.world.item.ItemStack>();
        for(String prefix:java.util.List.of("","sand_","metal_")){var id=ModBlocks.id(prefix+base);if(BuiltInRegistries.ITEM.containsKey(id))choices.add(new net.minecraft.world.item.ItemStack(BuiltInRegistries.ITEM.getValue(id)));}
        return choices.isEmpty()?java.util.List.of(mold):java.util.List.copyOf(choices);
    }
    public static void fluidTooltip(mezz.jei.api.gui.builder.IRecipeSlotBuilder slot,long units){
        slot.addRichTooltipCallback((view,tooltip)->{
            var first=tooltip.getLines().stream().findFirst().flatMap(line->line.left());
            tooltip.clear();
            first.ifPresent(tooltip::add);
            tooltip.add(io.github.kpaakeh.smog.gui.FluidAmountText.format(units));
        });
    }
    private static long sharedTankCapacity=81000;
    public static void configureTankScale(java.util.List<CopperWorkshops.RecipePreview> recipes){sharedTankCapacity=Math.max(1,recipes.stream().filter(r->r.machine().equals("copper_hearth")).mapToLong(r->{if(r.description().endsWith("alloy"))return 9000L*r.result().getCount();if(r.description().endsWith("melting")){var melt=CopperWorkshops.melt(r.inputs().getFirst());return melt==null?0:(long)melt[1]*r.inputs().getFirst().getCount();}return 0;}).max().orElse(81000));}
    private static long tankCapacity(){return sharedTankCapacity;}
    @Override public void createRecipeExtras(IRecipeExtrasBuilder builder,CopperWorkshops.RecipePreview recipe,IFocusGroup focuses){
        boolean oil=machine.equals("lever_press")&&!recipe.description().endsWith("fiber_pressing");
        builder.addRecipeArrow().setPosition(machine.equals("tool_workbench")?110:oil?88:108,7);
        if(machine.equals("tool_workbench"))builder.addText(Component.translatable("jei.smog.example_label"),174,10).setPosition(2,getHeight()-11);
    }
}
