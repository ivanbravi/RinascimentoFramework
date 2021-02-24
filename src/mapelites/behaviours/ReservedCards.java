package mapelites.behaviours;

import log.entities.event.EventLogger;
import mapelites.loggers.PlayerReserveCardLogger;
import game.AbstractGameState;
import game.state.State;
import statistics.GameStats;
import statistics.player.PlayerNumericalStatistic;

public class ReservedCards extends PlayerNumericalStatistic implements LoggingStatistic {

	public static String loggerName = "reserveLogger:";

	public ReservedCards(){}

	public ReservedCards(double value) {
		super(value);
	}

	public String getPlayerLoggerName(String player){
		return loggerName+player;
	}

	@Override
	public EventLogger getLoggerInstance(String playerName) {
		return new PlayerReserveCardLogger(playerName);
	}

	public PlayerNumericalStatistic clone(){
		ReservedCards clone = new ReservedCards();

		clone.copy(this);
		clone.loggerName = this.loggerName;

		return clone;
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		if(gs instanceof State){
			State s = (State) gs;
			PlayerReserveCardLogger l = (PlayerReserveCardLogger) s.eventLogger(getPlayerLoggerName(player));
			ReservedCards c = new ReservedCards(l.getReserveCount());
			c.setPlayer(player);
			return c;
		}
		return null;
	}
}