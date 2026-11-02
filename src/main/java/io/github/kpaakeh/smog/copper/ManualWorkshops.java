package io.github.kpaakeh.smog.copper;

import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.*;

/** A short manual action orders finite batches. Work pauses without losing input or fluid. */
public final class ManualWorkshops {
    // Match CopperState's persisted 0..8 bound. Four-item recipes can fill two orders per stack.
    public static final int MAX_QUEUED_BATCHES=8;
    public static boolean handles(MachineEntity m){return CopperWorkshops.workshop(m,"washing_trough")||CopperWorkshops.workshop(m,"lever_press");}
    public static boolean fiber(MachineEntity m){return !m.processFluid.washing()&&WorkshopModules.mode(m,"basket","oil").equals("fiber");}
    public static boolean accepts(MachineEntity m,ItemStack stack){return m.processFluid.washing()?WashingRecipes.find(stack)!=null:stack.is(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM,ModBlocks.id(fiber(m)?"fiber_sources":"oil_seeds")));}
    public static int batchSize(MachineEntity m){var recipe=m.processFluid.washing()?WashingRecipes.find(m.getItem(0)):null;return recipe==null?8:recipe.count();}
    public static int workTime(MachineEntity m){var recipe=m.processFluid.washing()?WashingRecipes.find(m.getItem(0)):null;return recipe==null?100:recipe.ticks();}
    public static int queue(MachineEntity m){m.copper.autoWork=false;if(m.copper.queued==0&&accepts(m,m.getItem(0)))m.copper.queued=Math.min(MAX_QUEUED_BATCHES,m.getItem(0).getCount()/batchSize(m));m.sync();return m.copper.queued;}
    public static ItemStack result(MachineEntity m){if(m.processFluid.washing()){var recipe=WashingRecipes.find(m.getItem(0));return recipe==null?ItemStack.EMPTY:recipe.result();}return new ItemStack(BuiltInRegistries.ITEM.getValue(ModBlocks.id(fiber(m)?"plant_fiber":"seed_cake")),fiber(m)?4:1);}
    public static void tick(ServerLevel level,MachineEntity m){
        var s=m.copper;if(!m.assembled())return;boolean wash=m.processFluid.washing(),fibers=fiber(m);var recipe=wash?WashingRecipes.find(m.getItem(0)):null;String mode=wash?"wash:"+(recipe==null?"":recipe.input()):fibers?"fiber":"oil";if(!s.workKey.equals(mode)){s.work=0;s.workKey=mode;}
        boolean drive=!m.processFluid.washing()&&BronzeDrive.connected(m);
        if(s.queued==0&&drive&&m.getItem(0).getCount()>=8&&accepts(m,m.getItem(0))){queue(m);s.autoWork=true;}
        if(s.queued>0&&s.autoWork&&!drive){s.status=8;return;}
        if(s.queued<=0){s.work=0;return;}
        var input=m.getItem(0);int batch=batchSize(m);if(!accepts(m,input)||input.getCount()<batch){s.queued=0;s.work=0;s.status=2;m.sync();return;}
        int fluid=wash?recipe.water():WorkshopFluidStorage.BUCKET/4;
        if(!fibers&&(wash?s.processFluid<fluid:s.processFluid>WorkshopFluidStorage.CAPACITY-fluid)){s.status=8;return;}
        var output=result(m);if(!m.canOutput(output)){s.status=5;return;}
        s.status=7;s.work++;m.setChanged();
        if(level.getGameTime()%20==0)level.sendParticles(wash?net.minecraft.core.particles.ParticleTypes.SPLASH:net.minecraft.core.particles.ParticleTypes.CRIT,m.getBlockPos().getX()+.5,m.getBlockPos().getY()+.8,m.getBlockPos().getZ()+.5,2,.2,.05,.2,0);
        if(s.work<(s.air>0&&!wash?80:workTime(m)))return;
        if(!wash&&s.air>0)s.air--;input.shrink(batch);s.processFluid+=fibers?0:wash?-fluid:fluid;s.queued--;s.work=0;s.status=0;
        if(m.getItem(3).isEmpty())m.setItem(3,output);else m.getItem(3).grow(output.getCount());m.sync();
    }
}
