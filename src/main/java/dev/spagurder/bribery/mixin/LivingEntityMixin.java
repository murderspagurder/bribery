package dev.spagurder.bribery.mixin;

import dev.spagurder.bribery.config.Config;
import dev.spagurder.bribery.core.BribeHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    //? <=1.21.1 {
    /*@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void beforeHurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
    *///?} else {
    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    public void beforeHurt(
            ServerLevel serverLevel, DamageSource damageSource,
            float f, CallbackInfoReturnable<Boolean> cir) {
    //?}
        if (damageSource.getEntity() instanceof LivingEntity damagingEntity) {
            if (damagingEntity instanceof ServerPlayer player) {
                LivingEntity entity = (LivingEntity)(Object) this;
                if (player.isShiftKeyDown()) {
                    ItemStack bribe = player.getWeaponItem();
                    if (Config.CURRENCY_CONFIGS.containsKey(bribe.getItem())) {
                        cir.setReturnValue(
                                BribeHandler.handle(entity, player, bribe)
                        );
                        return;
                    }
                }
                BribeHandler.cancel(entity, player);
            }
        }
    }

}
