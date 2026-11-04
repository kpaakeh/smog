package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import java.util.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

/** Native item pages, placed geometry, assembly views and a genuine finite crank stroke. */
public final class BronzeRegistrationClientScene {
    public static void run(ClientGameTestContext context){
        String requested=System.getProperty("smog.testLanguage","ru_ru");String previous=CopperClientScene.language(context,requested);
        context.runOnClient(mc->{mc.getWindow().setWindowed(1280,960);mc.options.guiScale().set(2);mc.options.fov().set(65);mc.resizeGui();});
        try(var world=context.worldBuilder().create()){
            var server=world.getServer();server.runCommand("fill -20 99 -20 30 99 30 minecraft:stone");server.runCommand("time set noon");server.runCommand("weather clear");
            server.runOnServer(s->{var p=s.getPlayerList().getPlayers().getFirst();p.setGameMode(net.minecraft.world.level.GameType.CREATIVE);p.getAbilities().flying=true;p.onUpdateAbilities();});
            server.runCommand("tp @a 4.5 106 -10 0 15");context.waitFor(mc->mc.player!=null&&mc.player.isCreative());
            context.setScreen(()->{var mc=net.minecraft.client.Minecraft.getInstance();return new net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen(mc.player,mc.level.enabledFeatures(),true);});
            var expected=new ArrayList<ItemStack>();var seen=new HashSet<net.minecraft.resources.Identifier>();
            context.runOnClient(mc->{var tab=BuiltInRegistries.CREATIVE_MODE_TAB.getValue(ModBlocks.id("bronze_age"));expected.addAll(tab.getDisplayItems());var screen=(net.fabricmc.fabric.api.client.creativetab.v1.FabricCreativeModeInventoryScreen)mc.gui.screen();screen.switchToPage(screen.getPage(tab));screen.setSelectedTab(tab);});
            for(int page=0;page<5;page++){
                if(page>0)context.runOnClient(mc->mc.gui.screen().mouseScrolled(mc.getWindow().getGuiScaledWidth()/2.,mc.getWindow().getGuiScaledHeight()/2.,0,-4));context.waitTicks(5);
                context.runOnClient(mc->{var menu=((net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen)mc.gui.screen()).getMenu();for(int slot=0;slot<45;slot++){var stack=menu.getSlot(slot).getItem();if(!stack.isEmpty())seen.add(BuiltInRegistries.ITEM.getKey(stack.getItem()));}});
                context.takeScreenshot("bronze-items-"+page);
            }
            for(var stack:expected)if(!seen.contains(BuiltInRegistries.ITEM.getKey(stack.getItem())))throw new IllegalStateException("Undiscoverable bronze item: "+stack);
            context.setScreen(()->null);
            if(net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("jei")){
                context.waitFor(mc->TestJeiProbe.ready());
                for(String id:List.of("bronze_stamper","bronze_casting_carousel_8","bronze_mine_hoist_24")){
                    context.runOnClient(mc->TestJeiProbe.showBronzeBlueprint(id));context.waitTicks(12);context.takeScreenshot("bronze-jei-"+id);context.setScreen(()->null);
                }
            }
            for(String group:List.of("network","structure","controller","decor")){
                var blocks=BronzeContent.blocks().stream().filter(b->b.group().equals(group)).toList();
                for(int page=0;page<(blocks.size()+11)/12;page++){
                    server.runCommand("fill -2 100 -2 14 104 12 minecraft:air");final int start=page*12;
                    server.runOnServer(s->{for(int i=0;i<12&&start+i<blocks.size();i++){
                        var entry=blocks.get(start+i);var pos=new BlockPos((i%4)*3,100,(i/4)*3);
                        if(entry.attachment())s.overworld().setBlockAndUpdate(pos.south(),Blocks.OAK_PLANKS.defaultBlockState());
                        s.overworld().setBlockAndUpdate(pos,entry.block().defaultBlockState());
                    }});
                    server.runCommand("tp @a 12 108 -12 0 25");context.waitTicks(20);context.getInput().lookAt(new BlockPos(4,100,3));context.waitTicks(12);context.takeScreenshot("bronze-blocks-"+group+"-"+page);
                }
            }
            // Each controller's first valid drawing, plus the extreme-height mine hoist.
            var selected=new LinkedHashMap<String,BronzeBlueprints.Blueprint>();for(var drawing:BronzeBlueprints.all())selected.putIfAbsent(drawing.controller(),drawing);selected.put("mine_hoist_max",BronzeBlueprints.get("bronze_mine_hoist_24"));selected.put("carousel_eight",BronzeBlueprints.get("bronze_casting_carousel_8"));
            for(var chosen:selected.entrySet()){
                var drawing=chosen.getValue();server.runCommand("fill -10 100 -10 20 132 20 minecraft:air");
                int minY=Math.min(0,drawing.cells().stream().mapToInt(c->c.offset().getY()).min().orElse(0));int maxY=Math.max(0,drawing.cells().stream().mapToInt(c->c.offset().getY()).max().orElse(0));
                var origin=new BlockPos(0,100-minY,0);int height=maxY-minY+1;
                server.runOnServer(s->{BronzeRegistrationTests.place(s.overworld(),origin,Direction.NORTH,drawing);var entity=(BronzeEntity)s.overworld().getBlockEntity(origin);entity.inspect(s.getPlayerList().getPlayers().getFirst());if(entity.blueprint().isEmpty())throw new IllegalStateException("Drawing failed in real client: "+drawing.id());});
                server.runCommand("tp @a "+(height>15?18:9)+" "+(102+height*.6)+" "+(height>15?-27:-12)+" 0 20");context.waitTicks(20);context.getInput().lookAt(new BlockPos(0,100+height/2,1));context.waitTicks(15);context.takeScreenshot("bronze-structure-"+chosen.getKey());
            }
            server.runCommand("fill -10 100 -10 20 132 20 minecraft:air");server.runOnServer(s->s.overworld().setBlockAndUpdate(new BlockPos(0,100,0),BronzeContent.entry("bronze_hand_crank").block().defaultBlockState()));server.runCommand("tp @a 2 102 -3 0 15");context.waitTicks(10);context.getInput().lookAt(new BlockPos(0,100,0));context.waitTicks(5);context.takeScreenshot("bronze-crank-idle");
            server.runOnServer(s->((BronzeEntity)s.overworld().getBlockEntity(new BlockPos(0,100,0))).crank());context.waitTicks(7);context.takeScreenshot("bronze-crank-moving");context.waitTicks(85);context.takeScreenshot("bronze-crank-stopped");
        }
        if(!previous.equals(requested))CopperClientScene.language(context,previous);
    }
}
