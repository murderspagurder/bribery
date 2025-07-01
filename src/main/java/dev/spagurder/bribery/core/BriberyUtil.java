package dev.spagurder.bribery.core;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.Vec3;
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

    public static void makeMobAngry(NeutralMob mob, ServerPlayer player) {
        mob.setTarget(player);
        mob.setPersistentAngerTarget(player.getUUID());
        mob.setRemainingPersistentAngerTime(12000);
    }

    public static boolean inFOV(LivingEntity observer, LivingEntity target, double fovDegrees) {
        Vec3 observerPos = observer.getEyePosition();
        Vec3 targetPos = target.getEyePosition();
        Vec3 targetVector = targetPos.subtract(observerPos).normalize();
        Vec3 lookVector = observer.getLookAngle().normalize();
        double dot = lookVector.dot(targetVector);
        double angle = Math.acos(dot) * (180 / Math.PI);
        return angle <= (fovDegrees / 2.0);
    }

    public static boolean isBribable(LivingEntity entity) {
        for (Class<? extends LivingEntity> clazz : BribableEntityRegistry.BRIBABLE_ENTITIES) {
            if (clazz.isInstance(entity)) return true;
        }
        return false;
    }

}
