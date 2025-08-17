package net.warcar.fruit_progression.new_data_reader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.warcar.fruit_progression.data.entity.abilities_addition.IContinuousComponentMixin;
import net.warcar.fruit_progression.requirements.RequirementSetInstance;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusManager;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusOperation;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;

import java.util.*;

public class RequirementModifiersPile {
    public final Map<ResourceLocation, List<Map<UUID, BonusManager.BonusValue>>> modifiers;
    private final RequirementSetInstance requirementSetInstance;

    public RequirementModifiersPile(Map<ResourceLocation, List<Map<UUID, BonusManager.BonusValue>>> modifiers, RequirementSetInstance instance) {
        requirementSetInstance = instance;
        this.modifiers = modifiers;
    }

    public void apply(IAbility ability) {
        if (modifiers == null) return;
        for (ResourceLocation modifier : modifiers.keySet()) {
            if (modifier.equals(ModAbilityKeys.COOLDOWN.getId())) {
                ability.getComponent(ModAbilityKeys.COOLDOWN).ifPresent(cooldown -> {
                    BonusManager bonusManager = cooldown.getBonusManager();
                    for (UUID uuid : modifiers.get(modifier).get(0).keySet()) {
                        BonusManager.BonusValue value = modifiers.get(modifier).get(0).get(uuid);
                        bonusManager.addBonus(uuid, value.getName(), value.getType(), value.getValue());
                    }
                });
            } else if (modifier.equals(ModAbilityKeys.CHARGE.getId())) {
                ability.getComponent(ModAbilityKeys.CHARGE).ifPresent(charge -> {
                    BonusManager bonusManager = charge.getMaxChargeBonusManager();
                    for (UUID uuid : modifiers.get(modifier).get(0).keySet()) {
                        BonusManager.BonusValue value = modifiers.get(modifier).get(0).get(uuid);
                        bonusManager.addBonus(uuid, value.getName(), value.getType(), value.getValue());
                    }
                });
            } else if (modifier.equals(ModAbilityKeys.DAMAGE.getId())) {
                ability.getComponent(ModAbilityKeys.DAMAGE).ifPresent(damage -> {
                    BonusManager bonusManager = damage.getBonusManager();
                    for (UUID uuid : modifiers.get(modifier).get(0).keySet()) {
                        BonusManager.BonusValue value = modifiers.get(modifier).get(0).get(uuid);
                        bonusManager.addBonus(uuid, value.getName(), value.getType(), value.getValue());
                    }
                });
            } else if (modifier.equals(ModAbilityKeys.HEAL.getId())) {
                ability.getComponent(ModAbilityKeys.HEAL).ifPresent(heal -> {
                    BonusManager bonusManager = heal.getBonusManager();
                    for (UUID uuid : modifiers.get(modifier).get(0).keySet()) {
                        BonusManager.BonusValue value = modifiers.get(modifier).get(0).get(uuid);
                        bonusManager.addBonus(uuid, value.getName(), value.getType(), value.getValue());
                    }
                });
            } else if (modifier.equals(ModAbilityKeys.RANGE.getId())) {
                ability.getComponent(ModAbilityKeys.RANGE).ifPresent(range -> {
                    BonusManager bonusManager = range.getBonusManager();
                    for (UUID uuid : modifiers.get(modifier).get(0).keySet()) {
                        BonusManager.BonusValue value = modifiers.get(modifier).get(0).get(uuid);
                        bonusManager.addBonus(uuid, value.getName(), value.getType(), value.getValue());
                    }
                });
            } else if (modifier.equals(ModAbilityKeys.PROJECTILE.getId())) {
                ability.getComponent(ModAbilityKeys.PROJECTILE).ifPresent(range -> {
                    BonusManager bonusManager = range.getDamageBonusManager();
                    for (UUID uuid : modifiers.get(modifier).get(0).keySet()) {
                        BonusManager.BonusValue value = modifiers.get(modifier).get(0).get(uuid);
                        bonusManager.addBonus(uuid, value.getName(), value.getType(), value.getValue());
                    }
                    BonusManager bonusManager1 = range.getInaccuracyBonusManager();
                    for (UUID uuid : modifiers.get(modifier).get(1).keySet()) {
                        BonusManager.BonusValue value = modifiers.get(modifier).get(1).get(uuid);
                        bonusManager1.addBonus(uuid, value.getName(), value.getType(), value.getValue());
                    }
                });
            } else if (modifier.equals(ModAbilityKeys.CONTINUOUS.getId())) {
                ability.getComponent(ModAbilityKeys.CONTINUOUS).ifPresent(continuous -> {
                    BonusManager bonusManager = ((IContinuousComponentMixin) continuous).getBonusManager();
                    for (UUID uuid : modifiers.get(modifier).get(0).keySet()) {
                        BonusManager.BonusValue value = modifiers.get(modifier).get(0).get(uuid);
                        bonusManager.addBonus(uuid, value.getName(), value.getType(), value.getValue());
                    }
                });
            }
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
        boolean debug = object.has("debug");
        Map<ResourceLocation, List<Map<UUID, BonusManager.BonusValue>>> modifiers;
        if (object.has("modifiers")) {
            JsonObject mods = object.getAsJsonObject("modifiers");
            modifiers = new HashMap<>();
            for (Map.Entry<String, JsonElement> s : mods.entrySet()) {
                ResourceLocation id = new ResourceLocation(s.getKey());
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
                        values.put(uuid, new BonusManager.BonusValue(name, operation, number));
                    }
                    list.add(values);
                }
                modifiers.put(id, list);
            }
        } else {
            modifiers = new HashMap<>();
        }
        RequirementModifiersPile pile = new RequirementModifiersPile(modifiers, requirementSet);
        if (debug) {
        }
        return pile;
    }

    public RequirementSetInstance getRequirementSetInstance() {
        return requirementSetInstance;
    }
}
