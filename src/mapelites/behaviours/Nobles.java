package mapelites.behaviours;

import game.AbstractGameState;
import game.state.State;
import statistics.GameStats;
import statistics.player.PlayerNumericalStatistic;
import statistics.types.StatisticInterface;

public class Nobles extends PlayerNumericalStatistic {

	public Nobles(){}

	public Nobles(double value) {
		super(value);
	}

	@Override
	public StatisticInterface clone() {
		Nobles clone = new Nobles();
		clone.copy(this);
		return clone;
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		if(gs instanceof State){
			State s = (State) gs;
			int pId =  s.getPlayerId(player);
			Nobles c = new Nobles(s.getPlayerState(pId).nobles.size());
			c.setPlayer(player);
			return c;
		}
		return null;
	}
}
