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
        if(kind.equals("fluid_canister"))properties.stacksTo(1);
        if(name.equals("seed_cake"))properties.component(net.minecraft.core.component.DataComponents.COMPOSTABLE,new net.minecraft.world.item.component.Compostable(ResourceKey.create(Registries.CONTEXT_INT_PROVIDER,net.minecraft.resources.Identifier.withDefaultNamespace("compostable/medium_high"))));
        if(name.equals("plant_oil_bottle"))properties.craftRemainder(net.minecraft.world.item.Items.GLASS_BOTTLE);
        if(name.equals("stone_work_hammer"))properties.stacksTo(1).durability(128);
        Item item=switch(kind){case "fluid_canister"->new io.github.kpaakeh.smog.copper.WorkshopCanisterItem(properties);case "rope"->new io.github.kpaakeh.smog.copper.RopeLinkItem(properties);case "handcart"->new io.github.kpaakeh.smog.copper.HandcartItem(properties);case "tool_part"->new io.github.kpaakeh.smog.copper.ToolPartItem(properties,io.github.kpaakeh.smog.copper.ToolDefinitions.part(id));case "modular_tool"->new io.github.kpaakeh.smog.copper.ModularToolItem(properties);case "field_journal"->new io.github.kpaakeh.smog.copper.FieldJournalItem(properties);case "wire"->new WireCoilItem(properties,ConductorMaterial.valueOf(conductor.toUpperCase(Locale.ROOT)));case "wrench","configurator"->new ConfiguratorItem(properties.stacksTo(1));default->new Item(properties);};
        Registry.register(BuiltInRegistries.ITEM,id,item);if(kind.equals("fluid_canister"))net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage.ITEM.registerForItems((stack,context)->new io.github.kpaakeh.smog.copper.FluidCanisterStorage(context),item);VALUES.add(new Entry(item,tab));return item;
    }
    public static List<Entry> all(){return List.copyOf(VALUES);}
}
