package mapelites;

import mapelites.interfaces.Solution;
import players.BasePlayerInterface;
import statistics.PlayerStatsWrapper;

public abstract class PlayerSolution implements Solution {

	public static boolean KEEPISTORY = true;
	protected PlayerStatsWrapper statsWithHistory;

	abstract public BasePlayerInterface getPlayer();

	public void setStatsWithHistory(PlayerStatsWrapper statsWithHistory) {
		if(KEEPISTORY)
			this.statsWithHistory = statsWithHistory;
	}
}
