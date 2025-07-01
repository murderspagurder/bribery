package dev.spagurder.bribery.neoforge;

//? neoforge {

/*import dev.spagurder.bribery.Bribery;
import dev.spagurder.bribery.client.KeyMappings;
import dev.spagurder.bribery.core.TickHandler;
import dev.spagurder.bribery.state.BriberyState;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

//? >1.20.4 {
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
//?} else {
/^import net.neoforged.neoforge.event.TickEvent;
^///?}

//? >1.20.4 {
@Mod(Bribery.MOD_ID)
@EventBusSubscriber
//?} else {
/^@Mod.EventBusSubscriber(modid = Bribery.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
^///?}
public class BriberyNeoforge {

    @SuppressWarnings("unused")
    public BriberyNeoforge(IEventBus modBus, ModContainer modContainer) {
        Bribery.init();
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        BriberyState.load(event.getServer());
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        BriberyState.save();
        BriberyState.unload();
    }

    @SubscribeEvent
    public static void onServerTick(
            //? >1.20.4 {
            ServerTickEvent.Post event
            //?} else {
            /^TickEvent.ServerTickEvent event
            ^///?}
    ) {
        //? <=1.20.4 {
        /^if (event.phase == TickEvent.Phase.END)
        ^///?}
            TickHandler.onTick(event.getServer());
    }

    //? >=1.21.1 {
    @EventBusSubscriber(modid = Bribery.MOD_ID, value = Dist.CLIENT)
    //?} else if >1.20.4 {
    /^@EventBusSubscriber(modid = Bribery.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    ^///?} else {
    /^@Mod.EventBusSubscriber(modid = Bribery.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    ^///?}
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KeyMappings.CONFIG_SCREEN);
        }
    }

    //? >=1.21.1 {
    @EventBusSubscriber(modid = Bribery.MOD_ID, value = Dist.CLIENT)
    //?} else if >1.20.4 {
    /^@EventBusSubscriber(modid = Bribery.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    ^///?} else {
    /^@Mod.EventBusSubscriber(modid = Bribery.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    ^///?}
    public static class ClientGameEvents {
        @SubscribeEvent
        public static void onClientTick(
                //? >1.20.4 {
                ClientTickEvent.Post event
                //?} else {
                /^TickEvent.ClientTickEvent event
                ^///?}
        ) {
            //? <=1.20.4 {
            /^if  (event.phase == TickEvent.Phase.END)
            ^///?}
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