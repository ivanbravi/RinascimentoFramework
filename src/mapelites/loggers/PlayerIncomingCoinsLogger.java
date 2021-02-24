package mapelites.loggers;

import game.log.converters.EventIdConverter;
import game.log.converters.IdentityConverter;
import log.entities.event.Event;
import log.entities.event.EventLogger;

public class PlayerIncomingCoinsLogger implements EventLogger {

	private int coinCount = 0;
	private EventIdConverter c = new IdentityConverter();
	private String player;

	public PlayerIncomingCoinsLogger(String player){
		this.player = player;
	}

	private PlayerIncomingCoinsLogger(){}

	@Override
	public void logEvent(Event e) {
		if(e.actuator().equals(player))
		if(c.isIncomingCoin(e)){
			int amount = (int) e.attributes().get("amount");
			coinCount += amount;
		}
	}

	public int getCoinCount(){
		return coinCount;
	}

	@Override
	public void reset() {
		coinCount = 0;
	}

	@Override
	public EventLogger copy() {
		PlayerIncomingCoinsLogger copy = new PlayerIncomingCoinsLogger();
		copy.c = this.c;
		copy.player = this.player;
		copy.coinCount = this.coinCount;
		return copy;
	}

	@Override
	public void setMaxEventId(int maxEventId) {}
}
