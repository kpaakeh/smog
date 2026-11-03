package io.github.kpaakeh.smog.test;
import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
/** Texture swatches exist only in the test mod; none enter the production registry or JAR. */
public final class TextureReviewCards implements ModInitializer {
 public record Card(String id,String texture,Item item){}
 public static final List<Card> ALL=new ArrayList<>();
 @Override public void onInitialize(){
  try(var stream=TextureReviewCards.class.getResourceAsStream("/smog-texture-review.json")){
   for(var value:JsonParser.parseReader(new InputStreamReader(Objects.requireNonNull(stream),StandardCharsets.UTF_8)).getAsJsonArray()){
    var entry=value.getAsJsonObject();var id=Identifier.fromNamespaceAndPath("smog_tests",entry.get("id").getAsString());
    var item=Registry.register(BuiltInRegistries.ITEM,id,new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM,id))));ALL.add(new Card(id.getPath(),entry.get("texture").getAsString(),item));
   }
  }catch(IOException e){throw new IllegalStateException(e);}
 }
}
