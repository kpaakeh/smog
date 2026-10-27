package io.github.kpaakeh.smog.render;

import java.util.*;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import io.github.kpaakeh.smog.energy.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.BlockPos;

public final class ClientWires {
    private static List<WireLink> links=List.of();
    private static String dimension="";
    private ClientWires(){}
    public static List<WireLink> from(BlockPos pos){return links.stream().filter(link->link.a().equals(pos)).toList();}
    public static void initialize(){
        ClientPlayNetworking.registerGlobalReceiver(WireSnapshot.TYPE,(payload,context)->{
            var json=JsonParser.parseString(payload.json()).getAsJsonObject();
            var parsed=WireLink.CODEC.listOf().parse(JsonOps.INSTANCE,json.get("links")).getOrThrow();
            var id=json.get("dimension").getAsString();
            context.client().execute(()->{if(context.client().level!=null&&context.client().level.dimension().identifier().toString().equals(id)){dimension=id;links=List.copyOf(parsed);}});
        });
        ClientTickEvents.END_CLIENT_TICK.register(client->{if(client.level==null||!client.level.dimension().identifier().toString().equals(dimension)){links=List.of();dimension="";}});
    }
}
