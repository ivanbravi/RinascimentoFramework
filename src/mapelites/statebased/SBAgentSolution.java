package mapelites.statebased;

import game.heuristics.StateBasedVectorHeuristic;
import game.state.vectorise.Vectoriser;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import log.heuristics.WeightedHeuristic;
import mapelites.PlayerSolution;
import players.BasePlayerInterface;

import java.util.Arrays;

public class SBAgentSolution extends PlayerSolution {

	private int[] agentConfig;
	private double[] weights;
	transient private HeuristicAgentFactorySpace factory;
	transient private WeightedHeuristic heuristic;
	transient private Vectoriser vectoriser;
	private String playerName;

	SBAgentSolution(String playerName, int[] agentConfig, double[] weights, HeuristicAgentFactorySpace factory, WeightedHeuristic heuristic, Vectoriser vectoriser){
		this.agentConfig = agentConfig;
		this.weights = weights;
		this.playerName = playerName;
		this.factory = factory;
		this.heuristic = heuristic;
		this.vectoriser = vectoriser;
	}

	public int[] getAgentConfig(){
		return agentConfig;
	}

	public double[] getWeights() {
		return weights;
	}

	@Override
	public BasePlayerInterface getPlayer() {
		StateBasedVectorHeuristic sbv = new StateBasedVectorHeuristic(heuristic, vectoriser);
		BasePlayerInterface agent = factory.agent(agentConfig, sbv);
		agent.setName(playerName);
		return agent;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(factory.getClass().getSimpleName()).
				append(" ").append(Arrays.toString(agentConfig)).
				append(" ").append(heuristic.getClass().getSimpleName()).
				append(" ").append(Arrays.toString(weights)).
				append(" ").append(vectoriser.getClass().getSimpleName());

		return builder.toString();
	}

}
