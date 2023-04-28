package benchmarks;

import game.Parameters;
import game.adapters.ResultStatAdapter;
import game.adapters.WonAdapter;
import game.budget.ActionsBudget;
import game.log.RinascimentoEventDispatcher;
import log.LogGroup;
import mapelites.behaviours.CardCount;
import mapelites.behaviours.Nobles;
import mapelites.behaviours.ReservedCards;
import mapelites.behaviours.TotalCoins;
import mapelites.loggers.PlayerIncomingCoinsLogger;
import mapelites.loggers.PlayerReserveCardLogger;
import players.BasePlayerInterface;
import statistics.GameStats;
import statistics.PlayerStatsWrapper;
import statistics.PlayersStats;
import statistics.player.PlayerNumericalStatistic;
import statistics.player.ResultStats;
import statistics.player.WinGameStats;
import utils.AgentsConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class RoundRobinTournament {

	private static boolean TEST = false;
	private static boolean VERBOSE = true;

	private static String agents = "agents/archive.json";
	private static String gameVersion = "assets/defaultx2/";
	private static int gameSamples = 1;
	private static int threads = 5;
	private static boolean partial = false;
	private static boolean rowSelection = false;
	private static boolean[] incremental = new boolean[]{false,false,false,false,false,false,false,false,false,false,false,true};
	private static double id = -1;

	public static void main(String[] args){
		if(TEST)
//			// agents/archive.json assets/defaultx2/ 10 1 1:1 1
//			args = new String[]{agents,
//					gameVersion,
//					gameSamples+"",
//					threads+"",
//					"111111111111", //"1:2",
//					"111111111111"
			args = new String[]{
					"agents/journal.json",
					"assets/defaultx2/",
					"1",
					"3"
			};

		if(args.length < 2){
			System.out.println("Illegal arguments: "+ Arrays.toString(args));
			return;
		}

		agents = args[0];
		gameVersion = args[1];
		gameSamples = args.length>2 ? Integer.parseInt(args[2]):threads;
		threads = args.length>3 ? Integer.parseInt(args[3]):threads;
		incremental = args.length>4 ? interpretIncremental(args[4]): incremental;
		id =  args.length>5 ? Double.parseDouble(args[5]): id;

		LogGroup lg;
		if( id != -1){
			lg = new LogGroup("RoundRobinTournament["+String.format("%.0f",id)+"]"+"/");
		}else {
			lg = new LogGroup("RoundRobinTournament - " +
					(new SimpleDateFormat("yy:MM:dd:HH:mm:ss")).format(new Date()) + "/");
		}
		lg.add("arguments", args);

		AgentsConfig playerData = AgentsConfig.readJson(agents);
		BasePlayerInterface[] players = PlayRinascimento.decodePlayers(playerData, Parameters.load(gameVersion));
		lg.add("players description", players);

		if(!(args.length >4)){
			incremental = new boolean[players.length];
		}

		RinascimentoEventDispatcher logger = new RinascimentoEventDispatcher();

		RinascimentoEnv environment = new RinascimentoLoggedEnv(gameVersion, logger).
				setPlayersBudget(new ActionsBudget(1000)).
				setPlayers(players);

		RinascimentoEnv.setTHREADS(threads);

		GameStats[][] results = new GameStats[players.length][];
		for(int i=0; i<players.length; i++)
			results[i] = new GameStats[players.length];
		ArrayList<GameStats> resultList = new ArrayList<>();

		String [] playersNames = new String[players.length];
		boolean shouldRun;

		lg.add("tournament results-grid", results);
		lg.add("tournament results-list", resultList);
		lg.add("player names",playersNames);

		for(int p1=0; p1<players.length; p1++){
			playersNames[p1] = players[p1].getName();
			for(int p2=p1; p2<players.length; p2++){
				BasePlayerInterface[] roundPlayers;
				if(rowSelection)
					shouldRun = (!partial || ( partial && incremental[p1]));
				else
					shouldRun = !partial || ( partial && (incremental[p1] || incremental[p2]));

				if(shouldRun) {
					if (p1 != p2) {
						roundPlayers = new BasePlayerInterface[]{players[p1], players[p2]};
					} else {
						roundPlayers = new BasePlayerInterface[2];
						roundPlayers[0] = players[p1];
						roundPlayers[1] = players[p1].clone();
						roundPlayers[1].setName(roundPlayers[1].getName() + ":2");
					}

					environment.
							setPlayers(roundPlayers).
							setStats(statistics(new String[]{roundPlayers[0].getName(), roundPlayers[1].getName()}, logger));
					if (VERBOSE) System.out.println(p1 + "\t+\t" + p2 + "\t : " + roundPlayers[0].getName() + " vs " + roundPlayers[1].getName());
					GameStats result = environment.runMultiple(gameSamples);
					results[p1][p2] = result;
					resultList.add(result);
				}else{
					if (VERBOSE) System.out.println(p1 + "\t+\t" + p2 + "\t : SKIPPED");
				}

			}
			lg.saveLog();
		}

		lg.saveLog();

		RinascimentoEnv.shutDown();
	}

	private static boolean[] interpretIncremental(String incrementalString){
		partial = true;
		if(incrementalString.contains(":"))
			return rangeIncremental(incrementalString);
		else
			return extensiveIncremental(incrementalString);
	}

	private static boolean[] rangeIncremental(String incrementalString) {
		String[] params = incrementalString.split(":");
		int from = Integer.parseInt(params[0]);
		int to = Integer.parseInt(params[1]);
		int size = AgentsConfig.readJson(agents).agents.length;
		boolean[] v = new boolean[size];

		for(int i=from; i<=to; i++)
				v[i] = true;

		rowSelection = true;
		return v;
	}

	private static boolean[] extensiveIncremental(String incrementalString){
		boolean[] i = new boolean[incrementalString.length()];

		for(int c=0; c<i.length; c++)
			i[c] = incrementalString.charAt(c)=='0'?false:true;

		return i;
	}

	private static PlayersStats statistics(String[] names, RinascimentoEventDispatcher logger){
		PlayerNumericalStatistic mainStat = new ResultStats(new ResultStatAdapter());
		String mainStatName = "Result";
		TotalCoins totalCoinsStat = new TotalCoins();
		ReservedCards reservedCardsStat = new ReservedCards();
		PlayerNumericalStatistic[] otherStats = new PlayerNumericalStatistic[]{
				(PlayerNumericalStatistic) new CardCount().keepHistory(),
				(PlayerNumericalStatistic) new Nobles().keepHistory(),
				(PlayerNumericalStatistic) totalCoinsStat.keepHistory(),
				(PlayerNumericalStatistic) reservedCardsStat.keepHistory(),
				(PlayerNumericalStatistic) new WinGameStats(new WonAdapter()).keepHistory()
		};
		String[] otherStatsNames = new String[]{"card count", "nobles", "tot coins", "reserved cards","win ratio"};

		PlayerStatsWrapper stats = new PlayerStatsWrapper("",mainStatName,mainStat,otherStatsNames,otherStats);
		PlayersStats allStats = new PlayersStats(stats,names);

		for(String p: names) {
			logger.addLogger(totalCoinsStat.getPlayerLoggerName(p), totalCoinsStat.getLoggerInstance(p));
			logger.addLogger(reservedCardsStat.getPlayerLoggerName(p), reservedCardsStat.getLoggerInstance(p));
		}

		return allStats;
	}

}
