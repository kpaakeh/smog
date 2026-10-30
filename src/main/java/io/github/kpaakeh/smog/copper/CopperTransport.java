package io.github.kpaakeh.smog.copper;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import io.github.kpaakeh.smog.registry.ModBlocks;

public final class CopperTransport {
    public static final EntityType<HandcartEntity> HANDCART=Registry.register(BuiltInRegistries.ENTITY_TYPE,ModBlocks.id("handcart"),EntityType.Builder.<HandcartEntity>of(HandcartEntity::new,MobCategory.MISC).sized(1.25f,1.05f).clientTrackingRange(8).updateInterval(2).build(ResourceKey.create(Registries.ENTITY_TYPE,ModBlocks.id("handcart"))));
    public static final EntityType<RopewayRideEntity> ROPEWAY_RIDE=Registry.register(BuiltInRegistries.ENTITY_TYPE,ModBlocks.id("ropeway_ride"),EntityType.Builder.<RopewayRideEntity>of(RopewayRideEntity::new,MobCategory.MISC).sized(.6f,.1f).clientTrackingRange(10).updateInterval(1).build(ResourceKey.create(Registries.ENTITY_TYPE,ModBlocks.id("ropeway_ride"))));
    public static void initialize(){}
}
