package players.ai.explicit;

import game.BudgetExtendedGameState;
import game.ExtendedGameState;
import game.action.Action;
import game.action.PlayableAction;
import players.BasePlayerInterface;

public interface ExplicitPlayerInterface extends BasePlayerInterface {
	Action[] getActions(BudgetExtendedGameState gameState, int playerId);
}
