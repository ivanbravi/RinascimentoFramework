package hyper.agents.eventbased;

import game.log.converters.EventIdConverter;
import hyper.agents.factory.HeuristicAgentFactory;
import log.heuristics.WeightedHeuristic;
import players.BasePlayerInterface;

public class EventBasedFixedAgentFactory extends EventBasedAgentFactory {

	private int[] config;

	public EventBasedFixedAgentFactory(HeuristicAgentFactory agentFactory, int[] config, WeightedHeuristic h, EventIdConverter converter) {
		super(agentFactory, h, converter);
		this.config = config;
	}

	@Override
	public BasePlayerInterface agent(int[] solution) {
		return super.agent(config);
	}
}
