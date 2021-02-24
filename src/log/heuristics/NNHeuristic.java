package log.heuristics;

import log.heuristics.nn.NNInterface;
import log.heuristics.nn.NNCreator;


public class NNHeuristic implements WeightedHeuristic {

	NNCreator creator;
	transient NNInterface nn;
	double[] w;
	int size;
	double[] result = new double[1];

	public NNHeuristic(NNCreator creator){
		this.creator = creator;
		nn = creator.create();
		size = nn.weightsCount();
	}

	@Override
	public double value(double[] features) {
		nn.compute(features, result);
		return result[0];
	}

	@Override
	public int dimensionality() {
		return size;
	}

	@Override
	public void setWeights(double[] w) {
		if(w.length!=size) {
			System.out.println("Invalid NN weights size! Expected:" + size + ", received:" + w.length);
			return;
		}
		this.w = w;
		nn.setWeights(w);
	}

	@Override
	public WeightedHeuristic clone() {
		NNHeuristic clone = new NNHeuristic(creator);
		if(this.w!=null) {
			clone.w = this.w;
			clone.setWeights(this.w);
		}
		return clone;
	}
}
