package dev.spagurder.bribery.mixin.compat;

import dev.spagurder.bribery.Bribery;
import dev.spagurder.bribery.state.BribeData;
import dev.spagurder.bribery.state.BriberyState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "io.github.mortuusars.thief.world.Witness")
public class ThiefCompatWitnessMixin {

    @Inject(method = "isWitness", at = @At("HEAD"), cancellable = true)
    private static void beforeIsWitness(
            LivingEntity criminal, LivingEntity entity,
            double visibility, CallbackInfoReturnable<Boolean> cir) {
        if (criminal instanceof ServerPlayer player) {
            BribeData state = BriberyState.getBribeData(entity.getUUID(), player.getUUID());
            if (state != null && state.isBribed) {
                Bribery.LOGGER.info("Blocking Witness");
                cir.setReturnValue(false);
            }
        }
    }

}
