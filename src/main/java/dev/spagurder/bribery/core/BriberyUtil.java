package dev.spagurder.bribery.core;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BriberyUtil {

    @Nullable
    public static Entity findEntity(MinecraftServer server, UUID entityUUID) {
        for (ServerLevel level : server.getAllLevels()) {
            Entity entity = level.getEntity(entityUUID);
            if (entity != null) return entity;
        }
        return null;
    }

    @Nullable
    public static ServerPlayer findPlayer(MinecraftServer server, UUID playerUUID) {
        return server.getPlayerList().getPlayer(playerUUID);
    }

    public static boolean inProximitySqr(Entity e1, Entity e2, double distance) {
        if (e1.level() != e2.level()) return false;
        return e1.distanceToSqr(e2) <= distance;
    }

    public static long overworldGameTime(MinecraftServer server) {
        return server.overworld().getGameTime();
    }

}
