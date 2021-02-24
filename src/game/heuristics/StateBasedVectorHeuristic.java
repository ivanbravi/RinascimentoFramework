package game.heuristics;

import game.BudgetExtendedGameState;
import game.state.vectorise.Vectoriser;
import log.heuristics.WeightedHeuristic;

public class StateBasedVectorHeuristic implements Heuristic {

	private static final double NORM_MAX=100;

	protected WeightedHeuristic h;
	protected Vectoriser sv;

	public StateBasedVectorHeuristic(WeightedHeuristic h, Vectoriser sv){
		this.h = h;
		this.sv = sv;
	}

	@Override
	public double value(BudgetExtendedGameState state, int playerID) {
		double[] features = sv.vectorise(state.getState(),playerID);
		normalise(features);
		return h.value(features);
	}

	private void normalise(double[] features){
		for(int i=0; i<features.length; i++){
			features[i] = features[i] / NORM_MAX;
		}
	}

	@Override
	public Heuristic clone() {
		return new StateBasedVectorHeuristic(h.clone(), sv.clone());
	}
}
