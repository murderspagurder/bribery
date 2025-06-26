package dev.spagurder.bribery.config;

public class EntityConfig {

    public static final String VILLAGER = "villager";
    public static final String GOLEM = "golem";

    private final String type;

    public EntityConfig(String type) {
        this.type = type;
    }

    public boolean bribable() {
        return switch (type) {
            case VILLAGER -> Config.bribableVillagers;
            case GOLEM -> Config.bribableGolems;
            default -> false;
        };
    }

    public float rejectionChance() {
        return switch (type) {
            case VILLAGER -> Config.villagerRejectionChance;
            case GOLEM -> Config.golemRejectionChance;
            default -> 0;
        };
    }

    public float minimumRejectionChance() {
        return switch (type) {
            case VILLAGER -> Config.minimumVillagerRejectionChance;
            case GOLEM -> Config.minimumGolemRejectionChance;
            default -> 0;
        };
    }

}
