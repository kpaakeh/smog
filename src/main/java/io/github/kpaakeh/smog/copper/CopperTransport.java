package io.github.kpaakeh.smog.copper;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import io.github.kpaakeh.smog.registry.ModBlocks;

public final class CopperTransport {
    public static final EntityType<HandcartEntity> HANDCART=Registry.register(BuiltInRegistries.ENTITY_TYPE,ModBlocks.id("handcart"),EntityType.Builder.<HandcartEntity>of(HandcartEntity::new,MobCategory.MISC).sized(1.25f,1.05f).clientTrackingRange(8).updateInterval(2).build(ResourceKey.create(Registries.ENTITY_TYPE,ModBlocks.id("handcart"))));
    public static void initialize(){}
}
