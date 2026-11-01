package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class LogisticsTests {
    private static Block block(String id){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(id));}
    private static MachineEntity crane(GameTestHelper h){var origin=h.absolutePos(new BlockPos(3,5,1));Multiblocks.get(ModBlocks.id("loading_crane")).parts().forEach((offset,part)->h.getLevel().setBlock(origin.offset(offset),part.block().defaultBlockState(),Block.UPDATE_ALL));h.getLevel().setBlock(origin,block("loading_crane").defaultBlockState(),Block.UPDATE_ALL);var m=(MachineEntity)h.getLevel().getBlockEntity(origin);h.assertTrue(m.tryAssemble(),"Fixed gantry assembles");return m;}
    @GameTest public void cartKeepsFiftyFourSlotsAndCanBeLoaded(GameTestHelper h){
        var cart=CopperTransport.HANDCART.create(h.getLevel(),EntitySpawnReason.COMMAND);cart.setInitialPos(h.absolutePos(new BlockPos(3,2,3)).getX()+.5,h.absolutePos(new BlockPos(3,2,3)).getY(),h.absolutePos(new BlockPos(3,2,3)).getZ()+.5);h.getLevel().addFreshEntity(cart);
        h.assertValueEqual(cart.getContainerSize(),54,"Two chests worth of normal slots");cart.setItem(53,new ItemStack(Items.DIAMOND,17));h.assertValueEqual(cart.getItem(53).getCount(),17,"Last slot is real storage");
        h.assertTrue(LoadingCrane.cargo(h.getLevel(),cart.blockPosition())!=null,"Crane discovers parked cart");var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);h.assertTrue(cart.createMenu(1,player.getInventory(),player)!=null,"Native chest menu available");h.succeed();
    }
    private static net.minecraft.world.Container barrel(GameTestHelper h,BlockPos pos){h.getLevel().setBlock(pos,Blocks.BARREL.defaultBlockState(),Block.UPDATE_ALL);return (net.minecraft.world.Container)h.getLevel().getBlockEntity(pos);}
    private static int count(net.minecraft.world.Container storage,Item item){int count=0;for(int slot=0;slot<storage.getContainerSize();slot++)if(storage.getItem(slot).is(item))count+=storage.getItem(slot).getCount();return count;}
    private static void steps(GameTestHelper h,MachineEntity m,int ticks){for(int i=0;i<ticks;i++)LoadingCrane.tick(h.getLevel(),m);}
    @GameTest public void craneTransfersFiniteBatchesAndPreservesBlockedCargo(GameTestHelper h){
        var m=crane(h);var source=barrel(h,LoadingCrane.bay(m,false));var target=barrel(h,LoadingCrane.bay(m,true));source.setItem(0,new ItemStack(Items.COBBLESTONE,64));source.setItem(1,new ItemStack(Items.COBBLESTONE,64));
        LoadingCrane.queue(m);steps(h,m,LoadingCrane.duration(m)*3/20+1);
        h.assertValueEqual(m.getItem(1).getCount(),64,"Pickup reserves a real finite stack in the saved hoist slot");h.assertValueEqual(count(source,Items.COBBLESTONE),64,"Exactly one stack leaves the source at pickup");h.assertValueEqual(count(target,Items.COBBLESTONE),0,"Destination receives nothing before lowering");
        for(int i=0;i<target.getContainerSize();i++)target.setItem(i,new ItemStack(Items.DIRT,64));steps(h,m,LoadingCrane.duration(m)*2);
        h.assertValueEqual(m.getItem(1).getCount(),64,"Blocked receiver preserves the hoisted batch");h.assertValueEqual(count(source,Items.COBBLESTONE),64,"Waiting does not remove another stack");
        target.setItem(0,ItemStack.EMPTY);steps(h,m,LoadingCrane.duration(m));h.assertTrue(m.getItem(1).isEmpty(),"Cargo is cleared only after actual delivery");h.assertValueEqual(count(target,Items.COBBLESTONE),64,"Exactly the reserved batch reaches the target");h.assertValueEqual(count(source,Items.COBBLESTONE),64,"Manual crane does not repeat itself");h.succeed();
    }
    @GameTest public void craneRedstoneStartsOneBatchPerRisingEdge(GameTestHelper h){
        var m=crane(h);var source=barrel(h,LoadingCrane.bay(m,false));var target=barrel(h,LoadingCrane.bay(m,true));source.setItem(0,new ItemStack(Items.COBBLESTONE,64));source.setItem(1,new ItemStack(Items.COBBLESTONE,64));var power=m.getBlockPos().north();
        h.getLevel().setBlock(power,Blocks.REDSTONE_BLOCK.defaultBlockState(),Block.UPDATE_ALL);steps(h,m,LoadingCrane.duration(m)*2);h.assertValueEqual(count(target,Items.COBBLESTONE),64,"Constant high signal starts one batch only");h.assertTrue(m.copper.craneRedstoneHigh,"Last high state is tracked");
        h.getLevel().setBlock(power,Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);steps(h,m,1);h.getLevel().setBlock(power,Blocks.REDSTONE_BLOCK.defaultBlockState(),Block.UPDATE_ALL);steps(h,m,LoadingCrane.duration(m));h.assertValueEqual(count(target,Items.COBBLESTONE),128,"A new rising edge starts the second batch");h.assertValueEqual(count(source,Items.COBBLESTONE),0,"No phantom or lost source items");h.succeed();
    }
    @GameTest public void craneObstructionKeepsReservedLoad(GameTestHelper h){
        var m=crane(h);var source=barrel(h,LoadingCrane.bay(m,false));var target=barrel(h,LoadingCrane.bay(m,true));source.setItem(0,new ItemStack(Items.DIAMOND,11));LoadingCrane.queue(m);steps(h,m,LoadingCrane.duration(m)*3/20+1);var obstruction=m.getBlockPos().offset(0,-2,2);h.getLevel().setBlock(obstruction,Blocks.STONE.defaultBlockState(),Block.UPDATE_ALL);int progress=m.copper.work;steps(h,m,100);
        h.assertValueEqual(m.copper.work,progress,"Solid obstacle pauses the physical hoist");h.assertValueEqual(m.getItem(1).getCount(),11,"Obstacle never deletes reserved items");h.getLevel().setBlock(obstruction,Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);steps(h,m,LoadingCrane.duration(m));h.assertValueEqual(count(target,Items.DIAMOND),11,"Clearing the obstruction resumes the saved batch");h.succeed();
    }
    @GameTest public void hearthSupportsSuitableMetalsButNotAluminumOre(GameTestHelper h){
        h.assertTrue(CopperWorkshops.melt(new ItemStack(Items.GOLD_INGOT))!=null,"Existing vanilla gold accepted");h.assertTrue(CopperWorkshops.melt(new ItemStack(BuiltInRegistries.ITEM.getValue(ModBlocks.id("aluminum_ingot"))))!=null,"Already reduced aluminum accepted");
        h.assertTrue(CopperWorkshops.melt(new ItemStack(BuiltInRegistries.ITEM.getValue(ModBlocks.id("raw_bauxite"))))==null,"Heating bauxite cannot produce metal");h.assertTrue(CopperWorkshops.meltingHeat(CopperWorkshops.melt(new ItemStack(Items.IRON_INGOT))[0])>1100,"Registered iron requires heat above this furnace's working range");
        var state=new CopperState();state.metal(3,9000);state.metal(7,18000);h.assertValueEqual(state.total(),27000,"Additional metals share capacity");h.assertValueEqual(state.metal(3),9000,"Independent lead store");h.assertValueEqual(state.metal(7),18000,"Independent gold store");h.succeed();
    }
    @GameTest public void ropewayPausesOnBrokenLinkWithoutLosingLoad(GameTestHelper h){
        var a=h.absolutePos(new BlockPos(2,3,2));var b=h.absolutePos(new BlockPos(6,3,2));for(int x=2;x<=6;x++)h.getLevel().setBlock(h.absolutePos(new BlockPos(x,3,2)),Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);h.getLevel().setBlock(a,block("rope_anchor").defaultBlockState(),Block.UPDATE_ALL);h.getLevel().setBlock(b,block("rope_anchor").defaultBlockState(),Block.UPDATE_ALL);var source=(MachineEntity)h.getLevel().getBlockEntity(a);var target=(MachineEntity)h.getLevel().getBlockEntity(b);
        h.assertTrue(Ropeway.link(h.getLevel(),source,target),"Two anchors form a clear physical span");source.setItem(0,new ItemStack(Items.DIAMOND,11));source.copper.queued=1;Ropeway.tick(h.getLevel(),source);h.assertValueEqual(source.getItem(1).getCount(),11,"Actual cargo in persistent travelling slot");
        Ropeway.unlink(h.getLevel(),source);for(int i=0;i<100;i++)Ropeway.tick(h.getLevel(),source);h.assertValueEqual(source.getItem(1).getCount(),11,"Broken line never deletes cargo");h.assertTrue(target.getItem(3).isEmpty(),"Disconnected endpoint receives nothing");
        h.assertTrue(Ropeway.link(h.getLevel(),source,target),"Can repair the line");for(int i=0;i<100;i++)Ropeway.tick(h.getLevel(),source);h.assertTrue(source.getItem(1).isEmpty(),"Load leaves once");h.assertValueEqual(target.getItem(3).getCount(),11,"All cargo arrives once");h.succeed();
    }
    private static MachineEntity[] rideLine(GameTestHelper h){
        for(int x=0;x<=7;x++)for(int z=0;z<=4;z++){h.getLevel().setBlock(h.absolutePos(new BlockPos(x,0,z)),Blocks.STONE.defaultBlockState(),Block.UPDATE_ALL);for(int y=1;y<=6;y++)h.getLevel().setBlock(h.absolutePos(new BlockPos(x,y,z)),Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);}
        var a=h.absolutePos(new BlockPos(1,4,1));var b=h.absolutePos(new BlockPos(6,4,1));for(var pos:new BlockPos[]{a,b})h.getLevel().setBlock(pos,block("rope_anchor").defaultBlockState(),Block.UPDATE_ALL);var first=(MachineEntity)h.getLevel().getBlockEntity(a);var last=(MachineEntity)h.getLevel().getBlockEntity(b);h.assertTrue(Ropeway.link(h.getLevel(),first,last),"Passenger fixture has a real clear rope link");return new MachineEntity[]{first,last};
    }
    @GameTest public void stickRideReservesLineAndDismountsAtDestination(GameTestHelper h){
        var line=rideLine(h);var player=h.makeMockServerPlayerInLevel();player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,new ItemStack(Items.STICK));CopperInteractions.use(h.getLevel(),line[0],player,net.minecraft.world.InteractionHand.MAIN_HAND,line[0].getBlockPos());h.assertTrue(player.getVehicle() instanceof RopewayRideEntity,"Native stick interaction boards the passenger hoist");var ride=(RopewayRideEntity)player.getVehicle();h.assertTrue(RopewayRideEntity.busy(h.getLevel(),line[0].getBlockPos()),"Passenger reserves the physical line");
        var second=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);RopewayRideEntity.mount(h.getLevel(),line[1],second);h.assertFalse(second.isPassenger(),"Second player cannot enter an occupied line");line[0].setItem(0,new ItemStack(Items.COBBLESTONE,16));line[0].copper.queued=1;Ropeway.tick(h.getLevel(),line[0]);h.assertTrue(line[0].getItem(1).isEmpty(),"Cargo cannot launch through an occupied passenger line");h.assertValueEqual(line[0].getItem(0).getCount(),16,"Waiting cargo remains in its source");
        for(int i=0;i<80&&!ride.isRemoved();i++){ride.tick();for(var rider:ride.getPassengers())ride.positionRider(rider);}h.assertFalse(player.isPassenger(),"Trip releases its passenger at the destination");h.assertTrue(ride.isRemoved(),"Finished hoist does not leave a ghost vehicle");h.assertTrue(player.position().distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(line[1].getBlockPos()))<36,"Safe dismount is near the actual target anchor");h.succeed();
    }
    @GameTest public void stickRideRejectsObstructedSpanWithoutEatingStick(GameTestHelper h){
        var line=rideLine(h);var obstacle=h.absolutePos(new BlockPos(3,4,1));h.getLevel().setBlock(obstacle,Blocks.STONE.defaultBlockState(),Block.UPDATE_ALL);var player=h.makeMockServerPlayerInLevel();var stick=new ItemStack(Items.STICK);player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,stick);CopperInteractions.use(h.getLevel(),line[0],player,net.minecraft.world.InteractionHand.MAIN_HAND,line[0].getBlockPos());h.assertFalse(player.isPassenger(),"Blocked physical route cannot board a passenger");h.assertTrue(stick.is(Items.STICK)&&stick.getCount()==1,"Using the transport never consumes the stick");h.assertFalse(RopewayRideEntity.busy(h.getLevel(),line[0].getBlockPos()),"Failed boarding does not create an invisible reserved hoist");h.succeed();
    }

    private static void rideSteps(RopewayRideEntity ride,int ticks){for(int n=0;n<ticks&&!ride.isRemoved();n++){ride.tick();for(var rider:ride.getPassengers())ride.positionRider(rider);}}
    private static net.minecraft.world.phys.Vec3 ropePoint(MachineEntity[] line,double t){var start=line[0].getBlockPos();return Ropeway.point(net.minecraft.world.phys.Vec3.atLowerCornerOf(line[1].getBlockPos().subtract(start)),t).add(start.getX(),start.getY(),start.getZ());}
    @GameTest public void midspanRideFollowsLookAndCanReverseWithoutTeleporting(GameTestHelper h){
        var line=rideLine(h);var player=h.makeMockServerPlayerInLevel();player.setYRot(-90);player.setXRot(0);
        RopewayRideEntity.mountAt(h.getLevel(),line[0],player,.5);h.assertTrue(player.getVehicle() instanceof RopewayRideEntity,"Can board the middle of a rope instead of an endpoint");var ride=(RopewayRideEntity)player.getVehicle();
        var center=ropePoint(line,.5);h.assertTrue(Math.abs(ride.getX()-center.x)<.2,"Mount starts at the selected rope point");double initial=ride.getX();rideSteps(ride,5);h.assertTrue(ride.getX()>initial+.1,"Looking toward the positive endpoint moves forward");double turn=ride.getX();player.setYRot(90);rideSteps(ride,5);h.assertTrue(ride.getX()<turn-.1,"Turning to the opposite endpoint reverses travel");h.assertTrue(player.isPassenger(),"Changing direction does not eject the passenger");double beforeVertical=ride.getX();player.setXRot(-90);rideSteps(ride,2);h.assertTrue(ride.getX()<beforeVertical,"Vertical look preserves the previous direction in the deadzone");
        player.setXRot(0);player.setYRot(90);rideSteps(ride,80);h.assertFalse(player.isPassenger(),"Reversed journey reaches a safe endpoint and releases its passenger");h.succeed();
    }

    private static void aimAtRope(net.minecraft.server.level.ServerPlayer player,net.minecraft.world.phys.Vec3 point,double distance){player.setPos(point.x,point.y-player.getEyeHeight(),point.z+distance);player.lookAt(net.minecraft.commands.arguments.EntityAnchorArgument.Anchor.EYES,point);player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,new ItemStack(Items.STICK));}
    @GameTest public void ropeRayBoardsOnlyVisibleReachablePoint(GameTestHelper h){
        var line=rideLine(h);var point=ropePoint(line,.5);var player=h.makeMockServerPlayerInLevel();
        aimAtRope(player,point,12);h.assertTrue(RopewayRideEntity.mountLookedAt(h.getLevel(),player)==null,"Ray outside native interaction reach cannot select a rope");h.assertFalse(player.isPassenger(),"Distant stick use cannot teleport the player onto a rope");
        aimAtRope(player,point,2);var obstacle=BlockPos.containing(point.x,point.y,point.z+1);for(int dx=-1;dx<=1;dx++)h.getLevel().setBlock(obstacle.offset(dx,0,0),Blocks.STONE.defaultBlockState(),Block.UPDATE_ALL);h.assertTrue(RopewayRideEntity.mountLookedAt(h.getLevel(),player)==null,"A solid block occludes the authoritative eye ray");h.assertFalse(player.isPassenger(),"A hidden rope cannot be boarded through a wall");for(int dx=-1;dx<=1;dx++)h.getLevel().setBlock(obstacle.offset(dx,0,0),Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);
        aimAtRope(player,point,2);h.assertTrue(net.fabricmc.fabric.api.event.player.UseItemCallback.EVENT.invoker().interact(player,h.getLevel(),net.minecraft.world.InteractionHand.MAIN_HAND).consumesAction(),"Registered native stick callback consumes a visible rope interaction");h.assertTrue(player.getVehicle() instanceof RopewayRideEntity,"Stick eye ray creates a real passenger vehicle");h.assertTrue(Math.abs(player.getVehicle().getX()-point.x)<.25,"Ray boarding starts at the looked-at middle, not an anchor");h.assertValueEqual(player.getMainHandItem().getCount(),1,"Ray boarding never consumes the stick");h.succeed();
    }

    @GameTest public void visibleRopeTakesPriorityOverClickableBlockBehindIt(GameTestHelper h){
        var line=rideLine(h);var point=ropePoint(line,.5);var player=h.makeMockServerPlayerInLevel();aimAtRope(player,point,2);var chest=BlockPos.containing(point.x,point.y,point.z-1);h.getLevel().setBlock(chest,Blocks.CHEST.defaultBlockState(),Block.UPDATE_ALL);
        var hit=new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(chest),Direction.SOUTH,chest,false);var result=net.fabricmc.fabric.api.event.player.UseBlockCallback.EVENT.invoker().interact(player,h.getLevel(),net.minecraft.world.InteractionHand.MAIN_HAND,hit);
        h.assertTrue(result.consumesAction(),"Registered block callback intercepts a visible rope in front of the clicked chest");h.assertTrue(player.getVehicle() instanceof RopewayRideEntity,"Clicking toward a background chest still boards the visible rope");h.assertTrue(player.containerMenu==player.inventoryMenu,"Rope interaction does not open the background container");h.succeed();
    }

    @GameTest public void unrelatedMenuButtonsCannotRedirectReservedCraneCargo(GameTestHelper h){
        var m=crane(h);var source=barrel(h,LoadingCrane.bay(m,false));var target=barrel(h,LoadingCrane.bay(m,true));source.setItem(0,new ItemStack(Items.DIAMOND,11));LoadingCrane.queue(m);steps(h,m,LoadingCrane.duration(m)*3/20+1);
        int progress=m.copper.work,selection=m.copper.selection;
        h.assertFalse(CopperWorkshops.button(m,20),"Crane rejects pottery previous button");h.assertFalse(CopperWorkshops.button(m,21),"Crane rejects pottery next button");
        h.assertValueEqual(m.copper.selection,selection,"Reserved cargo keeps its delivery direction");h.assertValueEqual(m.copper.work,progress,"Unrelated packets do not rewind the hoist");
        steps(h,m,LoadingCrane.duration(m));h.assertValueEqual(count(target,Items.DIAMOND),11,"Reserved batch still reaches its original destination");h.succeed();
    }
    @GameTest public void incompleteCraneReportsTheActualMissingPosition(GameTestHelper h){
        var m=crane(h);var missing=m.structure().parts().entrySet().stream().filter(e->!e.getValue().value().equals("minecraft:air")).findFirst().orElseThrow().getKey().rotate(m.rotation()).offset(m.getBlockPos());m.disassemble();h.getLevel().setBlock(missing,Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);
        h.assertFalse(m.tryAssemble(),"Incomplete gantry cannot assemble");h.assertTrue(m.assemblyDetail().equals("fixed|"+missing.getX()+"|"+missing.getY()+"|"+missing.getZ()),"Assembly feedback identifies the missing world block");h.succeed();
    }
}
