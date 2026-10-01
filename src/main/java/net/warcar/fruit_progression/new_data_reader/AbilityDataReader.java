package net.warcar.fruit_progression.new_data_reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.warcar.fruit_progression.DevilFruitProgressionMod;

import java.util.HashMap;
import java.util.Map;

public class AbilityDataReader<T> extends SimpleJsonResourceReloadListener {
    public final Map<ResourceLocation, T> map = new HashMap<>();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final String name;
    private final Provider<T> provider;

    public AbilityDataReader(String name, Provider<T> provider) {
        super(GSON, name);
        this.name = name;
        this.provider = provider;
    }

    protected void apply(Map<ResourceLocation, JsonElement> elementMap, ResourceManager manager, ProfilerFiller profiler) {
        DevilFruitProgressionMod.LOGGER.info("Started Deserialization of {} data", this.name);
        map.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : elementMap.entrySet()) {
            if (entry.getKey().getPath().startsWith("_")) continue;
            map.put(entry.getKey(), provider.get(entry.getValue(), entry.getKey()));
        }
        DevilFruitProgressionMod.LOGGER.info("Ended Deserialization of {} data", name);
    }

    @FunctionalInterface
    public interface Provider<T> {
        T get(JsonElement jsonElement, ResourceLocation location);
    }
}
