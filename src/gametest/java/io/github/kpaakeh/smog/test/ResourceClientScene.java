package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.registry.ModDeposits;
import io.github.kpaakeh.smog.fluid.ModFluids;
import net.fabricmc.fabric.api.client.gametest.v1.context.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public final class ResourceClientScene {
    public static void run(ClientGameTestContext context,TestSingleplayerContext world) {
        world.getServer().runOnServer(server->{
            var level=server.overworld();
            for(int x=-10;x<=10;x++)for(int z=10;z<=20;z++)level.setBlockAndUpdate(new BlockPos(x,103,z),Blocks.STONE.defaultBlockState());
            for(int i=0;i<ModDeposits.all().size();i++)level.setBlockAndUpdate(new BlockPos(-7+i*2,104,12),ModDeposits.all().get(i).block().defaultBlockState());
            for(String name:new String[]{"crude_oil","mineral_brine"}) {
                var fluid=ModFluids.all().stream().filter(f->f.name.equals(name)).findFirst().orElseThrow();int center=name.equals("crude_oil")?-4:4;
                for(int x=center-1;x<=center+1;x++)for(int z=16;z<=18;z++)level.setBlockAndUpdate(new BlockPos(x,104,z),x==center&&z==17?fluid.block.defaultBlockState():Blocks.STONE.defaultBlockState());
            }
            var player=server.getPlayerList().getPlayers().getFirst();player.getAbilities().flying=true;player.onUpdateAbilities();
            player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,new ItemStack(ModFluids.all().stream().filter(f->f.name.equals("crude_oil")).findFirst().orElseThrow().bucket));
        });
        world.getServer().runCommand("tp @a 0.5 107 8.5 0 25");context.getInput().lookAt(new BlockPos(0,104,15));context.waitTicks(40);
        context.waitFor(mc->!mc.level.getFluidState(new BlockPos(-4,104,17)).isEmpty()&&!mc.level.getFluidState(new BlockPos(4,104,17)).isEmpty());
        context.takeScreenshot("resource-deposits-and-fluids");
    }
}
