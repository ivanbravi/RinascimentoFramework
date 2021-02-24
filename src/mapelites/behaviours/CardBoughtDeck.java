package mapelites.behaviours;

import game.AbstractGameState;
import game.state.State;
import log.entities.event.EventLogger;
import mapelites.loggers.PlayerCardDeckLogger;
import statistics.GameStats;
import statistics.player.PlayerNumericalStatistic;
import statistics.types.StatisticInterface;

public class CardBoughtDeck extends PlayerNumericalStatistic implements LoggingStatistic {

	public static String loggerName = "deckLogger:";

	public CardBoughtDeck(){}
	public CardBoughtDeck(double value){super(value);}

	public String getPlayerLoggerName(String player){
		return loggerName+player;
	}

	@Override
	public EventLogger getLoggerInstance(String playerName) {
		return new PlayerCardDeckLogger(playerName);
	}

	@Override
	public StatisticInterface clone() {
		CardBoughtDeck clone = new CardBoughtDeck();
		clone.copy(this);
		return clone;
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		if(gs instanceof State){
			State s = (State) gs;
			PlayerCardDeckLogger l = (PlayerCardDeckLogger) s.eventLogger(getPlayerLoggerName(player));
			CardBoughtDeck d = new CardBoughtDeck(l.getCardDeckId());
			d.setPlayer(player);
			return d;
		}

		return null;
	}
}
