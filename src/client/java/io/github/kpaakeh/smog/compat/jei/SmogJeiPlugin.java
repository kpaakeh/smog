package io.github.kpaakeh.smog.compat.jei;

import java.util.*;
import io.github.kpaakeh.smog.machine.Machines;
import io.github.kpaakeh.smog.multiblock.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import mezz.jei.api.*;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

/** Optional JEI integration: full depth-tested 3D structure and its bill of materials. */
@JeiPlugin
public final class SmogJeiPlugin implements IModPlugin {
    public record Page(MultiblockDefinition structure, Block controller) {
        public net.minecraft.world.level.block.state.BlockState previewState(boolean formed){
            var kind=io.github.kpaakeh.smog.machine.GeneratorKind.NONE;
            if(structure.id().getPath().startsWith("generator_"))kind=io.github.kpaakeh.smog.machine.GeneratorKind.valueOf(structure.id().getPath().substring(10).toUpperCase(java.util.Locale.ROOT));
            return controller.defaultBlockState().setValue(io.github.kpaakeh.smog.machine.MachineBlock.ASSEMBLED,formed).setValue(io.github.kpaakeh.smog.machine.MachineBlock.GENERATOR,kind);
        }
    }
    public static final IRecipeType<Page> TYPE = IRecipeType.create("smog", "multiblocks", Page.class);
    @Override public Identifier getPluginUid() { return ModBlocks.id("jei"); }
    @Override public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new Category(registration.getJeiHelpers().getGuiHelper()));
        if(Machines.all().stream().anyMatch(m->m.spec().kind()==io.github.kpaakeh.smog.machine.MachineSpec.Kind.COPPER_WORKSHOP))for(String machine:CopperCategory.TYPES.keySet())registration.addRecipeCategories(new CopperCategory(registration.getJeiHelpers().getGuiHelper(),machine));
        for(var kind:List.of(io.github.kpaakeh.smog.machine.MachineSpec.Kind.CRUSHER,io.github.kpaakeh.smog.machine.MachineSpec.Kind.PRESS))
            if(Machines.all().stream().anyMatch(machine->machine.spec().kind()==kind))registration.addRecipeCategories(new ProcessingCategory(registration.getJeiHelpers().getGuiHelper(),kind));
    }
    @Override public void registerRecipes(IRecipeRegistration registration) {
        List<Page> pages = new ArrayList<>();
        for (var entry : Machines.all()) {
            if(entry.spec().kind()==io.github.kpaakeh.smog.machine.MachineSpec.Kind.GENERATOR_CONTROLLER)
                for(var kind:io.github.kpaakeh.smog.machine.GeneratorKind.values())if(kind!=io.github.kpaakeh.smog.machine.GeneratorKind.NONE)pages.add(new Page(Multiblocks.get(kind.structureId()),entry.block()));
            var id = entry.definition().multiblock();
            if (id != null) pages.add(new Page(Multiblocks.get(id), entry.block()));
        }
        registration.addRecipes(TYPE, pages);
        if(Machines.all().stream().anyMatch(m->m.spec().kind()==io.github.kpaakeh.smog.machine.MachineSpec.Kind.COPPER_WORKSHOP))CopperCategory.TYPES.forEach((machine,type)->registration.addRecipes(type,io.github.kpaakeh.smog.copper.CopperWorkshops.recipes().stream().filter(recipe->recipe.machine().equals(machine)).toList()));
        if(Machines.all().stream().anyMatch(m->m.spec().kind()==io.github.kpaakeh.smog.machine.MachineSpec.Kind.CRUSHER))registration.addRecipes(ProcessingCategory.CRUSHING,io.github.kpaakeh.smog.machine.ProcessingRecipes.all().stream().filter(r->r.kind()==io.github.kpaakeh.smog.machine.MachineSpec.Kind.CRUSHER).toList());
        if(Machines.all().stream().anyMatch(m->m.spec().kind()==io.github.kpaakeh.smog.machine.MachineSpec.Kind.PRESS))registration.addRecipes(ProcessingCategory.PRESSING,io.github.kpaakeh.smog.machine.ProcessingRecipes.all().stream().filter(r->r.kind()==io.github.kpaakeh.smog.machine.MachineSpec.Kind.PRESS).toList());
    }
    @Override public void registerRecipeCatalysts(IRecipeCatalystRegistration registration){
        for(var entry:Machines.all()){
            if(entry.definition().multiblock()!=null)registration.addCraftingStation(TYPE,entry.block());
            if(entry.spec().kind()==io.github.kpaakeh.smog.machine.MachineSpec.Kind.COPPER_WORKSHOP&&CopperCategory.TYPES.containsKey(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(entry.block()).getPath()))registration.addCraftingStation(CopperCategory.TYPES.get(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(entry.block()).getPath()),entry.block());
        }
    }
    private static final class Category implements IRecipeCategory<Page> {
        private final IDrawable icon;
        private Category(IGuiHelper gui) { icon=gui.createDrawableItemLike(net.minecraft.world.level.block.Blocks.IRON_BLOCK); }
        @Override public IRecipeType<Page> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("jei.smog.multiblocks"); }
        @Override public int getWidth() { return 170; }
        @Override public int getHeight() { return 154; }
        @Override public IDrawable getIcon() { return icon; }
        @Override public void setRecipe(IRecipeLayoutBuilder builder, Page page, IFocusGroup focuses) {
            builder.addOutputSlot(150,0).add(page.controller());
            Map<String,Integer> materials=new LinkedHashMap<>();
            page.structure().parts().values().forEach(part -> {
                if (!part.value().equals("minecraft:air")) materials.merge(part.value(),1,Integer::sum);
            });
            for (var entry : materials.entrySet()) {
                var items=new BlockIngredient(entry.getKey()).displayItems().stream().map(stack -> {
                    var copy=stack.copy();copy.setCount(entry.getValue());return copy;
                }).toList();
                builder.addInputSlot().setStandardSlotBackground().addItemStacks(items);
            }
        }
        @Override public void createRecipeExtras(IRecipeExtrasBuilder builder, Page page, IFocusGroup focuses) {
            var preview=new StructurePreviewWidget(page);
            builder.addWidget(preview);builder.addInputHandler(preview);
            var complex=io.github.kpaakeh.smog.multiblock.complex.ComplexMultiblocks.get(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(page.controller()));
            var b=page.structure().bounds();
            builder.addText(Component.translatable("multiblock."+page.structure().id().getNamespace()+"."+page.structure().id().getPath()),146,12).setPosition(2,0);
            builder.addText(Component.literal(b.width()+" x "+b.height()+" x "+b.depth()),140,10).setPosition(2,14);
            builder.addScrollGridWidget(builder.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT),9,1).setPosition(2,110);
            builder.addText(List.of(Component.translatable("jei.smog.controls"),Component.translatable("jei.smog.controls_more")),166,20).setPosition(2,130);
        }
    }
}
