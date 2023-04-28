package hyper.search;

import benchmarks.RinascimentoEnv;
import com.google.gson.JsonObject;
import evodef.SearchSpace;
import hyper.environment.RinascimentoAgentEvaluator;
import ntbea.*;
import ntbea.params.Param;
import statistics.types.StatisticInterface;
import utilities.ElapsedTimer;
import utils.loaders.EasyJSON;
import utils.loaders.LoadAgentEvaluator;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class RunNTBEA {

	public static JsonObject params;
	public static int runId;

	public static void main(String[] args) {

        if(args.length < 2){
			System.out.println("Missing NTBEA parameters file argument or RUN_ID!");
			System.out.println("See file \"agents/ntbea_parameters.json\" for an example.");
			return;
		}

		RinascimentoEnv.setTHREADS(1);

		params = EasyJSON.getObject(args[0]);
		runId = Integer.parseInt(args[1]);
		System.out.println("Run ID: " + runId);
		System.out.println("Parameters file: " + args[0]);
		System.out.println("Parameters content: " + params);

        LoadAgentEvaluator aeLoader = new LoadAgentEvaluator(
        		params.get("game/version").getAsString(),
				params.get("agent/type").getAsString(),
				params.get("agent/space").getAsString(),
				params.get("game/budget").getAsInt(),
				params.get("game/opponents").getAsString()
		);
		RinascimentoAgentEvaluator problem = aeLoader.load();

		problem.trueFitnessSamples = params.get("ntbea/truefitnessbudget").getAsInt();

		System.out.println("[Problem]\n"+problem.toString());

        NTupleBanditEA banditEA = new NTupleBanditEA().
				setKExplore(params.get("ntbea/k").getAsDouble()).
				setEpsilon(params.get("ntbea/eps").getAsDouble());

        NTupleSystem model = new NTupleSystem();

        model.use1Tuple = params.get("ntbea/1Tuple").getAsBoolean();
        model.use2Tuple = params.get("ntbea/2Tuple").getAsBoolean();
        model.use3Tuple = params.get("ntbea/3Tuple").getAsBoolean();
        model.useNTuple = params.get("ntbea/nTuple").getAsBoolean();
        banditEA.setModel(model);

        ElapsedTimer timer = new ElapsedTimer();
        System.out.println("SS size: "+searchSpaceSize(problem.searchSpace()));
        int[] solution = banditEA.runTrial(problem, params.get("ntbea/budget").getAsInt());
		Param[] space_params = problem.searchSpace().getParams();

		if(params.get("ntbea/printreport").getAsBoolean()) {
			System.out.println("Report: ");
			new NTupleSystemReport().setModel(model).printDetailedReport();
			new NTupleSystemReport().setModel(model).printSummaryReport();
		}

		String outFilePath = params.get("ntbea/result").getAsString()+runId+".csv";

		//saving the configuration BEFORE *real* fitness evaluation
		saveFile(outFilePath, aeLoader.getAgentFactory().getSearchSpace().getParams(), solution, -9999, -9999);

		System.out.println("Model created: ");
		System.out.println(model);
		System.out.println("Model used: ");
		System.out.println(banditEA.getModel());

		System.out.println();
		System.out.println("Solution returned: " + Arrays.toString(solution));
		printExplicitSolution(solution, space_params);
		System.out.println();
		StatisticInterface stats = problem.trueFitnessComplete(solution);
		System.out.println("Solution fitness:  " + stats.value());
		System.out.println("k Explore: " + banditEA.kExplore);
		System.out.println(timer);

		//saving the configuration AFTER *real* fitness evaluation
		saveFile(outFilePath, aeLoader.getAgentFactory().getSearchSpace().getParams(), solution, stats.value(), stats.error());
    }

    private static void saveFile(String path, Param[] ps,int[] solution, double avg, double stderr){
		try(FileWriter w = new FileWriter(path)){
			String solutionArray = Arrays.toString(solution);
			solutionArray = solutionArray.replaceAll("\\[","").replaceAll("\\]","").replaceAll(" ","");
			String solutionNames="";
			for(Param p :ps){
				solutionNames = solutionNames+p.getName()+",";
			}
			solutionNames = solutionNames.substring(0,solutionNames.length()-1);
			w.append(solutionNames+",truefitness"+",truefitnessError"+"\n");
			w.append(solutionArray+","+avg+","+stderr);
			w.flush();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

    private static long searchSpaceSize(SearchSpace s){
		long size=1;
    	for(int i=0; i<s.nDims(); i++){
    		size*=s.nValues(i);
		}
		return size;
	}

    private static void printExplicitSolution(int[] solution, Param[] params){
    	for(int i=0; i<params.length; i++){
    		System.out.println(params[i].getName()+": "+params[i].getValue(solution[i]));
		}
	}



}

