package net.warcar.fruit_progression.quests.fruit.zoan;

import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.fruit_progression.events.TrainingEvents;
import net.warcar.fruit_progression.init.ModQuests;
import net.warcar.fruit_progression.init.ModRequirements;
import net.warcar.fruit_progression.init.ModTexts;
import net.warcar.fruit_progression.quests.fruit.FruitControlsQuest;
import net.warcar.fruit_progression.quests.objectives.TimedObjective;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.quests.Quest;
import xyz.pixelatedw.mineminenomi.api.quests.QuestId;
import xyz.pixelatedw.mineminenomi.quests.QuestCompletionEvents;

public class TransformationEnduranceQuest extends Quest {
    public static final RegistryObject<QuestId<TransformationEnduranceQuest>> INSTANCE = ModQuests.registerQuest(() -> new QuestId.Builder<>(TransformationEnduranceQuest::new)
            .setRepeatCooldown(WyHelper.hoursToTicks(2.0F))
            .addRequirements(FruitControlsQuest.INSTANCE)
            .build(), "Transformation Endurance");
    private static final Component TITLE_1 = ModTexts.registerObjectiveTitle(INSTANCE.getId(), 0, "Stay in your zoan form for %d minutes", 5);


    private final TimedObjective objective = new TimedObjective(this, TITLE_1, WyHelper.minutesToTicks(5), TrainingEvents.IS_MORPHED, false);
    public TransformationEnduranceQuest(QuestId<TransformationEnduranceQuest> core) {
        super(core);

        this.addObjectives(objective);

        this.addCompleteEvent(QuestCompletionEvents.giveTrainingPoints(ModRequirements.DEVIL_FRUIT_POINTS, 3));
    }
}
