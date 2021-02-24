package log.entities.action;

public interface LoggableAction {

	default Action getActionLog(){return null;}

}
