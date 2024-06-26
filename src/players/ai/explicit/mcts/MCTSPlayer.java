package players.ai.explicit.mcts;

import game.BudgetExtendedGameState;
import game.action.Action;
import game.state.PlayerState;
import players.BasePlayerInterface;
import players.HeuristicBasedPlayerInterface;
import players.ai.explicit.ExplicitPlayerInterface;
import utils.math.permutation.P;

import java.util.Arrays;
import java.util.HashMap;

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
	public boolean useAMAFStatistics = false;
	public double adaptiveRollout = 0;
	public double progressiveWidening = 0;
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
		clone.adaptiveRollout = this.adaptiveRollout;
		clone.progressiveWidening = this.progressiveWidening;
		clone.useAMAFStatistics = this.useAMAFStatistics;

		clone.setHeuristic(this.h.clone());
		clone.setName(this.name);
		clone.setId(this.id);

		return clone;
	}

	@Override
	public Action[] getActions(BudgetExtendedGameState gameState, int playerId) {
		MCTSRootNode.VERBOSE = VERBOSE;
		MCTSRootNode root = new MCTSRootNode(this.h);

		double true_rollout = maxDepth;
		if (adaptiveRollout > 0)
		{
			true_rollout = getAdaptedRolloutDepth(gameState);
		}

		root.setExploration(exploration).
				setDepth((int)true_rollout).
				setOpponentModel(opponentModel).
				setOpponentBudgetRatio(opponentBudgetRatio).
				setExpansionProbability(expansionProbability).
				setProgressionSize(progressionSize).
				setUCBEpsilon(epsilon).
				setRecommendationType(recommendationType).
				setRollWithOpponents(rollWithOpponents).
				setProgressiveWideningFactor(progressiveWidening).
				setUseAMAFStatistics(useAMAFStatistics);

		BudgetExtendedGameState curr_state = gameState.copy();
		root.search(curr_state,playerId);
		Action retAction = root.suggestedAction();
		if(retAction==null){
			retAction = curr_state.getRandomAction(playerId);
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

	private double getAdaptedRolloutDepth(BudgetExtendedGameState gameState)
	{
		PlayerState[] playerStates = gameState.getState().playerStates;
		int max_points = Arrays.stream(playerStates).max((o1, o2) -> o1.points - o2.points).get().points;
		if (max_points >= 8) {
			return adaptiveRollout * maxDepth;
		} else {
			return maxDepth;
		}

	}
}
