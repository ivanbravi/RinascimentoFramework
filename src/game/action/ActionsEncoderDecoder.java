package game.action;


import game.state.State;

public interface ActionsEncoderDecoder {
	void addManagedActionType(ActionType at);
	int countActions(State gs,int playerId);
	PlayableAction decodeAction(int actionId, State gs, int playerID);
}
