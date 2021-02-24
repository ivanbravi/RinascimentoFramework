package log.heuristics.nn;

public interface NNInterface {

	void setWeights(double[] w);
	int weightsCount();
	void compute(double[] input, double[] output);

}
