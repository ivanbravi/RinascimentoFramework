package benchmarks.statistics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.adapters.ResultStatAdapter;
import game.adapters.WonAdapter;
import game.log.RinascimentoEventDispatcher;
import mapelites.behaviours.*;
import mapelites.loggers.PlayerCardCostLogger;
import mapelites.loggers.PlayerCardDeckLogger;
import mapelites.loggers.PlayerIncomingCoinsLogger;
import mapelites.loggers.PlayerReserveCardLogger;
import statistics.GameStats;
import statistics.PlayerStatsWrapper;
import statistics.PlayersStats;
import statistics.player.PlayerNumericalStatistic;
import statistics.player.ResultStats;
import statistics.player.WinGameStats;

public class FullMetricsBuilder implements StatsBuilder {
	@Override
	public GameStats build(RinascimentoEventDispatcher logger, String[] playerNames) {
		PlayerNumericalStatistic mainStat = new ResultStats(new ResultStatAdapter());
		String mainStatName = "Result";

		TotalCoins totalCoinsStat = new TotalCoins();
		ReservedCards reservedCardsStat = new ReservedCards();
		CardBoughtDeck cardBoughtDeckStat = new CardBoughtDeck();
		CardCost cardCostStat = new CardCost();

		PlayerNumericalStatistic[] otherStats = new PlayerNumericalStatistic[]{
				(PlayerNumericalStatistic) new CardCount().keepHistory(),
				(PlayerNumericalStatistic) new Nobles().keepHistory(),
				(PlayerNumericalStatistic) totalCoinsStat.keepHistory(),
				(PlayerNumericalStatistic) reservedCardsStat.keepHistory(),
				(PlayerNumericalStatistic) new WinGameStats(new WonAdapter()).keepHistory(),
				(PlayerNumericalStatistic) new GameDuration().keepHistory(),
				(PlayerNumericalStatistic) cardBoughtDeckStat.keepHistory(),
				(PlayerNumericalStatistic) cardCostStat.keepHistory()
		};
		String[] otherStatsNames = new String[]{"card count", "nobles", "tot coins", "reserved cards","win ratio","duration","card deck", "card cost"};

		PlayerStatsWrapper stats = new PlayerStatsWrapper("",mainStatName,mainStat,otherStatsNames,otherStats);
		PlayersStats allStats = new PlayersStats(stats,playerNames);

		for(String p: playerNames) {
			logger.addLogger(totalCoinsStat.getPlayerLoggerName(p), totalCoinsStat.getLoggerInstance(p));
			logger.addLogger(reservedCardsStat.getPlayerLoggerName(p), reservedCardsStat.getLoggerInstance(p));
			logger.addLogger(cardBoughtDeckStat.getPlayerLoggerName(p), cardBoughtDeckStat.getLoggerInstance(p));
			logger.addLogger(cardCostStat.getPlayerLoggerName(p), cardCostStat.getLoggerInstance(p));
		}

		return allStats;
	}
}
