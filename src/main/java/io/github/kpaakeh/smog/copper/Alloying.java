package io.github.kpaakeh.smog.copper;

import java.util.*;
import com.google.gson.*;
import io.github.kpaakeh.smog.machine.MachineEntity;

/** Ordered explicit recipes mix co-present liquids automatically; no era checks or mix button. */
public final class Alloying {
    public record Recipe(String output,Map<String,Integer> ingredients,int heat,boolean closed){public int total(){return ingredients.values().stream().mapToInt(Integer::intValue).sum();}}
    private static final List<Recipe> RECIPES=new ArrayList<>();
    private static int kind(String name){return Arrays.asList(MoltenMetalStorage.NAMES).indexOf(name);}
    public static void initialize(JsonObject root){RECIPES.clear();if(!root.has("alloy_recipes"))return;for(var entry:root.getAsJsonArray("alloy_recipes")){var o=entry.getAsJsonObject();var input=new LinkedHashMap<String,Integer>();o.getAsJsonObject("ingredients").entrySet().forEach(e->{if(kind(e.getKey())<0||e.getValue().getAsInt()<1)throw new IllegalArgumentException("Invalid alloy input "+e.getKey());input.put(e.getKey(),e.getValue().getAsInt());});String output=o.get("output").getAsString();if(kind(output)<0||input.containsKey(output)||input.size()<2||input.values().stream().mapToLong(Integer::longValue).sum()>1000)throw new IllegalArgumentException("Invalid alloy output "+output);RECIPES.add(new Recipe(output,Map.copyOf(input),o.get("heat").getAsInt(),o.has("closed")&&o.get("closed").getAsBoolean()));}}
    public static List<Recipe> recipes(){return List.copyOf(RECIPES);}
    private static int batches(MachineEntity m,Recipe recipe){int result=Integer.MAX_VALUE;for(var entry:recipe.ingredients().entrySet())result=Math.min(result,m.copper.metal(kind(entry.getKey()))/entry.getValue());return result;}
    public static boolean possible(MachineEntity m){return possible(m,Integer.MAX_VALUE);}
    public static boolean possible(MachineEntity m,int limit){return RECIPES.stream().anyMatch(r->r.heat()<=Math.min(limit,Smeltery.maximumHeat(m))&&(!r.closed()||CopperWorkshops.closed(m))&&MetalProcessing.compatible(m,kind(r.output()))&&batches(m,r)>0);}
    public static int requiredHeat(MachineEntity m){return RECIPES.stream().filter(r->r.heat()<=Smeltery.maximumHeat(m)&&(!r.closed()||CopperWorkshops.closed(m))&&batches(m,r)>0).mapToInt(Recipe::heat).max().orElse(0);}
    public static void tick(MachineEntity m){if(!Smeltery.handles(m)||!m.assembled()||!m.smeltery.fuelActive)return;for(var recipe:RECIPES){if(m.copper.heat<recipe.heat()||m.smeltery.fuelHeat<recipe.heat()||recipe.closed()&&!CopperWorkshops.closed(m)||!MetalProcessing.compatible(m,kind(recipe.output())))continue;int batches=batches(m,recipe);if(batches==0)continue;for(var input:recipe.ingredients().entrySet()){int metal=kind(input.getKey());m.copper.metal(metal,m.copper.metal(metal)-batches*input.getValue());}int output=kind(recipe.output());m.copper.metal(output,m.copper.metal(output)+batches*recipe.total());m.setChanged();}}
}
