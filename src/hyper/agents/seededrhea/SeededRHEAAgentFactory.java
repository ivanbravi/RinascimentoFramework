package hyper.agents.seededrhea;

import game.heuristics.Heuristic;
import hyper.agents.factory.HeuristicAgentFactory;
import ntbea.params.Param;
import players.BasePlayerInterface;
import players.ai.seeding.SeedingRHEA;

public class SeededRHEAAgentFactory extends HeuristicAgentFactory{

	private static int counter = 0;

	public SeededRHEAAgentFactory(){
		this.setAgentType("SeededRHEA"+counter++);
	}

	@Override
	public BasePlayerInterface agent(int[] solution) {
		SeedingRHEA agent = new SeedingRHEA();
		Param[] params = getSearchSpace().getParams();

		agent.flipAtLeastOneValue 	= (boolean)params[0].getValue(solution[0]);
		agent.mutationRate		 	= (double) params[1].getValue(solution[1]);
		agent.sequenceLength 		= ((Double)params[2].getValue(solution[2])).intValue();
		agent.nEvals 				= ((Double)params[3].getValue(solution[3])).intValue();
		agent.useShiftBuffer 		= (boolean)params[4].getValue(solution[4]);
		agent.opponentModelType 	= ((Double)params[5].getValue(solution[5])).intValue();
		agent.opponentBudgetShare 	= (double) params[6].getValue(solution[6]);

		agent.setName(getAgentType());
		agent.setHeuristic(getHeuristic());

		return agent;
	}

	@Override
	public BasePlayerInterface agent(int[] solution, Heuristic h) {
		SeedingRHEA agent = (SeedingRHEA) agent(solution);
		agent.setHeuristic(h);
		return agent;
	}
}
