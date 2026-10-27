package io.github.kpaakeh.smog.registry;

import java.util.*;
import io.github.kpaakeh.smog.fluid.ModFluids;
import io.github.kpaakeh.smog.machine.Machines;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

public final class ModCreativeTabs {
    private ModCreativeTabs() {}
    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ModBlocks.id("machines"), FabricCreativeModeTab.builder()
            .title(Component.translatable("itemGroup.smog.machines"))
            .icon(() -> Machines.all().iterator().next().block().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {Machines.all().forEach(e -> output.accept(e.block()));ModItems.all().stream().filter(e->e.tab().equals("machines")).forEach(e->output.accept(e.item()));}).build());
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ModBlocks.id("ores"), FabricCreativeModeTab.builder()
            .title(Component.translatable("itemGroup.smog.ores"))
            .icon(() -> ModOres.all().getFirst().stone().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {ModOres.all().forEach(ore -> ore.contents().forEach(output::accept));ModItems.all().stream().filter(e->e.tab().equals("ores")).forEach(e->output.accept(e.item()));}).build());
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ModBlocks.id("blocks"), FabricCreativeModeTab.builder()
            .title(Component.translatable("itemGroup.smog.blocks"))
            .icon(() -> BuiltInRegistries.ITEM.getValue(ModBlocks.id("machine_casing")).getDefaultInstance())
            .displayItems((parameters, output) -> {
                Set<Item> excluded = new HashSet<>();
                Machines.all().forEach(e -> excluded.add(e.block().asItem()));
                ModOres.all().forEach(ore -> ore.contents().forEach(value -> excluded.add(value.asItem())));
                for (var block : BuiltInRegistries.BLOCK)
                    if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals("smog") && !excluded.contains(block.asItem()) && block.asItem() != Items.AIR)
                        output.accept(block);
                ModFluids.all().forEach(fluid -> output.accept(fluid.bucket));
            }).build());
    }
}
