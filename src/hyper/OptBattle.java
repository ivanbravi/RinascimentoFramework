package hyper;

import benchmarks.RinascimentoEnv;
import statistics.PlayersStats;
import statistics.types.NumericalStatistic;
import statistics.player.PlayerNumericalStatistic;
import statistics.player.ResultStats;
import statistics.player.WinGameStats;
import game.adapters.ResultStatAdapter;
import game.adapters.WonAdapter;
import game.budget.ActionsBudget;
import game.heuristics.PointsHeuristic;
import hyper.agents.factory.AgentFactorySpace;
import hyper.environment.RAEPooledEnemies;
import hyper.environment.RinascimentoAgentEvaluator;
import hyper.agents.rhea.RHEAAgentFactorySpace;
import hyper.agents.seededrhea.SeededRHEAAgentFactorySpace;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import hyper.utilities.DiscreteLiniariser;
import hyper.utilities.Distance;
import log.LogGroup;
import log.TimeSeries;
import ntbea.NTupleBanditEA;
import ntbea.NTupleSystem;
import players.BasePlayerInterface;
import players.ai.factory.SafeRandomFactory;
import players.ai.factory.SimpleAgentFactory;
import utils.History;
import utils.RandomBased;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class OptBattle  extends RandomBased{

	public static boolean VERBOSE = true;

	public int rounds = 100;
	public int ntbeaBudget = 200;
	public int testGames = 500;

	private int currRound;

	private RinascimentoEnv env;

	private HashMap<String,TimeSeries> configsHistoryLogs = new HashMap<>();
	private HashMap<String,TimeSeries> configDistances = new HashMap<>();
	private HashMap<String,NumericalStatistic> distanceStatistics = new HashMap<>();

	public LogGroup lg = new LogGroup("optbattle");

	private SimpleAgentFactory defaultAgentFactory = new SafeRandomFactory(); //new OSLAFactory();
	private ArrayList<AgentFactorySpace> agentSpaces;
	private History<SimpleAgentFactory> history = new History<>();
	private TimeSeries roundStats;

	public OptBattle(RinascimentoEnv env){
		this.env = env;
	}

	public OptBattle setHistoryLimit(int limit){
		history = new History<>(limit);
		history.add(defaultAgentFactory);
		return this;
	}

	public OptBattle setAgentSpaces(ArrayList<AgentFactorySpace> agentSpaces){
		this.agentSpaces = agentSpaces;
		for(int i=0; i<agentSpaces.size(); i++){
			String key = agentSpaces.get(i).getAgentType();

			TimeSeries dist = new TimeSeries(key+"_distance");
			lg.add(dist);
			configDistances.put(key, dist);

			TimeSeries conf = new TimeSeries(key+"_configs");
			lg.add(conf);
			configsHistoryLogs.put(key,conf);

			NumericalStatistic avgDist = new NumericalStatistic();
			lg.add(key+"_avgDistance",avgDist);
			distanceStatistics.put(key,avgDist);
		}
		return this;
	}

	public HashMap<String,int[]> run(){
		if(agentSpaces==null){
			System.out.println("Agent spaces not setup");
			return null;
		}

		HashMap<String,int[]>agents = new HashMap<>();
		roundStats = new TimeSeries("agentsStatsPerRound");
		this.lg.add(roundStats);
		PlayersStats currRoundAgentsStats;

		for(currRound=0; currRound<rounds; currRound++){
			if(VERBOSE) System.out.println("Round: "+currRound);
			agents = optimisation(agents);
			currRoundAgentsStats = test(agents,testGames);
			addToHistory(agents);
			roundStats.log(currRoundAgentsStats);
		}

		return agents;
	}

	private void addToHistory(HashMap<String,int[]> agents){
		for(AgentFactorySpace afs: agentSpaces){
			String agentType = afs.getAgentType();
			int[] config = agents.get(agentType);
			String suffix = "_r"+currRound+"";
			SimpleAgentFactory saf = afs.getSimpleFactory(config,suffix);
			history.add(saf);
		}
	}

	private HashMap<String,int[]> optimisation(HashMap<String,int[]> prevConfigs){
		HashMap<String,int[]> agentsConfigs = new HashMap<>();

		for(AgentFactorySpace optSpace:agentSpaces){

			if(VERBOSE) System.out.print("[Optimisation NTBEA: start... ");
			int[] agOptParams = runNTBEA(optSpace);
			if(VERBOSE) System.out.println("end!]");

			if(prevConfigs.size()!=0) {
				TimeSeries cH = configsHistoryLogs.get(optSpace.getAgentType());
				int packedConfig = DiscreteLiniariser.pack(optSpace.getSearchSpace(),agOptParams);
				cH.log(Arrays.asList(packedConfig,agOptParams));

				NumericalStatistic dist = new NumericalStatistic(Distance.manhattan(prevConfigs.get(optSpace.getAgentType()), agOptParams));
				TimeSeries cDist = configDistances.get(optSpace.getAgentType());
				cDist.log(dist);

				NumericalStatistic avgDistance = distanceStatistics.get(optSpace.getAgentType());
				avgDistance.add(dist);
			}

			agentsConfigs.put(optSpace.getAgentType(),agOptParams);
		}

		return agentsConfigs;
	}

	private String[] getFactoryNames() {
		String[] names = new String[agentSpaces.size()];
		for (int i=0; i<names.length; i++){
			AgentFactorySpace afs = agentSpaces.get(i);
			names[i] = afs.getAgentType();
		}
		return names;
	}

	private PlayersStats test(HashMap<String,int[]> configs,int games){
		String[] agentNames = getFactoryNames();
		PlayersStats stats = new PlayersStats(new ResultStats(new ResultStatAdapter()), agentNames);
		BasePlayerInterface[] players = new BasePlayerInterface[env.players()];

		for(int i=0; i<games; i++) {

			for (int pId=0; pId<agentSpaces.size(); pId++) {
				AgentFactorySpace afs = agentSpaces.get(pId);
				int[] config = configs.get(afs.getAgentType());
				players[pId] = afs.agent(config);
			}

			for (int pId = configs.size(); pId < players.length; pId++) {
				players[pId] = history.get(rnd.nextInt(history.size())).agent();
			}

			env.setPlayers(players);
			env.setStats(stats);

			PlayersStats currStats = (PlayersStats) env.runOnce();
			stats.add(currStats);
		}

		return stats;
	}

	private int[] runNTBEA(AgentFactorySpace factorySpace){
		double kExplore = 1;
		double epsilon = 0.7;
		NTupleBanditEA banditEA = new NTupleBanditEA().setKExplore(kExplore).setEpsilon(epsilon);

		NTupleSystem model = new NTupleSystem();

		RAEPooledEnemies evaluator = new RAEPooledEnemies();

		PlayerNumericalStatistic ps = new WinGameStats(new WonAdapter());
		ps.setPlayer(factorySpace.getAgentType());

		evaluator.setOpponentsPool(history.asList()).
				setAgentFactory(factorySpace).
				setEnvironment(env).
				setAgentQuality(ps);

		model.use1Tuple = true;
		model.use2Tuple = true;
		model.use3Tuple = false;
		model.useNTuple = true;
		banditEA.setModel(model);

		return banditEA.runTrial(evaluator, ntbeaBudget);
	}

	public static void main(String[] args){

		RinascimentoEnv.VERBOSE = false;
		RinascimentoAgentEvaluator.VERBOSE = true;
		String gameVersion = "assets/default/";
		int agentSimulationBudget = 1000;

		ArrayList<AgentFactorySpace> spaces = new ArrayList<>();

		AgentFactorySpace space1 = new RHEAAgentFactorySpace().
				setHeuristic(new PointsHeuristic()).
				setSearchSpace(CompleteAnnotatedSearchSpace.load("agents/RHEAParams.json"));

		AgentFactorySpace space2 = new SeededRHEAAgentFactorySpace().
				setHeuristic(new PointsHeuristic()).
				setSearchSpace(CompleteAnnotatedSearchSpace.load("agents/SeededRHEAParams.json"));

		spaces.add(space1);
		spaces.add(space2);

		RinascimentoEnv env = new RinascimentoEnv(gameVersion).
				setStats(new WinGameStats(new WonAdapter())).
				setPlayersBudget(new ActionsBudget(agentSimulationBudget));

		OptBattle battle = new OptBattle(env);

		battle.setAgentSpaces(spaces);

		boolean quickRun = false;

		if(quickRun) {
			battle.rounds = 5;
			battle.ntbeaBudget = 30;
			battle.testGames = 5;
		}else{
			battle.rounds = 200;
			battle.ntbeaBudget = 500;
			battle.testGames = 500;
		}

		battle.setHistoryLimit(-1);

		HashMap<String,int[]> solutions = battle.run();
		PlayersStats r = battle.test(solutions,battle.testGames);

		System.out.println(r.toString());
		battle.lg.saveLog();
	}

}
