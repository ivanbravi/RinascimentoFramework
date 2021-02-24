package benchmarks.statistics;

import com.google.gson.GsonBuilder;
import game.log.RinascimentoEventDispatcher;
import statistics.GameStats;

public interface StatsBuilder {

	GameStats build(RinascimentoEventDispatcher logger, String[] playerNames);
	default String process(GameStats stats){
		stats.value();
		return (new GsonBuilder().create()).toJson(stats);
	}

}
