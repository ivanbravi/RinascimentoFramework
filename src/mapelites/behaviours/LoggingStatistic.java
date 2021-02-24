package mapelites.behaviours;

import log.entities.event.EventLogger;

public interface LoggingStatistic {
	String getPlayerLoggerName(String playerName);
	EventLogger getLoggerInstance(String playerName);
}
