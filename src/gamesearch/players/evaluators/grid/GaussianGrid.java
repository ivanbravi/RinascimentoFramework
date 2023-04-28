package gamesearch.players.evaluators.grid;

import gamesearch.players.evaluators.matrix.GridGenerator;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

public class GaussianGrid implements GridGenerator {
	MultivariateNormalDistribution distribution;

	public GaussianGrid(double[] mean, double[][]covMatrix){
		distribution = new MultivariateNormalDistribution(mean, covMatrix);
	}

	public GaussianGrid(Gaussian g){
		distribution = new MultivariateNormalDistribution(g.mean, g.covMatrix);
	}

	@Override
	public double[][] samplesGrid(double[] x, double[] y){
		double[][] samples = new double[x.length][];
		double[] coordinates = new double[2];
		for(int i=0; i<x.length; i++) {
			samples[i] = new double[y.length];
			for (int j = 0; j < x.length; j++) {
				coordinates[0] = x[i];
				coordinates[1] = y[i];
				samples[i][j] = distribution.density(coordinates);
			}
		}
		return samples;
	}

}
