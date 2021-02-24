package log.heuristics.weights;

public interface WeightGenerator {
	void setSeed(long seed);
	double weight();
	double[] weights(int size);
}
