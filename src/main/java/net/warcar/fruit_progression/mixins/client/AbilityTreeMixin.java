package net.warcar.fruit_progression.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.warcar.fruit_progression.data.mixin_interfaces.INodeMixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.pixelatedw.mineminenomi.api.WyDebug;
import xyz.pixelatedw.mineminenomi.api.abilities.nodes.AbilityNode;
import xyz.pixelatedw.mineminenomi.api.helpers.RendererHelper;
import xyz.pixelatedw.mineminenomi.config.ServerConfig;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.ui.screens.AbilityTreeScreen;

import java.util.List;
import java.util.Map;

@Mixin(value = AbilityTreeScreen.class, remap = false)
public abstract class AbilityTreeMixin extends Screen {
    @Shadow
    private boolean editMode;

    @Shadow
    private int iconRadius;

    @Shadow
    private float zoom;

    @Shadow
    @Final
    private static int NODE_SIZE;

    @Shadow
    private int marginScaled;

    @Shadow
    @Final
    private static int MARGIN_SIZE;

    @Shadow
    private float gridStepScaled;

    @Shadow
    @Final
    private static int GRID_STEP;

    @Shadow
    private AbilityNode hoveredNode;

    @Shadow
    private IAbilityData abilityProps;

    @Shadow
    protected abstract AbilityNode.NodePos getNodePosition(AbilityNode node);

    @Shadow
    protected abstract int getCenteredDragX(AbilityNode.NodePos pos);

    @Shadow
    protected abstract int getCenteredDragY(AbilityNode.NodePos pos);

    @Shadow
    private int centerX;

    @Shadow
    private int centerY;

    @Shadow
    public List<AbilityNode> unlockableNodes;

    @Shadow
    private AbilityNode draggingNode;

    @Shadow
    private boolean isDragging;

    @Shadow
    protected abstract void drawNodeTooltip(GuiGraphics graphics, AbilityNode node);

    @Shadow
    private Map<AbilityNode, AbilityNode.NodePos> editedPositions;

    @Shadow
    protected abstract void drawLine(GuiGraphics graphics, AbilityNode parentNode, AbilityNode node, float startPosX, float startPosY, float endPosX, float endPosY, int color, float thickness);

    @Shadow
    @Final
    private static float LINE_THICKNESS;

    @Shadow
    protected abstract void drawTrainingPointsPanel(GuiGraphics graphics);

    private AbilityTreeMixin() {
        super(Component.empty());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics);

        // Reverts the white highlight from the edit button when stopping edit mode
        if (WyDebug.isDebug() && !this.editMode) {
            this.setFocused(null);
        }

        Minecraft mc = super.getMinecraft();

        LocalPlayer player = mc.player;

        this.iconRadius = Math.round(NODE_SIZE * this.zoom / 2.0F);
        this.marginScaled = Math.round(MARGIN_SIZE * this.zoom);
        this.gridStepScaled = GRID_STEP * this.zoom;

        this.hoveredNode = null;

        for (AbilityNode node : this.abilityProps.getNodes()) {
            if (!((INodeMixin) node).ability_progression$isVisible(player)) {
                continue;
            }
            graphics.pose().pushPose();

            AbilityNode.NodePos pos = this.getNodePosition(node);
            this.centerX = this.getCenteredDragX(pos);
            this.centerY = this.getCenteredDragY(pos);

            int colour = 0xFF888888;
            int backgroundColour = 0;

            if (node.isUnlocked(player)) {
                colour = 0xFF13b313;
                backgroundColour = 0xFF0e800e;
            } else if (this.unlockableNodes.contains(node)) {
                colour = 0xFFFFA500;
                backgroundColour = 0xFFcc8400;
            } else {
                colour = 0xFFB81414;
                backgroundColour = 0xFF850f0f;
            }

            int fillMinX = this.centerX - this.iconRadius - this.marginScaled - 1;
            int fillMinY = this.centerY - this.iconRadius - this.marginScaled - 1;
            int fillMaxX = this.centerX + this.iconRadius + this.marginScaled - 1;
            int fillMaxY = this.centerY + this.iconRadius + this.marginScaled - 1;

            graphics.fill(fillMinX, fillMinY, fillMaxX, fillMaxY, 10, backgroundColour);
            graphics.fill(fillMinX + 2, fillMinY + 2, fillMaxX - 2, fillMaxY - 2, 15, colour);

            float iconLeft = this.centerX - 1.0f - this.iconRadius;
            float iconTop = this.centerY - 1.0f - this.iconRadius;
            int iconSizeI = this.iconRadius * 2;

            RendererHelper.drawIcon(node.getIcon(), graphics.pose(), iconLeft, iconTop, 15.0F, iconSizeI, iconSizeI);

            if (!this.isDragging && mouseX >= this.centerX - this.iconRadius && mouseX <= this.centerX + this.iconRadius && mouseY >= this.centerY - this.iconRadius && mouseY <= this.centerY + this.iconRadius) {
                this.hoveredNode = node;

                if (this.draggingNode == null) {
                    this.drawNodeTooltip(graphics, this.hoveredNode);
                }

                int overlayMinX = this.centerX - this.iconRadius - 1;
                int overlayMinY = this.centerY - this.iconRadius - 1;
                int overlayMaxX = this.centerX + this.iconRadius - 1;
                int overlayMaxY = this.centerY + this.iconRadius - 1;

                graphics.fill(overlayMinX, overlayMinY, overlayMaxX, overlayMaxY, 20, 0x40FFFFFF);

                if (this.editMode) {
                    AbilityNode.NodePos hoveredPos = this.editedPositions.getOrDefault(node, node.getPosition());

                    String label = "Grid Position: (" + hoveredPos.x() + ", " + hoveredPos.y() + ")";

                    int labelX = super.width / 2 - super.font.width(label) / 2;
                    int labelY = 5;

                    graphics.drawString(super.font, label, labelX, labelY, 0xFFFFFF, true);
                }
            }

            graphics.pose().translate(0.0, 0.0, -30.0); //Not sure why -30 specifically but it works ¯\_(ツ)_/¯
            for (AbilityNode parentNode : node.getPrerequisites()) {
                AbilityNode.NodePos parentPos = this.getNodePosition(parentNode);
                float parentCenterXF = this.getCenteredDragX(parentPos);
                float parentCenterYF = this.getCenteredDragY(parentPos);

//				NodePos start = clipToSquareEdge(parentCenterXF, parentCenterYF, centerXF, centerYF, iconRadiusF + marginScaledF * 3);
//				NodePos end = clipToSquareEdge(centerXF, centerYF, parentCenterXF, parentCenterYF, iconRadiusF + marginScaledF * 3);

                this.drawLine(graphics, parentNode, node, parentCenterXF, parentCenterYF, this.centerX, this.centerY, 0xFF808080, LINE_THICKNESS * this.zoom);
            }

            graphics.pose().popPose();
        }

        if (ServerConfig.isQuestProgressionEnabled()) {
            this.drawTrainingPointsPanel(graphics);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
