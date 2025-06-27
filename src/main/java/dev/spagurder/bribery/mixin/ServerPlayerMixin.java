package dev.spagurder.bribery.mixin;

import dev.spagurder.bribery.state.BribeData;
import dev.spagurder.bribery.state.BriberyState;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "die", at = @At("HEAD"))
    public void beforeDie(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;
        //? >1.21.5 {
        if (!player.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) return;
        //?} else {
        /*if (!player.serverLevel().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) return;
        *///?}
        if (!(damageSource.getEntity() instanceof LivingEntity entity)) return;
        BribeData state = BriberyState.getBribeData(entity.getUUID(), player.getUUID());
        if (state == null) return;
        if (state.isRejected) {
            player.getServer().getPlayerList().broadcastSystemMessage(
                    Component.empty()
                            .append(player.getDisplayName())
                            .append(" tried to bribe ")
                            .append(entity.getDisplayName()),
                    false
            );
        }
    }

}
