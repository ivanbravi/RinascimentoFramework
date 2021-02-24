package game.action;

import game.state.State;
import log.entities.action.LoggableAction;

public interface Action extends LoggableAction{
	boolean canPerform(State s);
	int getPlayerId();
}
