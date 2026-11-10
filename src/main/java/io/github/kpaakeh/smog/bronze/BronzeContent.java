package io.github.kpaakeh.smog.bronze;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Chapter II registrations are separate from copper processes and from electrical test fixtures. */
public final class BronzeContent {
    public record Entry(String id,String group,String animation,boolean axis,boolean attachment,BronzeBlock block) {}
    private static final Map<String,Entry> BLOCKS=new LinkedHashMap<>();
    private static final Map<String,Item> ITEMS=new LinkedHashMap<>();
    public static BlockEntityType<BronzeEntity> ENTITY_TYPE;
    private BronzeContent() {}
    public static Collection<Entry> blocks(){return List.copyOf(BLOCKS.values());}
    public static Collection<Item> items(){return List.copyOf(ITEMS.values());}
    public static Entry entry(String id){return BLOCKS.get(id);}
    public static boolean contains(Item item){return ITEMS.containsValue(item)||BLOCKS.values().stream().anyMatch(e->e.block().asItem()==item);}
    public static void initialize(){
        try(var stream=BronzeContent.class.getResourceAsStream("/smog.bronze.json")){
            if(stream==null)throw new IllegalStateException("Missing bronze catalog");
            var root=JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
            for(var value:root.getAsJsonArray("blocks")){
                var o=value.getAsJsonObject();String id=o.get("id").getAsString();
                var block=ModBlocks.register(id,p->switch(id){case "bronze_shaft_support"->new BronzeSupportBlock(p);case "bronze_angle_gearbox"->new BronzeAngleBlock(p);case "bronze_clutch"->new BronzeClutchBlock(p);case "bronze_splitter"->new BronzeSplitterBlock(p);case "bronze_reduction_gearbox","bronze_cogwheel"->new BronzeGearBlock(p,id);default->new BronzeBlock(p,id);},BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.WOOD).noOcclusion().dynamicShape().requiresCorrectToolForDrops(),true);
                var entry=new Entry(id,o.get("group").getAsString(),o.get("animation").getAsString(),o.has("axis")&&o.get("axis").getAsBoolean(),o.has("attachment")&&o.get("attachment").getAsBoolean(),block);
                if(BLOCKS.putIfAbsent(id,entry)!=null)throw new IllegalStateException("Duplicate bronze block "+id);
            }
            ENTITY_TYPE=Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,ModBlocks.id("bronze_workshop"),new BlockEntityType<>(BronzeEntity::new,new HashSet<Block>(BLOCKS.values().stream().map(Entry::block).toList())));
            for(var value:root.getAsJsonArray("items")){
                var o=value.getAsJsonObject();String id=o.get("id").getAsString();
                var key=ModBlocks.id(id);var item=new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM,key)).stacksTo(o.has("stack")?o.get("stack").getAsInt():64));
                Registry.register(BuiltInRegistries.ITEM,key,item);if(ITEMS.putIfAbsent(id,item)!=null)throw new IllegalStateException("Duplicate bronze item "+id);
            }
            BronzeBlueprints.load(root.getAsJsonArray("blueprints"));
        }catch(IOException e){throw new IllegalStateException("Unable to load bronze catalog",e);}
    }
}
