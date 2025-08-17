package net.warcar.fruit_progression.new_data_reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class AbilityDataReader<T> extends JsonReloadListener {
    public final Map<ResourceLocation, T> map = new HashMap<>();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final String name;
    private final Provider<T> provider;

    public AbilityDataReader(String name, Provider<T> provider) {
        super(GSON, name);
        this.name = name;
        this.provider = provider;
    }

    protected void apply(Map<ResourceLocation, JsonElement> elementMap, IResourceManager manager, IProfiler profiler) {
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
