package hyper.agents.rhea;


import game.heuristics.Heuristic;
import hyper.agents.factory.HeuristicAgentFactory;
import ntbea.params.*;
import players.BasePlayerInterface;
import players.ai.explicit.HyperRHEA;

public class RHEAAgentFactory extends HeuristicAgentFactory {

	private static int counter = 0;

	public RHEAAgentFactory(){
		setAgentType("RHEA"+counter++);
	}


	@Override
	public BasePlayerInterface agent(int[] solution){
		Param[] params = getSearchSpace().getParams();
		HyperRHEA testAgent = new HyperRHEA();

		testAgent.useShiftBuffer 		= (boolean)params[0].getValue(solution[0]);
		testAgent.sequenceLength 		= ((Double)params[1].getValue(solution[1])).intValue();
		testAgent.evals 				= ((Double)params[2].getValue(solution[2])).intValue();
		testAgent.flipAtLeastOneValue 	= (boolean)params[3].getValue(solution[3]);
		testAgent.mutationStyle 		= ((Double)params[4].getValue(solution[4])).intValue();
		testAgent.opponentType 			= ((Double)params[5].getValue(solution[5])).intValue();
		testAgent.opponentBudget 		= (double) params[6].getValue(solution[6]);
		testAgent.espMutationProb 		= (double) params[7].getValue(solution[7]);
		testAgent.gMean					= (double) params[8].getValue(solution[8]);
		testAgent.gStdDev				= (double) params[9].getValue(solution[9]);

		testAgent.setName(getAgentType());
		testAgent.setHeuristic(getHeuristic());

		return testAgent;
	}

	@Override
	public BasePlayerInterface agent(int[] solution, Heuristic h) {
		HyperRHEA agent = (HyperRHEA) agent(solution);
		agent.setHeuristic(h);
		return agent;
	}
}
