package dev.spagurder.bribery.mixin;

import dev.spagurder.bribery.Bribery;
import dev.spagurder.bribery.config.Config;
import eu.midnightdust.lib.config.MidnightConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MidnightConfig.class, remap = false)
public class MidnightConfigMixin {

    //? if >=1.20.2 {
    @Inject(method = "writeChanges", at = @At("RETURN"))
    private void writeChanges(String modid, CallbackInfo ci) {
    //?} else {
    /*@Inject(method = "write", at = @At("RETURN"))
    private static void write(String modid, CallbackInfo ci) {
    *///?}
        if (modid.equals(Bribery.MOD_ID)) {
            Config.refreshCurrencyItems();
        }
    }

}
