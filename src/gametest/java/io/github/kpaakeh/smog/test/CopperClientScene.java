package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import io.github.kpaakeh.smog.multiblock.Multiblocks;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

/** Isolated copper workshop scene; no desktop input, no user world or saves. */
public final class CopperClientScene {
    private static Block block(String name){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name));}
    private static void hover(ClientGameTestContext context,int localX,int localY){
        double[] position=new double[2];
        context.runOnClient(mc->{
            var menu=((io.github.kpaakeh.smog.gui.MachineScreen)mc.gui.screen()).getMenu();var window=mc.getWindow();
            position[0]=((window.getGuiScaledWidth()-menu.layout.width)/2.0+localX)*window.getScreenWidth()/window.getGuiScaledWidth();
            position[1]=((window.getGuiScaledHeight()-menu.layout.height)/2.0+localY)*window.getScreenHeight()/window.getGuiScaledHeight();
        });
        context.getInput().setCursorPos(position[0],position[1]);context.waitTicks(3);
    }
    private static String language(ClientGameTestContext context,String requested){
        var previous=new java.util.concurrent.atomic.AtomicReference<String>();
        var reload=new java.util.concurrent.atomic.AtomicReference<java.util.concurrent.CompletableFuture<Void>>();
        context.runOnClient(mc->{
            previous.set(mc.getLanguageManager().getSelected());
            if(previous.get().equals(requested))return;
            if(!mc.getLanguageManager().getLanguages().containsKey(requested))throw new IllegalArgumentException("Unavailable test language: "+requested);
            mc.getLanguageManager().setSelected(requested);mc.options.languageCode=requested;reload.set(mc.reloadResourcePacks());
        });
        if(reload.get()!=null){context.waitFor(mc->reload.get().isDone());reload.get().join();}
        return previous.get();
    }
    public static void run(ClientGameTestContext context){
        String requestedLanguage=System.getProperty("smog.testLanguage","");
        String previousLanguage=requestedLanguage.isEmpty()?null:language(context,requestedLanguage);
        try(var world=context.worldBuilder().create()){
            world.getServer().runCommand("fill -12 99 -8 12 99 8 minecraft:stone");
            world.getServer().runCommand("time set noon");world.getServer().runCommand("weather clear");
            world.getServer().runCommand("tp @a 0.5 104 -7.5 0 25");
            world.getServer().runOnServer(server->{
                var level=server.overworld();var player=server.getPlayerList().getPlayers().getFirst();player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);player.getAbilities().flying=true;player.onUpdateAbilities();
                int hotbar=0;
                for(String metal:java.util.List.of("copper","bronze","iron"))for(String kind:java.util.List.of("pickaxe","hammer")){
                    var tool=io.github.kpaakeh.smog.copper.ToolParts.example(io.github.kpaakeh.smog.copper.ToolDefinitions.template(kind),metal);
                    if(tool.isEmpty())throw new IllegalStateException("Dynamic tool fixture missing: "+metal+" "+kind);
                    player.getInventory().setItem(hotbar++,tool);
                }
                var mattock=io.github.kpaakeh.smog.copper.ToolParts.example(io.github.kpaakeh.smog.copper.ToolDefinitions.template("mattock"),"bronze");if(mattock.isEmpty())throw new IllegalStateException("Dynamic mattock fixture missing");player.getInventory().setItem(6,mattock);
                player.getInventory().setItem(7,new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("handcart")));
                player.getInventory().setSelectedSlot(0);
                var cart=io.github.kpaakeh.smog.copper.CopperTransport.HANDCART.create(level,net.minecraft.world.entity.EntitySpawnReason.COMMAND);cart.setInitialPos(-3.5,100,-2.5);cart.setItem(53,new ItemStack(Items.BRICKS,64));level.addFreshEntity(cart);
                var craneOrigin=new BlockPos(8,104,4);Multiblocks.get(ModBlocks.id("loading_crane")).parts().forEach((p,ingredient)->level.setBlockAndUpdate(craneOrigin.offset(p),ingredient.block().defaultBlockState()));level.setBlockAndUpdate(craneOrigin,block("loading_crane").defaultBlockState());var crane=(MachineEntity)level.getBlockEntity(craneOrigin);if(!crane.tryAssemble())throw new IllegalStateException("Crane failed");level.setBlockAndUpdate(io.github.kpaakeh.smog.copper.LoadingCrane.bay(crane,false),Blocks.BARREL.defaultBlockState());level.setBlockAndUpdate(io.github.kpaakeh.smog.copper.LoadingCrane.bay(crane,true),Blocks.BARREL.defaultBlockState());
                var anchorA=new BlockPos(-9,104,-6);var anchorB=new BlockPos(9,104,-6);level.setBlockAndUpdate(anchorA,block("rope_anchor").defaultBlockState());level.setBlockAndUpdate(anchorB,block("rope_anchor").defaultBlockState());var firstAnchor=(MachineEntity)level.getBlockEntity(anchorA);var lastAnchor=(MachineEntity)level.getBlockEntity(anchorB);if(!io.github.kpaakeh.smog.copper.Ropeway.link(level,firstAnchor,lastAnchor))throw new IllegalStateException("Rope span failed");firstAnchor.setItem(0,new ItemStack(Items.BRICKS,32));firstAnchor.copper.queued=1;firstAnchor.sync();
                var origin=new BlockPos(0,100,0);
                var definition=Multiblocks.get(ModBlocks.id("copper_hearth"));
                definition.parts().forEach((p,ingredient)->level.setBlockAndUpdate(origin.offset(p),ingredient.block().defaultBlockState()));
                level.setBlockAndUpdate(origin,block("copper_hearth").defaultBlockState().setValue(MachineBlock.FACING,Direction.SOUTH));
                var hearth=(MachineEntity)level.getBlockEntity(origin);if(!hearth.tryAssemble())throw new IllegalStateException("Client scene hearth assembly failed");hearth.copper.copper=8*9000;hearth.copper.heat=1000;hearth.sync();
                for(String name:java.util.List.of("washing_trough","lever_press")){
                    var pos=new BlockPos(name.equals("washing_trough")?-5:5,100,0);
                    Multiblocks.get(ModBlocks.id(name)).parts().forEach((p,ingredient)->level.setBlockAndUpdate(pos.offset(p),ingredient.block().defaultBlockState()));level.setBlockAndUpdate(pos,block(name).defaultBlockState());
                    var machine=(MachineEntity)level.getBlockEntity(pos);if(!machine.tryAssemble())throw new IllegalStateException("Manual scene assembly failed");
                    machine.setItem(0,new ItemStack(name.equals("washing_trough")?Items.RAW_COPPER:Items.WHEAT_SEEDS,16));machine.copper.processFluid=name.equals("washing_trough")?162000:20250;machine.sync();
                }
                var benchPos=new BlockPos(0,100,-3);level.setBlockAndUpdate(benchPos,block("tool_workbench").defaultBlockState());var bench=(MachineEntity)level.getBlockEntity(benchPos);bench.copper.selection=io.github.kpaakeh.smog.copper.ToolDefinitions.index("hammer");
                var hammer=io.github.kpaakeh.smog.copper.ToolParts.example(io.github.kpaakeh.smog.copper.ToolDefinitions.template("hammer"),"bronze");var components=io.github.kpaakeh.smog.copper.ToolParts.components(hammer);for(int role=0;role<components.size();role++)bench.setItem(io.github.kpaakeh.smog.copper.ToolDefinitions.slot(role),components.get(role));bench.sync();
                level.setBlockAndUpdate(new BlockPos(-5,100,-1),block("water_tank").defaultBlockState());var tank=(MachineEntity)level.getBlockEntity(new BlockPos(-5,100,-1));tank.copper.processFluid=4*81000;tank.sync();
            });
            world.getServer().runCommand("tp @a 0.5 104 -5.5 0 35");context.waitTicks(40);context.waitFor(mc->mc.level.getBlockState(new BlockPos(0,100,0)).getValue(MachineBlock.ASSEMBLED));
            context.runOnClient(mc->{
                var player=mc.player;var original=player.getMainHandItem();
                try{
                    var tool=io.github.kpaakeh.smog.copper.ToolParts.example(io.github.kpaakeh.smog.copper.ToolDefinitions.template("pickaxe"),"copper");
                    player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,tool);var stone=Blocks.STONE.defaultBlockState();float base=player.getDestroySpeed(stone);
                    var data=io.github.kpaakeh.smog.copper.ModularToolItem.parts(tool);data.putInt("trait_work_stacks",4);data.putLong("trait_work_until",mc.level.getGameTime()+100);tool.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.of(data));
                    if(player.getDestroySpeed(stone)<=base)throw new IllegalStateException("Active work hardening is missing on the client");
                    data.putLong("trait_work_until",mc.level.getGameTime());tool.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.of(data));
                    if(Math.abs(player.getDestroySpeed(stone)-base)>.0001f)throw new IllegalStateException("Expired work hardening survived the client mining query");
                }finally{player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,original);}
            });
            context.getInput().lookAt(new BlockPos(0,101,1));context.waitTicks(20);context.takeScreenshot("copper-workshop-world");
            world.getServer().runCommand("tp @a 0.5 105 1.5 0 90");context.waitTicks(12);context.takeScreenshot("copper-hearth-liquid-overhead");
            world.getServer().runCommand("tp @a 0.5 104 -5.5 0 35");context.waitTicks(8);
            world.getServer().runCommand("tp @a 0.5 107 -9.5 0 20");context.waitTicks(8);context.getInput().lookAt(new BlockPos(0,103,4));context.waitTicks(10);context.takeScreenshot("copper-logistics-world");
            world.getServer().runCommand("tp @a 8.5 102.5 0.5 0 0");context.waitTicks(10);context.getInput().lookAt(new BlockPos(8,104,4));context.waitTicks(5);context.takeScreenshot("copper-crane-redmarker-front");
            context.waitFor(mc->TestJeiProbe.ready());
            for(String machine:java.util.List.of("copper_hearth","casting_table","potter_table","ceramic_kiln","washing_trough","lever_press","tool_workbench")){
                context.runOnClient(mc->TestJeiProbe.showCopper(machine));context.waitTicks(6);context.takeScreenshot("copper-jei-"+machine);context.setScreen(()->null);
            }
            world.getServer().runOnServer(server->{var player=server.getPlayerList().getPlayers().getFirst();player.openMenu((MachineEntity)server.overworld().getBlockEntity(new BlockPos(5,100,0)));});
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(10);context.takeScreenshot("copper-press-gui");context.setScreen(()->null);
            world.getServer().runOnServer(server->{server.getPlayerList().getPlayers().getFirst().openMenu((MachineEntity)server.overworld().getBlockEntity(new BlockPos(0,100,-3)));});
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(10);context.takeScreenshot("copper-tool-workbench-gui");
            hover(context,20,24);context.takeScreenshot("copper-tool-comparison-tooltip");
            hover(context,85,67);context.takeScreenshot("copper-tool-traits-tooltip");context.setScreen(()->null);
            world.getServer().runCommand("tp @a 0.5 102 -5.5 0 45");context.getInput().lookAt(new BlockPos(0,100,-3));context.waitTicks(12);context.takeScreenshot("copper-tool-workbench-world");
            context.setScreen(()->{var mc=net.minecraft.client.Minecraft.getInstance();return new net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen(mc.player,mc.level.enabledFeatures(),true);});
            for(String tabId:java.util.List.of("copper_age","blocks","mechanisms","controllers","tool_parts","tools")){
                context.runOnClient(mc->{var screen=(net.fabricmc.fabric.api.client.creativetab.v1.FabricCreativeModeInventoryScreen)mc.gui.screen();var tab=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id(tabId));screen.switchToPage(screen.getPage(tab));screen.setSelectedTab(tab);});
                context.waitTicks(5);context.takeScreenshot("copper-creative-"+tabId);
            }
            context.setScreen(()->null);
            world.getServer().runCommand("tp @a -4.5 102 -3.5 0 35");context.getInput().lookAt(new BlockPos(-5,100,0));context.waitTicks(20);context.takeScreenshot("copper-washing-and-water");
            world.getServer().runOnServer(server->{
                var hearth=(MachineEntity)server.overworld().getBlockEntity(new BlockPos(0,100,0));
                hearth.copper.tin=2*9000;hearth.copper.bronze=3*9000;hearth.sync();
                server.getPlayerList().getPlayers().getFirst().openMenu(hearth);
            });
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(10);context.takeScreenshot("copper-smeltery-multiple-layers-gui");context.setScreen(()->null);
            world.getServer().runOnServer(server->{
                var level=server.overworld();var pos=new BlockPos(2,100,-3);level.setBlockAndUpdate(pos,block("casting_table").defaultBlockState());
                var table=(MachineEntity)level.getBlockEntity(pos);table.setItem(1,new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("ingot_mold")));
                try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
                    if(table.castingFluid.insert(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0),4500,tx)!=4500)throw new IllegalStateException("Partial casting fixture rejected");tx.commit();
                }
                server.getPlayerList().getPlayers().getFirst().openMenu(table);
            });
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(10);context.takeScreenshot("copper-casting-partial-gui");context.setScreen(()->null);
            world.getServer().runOnServer(server->{
                var table=(MachineEntity)server.overworld().getBlockEntity(new BlockPos(2,100,-3));
                try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
                    if(table.castingFluid.insert(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0),4500,tx)!=4500)throw new IllegalStateException("Casting top-up fixture rejected");tx.commit();
                }
                server.getPlayerList().getPlayers().getFirst().openMenu(table);
            });
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(6);context.takeScreenshot("copper-casting-cooling-gui");
            context.waitTicks(50);context.takeScreenshot("copper-casting-ready-gui");context.setScreen(()->null);
            context.runOnClient(mc->TestJeiProbe.showCoredCasting());context.waitTicks(8);context.takeScreenshot("copper-jei-cored-casting");context.setScreen(()->null);
            world.getServer().runOnServer(server->{
                var table=(MachineEntity)server.overworld().getBlockEntity(new BlockPos(2,100,-3));table.setItem(3,ItemStack.EMPTY);
                table.setItem(1,new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("pipe_mold")));table.setItem(2,new ItemStack(io.github.kpaakeh.smog.copper.ToolParts.item("ceramic_core"),2));
                try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
                    if(table.castingFluid.insert(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0),3375,tx)!=3375)throw new IllegalStateException("Cored partial casting fixture rejected");tx.commit();
                }
                server.getPlayerList().getPlayers().getFirst().openMenu(table);
            });
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(10);context.takeScreenshot("copper-casting-core-partial-gui");context.setScreen(()->null);
            world.getServer().runCommand("tp @a 2.5 102 -5.5 0 45");context.getInput().lookAt(new BlockPos(2,100,-3));context.waitTicks(12);context.takeScreenshot("copper-casting-core-world");
            world.getServer().runOnServer(server->{
                var table=(MachineEntity)server.overworld().getBlockEntity(new BlockPos(2,100,-3));
                try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
                    if(table.castingFluid.insert(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0),3375,tx)!=3375)throw new IllegalStateException("Cored top-up fixture rejected");tx.commit();
                }
            });
            context.waitTicks(60);
            world.getServer().runOnServer(server->{var table=(MachineEntity)server.overworld().getBlockEntity(new BlockPos(2,100,-3));if(!table.getItem(3).is(io.github.kpaakeh.smog.copper.ToolParts.item("hollow_copper_handle"))||table.getItem(2).getCount()!=1)throw new IllegalStateException("Cored casting did not produce one handle and consume one core");});
            context.takeScreenshot("copper-casting-core-ready-world");
            context.runOnClient(mc->TestJeiProbe.showMultiblocks());context.waitTicks(8);context.takeScreenshot("copper-jei-structure-size-range");context.setScreen(()->null);            world.getServer().runCommand("advancement grant @a from smog:copper/root");context.waitTicks(10);
            context.setScreen(()->{var mc=net.minecraft.client.Minecraft.getInstance();return new net.minecraft.client.gui.screens.advancements.AdvancementsScreen(mc.player.connection.getAdvancements());});
            context.runOnClient(mc->{var advancements=mc.player.connection.getAdvancements();var root=advancements.get(ModBlocks.id("copper/root"));if(root==null)throw new IllegalStateException("Workshop advancement root did not synchronize");advancements.setSelectedTab(root,true);});
            context.waitTicks(12);context.takeScreenshot("copper-workshop-advancements");context.setScreen(()->null);
        }finally{if(previousLanguage!=null)language(context,previousLanguage);}
    }
}

