package gamesearch.genotype.evaluators;

import game.Parameters;
import gamesearch.ParametersSpace;
import hyper.utilities.CompleteAnnotatedSearchSpace;

public class ParametersSpaceChebyshev implements GenotypeEvaluator{

	/*
	* Chebyshev distance a.k.a. chessboard distance
	* 		5  4  3  2  2  2  2  2
	* 		5  4  3  2  1  1  1  2
	* 		5  4  3  2  1  K  1  2
	* 		5  4  3  2  1  1  1  2
	* 		5  4  3  2  2  2  2  2
	* 		5  4  3  3  3  3  3  3
	* 		5  4  4  4  4  4  4  4
	* 		5  5  5  5  5  5  5  5
	* with K being the center
	* "minimum number of moves a King requires to move between its position and another cell"
	* */

	private int[] configCenter;
	private int maxDistance;
	private CompleteAnnotatedSearchSpace parametersSpace;

	public ParametersSpaceChebyshev(Parameters center, CompleteAnnotatedSearchSpace gss){
		parametersSpace = gss;
		configCenter = ParametersSpace.closestConfigIndices(parametersSpace, center);
		maxDistance = maxConfigDistance(configCenter, parametersSpace);
	}

	private static int maxConfigDistance(int[] config, CompleteAnnotatedSearchSpace gss){
		int max = 0;
		for(int i=0; i<config.length; i++){
			int currMax = Math.max(gss.nValues(i)-1-config[i], config[i]);
			max = Math.max(currMax,max);
		}
		return max;
	}

	@Override
	public double evaluateNormalised(Parameters p) {
		return evaluate(p)/maxDistance;
	}

	@Override
	public double evaluate(Parameters p) {
		int max = 0;
		int[] pConfig = ParametersSpace.closestConfigIndices(parametersSpace, p);

		for(int i=0; i<pConfig.length; i++)
			max = Math.max(max, Math.abs(pConfig[i]-configCenter[i]));
		return max;
	}

	public static void main(String[] args) {
		Parameters p = Parameters.defaultParameters();
		Parameters pOther = Parameters.unpack(new int[]{0, 0, 27, 2, 105, 0, 7, 9, 21, 8, 2, 2, 3, 0});
		CompleteAnnotatedSearchSpace space = CompleteAnnotatedSearchSpace.load("assets/RinascimentoParamsTight.json");
		ParametersSpaceChebyshev distance = new ParametersSpaceChebyshev(p, space);
		double d1 = distance.evaluateNormalised(pOther);
		double d2 = distance.evaluateNormalised(p);

		System.out.println("d(p, pOther) = "+d1);
		System.out.println("d(p, p     ) = "+d2);
	}


}
