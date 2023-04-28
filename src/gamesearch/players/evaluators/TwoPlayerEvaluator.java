package gamesearch.players.evaluators;

import gamesearch.players.extractor.StatsExtractor;
import statistics.GameStats;
import statistics.types.NumericalStatistic;
import statistics.types.StatisticInterface;
import java.util.HashMap;
import java.util.stream.DoubleStream;

public class TwoPlayerEvaluator implements PlayersEvaluator{

	private StatsExtractor extractor;

	public TwoPlayerEvaluator(StatsExtractor extractor){
		this.extractor = extractor;
	}

	@Override
	public NumericalStatistic evaluateWithStatistic(HashMap<Double, GameStats> data, String logID) {
		DoubleStream resultStream = data.values().stream().
				filter(entry -> extractor.extract(entry).isPresent()).
				mapToDouble(entry -> extractor.extract(entry).getAsDouble());
		NumericalStatistic sResult = new NumericalStatistic();
		resultStream.forEach(value -> sResult.add(new NumericalStatistic(value)));
		return sResult;
	}
}
