package hyper.agents.meta;

import game.heuristics.Heuristic;
import hyper.agents.factory.AgentFactorySpace;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import ntbea.params.Param;
import players.BasePlayerInterface;
import players.ai.meta.MetaTuningAgent;

public class MetaAgentFactorySpace extends HeuristicAgentFactorySpace {

	private static int counter = 0;

	private AgentFactorySpace meta;

	public MetaAgentFactorySpace(AgentFactorySpace afs){
		this.meta = afs;
		setAgentType("Meta-"+afs.getAgentType()+"-"+counter++);
	}

	@Override
	public BasePlayerInterface agent(int[] solution) {
		Param[] params = getSearchSpace().getParams();
		MetaTuningAgent agent = new MetaTuningAgent(meta);

		agent.tuningTurns = ((Double)params[0].getValue(solution[0])).intValue();
		agent.rollsDepth  = ((Double)params[1].getValue(solution[1])).intValue();
		agent.eps 		  = (double) params[2].getValue(solution[2]);
		agent.exp 		  = (double) params[3].getValue(solution[3]);
		agent.ratio		  = (double) params[4].getValue(solution[4]);

		agent.setName(getAgentType());
		agent.setHeuristic(getHeuristic());

		return agent;
	}

	@Override
	public BasePlayerInterface agent(int[] solution, Heuristic h) {
		this.setHeuristic(h);
		return agent(solution);
	}
}
