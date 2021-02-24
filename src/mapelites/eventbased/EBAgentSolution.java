package mapelites.eventbased;

import game.log.converters.EventIdConverter;
import log.entities.event.PlayerEventVectorLogger;
import mapelites.PlayerSolution;
import game.heuristics.LogBasedVectorHeuristic;
import hyper.agents.factory.HeuristicAgentFactory;
import log.heuristics.WeightedHeuristic;
import players.BasePlayerInterface;

import java.util.Arrays;

public class EBAgentSolution extends PlayerSolution {

	private int[] agentConfig;
	private double[] weights;
	transient private HeuristicAgentFactory factory;
	transient private WeightedHeuristic heuristic;
	private String playerName;
	private EventIdConverter converter;

	public EBAgentSolution(String playerName, int[] agentConfig, double[] weights, HeuristicAgentFactory factory, WeightedHeuristic heuristic, EventIdConverter converter){
		this.playerName = playerName;
		this.agentConfig = agentConfig;
		this.weights = weights;
		this.factory = factory;
		this.heuristic = heuristic.clone();
		this.heuristic.setWeights(weights);
		this.converter = converter;
	}

	public int[] getAgentConfig(){
		return agentConfig;
	}

	public double[] getWeights() {
		return weights;
	}

	@Override
	public BasePlayerInterface getPlayer() {
		LogBasedVectorHeuristic compatibleHeuristic = new LogBasedVectorHeuristic(heuristic, new PlayerEventVectorLogger(converter));
		BasePlayerInterface agent = factory.agent(agentConfig,compatibleHeuristic);
		agent.setName(playerName);
		return agent;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(factory.getClass().getSimpleName()).
				append(" ").append(Arrays.toString(agentConfig)).
				append(" ").append(heuristic.getClass().getSimpleName()).
				append(" ").append(Arrays.toString(weights));

		return builder.toString();
	}
}
