package mapelites.loggers;

import game.log.converters.EventIdConverter;
import game.log.converters.IdentityConverter;
import log.entities.event.Event;
import log.entities.event.EventLogger;
import statistics.types.NumericalStatistic;

public class PlayerCardDeckLogger implements EventLogger {
	private static EventIdConverter converter = new IdentityConverter();

	private NumericalStatistic deckIdStat;
	private String player;

	public PlayerCardDeckLogger(String player){
		this.player = player;
		deckIdStat = new NumericalStatistic();
	}

	@Override
	public void logEvent(Event e) {
		if(e.actuator().equals(player)){
			if(converter.isGetCard(e)){
				int deckId = (int) e.triggeredBy().parameters().get("deckId");
				deckIdStat.add(new NumericalStatistic(deckId));
			}
		}
	}

	public double getCardDeckId(){
		return deckIdStat.value();
	}

	@Override
	public void reset() {
		this.deckIdStat.reset();
	}

	@Override
	public EventLogger copy() {
		PlayerCardDeckLogger copy = new PlayerCardDeckLogger(this.player);
		copy.deckIdStat.copy(this.deckIdStat);
		return copy;
	}

	@Override
	public void setMaxEventId(int maxEventId) {

	}
}
