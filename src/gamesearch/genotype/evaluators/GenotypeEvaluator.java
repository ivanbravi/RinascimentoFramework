package gamesearch.genotype.evaluators;

import game.Parameters;

public interface GenotypeEvaluator {

	double evaluateNormalised(Parameters p);
	double evaluate(Parameters p);

}
