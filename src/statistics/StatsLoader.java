package statistics;

import game.adapters.RWonStaleLossAdapter;
import game.log.RinascimentoEventDispatcher;
import mapelites.behaviours.*;
import statistics.player.PlayerNumericalStatistic;
import statistics.player.WinStaleLostStatsFull;

public class StatsLoader {

	public static PlayerNumericalStatistic plugStatistic(String playerName, String code, boolean keepHistory, RinascimentoEventDispatcher dispatcher){
		PlayerNumericalStatistic stat = StatsLoader.pickStatistic(code, keepHistory);
		if (stat instanceof LoggingStatistic) {
			LoggingStatistic ls = (LoggingStatistic) stat;
			dispatcher.addLogger(ls.getPlayerLoggerName(playerName), ls.getLoggerInstance(playerName));
		}
		return stat;
	}

	public static PlayerNumericalStatistic[] plugStatistics(String playerName, String[] codes, boolean keepHistory, RinascimentoEventDispatcher dispatcher){
		PlayerNumericalStatistic[] stats = new PlayerNumericalStatistic[codes.length];
		for(int i=0; i<stats.length; i++){
			stats[i] = plugStatistic(playerName, codes[i], keepHistory, dispatcher);
		}
		return stats;
	}

	public interface StatsCompiler {
		 GameStats compile(RinascimentoEventDispatcher dispatcher, String mainPlayerName);
	}

	public static PlayerNumericalStatistic getPerformanceStat(String name){
		return new WinStaleLostStatsFull(
					new RWonStaleLossAdapter(),
					WinStaleLostStatsFull.WinStaleLostStatsFullMode.valueOf(name.toUpperCase())
			);
	}

	public static String pickCodeForStatistic(String name){
		String code;

		switch (name){
			case "CardCount": code="card count"; break;
			case "Nobles": code="nobles"; break;
			case "TotalCoins": code="total coins"; break;
			case "ReservedCards": code="reserved cards"; break;
			case "CardBoughtDeck": code="card bought deck"; break;
			case "CardCost": code="card cost"; break;
			case "GameDuration": code="game duration"; break;
			case "Points": code="points"; break;
			default: code = null;
		}

		return code;
	}

	public static PlayerNumericalStatistic pickStatistic(String code, boolean keepHistory){
		PlayerNumericalStatistic stat;
		switch (code){
			case "CardCount": stat = new CardCount();break;
			case "Nobles": stat = new Nobles();break;
			case "TotalCoins": stat = new TotalCoins();break;
			case "ReservedCards": stat = new ReservedCards();break;
			case "CardBoughtDeck": stat = new CardBoughtDeck();break;
			case "CardCost": stat = new CardCost();break;
			case "GameDuration": stat = new GameDuration();break;
			case "Points": stat = new Points(); break;
			default: stat = null;
		}

		if(keepHistory){
			stat = (PlayerNumericalStatistic) stat.keepHistory();
		}

		return stat;
	}

}
