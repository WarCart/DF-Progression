package net.warcar.fruit_progression.init;

import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.init.ModRegistry.I18nCategory;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class ModTexts {
    private static final Map<String, String> LANG_MAP = new HashMap<>();

    public static final String DEVIL_FRUIT_POINTS_CHECK = registerName(I18nCategory.ABILITY_NODE, "devil_fruit_points_check", "Devil Fruit Points: %d");
    public static final String NEEDS_ABILITY = registerName(I18nCategory.GUI, "needs_ability_unlocked", "Needs unlocked ability: %s");
    public static final String NEEDS_AWAKENING = registerName(I18nCategory.GUI, "needs_awakening", "Needs awakened devil fruit");
    public static final String NEEDS_DF_MASTERY = registerName(I18nCategory.GUI, "needs_df_mastery", "Needs devil fruit mastery: %d");
    public static final String NEEDS_DF = registerName(I18nCategory.GUI, "needs_df", "Needs devil fruit: %s");
    public static final String NEEDS_TOTAL_HAKIXP = registerName(I18nCategory.GUI, "needs_hakixp.total", "Needs total Haki XP: %d");
    public static final String NEEDS_KENB_HAKIXP = registerName(I18nCategory.GUI, "needs_hakixp.kenb", "Needs kenbunshoku Haki XP: %d");
    public static final String NEEDS_BUSO_HAKIXP = registerName(I18nCategory.GUI, "needs_hakixp.buso", "Needs busoshoku Haki XP: %d");
    public static final String NEEDS_HAO_BORN = registerName(I18nCategory.GUI, "needs_haoshoku", "Needs to be able to develop Haoshoku haki");
    public static final String NEEDS_LOYALTY = registerName(I18nCategory.GUI, "needs_loyalty", "Needs loyalty: %d");
    public static final String NEEDS_QUEST = registerName(I18nCategory.GUI, "needs_finished_quest", "Needs finished quest: %s");
    public static final String NEEDS_USED_ABILITY = registerName(I18nCategory.GUI, "needs_used_ability", "Needs to use %s %d times");

    public static void init() {
        Map<String, String> sorted = WyHelper.sortAlphabetically(LANG_MAP);
        Set<Map.Entry<String, String>> set = sorted.entrySet();
        Iterator<Map.Entry<String, String>> iter = set.iterator();

        Map.Entry<String, String> prevEntry = null;

        File langFolder = new File(WyHelper.getResourceFolderPath() + "/assets/" + DevilFruitProgressionMod.MOD_ID + "/lang/");
        langFolder.mkdirs();

        if (langFolder.exists()) {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(WyHelper.getResourceFolderPath() + "/assets/" +  DevilFruitProgressionMod.MOD_ID + "/lang/en_us.json"), "UTF-8"))) {
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
        }
    }

    private static String registerName(I18nCategory category, String key, String localizedName) {
        key = category.getId() + ".ability_progression." + key;
        LANG_MAP.put(key, localizedName);
        return key;
    }
}
