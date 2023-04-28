package gamesearch.players.extractor;

import mapelites.behaviours.BehaviourReader;
import mapelites.core.binning.Binning;
import mapelites.core.binning.LinearBinning;
import statistics.GameStats;

import java.util.OptionalDouble;

public class BehavioursExtractor {

	private Binning[] bins;
	private String[] binNames;
	private WrapperExtractors extractor;

	public BehavioursExtractor(String behavioursFile){
		BehaviourReader br = new BehaviourReader(behavioursFile);
		if(br.size()!=2)
			throw new RuntimeException("Too many behavioural metrics: max supported is 2");
		binNames = new String[br.metricsCount()];
		bins = new Binning[br.size()];
		populateBins(br);
		extractor = new WrapperExtractors(binNames);
	}

	private void populateBins(BehaviourReader br){
		for(int i=0; i<br.size(); i++){
			binNames[i] = br.getCode(i);
			if(br.getIsMetric(i))
				bins[i] = new LinearBinning(br.getMin(i),br.getMax(i),br.getBreaks(i));
		}
	}

	public double[] extractBehaviours(GameStats stat) {
		return convert(extractor.extract(stat));
	}

	private double[] convert(OptionalDouble[] b){
		double[] c = new double[b.length];
		for(int i=0;i<b.length; i++)
			c[i] = b[i].getAsDouble();
		return c;
	}

	private Binning getBinForMetric(String metric){
		for(int i=0; i<bins.length; i++)
			if(binNames[i].equals(metric))
				return bins[i];
		return null;
	}

	public double[] axisBins(int index){
		return BehaviourReader.getAxis(bins[index]);
	}

	public double[] axisBins(String metric){
		Binning bin = getBinForMetric(metric);
		double[] values = new double[bin.binCount()];
		for(int i=0; i<values.length; i++)
			values[i] = bin.marker(i);
		return values;
	}

	public int[] place(double[] b){
		int[] p = new int[b.length];
		for(int i=0;i<b.length; i++){
			p[i] = bins[i].bin(b[i]);
		}
		return p;
	}

}
