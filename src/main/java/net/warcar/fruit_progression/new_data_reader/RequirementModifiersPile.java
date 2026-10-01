package net.warcar.fruit_progression.new_data_reader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.warcar.fruit_progression.data.entity.abilities_addition.IContinuousComponentMixin;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusManager;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusOperation;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;

import java.util.*;

public record RequirementModifiersPile(Map<ResourceLocation, List<Map<UUID, BonusManager.BonusValue>>> modifiers,
                                       RequirementSetInstance requirementSetInstance) {
    public void apply(IAbility ability) {
        if (modifiers == null) return;
        for (ResourceLocation modifier : modifiers.keySet()) {
            if (modifier.equals(ModAbilityComponents.COOLDOWN.getId())) {
                ability.getComponent(ModAbilityComponents.COOLDOWN.get()).ifPresent(cooldown -> {
                    BonusManager bonusManager = cooldown.getBonusManager();
                    applyModifiers(modifier, bonusManager);
                });
            } else if (modifier.equals(ModAbilityComponents.CHARGE.getId())) {
                ability.getComponent(ModAbilityComponents.CHARGE.get()).ifPresent(charge -> {
                    BonusManager bonusManager = charge.getMaxChargeBonusManager();
                    applyModifiers(modifier, bonusManager);
                });
            } else if (modifier.equals(ModAbilityComponents.DAMAGE.getId())) {
                ability.getComponent(ModAbilityComponents.DAMAGE.get()).ifPresent(damage -> {
                    BonusManager bonusManager = damage.getBonusManager();
                    applyModifiers(modifier, bonusManager);
                });
            } else if (modifier.equals(ModAbilityComponents.HEAL.getId())) {
                ability.getComponent(ModAbilityComponents.HEAL.get()).ifPresent(heal -> {
                    BonusManager bonusManager = heal.getBonusManager();
                    applyModifiers(modifier, bonusManager);
                });
            } else if (modifier.equals(ModAbilityComponents.RANGE.getId())) {
                ability.getComponent(ModAbilityComponents.RANGE.get()).ifPresent(range -> {
                    BonusManager bonusManager = range.getBonusManager();
                    applyModifiers(modifier, bonusManager);
                });
            } else if (modifier.equals(ModAbilityComponents.PROJECTILE.getId())) {
                ability.getComponent(ModAbilityComponents.PROJECTILE.get()).ifPresent(range -> {
                    BonusManager bonusManager = range.getDamageBonusManager();
                    applyModifiers(modifier, 0, bonusManager);
                    BonusManager bonusManager1 = range.getInaccuracyBonusManager();
                    applyModifiers(modifier, 1, bonusManager1);
                });
            } else if (modifier.equals(ModAbilityComponents.CONTINUOUS.getId())) {
                ability.getComponent(ModAbilityComponents.CONTINUOUS.get()).ifPresent(continuous -> {
                    BonusManager bonusManager = ((IContinuousComponentMixin) continuous).ability_progression$getBonusManager();
                    applyModifiers(modifier, bonusManager);
                });
            }
        }
    }

    private void applyModifiers(ResourceLocation modifier, BonusManager bonusManager) {
        applyModifiers(modifier, 0, bonusManager);
    }

    private void applyModifiers(ResourceLocation modifier, int index, BonusManager bonusManager) {
        for (UUID uuid : modifiers.get(modifier).get(index).keySet()) {
            BonusManager.BonusValue value = modifiers.get(modifier).get(index).get(uuid);
            bonusManager.addBonus(uuid, value.getName(), value.getType(), value.getValue());
        }
    }

    public static RequirementModifiersPile getFromJson(JsonElement jsonElement, ResourceLocation location) {
        JsonObject object = jsonElement.getAsJsonObject();
        RequirementSetInstance requirementSet;
        if (object.has("requirements")) {
            requirementSet = RequirementSetInstance.getRequirementSetInstance(jsonElement, location);
        } else {
            requirementSet = null;
        }
        Map<ResourceLocation, List<Map<UUID, BonusManager.BonusValue>>> modifiers;
        if (object.has("modifiers")) {
            JsonObject mods = object.getAsJsonObject("modifiers");
            modifiers = new HashMap<>();
            for (Map.Entry<String, JsonElement> s : mods.entrySet()) {
                ResourceLocation id = ResourceLocation.parse(s.getKey());
                List<Map<UUID, BonusManager.BonusValue>> list = new ArrayList<>();
                for (JsonElement mod : s.getValue().getAsJsonArray()) {
                    Map<UUID, BonusManager.BonusValue> values = new HashMap<>();
                    JsonObject value = mod.getAsJsonObject();
                    for (Map.Entry<String, JsonElement> e : value.entrySet()) {
                        UUID uuid = UUID.fromString(e.getKey());
                        JsonObject bonus = mod.getAsJsonObject().getAsJsonObject(e.getKey());
                        BonusOperation operation = BonusOperation.valueOf(bonus.get("type").getAsString().toUpperCase());
                        String name = bonus.get("name").getAsString();
                        float number = bonus.get("value").getAsFloat();
                        values.put(uuid, new BonusManager.BonusValue(uuid, name, operation, number));
                    }
                    list.add(values);
                }
                modifiers.put(id, list);
            }
        } else {
            modifiers = new HashMap<>();
        }
        return new RequirementModifiersPile(modifiers, requirementSet);
    }
}
