package dev.spagurder.bribery;

import dev.spagurder.bribery.config.Config;
import dev.spagurder.bribery.core.BribableEntityRegistry;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bribery {

    public static final String MOD_ID = "bribery";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing Bribery");
        MidnightConfig.init(MOD_ID, Config.class);
        Config.refreshCurrencyItems();
        loadEntities();
    }

    private static void loadEntities() {
        BribableEntityRegistry.BRIBABLE_ENTITIES.add(Villager.class);
        BribableEntityRegistry.BRIBABLE_ENTITIES.add(IronGolem.class);
        BribableEntityRegistry.tryLoad("tallestegg.guardvillagers.common.entities.Guard");
        BribableEntityRegistry.tryLoad("tallestegg.guardvillagers.entities.Guard");
        BribableEntityRegistry.tryLoad("dev.sterner.guardvillagers.common.entity.GuardEntity");
    }

}
