package io.github.kpaakeh.smog.test;

import java.nio.file.*;
import java.util.*;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.*;
import io.github.kpaakeh.smog.gui.MachineScreen;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;

/** Separate Java process and loopback TCP connection; no desktop input or user save. */
public final class MultiplayerClientScene {
    private MultiplayerClientScene(){}
    private static void mark(Path directory,String phase){try{Files.writeString(directory.resolve(phase),"ok");}catch(java.io.IOException e){throw new RuntimeException(e);}}
    private static void waitFor(ClientGameTestContext context,Path directory,String phase){
        context.waitFor(mc->{if(Files.exists(directory.resolve("peer-error.txt")))throw new IllegalStateException("Peer failed; read "+directory.resolve("peer-error.txt"));return Files.exists(directory.resolve(phase));},2400);
    }
    public static void run(ClientGameTestContext context){
        if(Integer.getInteger("smog.peerPort",0)>0){peer(context);return;}
        Process child=null;
        Path directory=Path.of(System.getProperty("user.dir")).toAbsolutePath().resolve("multiplayer-peer-"+System.currentTimeMillis());
        try(var world=context.worldBuilder().create()){
            Files.createDirectories(directory);var pos=new BlockPos(0,100,0);
            world.getServer().runOnServer(server->{server.setUsesAuthentication(false);var level=server.overworld();level.setBlockAndUpdate(pos,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("casting_table")).defaultBlockState());var table=(MachineEntity)level.getBlockEntity(pos);table.setItem(3,new ItemStack(Items.COPPER_INGOT,5));var host=server.getPlayerList().getPlayers().getFirst();host.setPos(.5,100,-2);});
            int port;try(var socket=new java.net.ServerSocket(0)){port=socket.getLocalPort();}
            final int tcpPort=port;
            context.runOnClient(mc->{if(!mc.getSingleplayerServer().publishServer(MinecraftServer.MultiplayerScope.LAN,false,tcpPort))throw new IllegalStateException("Cannot publish isolated LAN server");});
            child=startPeer(directory,port);
            waitFor(context,directory,"joined");
            world.getServer().runOnServer(server->{
                if(server.getPlayerList().getPlayerCount()!=2)throw new IllegalStateException("Expected two real client connections");
                for(var player:server.getPlayerList().getPlayers()){player.setPos(.5,100,-2);player.openMenu((MachineEntity)server.overworld().getBlockEntity(pos));}
            });
            context.waitFor(mc->mc.gui.screen() instanceof MachineScreen,1200);waitFor(context,directory,"menu");
            context.runOnClient(mc->{var menu=mc.player.containerMenu;mc.gameMode.handleContainerInput(menu.containerId,2,0,ContainerInput.PICKUP,mc.player);});
            context.waitFor(mc->mc.player.containerMenu.getCarried().getCount()==5,600);mark(directory,"take");waitFor(context,directory,"taken");
            world.getServer().runOnServer(server->{
                int carried=server.getPlayerList().getPlayers().stream().mapToInt(p->p.containerMenu.getCarried().getCount()).sum();
                var table=(MachineEntity)server.overworld().getBlockEntity(pos);
                if(carried!=5||!table.getItem(3).isEmpty())throw new IllegalStateException("Two real client requests duplicated or lost output");
            });
            context.takeScreenshot("two-real-clients-shared-casting-output");mark(directory,"finish");waitFor(context,directory,"done");
            Files.writeString(directory.resolve("result.json"),"{\"clients\":2,\"transport\":\"loopback TCP\",\"sharedOutput\":\"passed\"}");
        }catch(Exception e){throw new RuntimeException(e);}
        finally{if(child!=null&&child.isAlive()){child.destroy();try{if(!child.waitFor(5,java.util.concurrent.TimeUnit.SECONDS))child.destroyForcibly();}catch(InterruptedException e){Thread.currentThread().interrupt();child.destroyForcibly();}}}
    }
    private static void peer(ClientGameTestContext context){
        var directory=Path.of(System.getProperty("smog.peerDirectory"));
        try{
            int port=Integer.getInteger("smog.peerPort");String address="127.0.0.1:"+port;
            context.runOnClient(mc->ConnectScreen.startConnecting(new TitleScreen(),mc,ServerAddress.parseString(address),new ServerData("Smog isolated test",address,ServerData.Type.LAN),false,null));
            context.waitFor(mc->mc.level!=null&&mc.player!=null,2400);mark(directory,"joined");
            context.waitFor(mc->mc.gui.screen() instanceof MachineScreen,1200);mark(directory,"menu");waitFor(context,directory,"take");
            context.runOnClient(mc->{var menu=mc.player.containerMenu;mc.gameMode.handleContainerInput(menu.containerId,2,0,ContainerInput.PICKUP,mc.player);});
            context.waitTicks(10);context.runOnClient(mc->{if(!mc.player.containerMenu.getCarried().isEmpty())throw new IllegalStateException("Peer received duplicate already-taken output");});
            context.takeScreenshot("peer-shared-casting-output");mark(directory,"taken");waitFor(context,directory,"finish");mark(directory,"done");
        }catch(Throwable e){try{Files.writeString(directory.resolve("peer-error.txt"),e.toString());}catch(Exception ignored){}throw new RuntimeException(e);}
    }
    private static Process startPeer(Path directory,int port)throws Exception{
        var info=ProcessHandle.current().info();var arguments=new ArrayList<>(Arrays.asList(info.arguments().orElseThrow()));
        // Refuse ambiguous @argument files rather than accidentally sharing a profile or credentials.
        if(arguments.stream().anyMatch(s->s.startsWith("@")))throw new IllegalStateException("Peer harness cannot safely rewrite a Java @argument file");
        arguments.removeIf(s->s.startsWith("-Xmx")||s.startsWith("-Xms")||s.startsWith("-Dsmog.peer")||s.startsWith("-Dsmog.multiplayerTest"));
        setArgument(arguments,"--gameDir",directory.resolve("game").toString());setArgument(arguments,"--username","SmogPeer");setArgument(arguments,"--uuid",UUID.nameUUIDFromBytes("OfflinePlayer:SmogPeer".getBytes(java.nio.charset.StandardCharsets.UTF_8)).toString().replace("-",""));
        arguments.addFirst("-Dsmog.peerDirectory="+directory);arguments.addFirst("-Dsmog.peerPort="+port);arguments.addFirst("-Dsmog.multiplayerTest=true");arguments.addFirst("-Xmx1G");arguments.addFirst(info.command().orElseThrow());
        Files.createDirectories(directory.resolve("game"));
        var child=new ProcessBuilder(arguments).directory(directory.resolve("game").toFile()).redirectErrorStream(true).redirectOutput(directory.resolve("peer.log").toFile()).start();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{if(child.isAlive())child.destroyForcibly();},"Smog-peer-cleanup"));return child;
    }
    private static void setArgument(List<String> arguments,String option,String value){int at=arguments.indexOf(option);if(at>=0){if(at+1>=arguments.size())throw new IllegalArgumentException(option);arguments.set(at+1,value);}else{arguments.add(option);arguments.add(value);}}
}
