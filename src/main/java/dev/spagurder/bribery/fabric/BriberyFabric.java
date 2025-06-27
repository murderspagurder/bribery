package dev.spagurder.bribery.fabric;

//? fabric {
import dev.spagurder.bribery.Bribery;
import dev.spagurder.bribery.core.TickHandler;
import dev.spagurder.bribery.state.BriberyState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class BriberyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Bribery.init();

        ServerLifecycleEvents.SERVER_STARTED.register(BriberyState::load);

        ServerLifecycleEvents.SERVER_STOPPED.register((server) -> {
            BriberyState.save();
            BriberyState.unload();
        });

        ServerTickEvents.END_SERVER_TICK.register(TickHandler::onTick);
    }

}
//?}