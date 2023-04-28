package hyper.agents.eventbased;

import game.heuristics.LogBasedVectorHeuristic;
import game.log.converters.EventIdConverter;
import hyper.agents.factory.AgentFactory;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import log.entities.event.PlayerEventVectorLogger;
import log.heuristics.WeightedHeuristic;
import players.BasePlayerInterface;
import utils.loaders.LoadSearchSpace;

public class EventBasedSingleAgentFreeWeightsFactory implements AgentFactory {

	private static final String name="EB-Single-FreeWeights";
	private static int count = 0;

	protected EventIdConverter idConverter;
	protected HeuristicAgentFactorySpace afs;
	protected int[] algorithmConfig;
	protected double[] weights;
	protected String heuristicType;

	protected String playerName;

	public EventBasedSingleAgentFreeWeightsFactory(String heuristicType,
											String converterType,
											HeuristicAgentFactorySpace afs,
											int[] algorithmConfig,
											double[] weights){
		this.afs = afs;
		this.heuristicType = heuristicType;
		this.algorithmConfig = algorithmConfig;
		this.weights = weights;
		this.idConverter = LoadSearchSpace.loadConverter(converterType);
		this.playerName = getPlayerName();
	}

	private static String getPlayerName(){
		return name+"["+(count++)+"]";
	}

	@Override
	public BasePlayerInterface agent() {
		PlayerEventVectorLogger eventLogger = new PlayerEventVectorLogger(idConverter);
		WeightedHeuristic heuristic = LoadSearchSpace.decodeWeightedHeuristicWithWeights(heuristicType, idConverter.idCount(),weights);
		LogBasedVectorHeuristic compatibleHeuristic = new LogBasedVectorHeuristic(heuristic, eventLogger);
		BasePlayerInterface agent = afs.agent(algorithmConfig,compatibleHeuristic);
		agent.setName(this.playerName);
		return agent;
	}
}
