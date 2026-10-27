package io.github.kpaakeh.smog.energy;

import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.*;
import io.netty.buffer.ByteBuf;

public record WireSnapshot(String json) implements CustomPacketPayload {
    public static final Type<WireSnapshot> TYPE=new Type<>(ModBlocks.id("wires"));
    public static final StreamCodec<ByteBuf,WireSnapshot> CODEC=ByteBufCodecs.stringUtf8(262144).map(WireSnapshot::new,WireSnapshot::json);
    @Override public Type<WireSnapshot> type(){return TYPE;}
}
