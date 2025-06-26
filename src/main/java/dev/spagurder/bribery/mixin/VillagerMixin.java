package dev.spagurder.bribery.mixin;

import dev.spagurder.bribery.state.BribeData;
import dev.spagurder.bribery.state.BriberyState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(Villager.class)
public class VillagerMixin {

    @Inject(method = "onReputationEventFrom", at = @At("HEAD"), cancellable = true)
    public void beforeOnReputationEventFrom(ReputationEventType type, Entity entity, CallbackInfo ci) {
        if (entity instanceof ServerPlayer player) {
            Villager villager = (Villager)(Object)this;
            BribeData state = BriberyState.getBribeData(villager.getUUID(), player.getUUID());
            if (state == null) return;
            if (state.isBribed) return;
            if (type.equals(ReputationEventType.VILLAGER_HURT) ||  type.equals(ReputationEventType.VILLAGER_KILLED)) {
                if (state.bribeCredits < 25) return;
                state.bribeCredits -= 25;
                ci.cancel();
            }
        }
    }

    @Redirect(
            method = "gossip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/gossip/GossipContainer;transferFrom(Lnet/minecraft/world/entity/ai/gossip/GossipContainer;Lnet/minecraft/util/RandomSource;I)V"
            )
    )
    public void redirectTransferFrom(
            GossipContainer thisGossips, GossipContainer otherGossips,
            RandomSource randomSource, int i,
            ServerLevel level, Villager otherVillager, long gameTime
    ) {
        GossipContainer filtered = new GossipContainer();
        //? >1.21.4 {
        filtered.putAll(otherGossips);
        //?} else {
        /*// Cleanup access wideners when removing
        otherGossips.gossips.forEach((uUID, entityGossips) -> filtered.getOrCreate(uUID).entries.putAll(entityGossips.entries));
        *///?}

        Map<UUID, BribeData> entityData = BriberyState.bribeStates.get(((Villager)(Object)this).getUUID());
        if (entityData != null) {
            entityData.forEach((uuid, state) -> {
                if (state.isBribed) {
                    filtered.remove(uuid, GossipType.MAJOR_NEGATIVE);
                    filtered.remove(uuid, GossipType.MINOR_NEGATIVE);
                }
            });
        }

        thisGossips.transferFrom(filtered, randomSource, i);
    }

}
