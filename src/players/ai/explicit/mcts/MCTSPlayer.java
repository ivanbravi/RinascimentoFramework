package players.ai.explicit.mcts;

import game.BudgetExtendedGameState;
import game.action.Action;
import players.BasePlayerInterface;
import players.HeuristicBasedPlayerInterface;
import players.ai.explicit.ExplicitPlayerInterface;

public class MCTSPlayer extends HeuristicBasedPlayerInterface implements ExplicitPlayerInterface {

	public static boolean VERBOSE = false;

	/*--------- PARAMS ---------*/
	public double exploration = 1.41;
	public int maxDepth = 20;
	public int opponentModel = 0;
	public double opponentBudgetRatio = 0.05;
	public double expansionProbability = 0.2;
	public int progressionSize = 10;
	public double epsilon = 1e-6;
	public int recommendationType = 2;
	public boolean rollWithOpponents = false;
	/*--------------------------*/

	public MCTSPlayer(){
		this.setName("MCTS");
	}

	@Override
	public HeuristicBasedPlayerInterface clone() {
		MCTSPlayer clone = new MCTSPlayer();

		clone.exploration = this.exploration;
		clone.maxDepth = this.maxDepth;
		clone.opponentModel = this.opponentModel;
		clone.opponentBudgetRatio = this.opponentBudgetRatio;
		clone.expansionProbability = this.expansionProbability;
		clone.progressionSize = this.progressionSize;
		clone.epsilon = this.epsilon;
		clone.recommendationType = this.recommendationType;
		clone.rollWithOpponents = this.rollWithOpponents;

		clone.setHeuristic(this.h.clone());
		clone.setName(this.name);
		clone.setId(this.id);

		return clone;
	}

	@Override
	public Action[] getActions(BudgetExtendedGameState gameState, int playerId) {
		MCTSRootNode.VERBOSE = VERBOSE;
		MCTSRootNode root = new MCTSRootNode(this.h);
		root.setExploration(exploration).
				setDepth(maxDepth).
				setOpponentModel(opponentModel).
				setOpponentBudgetRatio(opponentBudgetRatio).
				setExpansionProbability(expansionProbability).
				setProgressionSize(progressionSize).
				setUCBEpsilon(epsilon).
				setRecommendationType(recommendationType).
				setRollWithOpponents(rollWithOpponents);
		root.search(gameState,playerId);
		Action retAction = root.suggestedAction();
		if(retAction==null){
			retAction = gameState.getRandomAction(playerId);
		}
		return new Action[]{retAction};
	}

	@Override
	public BasePlayerInterface reset() {
		return this;
	}

	@Override
	public double utility() {
		return 0;
	}
}
