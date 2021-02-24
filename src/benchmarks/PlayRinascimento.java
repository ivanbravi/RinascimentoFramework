package benchmarks;

import benchmarks.statistics.FullMetricsBuilder;
import benchmarks.statistics.StatsBuilder;
import benchmarks.statistics.WinRateBuilder;
import com.google.gson.GsonBuilder;
import game.Engine;
import game.Parameters;
import game.budget.ActionsBudget;
import game.heuristics.PointsHeuristic;
import game.log.RinascimentoEventDispatcher;
import hyper.agents.factory.AgentFactorySpace;
import hyper.agents.meta.MetaAgentFactory;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import log.LogGroup;
import players.BasePlayerInterface;
import players.ai.explicit.OneStepLookAheadAgent;
import players.ai.explicit.SafeRandomPlayer;
import statistics.GameStats;
import time.MilliTimer;
import time.Timer;
import utils.AgentDescription;
import utils.AgentsConfig;
import utils.loaders.LoadSearchSpace;
import utils.profiling.CountersProfiler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class PlayRinascimento {

	public static boolean VERBOSE = false;
	public static int threads = 4;

	public boolean LOG = true;

	public static String loggerName = "PlayRinascimento";

	RinascimentoEnv environment;
	LogGroup lg;
	StatsBuilder statsBuilder;

	/* Call this method when the players can be loaded from the configPath file. */
	PlayRinascimento(String gameVersion, String configPath, StatsBuilder statsBuilder){
		this.statsBuilder = statsBuilder;
		lg = new LogGroup(loggerName+" - "+(new SimpleDateFormat("yy:MM:dd:HH:mm:ss")).format(new Date())+"/");
		AgentsConfig playerData = AgentsConfig.readJson(configPath);
		BasePlayerInterface[] players = decodePlayers(playerData, Parameters.load(gameVersion));
		lg.add("players", players);
		RinascimentoEventDispatcher logger = new RinascimentoEventDispatcher();

		environment = new RinascimentoLoggedEnv(gameVersion, logger).
				setPlayersBudget(new ActionsBudget(1000)).
				setPlayers(players);
		GameStats stats = statsBuilder.build(logger, environment.engine.getPlayerNames());
		environment.setStats(stats);

		RinascimentoEnv.setTHREADS(threads);
	}

	/* Call this method when the players need to be created/crafted at runtime. */
	PlayRinascimento(String gameVersion, AgentsConfig playerData, StatsBuilder statsBuilder){
		this.statsBuilder = statsBuilder;
		lg = new LogGroup(loggerName+" - "+(new SimpleDateFormat("yy:MM:dd:HH:mm:ss")).format(new Date())+"/");
		BasePlayerInterface[] players = decodePlayers(playerData, Parameters.load(gameVersion));
		lg.add("players", players);
		RinascimentoEventDispatcher logger = new RinascimentoEventDispatcher();

		environment = new RinascimentoLoggedEnv(gameVersion, logger).
				setPlayersBudget(new ActionsBudget(1000)).
				setPlayers(players);

		GameStats stats = statsBuilder.build(logger, environment.engine.getPlayerNames());

		environment.setStats(stats);

		RinascimentoEnv.setTHREADS(threads);
	}

	public static void stop(){
		RinascimentoEnv.shutDown();
	}

	public String play(int nGames){
		MilliTimer t = new MilliTimer();
		t.start();
		GameStats s = environment.runMultiple(nGames);
		t.stop();

		lg.add("results", s);
		lg.add("time", nGames+" games in "+t.seconds()+" seconds with "+threads+" threads");

		if(VERBOSE) System.out.println(s.toString());
		if(VERBOSE) System.out.println("exec time: "+t.seconds()+" s");

		if(LOG)
			lg.saveLog();

		return statsBuilder.process(s);
	}

	public static StatsBuilder createBuilder(String type){
		if(type.equalsIgnoreCase("winrate")){
			return new WinRateBuilder();
		}else if(type.equalsIgnoreCase("metrics")){
			return new FullMetricsBuilder();
		}
		return null;
	}

	private static BasePlayerInterface createPlayer(String type, int[] config, Parameters gameParameters){
		BasePlayerInterface p = null;

		boolean isSimple = type.equals("RND") || type.equals("OSLA");
		boolean isMeta = type.substring(0,Math.min(type.length(),4)).equals("META");
		boolean isFactorySpace = !isSimple;

		if(isFactorySpace){
			LoadSearchSpace lss = new LoadSearchSpace();
			String paramFile = lss.defaultSpace(type);
			AgentFactorySpace afs = lss.loadFactorySpace(type, paramFile,gameParameters);
			if(isMeta){
				AgentFactorySpace meta = new MetaAgentFactory(afs).
						setHeuristic(new PointsHeuristic()).
						setSearchSpace(CompleteAnnotatedSearchSpace.load("agents/MetaParams.json"));
				p = meta.agent(config);
			}else{
				p = afs.agent(config);
			}
		}else{
			if(type.equals("RND")){
				p = new SafeRandomPlayer();
			} else if(type.equals("OSLA")){
				p = new OneStepLookAheadAgent();
			}
		}

		return p;
	}

	public static BasePlayerInterface[] decodePlayers(AgentsConfig pData, Parameters gameParameters){
		BasePlayerInterface[] players = new BasePlayerInterface[pData.agents.length];

		for(int i=0; i<pData.agents.length; i++){
			AgentDescription currData = pData.agents[i];
			String type = currData.type;
			int[] config = currData.params;
			players[i] = createPlayer(type,config, gameParameters);
			players[i].setName(players[i].getName()+"_"+i);
		}

		return players;
	}

	public static boolean TEST = true;
	public static CountersProfiler profiler = new CountersProfiler();

	public static void main(String[] args){
		if(TEST){
			System.out.println("PlayRinascimento RUNNING TEST");
			String configFileName = "test"; // agents

			// assets/defaultx2/ agents/several/ 10 true metrics
			args = new String[]{"assets/defaultx2/",

					"agents/" +configFileName+".json",
					//"agents/several/",

					"1000",
					"4",
					"true",
					"metrics"};
		}

		if(args.length!=6){
			System.out.println("ILLEGAL ARGUMENTS: "+ Arrays.toString(args));
			return;
		}

		String gameVersion = args[0];
		String configPath = args[1];
		int nGames = Integer.parseInt(args[2]);
		int threads = Integer.parseInt(args[3]);
		boolean isLoggingActive = Boolean.parseBoolean(args[4]);
		String statsType = args[5];

		if(profiler!=null){
			Engine.attatchProfiler(profiler);
		}

		PlayRinascimento.threads = threads;
		String result;

		profiler.start();
		if((new File(configPath)).isDirectory()){
			File dir = new File(configPath);
			HashMap<String,String> results = new HashMap<>();
			LogGroup lg = new LogGroup(loggerName+" - ["+dir.getName()+"]/");

			for(File f: dir.listFiles()){
				String fileName = f.getName();
				if(fileName.endsWith(".json")){
					Timer t = new MilliTimer();
					System.out.print("Running "+fileName);
					t.start();
					String fResult = runSingleScenario(gameVersion,f.getPath(),nGames,statsType,isLoggingActive);
					t.stop();
					results.put(fileName,fResult);
					System.out.print(" exec: "+t.seconds()+"\n");
				}
			}

			result = (new GsonBuilder().create()).toJson(results);
			lg.add("results", results);
			lg.saveLog();
		}else{
			result = runSingleScenario(gameVersion,configPath,nGames,statsType,isLoggingActive);
		}
		profiler.stop();

		if(profiler!=null){
			System.out.println("{\n\"results\": "+result+",\n\"profiler\":\""+profiler.toString()+"\"\n}");
		}else {
			System.out.println(result);
		}
		RinascimentoEnv.shutDown();
	}

	private static String runSingleScenario(String game, String config, int nGames, String statsType, boolean isLoggingActive){
		PlayRinascimento player = new PlayRinascimento(game,config, createBuilder(statsType));
		player.LOG = isLoggingActive;
		return player.play(nGames);
	}


}
