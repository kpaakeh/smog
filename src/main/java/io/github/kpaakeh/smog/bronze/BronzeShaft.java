package io.github.kpaakeh.smog.bronze;

import net.minecraft.core.Direction;

/** A bare shaft has an axis, not two different front/back model orientations. */
public final class BronzeShaft {
    private BronzeShaft() {}
    public static Direction canonical(Direction direction){return switch(direction.getAxis()){case X->Direction.EAST;case Y->Direction.UP;case Z->Direction.NORTH;};}
    public static Direction nextAxis(Direction direction){return switch(direction.getAxis()){case X->Direction.UP;case Y->Direction.NORTH;case Z->Direction.EAST;};}
    public static int handedness(Direction sourceFacing){return sourceFacing==canonical(sourceFacing)?1:-1;}
}
