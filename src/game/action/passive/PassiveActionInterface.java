package game.action.passive;

import game.state.State;
import log.entities.action.LoggableAction;

public interface PassiveActionInterface extends LoggableAction {

	void perform(State s);
	PassiveActionInterface clone();

}
