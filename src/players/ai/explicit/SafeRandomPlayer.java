package players.ai.explicit;

import game.BudgetExtendedGameState;
import game.ExtendedGameState;
import game.action.Action;
import game.exceptions.StaleGameException;
import game.action.PlayableAction;
import players.BasePlayerInterface;

public class SafeRandomPlayer extends RandomPlayer{

	public SafeRandomPlayer(){
		name = "SafeRandomPlayer";
	}

	@Override
	public Action[] getActions(BudgetExtendedGameState gameState, int playerId) {
		try {
			return super.getActions(gameState, playerId);
		}catch (StaleGameException e){

		}

		return new Action[]{null};
	}

	@Override
	public BasePlayerInterface clone() {
		return super.clone();
	}
}
