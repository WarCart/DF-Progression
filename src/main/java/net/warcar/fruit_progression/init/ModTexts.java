package net.warcar.fruit_progression.init;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import xyz.pixelatedw.mineminenomi.init.ModRegistry.I18nCategory;

import java.util.HashMap;
import java.util.Map;


public class ModTexts {
    private static final Map<String, String> LANG_MAP = new HashMap<>();

    public static final String DEVIL_FRUIT_POINTS_CHECK = registerName(I18nCategory.ABILITY_NODE, "devil_fruit_points_check", "Devil Fruit Points: %d");
    public static final String NEEDS_ABILITY = registerName(I18nCategory.GUI, "needs_ability_unlocked", "Needs unlocked ability: %s");
    public static final String NEEDS_AWAKENING = registerName(I18nCategory.GUI, "needs_awakening", "Needs awakened devil fruit");
    public static final String NEEDS_DF_MASTERY = registerName(I18nCategory.GUI, "needs_df_mastery", "Needs devil fruit mastery: %d");
    public static final String NEEDS_DF = registerName(I18nCategory.GUI, "needs_df", "Needs devil fruit: %s");
    public static final String NEEDS_ANY_DF = registerName(I18nCategory.GUI, "needs_any_df", "Needs devil fruit");
    public static final String NEEDS_TOTAL_HAKIXP = registerName(I18nCategory.GUI, "needs_hakixp.total", "Needs total Haki XP: %d");
    public static final String NEEDS_KENB_HAKIXP = registerName(I18nCategory.GUI, "needs_hakixp.kenb", "Needs kenbunshoku Haki XP: %d");
    public static final String NEEDS_BUSO_HAKIXP = registerName(I18nCategory.GUI, "needs_hakixp.buso", "Needs busoshoku Haki XP: %d");
    public static final String NEEDS_HAO_BORN = registerName(I18nCategory.GUI, "needs_haoshoku", "Needs to be able to develop Haoshoku haki");
    public static final String NEEDS_LOYALTY = registerName(I18nCategory.GUI, "needs_loyalty", "Needs loyalty: %d");
    public static final String NEEDS_QUEST = registerName(I18nCategory.GUI, "needs_finished_quest", "Needs finished quest: %s");
    public static final String NEEDS_USED_ABILITY = registerName(I18nCategory.GUI, "needs_used_ability", "Needs to use %s %d times");
    public static final String ALREADY_READ = registerName(I18nCategory.GUI, "already_read", "You already know about %s");
    public static final String SAVE = registerName(I18nCategory.GUI, "save", "Save progress");

    public static void init() {/*
        Map<String, String> sorted = WyHelper.sortAlphabetically(LANG_MAP);
        Set<Map.Entry<String, String>> set = sorted.entrySet();
        Iterator<Map.Entry<String, String>> iter = set.iterator();

        Map.Entry<String, String> prevEntry = null;

        File langFolder = new File("assets/" + DevilFruitProgressionMod.MOD_ID + "/lang/");
        langFolder.mkdirs();

        if (langFolder.exists()) {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("assets/" +  DevilFruitProgressionMod.MOD_ID + "/lang/en_us.json"), "UTF-8"))) {
                writer.write("{\n");
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();

                    if (prevEntry != null) {
                        if (!prevEntry.getKey().substring(0, 2).equals(entry.getKey().substring(0, 2))) {
                            writer.write("\n");
                        }
                    }

                    String value = WyHelper.escapeJSON(entry.getValue());
                    if (iter.hasNext()) {
                        writer.write("\t\"" + entry.getKey() + "\": \"" + value + "\",\n");
                    } else {
                        writer.write("\t\"" + entry.getKey() + "\": \"" + value + "\"\n");
                    }

                    prevEntry = entry;
                }
                writer.write("}\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public static String registerName(I18nCategory category, String key, String localizedName) {
        key = category.getId() + ".ability_progression." + key;
        registerName(key, localizedName);
        return key;
    }

    private static String registerName(String key, String localizedName) {
        LANG_MAP.put(key, localizedName);
        return key;
    }

    public static Component registerObjectiveTitle(ResourceLocation questId, int objectiveId, String title, Object... args) {
        String id = registerName(questId + ".objective." + objectiveId, title);
        return Component.translatable(id, args);
    }
}
