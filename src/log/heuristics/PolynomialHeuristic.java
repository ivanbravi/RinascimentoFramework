package log.heuristics;

import utils.math.combination.repeated.MultiCombination;

import java.util.Arrays;
import java.util.Random;

public class PolynomialHeuristic implements WeightedHeuristic {

	private double[] weights;
	private int featuresSize;
	private int order;
	transient private MultiCombination combination;

	private PolynomialHeuristic(){

	}

	public PolynomialHeuristic(int featuresSize, int order){
		this.featuresSize = featuresSize;
		this.order = order;
		this.combination = new MultiCombination(featuresSize, order);
		long combCount = combination.combinations(featuresSize, order);
		this.weights = new double[(int) combCount];
	}

	public PolynomialHeuristic(int featuresSize, int order, double[] weights) {
		this.featuresSize = featuresSize;
		this.order = order;
		this.combination = new MultiCombination(featuresSize, order);
		long combCount = combination.combinations(featuresSize, order);

		if(weights.length != combCount)
			throw new RuntimeException("Inappropriate weights size");

		this.weights = Arrays.copyOf(weights,weights.length);
	}

	@Override
	public double value(double[] features) {
		double value = 0;
		for (int i = 0; i < weights.length; i++) {
			int[] polyConfig = combination.combination(features.length, order, i);
			double partial = weights[i]*poly(features, polyConfig);
			value += partial;
		}
		return value;
	}

	@Override
	public int dimensionality() {
		return weights.length;
	}

	@Override
	public void setWeights(double[] w) {
		if(w.length != weights.length)
			throw new RuntimeException("Inappropriate weights size");
		this.weights = Arrays.copyOf(w, w.length);
	}

	@Override
	public WeightedHeuristic clone() {
		PolynomialHeuristic copy = new PolynomialHeuristic();

		copy.featuresSize = this.featuresSize;
		copy.weights = this.weights;
		copy.combination = this.combination;
		copy.order = this.order;

		return copy;
	}

	private double poly(double[] features, int[] poly) {
		double value = 1;
		for (int f = 0; f < features.length; f++) {
			int exp = poly[f];
			double partial = Math.pow(features[f], exp);
			//System.out.print("f["+f+"]^"+exp);
//			if(f+1<features.length)
//				System.out.print("*");
			value *= partial;
		}
		return value;
	}

	@Override
	public String toString() {
		return "featuresSize: "+featuresSize+" order:"+order+" weights:"+Arrays.toString(weights);
	}

	public static void main(String[] args) {
		double[] f = new double[]{1,0,3};
		int order = 3;
		WeightedHeuristic h = new PolynomialHeuristic(f.length,order);
		double[] weights = rndWeights(h.dimensionality());

		h.setWeights(weights);

		System.out.println(h.value(f));
	}

	private static double[] rndWeights(int size){
		double[] v = new double[size];
		Random rnd = new Random();

		for(int i=0; i<size; i++)
			v[i] = rnd.nextDouble()-0.5;

		return v;
	}
}