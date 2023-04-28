package gamesearch.sampling;

import game.Parameters;
import gamesearch.ParametersFactory;
import gamesearch.ParametersSpace;
import gamesearch.genotype.evaluators.GenotypeEvaluator;
import gamesearch.genotype.evaluators.ParametersSpaceManhattan;
import gamesearch.genotype.evaluators.ParametersSpaceNone;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import utils.CopyToClipboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MutationExpanderSampler extends ParametersSampler {

	private int count = 0;
	private int samples;
	private int[] center;
	private Random rnd;
	private List<Integer> variableIndices;

	public MutationExpanderSampler(ParametersFactory gpf, Parameters center, int samples){
		super(gpf);
		this.samples = samples;
		this.center = ParametersSpace.closestConfigIndices(gpf.getGameSpace(), center);
		this.rnd = new Random();
		variableIndices = new ArrayList<>();

		for(int i=0; i<gpf.getGameSpace().nDims(); i++){
			if(gpf.getGameSpace().nValues(i) > 1)
				variableIndices.add(i);
		}
	}

	public void reset(){
		count = 0;
	}

	@Override
	public int[] sample() {
		int[] r = Arrays.copyOf(center, center.length);
		List<Integer> indices = new ArrayList<>(variableIndices);
		int mutations = 1 + (int) ((indices.size()-1)* ((double) count)/samples);

		for(int m=0; m<mutations; m++){
			int rndSelection = rnd.nextInt(indices.size());
			int indexSelected = indices.remove(rndSelection);
			r[indexSelected] = rnd.nextInt(gpf.getGameSpace().nValues(indexSelected));
		}

		count++;

		return r;
	}

	public static void main(String[] args) {
		CompleteAnnotatedSearchSpace gameSpace = CompleteAnnotatedSearchSpace.load("assets/RinascimentoParamsTight.json");
		ParametersFactory gpf = new ParametersFactory(gameSpace);
		Parameters center = Parameters.load("assets/defaultx2/");
		int samples = 500;
		MutationExpanderSampler sampler = new MutationExpanderSampler(gpf, center, samples);
		GenotypeEvaluator metric = new ParametersSpaceManhattan(center,gameSpace);

		ArrayList<Double> data = new ArrayList<>();

		for(int i=0; i<samples; i++){
			int[] sample = sampler.sample();
			Parameters sampleP = gpf.getParameters(sample);
			double distance = metric.evaluate(sampleP);
			data.add(distance);
			System.out.println("{"+distance+"}"+Arrays.toString(sample));
		}

		CopyToClipboard.copy(Arrays.toString(data.stream().mapToDouble(d -> d).toArray()));
	}
}
