package io.github.kpaakeh.smog.test;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import java.nio.file.*;
import java.util.*;
/** Actual supported GUI scales at a fixed physical viewport; no desktop input. */
public final class ScaleReviewClientScene {
 private static boolean checking;
 public static void run(ClientGameTestContext context){
  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(mc->{if(checking&&mc.gui.screen() instanceof io.github.kpaakeh.smog.gui.MachineScreen screen){var layout=screen.getMenu().layout;if(layout.width>mc.getWindow().getGuiScaledWidth()||layout.height>mc.getWindow().getGuiScaledHeight())throw new IllegalStateException("Menu exceeds viewport: "+layout.id);}});
  checking=true;
  context.runOnClient(mc->mc.getWindow().setWindowed(1280,960));context.waitTicks(10);
  for(int scale:new int[]{1,2,3,4}){
   var before=files();final int value=scale;
   context.runOnClient(mc->{mc.options.guiScale().set(value);mc.resizeGui();});context.waitTicks(8);
   context.runOnClient(mc->{if(mc.getWindow().getGuiScale()!=value)throw new IllegalStateException("Requested GUI scale was clamped: "+value);});
   context.takeScreenshot("scale-review-start-"+scale);CopperClientScene.run(context);archive(before,"scale-"+scale);
  }
  VisualReviewClientScene.run(context);archive(Set.of(),"combined-world-and-scale-review");
  LogisticsClientScene.run(context);checking=false;
 }
 private static Set<Path> files(){try{var p=Path.of("screenshots");if(!Files.exists(p))return Set.of();try(var stream=Files.list(p)){return new HashSet<>(stream.toList());}}catch(Exception e){throw new RuntimeException(e);}}
 private static void archive(Set<Path> before,String name){try{var target=Path.of("..","..","review","2026-11-03",name);Files.createDirectories(target);for(var p:files())if(!before.contains(p))Files.copy(p,target.resolve(p.getFileName()),StandardCopyOption.REPLACE_EXISTING);}catch(Exception e){throw new RuntimeException(e);}}
}
