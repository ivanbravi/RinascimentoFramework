package gamesearch.sampling;

import gamesearch.ParametersFactory;

import java.util.Random;

public class ParametersUniformSampler extends ParametersSampler{

	Random rnd;

	public ParametersUniformSampler(ParametersFactory gpf){
		super(gpf);
		rnd = new Random();
	}

	@Override
	public int[] sample() {
		int size = gpf.getGameSpace().nDims();
		int[] r = new int[size];

		for(int i=0; i<size; i++){
			r[i] = rnd.nextInt(gpf.getGameSpace().nValues(i));
		}

		return r;
	}
}
