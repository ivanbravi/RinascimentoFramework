package gamesearch.players.extractor;

import statistics.GameStats;
import statistics.PlayerStatsWrapper;

import java.util.OptionalDouble;

public class NamedWrapperExtractor extends WrapperExtractor{

	String name;

	public NamedWrapperExtractor(String performanceMetric, String playerName) {
		super(performanceMetric);
		this.name = playerName;
	}

	@Override
	public OptionalDouble extract(GameStats s) {
		if (s instanceof PlayerStatsWrapper){
			if(!name.equals(s.getPlayer()))
				throw new RuntimeException();
		}
		return super.extract(s);
	}
}
