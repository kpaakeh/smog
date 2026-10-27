package io.github.kpaakeh.smog.registry;

import java.util.*;
import io.github.kpaakeh.smog.energy.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ModItems {
    public record Entry(Item item,String tab){}
    private static final List<Entry> VALUES=new ArrayList<>();
    private ModItems(){}
    public static Item register(String name,String kind,String conductor,String tab){
        var id=ModBlocks.id(name);var properties=new Item.Properties().setId(ResourceKey.create(Registries.ITEM,id));
        Item item=switch(kind){case "wire"->new WireCoilItem(properties,ConductorMaterial.valueOf(conductor.toUpperCase(Locale.ROOT)));case "wrench","configurator"->new ConfiguratorItem(properties.stacksTo(1));default->new Item(properties);};
        Registry.register(BuiltInRegistries.ITEM,id,item);VALUES.add(new Entry(item,tab));return item;
    }
    public static List<Entry> all(){return List.copyOf(VALUES);}
}
