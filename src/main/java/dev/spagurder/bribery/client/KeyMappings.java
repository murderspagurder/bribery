package dev.spagurder.bribery.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.spagurder.bribery.Bribery;
import net.minecraft.client.KeyMapping;

public class KeyMappings {

    public static final KeyMapping CONFIG_SCREEN = new KeyMapping(
            "key." + Bribery.MOD_ID + ".openConfig",
            InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(),
            "category." + Bribery.MOD_ID + ".keybinds"
    );

}