package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.CopperWorkshops;
import io.github.kpaakeh.smog.gui.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

/** Exercise actual result-slot click paths, rather than calling a crafting helper directly. */
public final class PotteryTests {
    @GameTest public void copperAnvilHitboxIncludesHornAndLeavesEmptyCornersInEveryFacing(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,2,3));var block=BuiltInRegistries.BLOCK.getValue(ModBlocks.id("tool_workbench"));
        for(var facing:Direction.Plane.HORIZONTAL){
            var state=block.defaultBlockState().setValue(io.github.kpaakeh.smog.machine.MachineBlock.FACING,facing);h.getLevel().setBlock(pos,state,Block.UPDATE_ALL);
            var start=anvilPoint(1.24,1.1,.5,facing,pos);var end=anvilPoint(1.24,.6,.5,facing,pos);
            h.assertTrue(state.getShape(h.getLevel(),pos).clip(start,end,pos)!=null,"Selection includes the projecting anvil horn: "+facing);
            h.assertTrue(state.getCollisionShape(h.getLevel(),pos).clip(start,end,pos)!=null,"Collision follows the horn: "+facing);
            h.assertTrue(state.getShape(h.getLevel(),pos).clip(anvilPoint(.03,1.1,.03,facing,pos),anvilPoint(.03,-.1,.03,facing,pos),pos)==null,"The empty corner no longer collides with an invisible tabletop: "+facing);
        }
        h.succeed();
    }
    private static net.minecraft.world.phys.Vec3 anvilPoint(double x,double y,double z,Direction facing,BlockPos pos){
        int turns=switch(facing){case EAST->1;case SOUTH->2;case WEST->3;default->0;};
        for(int n=0;n<turns;n++){double old=x;x=1-z;z=old;}
        return new net.minecraft.world.phys.Vec3(pos.getX()+x,pos.getY()+y,pos.getZ()+z);
    }
    private static Item item(String name){return BuiltInRegistries.ITEM.getValue(ModBlocks.id(name));}
    private static MachineEntity table(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,2,3));
        h.getLevel().setBlock(pos,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("potter_table")).defaultBlockState(),Block.UPDATE_ALL);
        return (MachineEntity)h.getLevel().getBlockEntity(pos);
    }
    private static ServerPlayer player(GameTestHelper h,MachineEntity table){
        var player=h.makeMockServerPlayerInLevel();var pos=table.getBlockPos();player.setPos(pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5);return player;
    }
    private static MachineMenu menu(MachineEntity table,ServerPlayer player){return new MachineMenu(GuiLayouts.get("potter_table"),1,player.getInventory(),table,new SimpleContainerData(MachineMenu.DATA_SIZE));}
    private static int recipe(String output){var recipes=CopperWorkshops.potteryRecipes();for(int n=0;n<recipes.size();n++)if(recipes.get(n).output().equals("smog:"+output))return n;throw new IllegalArgumentException(output);}
    private static void choose(MachineMenu menu,ServerPlayer player,String output){if(!menu.clickMenuButton(player,MachineMenu.POTTERY_SELECT+recipe(output)))throw new IllegalStateException("Rejected pottery choice "+output);}

    @GameTest public void previewSelectionClosingAndAutomationNeverCreateStoredOutput(GameTestHelper h){
        var table=table(h);table.setItem(0,new ItemStack(Items.CLAY_BALL,12));var player=player(h,table);var menu=menu(table,player);
        choose(menu,player,"unfired_plate_mold");
        h.assertTrue(menu.getSlot(1).getItem().is(item("unfired_plate_mold")),"Selected recipe has a visible menu-only preview");
        h.assertValueEqual(table.getItem(0).getCount(),12,"Viewing and choosing never consume clay");
        h.assertTrue(table.getItem(3).isEmpty(),"Preview is never stored in the persistent output slot");
        var storage=net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.find(h.getLevel(),table.getBlockPos(),Direction.DOWN);
        h.assertTrue(storage!=null,"Table has its normal item storage");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
            h.assertValueEqual(storage.extract(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant.of(item("unfired_plate_mold")),64,tx),0L,"Automation cannot extract free preview items");tx.commit();
        }
        menu.removed(player);var reopened=menu(table,player);
        h.assertValueEqual(table.getItem(0).getCount(),12,"Closing and reopening costs no ingredients");
        h.assertTrue(table.getItem(3).isEmpty()&&!reopened.getSlot(1).getItem().isEmpty(),"Reopening reconstructs preview without persisting it");h.succeed();
    }
    @GameTest public void staleSecondViewerCannotTakeTheSameClayTwice(GameTestHelper h){
        var table=table(h);table.setItem(0,new ItemStack(Items.CLAY_BALL,2));var first=player(h,table);var second=player(h,table);var a=menu(table,first);var b=menu(table,second);
        h.assertTrue(!a.getSlot(1).getItem().isEmpty()&&!b.getSlot(1).getItem().isEmpty(),"Both viewers initially see the same available recipe");
        a.clicked(1,0,ContainerInput.PICKUP,first);b.clicked(1,0,ContainerInput.PICKUP,second);
        h.assertTrue(a.getCarried().is(item("unfired_ingot_mold")),"First pickup crafts the selected mold");
        h.assertValueEqual(a.getCarried().getCount(),1,"First pickup crafts exactly one recipe");
        h.assertTrue(b.getCarried().isEmpty()&&table.getItem(0).isEmpty(),"Stale second preview cannot duplicate the spent clay");
        table.setItem(0,new ItemStack(Items.CLAY_BALL,1));a.clicked(1,0,ContainerInput.PICKUP,first);
        h.assertValueEqual(a.getCarried().getCount(),1,"Insufficient replacement input cannot reuse an old preview");
        h.assertValueEqual(table.getItem(0).getCount(),1,"Rejected stale pickup does not consume partial ingredients");h.succeed();
    }
    @GameTest public void hotbarAndRightClickValidateDestinationBeforeConsuming(GameTestHelper h){
        var table=table(h);table.setItem(0,new ItemStack(Items.CLAY_BALL,6));var player=player(h,table);var menu=menu(table,player);
        player.getInventory().setItem(0,new ItemStack(Items.DIAMOND));menu.clicked(1,0,ContainerInput.SWAP,player);
        h.assertValueEqual(table.getItem(0).getCount(),6,"Occupied incompatible hotbar refuses crafting without payment");
        player.getInventory().setItem(0,ItemStack.EMPTY);menu.clicked(1,0,ContainerInput.SWAP,player);
        h.assertTrue(player.getInventory().getItem(0).is(item("unfired_ingot_mold")),"Hotbar action transfers a genuinely crafted result");
        h.assertValueEqual(table.getItem(0).getCount(),4,"Hotbar transfer consumes exactly one recipe");
        menu.setCarried(new ItemStack(Items.DIAMOND));menu.clicked(1,1,ContainerInput.PICKUP,player);
        h.assertValueEqual(table.getItem(0).getCount(),4,"Incompatible cursor refuses right-click without payment");
        menu.setCarried(ItemStack.EMPTY);menu.clicked(1,1,ContainerInput.PICKUP,player);
        h.assertValueEqual(menu.getCarried().getCount(),1,"Right-click takes a complete recipe result");
        h.assertValueEqual(table.getItem(0).getCount(),2,"Right-click consumes one complete recipe");h.succeed();
    }
    @GameTest public void shiftCraftingStopsAtInventoryCapacityAndPaysEveryRecipe(GameTestHelper h){
        var table=table(h);table.setItem(0,new ItemStack(Items.CLAY_BALL,6));var player=player(h,table);var menu=menu(table,player);
        for(int n=0;n<36;n++)player.getInventory().setItem(n,new ItemStack(Items.STONE,64));
        menu.quickMoveStack(player,1);
        h.assertValueEqual(table.getItem(0).getCount(),6,"Full inventory causes no hidden ingredient payment");
        h.assertValueEqual(player.getInventory().countItem(item("unfired_ingot_mold")),0,"Full inventory creates no output elsewhere");
        player.getInventory().setItem(9,ItemStack.EMPTY);menu.quickMoveStack(player,1);
        h.assertTrue(table.getItem(0).isEmpty(),"Shift craft consumes the three available recipes");
        h.assertValueEqual(player.getInventory().countItem(item("unfired_ingot_mold")),3,"Shift craft produces exactly three paid outputs");
        h.assertTrue(table.getItem(3).isEmpty()&&menu.getSlot(1).getItem().isEmpty(),"No stored or stale output remains after input is spent");h.succeed();
    }
    @GameTest public void legacyFinishedStackKeepsIdentityAndIsRecoveredOnceWithoutChargingClay(GameTestHelper h){
        var table=table(h);table.setItem(0,new ItemStack(Items.CLAY_BALL,4));
        var legacy=new ItemStack(item("unfired_plate_mold"),3);legacy.set(DataComponents.CUSTOM_NAME,net.minecraft.network.chat.Component.literal("Old batch"));table.setItem(3,legacy);
        var player=player(h,table);var menu=menu(table,player);menu.clicked(1,0,ContainerInput.PICKUP,player);
        h.assertValueEqual(menu.getCarried().getCount(),3,"Migration returns every pre-existing finished item");
        h.assertTrue(menu.getCarried().has(DataComponents.CUSTOM_NAME),"Migration retains custom data of stored output");
        h.assertValueEqual(table.getItem(0).getCount(),4,"Recovering old output does not charge fresh clay");
        h.assertTrue(table.getItem(3).isEmpty(),"Legacy stack is removed from storage exactly once");
        var second=player(h,table);var reopened=menu(table,second);reopened.clicked(1,0,ContainerInput.PICKUP,second);
        h.assertValueEqual(reopened.getCarried().getCount(),1,"Next viewer receives one new output, not a duplicate old batch");
        h.assertTrue(!reopened.getCarried().has(DataComponents.CUSTOM_NAME),"Newly made output does not inherit old batch data");
        h.assertValueEqual(table.getItem(0).getCount(),2,"Only the new recipe charges ingredients");h.succeed();
    }
    @GameTest public void viewerSelectionIsLocalAndInputReplacementInvalidatesOldChoice(GameTestHelper h){
        var table=table(h);table.setItem(0,new ItemStack(Items.CLAY_BALL,6));var first=player(h,table);var second=player(h,table);var a=menu(table,first);var b=menu(table,second);
        choose(a,first,"unfired_plate_mold");choose(b,second,"unfired_fitting_mold");
        h.assertValueEqual(a.potterySelection(),recipe("unfired_plate_mold"),"Second viewer cannot overwrite first viewer choice");
        a.clicked(1,0,ContainerInput.PICKUP,first);b.clicked(1,0,ContainerInput.PICKUP,second);
        h.assertTrue(a.getCarried().is(item("unfired_plate_mold"))&&b.getCarried().is(item("unfired_fitting_mold")),"Each viewer takes their own selected recipe");
        h.assertValueEqual(table.getItem(0).getCount(),2,"Both different outputs charge the shared input once each");
        table.setItem(0,new ItemStack(Items.DIAMOND,2));a.setCarried(ItemStack.EMPTY);a.clicked(1,0,ContainerInput.PICKUP,first);
        h.assertTrue(a.getCarried().isEmpty(),"Replacing clay cannot turn a stale selection into an unrelated output");
        h.assertValueEqual(table.getItem(0).getCount(),2,"Incompatible replacement input remains untouched");
        h.assertTrue(!a.clickMenuButton(first,MachineMenu.POTTERY_SELECT+recipe("unfired_ingot_mold")),"Recipe buttons reject incompatible input on the server");h.succeed();
    }
}
