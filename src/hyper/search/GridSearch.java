package hyper.search;

import evodef.NoisySolutionEvaluator;
import evodef.SearchSpace;
import hyper.utilities.DiscreteLiniariser;
import hyper.utilities.FullNoisySolutionEvaluator;
import statistics.types.StatisticInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class GridSearch {

	public static String resultDirectory="results/";

	private SearchSpace ss;
	private FullNoisySolutionEvaluator nse;

	public GridSearch(SearchSpace ss, FullNoisySolutionEvaluator nse){
		this.ss = ss;
		this.nse = nse;
	}

	public void logPartialSearchRange(String filePath, int actualFrom, int actualTo){
		long[] pSize = fillSizes(ss);
		long ssSize = ssSize(pSize);

		System.out.println("Search Space Size: "+ssSize);
		System.out.println("Search Space Dimensions: "+Arrays.toString(pSize));
		System.out.println("Evaluating from "+actualFrom+" to "+actualTo);

		System.out.println("Start...");

		File outDir = new File(resultDirectory);
		outDir.mkdirs();

		try (FileWriter writer = new FileWriter(resultDirectory+filePath)){
			int[] config;
			double fitness,error;
			for(int i=actualFrom; i<actualTo; i++){
				config = DiscreteLiniariser.unpack(ss,i);
				System.out.println("\t ["+i+"] Config: "+Arrays.toString(config));
				StatisticInterface stats = nse.trueFitnessComplete(config);
				fitness = stats.value();
				error = stats.error();
				writer.write(outString(i,Arrays.toString(config),fitness,error));
				writer.flush();
			}
		}catch (IOException e){
			e.printStackTrace();
		}
		System.out.println("...end!");
	}

	public void logPartialSearch(String filePath, double from, double to){
		long[] pSize = fillSizes(ss);
		long ssSize = ssSize(pSize);

		int actualFrom = (int) (from*ssSize);
		int actualTo = (int) (to*ssSize);

		System.out.println("Search Space Size: "+ssSize);
		System.out.println("Search Space Dimensions: "+Arrays.toString(pSize));
		System.out.println("Evaluating from "+actualFrom+" to "+actualTo);

		System.out.println("Start...");

		File outDir = new File(resultDirectory);
		outDir.mkdirs();

		try (FileWriter writer = new FileWriter(resultDirectory+filePath)){
			int[] config;
			double fitness,error;
			for(int i=actualFrom; i<actualTo; i++){
				config = DiscreteLiniariser.unpack(ss,i);
				System.out.println("\t ["+i+"] Config: "+Arrays.toString(config));
				StatisticInterface stats = nse.trueFitnessComplete(config);
				fitness = stats.value();
				error = stats.error();
				writer.write(outString(i,Arrays.toString(config),fitness,error));
				writer.flush();
			}
		}catch (IOException e){
			e.printStackTrace();
		}
		System.out.println("...end!");
	}

	private String outString(int id, String config, double fitness, double error){
		return id+","+config.replaceAll("\\[","").replaceAll("\\]","")+","+fitness+","+error+"\n";
	}

	public void logSearch(String filePath){
		long[] pSize = fillSizes(ss);
		long ssSize = ssSize(pSize);

		System.out.println("Search Space Size: "+ssSize);
		System.out.println("Search Space Dimensions: "+Arrays.toString(pSize));

		System.out.println("Start...");

		File outDir = new File(resultDirectory);
		outDir.mkdirs();

		try (FileWriter writer = new FileWriter(resultDirectory+filePath)){
			int[] config;
			double fitness,error;
			for(int i=0; i<ssSize; i++){
				config = DiscreteLiniariser.unpack(ss,i);
				System.out.println("\t ["+i+"] Config: "+Arrays.toString(config));
				StatisticInterface stats = nse.trueFitnessComplete(config);
				fitness = stats.value();
				error = stats.error();
				writer.write(outString(i,Arrays.toString(config),fitness,error));
				writer.flush();
			}
		}catch (IOException e){
			e.printStackTrace();
		}
		System.out.println("...end!");
	}


	private long[] fillSizes(SearchSpace ss){
		long[] sizes = new long[ss.nDims()];

		for(int i=0; i<sizes.length; i++)
			sizes[i] = ss.nValues(i);

		return sizes;
	}

	private long ssSize(long[] sizes){
		long size = 1;
		for(Long s:sizes){
			size*=s;
		}
		return size;
	}

}
