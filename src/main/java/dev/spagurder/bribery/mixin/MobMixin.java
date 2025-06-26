package dev.spagurder.bribery.mixin;

import dev.spagurder.bribery.state.BribeData;
import dev.spagurder.bribery.state.BriberyState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    public void setTarget(LivingEntity entity, CallbackInfo ci) {
        Mob mob = (Mob)(Object)this;
        if (!(entity instanceof ServerPlayer player)) return;
        if (!(mob instanceof NeutralMob)) return;

        BribeData state = BriberyState.getBribeData(mob.getUUID(), player.getUUID());
        if (state != null && state.isBribed) {
            ci.cancel();
        }
    }

}
