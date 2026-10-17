package net.warcar.fruit_progression.mixins.client;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.config.ModConfig;
import net.warcar.fruit_progression.data.entity.abilities_addition.AbilityAdditionDataCapability;
import net.warcar.fruit_progression.init.ModQuests;
import net.warcar.fruit_progression.init.ModTexts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pixelatedw.mineminenomi.api.DFEncyclopediaEntry;
import xyz.pixelatedw.mineminenomi.api.enums.FruitType;
import xyz.pixelatedw.mineminenomi.api.quests.Quest;
import xyz.pixelatedw.mineminenomi.api.quests.QuestId;
import xyz.pixelatedw.mineminenomi.config.ServerConfig;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.data.entity.quest.IQuestData;
import xyz.pixelatedw.mineminenomi.data.entity.quest.QuestCapability;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18n;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.items.DFEncyclopediaItem;
import xyz.pixelatedw.mineminenomi.ui.screens.EncyclopediaScreen;
import xyz.pixelatedw.mineminenomi.ui.screens.TrainerTrialsListScreen;
import xyz.pixelatedw.mineminenomi.ui.screens.dialogues.TrainerDialogueScreen;
import xyz.pixelatedw.mineminenomi.ui.widget.PlankButton;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(EncyclopediaScreen.class)
public class EncyclopediaScreenMixin extends Screen {
    @Shadow
    private List<DFEncyclopediaEntry> entries;

    @Shadow
    private int currPage;

    @Shadow
    private ItemStack bookStack;

    private EncyclopediaScreenMixin() {
        super(Component.empty());
    }

    @Unique
    private static Optional<AkumaNoMiItem> abilityProgression$getFruit(IDevilFruit iDevilFruit) {
        Item devilFruitItem = iDevilFruit.getDevilFruitItem();
        if (devilFruitItem instanceof AkumaNoMiItem df) {
            return Optional.of(df);
        }
        return Optional.empty();
    }

    @Unique
    private static boolean abilityProgression$isZoan(FruitType check) {
        return check == FruitType.ZOAN || check == FruitType.ANCIENT_ZOAN || check == FruitType.MYTHICAL_ZOAN || check == FruitType.ARTIFICIAL_ZOAN;
    }

    @ModifyVariable(method = "render", at = @At("STORE"), name = "baseColor")
    private Color newBaseColor(Color baseColor) {
        if (!ModConfig.isEncyclopediaOverhauled()) {
            return baseColor;
        }
        if (!ServerConfig.getRandomizedFruits()) {
            return Color.WHITE;
        }
        return baseColor;
    }

    @ModifyVariable(method = "render", at = @At("STORE"), name = "stemColor")
    private Color newStemColor(Color stemColor) {
        if (!ModConfig.isEncyclopediaOverhauled()) {
            return stemColor;
        }
        if (!ServerConfig.getRandomizedFruits()) {
            return Color.WHITE;
        }
        return stemColor;
    }

    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"), name = "baseIcon")
    private ResourceLocation newIcon(ResourceLocation baseIcon) {
        if (!ModConfig.isEncyclopediaOverhauled()) {
            return baseIcon;
        }
        if (ServerConfig.getRandomizedFruits()) {
            return baseIcon;
        }
        DFEncyclopediaEntry entry = this.entries.get(this.currPage);
        if (entry != null) {
            AkumaNoMiItem df = entry.getDevilFruit();
            if (df != null && AbilityAdditionDataCapability.get(this.minecraft.player).map(data -> data.hasReadAbout(df)).orElse(false)) {
                return convertToTexture(df.getRegistryKey());
            }
        }
        return baseIcon;
    }

    private ResourceLocation convertToTexture(ResourceLocation loc) {
        return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), "textures/items/" + loc.getPath() + ".png");
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addButtons(CallbackInfo ci) {
        if (!ModConfig.isEncyclopediaOverhauled()) {
            return;
        }
        LocalPlayer player = this.minecraft.player;
        this.addRenderableWidget(new PlankButton(this.width / 2 + 70, 171, 98, 25, Component.translatable(ModTexts.SAVE), (button) -> {
            AbilityAdditionDataCapability.get(player).ifPresent(data -> {
                CompoundTag unlocked = this.bookStack.getOrCreateTagElement(DFEncyclopediaEntry.ENCYCLOPEDIA_UNLOCKED_TAG);
                for (AkumaNoMiItem df : data.allReadFruits()) {
                    String name = df.getRegistryKey().toString();
                    if (!unlocked.contains(name)) {
                        unlocked.put(name, new CompoundTag());
                    }
                }
                if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof DFEncyclopediaItem) {
                    player.setItemSlot(EquipmentSlot.MAINHAND, this.bookStack);
                } else if (player.getItemBySlot(EquipmentSlot.OFFHAND).getItem() instanceof DFEncyclopediaItem) {
                    player.setItemSlot(EquipmentSlot.OFFHAND, this.bookStack);
                } else {
                    DevilFruitProgressionMod.LOGGER.warn("Player isn't holding the book, can't save");
                }
            });
        }));
        PlankButton quests = this.addRenderableWidget(new PlankButton(this.width / 2 - 170, 171, 98, 25, ModI18n.GUI_TRIALS, (button) -> {
            QuestCapability.get(player).ifPresent(data -> {
                List<TrainerDialogueScreen.QuestEntry> entries = new ArrayList<>(ability_progression$handleQuests(data, player, ModQuests.DF_QUESTS));
                boolean zoan = DevilFruitCapability.get(player).flatMap(EncyclopediaScreenMixin::abilityProgression$getFruit).map(AkumaNoMiItem::getType)
                        .map(EncyclopediaScreenMixin::abilityProgression$isZoan).orElse(false);
                if (zoan) {
                    entries.addAll(ability_progression$handleQuests(data, player, ModQuests.ZOAN_QUESTS));
                }
                this.minecraft.setScreen(new TrainerTrialsListScreen(this, player, entries, false));
            });
        }));
        if (!DevilFruitCapability.get(player).map(IDevilFruit::hasAnyDevilFruit).orElse(false)) {
            quests.active = false;
            quests.setTooltip(Tooltip.create(Component.translatable(ModTexts.NEEDS_ANY_DF)));
        }
    }

    @Unique
    private static List<TrainerDialogueScreen.QuestEntry> ability_progression$handleQuests(IQuestData data, LocalPlayer player, List<RegistryObject<? extends QuestId<? extends Quest>>> quests) {
        List<TrainerDialogueScreen.QuestEntry> entries = new ArrayList<>();
        for (RegistryObject<? extends QuestId<? extends Quest>> object : quests) {
            QuestId<? extends Quest> quest = object.get();
            if (!ability_progression$isAvailable(data, quest)) {
                continue;
            }
            TrainerDialogueScreen.QuestEntry entry = new TrainerDialogueScreen.QuestEntry(quest);
            if (data.hasInProgressQuest(quest) && data.getInProgressQuest(quest).isComplete()) {
                entry.setComplete();
            }
            if (quest.isLocked(player)) {
                entry.setLocked();
            }
            entries.add(entry);
        }
        return entries;
    }

    @Unique
    private static boolean ability_progression$isAvailable(IQuestData props, QuestId<?> quest) {
        Quest inprogressQuest = props.getInProgressQuest(quest);

        if (inprogressQuest != null && inprogressQuest.isComplete()) {
            DevilFruitProgressionMod.LOGGER.debug("complete");
            return true;
        }

        boolean hasQuestInProg = inprogressQuest != null;

        return !hasQuestInProg && (!props.hasFinishedQuest(quest) || quest.isRepeatable());}
}
