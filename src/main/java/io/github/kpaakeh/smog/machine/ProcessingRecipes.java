package io.github.kpaakeh.smog.machine;

import java.util.*;
import io.github.kpaakeh.smog.registry.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;

public final class ProcessingRecipes {
    public record Recipe(MachineSpec.Kind kind,Item input,int inputCount,Item output,int outputCount) {
        public ItemStack result() { return new ItemStack(output,outputCount); }
    }
    private static final List<Recipe> RECIPES=new ArrayList<>();
    private ProcessingRecipes() {}
    public static Recipe register(MachineSpec.Kind kind,Item input,int inputCount,Item output,int outputCount) {
        Recipe recipe=new Recipe(kind,input,inputCount,output,outputCount);RECIPES.add(recipe);return recipe;
    }
    public static Recipe find(MachineSpec.Kind kind,ItemStack input) {
        return RECIPES.stream().filter(r->r.kind()==kind && input.is(r.input()) && input.getCount()>=r.inputCount()).findFirst().orElse(null);
    }
    public static List<Recipe> all() { return List.copyOf(RECIPES); }
    public static void initialize() {
        for(var ore:ModOres.all()) {
            register(MachineSpec.Kind.CRUSHER,ore.raw(),1,ore.crushed(),1);
            register(MachineSpec.Kind.CRUSHER,ore.crushed(),1,ore.shard(),1);
            register(MachineSpec.Kind.CRUSHER,ore.shard(),1,ore.dust(),1);
            register(MachineSpec.Kind.PRESS,ore.product(),1,ore.plate(),1);
        }
        register(MachineSpec.Kind.CRUSHER,Items.COBBLESTONE,1,Items.GRAVEL,1);
        register(MachineSpec.Kind.CRUSHER,Items.GRAVEL,1,Items.SAND,1);
        register(MachineSpec.Kind.PRESS,Items.IRON_INGOT,1,BuiltInRegistries.ITEM.getValue(ModBlocks.id("iron_plate")),1);
        register(MachineSpec.Kind.PRESS,Items.COPPER_INGOT,1,BuiltInRegistries.ITEM.getValue(ModBlocks.id("copper_plate")),1);
    }
}
