package mapelites.behaviours;

import game.AbstractGameState;
import game.state.State;
import statistics.GameStats;
import statistics.player.PlayerNumericalStatistic;
import statistics.types.StatisticInterface;

public class GameDuration  extends PlayerNumericalStatistic {

	@Override
	public StatisticInterface clone() {
		GameDuration clone = new GameDuration();
		clone.copy(this);
		return clone;
	}

	public GameDuration(){}
	public GameDuration(double value){super(value);}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		if(gs.isTerminal()){
			return new GameDuration(((State)gs).getTick());
		}
		return null;
	}
}
