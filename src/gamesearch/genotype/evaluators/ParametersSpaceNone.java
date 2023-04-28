package gamesearch.genotype.evaluators;

import game.Parameters;

public class ParametersSpaceNone implements GenotypeEvaluator {
	@Override
	public double evaluateNormalised(Parameters p) {
		return 1.0;
	}
	public double evaluate(Parameters p) {
		return 1.0;
	}
}
