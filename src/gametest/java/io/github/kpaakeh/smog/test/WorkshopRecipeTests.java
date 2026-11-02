package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

public final class WorkshopRecipeTests {
    @GameTest public void jeiShowsTenAssemblyExamplesInsteadOfEveryMaterialPermutation(GameTestHelper h){
        var recipes=CopperWorkshops.recipes();var bench=recipes.stream().filter(r->r.machine().equals("tool_workbench")).toList();
        var assemblies=bench.stream().filter(r->ModularToolItem.configured(r.result())).toList();
        h.assertValueEqual(assemblies.size(),10,"One representative assembly exists for each tool type");
        h.assertValueEqual(assemblies.stream().map(r->ToolParts.schema(r.result()).id()).distinct().count(),10L,"Every tool is represented exactly once");
        h.assertTrue(bench.size()<=24,"Conversion, shaping, finishing and imprint examples stay bounded independently of material count");
        for(var recipe:assemblies){
            var schema=ToolParts.schema(recipe.result());var rebuilt=ToolParts.assemble(schema,ToolParts.components(recipe.result()));
            h.assertTrue(!rebuilt.isEmpty(),"The displayed example is a real compatible assembly: "+schema.id());
            h.assertTrue(recipe.inputs().size()==3,"Each example has one head, the ordinary rod and a binding");
        }
        long meltingParts=recipes.stream().filter(r->r.description().endsWith("melting")&&ToolDefinitions.part(r.inputs().getFirst())!=null).count();
        h.assertValueEqual(meltingParts,1L,"Tool-part remelting is taught by one example instead of a material matrix");h.succeed();
    }
    @GameTest public void nativeConcentratesHaveUsableSmeltingAndBlastingWithoutMetalGain(GameTestHelper h){
        for(String metal:java.util.List.of("copper","iron","gold")){
            var concentrate=new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("smog",metal+"_concentrate")));
            var input=new SingleRecipeInput(concentrate);var smelting=h.getLevel().recipeAccess().getRecipeFor(RecipeType.SMELTING,input,h.getLevel()).orElseThrow();
            var blasting=h.getLevel().recipeAccess().getRecipeFor(RecipeType.BLASTING,input,h.getLevel()).orElseThrow();
            var expected=BuiltInRegistries.ITEM.getValue(Identifier.withDefaultNamespace(metal+"_ingot"));
            for(var output:java.util.List.of(smelting.value().assemble(input),blasting.value().assemble(input))){
                h.assertTrue(output.is(expected),"The vanilla furnace returns the canonical ingot for "+metal);
                h.assertValueEqual(output.getCount(),2,"One concentrate yields its two stored ingots");
                h.assertValueEqual(CopperWorkshops.melt(concentrate)[1],CopperWorkshops.melt(output)[1]*output.getCount(),"The furnace route never creates more metal than the concentrate stores");
            }
        }
        h.succeed();
    }
}
