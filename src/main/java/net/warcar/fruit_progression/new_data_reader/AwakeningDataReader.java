package net.warcar.fruit_progression.new_data_reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.warcar.fruit_progression.init.ModRegistry;
import net.warcar.fruit_progression.requirements.RequirementInstance;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwakeningDataReader extends JsonReloadListener {
    public static final Map<ResourceLocation, RequirementSetInstance> map = new HashMap<>();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    public AwakeningDataReader() {
        super(GSON, "awakenings");
    }

    protected void apply(Map<ResourceLocation, JsonElement> elementMap, IResourceManager manager, IProfiler profiler) {
        LOGGER.info("Started Deserialization of awakenings data");
        map.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : elementMap.entrySet()) {
            if (entry.getKey().getPath().startsWith("_")) continue;
            for (JsonElement jsonArr : entry.getValue().getAsJsonObject().get("requirements").getAsJsonArray()) {
                List<List<RequirementInstance>> list = new ArrayList<>();
                for (JsonElement json : jsonArr.getAsJsonArray()) {
                    List<RequirementInstance> innerList = new ArrayList<>();
                    try {
                        JsonObject object = json.getAsJsonObject();
                        String name = object.get("name").getAsString();
                        innerList.add(ModRegistry.REQUIREMENTS.getValue(new ResourceLocation(name)).deserializeInstance(object.get("args").getAsJsonObject()));
                    } catch (Exception e) {
                        LOGGER.warn("Error while trying to process ability data {}", entry.getKey());
                        if (e instanceof NullPointerException) {
                            LOGGER.warn("'{}' requirement doesn't exist", json.getAsJsonObject().get("name").getAsString());
                        } else {
                            e.printStackTrace();
                        }
                    }
                    list.add(innerList);
                }
                map.put(entry.getKey(), new RequirementSetInstance(list, entry.getKey().toString()));
            }
        }
    }
}
