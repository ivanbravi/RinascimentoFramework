package players.ai.general;

import game.AbstractGameState;
import players.BasePlayerInterface;

public interface GeneralPlayerInterface extends BasePlayerInterface {
	int[] getAction(AbstractGameState gameState, int playerId);
}