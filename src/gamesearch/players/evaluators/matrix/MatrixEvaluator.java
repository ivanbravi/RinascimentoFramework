package gamesearch.players.evaluators.matrix;

import statistics.types.NumericalStatistic;

public interface MatrixEvaluator {

	default double evaluate(double[][] a, double[][] b){
		return evaluateWithStatistics(a,b).value();
	}

	NumericalStatistic evaluateWithStatistics(double[][] a, double[][] b);
}
