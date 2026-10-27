package io.github.kpaakeh.smog.registry;

import java.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Processed metals without invented ore blocks. */
public final class ModAlloys {
    public record Alloy(String name,Item ingot,Item dust,Item nugget,Item plate,Block block){
        public List<ItemLike> contents(){return List.of(ingot,dust,nugget,plate,block);}
    }
    private static final List<Alloy> VALUES=new ArrayList<>();
    private ModAlloys(){}
    public static Alloy register(String name){
        var alloy=new Alloy(name,part(name,"ingot"),part(name,"dust"),part(name,"nugget"),part(name,"plate"),
            ModBlocks.register(name+"_block",BlockBehaviour.Properties.of().strength(5).requiresCorrectToolForDrops()));
        VALUES.add(alloy);return alloy;
    }
    private static Item part(String name,String part){return ModItems.register(name+"_"+part,"item","copper","alloys");}
    public static List<Alloy> all(){return List.copyOf(VALUES);}
}
