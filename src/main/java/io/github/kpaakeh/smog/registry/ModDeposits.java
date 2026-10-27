package io.github.kpaakeh.smog.registry;

import java.util.*;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;

/** Natural nonmetal deposits are separate from metallic ore families. */
public final class ModDeposits {
    public record Deposit(String name,Block block,Item chunk,Item dust){}
    private static final List<Deposit> VALUES=new ArrayList<>();
    private ModDeposits(){}
    public static List<Deposit> all(){return List.copyOf(VALUES);}
    public static Deposit register(String name,float hardness){
        var entry=new Deposit(name,ModBlocks.register(name,BlockBehaviour.Properties.of().strength(hardness).requiresCorrectToolForDrops()),
            ModItems.register(name+"_chunk","item","copper","deposit_forms"),ModItems.register(name+"_dust","item","copper","deposit_forms"));
        VALUES.add(entry);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE,ModBlocks.id(name+"_deposit")));
        return entry;
    }
    public static void undergroundFluid(String name){
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE,ModBlocks.id(name)));
    }
}
