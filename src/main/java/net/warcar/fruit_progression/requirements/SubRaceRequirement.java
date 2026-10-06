package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.entities.charactercreator.Race;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.IEntityStats;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18nNodes;

import java.util.Optional;

public class SubRaceRequirement extends Requirement {
    public SubRaceRequirement() {
        super(String.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        Optional<IEntityStats> statsOptional = EntityStatsCapability.get(entity);
        if (statsOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no stats, can't check sub-race", entity);
            }
            return false;
        }
        Optional<Race> optionalRace = statsOptional.get().getSubRace();
        if (optionalRace.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no sub-race", entity);
            }
            return false;
        }
        return optionalRace.get().toString().equalsIgnoreCase(instance.getValues()[0]);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("subRace").getAsString());
        return instance;
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        Race race = Race.get(ResourceLocation.parse(instance.getValues()[0]));
        if (race == null) {
            return Component.translatable(ModI18nNodes.RACE_CHECK, instance.getValues()[0]);
        } else {
            return Component.translatable(ModI18nNodes.RACE_CHECK, race.getLabel().getString());
        }
    }
}
