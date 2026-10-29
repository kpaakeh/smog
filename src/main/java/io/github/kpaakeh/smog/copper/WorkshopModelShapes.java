package io.github.kpaakeh.smog.copper;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.*;

/** Read the artist's cuboids on both sides. Rotated handles use quarter-pixel collision steps. */
public final class WorkshopModelShapes {
    private static final Map<String,VoxelShape> CACHE=new HashMap<>();
    private static double[] vector(JsonArray a){return new double[]{a.get(0).getAsDouble(),a.get(1).getAsDouble(),a.get(2).getAsDouble()};}
    public static VoxelShape get(String model,Direction facing){return CACHE.computeIfAbsent(model+":"+facing,key->rotate(load(model),facing));}
    private static VoxelShape load(String model){
        try(var stream=WorkshopModelShapes.class.getResourceAsStream("/assets/smog/models/block/"+model+".json")){
            if(stream==null)throw new IllegalStateException("Missing collision model "+model);
            var root=JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();var result=Shapes.empty();
            for(var entry:root.getAsJsonArray("elements")){
                var e=entry.getAsJsonObject();double[] a=vector(e.getAsJsonArray("from")),b=vector(e.getAsJsonArray("to"));var rotation=e.has("rotation")?e.getAsJsonObject("rotation"):null;double angle=rotation==null?0:rotation.get("angle").getAsDouble();
                if(angle==0){result=Shapes.or(result,Shapes.box(a[0]/16,a[1]/16,a[2]/16,b[0]/16,b[1]/16,b[2]/16));continue;}
                var origin=vector(rotation.getAsJsonArray("origin"));int axis=switch(rotation.get("axis").getAsString()){case "x"->0;case "y"->1;default->2;};int u=(axis+1)%3,v=(axis+2)%3;double radians=Math.toRadians(angle),sin=Math.sin(radians),cos=Math.cos(radians);double scale=rotation.has("rescale")&&rotation.get("rescale").getAsBoolean()?1/Math.abs(cos):1;
                double minU=100,minV=100,maxU=-100,maxV=-100;
                for(double first:new double[]{a[u],b[u]})for(double second:new double[]{a[v],b[v]}){double uu=(first-origin[u])*scale,vv=(second-origin[v])*scale;double ru=origin[u]+uu*cos-vv*sin,rv=origin[v]+uu*sin+vv*cos;minU=Math.min(minU,ru);maxU=Math.max(maxU,ru);minV=Math.min(minV,rv);maxV=Math.max(maxV,rv);}
                for(double uu=Math.floor(minU*4)/4;uu<maxU;uu+=.25)for(double vv=Math.floor(minV*4)/4;vv<maxV;vv+=.25){double x=uu+.125-origin[u],y=vv+.125-origin[v];double inverseU=origin[u]+(x*cos+y*sin)/scale,inverseV=origin[v]+(-x*sin+y*cos)/scale;if(inverseU<a[u]||inverseU>b[u]||inverseV<a[v]||inverseV>b[v])continue;double[] low=a.clone(),high=b.clone();low[u]=uu;high[u]=Math.min(uu+.25,maxU);low[v]=vv;high[v]=Math.min(vv+.25,maxV);result=Shapes.or(result,Shapes.box(low[0]/16,low[1]/16,low[2]/16,high[0]/16,high[1]/16,high[2]/16));}
            }return result.optimize();
        }catch(IOException e){throw new IllegalStateException("Cannot read collision model "+model,e);}
    }
    private static VoxelShape rotate(VoxelShape shape,Direction facing){int turns=switch(facing){case EAST->1;case SOUTH->2;case WEST->3;default->0;};for(int i=0;i<turns;i++){var parts=new ArrayList<VoxelShape>();shape.forAllBoxes((x,y,z,xx,yy,zz)->parts.add(Shapes.box(1-zz,y,x,1-z,yy,xx)));shape=parts.stream().reduce(Shapes.empty(),Shapes::or);}return shape;}
}
