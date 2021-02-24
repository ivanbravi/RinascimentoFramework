package benchmarks;

import com.google.gson.Gson;
import utils.AgentDescription;
import utils.AgentsConfig;
import utils.loaders.LoadSearchSpace;

import java.io.File;
import java.util.Arrays;

public class EvaluateNN {

	public static boolean TEST = false;
	public static int mainArgumentsCount = 8;

	public static void main(String[] args){
		int index=0;

		if(TEST){
			args = new String[]{
					"3",
					"9999",
					"assets/defaultx2/",
					"10",
					"{\"agents\" : [ { \"type\" : \"MIX-1.0-RHEA\", \"params\" : [1,1,3,1,1,0,2,2,1,0], \"comment\" : \"Rinascimento:sfppaperBMRH*\" } ] }",
					"simple,nn,TANH,5",
							//0,1,3,1,0,1,1,3,3,0
							//2,6,4,2,3,3,4,4,5,3,
							"0,0.167,0.75,0.5,0,0.34,0.25,0.75,0.6,0,"+
							"0.2,-0.2,-0.4,-0.6,1.0",
					"winrate"};
		}

		if(args.length < mainArgumentsCount){
			System.out.println("Invalid arguments: "+ Arrays.toString(args) + "expected "+mainArgumentsCount+" arguments.");
			return;
		}

		PlayRinascimento.loggerName = "EvaluateNN";

		int threads = Integer.parseInt(args[index++]); // "30"
		int runId = Integer.parseInt(args[index++]); // "9999"
		String gameVersion = args[index++]; // "assets/defaultx2"
		int gamesCount = Integer.parseInt(args[index++]); // "10"
		String opponents = args[index++]; // "agents/nnopp.json"
		String heuristicType  = args[index++]; // "nn,5"
		String converter = heuristicType.substring(0,heuristicType.indexOf(","));
		heuristicType = heuristicType.substring(heuristicType.indexOf(",")+1);
		String weights = args[index++]; // "0.2,-0.2,-0.4,-0.6,1.0"
		String statsType = args[index++]; // "winrate"

		String weightsFile = "EvaluateNN/weights/w"+runId;
		String weightsPath = weightsFile+".json";

		new File(weightsPath).getParentFile().mkdirs();

		LoadSearchSpace.saveWeightsArray(weightsPath,parseWeights(weights));

		AgentsConfig agents = agents(opponents, converter, heuristicType, weightsFile);

		PlayRinascimento.threads = threads;
		PlayRinascimento player = new PlayRinascimento(gameVersion, agents, PlayRinascimento.createBuilder(statsType));
		player.LOG = false;
		player.lg.add("args",args);
		String result = player.play(gamesCount);
		System.out.println(result);
		PlayRinascimento.stop();
	}



	public static AgentsConfig agents(String opponents, String converter,String heuristicType, String weightsFile){
		AgentsConfig oppConfig;
		if(opponents.endsWith(".json")){
			oppConfig = AgentsConfig.readJson(opponents);
		}else{
			oppConfig = new Gson().fromJson(opponents, AgentsConfig.class);
		}

		AgentsConfig allAgents = new AgentsConfig(new AgentsConfig(new AgentDescription[]{
				new AgentDescription("EHB-"+converter+"-RHEA-"+heuristicType+"-"+weightsFile,new int[]{0,1,3,1,0,1,1,3,3,0}),
		}), oppConfig);

		return allAgents;
	}

	public static double[] parseWeights(String weightsString){
		String[] parts = weightsString.split(",");
		double[] w = new double[parts.length];
		for(int i=0; i<parts.length; i++)
			w[i] = Double.parseDouble(parts[i]);

		return w;
	}


}
