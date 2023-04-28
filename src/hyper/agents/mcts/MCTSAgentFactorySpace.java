package hyper.agents.mcts;

import game.heuristics.Heuristic;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import ntbea.params.Param;
import players.BasePlayerInterface;
import players.ai.explicit.mcts.MCTSPlayer;

public class MCTSAgentFactorySpace extends HeuristicAgentFactorySpace {

	private static int counter = 0;

	public MCTSAgentFactorySpace(){
		setAgentType("MCTS"+counter++);
	}

	@Override
	public BasePlayerInterface agent(int[] solution) {
		Param[] params = getSearchSpace().getParams();
		MCTSPlayer agent = new MCTSPlayer();

		agent.exploration			=  (double)params[0].getValue(solution[0]);
		agent.maxDepth				= ((Double)params[1].getValue(solution[1])).intValue();
		agent.opponentModel			= ((Double)params[2].getValue(solution[2])).intValue();
		agent.opponentBudgetRatio	=  (double)params[3].getValue(solution[3]);
		agent.expansionProbability	=  (double)params[4].getValue(solution[4]);
		agent.progressionSize		= ((Double)params[5].getValue(solution[5])).intValue();
		agent.epsilon				=  (double)params[6].getValue(solution[6]);
		agent.recommendationType	= ((Double)params[7].getValue(solution[7])).intValue();
		agent.rollWithOpponents		= (boolean)params[8].getValue(solution[8]);

		agent.setName(getAgentType());
		agent.setHeuristic(getHeuristic());

		return agent;
	}

	@Override
	public BasePlayerInterface agent(int[] solution, Heuristic h) {
		MCTSPlayer agent = (MCTSPlayer) agent(solution);
		agent.setHeuristic(h);
		return agent;
	}
}
