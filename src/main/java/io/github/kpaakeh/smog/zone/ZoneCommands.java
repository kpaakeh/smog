package io.github.kpaakeh.smog.zone;

import com.mojang.brigadier.arguments.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.phys.Vec3;
import static net.minecraft.commands.Commands.*;

public final class ZoneCommands {
    private ZoneCommands() {}
    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registries, environment) -> dispatcher.register(
            literal("smogzone").requires(s -> s.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .then(literal("list").executes(c -> {
                    var zones = ZoneData.get(c.getSource().getLevel()).all();
                    zones.forEach(z -> c.getSource().sendSuccess(() -> Component.literal(z.id() + " " + z.type() + " " + z.center()), false));
                    return zones.size();
                }))
                .then(literal("remove").then(argument("id", StringArgumentType.word()).executes(c ->
                    ZoneData.get(c.getSource().getLevel()).remove(StringArgumentType.getString(c, "id")) ? 1 : 0)))
                .then(literal("clear").executes(c -> { ZoneData.get(c.getSource().getLevel()).clear(); return 1; }))
                .then(literal("create").then(argument("type", StringArgumentType.word())
                    .suggests((c,b) -> SharedSuggestionProvider.suggest(ZoneType.ids().stream().map(Identifier::toString), b))
                    .then(argument("radius", DoubleArgumentType.doubleArg(0.1, 512))
                        .executes(c -> create(c.getSource(), StringArgumentType.getString(c,"type"), DoubleArgumentType.getDouble(c,"radius"), 0, 0))
                        .then(argument("seconds", IntegerArgumentType.integer(1, 86400))
                            .executes(c -> create(c.getSource(), StringArgumentType.getString(c,"type"), DoubleArgumentType.getDouble(c,"radius"), IntegerArgumentType.getInteger(c,"seconds"), 0))
                            .then(argument("growth_per_second", DoubleArgumentType.doubleArg(-10, 10))
                                .executes(c -> create(c.getSource(), StringArgumentType.getString(c,"type"), DoubleArgumentType.getDouble(c,"radius"), IntegerArgumentType.getInteger(c,"seconds"), DoubleArgumentType.getDouble(c,"growth_per_second"))))))))));
    }
    private static int create(CommandSourceStack source, String name, double radius, int seconds, double growth) {
        Identifier type = Identifier.tryParse(name.contains(":") ? name : "smog:" + name);
        if (type == null || ZoneType.get(type) == null) { source.sendFailure(Component.literal("Unknown zone type: " + name)); return 0; }
        Zone zone = Zone.sphere(type, source.getPosition(), radius);
        zone = new Zone(zone.id(), type, zone.shape(), zone.center(), zone.halfSize(), Vec3.ZERO,
            new Vec3(growth/20, growth/20, growth/20), seconds == 0 ? -1 : source.getLevel().getGameTime() + seconds * 20L, 1);
        ZoneData.get(source.getLevel()).put(zone);
        String id = zone.id(); source.sendSuccess(() -> Component.literal("Created zone " + id), true);
        return 1;
    }
}
