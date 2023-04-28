package gamesearch.sampling;

import gamesearch.ParametersFactory;

public abstract class ParametersSampler {
	protected ParametersFactory gpf;
	abstract public int[] sample();

	public ParametersSampler(ParametersFactory gpf){
		this.gpf = gpf;
	}

}
