package hyper.agents.mixed;

import game.heuristics.Heuristic;
import hyper.agents.factory.AgentFactorySpace;
import hyper.agents.factory.HeuristicAgentFactory;
import players.BasePlayerInterface;
import players.ai.explicit.ExplicitPlayerInterface;
import players.ai.explicit.SafeRandomPlayer;
import players.ai.explicit.mixer.MixerAgent;

public class MixerFactorySpace extends HeuristicAgentFactory {

	AgentFactorySpace afs;
	double weight;

	public MixerFactorySpace(AgentFactorySpace afs, double weight){
		this.afs = afs;
		this.weight = Math.max(0,Math.min(1,weight));
		this.setAgentType("Mixer-RND-"+afs.getAgentType());
	}

	@Override
	public BasePlayerInterface agent(int[] solution) {
		BasePlayerInterface agent = new MixerAgent(
				new ExplicitPlayerInterface[]{
						new SafeRandomPlayer(),
						(ExplicitPlayerInterface) afs.agent(solution)
				},
				new double[]{1-weight,weight});
		agent.setName(getAgentType());
		return agent;
	}

	@Override
	public BasePlayerInterface agent(int[] solution, Heuristic h) {
		MixerAgent agent = (MixerAgent) agent(solution);
		agent.setHeuristic(h);
		return agent;
	}
}
