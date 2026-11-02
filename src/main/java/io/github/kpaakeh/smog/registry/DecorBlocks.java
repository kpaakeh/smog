package io.github.kpaakeh.smog.registry;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

/** Decorative shapes use vanilla placement, waterlogging and collision implementations. */
public final class DecorBlocks {
    private static final List<Block> BLOCKS=new ArrayList<>();
    private static final class Stairs extends StairBlock {private Stairs(BlockState base,BlockBehaviour.Properties properties){super(base,properties);}}
    private DecorBlocks(){}
    public static List<Block> all(){return List.copyOf(BLOCKS);}
    public static void initialize(){
        try(var stream=DecorBlocks.class.getResourceAsStream("/decor.blocks.json")){
            if(stream==null)throw new IllegalStateException("Missing generated decorative families");
            for(var value:JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonArray()){
                var entry=value.getAsJsonObject();String name=entry.get("name").getAsString(),kind=entry.get("kind").getAsString();
                var parent=BuiltInRegistries.BLOCK.getValue(ModBlocks.id(entry.get("parent").getAsString()));
                var properties=BlockBehaviour.Properties.ofFullCopy(parent).requiresCorrectToolForDrops();
                Block block=switch(kind){case "stairs"->ModBlocks.register(name,p->new Stairs(parent.defaultBlockState(),p),properties,true);case "slab"->ModBlocks.register(name,SlabBlock::new,properties,true);case "wall"->ModBlocks.register(name,WallBlock::new,properties,true);case "cube"->ModBlocks.register(name,properties);default->throw new IllegalArgumentException(kind);};
                BLOCKS.add(block);
            }
        }catch(IOException exception){throw new IllegalStateException("Cannot read decorative families",exception);}
    }
}
