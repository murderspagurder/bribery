package dev.spagurder.bribery.core;

import dev.spagurder.bribery.config.Config;
import dev.spagurder.bribery.state.BribeData;
import dev.spagurder.bribery.state.BriberyState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

public class TickHandler {

    private static final Queue<BribeKey> pendingBribes = new ArrayDeque<>();
    private record BribeKey(UUID entityUUID, @Nullable UUID playerUUID) {}

    public static void onTick(MinecraftServer server) {
        processNextBribeState(server);
    }

    private static void refreshBribeQueue() {
        pendingBribes.clear();
        BriberyState.bribeStates.forEach((entityUUID, entityStates) -> {
            if (entityStates.isEmpty()) {
                pendingBribes.add(new BribeKey(entityUUID, null));
            } else {
                entityStates.forEach((playerUUID, state) -> {
                    pendingBribes.add(new BribeKey(entityUUID, playerUUID));
                });
            }
        });
    }

    private static void processNextBribeState(MinecraftServer server) {
        BribeKey key = pendingBribes.poll();
        if (key == null) {
            refreshBribeQueue();
            return;
        }

        Map<UUID, BribeData> entityStates = BriberyState.bribeStates.get(key.entityUUID);
        if (entityStates == null) return;
        if (key.playerUUID == null) {
            if (entityStates.isEmpty()) {
                BriberyState.bribeStates.remove(key.entityUUID);
            }
            return;
        }

        BribeData state = entityStates.get(key.playerUUID);
        if (state == null) return;

        processBribeData(
                () -> entityStates.remove(key.playerUUID),
                server, key.entityUUID, key.playerUUID, state
        );
    }

    private static void processBribeData(
            Runnable remove, MinecraftServer server, UUID entityUUID, UUID playerUUID, BribeData state) {
        long gameTime = BriberyUtil.overworldGameTime(server);
        if (state.isExtortionist) {
            long hardDelta = gameTime - state.bribedAt;
            if (hardDelta < 0 || hardDelta >= Config.hardExpiryDays * 28800L) {
                remove.run();
                return;
            }
            LivingEntity entity = (LivingEntity) BriberyUtil.findEntity(server, entityUUID);
            if (entity == null) return;
            ServerPlayer player = BriberyUtil.findPlayer(server, playerUUID);
            if (player == null) return;
            long delta = gameTime - state.extortedAt;
            if (state.isExtorting) {
                if (delta >= 0 && delta < Config.extortionDeadlineMinutes) {
                    ((ServerLevel) entity.level()).sendParticles(
                            ParticleTypes.SMOKE, entity.getX(), entity.getY() + 1.0, entity.getZ(),
                            1, 0.1, 0.1, 0.1, 0.005
                    );
                    return;
                }
                state.isExtortionist = false;
                state.isExtorting = false;
                state.extortedAt = 0L;
                int extortionBalance = state.extortionBalance;
                state.extortionBalance = 0;
                BribeHandler.reject(entity, player, state, extortionBalance);
            } else {
                if (delta < 0 || delta > Config.extortionTimeMinutes) {
                    if (BriberyUtil.inProximitySqr(entity, player, Config.extortionDetectionRangeSqr)) {
                        if (!Config.extortionRequiresLineOfSight || entity.hasLineOfSight(player)) {
                            state.isExtorting = true;
                            state.extortedAt = gameTime;
                            state.extortionBalance = state.largestBribe;
                            player.displayClientMessage(Component.literal(
                                    entity.getDisplayName() + " has demanded additional payment...\n" +
                                            "You have " + Config.extortionTimeMinutes + " minutes to pay... or else..."
                            ), false);
                        }
                    }
                }
            }
        } else if (state.isBribed) {
            if (Config.bribeExpiryMinutes > 0) {
                long delta = gameTime - state.bribedAt;
                if (delta < 0 || delta >= Config.bribeExpiryMinutes * 1200L) {
                    remove.run();
                }
            }
        } else if (state.isRejected) {
            long delta = gameTime - state.rejectedAt;
            if (delta < 0 || delta >= Config.rejectedCooldownSeconds * 20L) {
                remove.run();
            }
        }
    }

}
