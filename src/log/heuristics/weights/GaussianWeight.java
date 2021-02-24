package log.heuristics.weights;

import java.util.Random;

public class GaussianWeight implements WeightGenerator{

	private Random rnd;
	private double mean;
	private double variance;

	public GaussianWeight(){
		mean = 0;
		variance = 1.0;
	}

	public GaussianWeight(double mean, double variance){
		this.mean = mean;
		this.variance = variance;
	}

	@Override
	public void setSeed(long seed) {
		rnd.setSeed(seed);
	}

	@Override
	public double weight() {
		return (rnd.nextGaussian()-0.5+mean)*variance;
	}

	@Override
	public double[] weights(int size) {
		double[] w = new double[size];
		for(int i=0; i<size; i++)
			w[i] = weight();
		return w;
	}
}
