package net.warcar.fruit_progression.requirements;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.entities.charactercreator.Faction;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.IEntityStats;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18nNodes;

import java.util.Optional;

public class FactionRequirement extends Requirement {
    public FactionRequirement() {
        super(Faction.class);
    }

    public boolean requirementMet(LivingEntity entity, AbilityCore<?> core, RequirementInstance instance) {
        Optional<IEntityStats> statsOptional = EntityStatsCapability.get(entity);
        if (statsOptional.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no stats, can't check fraction", entity);
            }
            return false;
        }
        IEntityStats entityStats = statsOptional.get();
        Optional<Faction> optionalFaction = entityStats.getFaction();
        if (optionalFaction.isEmpty()) {
            if (instance.isDebug()) {
                DevilFruitProgressionMod.LOGGER.warn("Entity {} has no fraction", entity);
            }
            return false;
        }
        if (instance.isDebug()) {
            DevilFruitProgressionMod.LOGGER.info("{}/{}", optionalFaction.get(), instance.getValues()[0]);
        }
        return optionalFaction.get().getRegistryName().toString().equalsIgnoreCase(instance.getValues()[0]);
    }

    public RequirementInstance deserializeInstance(JsonObject json) {
        RequirementInstance instance = new RequirementInstance(this);
        instance.setValues(json.get("faction").getAsString());
        return instance;
    }

    @Override
    public MutableComponent getTooltip(RequirementInstance instance) {
        Faction race = Faction.get(ResourceLocation.parse(instance.getValues()[0]));
        if (race == null) {
            return Component.translatable(ModI18nNodes.FACTION_CHECK, instance.getValues()[0]);
        } else {
            return Component.translatable(ModI18nNodes.FACTION_CHECK, race.getLabel().getString());
        }
    }
}
