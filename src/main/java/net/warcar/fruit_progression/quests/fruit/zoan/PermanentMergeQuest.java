package net.warcar.fruit_progression.quests.fruit.zoan;

import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.fruit_progression.events.TrainingEvents;
import net.warcar.fruit_progression.init.ModQuests;
import net.warcar.fruit_progression.init.ModRequirements;
import net.warcar.fruit_progression.init.ModTexts;
import net.warcar.fruit_progression.quests.fruit.FineControlQuest;
import net.warcar.fruit_progression.quests.objectives.TimedObjective;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.quests.Quest;
import xyz.pixelatedw.mineminenomi.api.quests.QuestId;
import xyz.pixelatedw.mineminenomi.quests.QuestCompletionEvents;
import xyz.pixelatedw.mineminenomi.quests.objectives.KillEntityObjective;

public class PermanentMergeQuest extends Quest {
    public static final RegistryObject<QuestId<PermanentMergeQuest>> INSTANCE = ModQuests.registerQuest(() -> new QuestId.Builder<>(PermanentMergeQuest::new)
            .addRequirements(TransformationEnduranceQuest.INSTANCE, FineControlQuest.INSTANCE)
            .build(), "Permanent merge");
    private static final Component TITLE_1 = ModTexts.registerObjectiveTitle(INSTANCE.getId(), 0, "Stay in your zoan form for %d minutes consecutively", 10);
    private static final Component TITLE_2 = ModTexts.registerObjectiveTitle(INSTANCE.getId(), 1, "Kill %d enemies while in your zoan form", 35);



    private final TimedObjective objective = new TimedObjective(this, TITLE_1, WyHelper.minutesToTicks(10), TrainingEvents.IS_MORPHED, true);
    private final KillEntityObjective objective2 = new KillEntityObjective(this, TITLE_2, 35, (player, livingEntity, damageSource) ->
            TrainingEvents.IS_MORPHED.test(player));
    public PermanentMergeQuest(QuestId<PermanentMergeQuest> core) {
        super(core);

        this.addObjectives(objective, objective2);

        this.addCompleteEvent(QuestCompletionEvents.giveTrainingPoints(ModRequirements.DEVIL_FRUIT_POINTS, 10));
    }
}
