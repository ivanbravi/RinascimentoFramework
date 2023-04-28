package gamesearch;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import game.Parameters;
import game.adapters.WonAdapter;
import game.log.RinascimentoEventDispatcher;
import gamesearch.genotype.evaluators.GenotypeEvaluator;
import gamesearch.genotype.evaluators.ParametersSpaceChebyshev;
import gamesearch.genotype.evaluators.ParametersSpaceManhattan;
import gamesearch.genotype.evaluators.ParametersSpaceNone;
import gamesearch.players.RinascimentoGameEvaluatorParameters;
import gamesearch.players.evaluators.*;
import gamesearch.players.evaluators.matrix.JensenShannonDivergence;
import gamesearch.players.evaluators.matrix.LoadGridGenerator;
import gamesearch.players.evaluators.matrix.MatrixEvaluator;
import gamesearch.players.evaluators.matrix.NormalisedMatrixSimilarity;
import gamesearch.players.extractor.NamedWrapperExtractor;
import gamesearch.players.extractor.WrapperExtractor;
import gamesearch.players.samplers.AgentFactorySampler;
import gamesearch.players.samplers.PlayersSampler;
import gamesearch.players.samplers.TwoPlayersSampler;
import gamesearch.players.samplers.db.PlayersDB;
import gamesearch.players.samplers.db.PlayersDBLoader;
import gamesearch.players.samplers.db.PlayersDBSampler;
import gamesearch.players.samplers.sampling.LinearSampling;
import gamesearch.players.samplers.sampling.Sampling;
import gamesearch.players.samplers.sampling.UniformSampling;
import hyper.agents.eventbased.EventBasedSingleAgentFreeWeightsFactory;
import hyper.agents.factory.*;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import log.LogGroup;
import statistics.GameStats;
import statistics.PlayerStatsWrapper;
import statistics.StatsLoader;
import statistics.player.PlayerNumericalStatistic;
import statistics.player.WinGameStats;
import utils.filters2D.Filter2D;
import utils.filters2D.FilterFactory;
import utils.gson.GsonUtils;
import utils.loaders.LoadSearchSpace;

import java.util.ArrayList;

public class RinascimentoGameEvaluatorFactory {

	protected static String performanceMetric = "winrate";

	public static RinascimentoGameEvaluator create(JsonObject params, LogGroup lg){
		RinascimentoGameEvaluatorParameters rgeParameters = getParameters(params);
		StatsLoader.StatsCompiler statsCompiler = statsBuilder(
				params.get("fitness/stats").getAsJsonArray(),
				StatsLoader.getPerformanceStat(params.get("fitness/performance").getAsString()));
		ParametersFactory gameFactory = getGameFactory(params);
		PlayersSampler mainSampler = getSampler(params, rgeParameters);
		PlayersSampler opponentSampler = getSimpleSampler("fitness/opponent/", params);
		PlayersEvaluator playersEvaluator = getEvaluator(params, rgeParameters,lg);
		GenotypeEvaluator genotypeEvaluator = getGenotypeEvaluator(params,gameFactory.gameSpace);

		mainSampler.setPlayerNameName(rgeParameters.mainPlayerName);
		opponentSampler.setPlayerNameName(rgeParameters.opponentPlayerName);

		return new RinascimentoGameEvaluator(
				gameFactory,
				mainSampler,
				opponentSampler,
				playersEvaluator,
				genotypeEvaluator,
				statsCompiler,
				rgeParameters
		);
	}

	public static RinascimentoGameEvaluatorParameters getParameters(JsonObject params){
		RinascimentoGameEvaluatorParameters evaluatorParameters = new RinascimentoGameEvaluatorParameters();
		evaluatorParameters.opponentPlayerName = "opponent";
		evaluatorParameters.mainPlayerName = "main";
		evaluatorParameters.fitnessPlayerSamples = params.get("fitness/samples").getAsInt();
		evaluatorParameters.fitnessPerPlayerSamples = params.get("fitness/repeats").getAsInt();
		evaluatorParameters.validationPlayerSamples = params.get("fitness/true/samples").getAsInt();
		evaluatorParameters.validationPerPlayerSamples = params.get("fitness/true/repeats").getAsInt();
		evaluatorParameters.threads = params.get("game/threads").getAsInt();
		evaluatorParameters.playerSimulationBudget = params.get("game/budget").getAsInt();
		return evaluatorParameters;
	}

	private static ParametersFactory getGameFactory(JsonObject params){
		String gameSpacePath = params.get("game/space").getAsString();
		CompleteAnnotatedSearchSpace gameSpace = CompleteAnnotatedSearchSpace.load(gameSpacePath);

		ParametersFactory r;
		r = new ParametersFactory(gameSpace);

		if(params.has("game/space/reduction/around") && params.has("game/space/reduction/rate")){
			String centerGamePath = params.get("game/space/reduction/around").getAsString();
			double gameSpaceReduction = params.get("game/space/reduction/rate").getAsDouble();
			Parameters center = Parameters.load(centerGamePath);
			r = r.resizeAround(gameSpaceReduction, center);
		}

		return r;
	}

	public static PlayersSampler getSimpleSampler(String root, JsonObject params){
		String agentType = params.get(root+"type").getAsString();
		AgentFactory factory;

		if(LoadSearchSpace.isFreeWeightsHeuristicAgent(agentType)){
			String heuristicType = params.get(root+"eb/heuristic").getAsString();
			String converterType  = params.get(root+"eb/converter").getAsString();
			String algorithm = params.get(root+"eb/algorithm").getAsString();
			HeuristicAgentFactorySpace afs = (HeuristicAgentFactorySpace) LoadSearchSpace.loadFactorySpace(algorithm,null,null);
			int[] config = GsonUtils.fromJsonArrayToIntArray(params.get(root+"config").getAsJsonArray());
			double[] weights = GsonUtils.fromJsonArrayToDoubleArray(params.get(root+"eb/weights").getAsJsonArray());
			factory = new EventBasedSingleAgentFreeWeightsFactory(heuristicType,converterType,afs,config,weights);
		}else if(BasicAgentFactory.isBasicAgent(agentType)){
			factory = new BasicAgentFactory(agentType);
		}else{
			AgentFactorySpace afs = LoadSearchSpace.loadFactorySpace(agentType,null,null);
			int[] config = GsonUtils.fromJsonArrayToIntArray(params.get(root+"config").getAsJsonArray());
			factory = new SpaceBasedAgentFactory(afs,config);
		}

		return new AgentFactorySampler(factory);
	}

	private static String getNumberedPlayer(String baseName, int count){
		return baseName+"["+count+"]";
	}

	public static PlayersSampler getSampler(JsonObject params, RinascimentoGameEvaluatorParameters rgep){
		String samplerType = params.get("fitness/player_sampler").getAsString();
		PlayersSampler sampler;

		if(samplerType.equals("base")) {
			sampler = getSimpleSampler("fitness/player_sampler/base/", params);
		}else if(samplerType.equals("double")){
			PlayersSampler firstSampler = getSimpleSampler("fitness/player_sampler/base/", params);
			PlayersSampler secondSampler= getSimpleSampler("fitness/player_sampler/double/", params);
			firstSampler.setPlayerNameName(getNumberedPlayer(rgep.mainPlayerName, 0));
			secondSampler.setPlayerNameName(getNumberedPlayer(rgep.mainPlayerName, 1));
			return new TwoPlayersSampler(firstSampler,secondSampler);
		}else if(samplerType.equals("database")){
			String dbFile = params.get("fitness/player_sampler/database/file").getAsString();
			PlayersDB pdb = PlayersDBLoader.loadPlayerDB(dbFile);
			String smplType = params.get("fitness/player_sampler/database/sampling").getAsString();
			Sampling smpl = null;
			if(smplType.equals("random")){
				smpl = new UniformSampling();
			}else if(smplType.equals("linear")){
				smpl = new LinearSampling();
			}
			sampler = new PlayersDBSampler(pdb, smpl);
		}else{
			throw new RuntimeException("Add sampler options here for new type: "+samplerType);
		}

		return sampler;
	}

	public static PlayersEvaluator getEvaluator(JsonObject params,RinascimentoGameEvaluatorParameters rgep, LogGroup lg){
		String evaluatorType = params.get("fitness/gameplay").getAsString();
		PlayersEvaluator evaluator;

		if(evaluatorType.equals("2Player"))
			evaluator = new TwoPlayerEvaluator(new WrapperExtractor(performanceMetric));
		else if(evaluatorType.equals("2PlayerSplit")) {
			evaluator = new TwoPlayersSplitEvaluator(
					new NamedWrapperExtractor(performanceMetric,getNumberedPlayer(rgep.mainPlayerName, 0)),
					new NamedWrapperExtractor(performanceMetric,getNumberedPlayer(rgep.mainPlayerName, 1)));
		}else if(evaluatorType.equals("matrix")) {

			JsonArray filtersJson = params.getAsJsonArray("fitness/gameplay/matrix/filters");
			ArrayList<Filter2D> filters = new ArrayList<>();
			for(JsonElement e :filtersJson)
				filters.add(FilterFactory.create((JsonObject) e));

			MatrixEvaluator similarity ;
			String similarityMeasure = params.get("fitness/gameplay/matrix/similarity").getAsString();
			if(similarityMeasure.equals("difference")){
				similarity = new NormalisedMatrixSimilarity();
			}else if(similarityMeasure.equals("divergence")){
				double smoothing = params.get("fitness/gameplay/matrix/similarity/divergence/smoothing").getAsDouble();
				similarity = new JensenShannonDivergence(smoothing);
			}else{
				throw new RuntimeException("Unknown Similarity measure: "+similarityMeasure);
			}

			evaluator = new BehaviouralEvaluation(
					params.get("fitness/gameplay/matrix/metrics").getAsString(),
					new WrapperExtractor(performanceMetric),
					LoadGridGenerator.load(params.get("fitness/gameplay/matrix/target").getAsString()),
					similarity,
					filters.toArray(new Filter2D[filters.size()])
			);
			BehaviouralEvaluation.lg = lg;
			lg.add("TargetDistribution", ((BehaviouralEvaluation)evaluator).getTargetDistribution());
		}else{
			throw new RuntimeException("Add evaluator options here for new type: " + evaluatorType);
		}

		return evaluator;
	}

	public static GenotypeEvaluator getGenotypeEvaluator(JsonObject params, CompleteAnnotatedSearchSpace gameSpace){
		String genotypeEvaluatorType = params.get("fitness/genotype/type").getAsString();
		GenotypeEvaluator gEvaluator;
		if(genotypeEvaluatorType.equals("Manhattan")){
			String refPath = params.get("fitness/genotype/reference").getAsString();
			Parameters ref = Parameters.load(refPath);
			gEvaluator = new ParametersSpaceManhattan(ref, gameSpace);
		}else if(genotypeEvaluatorType.equals("Chebyshev")){
			String refPath = params.get("fitness/genotype/reference").getAsString();
			Parameters ref = Parameters.load(refPath);
			gEvaluator = new ParametersSpaceChebyshev(ref,gameSpace);
		}else if(genotypeEvaluatorType.equals("None")){
			gEvaluator = new ParametersSpaceNone();
		}else{
			throw new RuntimeException("Add genotype evaluator options here for new type: "+genotypeEvaluatorType);
		}
		return gEvaluator;
	}

	private static StatsLoader.StatsCompiler statsBuilder(JsonArray stats, PlayerNumericalStatistic performanceStat){

		return new StatsLoader.StatsCompiler() {
			@Override
			public GameStats compile(RinascimentoEventDispatcher dispatcher, String mainPlayerName) {
				String[] gameStatsCodes = GsonUtils.fromJsonArrayToStringArray(stats);
				PlayerNumericalStatistic[] pStats = StatsLoader.plugStatistics(
						mainPlayerName,
						gameStatsCodes,
						true,
						dispatcher);
				PlayerStatsWrapper pStatsWrapper = new PlayerStatsWrapper(
						mainPlayerName,
						performanceMetric,
						performanceStat,
						gameStatsCodes,
						pStats
				);
				return pStatsWrapper;
			}
		};
	}


}
