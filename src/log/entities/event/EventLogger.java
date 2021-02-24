package log.entities.event;

public interface EventLogger {

	void logEvent(Event e);
	void reset();
	EventLogger copy();

	void setMaxEventId(int maxEventId);

}
