package mapelites.eventbased;

import game.log.converters.EventIdConverter;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import log.heuristics.WeightedHeuristic;
import log.heuristics.weights.UniformWeight;
import log.heuristics.weights.WeightGenerator;
import mapelites.interfaces.Solution;
import mapelites.interfaces.SolutionSpace;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

public class EBAgentSolutionSpace implements SolutionSpace {

	private String agentName;
	private Random rnd = new Random();
	private HeuristicAgentFactorySpace agentSpace;
	private WeightedHeuristic heuristic;
	private WeightGenerator wGenerator = new UniformWeight(-1,1);
	private EventIdConverter converter;

	public EBAgentSolutionSpace(HeuristicAgentFactorySpace agentSpace, WeightedHeuristic heuristic, EventIdConverter converter){
		this.agentSpace = agentSpace;
		this.heuristic = heuristic;
		this.converter = converter;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public void setWeightGenerator(WeightGenerator wGenerator) {
		this.wGenerator = wGenerator;
	}

	@NotNull
	@Override
	public Solution crossover(@NotNull Solution solution, @NotNull Solution solution1) {
		return null;
	}

	@NotNull
	@Override
	public Solution mutate(@NotNull Solution solution) {
		if(!(solution instanceof EBAgentSolution))
			return null;
		EBAgentSolution currSolution = (EBAgentSolution) solution;
		int[] agentConfig = currSolution.getAgentConfig();
		double[] weightsConfig = currSolution.getWeights();
		if(rnd.nextDouble()>0.5){
			agentConfig = mutateAgent(agentConfig);
		}else{
			weightsConfig = mutateWeights(weightsConfig);
		}
		return new EBAgentSolution(
				agentName,
				agentConfig,
				weightsConfig,
				agentSpace,
				heuristic,
				converter);
	}

	private int[] mutateAgent(int[] currAgent){
		int[] mutation = Arrays.copyOf(currAgent,currAgent.length);
		int mPosition = rnd.nextInt(currAgent.length);
		int mValue = rnd.nextInt(agentSpace.getSearchSpace().nValues(mPosition));
		mutation[mPosition] = mValue;
		return mutation;
	}

	private double[] mutateWeights(double[] currWeights){
		double[] mutation = Arrays.copyOf(currWeights, currWeights.length);
		int mPosition = rnd.nextInt(currWeights.length);
		double mValue = wGenerator.weight();
		mutation[mPosition] = mValue;
		return mutation;
	}

	@NotNull
	@Override
	public Solution rndPoint() {
		int[] rndAgent = this.agentSpace.randomPoint(rnd);
		double[] rndWeights = wGenerator.weights(heuristic.dimensionality());
		WeightedHeuristic rndHeuristic = heuristic.clone();
		rndHeuristic.setWeights(rndWeights);
		EBAgentSolution solution = new EBAgentSolution(
				agentName,
				rndAgent,
				rndWeights,
				agentSpace,
				heuristic,
				converter);
		return solution;
	}
}
