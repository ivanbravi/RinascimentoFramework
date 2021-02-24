package log.heuristics.weights;

import java.util.Random;

public class UniformWeight implements WeightGenerator {

	private double min,max;
	private Random rnd = new Random();

	public UniformWeight(double min, double max){
		this.min = min;
		this.max = max;
	}

	@Override
	public void setSeed(long seed) {
		rnd.setSeed(seed);
	}

	@Override
	public double weight() {
		return min + rnd.nextDouble()*(max-min);
	}

	@Override
	public double[] weights(int size) {
		double[] w = new double[size];
		for(int i=0; i<size; i++)
			w[i] = weight();
		return w;
	}
}
