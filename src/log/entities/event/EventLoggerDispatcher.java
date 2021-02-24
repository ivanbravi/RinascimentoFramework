package log.entities.event;

public interface EventLoggerDispatcher {

	EventLogger getLogger(String o);
	void addLogger(String o, EventLogger e);

}
