package io.github.kpaakeh.smog.bronze;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import java.util.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.item.*;

/** Real initial manufacturing on the existing foundry and copper anvil. */
public final class BronzeManufacturing {
    public static final Map<String,Integer> SHAPES;
    static {var shapes=new LinkedHashMap<String,Integer>();shapes.put("gear",18000);shapes.put("hub",9000);shapes.put("cam",18000);shapes.put("piston",18000);shapes.put("bushing",4500);shapes.put("die",9000);SHAPES=Collections.unmodifiableMap(shapes);}
    private BronzeManufacturing() {}
    public static ItemStack stack(String id){return new ItemStack(BuiltInRegistries.ITEM.getValue(ModBlocks.id(id)));}
    private static String name(ItemStack stack){var key=BuiltInRegistries.ITEM.getKey(stack.getItem());return key.getNamespace().equals("smog")?key.getPath():"";}
    public static String pattern(ItemStack sample){String n=name(sample);for(String shape:SHAPES.keySet())if(n.equals(shape+"_pattern")||n.equals("bronze_"+shape))return shape;return null;}
    public static int[] melt(ItemStack input){String n=name(input);int bronze=Arrays.asList(MoltenMetalStorage.NAMES).indexOf("bronze");for(var e:SHAPES.entrySet())if(n.equals("bronze_"+e.getKey())||n.equals("cast_bronze_"+e.getKey()))return new int[]{bronze,e.getValue()};return null;}
    public record Plan(ItemStack output,int first,int second,boolean retainSecond,boolean hammer) {}
    public static String key(Plan plan){return plan==null?"":"bronze:"+BuiltInRegistries.ITEM.getKey(plan.output().getItem());}
    private static boolean planks(ItemStack stack){return stack.is(net.minecraft.tags.ItemTags.PLANKS);}
    public static boolean accepts(int slot,ItemStack stack){String n=name(stack);if(slot==0)return n.startsWith("cast_bronze_")&&SHAPES.containsKey(n.substring(12))||n.equals("iron_bloom");return slot==1&&(pattern(stack)!=null||Set.of("wooden_drum","wooden_pulley_rim","bronze_fitting").contains(n));}
    public static Plan plan(Container inventory){
        if(!inventory.getItem(5).isEmpty())return null;
        for(int i=0;i<ToolDefinitions.maxParts();i++){int slot=ToolDefinitions.slot(i);if(slot!=0&&slot!=1&&!inventory.getItem(slot).isEmpty())return null;}
        var a=inventory.getItem(0);var b=inventory.getItem(1);String n=name(a);
        if(b.isEmpty()){
            for(String shape:SHAPES.keySet())if(n.equals("cast_bronze_"+shape))return new Plan(stack("bronze_"+shape),1,0,false,true);
            if(n.equals("iron_bloom")){var result=stack("iron_billet");result.setCount(4);return new Plan(result,1,0,false,true);}
        }
        if(n.equals("bronze_ingot")&&name(b).equals("bronze_fitting"))return new Plan(stack("bronze_drill_bit"),1,1,false,true);
        if(planks(a)){
            String template=switch(name(b)){case "hub_pattern"->"hub_template";case "wooden_drum"->"drum_template";case "wooden_pulley_rim"->"pulley_template";default->"";};
            if(!template.isEmpty())return new Plan(stack(template),1,0,true,false);
        }return null;
    }
    public static List<CopperWorkshops.RecipePreview> previews(){
        var pages=new ArrayList<CopperWorkshops.RecipePreview>();var hammer=ToolParts.example(ToolDefinitions.template("hammer"),"stone");
        SHAPES.forEach((shape,units)->pages.add(new CopperWorkshops.RecipePreview("tool_workbench",List.of(stack("cast_bronze_"+shape)),hammer,stack("bronze_"+shape),"jei.smog.bronze.finishing")));
        var billets=stack("iron_billet");billets.setCount(4);pages.add(new CopperWorkshops.RecipePreview("tool_workbench",List.of(stack("iron_bloom")),hammer,billets,"jei.smog.bronze.finishing"));
        pages.add(new CopperWorkshops.RecipePreview("tool_workbench",List.of(stack("bronze_ingot"),stack("bronze_fitting")),hammer,stack("bronze_drill_bit"),"jei.smog.bronze.finishing"));
        for(var e:Map.of("hub_pattern","hub_template","wooden_drum","drum_template","wooden_pulley_rim","pulley_template").entrySet())pages.add(new CopperWorkshops.RecipePreview("tool_workbench",List.of(new ItemStack(Items.OAK_PLANKS)),stack(e.getKey()),stack(e.getValue()),"jei.smog.bronze.template"));
        return List.copyOf(pages);
    }
}
