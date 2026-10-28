package io.github.kpaakeh.smog.test;

import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.Identifier;

/** Test-only access to JEI's public runtime; excluded from the release JAR. */
public final class TestJeiProbe implements IModPlugin {
    private static volatile IJeiRuntime runtime;
    @Override public Identifier getPluginUid() { return Identifier.fromNamespaceAndPath("smog_tests", "probe"); }
    @Override public void onRuntimeAvailable(IJeiRuntime value) { runtime = value; }
    @Override public void onRuntimeUnavailable() { runtime = null; }
    public static boolean ready() { return runtime != null; }
    public static void showCopper(String machine){
        var type=io.github.kpaakeh.smog.compat.jei.CopperCategory.TYPES.get(machine);var manager=runtime.getRecipeManager();
        if(type==null||manager.createRecipeLookup(type).get().findAny().isEmpty())throw new IllegalStateException("Missing copper JEI category: "+machine);
        runtime.getRecipesGui().showTypes(List.of(type));
    }
    public static void showMultiblocks() {
        var type = runtime.getRecipeManager().getRecipeType(Identifier.fromNamespaceAndPath("smog", "multiblocks")).orElseThrow();
        runtime.getRecipesGui().showTypes(List.of(type));
    }
    public static void showSolar() {
        var type=io.github.kpaakeh.smog.compat.jei.SmogJeiPlugin.TYPE;
        var manager=runtime.getRecipeManager();
        var pages=manager.createRecipeLookup(type).get().filter(p->p.structure().id().getPath().equals("generator_solar")).toList();
        runtime.getRecipesGui().showRecipes(manager.getRecipeCategory(type),pages,List.of());
    }
    public static void showDepthFixture() {
        var type=io.github.kpaakeh.smog.compat.jei.SmogJeiPlugin.TYPE;
        var definition=new io.github.kpaakeh.smog.multiblock.MultiblockDefinition(
            Identifier.fromNamespaceAndPath("smog","test_structure"),java.util.Map.of(
                new net.minecraft.core.BlockPos(0,1,0),new io.github.kpaakeh.smog.multiblock.BlockIngredient("minecraft:grass_block"),
                new net.minecraft.core.BlockPos(1,0,0),new io.github.kpaakeh.smog.multiblock.BlockIngredient("minecraft:gold_block")));
        var page=new io.github.kpaakeh.smog.compat.jei.SmogJeiPlugin.Page(definition,net.minecraft.world.level.block.Blocks.GRASS_BLOCK);
        runtime.getRecipesGui().showRecipes(runtime.getRecipeManager().getRecipeCategory(type),List.of(page),List.of());
    }
}
