package benchmarks.statistics;

import game.adapters.WonAdapter;
import game.log.RinascimentoEventDispatcher;
import statistics.GameStats;
import statistics.player.WinGameStats;

public class WinRateBuilder implements StatsBuilder{
	@Override
	public GameStats build(RinascimentoEventDispatcher logger, String[] playerNames) {
		WinGameStats s = new WinGameStats(new WonAdapter());
		s.keepHistory();
		s.setPlayer(playerNames[0]);
		return s;
	}
}
