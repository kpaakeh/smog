package io.github.kpaakeh.smog.copper;

import com.google.gson.JsonObject;
import java.util.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;

/** Finite primary-material washing recipes. Outputs are never valid washing inputs. */
public final class WashingRecipes {
    public record Recipe(String input,int count,String output,int outputCount,int water,int ticks,String metal,int metalUnits){
        public boolean accepts(ItemStack stack){return !stack.isEmpty()&&stack.is(BuiltInRegistries.ITEM.getValue(Identifier.parse(input)));}
        public ItemStack result(){return new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(output)),outputCount);}
        public ItemStack ingredients(){return new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(input)),count);}
    }
    private static final List<Recipe> RECIPES=new ArrayList<>();
    private WashingRecipes(){}
    public static void initialize(JsonObject root){
        RECIPES.clear();var inputs=new HashSet<String>();
        for(var value:root.getAsJsonArray("washing")){
            var o=value.getAsJsonObject();var recipe=new Recipe(o.get("input").getAsString(),o.get("count").getAsInt(),o.get("output").getAsString(),o.get("output_count").getAsInt(),o.get("water").getAsInt(),o.get("ticks").getAsInt(),o.has("metal")?o.get("metal").getAsString():"",o.has("metal_units")?o.get("metal_units").getAsInt():0);
            if(!inputs.add(recipe.input)||recipe.count<1||recipe.count>64||recipe.outputCount<1||recipe.outputCount>64||recipe.water<1||recipe.water>WorkshopFluidStorage.CAPACITY||recipe.ticks<1||recipe.ticks>1000||!BuiltInRegistries.ITEM.containsKey(Identifier.parse(recipe.input))||!BuiltInRegistries.ITEM.containsKey(Identifier.parse(recipe.output)))throw new IllegalArgumentException("Invalid washing recipe: "+recipe);
            if(!recipe.metal.isEmpty()&&(Arrays.asList(MoltenMetalStorage.NAMES).indexOf(recipe.metal)<0||recipe.metalUnits<=0))throw new IllegalArgumentException("Invalid concentrate metal: "+recipe);
            RECIPES.add(recipe);
        }
        for(var recipe:RECIPES)if(inputs.contains(recipe.output))throw new IllegalArgumentException("Washing output cannot be washed again: "+recipe.output);
    }
    public static List<Recipe> all(){return List.copyOf(RECIPES);}
    public static Recipe find(ItemStack input){return RECIPES.stream().filter(r->r.accepts(input)).findFirst().orElse(null);}
    public static int[] concentrate(ItemStack stack){
        if(stack.isEmpty())return null;
        String id=BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        for(var recipe:RECIPES)if(!recipe.metal.isEmpty()&&recipe.output.equals(id))return new int[]{Arrays.asList(MoltenMetalStorage.NAMES).indexOf(recipe.metal),recipe.metalUnits};
        return null;
    }
}
