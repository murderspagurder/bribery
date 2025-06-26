package dev.spagurder.bribery.mixin;

import dev.spagurder.bribery.state.BriberyState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class WorldSaveMixin {

    @Inject(method = "save", at = @At("HEAD"))
    private void onSave(ProgressListener progressListener, boolean flush, boolean skipSave, CallbackInfo ci) {
        if (!skipSave) {
            BriberyState.save();
        }
    }

}
