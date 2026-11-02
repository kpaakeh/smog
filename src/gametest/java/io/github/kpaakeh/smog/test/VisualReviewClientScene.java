package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

/** World-only visual matrix. All fixtures and captures belong to a disposable test world. */
public final class VisualReviewClientScene {
    private static final BlockPos SMALL=new BlockPos(-22,101,0),LARGE=new BlockPos(8,101,0),FLOW=new BlockPos(-10,101,14),STAGES=new BlockPos(-18,100,8),CRANE=new BlockPos(26,104,15);
    private static Block block(String name){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name));}
    private static Item item(String name){return BuiltInRegistries.ITEM.getValue(ModBlocks.id(name));}
    private static MachineEntity machine(ServerLevel level,BlockPos pos){return (MachineEntity)level.getBlockEntity(pos);}
    private static MachineEntity hearth(ServerLevel level,BlockPos origin,int size,int height,boolean glass){
        int left=size/2;
        for(int x=-left;x<size-left;x++)for(int y=-1;y<height-1;y++)for(int z=0;z<size;z++){
            boolean wall=x==-left||x==size-left-1||z==0||z==size-1;var state=y==-1?Blocks.COBBLESTONE.defaultBlockState():wall?Blocks.BRICKS.defaultBlockState():Blocks.AIR.defaultBlockState();
            boolean corner=(x==-left||x==size-left-1)&&(z==0||z==size-1);
            if(glass&&wall&&!corner&&y>=0)state=block("smeltery_glass").defaultBlockState();
            level.setBlockAndUpdate(origin.offset(x,y,z),state);
        }
        level.setBlockAndUpdate(origin.offset(0,-1,1),block("solid_firebox").defaultBlockState());
        level.setBlockAndUpdate(origin.offset(-left,0,1),block("casting_spout").defaultBlockState());
        level.setBlockAndUpdate(origin.offset(left,0,1),block("air_intake").defaultBlockState());
        level.setBlockAndUpdate(origin,block("copper_hearth").defaultBlockState());var m=machine(level,origin);
        if(!m.tryAssemble())throw new IllegalStateException("Visual hearth did not assemble: "+size+"×"+height);
        int capacity=CopperWorkshops.capacity(m);m.copper.copper=capacity/2;m.copper.tin=capacity/5;m.copper.bronze=capacity/10;m.copper.heat=1000;m.enabled(false);m.sync();return m;
    }
    private static void camera(ClientGameTestContext context,net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext server,double x,double y,double z,BlockPos target,String name){
        server.runCommand("tp @a "+x+" "+y+" "+z+" 0 20");context.waitTicks(5);context.getInput().lookAt(target);context.waitTicks(5);context.takeScreenshot(name);
    }
    public static void run(ClientGameTestContext context){
        context.runOnClient(mc->{
            var title=net.minecraft.network.chat.Component.literal("Никелевый жаропрочный сплав: "+"ОченьДлинноеИмяБезПробелов".repeat(8)).withColor(0x71CFC3);
            var wrapped=io.github.kpaakeh.smog.gui.ToolTooltipLayout.wrap(java.util.List.of(title),mc.font,200);
            if(wrapped.size()<2||wrapped.stream().anyMatch(line->mc.font.width(line)>200))throw new IllegalStateException("Long titles and uninterrupted names must fit the tooltip width");
        });
        try(var world=context.worldBuilder().create()){
            var server=world.getServer();server.runCommand("fill -40 99 -15 40 99 30 minecraft:stone");server.runCommand("time set noon");server.runCommand("weather clear");
            server.runOnServer(s->{
                var level=s.overworld();var p=s.getPlayerList().getPlayers().getFirst();p.setGameMode(net.minecraft.world.level.GameType.CREATIVE);p.getAbilities().flying=true;p.onUpdateAbilities();
                hearth(level,SMALL,3,3,true);hearth(level,LARGE,11,11,true);
                // A contained vanilla-water column next to the large translucent shell.
                var water=LARGE.offset(-7,0,-2);for(int x=-1;x<=1;x++)for(int y=-1;y<=2;y++)for(int z=-1;z<=1;z++)level.setBlockAndUpdate(water.offset(x,y,z),y==-1?Blocks.STONE.defaultBlockState():x==0&&z==0?Blocks.WATER.defaultBlockState():Blocks.GLASS.defaultBlockState());
                var tank=SMALL.offset(4,0,0);level.setBlockAndUpdate(tank,block("smeltery_fuel_tank").defaultBlockState());var fuel=machine(level,tank);fuel.smeltery.fuel="smog:blazing_fuel";fuel.smeltery.fuelAmount=6*81000;fuel.sync();
            });
            for(var origin:java.util.List.of(SMALL,LARGE)){
                int size=origin.equals(SMALL)?3:11;double distance=size*1.15+2,cy=origin.getY()+size*.45;var focus=origin.offset(0,size/2,size/2);
                camera(context,server,origin.getX()+.5,cy,origin.getZ()+size/2.0-distance,focus,"visual-smeltery-"+size+"-north");
                camera(context,server,origin.getX()+.5+distance,cy,origin.getZ()+size/2.0,focus,"visual-smeltery-"+size+"-east");
                camera(context,server,origin.getX()+.5,cy,origin.getZ()+size/2.0+distance,focus,"visual-smeltery-"+size+"-south");
                camera(context,server,origin.getX()+.5-distance,cy,origin.getZ()+size/2.0,focus,"visual-smeltery-"+size+"-west");
            }
            server.runCommand("time set 13000");camera(context,server,LARGE.getX()-13,107,-7,LARGE.offset(0,4,4),"visual-glass-water-metals-dusk");server.runCommand("time set noon");
            camera(context,server,SMALL.getX()+6.5,103,-3,SMALL.offset(4,0,0),"visual-fuel-tank-liquid-sides");
            server.runOnServer(s->{
                var level=s.overworld();hearth(level,FLOW,5,3,false);
                level.setBlockAndUpdate(FLOW.offset(2,0,1),block("casting_spout").defaultBlockState());
                // The replacement stays a valid functional wall insert.
                var source=machine(level,FLOW);source.disassemble();if(!source.tryAssemble())throw new IllegalStateException("Two-spout flow source failed");source.copper.copper=CopperWorkshops.capacity(source)/2;source.sync();
                for(boolean direct:new boolean[]{true,false}){
                    var faucet=FLOW.offset(direct?-3:3,0,1);level.setBlockAndUpdate(faucet,block("casting_faucet").defaultBlockState().setValue(WorkshopPartBlock.FACING,direct?Direction.WEST:Direction.EAST).setValue(WorkshopPartBlock.OPEN,true));
                    var target=FLOW.offset(direct?-3:5,-1,1);level.setBlockAndUpdate(target,block("casting_basin").defaultBlockState());
                    if(!direct)for(int dx=3;dx<=4;dx++)level.setBlockAndUpdate(FLOW.offset(dx,-1,1),block("casting_channel").defaultBlockState().setValue(WorkshopPartBlock.FACING,Direction.EAST));
                }
            });
            camera(context,server,-9.5,104,10,FLOW.offset(0,0,1),"visual-flow-direct-and-channel");
            server.runOnServer(s->{for(var target:java.util.List.of(FLOW.offset(-3,-1,1),FLOW.offset(5,-1,1))){var m=machine(s.overworld(),target);if(m.smeltery.filled<=0)throw new IllegalStateException("Visual flow has no actual received metal: "+target);m.enabled(false);m.sync();}});
            server.runOnServer(s->{var level=s.overworld();level.setBlockAndUpdate(STAGES,block("casting_table").defaultBlockState());var m=machine(level,STAGES);m.enabled(false);m.setItem(1,new ItemStack(item("ingot_mold")));m.sync();});
            camera(context,server,-15.5,102,5.5,STAGES,"visual-casting-empty");
            server.runOnServer(s->{var m=machine(s.overworld(),STAGES);try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){if(m.castingFluid.insert(MoltenMetalStorage.variant(0),4500,tx)!=4500)throw new IllegalStateException("Partial visual cast rejected");tx.commit();}m.sync();});
            context.waitTicks(6);context.takeScreenshot("visual-casting-partial-hot");
            server.runOnServer(s->{var m=machine(s.overworld(),STAGES);try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){m.castingFluid.insert(MoltenMetalStorage.variant(0),4500,tx);tx.commit();}for(int i=0;i<10;i++)CastingProcesses.tick(s.overworld(),m);if(CastingProcesses.stage(m)!=3)throw new IllegalStateException("Cooling fixture is not actually cooling");m.sync();});
            context.waitTicks(6);context.takeScreenshot("visual-casting-cooling");
            server.runOnServer(s->{var m=machine(s.overworld(),STAGES);for(int i=0;i<80;i++)CastingProcesses.tick(s.overworld(),m);if(!m.getItem(3).is(Items.COPPER_INGOT))throw new IllegalStateException("Ready fixture produced no ingot");m.sync();});
            context.waitTicks(6);context.takeScreenshot("visual-casting-ready");
            server.runOnServer(s->{var level=s.overworld();Multiblocks.get(ModBlocks.id("loading_crane")).parts().forEach((offset,part)->level.setBlockAndUpdate(CRANE.offset(offset),part.block().defaultBlockState()));level.setBlockAndUpdate(CRANE,block("loading_crane").defaultBlockState());var m=machine(level,CRANE);if(!m.tryAssemble())throw new IllegalStateException("Visual crane did not assemble");m.enabled(false);for(boolean second:new boolean[]{false,true})level.setBlockAndUpdate(LoadingCrane.bay(m,second),Blocks.BARREL.defaultBlockState());((Container)level.getBlockEntity(LoadingCrane.bay(m,false))).setItem(0,new ItemStack(Items.COBBLESTONE,64));LoadingCrane.queue(m);m.sync();});
            camera(context,server,32.5,105,10.5,CRANE.offset(0,-2,2),"visual-crane-start");
            int[] steps={25,45,50,50};String[] phases={"pickup","raised-travel","delivery","finished"};
            for(int phase=0;phase<steps.length;phase++){int ticks=steps[phase];server.runOnServer(s->{var m=machine(s.overworld(),CRANE);for(int tick=0;tick<ticks;tick++)LoadingCrane.tick(s.overworld(),m);m.sync();});context.waitTicks(6);context.takeScreenshot("visual-crane-"+phases[phase]);}
            server.runOnServer(s->{var m=machine(s.overworld(),CRANE);if(((Container)s.overworld().getBlockEntity(LoadingCrane.bay(m,true))).getItem(0).getCount()!=64)throw new IllegalStateException("Visual crane never delivered its cargo");});
            server.runCommand("time set noon");server.runCommand("fill -4 99 46 34 99 84 minecraft:stone");
            server.runOnServer(s->{var blocks=io.github.kpaakeh.smog.registry.DecorBlocks.all();for(int index=0;index<blocks.size();index++)s.overworld().setBlockAndUpdate(new BlockPos((index%14)*2,100,50+(index/14)*2),blocks.get(index).defaultBlockState());});
            camera(context,server,13,123,40,new BlockPos(13,100,64),"visual-decor-all-families");
            camera(context,server,7,104,45,new BlockPos(7,100,52),"visual-decor-refractory-close");
        }
    }
}
