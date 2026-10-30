package io.github.kpaakeh.smog.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.kpaakeh.smog.copper.LoadingCrane;
import io.github.kpaakeh.smog.machine.MachineEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.*;
import net.minecraft.world.item.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomModelData;
import java.util.*;

/** Editable JSON segments retain native pixel density over the entire five-block gantry. */
public final class CraneRendering {
    public record Part(ItemStackRenderState model,float x,float y,float z,float yaw,float roll){}
    public record State(List<Part> parts,float yaw,float z,float hookY,boolean load,int light){}
    public static State extract(MachineEntity machine,ItemModelResolver resolver,float partial){
        if(!LoadingCrane.handles(machine)||!machine.assembled())return null;
        var models=new HashMap<String,ItemStackRenderState>();
        for(String name:List.of("post","beam","rail","brace","trolley","drum","hook","cargo","marker_a","marker_b","redstone_marker")){
            var stack=new ItemStack(machine.getBlockState().getBlock());stack.set(DataComponents.CUSTOM_MODEL_DATA,new CustomModelData(List.of(),List.of(),List.of(name),List.of()));
            var model=new ItemStackRenderState();resolver.updateForTopItem(model,stack,ItemDisplayContext.NONE,machine.getLevel(),null,0);models.put(name,model);
        }
        var parts=new ArrayList<Part>();
        for(int x:new int[]{-2,2})for(int z:new int[]{0,4}){
            for(int y=-4;y<0;y++)parts.add(new Part(models.get("post"),x+.5f,y+.5f,z+.5f,0,0));
            parts.add(new Part(models.get("brace"),x+(x<0?1.015f:-.015f),-.5f,z+.5f,x<0?0:180,0));
        }
        for(int z:new int[]{0,4})for(int x=-2;x<=2;x++)parts.add(new Part(models.get("beam"),x+.5f,.5f,z+.5f,0,0));
        for(int z=0;z<=4;z++)parts.add(new Part(models.get("rail"),.5f,.51f,z+.5f,0,0));
        parts.add(new Part(models.get("marker_a"),.5f,-3.5f,1.5f,0,0));parts.add(new Part(models.get("marker_b"),.5f,-3.5f,3.5f,0,0));
        parts.add(new Part(models.get("redstone_marker"),.5f,.5f,.5f,0,0));
        float t=machine.copper.queued==0?0:Math.clamp((machine.copper.work+(machine.copper.status==0?partial:0))/LoadingCrane.duration(machine),0,1);
        float travel=t<.3f?0:t<.6f?smooth((t-.3f)/.3f):t<.9f?1:1-smooth((t-.9f)/.1f);
        float lower=t<.15f?smooth(t/.15f):t<.3f?1-smooth((t-.15f)/.15f):t<.6f?0:t<.75f?smooth((t-.6f)/.15f):t<.9f?1-smooth((t-.75f)/.15f):0;
        if(machine.copper.queued==0)lower=0;
        if(machine.copper.selection%2==1)travel=1-travel;
        float z=1.5f+2*travel,hook=-1.65f-.85f*lower;
        parts.add(new Part(models.get("trolley"),.5f,.1f,z,0,0));
        parts.add(new Part(models.get("drum"),.5f,-.85f,z,0,lower*360));
        parts.add(new Part(models.get("hook"),.5f,hook,z,0,0));
        if(!machine.getItem(1).isEmpty())parts.add(new Part(models.get("cargo"),.5f,hook-.65f,z,0,0));
        float yaw=switch(machine.rotation()){case CLOCKWISE_90->-90;case CLOCKWISE_180->180;case COUNTERCLOCKWISE_90->90;default->0;};
        int light=net.minecraft.util.LightCoordsUtil.pack(machine.getLevel().getBrightness(net.minecraft.world.level.LightLayer.BLOCK,machine.getBlockPos().above()),machine.getLevel().getBrightness(net.minecraft.world.level.LightLayer.SKY,machine.getBlockPos().above()));
        return new State(List.copyOf(parts),yaw,z,hook,!machine.getItem(1).isEmpty(),light);
    }
    private static float smooth(float t){t=Math.clamp(t,0,1);return t*t*(3-2*t);}
    public static void submit(State state,PoseStack pose,SubmitNodeCollector collector){
        pose.pushPose();pose.translate(.5,0,.5);pose.rotate(Axis.YP.rotationDegrees(state.yaw()));pose.translate(-.5,0,-.5);
        for(var part:state.parts()){pose.pushPose();pose.translate(part.x(),part.y(),part.z());if(part.yaw()!=0)pose.rotate(Axis.YP.rotationDegrees(part.yaw()));if(part.roll()!=0)pose.rotate(Axis.XP.rotationDegrees(part.roll()));part.model().submit(pose,collector,state.light(),net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,0);pose.popPose();}
        for(double x:new double[]{.35,.65})RopeRendering.straight(new net.minecraft.world.phys.Vec3(x,-.85,state.z()),new net.minecraft.world.phys.Vec3(x,state.hookY()+.5,state.z()),pose,collector,state.light());pose.popPose();
    }
}
