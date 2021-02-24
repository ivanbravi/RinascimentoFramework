package players;

import game.AbstractGameState;

public interface GeneralPlayerInterface extends BasePlayerInterface {
	int[] getActions(AbstractGameState gameState, int playerId);
}