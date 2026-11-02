package io.github.kpaakeh.smog.test;

import com.google.gson.*;
import java.util.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.registries.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

/** Real recipe lookup with alternate vanilla materials, not just inspection of tag strings. */
public final class WorkshopMaterialTests {
    private static ItemStack ingredient(String id){
        return switch(id){
            case "#minecraft:planks"->new ItemStack(Items.SPRUCE_PLANKS);
            case "#minecraft:wooden_fences"->new ItemStack(Items.CRIMSON_FENCE);
            case "#minecraft:stone_tool_materials"->new ItemStack(Items.BLACKSTONE);
            case "#minecraft:wool"->new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.withDefaultNamespace("magenta_wool")));
            case "#minecraft:sand"->new ItemStack(Items.RED_SAND);
            default->id.startsWith("#")?BuiltInRegistries.ITEM.getTagOrEmpty(TagKey.create(Registries.ITEM,Identifier.parse(id.substring(1)))).iterator().next().value().getDefaultInstance():BuiltInRegistries.ITEM.getValue(Identifier.parse(id)).getDefaultInstance();
        };
    }
    @GameTest public void workshopRecipesAcceptAlternateVanillaMaterialsAndMeaningfulWrapInput(GameTestHelper h) throws java.io.IOException {
        try(var stream=WorkshopMaterialTests.class.getResourceAsStream("/smog.content.json")){
            var root=JsonParser.parseReader(new java.io.InputStreamReader(stream,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            var names=Set.of("potter_table","casting_table","lever_press","tempered_clay","wool_wrap","fiber_wrap");int checked=0;
            for(var entry:root.getAsJsonArray("crafting")){
                var c=entry.getAsJsonObject();if(!names.contains(c.get("name").getAsString()))continue;var recipe=c.getAsJsonObject("data");var grid=new ArrayList<ItemStack>(Collections.nCopies(9,ItemStack.EMPTY));
                if(recipe.get("type").getAsString().endsWith("crafting_shaped")){
                    var rows=recipe.getAsJsonArray("pattern");var key=recipe.getAsJsonObject("key");for(int y=0;y<rows.size();y++)for(int x=0;x<rows.get(y).getAsString().length();x++){char symbol=rows.get(y).getAsString().charAt(x);if(symbol!=' ')grid.set(y*3+x,ingredient(key.get(String.valueOf(symbol)).getAsString()));}
                }else{var in=recipe.getAsJsonArray("ingredients");for(int i=0;i<in.size();i++)grid.set(i,ingredient(in.get(i).getAsString()));}
                var input=CraftingInput.of(3,3,grid);var actual=h.getLevel().recipeAccess().getRecipeFor(RecipeType.CRAFTING,input,h.getLevel()).map(holder->holder.value().assemble(input)).orElse(ItemStack.EMPTY);var result=recipe.getAsJsonObject("result");
                h.assertTrue(actual.is(BuiltInRegistries.ITEM.getValue(Identifier.parse(result.get("id").getAsString())))&&actual.getCount()==result.get("count").getAsInt(),"Alternate vanilla material produces the unchanged recipe output: "+c.get("name"));
                if(c.get("name").getAsString().equals("fiber_wrap"))h.assertTrue(grid.getFirst().is(BuiltInRegistries.ITEM.getValue(Identifier.parse("smog:plant_fiber")))&&grid.stream().filter(s->!s.isEmpty()).count()==1,"Plant-fiber wrap consumes actual plant fiber at half a unit per wrap");checked++;
            }
            h.assertValueEqual(checked,names.size(),"Every selected alternate-material route was exercised");
        }
        h.succeed();
    }
    @GameTest public void ordinaryRedstoneCraftingRemainsAvailableWithoutWorkshopProgress(GameTestHelper h){
        var torch=CraftingInput.of(1,2,List.of(new ItemStack(Items.REDSTONE),new ItemStack(Items.STICK)));
        var actual=h.getLevel().recipeAccess().getRecipeFor(RecipeType.CRAFTING,torch,h.getLevel()).map(holder->holder.value().assemble(torch)).orElse(ItemStack.EMPTY);
        h.assertTrue(actual.is(Items.REDSTONE_TORCH),"Vanilla redstone torch remains directly craftable");
        var lever=CraftingInput.of(1,2,List.of(new ItemStack(Items.STICK),new ItemStack(Items.COBBLESTONE)));
        actual=h.getLevel().recipeAccess().getRecipeFor(RecipeType.CRAFTING,lever,h.getLevel()).map(holder->holder.value().assemble(lever)).orElse(ItemStack.EMPTY);
        h.assertTrue(actual.is(Items.LEVER),"Vanilla switching remains directly craftable");h.succeed();
    }
}
