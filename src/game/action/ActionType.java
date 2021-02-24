package game.action;

import game.state.State;

public interface ActionType {

	int countActions(State gs, int playerId);
	PlayableAction createAction(int actionId, State gs, int playerId);

}
