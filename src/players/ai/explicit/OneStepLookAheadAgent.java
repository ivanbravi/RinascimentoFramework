package players.ai.explicit;

import game.BudgetExtendedGameState;
import game.action.Action;
import game.heuristics.PointsHeuristic;
import players.BasePlayerInterface;
import players.BudgetOverException;
import players.HeuristicBasedPlayerInterface;


public class OneStepLookAheadAgent extends HeuristicBasedPlayerInterface implements ExplicitPlayerInterface {

	public OneStepLookAheadAgent(){
		this.setHeuristic(new PointsHeuristic());
		setName("OneStepLookAhead");
	}

	@Override
	public Action[] getActions(BudgetExtendedGameState gameState, int playerId) {
		this.h.ground(gameState,playerId);
		this.setId(playerId);
		Action bestAction = null;

		try{
			double bestValue=0, newValue;
			Action newAction = gameState.getRandomAction(playerId);

			do{
				newValue = utility((gameState.copy().perform(newAction)));
				if(bestAction==null || newValue>bestValue){
					bestAction = newAction;
					bestValue = newValue;
				}
			}while(newAction!=null);

		}catch (BudgetOverException e){}

		return new Action[]{bestAction};
	}

	@Override
	public BasePlayerInterface reset() {
		return this;
	}

	@Override
	public double utility() {
		return 0;
	}

	@Override
	public HeuristicBasedPlayerInterface clone() {
		OneStepLookAheadAgent clone = new OneStepLookAheadAgent();
		clone.setHeuristic(this.h.clone());
		clone.setName(this.name);
		clone.setId(this.id);
		return clone;
	}
}
