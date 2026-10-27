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
    public static void initialize(com.google.gson.JsonObject catalog) {
        if(!catalog.getAsJsonArray("machines").isEmpty())Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ModBlocks.id("machines"), FabricCreativeModeTab.builder()
            .title(Component.translatable("itemGroup.smog.machines"))
            .icon(() -> Machines.all().iterator().next().block().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {Machines.all().forEach(e -> output.accept(e.block()));ModItems.all().stream().filter(e->e.tab().equals("machines")).forEach(e->output.accept(e.item()));}).build());
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ModBlocks.id("ores"), FabricCreativeModeTab.builder()
            .title(Component.translatable("itemGroup.smog.ores"))
            .icon(() -> ModOres.all().getFirst().stone().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {ModOres.all().forEach(ore -> ore.contents().forEach(output::accept));ModAlloys.all().forEach(alloy->alloy.contents().forEach(output::accept));ModDeposits.all().forEach(deposit->{output.accept(deposit.chunk());output.accept(deposit.dust());});ModItems.all().stream().filter(e->e.tab().equals("ores")).forEach(e->output.accept(e.item()));}).build());
        // Catalog ownership is explicit: test fixtures and late registrations cannot leak into tabs.
        List<net.minecraft.world.level.block.Block> blocks=new ArrayList<>();
        for(var value:catalog.getAsJsonArray("blocks"))blocks.add(BuiltInRegistries.BLOCK.getValue(ModBlocks.id(value.getAsJsonObject().get("name").getAsString())));
        ModDeposits.all().forEach(deposit->blocks.add(deposit.block()));
        var fluids=ModFluids.all();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ModBlocks.id("blocks"), FabricCreativeModeTab.builder()
            .title(Component.translatable("itemGroup.smog.blocks"))
            .icon(()->ModDeposits.all().isEmpty()?Items.STONE.getDefaultInstance():ModDeposits.all().getFirst().block().asItem().getDefaultInstance())
            .displayItems((parameters,output)->{blocks.forEach(output::accept);fluids.forEach(fluid->output.accept(fluid.bucket));}).build());
    }
}
