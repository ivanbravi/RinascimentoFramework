package gamesearch.sampling;

import java.util.Random;

public class ArraySampler {

	double[] cdf;
	Random rnd;

	public ArraySampler(int center, int size, double sigma){
		double[] pdf = Distribution.Gaussian.pdf(center, size, sigma);
		cdf = Distribution.cdf(pdf);
		rnd = new Random();
	}

	public int next(){
		return Distribution.nextIndex(cdf,rnd.nextDouble());
	}

}
