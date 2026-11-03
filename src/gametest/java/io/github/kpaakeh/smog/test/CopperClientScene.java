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
        if(!io.github.kpaakeh.smog.copper.FluidAmounts.format(81000+18000,true).getString().equals("1222 2/9 mB"))throw new IllegalStateException("Exact mB must retain fractional Fabric units");
        if(!io.github.kpaakeh.smog.copper.FluidAmounts.format(1,true).getString().equals("0 1/81 mB"))throw new IllegalStateException("Sub-mB volume must not round to zero");
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
                var saw=io.github.kpaakeh.smog.copper.ToolParts.example(io.github.kpaakeh.smog.copper.ToolDefinitions.template("saw"),"bronze");if(saw.isEmpty())throw new IllegalStateException("Dynamic saw fixture missing");player.getInventory().setItem(6,saw);
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
            hover(context,85,67);context.takeScreenshot("copper-tool-tooltip-overview");
            context.getInput().holdControl();context.waitTicks(3);context.takeScreenshot("copper-tool-tooltip-properties");
            context.runOnClient(mc->{var tool=io.github.kpaakeh.smog.copper.ToolParts.example(io.github.kpaakeh.smog.copper.ToolDefinitions.template("pickaxe"),"copper");var lines=tool.getTooltipLines(net.minecraft.world.item.Item.TooltipContext.of(mc.level),mc.player,net.minecraft.world.item.TooltipFlag.NORMAL);if(lines.stream().noneMatch(line->line.getString().contains("Характеристики")||line.getString().contains("Properties")))throw new IllegalStateException("SDL Ctrl did not expand tool properties");});
            context.getInput().releaseControl();context.getInput().holdAlt();context.waitTicks(3);context.takeScreenshot("copper-tool-tooltip-construction");
            context.runOnClient(mc->{var tool=io.github.kpaakeh.smog.copper.ToolParts.example(io.github.kpaakeh.smog.copper.ToolDefinitions.template("pickaxe"),"copper");var lines=tool.getTooltipLines(net.minecraft.world.item.Item.TooltipContext.of(mc.level),mc.player,net.minecraft.world.item.TooltipFlag.NORMAL);if(lines.stream().noneMatch(line->line.getString().equals("Состав")||line.getString().equals("Construction")))throw new IllegalStateException("SDL Alt did not expand tool construction");});
            context.getInput().releaseAlt();
            world.getServer().runOnServer(server->{var player=server.getPlayerList().getPlayers().getFirst();player.getInventory().getItem(0).set(net.minecraft.core.component.DataComponents.TOOLTIP_STYLE,ModBlocks.id("workshop"));player.containerMenu.broadcastChanges();});
            context.waitFor(mc->ModBlocks.id("workshop").equals(mc.player.getInventory().getItem(0).get(net.minecraft.core.component.DataComponents.TOOLTIP_STYLE)));
            int[] legacyHeight={0};context.runOnClient(mc->legacyHeight[0]=((io.github.kpaakeh.smog.gui.MachineScreen)mc.gui.screen()).getMenu().layout.height);hover(context,16,legacyHeight[0]-16);context.takeScreenshot("copper-tool-tooltip-legacy-vanilla");
            world.getServer().runOnServer(server->{var player=server.getPlayerList().getPlayers().getFirst();player.getInventory().getItem(0).remove(net.minecraft.core.component.DataComponents.TOOLTIP_STYLE);player.containerMenu.broadcastChanges();});
            context.setScreen(()->null);
            world.getServer().runCommand("tp @a 0.5 102 -5.5 0 45");context.getInput().lookAt(new BlockPos(0,100,-3));context.waitTicks(12);context.takeScreenshot("copper-tool-workbench-world");
            context.setScreen(()->{var mc=net.minecraft.client.Minecraft.getInstance();return new net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen(mc.player,mc.level.enabledFeatures(),true);});
            for(String tabId:java.util.List.of("copper_age","materials","blocks","mechanisms","controllers","tool_parts","tools")){
                context.runOnClient(mc->{var screen=(net.fabricmc.fabric.api.client.creativetab.v1.FabricCreativeModeInventoryScreen)mc.gui.screen();var tab=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id(tabId));screen.switchToPage(screen.getPage(tab));screen.setSelectedTab(tab);});
                context.waitTicks(5);context.takeScreenshot("copper-creative-"+tabId);
            }
            context.setScreen(()->null);
            world.getServer().runCommand("tp @a -2.5 102 -5.5 0 25");context.waitTicks(6);
            world.getServer().runOnServer(server->{
                var pos=new BlockPos(-2,100,-3);var level=server.overworld();level.setBlockAndUpdate(pos,block("potter_table").defaultBlockState());var potter=(MachineEntity)level.getBlockEntity(pos);potter.setItem(0,new ItemStack(Items.CLAY_BALL,16));potter.sync();server.getPlayerList().getPlayers().getFirst().openMenu(potter);
            });
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen screen&&screen.getMenu().pottery());context.waitTicks(8);context.takeScreenshot("copper-pottery-picker");
            hover(context,76,23);context.takeScreenshot("copper-pottery-choice-hover");context.getInput().pressMouse(com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_LEFT);context.waitTicks(6);
            context.takeScreenshot("copper-pottery-after-click");context.runOnClient(mc->{var menu=((io.github.kpaakeh.smog.gui.MachineScreen)mc.gui.screen()).getMenu();if(menu.potterySelection()!=1||menu.container.getItem(0).getCount()!=16)throw new IllegalStateException("Pottery selection must not consume clay: selected="+menu.potterySelection()+", clay="+menu.container.getItem(0).getCount()+", size="+menu.layout.width+"x"+menu.layout.height);});
            context.takeScreenshot("copper-pottery-selected");hover(context,151,41);context.getInput().pressMouse(com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_LEFT);context.waitTicks(6);
            world.getServer().runOnServer(server->{var potter=(MachineEntity)server.overworld().getBlockEntity(new BlockPos(-2,100,-3));if(potter.getItem(0).getCount()!=14||!potter.getItem(3).isEmpty())throw new IllegalStateException("Taking pottery must consume exactly one recipe without storing a duplicate: input="+potter.getItem(0)+", stored="+potter.getItem(3)+", cursor="+server.getPlayerList().getPlayers().getFirst().containerMenu.getCarried());});
            context.takeScreenshot("copper-pottery-taken");context.setScreen(()->null);
            world.getServer().runOnServer(server->{var level=server.overworld();var pos=new BlockPos(-8,100,-3);level.setBlockAndUpdate(pos,block("water_tank").defaultBlockState());var tank=(MachineEntity)level.getBlockEntity(pos);tank.serverTick();try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){tank.processFluid.insert(net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(BuiltInRegistries.FLUID.getValue(ModBlocks.id("plant_oil"))),12345,tx);tx.commit();}server.getPlayerList().getPlayers().getFirst().openMenu(tank);});
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen screen&&screen.getMenu().layout.id.equals("water_supply"));context.waitTicks(8);context.runOnClient(mc->{var menu=((io.github.kpaakeh.smog.gui.MachineScreen)mc.gui.screen()).getMenu();if(((menu.data.get(23)&65535)|(menu.data.get(24)<<16))!=12345)throw new IllegalStateException("Reservoir GUI must retain the exact fractional-mB amount");});context.takeScreenshot("copper-reservoir-oil-gui");context.setScreen(()->null);
            world.getServer().runCommand("tp @a -4.5 102 -3.5 0 35");context.getInput().lookAt(new BlockPos(-5,100,0));context.waitTicks(20);context.takeScreenshot("copper-washing-and-water");
            world.getServer().runOnServer(server->{
                var hearth=(MachineEntity)server.overworld().getBlockEntity(new BlockPos(0,100,0));
                hearth.copper.tin=2*9000;hearth.copper.bronze=3*9000;hearth.sync();
                server.getPlayerList().getPlayers().getFirst().openMenu(hearth);
            });
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(10);context.takeScreenshot("copper-smeltery-multiple-layers-gui");
            int liquidY=context.computeOnClient(mc->{var menu=((io.github.kpaakeh.smog.gui.MachineScreen)mc.gui.screen()).getMenu();var layer=io.github.kpaakeh.smog.gui.SmelteryGui.layers(menu).getFirst();return layer.y()+layer.height()/2;});
            hover(context,100,liquidY);context.takeScreenshot("copper-smeltery-volume-units");
            context.getInput().holdShift();context.waitTicks(3);
            context.runOnClient(mc->{if(!io.github.kpaakeh.smog.gui.FluidAmountText.format(1000).getString().contains("mB"))throw new IllegalStateException("SDL left Shift did not enable exact mB");});
            context.takeScreenshot("copper-smeltery-volume-millibuckets");context.getInput().releaseShift();
            context.getInput().holdKey(com.mojang.blaze3d.platform.InputConstants.KEY_RSHIFT);context.waitTicks(3);
            context.runOnClient(mc->{if(!io.github.kpaakeh.smog.gui.FluidAmountText.format(1000).getString().contains("mB"))throw new IllegalStateException("SDL right Shift did not enable exact mB");});
            context.getInput().releaseKey(com.mojang.blaze3d.platform.InputConstants.KEY_RSHIFT);context.waitTicks(2);
            context.runOnClient(mc->{if(io.github.kpaakeh.smog.gui.FluidAmountText.format(1000).getString().contains("mB"))throw new IllegalStateException("Releasing Shift must restore block/ingot/nugget units");});
            context.setScreen(()->null);
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
                    if(table.castingFluid.insert(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0),2250,tx)!=2250)throw new IllegalStateException("Cored partial casting fixture rejected");tx.commit();
                }
                server.getPlayerList().getPlayers().getFirst().openMenu(table);
            });
            context.waitFor(mc->mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen);context.waitTicks(10);context.takeScreenshot("copper-casting-core-partial-gui");context.setScreen(()->null);
            world.getServer().runCommand("tp @a 2.5 102 -5.5 0 45");context.getInput().lookAt(new BlockPos(2,100,-3));context.waitTicks(12);context.takeScreenshot("copper-casting-core-world");
            world.getServer().runOnServer(server->{
                var table=(MachineEntity)server.overworld().getBlockEntity(new BlockPos(2,100,-3));
                try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
                    if(table.castingFluid.insert(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0),2250,tx)!=2250)throw new IllegalStateException("Cored top-up fixture rejected");tx.commit();
                }
            });
            context.waitTicks(60);
            world.getServer().runOnServer(server->{var table=(MachineEntity)server.overworld().getBlockEntity(new BlockPos(2,100,-3));if(!table.getItem(3).is(io.github.kpaakeh.smog.copper.ToolParts.item("copper_standard_handle"))||table.getItem(2).getCount()!=1)throw new IllegalStateException("Cored casting did not produce one handle and consume one core");});
            context.takeScreenshot("copper-casting-core-ready-world");
            world.getServer().runOnServer(server->{
                var level=server.overworld();var pos=new BlockPos(5,100,0);var press=(MachineEntity)level.getBlockEntity(pos);var drive=pos.above();
                level.setBlockAndUpdate(drive,block("bronze_drive").defaultBlockState().setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING,Direction.NORTH));if(!press.tryAssemble())throw new IllegalStateException("Finale press with drive did not assemble");
                press.setItem(0,ItemStack.EMPTY);press.setItem(3,ItemStack.EMPTY);press.copper.processFluid=0;press.copper.queued=0;press.copper.work=0;press.copper.autoWork=false;
                for(int y=100;y<=103;y++)for(var side:java.util.List.of(Direction.NORTH,Direction.SOUTH,Direction.EAST,Direction.WEST)){var glass=new BlockPos(5,y,-1).relative(side);if(!glass.equals(pos)&&!glass.equals(drive))level.setBlockAndUpdate(glass,Blocks.GLASS.defaultBlockState());}
                level.setBlockAndUpdate(new BlockPos(5,103,-1),Blocks.WATER.defaultBlockState());press.sync();
            });
            world.getServer().runCommand("tp @a 8.5 103 -3.5 35 25");context.waitTicks(30);
            context.runOnClient(mc->{var drive=new BlockPos(5,101,0);if(!io.github.kpaakeh.smog.copper.BronzeDrive.working(mc.level,drive))throw new IllegalStateException("Real flowing water did not power the finale drive");});
            world.getServer().runOnServer(server->{
                var level=server.overworld();var player=server.getPlayerList().getPlayers().getFirst();player.getInventory().setSelectedSlot(8);player.getInventory().setItem(8,ItemStack.EMPTY);var drive=new BlockPos(5,101,0);
                var used=player.gameMode.useItemOn(player,level,ItemStack.EMPTY,net.minecraft.world.InteractionHand.MAIN_HAND,new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(drive),Direction.EAST,drive,false));
                var press=(MachineEntity)level.getBlockEntity(drive.below());if(!press.copper.commissioningOwner.equals(player.getUUID().toString()))throw new IllegalStateException("Native empty-hand use did not arm commissioning: result="+used+", assembled="+press.assembled()+", enabled="+press.enabled()+", claim="+io.github.kpaakeh.smog.multiblock.complex.ComplexFormationData.get(level).owner(drive)+", flow="+io.github.kpaakeh.smog.copper.BronzeDrive.working(level,drive)+", chapter="+player.getAdvancements().getOrStartProgress(server.getAdvancements().get(ModBlocks.id("copper/mechanized"))).isDone());press.setItem(0,new ItemStack(Items.WHEAT_SEEDS,16));press.sync();
            });
            context.runOnClient(mc->mc.gui.toastManager().clear());context.getInput().lookAt(new BlockPos(5,101,0));context.takeScreenshot("copper-finale-running");
            context.waitFor(mc->mc.level.getBlockEntity(new BlockPos(5,100,0)) instanceof MachineEntity press&&press.copper.commissioningWork>=2);
            world.getServer().runOnServer(server->{var player=server.getPlayerList().getPlayers().getFirst();var node=server.getAdvancements().get(ModBlocks.id("copper/mechanized"));var press=(MachineEntity)server.overworld().getBlockEntity(new BlockPos(5,100,0));if(!player.getAdvancements().getOrStartProgress(node).isDone()||press.getItem(3).getCount()!=2||press.copper.processFluid!=40500)throw new IllegalStateException("The finale must award real automatic production and preserve its outputs");});
            context.waitTicks(6);context.takeScreenshot("copper-finale-complete");
            context.runOnClient(mc->TestJeiProbe.showMultiblocks());context.waitTicks(8);context.takeScreenshot("copper-jei-structure-size-range");context.setScreen(()->null);            world.getServer().runCommand("advancement grant @a from smog:copper/root");context.waitTicks(10);
            context.setScreen(()->{var mc=net.minecraft.client.Minecraft.getInstance();return new net.minecraft.client.gui.screens.advancements.AdvancementsScreen(mc.player.connection.getAdvancements());});
            context.runOnClient(mc->{var advancements=mc.player.connection.getAdvancements();var root=advancements.get(ModBlocks.id("copper/root"));if(root==null)throw new IllegalStateException("Workshop advancement root did not synchronize");advancements.setSelectedTab(root,true);});
            context.waitTicks(12);context.runOnClient(mc->mc.gui.toastManager().clear());context.waitTicks(2);context.takeScreenshot("copper-workshop-advancements");
            context.runOnClient(mc->mc.gui.screen().mouseScrolled(mc.getWindow().getGuiScaledWidth()/2.0,mc.getWindow().getGuiScaledHeight()/2.0,0,20));context.waitTicks(2);context.takeScreenshot("copper-workshop-advancements-upper");
            context.runOnClient(mc->mc.gui.screen().mouseScrolled(mc.getWindow().getGuiScaledWidth()/2.0,mc.getWindow().getGuiScaledHeight()/2.0,0,-40));context.waitTicks(2);context.takeScreenshot("copper-workshop-advancements-lower");context.setScreen(()->null);
        }finally{if(previousLanguage!=null)language(context,previousLanguage);}
    }
}

