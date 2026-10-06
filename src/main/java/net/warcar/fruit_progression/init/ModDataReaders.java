package net.warcar.fruit_progression.init;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.new_data_reader.AbilityDataReader;
import net.warcar.fruit_progression.new_data_reader.RequirementModifiersPile;
import net.warcar.fruit_progression.nodes.AbilityNodeLink;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;


@Mod.EventBusSubscriber(modid = DevilFruitProgressionMod.MOD_ID)
public class ModDataReaders {
    public static final AbilityDataReader<RequirementModifiersPile> ABILITIES_READER = new AbilityDataReader<>("abilities", RequirementModifiersPile::getFromJson);
    public static final AbilityDataReader<RequirementSetInstance> AWAKENINGS_READER = new AbilityDataReader<>("awakenings", RequirementSetInstance::getRequirementSetInstance);
    public static final AbilityDataReader<RequirementSetInstance> SAVED_REQUIREMENTS_READER = new AbilityDataReader<>("saved_requirements", RequirementSetInstance::getRequirementSetInstance);
    public static final AbilityDataReader<AbilityNodeLink> ABILITY_TREE_READER = new AbilityDataReader<>("ability_tree_nodes", AbilityNodeLink::deserializeFromJson);

    public static void init() {}

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(SAVED_REQUIREMENTS_READER);
        event.addListener(ABILITIES_READER);
        event.addListener(AWAKENINGS_READER);
        event.addListener(ABILITY_TREE_READER);
    }
}
