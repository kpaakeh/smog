package io.github.kpaakeh.techmod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechMod implements ModInitializer {
    public static final String MOD_ID = "tech_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        io.github.kpaakeh.techmod.registry.ContentCatalog.initialize();
        io.github.kpaakeh.techmod.zone.BuiltinZones.initialize();
        io.github.kpaakeh.techmod.zone.ZoneSystem.initialize();
        io.github.kpaakeh.techmod.zone.ZoneCommands.initialize();
        LOGGER.info("Tech Mod initialized");
    }
}
