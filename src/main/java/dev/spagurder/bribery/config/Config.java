package dev.spagurder.bribery.config;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class Config extends MidnightConfig {

    @Entry(min = 0) public static float ironBribeCredit = 1.0f;
    @Entry(min = 0, max = 100) public static float ironRejectionChanceModifier = 0.0f;

    @Entry(min = 0) public static float goldBribeCredit = 2.0f;
    @Entry(min = 0, max = 100) public static float goldRejectionChanceModifier = 0.025f;

    @Entry(min = 0) public static float emeraldBribeCredit = 3.0f;
    @Entry(min = 0, max = 100) public static float emeraldRejectionChanceModifier = 0.1f;

    @Entry(min = 0) public static float diamondBribeCredit = 20.0f;
    @Entry(min = 0, max = 100) public static float diamondRejectionChanceModifier = 0.4f;

    @Entry(min = 0) public static float netheriteBribeCredit = 50.0f;
    @Entry(min = 0, max = 100) public static float netheriteRejectionChanceModifier = 1.0f;

    @Entry public static boolean bribableVillagers = true;
    @Entry(min = 0, max = 100) public static float villagerRejectionChance = 30.0f;
    @Entry(min = 0, max = 100) public static float minimumVillagerRejectionChance = 1.0f;

    @Entry public static boolean bribableGolems = true;
    @Entry(min = 0, max = 100) public static float golemRejectionChance = 80.0f;
    @Entry(min = 0, max = 100) public static float minimumGolemRejectionChance = 10.0f;

    @Entry(min = 0) public static int acceptedCooldownSeconds = 300;
    @Entry(min = 0) public static float acceptedGossipMultiplier = 0.25f;
    @Entry(min = 0) public static int rejectedCooldownSeconds = 300;
    @Entry public static boolean rejectedCooldownAllowAttempts = true;
    @Entry(min = 0) public static float rejectedGossipMultiplier = 1.0f;

    @Entry(min = 0, max = 1) public static float alreadyBribedMultiplier = 0.1f;
    @Entry(min = 0, max = 100) public static float extortionistChance = 5.0f;
    @Entry(min = 0) public static int bribeExpiryMinutes = 120;
    @Entry(min = 0) public static int extortionTimeMinutes = 120;
    @Entry(min = 1) public static int extortionDeadlineMinutes = 20;
    @Entry(min = 0.1f, max = 1) public static float extortionPriceMultiplier = 0.5f;
    @Entry public static boolean extortionRequiresLineOfSight = true;
    @Entry(min = 1) public static double extortionDetectionRangeSqr = 64;
    @Entry(min = 1) public static int hardExpiryDays = 15;

    @Entry(min = 0) public static float bribeXpMultiplier = 0.25f;

    @Entry public static boolean verbose = false;

    public static final Map<Item, CurrencyConfig> CURRENCY_CONFIGS = new HashMap<>();

    public static void refreshCurrencyItems() {
        CURRENCY_CONFIGS.clear();
        CURRENCY_CONFIGS.put(Items.IRON_INGOT, new CurrencyConfig(ironBribeCredit, ironRejectionChanceModifier));
        CURRENCY_CONFIGS.put(Items.GOLD_INGOT, new CurrencyConfig(goldBribeCredit, goldRejectionChanceModifier));
        CURRENCY_CONFIGS.put(Items.EMERALD, new CurrencyConfig(emeraldBribeCredit, emeraldRejectionChanceModifier));
        CURRENCY_CONFIGS.put(Items.DIAMOND, new CurrencyConfig(diamondBribeCredit, diamondRejectionChanceModifier));
        CURRENCY_CONFIGS.put(Items.NETHERITE_INGOT, new CurrencyConfig(netheriteBribeCredit, netheriteRejectionChanceModifier));
    }

}
