package benchmarks;

import statistics.GameStats;
import statistics.PositionStats;
import statistics.player.WinGameStats;
import game.Engine;
import game.Factory;
import game.Parameters;
import game.adapters.PositionAdapter;
import game.adapters.WonAdapter;
import game.heuristics.Heuristic;
import game.heuristics.WinHeuristic;
import game.state.Result;
import game.state.State;
import players.BasePlayerInterface;
import players.HeuristicBasedPlayerInterface;
import players.ai.explicit.RandomPlayer;
import players.ai.explicit.SafeRandomPlayer;
import players.ai.explicit.HyperRHEA;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Evaluation {

	private static BasePlayerInterface[] opponents;
	private static BasePlayerInterface testAgent;
	private static String[] agentParams;
	private static Parameters parameters;
	private static Engine engine;
	private static State gameState;
	private static GameStats stats;

	private static int functionID;
	private static int instanceID;
	private static int samples = 100;

	private static boolean VERBOSE = true;

	public static void main(String[] args){

		// for testing purposes
		//args = new String[]{"1","-1","test.txt","4","0","30","30","0"};

		/* args[0]: function ID {0:"win ratio", 1:"avg position", }
		 * args[1]: instance ID {-1:"random agent", 0:"rhea"}
		 * args[2]: file name
		 * args[3]: agent parameters size
		 * args[…]: agent parameters
		 * args[3+args[3]]: other parameters
		 * */
		String fileName;
		int agentParamsCount;

		fileName = args[2];
		try {
			functionID = Integer.parseInt(args[0]);
			instanceID = Integer.parseInt(args[1]);
			agentParamsCount = Integer.parseInt(args[3]);
			agentParams = Arrays.copyOfRange(args,4, 4+agentParamsCount);
			stats = createStats();
			for (int i = 0; i < samples; i++) {
				GameStats thisStats;
				loadParameters();
				loadPlayer();
				loadOpponents();
				loadHeuristic();
				loadEngine();
				loadGameState();
				thisStats = playGame();
				stats.add(thisStats);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		if(VERBOSE)System.out.println(stats.toString());
		if(VERBOSE)System.out.println(stats.value());

		try(FileWriter writer = new FileWriter(fileName)){
			writer.write("1\n");
			writer.write(String.valueOf(stats.value()));
		}catch (IOException e){
			System.out.println("fun");
		}
	}

	private static void loadParameters(){
		parameters = Parameters.load("assets/default/");
	}

	private static GameStats playGame(){
		Result r = engine.playFullGame(gameState);
		if(VERBOSE)System.out.println(r);
		return stats.create(gameState,testAgent.getName());
	}

	private static void loadEngine(){
		BasePlayerInterface[] playersArray;
		ArrayList<BasePlayerInterface> players = new ArrayList<>();

		players.add(testAgent);
		players.addAll(Arrays.asList(opponents));
		playersArray = players.toArray(new BasePlayerInterface[0]);

		engine = Engine.defaultEngine(playersArray);
	}

	private static GameStats createStats(){
		switch (functionID){
			case 0: return new WinGameStats(new WonAdapter());
			case 1: return new PositionStats(new PositionAdapter());
		}
		return null;
	}

	private static void loadHeuristic(){
		if(instanceID==0){
			Heuristic h = new WinHeuristic();
			if(testAgent instanceof HeuristicBasedPlayerInterface)
				((HeuristicBasedPlayerInterface) testAgent).setHeuristic(h);
		}else{
		}
	}

	private static void loadGameState(){
		if(instanceID==0) {
			gameState = Factory.createState(parameters,engine);
		}else{
			gameState = Factory.createState(parameters,engine);
		}
	}

	private static void loadOpponents(){
		//if(instanceID==0){
			int opponentsCount = parameters.playerCount-1;
			opponents = new BasePlayerInterface[opponentsCount];
			for(int i=0; i<opponentsCount; i++){
				opponents[i] = new RandomPlayer();
			}
		//}
	}

	private static void loadPlayer(){
		if(instanceID==-1){
			testAgent = new SafeRandomPlayer();
		}else if(instanceID==0){
			/*
			* args[4]: use shift buffer
			* args[5]: sequence length
			* args[6]: evals
			* args[7]: flip at least one value
			* */
			HyperRHEA agent = new HyperRHEA();
			agent.useShiftBuffer = Boolean.parseBoolean(agentParams[0]);
			agent.sequenceLength = Integer.parseInt(agentParams[1]);
			agent.evals = Integer.parseInt(agentParams[2]);
			agent.flipAtLeastOneValue = Boolean.parseBoolean(agentParams[3]);
			testAgent = agent;
		}
	}
}
