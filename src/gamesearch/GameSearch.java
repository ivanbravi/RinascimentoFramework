package gamesearch;

import com.google.gson.JsonObject;
import log.LogGroup;
import ntbea.NTupleBanditEA;
import ntbea.NTupleSystem;
import ntbea.params.Param;
import statistics.types.StatisticInterface;
import utils.Pair;
import utils.PausableElapsedTimer;
import utils.loaders.EasyJSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class GameSearch {

	private int ntbeaBudget;
	private int candidateSolutionFitnessChecks;
	private double k;
	private double eps;
	private RinascimentoGameEvaluator problem;

	private static LogGroup lg = LogGroup.getGroupWithDatedFolder("GS");

	public GameSearch(JsonObject params){
		lg.add("exp_parameters", params);

		ntbeaBudget = params.get("ntbea/budget").getAsInt();
		k = params.get("ntbea/k").getAsDouble();
		eps = params.get("ntbea/eps").getAsDouble();
		candidateSolutionFitnessChecks = params.get("ntbea/checks").getAsInt();

		problem = RinascimentoGameEvaluatorFactory.create(params, lg);
	}



	public void run(){
		NTupleBanditEA ntbea = new NTupleBanditEA().
				setKExplore(k).
				setEpsilon(eps).
				setResetModelEachRun(false);

		NTupleSystem model = new NTupleSystem();
		model.use1Tuple = true;
		model.use2Tuple = true;
		model.use3Tuple = false;
		model.useNTuple = true;
		ntbea.setModel(model);

		PausableElapsedTimer ntbeaTimer = new PausableElapsedTimer();
		PausableElapsedTimer trueFitnessTimer = new PausableElapsedTimer();
		PausableElapsedTimer overall = new PausableElapsedTimer();
		overall.start();

		int stepSize = ntbeaBudget/candidateSolutionFitnessChecks;
		ArrayList<Pair<int[], Double>> fitnessTrend = new ArrayList<>();

		Runnable callback = () -> {
			if(problem.nEvals()%stepSize == 0){
				ntbeaTimer.pause();
				trueFitnessTimer.start();
				int[] solution = ntbea.banditLandscapeModel.getBestOfSampledPlusNeighbours(1000);
				StatisticInterface fitness = problem.trueFitnessComplete(solution);
				fitnessTrend.add(new Pair(solution, fitness.value()));
				trueFitnessTimer.pause();
				ntbeaTimer.start();
			}
		};

		problem.setPostEvaluateCallback(callback);

		ntbeaTimer.start();
		int[] solution = ntbea.runTrial(problem, ntbeaBudget);
		ntbeaTimer.pause();

		trueFitnessTimer.start();
		StatisticInterface fitness = problem.trueFitnessComplete(solution);
		trueFitnessTimer.pause();

		String searchOut = compileSearchOut(ntbeaTimer,solution);
		String trueFitnessOut = compileTrueFitnessOut(trueFitnessTimer, fitness.value());

		System.out.println(searchOut);
		System.out.println(trueFitnessOut);
		System.out.println("Overall time elapsed: "+overall.prettyElapsed());

		lg.add("FitnessTrend",fitnessTrend);
		lg.add("ResultDescription", resultDescription(solution));
		lg.add("ActualGameSpace", problem.searchSpace());
		lg.add("SingleEvaluations",problem.getEvaluationsLog());
		lg.add("EvaluationLog",problem.getLogString());
		lg.add("ComplexEvaluationLog", problem.getComplexLog());
		lg.add("EvaluationDurations",problem.getLogDurations());
		lg.add("PlayerSamples",problem.getSamplerLog());
		lg.add("solution", solution);
		lg.add("[solution]parameters", problem.getParameters(solution));
		lg.add("fitness",fitness);
		lg.add("searchResultLog",searchOut);
		lg.add("trueFitnessLog",trueFitnessOut);
		lg.saveLog();
	}

	private String compileTrueFitnessOut(PausableElapsedTimer timer, double value){
		return "Solution fitness:  " + value + "\n"+
				"Elapsed time [true fitness]: " + timer.prettyElapsed() + "\n";
	}

	private String compileSearchOut(PausableElapsedTimer timer, int[] solution){
		String searchOut = "";
		searchOut += "Solution returned: " + Arrays.toString(solution)+"\n";
		searchOut += "Elapsed time [search]: "+timer.prettyElapsed()+"\n";
		return searchOut;
	}

	private String resultDescription(int[] solution){
		Param[] space_params = problem.searchSpace().getParams();
		StringBuilder resultDescriptionBuilder = new StringBuilder();
		IntStream.range(0,space_params.length).
				mapToObj(index ->space_params[index].getName()+": "+space_params[index].getValue(solution[index])).
				forEach(s -> resultDescriptionBuilder.append(s+"\n"));
		return resultDescriptionBuilder.toString();
	}

	public static void main(String[] args){
		if(args.length==0) {
			String file;

//			file = "assets/game_search/config_rhea_rnd.json";
// 			file = "assets/game_search/config_rhea_rnd_fast.json";
//			file = "assets/game_search/config_efrhea_rnd.json";
//			file = "assets/game_search/config_efdb_rnd.json";
//			file = "assets/game_search/config_efrhea_efrhea_100_50.json";
// 			file = "assets/game_search/config_efrhea_efrhea_100_76.json";
 			file = "assets/game_search/config_efrhea_efrhea_100_76_fast.json";
			args = new String[]{file};
			String warning = "Experiment running with default testing configuration: "+args[0];
			lg.add("WARNING", warning);
			System.out.println("[WARNING] "+warning);
		}
		JsonObject mapArgs = EasyJSON.getObject(args[0]);
		GameSearch gs = new GameSearch(mapArgs);
		gs.run();
	}

}
