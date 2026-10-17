package net.warcar.fruit_progression.mixins;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.fruit_progression.init.ModQuests;
import org.spongepowered.asm.mixin.Mixin;
import xyz.pixelatedw.mineminenomi.api.entities.ITrainer;
import xyz.pixelatedw.mineminenomi.api.quests.QuestId;
import xyz.pixelatedw.mineminenomi.data.entity.quest.IQuestData;
import xyz.pixelatedw.mineminenomi.data.entity.quest.QuestCapability;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(Player.class)
public class PlayerMixin implements ITrainer {
    @Override
    public List<QuestId<?>> getAvailableQuests(Player player) {
        IQuestData questProps = QuestCapability.get(player).orElse(null);
        if (questProps == null) {
            return EMPTY;
        } else {
            List<QuestId<?>> availableQuests = ModQuests.DF_QUESTS.stream().map(RegistryObject::get).collect(Collectors.toList());
            availableQuests.addAll(ModQuests.ZOAN_QUESTS.stream().map(RegistryObject::get).toList());
            return availableQuests;
        }
    }
}
