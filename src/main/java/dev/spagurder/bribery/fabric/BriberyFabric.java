package dev.spagurder.bribery.fabric;

//? fabric {
import dev.spagurder.bribery.Bribery;
import dev.spagurder.bribery.core.TickHandler;
import dev.spagurder.bribery.state.BriberyState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class BriberyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Bribery.init();

        ServerWorldEvents.LOAD.register((server, world) -> {
            BriberyState.load(world);
        });

        ServerWorldEvents.UNLOAD.register((server, world) -> {
            BriberyState.save();
            BriberyState.unload();
        });

        ServerTickEvents.END_WORLD_TICK.register(TickHandler::onTick);
    }

}
//?}