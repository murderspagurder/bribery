package dev.spagurder.bribery.core;

import dev.spagurder.bribery.config.Config;
import dev.spagurder.bribery.config.CurrencyConfig;
import dev.spagurder.bribery.config.EntityConfig;
import dev.spagurder.bribery.config.TransientConfig;
import dev.spagurder.bribery.state.BribeData;
import dev.spagurder.bribery.state.BriberyState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class BribeHandler {

    public static boolean handle(LivingEntity entity, ServerPlayer player, ItemStack bribe) {
        EntityConfig ec;
        if (entity instanceof Villager) {
            ec = new EntityConfig(EntityConfig.VILLAGER);
        } else if (BriberyUtil.isBribable(entity)) {
            ec = new EntityConfig(EntityConfig.GOLEM);
        } else {
            return true;
        }
        witnessBribe(entity, player);

        CurrencyConfig cc = TransientConfig.CURRENCY_CONFIGS.get(bribe.getItem());
        if (cc.bribeCredit <= 0) {
            if (Config.verbose) {
                player.displayClientMessage(Component.literal("This currency is not enabled for bribing."), true);
            }
            return false;
        }

        if (!ec.bribable()) {
            if (Config.verbose) {
                player.displayClientMessage(Component.literal("Entity is not bribable."), true);
            }
            return false;
        }

        BribeData state = BriberyState.getOrCreateBribeData(entity.getUUID(), player.getUUID());
        float credit = cc.bribeCredit * bribe.getCount();
        state.largestBribe = (int) Math.ceil(Math.max(state.largestBribe, credit));
        long gameTime = BriberyUtil.overworldGameTime(BriberyUtil.getEntityServer(entity));

        if (state.isExtorting) {
            extort(entity, player, state, credit);
            return false;
        } else if (state.isCoolingDown) {
            if (state.isBribed) {
                long delta = gameTime - state.bribedAt;
                if (delta >= 0 && delta < Config.acceptedCooldownSeconds * 20L) {
                    if (Config.verbose) {
                        player.displayClientMessage(
                                Component.literal("This entity cannot accept bribes right now."), true
                        );
                    }
                    return false;
                }
            }
            if (state.isRejected) {
                long delta = gameTime - state.rejectedAt;
                if (delta >= 0 && delta < Config.rejectedCooldownSeconds * 20L) {
                    if (Config.rejectedCooldownAllowAttempts) {
                        reject(entity, player, state, credit);
                        return false;
                    }
                    if (Config.verbose) {
                        player.displayClientMessage(
                                Component.literal("This entity cannot accept bribes right now."), true
                        );
                    }
                }
                state.isRejected = false;
            }
            state.isCoolingDown = false;
        } else {
            state.isRejected = false;
        }

        if (Config.bribeExpiryMinutes > 0) {
            long delta = gameTime - state.bribedAt;
            if (delta < 0 || delta >= Config.bribeExpiryMinutes * 1200L) {
                state.isBribed = false;
            }
        }

        Random random = new Random();
        if (!state.isExtortionist) {
            float rejectionChance = Math.max(
                    ec.rejectionChance() - (cc.rejectionChanceModifier * bribe.getCount()),
                    ec.minimumRejectionChance()
            );
            if (state.isBribed) {
                rejectionChance *= Config.alreadyBribedMultiplier;
            }
            if (entity instanceof NeutralMob mob) {
                if (mob.isAngry()) {
                    rejectionChance += Config.aggroRejectionModifier;
                }
            }
            if (random.nextFloat() < (rejectionChance / 100f)) {
                reject(entity, player, state, credit);
                return false;
            }
        }
        accept(entity, player, state, credit, random);

        return false;
    }

    public static void accept(LivingEntity entity, ServerPlayer player, BribeData state, float credit, Random random) {
        long gameTime = BriberyUtil.overworldGameTime(BriberyUtil.getEntityServer(entity));
        if (!state.isExtortionist) {
            if (random.nextFloat() < (Config.extortionistChance / 100f)) {
                state.isExtortionist = true;
                state.extortedAt = gameTime;
            }
        }
        state.isBribed = true;
        state.isCoolingDown = true;
        state.bribedAt = gameTime;
        state.bribeCredits += (int) Math.ceil(credit);
        if (Config.bribeXpMultiplier > 0) {
            int totalXp = (int) Math.ceil(credit * Config.bribeXpMultiplier);
            entity.level().addFreshEntity(
                    new ExperienceOrb(entity.level(), entity.getX(), entity.getY(), entity.getZ(), totalXp)
            );
        }
        if (entity instanceof NeutralMob mob) {
            if (mob.isAngry()) {
                mob.setTarget(null);
                mob.setPersistentAngerTarget(null);
                mob.setRemainingPersistentAngerTime(0);
            }
        }
        if (entity instanceof Villager villager) {
            villager.playSound(SoundEvents.VILLAGER_YES);
            ((ServerLevel) villager.level()).sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    villager.getX(), villager.getY() + 1.0, villager.getZ(),
                    5, 0.5, 0.5, 0.5, 0
            );
            villager.getGossips().remove(player.getUUID(), GossipType.MAJOR_NEGATIVE);
            villager.getGossips().remove(player.getUUID(), GossipType.MINOR_NEGATIVE);
            if (Config.acceptedGossipMultiplier > 0) {
                villager.getGossips()
                        .add(
                                player.getUUID(),
                                GossipType.TRADING,
                                (int) (credit * Config.acceptedGossipMultiplier)
                        );
            }
        }
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }

    public static void reject(LivingEntity entity, ServerPlayer player, BribeData state, float credit) {
        state.isExtortionist = false;
        state.isExtorting = false;
        state.isBribed = false;
        state.isCoolingDown = true;
        state.isRejected = true;
        state.rejectedAt = BriberyUtil.overworldGameTime(BriberyUtil.getEntityServer(entity));
        if (entity instanceof Villager villager) {
            villager.playSound(SoundEvents.VILLAGER_NO);
            ((ServerLevel) villager.level()).sendParticles(
                    ParticleTypes.ANGRY_VILLAGER,
                    villager.getX(), villager.getY() + 1.0, villager.getZ(),
                    5, 0.5, 0.5, 0.5, 0
            );
            if (Config.rejectedGossipMultiplier > 0) {
                villager.getGossips()
                        .add(
                                player.getUUID(),
                                GossipType.MAJOR_NEGATIVE,
                                (int) (credit * Config.rejectedGossipMultiplier)
                        );
                villager.setLastHurtByMob(player);
            }
        }
        if (entity instanceof NeutralMob mob) {
            BriberyUtil.makeMobAngry(mob, player);
        }
    }

    public static void extort(LivingEntity entity, ServerPlayer player, BribeData state, float credit) {
        state.extortionBalance = Math.max(
                0,
                state.extortionBalance - (int) Math.ceil(credit * Config.extortionPriceMultiplier)
        );
        if (state.extortionBalance > 0) {
            player.displayClientMessage(
                    Component.empty().append(entity.getDisplayName()).append(" is demanding more payment."),
                    true
            );
        } else {
            player.displayClientMessage(
                    Component.empty().append(entity.getDisplayName()).append(" is satisfied... for now."),
                    true
            );
            state.isExtorting = false;
            long gameTime = BriberyUtil.overworldGameTime(BriberyUtil.getEntityServer(entity));
            state.extortedAt = gameTime;
            state.bribedAt = gameTime;
        }
    }

    public static void cancel(LivingEntity entity, ServerPlayer player) {
        BribeData state = BriberyState.getBribeData(entity.getUUID(), player.getUUID());
        if (state == null) return;
        if (state.isRejected) return;
        if (state.isExtorting) {
            reject(entity, player, state, state.largestBribe * Config.extortionPriceMultiplier);
            return;
        }
        BriberyState.bribeStates.get(entity.getUUID()).remove(player.getUUID());
    }

    public static void witnessBribe(LivingEntity entity, ServerPlayer player) {
        if (Config.witnessBribeRange <= 0) return;
        entity.level().getEntitiesOfClass(
                LivingEntity.class,
                entity.getBoundingBox().inflate(Config.witnessBribeRange),
                e -> e != entity && BriberyUtil.isBribable(e) && e.hasLineOfSight(player)
        ).forEach(witness -> {
            BribeData state = BriberyState.getBribeData(witness.getUUID(), player.getUUID());
            if (state == null || !state.isBribed) {
                if (BriberyUtil.inFOV(witness, player, Config.witnessBribeFovDegrees)) {
                    if (witness instanceof Villager villager) {
                        villager.getGossips().add(player.getUUID(), GossipType.MAJOR_POSITIVE, 25);
                    }
                    if (witness instanceof NeutralMob mob) {
                        BriberyUtil.makeMobAngry(mob, player);
                    }
                }
            }
        });
    }

}
