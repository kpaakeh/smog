package io.github.kpaakeh.smog.registry;

import java.util.*;
import com.google.gson.JsonObject;
import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.fluid.ModFluids;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;

/** Six disjoint catalog-owned categories. Late test registrations never enter their snapshots. */
public final class ModCreativeTabs {
    public static final List<String> IDS=List.of("copper_age","blocks","mechanisms","controllers","tool_parts","tools");
    private static final Set<String> MECHANISM_PARTS=Set.of("casting_faucet","copper_water_pipe","water_valve","water_outlet","bronze_drive");
    private static final List<String> EXAMPLE_MATERIALS=List.of("copper","bronze","iron","gold");
    private ModCreativeTabs() {}
    private static Item item(String name){return BuiltInRegistries.ITEM.getValue(ModBlocks.id(name));}
    public static void initialize(JsonObject catalog) {
        Map<String,LinkedHashSet<Item>> groups=new LinkedHashMap<>();IDS.forEach(id->groups.put(id,new LinkedHashSet<>()));
        Set<Item> assigned=new HashSet<>();
        java.util.function.BiConsumer<String,ItemLike> add=(category,value)->{Item item=value.asItem();if(item!=Items.AIR&&assigned.add(item))groups.get(category).add(item);};
        // Resources are captured once, before the separate test mod registers its fixtures.
        ModOres.all().forEach(ore->ore.contents().forEach(value->add.accept("blocks",value)));
        ModAlloys.all().forEach(alloy->alloy.contents().forEach(value->add.accept("blocks",value)));
        ModDeposits.all().forEach(deposit->{add.accept("blocks",deposit.block());add.accept("blocks",deposit.chunk());add.accept("blocks",deposit.dust());});
        for(var value:catalog.getAsJsonArray("blocks")){String name=value.getAsJsonObject().get("name").getAsString();add.accept(MECHANISM_PARTS.contains(name)?"mechanisms":"blocks",item(name));}
        for(var value:catalog.getAsJsonArray("machines")){var machine=value.getAsJsonObject();String name=machine.get("name").getAsString();add.accept(machine.has("multiblock")?"controllers":"mechanisms",item(name));}
        for(var value:catalog.getAsJsonArray("items")){
            var entry=value.getAsJsonObject();String name=entry.get("name").getAsString(),kind=entry.has("kind")?entry.get("kind").getAsString():"item";var item=item(name);
            if(item instanceof ModularToolItem)continue; // Only configured examples are discoverable.
            String category=item instanceof ToolPartItem?"tool_parts":Set.of("configurator","wrench","rope").contains(kind)?"tools":kind.equals("handcart")?"mechanisms":name.endsWith("_plate")?"blocks":"copper_age";
            add.accept(category,item);
        }
        ModFluids.all().forEach(fluid->add.accept(fluid.name.equals("plant_oil")?"copper_age":"blocks",fluid.bucket));
        for(String id:IDS){var contents=List.copyOf(groups.get(id));
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,ModBlocks.id(id),FabricCreativeModeTab.builder()
                .title(Component.translatable("itemGroup.smog."+id))
                .icon(()->id.equals("tools")?ToolParts.example(ToolDefinitions.template("pickaxe"),"copper"):contents.isEmpty()?Items.COPPER_INGOT.getDefaultInstance():contents.getFirst().getDefaultInstance())
                .displayItems((parameters,output)->{
                    contents.forEach(output::accept);
                    if(id.equals("tools"))for(var schema:ToolDefinitions.templates())for(String material:EXAMPLE_MATERIALS){var example=ToolParts.example(schema,material);if(!example.isEmpty())output.accept(example);}
                }).build());
        }
    }
}
