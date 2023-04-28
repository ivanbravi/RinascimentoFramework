package gamesearch.players.evaluators.grid;

public class Gaussian {

	double[] mean;
	double[][] covMatrix;

	public Gaussian(double[] mean, double[][] covMatrix) {
		this.mean = mean;
		this.covMatrix = covMatrix;
	}
}
