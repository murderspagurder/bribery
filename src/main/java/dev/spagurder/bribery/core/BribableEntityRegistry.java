package dev.spagurder.bribery.core;

import dev.spagurder.bribery.Bribery;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class BribableEntityRegistry {

    public static final List<Class<? extends LivingEntity>> BRIBABLE_ENTITIES = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public static void tryLoad(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (LivingEntity.class.isAssignableFrom(clazz)) {
                BribableEntityRegistry.BRIBABLE_ENTITIES.add((Class<? extends LivingEntity>) clazz);
                Bribery.LOGGER.info("Loading {}", className);
            }
        } catch (ClassNotFoundException ignored) {}
    }

}
