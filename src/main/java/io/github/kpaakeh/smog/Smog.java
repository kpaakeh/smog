package io.github.kpaakeh.smog;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Smog implements ModInitializer {
    public static final String MOD_ID = "smog";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        io.github.kpaakeh.smog.gui.GuiLayouts.initialize();
        io.github.kpaakeh.smog.registry.ContentCatalog.initialize();
        io.github.kpaakeh.smog.machine.ProcessingRecipes.initialize();
        io.github.kpaakeh.smog.energy.EnergyNetwork.initialize();
        io.github.kpaakeh.smog.zone.BuiltinZones.initialize();
        io.github.kpaakeh.smog.zone.ZoneSystem.initialize();
        io.github.kpaakeh.smog.zone.ZoneCommands.initialize();
        LOGGER.info("Smog initialized");
    }
}
