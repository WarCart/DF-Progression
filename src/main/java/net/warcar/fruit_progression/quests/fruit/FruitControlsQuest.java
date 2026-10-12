package net.warcar.fruit_progression.quests.fruit;

import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.fruit_progression.init.ModQuests;
import net.warcar.fruit_progression.init.ModRequirements;
import net.warcar.fruit_progression.init.ModTexts;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.quests.Quest;
import xyz.pixelatedw.mineminenomi.api.quests.QuestId;
import xyz.pixelatedw.mineminenomi.quests.QuestCompletionEvents;
import xyz.pixelatedw.mineminenomi.quests.objectives.UseAbilityObjective;

public class FruitControlsQuest extends Quest {
    public static final RegistryObject<QuestId<FruitControlsQuest>> INSTANCE = ModQuests.registerQuest(() -> new QuestId.Builder<>(FruitControlsQuest::new)
            .setRepeatCooldown(WyHelper.hoursToTicks(1.0F))
            .build(), "Fruit controls");
    private static final Component TITLE_1 = ModTexts.registerObjectiveTitle(INSTANCE.getId(), 0, "Use %d devil fruit abilities", 10);

    private static final UseAbilityObjective.ICheckAbilityUse CHECK = (player, ability) ->
            ability.getCore().getCategory() == AbilityCategory.DEVIL_FRUITS;


    private final UseAbilityObjective objective = new UseAbilityObjective(this, TITLE_1, 10, CHECK);
    public FruitControlsQuest(QuestId<FruitControlsQuest> core) {
        super(core);

        this.addObjectives(objective);

        this.addCompleteEvent(QuestCompletionEvents.giveTrainingPoints(ModRequirements.DEVIL_FRUIT_POINTS, 1));
    }
}
