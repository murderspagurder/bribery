package dev.spagurder.bribery.neoforge;

//? neoforge {

/*import dev.spagurder.bribery.Bribery;
import dev.spagurder.bribery.client.KeyMappings;
import dev.spagurder.bribery.core.TickHandler;
import dev.spagurder.bribery.state.BriberyState;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@Mod(Bribery.MOD_ID)
public class BriberyNeoforge {

    @SuppressWarnings("unused")
    public BriberyNeoforge(IEventBus modBus, ModContainer modContainer) {
        Bribery.init();

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level) {
            BriberyState.load(level);
        }
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel) {
            BriberyState.save();
            BriberyState.unload();
        }
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        TickHandler.onTick(event.getServer());
    }

    @EventBusSubscriber(modid = Bribery.MOD_ID, value = Dist.CLIENT)
    public static class ClientGameEvents {

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KeyMappings.CONFIG_SCREEN);
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            processConfigKey();
        }

        private static void processConfigKey() {
            while (KeyMappings.CONFIG_SCREEN.consumeClick()) {
                Minecraft client = Minecraft.getInstance();
                if (client.player == null || client.level == null) return;
                client.setScreen(
                        MidnightConfig.getScreen(client.screen, Bribery.MOD_ID)
                );
            }
        }

    }

}
*///?}