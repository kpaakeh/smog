package io.github.kpaakeh.smog.gui;

import java.util.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.*;
import net.minecraft.world.flag.FeatureFlags;

/** Register one layout to obtain its MenuType, client screen and synchronized slot/data handling. */
public final class GuiLayouts {
    public record SlotPosition(int index,int x,int y,boolean output) {}
    public static final class Layout {
        public final String id;
        public final int width,height;
        public final net.minecraft.resources.Identifier texture;
        public final List<SlotPosition> slots;
        public final int inventorySize;
        public MenuType<MachineMenu> type;
        private Layout(String id,int width,int height,List<SlotPosition> slots,net.minecraft.resources.Identifier texture) {
            this.id=id;this.width=width;this.height=height;this.slots=List.copyOf(slots);this.texture=texture;
            inventorySize=slots.stream().mapToInt(SlotPosition::index).max().orElse(-1)+1;
        }
    }
    private static final Map<String,Layout> VALUES=new LinkedHashMap<>();
    private GuiLayouts() {}
    public static Layout register(String id,int width,int height,List<SlotPosition> slots) {
        return register(id,width,height,slots,ModBlocks.id("textures/gui/"+id+".png"));
    }
    public static Layout register(String id,int width,int height,List<SlotPosition> slots,net.minecraft.resources.Identifier texture) {
        if(VALUES.containsKey(id))throw new IllegalArgumentException("Duplicate GUI "+id);
        Layout layout=new Layout(id,width,height,slots,texture);
        layout.type=Registry.register(BuiltInRegistries.MENU,ModBlocks.id(id),new MenuType<>((window,inventory)->
            new MachineMenu(layout,window,inventory,new SimpleContainer(layout.inventorySize),new SimpleContainerData(MachineMenu.DATA_SIZE)),FeatureFlags.DEFAULT_FLAGS));
        VALUES.put(id,layout);return layout;
    }
    public static Layout get(String id) { return Objects.requireNonNull(VALUES.get(id),id); }
    public static Collection<Layout> all() { return List.copyOf(VALUES.values()); }
    public static void initialize() {
        register("copper_workshop",176,190,List.of(new SlotPosition(0,26,35,false),new SlotPosition(1,71,35,false),new SlotPosition(2,26,65,false),new SlotPosition(3,134,35,true)));
        register("processor",176,190,List.of(new SlotPosition(0,35,35,false),new SlotPosition(3,110,35,true)));
        register("generator",176,190,List.of(new SlotPosition(2,35,35,false),new SlotPosition(3,110,35,true)));
        register("environment",176,190,List.of());
    }
}
