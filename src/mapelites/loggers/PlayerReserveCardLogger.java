package mapelites.loggers;

import game.log.converters.EventIdConverter;
import game.log.converters.IdentityConverter;
import log.entities.event.Event;
import log.entities.event.EventLogger;

public class PlayerReserveCardLogger implements EventLogger {

	private int reserveCount = 0;
	private EventIdConverter c = new IdentityConverter();
	private String pID;

	public PlayerReserveCardLogger(String pID){
		this.pID = pID;
	}

	private PlayerReserveCardLogger(){}

	@Override
	public void logEvent(Event e) {
		if(e.actuator().equals(pID))
			if(c.isReserve(e))
				reserveCount++;
	}

	public int getReserveCount(){
		return reserveCount;
	}

	@Override
	public void reset() {
		reserveCount = 0;
	}

	@Override
	public EventLogger copy() {
		PlayerReserveCardLogger copy = new PlayerReserveCardLogger();

		copy.c = this.c;
		copy.reserveCount = this.reserveCount;
		copy.pID = this.pID;

		return copy;
	}

	@Override
	public void setMaxEventId(int maxEventId) {}
}
