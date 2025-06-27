package dev.spagurder.bribery.state;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import dev.spagurder.bribery.Bribery;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BriberyState {

    public static Map<UUID, Map<UUID, BribeData>> bribeStates;
    private static Path SAVE_FILE;
    private static final Gson GSON = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create();

    public static BribeData getOrCreateBribeData(UUID entityUUID, UUID playerUUID) {
        return bribeStates.computeIfAbsent(entityUUID, k -> new HashMap<>())
                .computeIfAbsent(playerUUID, k -> new BribeData());
    }

    @Nullable
    public static BribeData getBribeData(UUID entityUUID, UUID playerUUID) {
        Map<UUID, BribeData> entityData = bribeStates.get(entityUUID);
        return entityData != null ? entityData.get(playerUUID) : null;
    }

    public static void load(MinecraftServer server) {
        Path worldPath = server.getWorldPath(LevelResource.ROOT);
        SAVE_FILE = worldPath.resolve(Bribery.MOD_ID).resolve("state.json");
        if (!Files.exists(SAVE_FILE)) {
            bribeStates = new HashMap<>();
            return;
        }
        try (Reader reader = Files.newBufferedReader(SAVE_FILE)) {
            Type type = new TypeToken<Map<UUID, Map<UUID, BribeData>>>() {}.getType();
            bribeStates = GSON.fromJson(reader, type);
            if (bribeStates != null) return;
        } catch (JsonParseException e) {
            Bribery.LOGGER.error("Invalid JSON data in bribe state file: {}", e.getMessage());
        } catch (IOException e) {
            Bribery.LOGGER.error("Error loading bribe states: {}", e.getMessage());
        }
        bribeStates = new HashMap<>();
    }

    public static void save() {
        if (bribeStates == null) return;
        try {
            Files.createDirectories(SAVE_FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(SAVE_FILE)) {
                GSON.toJson(bribeStates, writer);
            }
        } catch (IOException e) {
            Bribery.LOGGER.error("Error saving bribe states: {}", e.getMessage());
        }
    }

    public static void unload() {
        SAVE_FILE = null;
        bribeStates = null;
    }

}
