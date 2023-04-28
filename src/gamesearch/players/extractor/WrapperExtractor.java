package gamesearch.players.extractor;

import statistics.GameStats;
import statistics.PlayerStatsWrapper;

import java.util.OptionalDouble;

public class WrapperExtractor implements StatsExtractor{

	String performanceMetric;

	public WrapperExtractor(String performanceMetric){
		this.performanceMetric = performanceMetric;
	}

	@Override
	public OptionalDouble extract(GameStats s) {
		if(s instanceof PlayerStatsWrapper)
			return OptionalDouble.of(((PlayerStatsWrapper) s).value(performanceMetric));
		return OptionalDouble.empty();
	}
}
