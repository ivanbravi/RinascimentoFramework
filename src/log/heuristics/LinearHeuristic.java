package log.heuristics;

import java.util.Arrays;

public class LinearHeuristic implements WeightedHeuristic {

	double[] weights;
	int featuresSize;

	public LinearHeuristic(int featuresSize){
		this.featuresSize = featuresSize;
		weights = new double[featuresSize];
	}

	public LinearHeuristic(double[] w){
		this.featuresSize = w.length;
		setWeights(Arrays.copyOf(w, w.length));
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	@Override
	public double value(double[] features) {
		double acc = 0;
		if(features.length!=weights.length){
			throw new RuntimeException("Features and weights should have matching dimensions.");
		}
		for(int i=0; i<features.length; i++){
			acc += features[i]*weights[i];
		}
		return acc;
	}

	@Override
	public String toString() {
		return "featuresSize: "+featuresSize+" weights:"+Arrays.toString(weights);
	}

	@Override
	public int dimensionality() {
		return weights.length;
	}

	@Override
	public WeightedHeuristic clone() {
		return new LinearHeuristic(this.weights);
	}
}
