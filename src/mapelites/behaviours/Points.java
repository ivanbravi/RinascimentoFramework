package mapelites.behaviours;

import game.AbstractGameState;
import game.state.Result;
import game.state.State;
import statistics.GameStats;
import statistics.player.PlayerNumericalStatistic;
import statistics.types.StatisticInterface;

public class Points extends PlayerNumericalStatistic {

	public Points(){}

	public Points(double value) {
		super(value);
	}

	@Override
	public StatisticInterface clone() {
		Points clone = new Points();
		clone.copy(this);
		return clone;
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		if(gs instanceof State){
			State s = (State) gs;
			Result r = new Result(s);
			int pId = s.getPlayerId(this.getPlayer());
			Points c = new Points(r.points[pId]);
			c.setPlayer(this.getPlayer());
			return c;
		}
		return null;
	}
}
