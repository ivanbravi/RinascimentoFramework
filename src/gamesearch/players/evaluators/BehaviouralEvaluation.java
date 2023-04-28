package gamesearch.players.evaluators;

import gamesearch.players.evaluators.matrix.GridGenerator;
import gamesearch.players.evaluators.matrix.LoadGridGenerator;
import gamesearch.players.evaluators.matrix.NormalisedMatrixSimilarity;
import gamesearch.players.evaluators.matrix.MatrixEvaluator;
import gamesearch.players.extractor.BehavioursExtractor;
import gamesearch.players.extractor.StatsExtractor;
import log.LogGroup;
import log.Logger;
import mapelites.behaviours.BehaviourReader;
import mapelites.core.binning.Binning;
import statistics.GameStats;
import statistics.types.NumericalStatistic;
import statistics.types.StatisticInterface;
import utils.filters2D.*;
import utils.ops2D.Data;
import utils.ops2D.Init2D;
import utils.ops2D.Math2D;
import utils.ops2D.ToString2D;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BehaviouralEvaluation implements PlayersEvaluator{

	private transient BehavioursExtractor extractor;
	private transient StatsExtractor performanceExtractor;
	private double[][] targetDistribution;
	private MatrixEvaluator evaluator;
	private Filter2D[] preProcessingFilters;

	private BehaviouralEvaluationDelegate delegate;

	public static boolean logEvaluations = true;
	public static LogGroup lg;
	public static ToString2D.Format logFormat = new ToString2D.Format("","","\t", "\n");

	public BehaviouralEvaluation(String behavioursFile,
								 StatsExtractor performanceExtractor,
								 GridGenerator gg,
								 MatrixEvaluator evaluator,
								 Filter2D[] preProcessingFilters){
		this.extractor = new BehavioursExtractor(behavioursFile);
		this.performanceExtractor = performanceExtractor;
		this.targetDistribution = gg.samplesGrid(extractor.axisBins(0), extractor.axisBins(1));
		this.evaluator = evaluator;
		this.preProcessingFilters = preProcessingFilters;
	}

	private double[][] extractMatrixFromData(HashMap<Double, GameStats> data){
		double[][] empiricalDistribution = Init2D.copyShape(targetDistribution);
		Init2D.init(empiricalDistribution,Double.NEGATIVE_INFINITY);

		for(Map.Entry<Double, GameStats> e: data.entrySet()){
			GameStats stats = e.getValue();

			double[] newBehaviour = extractor.extractBehaviours(stats);
			int[] newCoordinates = extractor.place(newBehaviour);
			double newPerformance = performanceExtractor.extract(stats).getAsDouble();
			int x = newCoordinates[0];
			int y = newCoordinates[1];
			if(empiricalDistribution[x][y]<newPerformance)
				empiricalDistribution[x][y] = newPerformance;

		}
		return empiricalDistribution;
	}

	public void setDelegate(BehaviouralEvaluationDelegate delegate){
		this.delegate = delegate;
	}

	@Override
	public NumericalStatistic evaluateWithStatistic(HashMap<Double, GameStats> data, String logID) {
		double[][] empiricalDistribution = extractMatrixFromData(data);
		double[][] filteredDistribution;

		delegate.empiricalMatrix(empiricalDistribution);
		filteredDistribution = applyFiltersUntilConvergence(empiricalDistribution, preProcessingFilters);
		delegate.filteredMatrix(filteredDistribution);
		NumericalStatistic filteredEvaluation = evaluator.evaluateWithStatistics(targetDistribution, filteredDistribution);
		if(logEvaluations){
			NumericalStatistic empiricalEvaluation = evaluator.evaluateWithStatistics(targetDistribution, empiricalDistribution);
			logEvaluation(
					empiricalDistribution,
					filteredDistribution,
					empiricalEvaluation.value(),
					filteredEvaluation.value(),
					logID
			);
			Logger.saveAtom(data,Logger.atomDestination(lg.folderPath()+"evalData["+logID+"]"));
		}

		return filteredEvaluation;
	}

	private static void logEvaluation(double[][] empiricalDistribution, double[][] filteredDistribution, double empiricalEvaluation, double filteredEvaluation, String logIdD){
		if(!logEvaluations)
			return;
		String empiricalString = ToString2D.matrixToString(empiricalDistribution, logFormat,false);
		String filteredString = ToString2D.matrixToString(filteredDistribution, logFormat,false);
		BehaviouralEvaluationLog log = new BehaviouralEvaluationLog(
				empiricalString,
				filteredString,
				empiricalEvaluation,
				filteredEvaluation
		);
		Logger.saveAtom(log,Logger.atomDestination(lg.folderPath()+"evalLog["+logIdD+"]"));
	}

	private static double[][] applyFilters(double[][] data, Filter2D[] filters){
		double[][] r = data;
		for(int i=0; i<filters.length; i++){
			r = filters[i].apply(r);
		}
		return r;
	}

	private static double[][] applyFiltersUntilConvergence(double[][] data, Filter2D[] filters){
		double[][] r = data;
//		System.out.println(ToString2D.matrixToString(r,ToString2D.defaultFormat,false));
//		System.out.println();
		do{
			r = applyFilters(r,filters);
//			System.out.println(ToString2D.matrixToString(r,ToString2D.defaultFormat,false));
//			System.out.println();
		}while(Math2D.contains(r,Double.NEGATIVE_INFINITY));
		return r;
	}

	public double[][] getTargetDistribution() {
		return Init2D.copy(targetDistribution);
	}

	public static void main(String[] args) {
		String gridGeneratorFile = "assets/game_search/grid.json";
		String behavioursFile = "assets/game_search/behaviours_8x8.json";

		BehaviouralEvaluation.lg = new LogGroup("tmp/TEST - "+
				(new SimpleDateFormat("yy:MM:dd:HH:mm:ss")).format(new Date())+"/");

		GridGenerator eg = LoadGridGenerator.load(gridGeneratorFile);
		BehaviourReader br = new BehaviourReader(behavioursFile);
		Binning[] bins = br.getLinearBins();

		double[] xAxis = BehaviourReader.getAxis(bins[0]);
		double[] yAxis = BehaviourReader.getAxis(bins[1]);
		double[][] targetGrid = eg.samplesGrid(xAxis,yAxis);

		double[][] empiricalData = Init2D.copy(Data.elInf10x10); // Try with elInf10x10 for example of good filtering
		double[][] filteredData;

		if(Math2D.areNotCompatible(empiricalData,targetGrid)){
			String warning = "[WARNING:Behavioural Evaluation]\nThe matrices empiricalData and targetGrid are not compatible!";
			System.out.println(warning);
			lg.add("WARNING_BE",warning);
			lg.saveLog();
			return;
		}

		MatrixEvaluator evaluator = new NormalisedMatrixSimilarity();
		Filter2D[] filters = new Filter2D[]{new InfFiller(new Mean(1),0.8)};

		filteredData = applyFiltersUntilConvergence(empiricalData,filters);

		StatisticInterface evaluation = evaluator.evaluateWithStatistics(empiricalData,targetGrid);
		StatisticInterface evaluationFiltered = evaluator.evaluateWithStatistics(filteredData,targetGrid);

		logEvaluation(empiricalData,filteredData,evaluation.value(),evaluationFiltered.value(),"0");

		lg.saveLog();
	}

}
