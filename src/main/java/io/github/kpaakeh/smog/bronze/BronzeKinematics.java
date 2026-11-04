package io.github.kpaakeh.smog.bronze;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/** Angular velocity at a port is measured around its outward normal. */
public final class BronzeKinematics {
    private BronzeKinematics(){}
    public static RotationRate port(BlockState state,Direction side){
        var facing=state.hasProperty(BronzeBlock.FACING)?state.getValue(BronzeBlock.FACING):state.getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING);
        if(state.getBlock() instanceof BronzeAngleBlock)return RotationRate.of(side==facing.getOpposite()?1:-1,1);
        if(state.getBlock() instanceof BronzeBlock block&&(block.id().equals("bronze_shaft")||block.id().equals("bronze_shaft_support")||block instanceof BronzeGearBlock))facing=BronzeShaft.canonical(facing);
        // The splitter is still a prototype; its perpendicular axes are completed in its own pass.
        if(side.getAxis()!=facing.getAxis())return RotationRate.of(-BronzeShaft.handedness(side),1);
        return RotationRate.of(side==facing.getOpposite()?1:-1,1);
    }
    public static RotationRate across(BlockState from,Direction side,BlockState to){
        if(from.getBlock() instanceof BronzeGearBlock a&&to.getBlock() instanceof BronzeGearBlock b&&side.getAxis()!=from.getValue(BronzeBlock.FACING).getAxis())return RotationRate.of(-a.teeth(),b.teeth());
        return port(from,side).negate().divide(port(to,side.getOpposite()));
    }
}
