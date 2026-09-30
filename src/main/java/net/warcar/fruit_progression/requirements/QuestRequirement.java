package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import xyz.pixelatedw.mineminenomi.api.WyRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.quests.QuestId;
import xyz.pixelatedw.mineminenomi.data.entity.quest.QuestCapability;

public class QuestRequirement extends Requirement {
    public QuestRequirement() {
        super(QuestId.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        if (entity instanceof Player) {
            return QuestCapability.get((Player) entity).get().hasFinishedQuest(WyRegistry.QUESTS.get().getValue(ResourceLocation.parse(instance.getValues()[0])));
        }
        return false;
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("questID").getAsString());
        return instance;
    }
}
