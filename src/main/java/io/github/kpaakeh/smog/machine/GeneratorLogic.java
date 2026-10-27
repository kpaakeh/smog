package io.github.kpaakeh.smog.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

public final class GeneratorLogic {
    private GeneratorLogic() {}
    public static void tick(ServerLevel level,MachineEntity machine) {
        var structure=machine.structure();if(structure==null)return;
        var bounds=structure.bounds();var top=machine.getBlockPos().above(bounds.maxY()+1);
        if(machine.generator()==GeneratorKind.LIGHTNING) {
            for(var bolt:level.getEntitiesOfClass(LightningBolt.class,new AABB(machine.getBlockPos()).inflate(5,12,5)))
                if(machine.recordStrike(bolt.getUUID().toString()))machine.receive(50000);
            return;
        }
        if(machine.generator()==GeneratorKind.SOLAR) {
            if(level.dimensionType().hasSkyLight() && level.canSeeSky(top) && level.getBrightness(LightLayer.SKY,top)>=14
                && Math.floorMod(level.getDefaultClockTime(),24000)<12000)machine.receive(level.isRaining()?3:8);
            return;
        }
        if(machine.generator()==GeneratorKind.WIND) {
            if(level.canSeeSky(top) && top.getY()>=70)machine.receive(Math.min(96,8+(top.getY()-70)/3)+(level.isRaining()?8:0));
            return;
        }
        int hot=0,water=0,lava=0,moving=0;
        for(int x=bounds.minX()-1;x<=bounds.maxX()+1;x++)for(int y=bounds.minY()-1;y<=bounds.maxY()+1;y++)for(int z=bounds.minZ()-1;z<=bounds.maxZ()+1;z++) {
            BlockPos pos=machine.getBlockPos().offset(new BlockPos(x,y,z).rotate(machine.rotation()));
            if(!level.hasChunkAt(pos))continue;
            var state=level.getBlockState(pos);var fluid=state.getFluidState();
            if(fluid.is(FluidTags.LAVA)) {hot++;lava++;}
            if(state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.FIRE) || state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT))hot++;
            if(fluid.is(FluidTags.WATER)) {water++;if(!fluid.isSource() || fluid.getFlow(level,pos).lengthSqr()>.01)moving++;}
        }
        int production=switch(machine.generator()) {
            case THERMAL -> Math.min(32,hot*8);
            case FLOW -> Math.min(32,moving*4);
            case STEAM -> hot>0 && water>0?48:0;
            case GEOTHERMAL -> lava>0?64:0;
            default -> 0;
        };
        if(production>0)machine.receive(production);
    }
}
