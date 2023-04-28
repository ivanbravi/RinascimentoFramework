package hyper.search;

import benchmarks.RinascimentoEnv;
import game.adapters.WonAdapter;
import game.budget.ActionsBudget;
import game.heuristics.PointsHeuristic;
import hyper.agents.factory.AgentFactorySpace;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import hyper.agents.mcts.MCTSAgentFactorySpace;
import hyper.agents.rhea.RHEAAgentFactorySpace;
import hyper.agents.seededrhea.SeededRHEAAgentFactorySpace;
import hyper.environment.RinascimentoAgentEvaluator;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import players.BasePlayerInterface;
import players.ai.explicit.SafeRandomPlayer;
import statistics.player.WinGameStats;

import java.util.Arrays;

public class RunSelectiveGridSearch {

	public static boolean TEST = false;

	public static void main(String[] args){

		if(TEST){
			args = new String[]{
					"test.csv",
					"assets/default/",
					"10",
					"MCTS",
					"agents/MCTSParams.json",
					"19926"
			};
		}

		//19926 to 20088

		System.out.println("Args: "+ Arrays.toString(args));
		if(args.length<5){
			System.out.println("Not enough arguments");
			return;
		}

		boolean partial = args.length==7;
		String fileName = args[0];
		String gameVersion = args[1];
		int agentSimulationBudget = Integer.parseInt(args[2]);
		String agentType = args[3];
		String agentParametersFile = args[4];


		RinascimentoAgentEvaluator.VERBOSE = false;
		RinascimentoEnv.VERBOSE = true;

		AgentFactorySpace agentFactory = loadProblem(agentType).
				setHeuristic(new PointsHeuristic()).
				setSearchSpace(CompleteAnnotatedSearchSpace.load(agentParametersFile));

		RinascimentoEnv env = new RinascimentoEnv(gameVersion).
				setStats(new WinGameStats(new WonAdapter())).
				setPlayersBudget(new ActionsBudget(agentSimulationBudget));

		BasePlayerInterface[] opponents = new BasePlayerInterface[env.players()-1];
		Arrays.fill(opponents,new SafeRandomPlayer());

		RinascimentoAgentEvaluator problem = new RinascimentoAgentEvaluator().
				setAgentFactory(agentFactory).
				setEnvironment(env).
				setOpponents(opponents).
				setAgentQuality(new WinGameStats(new WonAdapter()));

		System.out.println("[Problem Log]");
		System.out.println(problem.toString());

		GridSearch gs = new GridSearch(problem.searchSpace(),problem);

		if(partial) {
			int from = Integer.parseInt(args[5]);
			int to = from+1;
			System.out.println("Running partial Grid Search log");

			gs.logPartialSearchRange(fileName,from,to);
		}else {
			System.out.println("Running full Grid Search log");
			gs.logSearch(fileName);
		}

	}

	private static HeuristicAgentFactorySpace loadProblem(String agentType){
		if(agentType.equals("RHEA")){
			return new RHEAAgentFactorySpace();
		}else if(agentType.equals("SeededRHEA")){
			return new SeededRHEAAgentFactorySpace();
		}else if(agentType.equals("MCTS")){
			return new MCTSAgentFactorySpace();
		}
		return null;
	}

}
