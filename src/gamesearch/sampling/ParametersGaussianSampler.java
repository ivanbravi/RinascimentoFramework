package gamesearch.sampling;

import game.Parameters;
import gamesearch.ParametersFactory;
import gamesearch.ParametersSpace;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import ntbea.params.Param;
import utils.CopyToClipboard;

import java.util.Arrays;

public class ParametersGaussianSampler extends ParametersSampler {

	ArraySampler[] samplers;
	int[] centers;
	int size;

	public ParametersGaussianSampler(ParametersFactory gpf, Parameters center, double sigma){
		super(gpf);
		centers = ParametersSpace.closestConfigIndices(gpf.getGameSpace(),center);
		samplers = buildSamplers(gpf, centers, sigma);
		size = gpf.getGameSpace().nDims();
	}

	@Override
	public int[] sample(){
		int[] config = new int[size];
		for(int i=0; i<size; i++){
			config[i] = samplers[i].next();
		}
		return config;
	}

	private static ArraySampler[] buildSamplers(ParametersFactory gpf, int[] centers, double sigma){
		Param[] ps = gpf.getGameSpace().getParams();
		ArraySampler[] r = new ArraySampler[ps.length];

		for(int i=0; i<r.length; i++)
			r[i] = new ArraySampler(centers[i], gpf.getGameSpace().nValues(i), sigma);

		return r;
	}

	public int[] centers(){
		return Arrays.copyOf(centers, centers.length);
	}

	public static void main(String[] args) {
		String spaceFile = "assets/RinascimentoParamsTight.json";
		String gameFile = "assets/defaultx2/";
		double sigma = 0.5;
		CompleteAnnotatedSearchSpace gameSpace = CompleteAnnotatedSearchSpace.load(spaceFile);
		ParametersFactory gpf = new ParametersFactory(gameSpace);
		Parameters p = Parameters.load(gameFile);
		ParametersGaussianSampler sampler = new ParametersGaussianSampler(gpf, p, sigma);
		int samples = 1000;

		StringBuilder builder = new StringBuilder();

		for(int i=0; i<samples; i++){
			int[] config = sampler.sample();
			builder.append(Arrays.toString(config)+",\n");
		}

		CopyToClipboard.copy(builder.toString());
		System.out.println(builder);
	}

}
