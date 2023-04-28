package hyper.agents.featurebased;

import game.state.vectorise.Vectoriser;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import log.heuristics.WeightedHeuristic;
import players.BasePlayerInterface;

public class FeatureBasedFixedAgentFactory extends FeatureBasedAgentFactory {

	private int[] config;

	public FeatureBasedFixedAgentFactory(HeuristicAgentFactorySpace agentFactory, int[] config, WeightedHeuristic h, Vectoriser v) {
		super(agentFactory, h, v);
		this.config = config;
	}

	@Override
	public BasePlayerInterface agent(int[] solution) {
		return super.agent(config);
	}
}
