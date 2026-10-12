package net.warcar.fruit_progression.init;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.quests.fruit.FruitControlsQuest;
import net.warcar.fruit_progression.quests.fruit.TransformationEnduranceQuest;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.WyRegistry;
import xyz.pixelatedw.mineminenomi.api.quests.Quest;
import xyz.pixelatedw.mineminenomi.api.quests.QuestId;
import xyz.pixelatedw.mineminenomi.init.ModRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModQuests {
    public static final DeferredRegister<QuestId<?>> QUESTS_REGISTER = DeferredRegister.create(WyRegistry.Keys.QUESTS, DevilFruitProgressionMod.MOD_ID);

    public static final List<RegistryObject<? extends QuestId<? extends Quest>>> DF_QUESTS = Arrays.asList(FruitControlsQuest.INSTANCE);
    public static final List<RegistryObject<? extends QuestId<? extends Quest>>> ZOAN_QUESTS = Arrays.asList(TransformationEnduranceQuest.INSTANCE);

    public static <T extends QuestId<?>> RegistryObject<T> registerQuest(Supplier<T> supplier, String name) {
        String resourceName = WyHelper.getResourceName(name);
        ModTexts.registerName(ModRegistry.I18nCategory.QUEST, resourceName, name);
        return QUESTS_REGISTER.register(resourceName, supplier);
    }

    public static void register(IEventBus bus) {
        QUESTS_REGISTER.register(bus);
    }
}
