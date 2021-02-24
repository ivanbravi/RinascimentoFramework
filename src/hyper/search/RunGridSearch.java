package hyper.search;

import hyper.environment.RinascimentoAgentEvaluator;
import utils.loaders.LoadAgentEvaluator;

import java.util.Arrays;

public class RunGridSearch {

	// [0] -> file name			: results file name
	// [1] -> game directory	: version of the game
	// [2] -> agent budget		: version of the game
	// [3] -> agent type		: "SeededRHEA" "RHEA"
	// [4] -> agent params		: file name
	// [5] -> opponents			: file name
	// [6] -> id				: int
	// [7] -> count				: int

	public static boolean TEST = false;
	// java -Xmx900m -jar gridsearch.jar test.csv assets/default/ 1000 MCTS agents/MCTSParams.json agents/opponents.json 1 100

	public static void main(String[] args){

		if(TEST){
			System.out.println("RunGridSearch RUNNING TEST");
			args = new String[]{
				"test.csv",
				"assets/defaultx2/",
				"10",
				"EHB-simple-RHEA-linear-3", //"EHB-RHEA-poly,3-11"
				"a",
				"agents/opponents.json",
				"250",
				"500"
			};
		}

		System.out.println("Args: "+ Arrays.toString(args));
		if(args.length<6){
			System.out.println("Not enough arguments");
			return;
		}

		boolean partial = args.length==8;
		String fileName = args[0];
		String gameVersion = args[1];
		int agentSimulationBudget = Integer.parseInt(args[2]);
		String agentType = args[3];
		String agentParametersFile = args[4];
		String opponentsFile = args[5];

		LoadAgentEvaluator aeLoader = new LoadAgentEvaluator(gameVersion, agentType,agentParametersFile,agentSimulationBudget,opponentsFile);
		RinascimentoAgentEvaluator problem = aeLoader.load();

		System.out.println("[Problem Log]");
		System.out.println(problem.toString());

		GridSearch gs = new GridSearch(problem.searchSpace(),problem);

		if(partial) {
			int id = Integer.parseInt(args[6]);
			int count = Integer.parseInt(args[7]);
			double from = (id-1.0)/count;
			double to = (double)id/count;

			System.out.println("Running partial Grid Search log");

			gs.logPartialSearch(fileName,from,to);
		}else {
			System.out.println("Running full Grid Search log");
			gs.logSearch(fileName);
		}

	}
}
