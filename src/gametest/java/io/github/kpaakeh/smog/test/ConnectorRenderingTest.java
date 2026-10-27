package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.energy.*;
import io.github.kpaakeh.smog.machine.ConnectorBlock;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.client.gametest.v1.context.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;

public final class ConnectorRenderingTest {
    public static void run(ClientGameTestContext context,TestSingleplayerContext world) {
        world.getServer().runOnServer(server->{
            var level=server.overworld();
            for(var face:Direction.values()) {
                var pos=new BlockPos(-8+face.ordinal()*4,148,20);
                level.setBlockAndUpdate(pos.relative(face.getOpposite()),Blocks.STONE.defaultBlockState());
                level.setBlockAndUpdate(pos,BuiltInRegistries.BLOCK.getValue(ModBlocks.id(ConductorMaterial.values()[face.ordinal()].connectorId())).defaultBlockState().setValue(ConnectorBlock.ATTACHMENT,face));
            }
            for(int x:new int[]{0,4}) {
                var pos=new BlockPos(x,146,20);var face=x==0?Direction.NORTH:Direction.SOUTH;
                level.setBlockAndUpdate(pos.relative(face.getOpposite()),Blocks.STONE.defaultBlockState());
                level.setBlockAndUpdate(pos,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("connector_lv")).defaultBlockState().setValue(ConnectorBlock.ATTACHMENT,face));
            }
            WireData.get(level).add(new WireLink(new BlockPos(0,146,20),new BlockPos(4,146,20),ConductorMaterial.COPPER,Direction.NORTH,Direction.SOUTH));
        });
        world.getServer().runCommand("tp @a 2.5 151 9.5 0 15");context.getInput().lookAt(new BlockPos(2,148,20));context.waitTicks(50);
        context.waitFor(mc->!io.github.kpaakeh.smog.render.ClientWires.from(new BlockPos(0,146,20)).isEmpty());
        context.runOnClient(mc->{
            for(var face:Direction.values())if(ConnectorBlock.attachment(mc.level.getBlockState(new BlockPos(-8+face.ordinal()*4,148,20)))!=face)throw new IllegalStateException("Attachment state did not synchronize: "+face);
            var link=io.github.kpaakeh.smog.render.ClientWires.from(new BlockPos(0,146,20)).getFirst();
            if(link.aFace()!=Direction.NORTH||link.bFace()!=Direction.SOUTH)throw new IllegalStateException("Wire tip orientation did not synchronize");
        });
        context.takeScreenshot("connectors-all-six-faces");
    }
}
