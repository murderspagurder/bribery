package dev.spagurder.bribery;

import dev.spagurder.bribery.config.Config;
import eu.midnightdust.lib.config.MidnightConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bribery {

    public static final String MOD_ID = "bribery";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing Bribery");
        MidnightConfig.init(MOD_ID, Config.class);
        Config.refreshCurrencyItems();
    }

}
