package dev.spagurder.bribery.fabric;

//? fabric {
import dev.spagurder.bribery.Bribery;
import dev.spagurder.bribery.client.KeyMappings;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class BriberyFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(KeyMappings.CONFIG_SCREEN);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KeyMappings.CONFIG_SCREEN.consumeClick()) {
                client.setScreen(
                        MidnightConfig.getScreen(client.screen, Bribery.MOD_ID)
                );
            }
        });
    }

}
//?}