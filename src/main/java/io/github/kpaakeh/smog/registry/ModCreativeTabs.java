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

/** Functional categories and a curated chapter overview. Late test registrations never enter their snapshots. */
public final class ModCreativeTabs {
    public static final List<String> IDS=List.of("copper_age","materials","blocks","mechanisms","controllers","tool_parts","tools");
    private static final Set<String> MECHANISM_PARTS=Set.of("casting_spout","air_intake","kiln_vent","casting_channel","solid_firebox","casting_faucet","air_nozzle","copper_water_pipe","water_valve","water_outlet","bronze_drive","closed_crucible_insert","smeltery_item_port");
    private static final List<String> EXAMPLE_MATERIALS=List.of("copper","bronze","iron","gold");
    private ModCreativeTabs() {}
    private static Item item(String name){return BuiltInRegistries.ITEM.getValue(ModBlocks.id(name));}
    public static void initialize(JsonObject catalog,Set<Item> preexistingItems) {
        Map<String,LinkedHashSet<Item>> groups=new LinkedHashMap<>();IDS.forEach(id->groups.put(id,new LinkedHashSet<>()));
        Set<Item> assigned=new HashSet<>();
        java.util.function.BiConsumer<String,ItemLike> add=(category,value)->{Item item=value.asItem();if(item!=Items.AIR&&assigned.add(item))groups.get(category).add(item);};
        // Resources are captured once, before the separate test mod registers its fixtures.
        ModOres.all().forEach(ore->ore.contents().forEach(value->add.accept(value.asItem() instanceof BlockItem?"blocks":"materials",value)));
        ModAlloys.all().forEach(alloy->alloy.contents().forEach(value->add.accept(value.asItem() instanceof BlockItem?"blocks":"materials",value)));
        ModDeposits.all().forEach(deposit->{add.accept("blocks",deposit.block());add.accept("materials",deposit.chunk());add.accept("materials",deposit.dust());});
        for(var value:catalog.getAsJsonArray("blocks")){String name=value.getAsJsonObject().get("name").getAsString();add.accept(MECHANISM_PARTS.contains(name)?"mechanisms":"blocks",item(name));}
        for(var value:catalog.getAsJsonArray("machines")){var machine=value.getAsJsonObject();String name=machine.get("name").getAsString();add.accept(machine.has("multiblock")?"controllers":"mechanisms",item(name));}
        for(var value:catalog.getAsJsonArray("items")){
            var entry=value.getAsJsonObject();String name=entry.get("name").getAsString(),kind=entry.has("kind")?entry.get("kind").getAsString():"item";var item=item(name);
            if(item instanceof ModularToolItem)continue; // Only configured examples are discoverable.
            String category=item instanceof ToolPartItem?"tool_parts":Set.of("configurator","wrench","rope").contains(kind)?"tools":kind.equals("handcart")?"mechanisms":"materials";
            add.accept(category,item);
        }
        DecorBlocks.all().forEach(block->add.accept("blocks",block));
        ModFluids.all().forEach(fluid->add.accept("materials",fluid.bucket));
        // Audit the real production registry, not just the hand-maintained catalog lists.
        for(var value:BuiltInRegistries.ITEM){var key=BuiltInRegistries.ITEM.getKey(value);if(!key.getNamespace().equals("smog")||preexistingItems.contains(value)||value instanceof ModularToolItem)continue;
            add.accept(value instanceof ToolPartItem?"tool_parts":value instanceof BlockItem?"blocks":"materials",value);
        }
        // The chapter deliberately overlaps functional categories; tools and parts never do.
        var chapter=groups.get("copper_age");
        java.util.function.Consumer<ItemLike> chapterAdd=value->{var item=value.asItem();if(item!=Items.AIR&&!(item instanceof ToolPartItem)&&!(item instanceof ModularToolItem)&&!groups.get("tools").contains(item))chapter.add(item);};
        for(var value:catalog.getAsJsonArray("machines")){var entry=value.getAsJsonObject();if(entry.has("kind")&&entry.get("kind").getAsString().equals("COPPER_WORKSHOP"))chapterAdd.accept(item(entry.get("name").getAsString()));}
        for(var value:catalog.getAsJsonArray("blocks"))chapterAdd.accept(item(value.getAsJsonObject().get("name").getAsString()));
        for(var value:catalog.getAsJsonArray("items")){var entry=value.getAsJsonObject();if(!entry.has("tab")||entry.get("tab").getAsString().equals("copper_age"))chapterAdd.accept(item(entry.get("name").getAsString()));}
        for(String metal:List.of("copper","iron","gold"))for(String name:List.of(metal+"_ore","deepslate_"+metal+"_ore","raw_"+metal,"raw_"+metal+"_block",metal+"_ingot",metal+"_block"))chapterAdd.accept(BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.withDefaultNamespace(name)));
        ModOres.all().stream().filter(ore->Set.of("tin","lead").contains(ore.name())).forEach(ore->ore.contents().forEach(chapterAdd));
        ModAlloys.all().stream().filter(alloy->Set.of("bronze","wrought_iron").contains(alloy.name())).forEach(alloy->alloy.contents().forEach(chapterAdd));
        ModDeposits.all().forEach(deposit->{chapterAdd.accept(deposit.block());chapterAdd.accept(deposit.chunk());chapterAdd.accept(deposit.dust());});
        ModFluids.all().stream().filter(fluid->fluid.name.equals("plant_oil")).forEach(fluid->chapterAdd.accept(fluid.bucket));
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
