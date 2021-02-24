package hyper.agents.featurebased;

import game.state.vectorise.Vectoriser;
import hyper.agents.factory.HeuristicAgentFactory;
import log.heuristics.WeightedHeuristic;
import players.BasePlayerInterface;

public class FeatureBasedFixedAgentFactory extends FeatureBasedAgentFactory {

	private int[] config;

	public FeatureBasedFixedAgentFactory(HeuristicAgentFactory agentFactory, int[] config, WeightedHeuristic h, Vectoriser v) {
		super(agentFactory, h, v);
		this.config = config;
	}

	@Override
	public BasePlayerInterface agent(int[] solution) {
		return super.agent(config);
	}
}
