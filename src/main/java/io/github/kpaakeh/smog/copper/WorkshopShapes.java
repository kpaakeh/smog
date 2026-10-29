package io.github.kpaakeh.smog.copper;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.*;

/** Collision/selection follows the prototype silhouettes instead of filling their empty space. */
public final class WorkshopShapes {
    private static VoxelShape boxes(double[]... boxes){VoxelShape result=Shapes.empty();for(var b:boxes)result=Shapes.or(result,Shapes.box(b[0]/16,b[1]/16,b[2]/16,b[3]/16,b[4]/16,b[5]/16));return result;}
    public static VoxelShape shape(String name,Direction facing){
        VoxelShape shape=switch(name){
            case "water_valve"->boxes(new double[]{5,5,0,11,11,16},new double[]{3,11,3,13,15,13});
            case "water_outlet"->boxes(new double[]{5,5,8,11,11,16},new double[]{5,0,5,11,11,11},new double[]{3,11,3,13,15,13});
            case "casting_faucet"->boxes(new double[]{4,6,12,12,12,16},new double[]{6,3,6,10,11,16},new double[]{4,0,4,12,3,12},new double[]{3,13,3,13,15,13});
            case "air_nozzle"->boxes(new double[]{4,4,11,12,12,16},new double[]{4,4,0,12,12,12},new double[]{3,13,3,13,15,13});
            case "casting_channel"->boxes(new double[]{0,12,0,16,14,16},new double[]{0,14,0,3,16,16},new double[]{13,14,0,16,16,16});
            case "potter_table","tool_workbench"->boxes(new double[]{0,11,0,16,16,16},new double[]{1,0,1,4,11,4},new double[]{12,0,1,15,11,4},new double[]{1,0,12,4,11,15},new double[]{12,0,12,15,11,15});
            case "casting_table"->boxes(new double[]{0,0,0,3,11,16},new double[]{13,0,0,16,11,16},new double[]{0,9,0,16,12,16});
            case "hand_bellows"->boxes(new double[]{1,0,1,15,13,15},new double[]{0,4,5,3,9,11});
            case "washing_trough","washing_section","washing_sieve"->boxes(new double[]{0,3,0,16,5,16},new double[]{0,5,0,2,13,16},new double[]{14,5,0,16,13,16},new double[]{2,5,0,14,13,2});
            case "workshop_support"->boxes(new double[]{1,0,1,5,16,5},new double[]{11,0,11,15,16,15},new double[]{0,9,0,16,12,16});
            case "press_frame"->boxes(new double[]{0,0,0,4,16,4},new double[]{12,0,12,16,16,16},new double[]{0,12,0,16,16,16});
            case "bronze_drive"->boxes(new double[]{3,3,6,13,13,16},new double[]{6,6,0,10,10,6},new double[]{1,1,1,15,15,3});
            case "press_basket","fiber_press_basket"->boxes(new double[]{1,0,1,15,3,15},new double[]{1,3,1,3,13,15},new double[]{13,3,1,15,13,15},new double[]{3,3,1,13,13,3},new double[]{3,3,13,13,13,15});
            case "lever_press"->boxes(new double[]{0,0,0,16,4,16},new double[]{1,4,1,4,16,4},new double[]{12,4,12,15,16,15},new double[]{4,6,4,12,8,12},new double[]{7,8,7,9,16,9},new double[]{3,14,7,13,16,9});
            case "water_tank"->boxes(new double[]{0,0,0,16,3,16},new double[]{0,3,0,2,16,16},new double[]{14,3,0,16,16,16},new double[]{2,3,0,14,16,2},new double[]{2,3,14,14,16,16});
            case "hand_water_pump"->boxes(new double[]{3,0,3,13,3,13},new double[]{6,3,6,10,12,10},new double[]{7,12,7,9,16,9},new double[]{2,14,7,14,16,9},new double[]{8,7,2,10,9,6});
            default->null;
        };
        if(shape==null)return null;int turns=switch(facing){case EAST->1;case SOUTH->2;case WEST->3;default->0;};
        for(int i=0;i<turns;i++){final VoxelShape[] next={Shapes.empty()};shape.forAllBoxes((x,y,z,xx,yy,zz)->next[0]=Shapes.or(next[0],Shapes.box(1-zz,y,x,1-z,yy,xx)));shape=next[0];}return shape;
    }
}
