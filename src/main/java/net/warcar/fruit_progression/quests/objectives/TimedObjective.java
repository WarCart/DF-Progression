package net.warcar.fruit_progression.quests.objectives;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import xyz.pixelatedw.mineminenomi.api.quests.Quest;
import xyz.pixelatedw.mineminenomi.api.quests.objectives.ISurviveObjective;
import xyz.pixelatedw.mineminenomi.api.quests.objectives.Objective;

import java.util.function.Predicate;

public class TimedObjective extends Objective implements ISurviveObjective {
	private int timeToSurvive;
	private final Predicate<Player> predicate;

	public TimedObjective(Quest parent, Component titleId, float ticks, Predicate<Player> counts) {
		super(parent, titleId);
		this.timeToSurvive = (int) ticks;
		this.setMaxProgress(this.timeToSurvive);
		this.predicate = counts;
	}

	@Override
	public boolean checkTime(Player player) {
		if (!predicate.test(player)) {
			this.setProgress(player, 0, false);
			return false;
		}
		return true;
	}
}