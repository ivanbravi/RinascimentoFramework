package log.entities.event;

import game.log.converters.EventIdConverter;

public abstract class EventConvertedLogger implements EventLogger {
	protected EventIdConverter converter;

	public EventConvertedLogger(EventIdConverter converter){
		this.converter = converter;
	}

	@Override
	public final void logEvent(Event e) {
		Event convertedEvent = new Event(
				converter.convertName(e.type()),
				e.tick(),
				e.actuator(),
				converter.convert(e.type()),
				e.duration(),
				e.durationType(),
				e.attributes(),
				e.signature(),
				e.triggeredBy()
		);
		manageEvent(convertedEvent);
	}

	public abstract void manageEvent(Event e);

}
