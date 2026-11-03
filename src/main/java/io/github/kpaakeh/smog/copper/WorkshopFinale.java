package io.github.kpaakeh.smog.copper;

import java.util.UUID;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.multiblock.complex.ComplexFormationData;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.*;

/** Commission existing equipment through real productive work. No recipe or era locks. */
public final class WorkshopFinale {
    public static final int REQUIRED=2;
    private WorkshopFinale(){}
    private static void tell(ServerPlayer player,String key,Object... args){player.sendOverlayMessage(Component.translatable("message.smog.finale."+key,args));}
    private static ServerPlayer owner(ServerLevel level,CopperState state){try{var id=UUID.fromString(state.commissioningOwner);var player=level.getServer().getPlayerList().getPlayer(id);return player!=null?player:level.getPlayerByUUID(id) instanceof ServerPlayer local?local:null;}catch(IllegalArgumentException invalid){return null;}}
    private static boolean completed(ServerPlayer player){var node=player.level().getServer().getAdvancements().get(ModBlocks.id("copper/mechanized"));return node!=null&&player.getAdvancements().getOrStartProgress(node).isDone();}
    private static void award(ServerPlayer player,String name,String criterion){var node=player.level().getServer().getAdvancements().get(ModBlocks.id("copper/"+name));if(node!=null)player.getAdvancements().award(node,criterion);}
    private static MachineEntity consumer(ServerLevel level,BlockPos drive){
        var controller=ComplexFormationData.get(level).owner(drive);
        if(controller!=null&&level.hasChunkAt(controller)&&level.getBlockEntity(controller) instanceof MachineEntity press&&CopperWorkshops.workshop(press,"lever_press")&&press.assembled())return press;
        for(var side:Direction.values()){var pos=drive.relative(side);if(level.hasChunkAt(pos)&&level.getBlockEntity(pos) instanceof MachineEntity bellows&&CopperWorkshops.workshop(bellows,"hand_bellows")&&bellows.assembled())return bellows;}
        return null;
    }
    private static boolean supplied(MachineEntity machine,BlockPos drive){
        if(!(machine.getLevel() instanceof ServerLevel level)||!machine.assembled()||!machine.enabled()||!BronzeDrive.working(level,drive))return false;
        if(CopperWorkshops.workshop(machine,"lever_press"))return machine.getBlockPos().equals(ComplexFormationData.get(level).owner(drive));
        return CopperWorkshops.workshop(machine,"hand_bellows")&&machine.getBlockPos().distManhattan(drive)==1;
    }
    public static boolean use(ServerLevel level,BlockPos drive,ServerPlayer player){
        var target=consumer(level,drive);if(target==null){tell(player,"attach");return false;}
        var state=target.copper;String uuid=player.getUUID().toString();
        if(player.isShiftKeyDown()&&state.commissioningOwner.equals(uuid)){state.commissioningOwner="";state.commissioningWork=0;target.sync();tell(player,"cancelled");return true;}
        if(completed(player)){tell(player,"already");return true;}
        if(state.commissioningOwner.equals(uuid)&&state.commissioningDrive==drive.asLong()){
            if(state.commissioningWork>=REQUIRED){finish(level,target);return true;}
            tell(player,supplied(target,drive)?"progress":"flow",state.commissioningWork,REQUIRED);return true;
        }
        if(!supplied(target,drive)){tell(player,"flow");return false;}
        if(CopperWorkshops.workshop(target,"hand_bellows")&&CopperWorkshops.bellowsTarget(level,target)==null){tell(player,"bellows");return false;}
        if(!state.commissioningOwner.isEmpty()&&!state.commissioningOwner.equals(uuid)){tell(player,"busy");return false;}
        if(!state.commissioningOwner.equals(uuid))state.commissioningWork=0;state.commissioningOwner=uuid;state.commissioningDrive=drive.asLong();target.sync();award(player,"commissioning","started");tell(player,CopperWorkshops.workshop(target,"lever_press")?"press":"smelt");return true;
    }
    private static void finish(ServerLevel level,MachineEntity machine){var player=owner(level,machine.copper);if(player==null)return;award(player,"commissioning","started");if(!completed(player)){award(player,"mechanized","completed");tell(player,"complete");}machine.copper.commissioningOwner="";machine.sync();}
    private static void productive(MachineEntity machine){
        if(!(machine.getLevel() instanceof ServerLevel level))return;var state=machine.copper;
        if(state.commissioningOwner.isEmpty()||state.commissioningWork>=REQUIRED||!supplied(machine,BlockPos.of(state.commissioningDrive)))return;
        state.commissioningWork++;machine.sync();if(state.commissioningWork>=REQUIRED)finish(level,machine);else {var player=owner(level,state);if(player!=null)tell(player,"progress",state.commissioningWork,REQUIRED);}
    }
    public static void pressed(MachineEntity press){if(press.copper.autoWork&&CopperWorkshops.workshop(press,"lever_press"))productive(press);}
    public static void melted(MachineEntity smeltery,boolean usedAir){
        if(!usedAir||!(smeltery.getLevel() instanceof ServerLevel level)||smeltery.copper.poweredBellowsAt==Long.MIN_VALUE||level.getGameTime()-smeltery.copper.poweredBellowsAt>40)return;
        var pos=BlockPos.of(smeltery.copper.poweredBellowsPos);if(!level.hasChunkAt(pos)||!(level.getBlockEntity(pos) instanceof MachineEntity bellows)||CopperWorkshops.bellowsTarget(level,bellows)!=smeltery)return;
        productive(bellows);
    }
}
