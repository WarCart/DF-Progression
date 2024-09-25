package net.warcar.fruit_progression.init;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.requirements.Requirement;
import net.warcar.fruit_progression.requirements.RequirementInstance;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModConfig {
    public static final ModConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;
    public final ForgeConfigSpec.ConfigValue<List<Config>> abilities;
    public final ForgeConfigSpec.ConfigValue<List<Config>> fruits;
    private final Map<ForgeConfigSpec.ConfigValue<List<Config>>, Map<String, RequirementSetInstance>> preBakedMaps = new HashMap<>();

    public ModConfig(ForgeConfigSpec.Builder builder) {
        this.abilities = builder.comment("List of abilities and their requirements").define("Unlocks.Ability", getDefaultValueAbilities());
        this.fruits = builder.comment("List of fruits and their requirements for awakenings").define("Awakenings.Fruit", getDefaultValueFruits());
    }

    public Map<String, RequirementSetInstance> getAbilities() {
        return this.preBakedMaps.computeIfAbsent(this.abilities, this::get);
    }

    public Map<String, RequirementSetInstance> getAwakenings() {
        return this.preBakedMaps.computeIfAbsent(this.fruits, this::get);
    }

    private Map<String, RequirementSetInstance> get(ForgeConfigSpec.ConfigValue<List<Config>> abilities) {
        Map<String, RequirementSetInstance> out = new HashMap<>();
        for (Config config : abilities.get()) {
            String partName = config.get("name");
            List<List<RequirementInstance>> outerList = new ArrayList<>();
            for (List<String> strings : (List<List<String>>) config.get("requirements")) {
                List<RequirementInstance> innerList = new ArrayList<>();
                for (String string : strings) {
                    String name = string.split("-")[0];
                    boolean debug;
                    if (name.startsWith("D")) {
                        debug = true;
                        name = name.replace("D", "");
                    } else {
                        debug = false;
                    }
                    boolean inverted;
                    if (name.startsWith("!")) {
                        inverted = true;
                        name = name.replace("!", "");
                    } else {
                        inverted = false;
                    }
                    RequirementInstance requirement;
                    try {
                        requirement = new RequirementInstance(GameRegistry.findRegistry(Requirement.class).getValue(new ResourceLocation(name)));
                        requirement.setInverted(inverted);
                        requirement.setDebug(debug);
                        if (string.split("-").length > 1) {
                            requirement.setValues(string.split("-")[1].split(";"));
                        }
                        innerList.add(requirement);
                    } catch (ResourceLocationException e) {
                        e.printStackTrace();
                        DevilFruitProgressionMod.LOGGER.info(name);
                    }
                }
                outerList.add(innerList);
            }
            RequirementSetInstance instance = new RequirementSetInstance(outerList, partName);
            out.put(partName, instance);
        }
        return out;
    }

    private static List<Config> getDefaultValueAbilities() {
        List<Config> defVal = new ArrayList<>();
        List<List<String>> gomuReqs = new ArrayList<>();
        List<String> innerGomuReqs = new ArrayList<>();
        innerGomuReqs.add("ability_progression:awakening");
        innerGomuReqs.add("ability_progression:devil_fruit-mineminenomi:gomu_gomu_no_mi");
        gomuReqs.add(innerGomuReqs);
        defVal.add(Config.of(() -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "mineminenomi:example1");
            List<List<String>> reqs = new ArrayList<>();
            List<String> innerReqs1 = new ArrayList<>();
            innerReqs1.add("ability_progression:race-human");
            innerReqs1.add("ability_progression:doriki-1000");
            reqs.add(innerReqs1);
            List<String> innerReqs2 = new ArrayList<>();
            innerReqs2.add("ability_progression:haki-50;BUSOSHOKU");
            reqs.add(innerReqs2);
            map.put("requirements", reqs);
            return map;
        }, TomlFormat.instance()));
        defVal.add(Config.of(() -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "mineminenomi:gear_fifth");
            map.put("requirements", gomuReqs);
            return map;
        }, TomlFormat.instance()));
        defVal.add(Config.of(() -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "mineminenomi:gomu_gomu_no_gigant");
            map.put("requirements", gomuReqs);
            return map;
        }, TomlFormat.instance()));
        defVal.add(Config.of(() -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "mineminenomi:gomu_gomu_no_dawn_whip");
            map.put("requirements", gomuReqs);
            return map;
        }, TomlFormat.instance()));
        return defVal;
    }

    private static List<Config> getDefaultValueFruits() {
        List<Config> defVal = new ArrayList<>();
        defVal.add(Config.of(() -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "mineminenomi:example_fruit");
            List<List<String>> reqs = new ArrayList<>();
            List<String> innerReqs = new ArrayList<>();
            innerReqs.add("ability_progression:haoshoku_born");
            innerReqs.add("ability_progression:doriki-1000");
            reqs.add(innerReqs);
            map.put("requirements", reqs);
            return map;
        }, TomlFormat.instance()));
        return defVal;
    }

    static {
        Pair<ModConfig, ForgeConfigSpec> pair = (new ForgeConfigSpec.Builder()).configure(ModConfig::new);
        SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }
}
