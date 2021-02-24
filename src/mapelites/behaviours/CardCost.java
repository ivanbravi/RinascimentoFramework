package mapelites.behaviours;

import game.AbstractGameState;
import game.state.State;
import log.entities.event.EventLogger;
import mapelites.loggers.PlayerCardCostLogger;
import statistics.GameStats;
import statistics.player.PlayerNumericalStatistic;
import statistics.types.StatisticInterface;

public class CardCost extends PlayerNumericalStatistic implements LoggingStatistic {

	public static String loggerName = "cardCostLogger:";

	public CardCost(){}
	public CardCost(double value){super(value);}

	public String getPlayerLoggerName(String player){
		return loggerName+player;
	}

	@Override
	public EventLogger getLoggerInstance(String playerName) {
		return new PlayerCardCostLogger(playerName);
	}

	@Override
	public StatisticInterface clone() {
		CardCost clone = new CardCost();
		clone.copy(this);
		return clone;
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		if(gs instanceof State){
			State s=(State) gs;
			PlayerCardCostLogger l = (PlayerCardCostLogger) s.eventLogger(getPlayerLoggerName(player));
			CardCost c = new CardCost(l.getCost());
			c.setPlayer(player);
			return c;
		}
		return null;
	}
}
