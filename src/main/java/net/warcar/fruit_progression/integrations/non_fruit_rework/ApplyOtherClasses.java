package net.warcar.fruit_progression.integrations.non_fruit_rework;

import net.warcar.non_fruit_rework.enums.Faction;
import net.warcar.non_fruit_rework.enums.Race;
import net.warcar.non_fruit_rework.enums.Style;

public class ApplyOtherClasses {
    public static void faction(Class<?>[] classes) {
        classes[0] = Faction.class;
    }

    public static void race(Class<?>[] classes) {
        classes[0] = Race.class;
    }

    public static void style(Class<?>[] classes) {
        classes[0] = Style.class;
    }
}
