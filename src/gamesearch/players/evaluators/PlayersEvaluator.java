package gamesearch.players.evaluators;

import statistics.GameStats;
import statistics.types.NumericalStatistic;
import statistics.types.StatisticInterface;

import java.util.HashMap;

public interface PlayersEvaluator {

	default double evaluate(HashMap<Double, GameStats> data, String logID){
		return evaluateWithStatistic(data, logID).value();
	}
	NumericalStatistic evaluateWithStatistic(HashMap<Double, GameStats> data, String logID);

}
