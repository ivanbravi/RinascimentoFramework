package gamesearch.players.extractor;

import statistics.GameStats;
import java.util.OptionalDouble;

public interface StatsExtractor {

	OptionalDouble extract(GameStats s);

}
