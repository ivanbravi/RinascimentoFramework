package mapelites.behaviours;

import log.entities.event.EventLogger;
import mapelites.FitnessFunction;
import mapelites.loggers.PlayerIncomingCoinsLogger;
import game.AbstractGameState;
import game.state.State;
import statistics.GameStats;
import statistics.player.PlayerNumericalStatistic;
import statistics.types.StatisticInterface;

public class TotalCoins extends PlayerNumericalStatistic implements LoggingStatistic {

	public static final String loggerName = "coinLogger:";

	public TotalCoins(){}

	public TotalCoins(double value){
		super(value);
	}

	public String getPlayerLoggerName(String playerName){
		return loggerName+playerName;
	}

	@Override
	public EventLogger getLoggerInstance(String playerName) {
		return new PlayerIncomingCoinsLogger(playerName);
	}

	@Override
	public StatisticInterface clone() {
		TotalCoins clone = new TotalCoins();
		clone.copy(this);
		return clone;
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		if(gs instanceof State){
			State s = (State) gs;
			PlayerIncomingCoinsLogger l = (PlayerIncomingCoinsLogger) s.eventLogger(getPlayerLoggerName(player));
			TotalCoins c = new TotalCoins(l.getCoinCount());
			c.setPlayer(player);
			return c;
		}

		return null;
	}
}
