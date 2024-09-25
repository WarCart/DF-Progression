package net.warcar.fruit_progression.integrations.clothconfig;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import me.shedaniel.clothconfig2.forge.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.forge.api.ConfigBuilder;
import me.shedaniel.clothconfig2.forge.api.ConfigCategory;
import me.shedaniel.clothconfig2.forge.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.forge.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.MultiElementListEntry;
import me.shedaniel.clothconfig2.forge.gui.entries.NestedListListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.warcar.fruit_progression.DevilFruitProgressionMod;
import net.warcar.fruit_progression.init.ModRegistry;
import net.warcar.fruit_progression.requirements.Requirement;
import xyz.pixelatedw.mineminenomi.abilities.hitodaibutsu.ImpactBlastAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.integrations.clothconfig.AbilityDropdownEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ModConfigsIntegration {
    private static final AbilityCore<?> dummy = ImpactBlastAbility.INSTANCE;
    public static void registerConfigBuilder() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (client, parent) -> {
            ConfigBuilder builder = ConfigBuilder.create().setTitle(new StringTextComponent("Test"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            ConfigCategory category = builder.getOrCreateCategory(new StringTextComponent("Category"));
            category.addEntry(new NestedListListEntry<>(new StringTextComponent("Abilities with dependencies list"), new ArrayList<>(), false, Optional::empty, list -> {},
                    () -> Lists.newArrayList(new Pair(dummy)),
                    entryBuilder.getResetButtonKey(), true, true, (elem, nestedListListEntry) -> {
                if (elem == null) {
                    Pair newDefaultElemValue = new Pair(dummy);
                    return new MultiElementListEntry<>(new StringTextComponent("Ability"), newDefaultElemValue, Lists.newArrayList(new AbstractConfigListEntry[]{getAbility(entryBuilder, newDefaultElemValue), getRight(entryBuilder, newDefaultElemValue)}), true);
                } else {
                    return new MultiElementListEntry<>(new StringTextComponent("Ability"), elem, Lists.newArrayList(new AbstractConfigListEntry[]{getAbility(entryBuilder, elem), getRight(entryBuilder, elem)}), true);
                }
            }));
            builder.setParentScreen(parent);
            return builder.build();
        });
    }

    private static DropdownBoxEntry<AbilityCore> getAbility(ConfigEntryBuilder entryBuilder, Pair value) {
        return entryBuilder.startDropdownMenu(new StringTextComponent("Name"), AbilityDropdownEntry.ofAbilityObject(value.getAbility()),
                AbilityDropdownEntry.ofAbilityObject()).setDefaultValue(value.getAbility()).build();
    }

    private static NestedListListEntry getRight(ConfigEntryBuilder entryBuilder, Pair value) {
        return new NestedListListEntry<>(new StringTextComponent("Requirement Outer list"), new ArrayList<>(), false, Optional::empty, list -> {},
                Lists::newArrayList, entryBuilder.getResetButtonKey(), true, true, (elem, nestedListListEntry) -> {
            if (elem == null) {
                return new MultiElementListEntry<>(new StringTextComponent("Requirement Inner list"), getRightInner(entryBuilder, value), Lists.newArrayList(getRightInner(entryBuilder, value)), true);
            } else {
                return new MultiElementListEntry<>(new StringTextComponent("Requirement Inner list"), elem, Lists.newArrayList(getRightInner(entryBuilder, value)), true);
            }
        });
    }

    private static NestedListListEntry getRightInner(ConfigEntryBuilder entryBuilder, Pair value) {
        return new NestedListListEntry<>(new StringTextComponent("Requirements"), new ArrayList<>(), false, Optional::empty, list -> {},
                Lists::newArrayList, entryBuilder.getResetButtonKey(), true, true, (elem, nestedListListEntry) -> {
            return new MultiElementListEntry<>(new StringTextComponent("Requirement"), getRight(entryBuilder, value), Lists.newArrayList(entryBuilder.startDropdownMenu(new StringTextComponent("Name"), ofRequirementObject(GameRegistry.findRegistry(Requirement.class).getValue(new ResourceLocation(DevilFruitProgressionMod.MOD_ID, "doriki"))), ofRequirementObject()).build(),
                    entryBuilder.startStrField(new StringTextComponent("parameters"), "1000").build()), true);
        });
    }

    public static final Function<String, Requirement> ABILITY_FUNCTION = (str) -> {
        try {
            ResourceLocation identifier = new ResourceLocation(str);
            return ModRegistry.REQUIREMENTS.getValue(identifier);
        } catch (Exception var2) {
            return null;
        }
    };

    private static DropdownBoxEntry.SelectionTopCellElement<Requirement> ofRequirementObject(Requirement ability) {
        return new DropdownBoxEntry.DefaultSelectionTopCellElement<Requirement>(ability, ABILITY_FUNCTION, (i) -> ITextComponent.nullToEmpty(ModRegistry.REQUIREMENTS.getKey(i).toString())) {
            public void render(MatrixStack matrixStack, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
                this.textFieldWidget.x = x + 4;
                this.textFieldWidget.y = y + 6;
                this.textFieldWidget.setWidth(width - 4 - 20);
                this.textFieldWidget.setEditable(this.getParent().isEditable());
                this.textFieldWidget.setTextColor(this.getPreferredTextColor());
                this.textFieldWidget.render(matrixStack, mouseX, mouseY, delta);
            }
        };
    }

    public static DropdownBoxEntry.SelectionCellCreator<Requirement> ofRequirementObject() {
        return ofRequirementObject(10);
    }

    public static DropdownBoxEntry.SelectionCellCreator<Requirement> ofRequirementObject(int maxItems) {
        return ofRequirementObject(20, 170, maxItems);
    }

    public static DropdownBoxEntry.SelectionCellCreator<Requirement> ofRequirementObject(final int cellHeight, final int cellWidth, final int maxItems) {
        return new DropdownBoxEntry.DefaultSelectionCellCreator<Requirement>((i) -> ITextComponent.nullToEmpty(ModRegistry.REQUIREMENTS.getKey(i).toString())) {
            public DropdownBoxEntry.SelectionCellElement<Requirement> create(final Requirement selection) {
                return new DropdownBoxEntry.DefaultSelectionCellElement<Requirement>(selection, this.toTextFunction) {
                    public void render(MatrixStack matrixStack, int mouseX, int mouseY, int x, int y, int width, int height, float delta) {
                        this.rendering = true;
                        this.x = x;
                        this.y = y;
                        this.width = width;
                        this.height = height;
                        boolean b = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
                        if (b) {
                            AbstractGui.fill(matrixStack, x + 1, y + 1, x + width - 1, y + height - 1, -15132391);
                        }

                        Minecraft mc = Minecraft.getInstance();
                        mc.font.drawShadow(matrixStack, this.toTextFunction.apply(this.r).getVisualOrderText(), (float)(x + 6 + 18), (float)(y + 6), b ? 16777215 : 8947848);
                    }
                };
            }

            public int getCellHeight() {
                return cellHeight;
            }

            public int getCellWidth() {
                return cellWidth;
            }

            public int getDropBoxMaxHeight() {
                return this.getCellHeight() * maxItems;
            }
        };
    }


    private static class Pair {
        private final AbilityCore<?> ability;
        private final List<Requirement> requirements = new ArrayList<>();

        Pair(AbilityCore<?> ability, Requirement... requirements) {
            this.ability = ability;
            this.requirements.addAll(Arrays.asList(requirements));
        }

        public AbilityCore<?> getAbility() {
            return this.ability;
        }

        public List<Requirement> getRequirements() {
            return this.requirements;
        }
    }
}
