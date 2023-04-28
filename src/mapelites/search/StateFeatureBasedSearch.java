package mapelites.search;

import com.google.gson.JsonObject;
import game.Parameters;
import game.state.vectorise.Vectoriser;
import log.LogGroup;
import log.heuristics.WeightedHeuristic;
import mapelites.FitnessFunction;
import mapelites.interfaces.SolutionSpace;
import mapelites.statebased.SBAgentSolutionSpace;
import utils.loaders.LoadSearchSpace;
import java.util.Arrays;

public class StateFeatureBasedSearch extends PointBasedSearch {
	// Heuristic
	private String heuristicType;
	private String vectoriserType;
	private WeightedHeuristic h;
	private Vectoriser v;


	@Override
	public void init(JsonObject mapArgs, LogGroup lg) {
		super.init(mapArgs,lg);

		// vectoriser [S, P, SP, SPP, SSPP]
		// heuristic  [linear poly,2 poly,3 nn,TANH,5 nn,TANH,18,5,2]
		heuristicType = mapArgs.get("SB/heuristic").getAsString();
		vectoriserType = mapArgs.get("SB/vectoriser").getAsString();

		v = LoadSearchSpace.decodeVectoriser(vectoriserType, Parameters.load(this.gameVersion));
		h = LoadSearchSpace.decodeWeightedHeuristic(heuristicType, v.size());

		lg.add("heuristicSpace",h);
	}

	@Override
	public SolutionSpace getSolutionSpace() {
		SBAgentSolutionSpace ss = new SBAgentSolutionSpace(getAgentSpace(), h, v);
		ss.setAgentName(FitnessFunction.playerName);
		return ss;
	}

}
