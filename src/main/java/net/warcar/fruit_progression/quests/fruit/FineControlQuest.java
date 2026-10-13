package net.warcar.fruit_progression.quests.fruit;

import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.fruit_progression.init.ModQuests;
import net.warcar.fruit_progression.init.ModRequirements;
import net.warcar.fruit_progression.init.ModTexts;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.damagesources.AbilityDamageSource;
import xyz.pixelatedw.mineminenomi.api.quests.Quest;
import xyz.pixelatedw.mineminenomi.api.quests.QuestId;
import xyz.pixelatedw.mineminenomi.quests.QuestCompletionEvents;
import xyz.pixelatedw.mineminenomi.quests.objectives.HitEntityObjective;

public class FineControlQuest extends Quest {
    public static final RegistryObject<QuestId<FineControlQuest>> INSTANCE = ModQuests.registerQuest(() -> new QuestId.Builder<>(FineControlQuest::new)
            .setRepeatCooldown(WyHelper.hoursToTicks(1.5F))
            .addRequirements(FruitControlsQuest.INSTANCE)
            .build(), "Fine control");
    private static final Component TITLE_1 = ModTexts.registerObjectiveTitle(INSTANCE.getId(), 0, "Attack %d entities with devil fruit abilities", 10);

    private static final HitEntityObjective.ICheckHit CHECK = (player, entity, source, dmg) -> {
        if (source instanceof AbilityDamageSource abilitySource) {
            return abilitySource.getAbilityCore().getCategory() == AbilityCategory.DEVIL_FRUITS;
        }
        return false;
    };


    private final HitEntityObjective objective = new HitEntityObjective(this, TITLE_1, 10, CHECK);
    public FineControlQuest(QuestId<FineControlQuest> core) {
        super(core);

        this.addObjectives(objective);

        this.addCompleteEvent(QuestCompletionEvents.giveTrainingPoints(ModRequirements.DEVIL_FRUIT_POINTS, 3));
    }
}
