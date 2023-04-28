package gamesearch.players.evaluators.matrix;

import statistics.types.NumericalStatistic;
import statistics.types.StatisticInterface;
import utils.ops2D.Stats2D;

public class JensenShannonDivergence implements MatrixEvaluator{

	double smoothing;

	 public JensenShannonDivergence(double smoothing){
		this.smoothing = smoothing;
	}

	@Override
	public NumericalStatistic evaluateWithStatistics(double[][] a, double[][] b) {
		double[][] na = Stats2D.additiveSmoothingNormalisation(a,smoothing);
		double[][] nb = Stats2D.additiveSmoothingNormalisation(b,smoothing);
		double divergence = Stats2D.jsDivergence(na,nb);
		return new NumericalStatistic(1-divergence);
	}


}
