package net.warcar.fruit_progression.quests.fruit;

import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.fruit_progression.init.ModQuests;
import net.warcar.fruit_progression.init.ModRequirements;
import net.warcar.fruit_progression.init.ModTexts;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.damagesources.AbilityDamageSource;
import xyz.pixelatedw.mineminenomi.api.quests.Quest;
import xyz.pixelatedw.mineminenomi.api.quests.QuestId;
import xyz.pixelatedw.mineminenomi.quests.QuestCompletionEvents;
import xyz.pixelatedw.mineminenomi.quests.objectives.HitEntityObjective;
import xyz.pixelatedw.mineminenomi.quests.objectives.KillEntityObjective;

public class KillingIntentQuest extends Quest {
    public static final RegistryObject<QuestId<KillingIntentQuest>> INSTANCE = ModQuests.registerQuest(() -> new QuestId.Builder<>(KillingIntentQuest::new)
            .addRequirements(FineControlQuest.INSTANCE)
            .build(), "Killing Intent");
    private static final Component TITLE_1 = ModTexts.registerObjectiveTitle(INSTANCE.getId(), 0, "Attack %d entities with devil fruit abilities", 15);
    private static final Component TITLE_2 = ModTexts.registerObjectiveTitle(INSTANCE.getId(), 1, "Kill %d entities with devil fruit abilities", 35);

    private static final HitEntityObjective.ICheckHit HIT_CHECK = (player, entity, source, dmg) -> {
        if (source instanceof AbilityDamageSource abilitySource) {
            return abilitySource.getAbilityCore().getCategory() == AbilityCategory.DEVIL_FRUITS;
        }
        return false;
    };

    private static final KillEntityObjective.ICheckKill KILL_CHECK = (player, entity, source) -> {
        if (source instanceof AbilityDamageSource abilitySource) {
            return abilitySource.getAbilityCore().getCategory() == AbilityCategory.DEVIL_FRUITS;
        }
        return false;
    };


    private final HitEntityObjective objective = new HitEntityObjective(this, TITLE_1, 15, HIT_CHECK);
    private final KillEntityObjective objective2 = new KillEntityObjective(this, TITLE_2, 35, KILL_CHECK).addRequirements(objective);
    public KillingIntentQuest(QuestId<KillingIntentQuest> core) {
        super(core);

        this.addObjectives(objective, objective2);

        this.addCompleteEvent(QuestCompletionEvents.giveTrainingPoints(ModRequirements.DEVIL_FRUIT_POINTS, 10));
    }
}
