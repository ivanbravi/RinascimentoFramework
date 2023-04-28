package gamesearch.genotype.evaluators;

import game.Parameters;
import gamesearch.ParametersSpace;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import ntbea.params.Param;

import java.util.stream.IntStream;

public class ParametersSpaceManhattan implements GenotypeEvaluator {

	private Parameters ref;
	private double maxDistance;
	private CompleteAnnotatedSearchSpace gss;

	public ParametersSpaceManhattan(Parameters ref, CompleteAnnotatedSearchSpace gss){
		this.ref = ref;
		this.gss = gss;
		this.maxDistance = maxDistance(ref,gss);
	}

	private double compute(Parameters center, Parameters point) {
		int[] p1Array = ParametersSpace.closestConfigIndices(gss,center);
		int[] p2Array = ParametersSpace.closestConfigIndices(gss,point);

		return IntStream.
				range(0, p1Array.length).
				mapToObj(index -> Math.abs(p1Array[index]-p2Array[index])).
				reduce(0, Integer::sum);
	}

	@Override
	public double evaluateNormalised(Parameters p) {
		return compute(ref,p)/this.maxDistance;
	}

	@Override
	public double evaluate(Parameters p) {
		return compute(ref,p);
	}

	public static double maxDistance(Parameters center, CompleteAnnotatedSearchSpace gss) {
		int[] centerVector = Parameters.vectorise(center);
		return IntStream.range(0,gss.nDims()).
				mapToObj(index -> {
					int distanceFromFirst = centerVector[index];
					int distanceFromLast  = (gss.nValues(index)-1) - centerVector[index];
					int max = Math.max(distanceFromFirst, distanceFromLast);
					return max;
				}).
				reduce(0,(integer, integer2) -> integer+integer2);
	}
}
