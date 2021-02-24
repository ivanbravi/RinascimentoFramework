package hyper.utilities;

import evodef.NoisySolutionEvaluator;
import statistics.types.StatisticInterface;

public interface FullNoisySolutionEvaluator extends NoisySolutionEvaluator {

	StatisticInterface trueFitnessComplete(int[] solution);

}
