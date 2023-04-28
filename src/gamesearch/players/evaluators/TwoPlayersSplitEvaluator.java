package gamesearch.players.evaluators;

import gamesearch.players.extractor.StatsExtractor;
import statistics.GameStats;
import statistics.types.NumericalStatistic;

import java.util.HashMap;
import java.util.Map;

public class TwoPlayersSplitEvaluator implements PlayersEvaluator{

	StatsExtractor firstExtractor;
	StatsExtractor secondExtractor;

	public TwoPlayersSplitEvaluator(StatsExtractor firstExtractor, StatsExtractor secondExtractor){
		this.firstExtractor = firstExtractor;
		this.secondExtractor = secondExtractor;
	}

	@Override
	public NumericalStatistic evaluateWithStatistic(HashMap<Double, GameStats> data, String logID) {
		NumericalStatistic first = new NumericalStatistic();
		NumericalStatistic second = new NumericalStatistic();
		for(Map.Entry<Double, GameStats> e: data.entrySet()){
			int extracted = 0;
			try{
				double value = firstExtractor.extract(e.getValue()).getAsDouble();
				first.add(new NumericalStatistic(value));
				extracted++;
			}catch (RuntimeException ex){}

			try{
				double value = secondExtractor.extract(e.getValue()).getAsDouble();
				second.add(new NumericalStatistic(value));
				extracted++;
			}catch (RuntimeException ex){}

			if(extracted == 0 || extracted==2){
				System.out.println("ERROR EXTRACTING STATISTIC: "+e.getValue()+
						"\n#"+extracted+"values extracted!");

			}
		}

		double performanceDelta = first.value()-second.value();
		double normalisedDelta = (performanceDelta+1)/2.0;

//		System.out.print("{"+" "+performanceDelta+" = |"+normalisedDelta+"|} ");

		return new NumericalStatistic(normalisedDelta);
	}
}
