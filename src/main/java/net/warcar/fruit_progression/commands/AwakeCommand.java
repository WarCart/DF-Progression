package net.warcar.fruit_progression.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.warcar.fruit_progression.data.entity.awakening.AwakeningDataCapability;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;

import java.util.List;

public class AwakeCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("awake").requires((source) -> source.hasPermission(2));
        builder.executes(context -> awake(context, Lists.newArrayList((ServerPlayerEntity) context.getSource().getEntity()), true))
                .then(Commands.argument("targets", EntityArgument.players()).executes(context -> awake(context, Lists.newArrayList(EntityArgument.getPlayers(context, "targets")), true))
                        .then(Commands.argument("awakened", BoolArgumentType.bool()).executes(context -> awake(context, Lists.newArrayList(EntityArgument.getPlayers(context, "targets")), BoolArgumentType.getBool(context, "awakened")))));
        dispatcher.register(builder);
    }

    public static int awake(CommandContext<CommandSource> context, List<ServerPlayerEntity> players, boolean set) {
        StringBuilder builder = new StringBuilder();
        for (ServerPlayerEntity player : players) {
            AwakeningDataCapability.get(player).setAwake(set);
            builder.append(player.getName().getString());
            builder.append(" set awakened to ");
            builder.append(set);
            builder.append("\n");
            AbilityHelper.validateDevilFruitMoves(player);
        }
        String str = builder.toString();
        str = str.substring(0, str.length() - 1);
        context.getSource().sendSuccess(new StringTextComponent(str), true);
        return 1;
    }
}
