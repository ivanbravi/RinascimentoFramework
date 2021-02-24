package mapelites.behaviours;

import game.AbstractGameState;
import game.state.Result;
import game.state.State;
import statistics.GameStats;
import statistics.player.PlayerNumericalStatistic;
import statistics.types.StatisticInterface;

public class CardCount extends PlayerNumericalStatistic {


	public CardCount(){}

	public CardCount(double value) {
		super(value);
	}

	@Override
	public StatisticInterface clone() {
		CardCount clone = new CardCount();
		clone.copy(this);
		return clone;
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		if(gs instanceof State){
			State s = (State) gs;
			Result r = new Result(s);
			int playerID = s.getPlayerId(player);
			CardCount c = new CardCount(r.cardsCount[playerID]);
			c.setPlayer(player);
			return c;
		}
		return null;
	}
}
