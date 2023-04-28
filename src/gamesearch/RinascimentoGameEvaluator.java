package gamesearch;

import benchmarks.RinascimentoEnv;
import benchmarks.RinascimentoLoggedEnv;
import evodef.AnnotatedSearchSpace;
import evodef.EvolutionLogger;
import game.Parameters;
import game.budget.ActionsBudget;
import game.log.RinascimentoEventDispatcher;
import gamesearch.players.RinascimentoGameEvaluatorParameters;
import gamesearch.genotype.evaluators.GenotypeEvaluator;
import gamesearch.players.evaluators.PlayersEvaluator;
import gamesearch.players.samplers.PlayersSampler;
import hyper.utilities.FullNoisySolutionEvaluator;
import players.BasePlayerInterface;
import statistics.GameStats;
import statistics.StatsLoader;
import statistics.types.NumericalStatistic;
import statistics.types.PayloadNumericalStatistic;
import statistics.types.StatisticInterface;
import utilities.ElapsedTimer;

import java.util.ArrayList;
import java.util.HashMap;

public class RinascimentoGameEvaluator implements FullNoisySolutionEvaluator {

	private ParametersFactory gpf;
	private PlayersSampler playersSampler;
	private PlayersSampler opponentSampler;
	private PlayersEvaluator playersEvaluator;
	private GenotypeEvaluator genEvaluator;
	private StatsLoader.StatsCompiler statsCompiler;
	private RinascimentoGameEvaluatorParameters searchParams;

	// counter only for the "evaluation" method thus regarding the counter for the optimisation algorithm
	private int evaluationStep =0;
	// counter for *all* evaluations whether that's a true evaluation or a simple evaluation
	private int evaluationId=0;

	private EvolutionLogger evolutionLogger = new EvolutionLogger();
	private StringBuilder evaluationString = new StringBuilder();
	private ArrayList<Long> durations = new ArrayList<>();
	private ArrayList<Double> evaluationsLog = new ArrayList<>();
	private ArrayList<EvaluationLogEntry> complexLog = new ArrayList<>();

	private Runnable postEvaluateCallback = () -> {};

	public RinascimentoGameEvaluator(ParametersFactory gpf,
									 PlayersSampler playersSampler,
									 PlayersSampler opponentSampler,
									 PlayersEvaluator playersEvaluator,
									 GenotypeEvaluator genEvaluator,
									 StatsLoader.StatsCompiler statsCompiler,
									 RinascimentoGameEvaluatorParameters searchParams){
		this.gpf = gpf;
		this.playersSampler = playersSampler;
		this.opponentSampler = opponentSampler;
		this.playersEvaluator = playersEvaluator;
		this.genEvaluator = genEvaluator;
		this.statsCompiler = statsCompiler;
		this.searchParams = searchParams;
	}

	@Override
	public StatisticInterface trueFitnessComplete(int[] solution) {
		if(searchParams.validationPlayerSamples == 0 || searchParams.validationPerPlayerSamples == 0){
			searchParams.validationPlayerSamples = searchParams.fitnessPlayerSamples;
			searchParams.validationPerPlayerSamples = searchParams.fitnessPerPlayerSamples;
		}
		return runEvaluations(
				solution,
				searchParams.validationPlayerSamples,
				searchParams.validationPerPlayerSamples,
				true
		);
	}

	public Parameters getParameters(int[] ints){
		return gpf.getParameters(ints);
	}

	private RinascimentoLoggedEnv buildEnvironment(Parameters p){
		RinascimentoEventDispatcher logger = new RinascimentoEventDispatcher();
		RinascimentoLoggedEnv game = new RinascimentoLoggedEnv(p);
		game.setDispatcher(logger);
		game.setPlayersBudget(new ActionsBudget(this.searchParams.playerSimulationBudget));
		return game;
	}

	@Override
	public double evaluate(int[] ints) {
		evaluationStep++;
		double  evaluationValue = runEvaluations(
			ints,
			searchParams.fitnessPlayerSamples,
			searchParams.fitnessPerPlayerSamples,
				false
	).value();
		postEvaluateCallback.run();
		return evaluationValue;
	}

	private String logID(boolean isTrueFitness){
		return isTrueFitness?"*"+trueFitnessCount()+"*": String.valueOf(evaluationStep);
	}

	private int trueFitnessCount(){
		return evaluationId- evaluationStep;
	}

	private GameStats statsForGameAndPlayer(RinascimentoLoggedEnv game, String player){
		return statsCompiler.compile((RinascimentoEventDispatcher) game.getDispatcher(),player);
	}

	private StatisticInterface runEvaluations(int[] solution, int playerSamples, int repeat, boolean isTrueFitness){
		evaluationId++;
		System.out.print("["+logID(isTrueFitness)+":");
		ElapsedTimer evalTimer = new ElapsedTimer();
		Parameters p = getParameters(solution);
		RinascimentoLoggedEnv game = buildEnvironment(p);
		HashMap<Double, GameStats> results = new HashMap<>();
		RinascimentoEnv.setTHREADS(this.searchParams.threads);
		double genEvalResult = this.genEvaluator.evaluateNormalised(p);
		playersSampler.beginRound(evaluationId);
		for(int pID = 0; pID< playerSamples; pID++){
			double pRange = (double) pID/ (playerSamples-1);
			BasePlayerInterface mainPlayer = playersSampler.getPlayer(pRange);
			BasePlayerInterface opponentPlayer = opponentSampler.getPlayer(pRange);
			game.setPlayers(new BasePlayerInterface[]{
					opponentPlayer,
					mainPlayer
			});
			game.setStats(statsForGameAndPlayer(game, mainPlayer.getName()));
			GameStats gs = game.runMultiple(repeat);
			results.put(pRange,gs);
		}
		playersSampler.endRound(evaluationId);
		RinascimentoEnv.shutDown();
		NumericalStatistic evaluation = playersEvaluator.evaluateWithStatistic(results, logID(isTrueFitness));
		PayloadNumericalStatistic finalResult = new PayloadNumericalStatistic(evaluation.value()*genEvalResult);
		finalResult.addPayload("phenotypic", evaluation);
		finalResult.addPayload("genotypic", genEvalResult);
		evaluationsLog.add(finalResult.value());
		complexLog.add(new EvaluationLogEntry(evaluation,genEvalResult, finalResult));
		durations.add(evalTimer.elapsed()/1000);
		evaluationString.append(String.format("[%d: %d s] -> { %f , %f } = %f\n",
				evaluationId,
				evalTimer.elapsed()/1000,
				evaluation.value(),
				genEvalResult,
				finalResult.value())
		);
		System.out.println("✔️]");
		return finalResult;
	}

	public Object getSamplerLog(){
		return playersSampler.getLog();
	}

	public Object getEvaluationsLog(){return evaluationsLog;}

	public Object getComplexLog(){ return complexLog;}

	public ArrayList<Long> getLogDurations(){
		ArrayList<Long> tmp = durations;
		durations = new ArrayList<>();
		return tmp;
	}

	public String getLogString(){
		String log = evaluationString.toString();
		evaluationString.setLength(0);
		return log;
	}

	@Override
	public AnnotatedSearchSpace searchSpace() {
		return gpf.gameSpace;
	}

	@Override
	public int nEvals() {
		return evaluationStep;
	}

	@Override
	public EvolutionLogger logger() {
		return evolutionLogger;
	}

	@Override
	public void reset() {
		evaluationStep =0;
	}

	@Override
	public Boolean isOptimal(int[] ints) {
		return null;
	}

	@Override
	public Double trueFitness(int[] ints) {
		return null;
	}

	@Override
	public Double optimalIfKnown() { return null; }

	@Override
	public boolean optimalFound() { return false; }

	public void setPostEvaluateCallback(Runnable postEvaluateCallback) {
		this.postEvaluateCallback = postEvaluateCallback;
	}

	public PlayersEvaluator getPlayersEvaluator(){
		return playersEvaluator;
	}
}
