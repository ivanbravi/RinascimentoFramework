package log.heuristics;

public interface WeightedHeuristic {

	double value(double[] features);
	int dimensionality();
	void setWeights(double[] w);

	WeightedHeuristic clone();
}
