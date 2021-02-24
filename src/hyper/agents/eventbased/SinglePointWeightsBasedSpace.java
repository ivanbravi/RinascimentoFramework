package hyper.agents.eventbased;

import hyper.utilities.CompleteAnnotatedSearchSpace;
import ntbea.params.DoubleParam;
import ntbea.params.Param;

public class SinglePointWeightsBasedSpace extends CompleteAnnotatedSearchSpace {

	public SinglePointWeightsBasedSpace(double[] weights) {
		int size = weights.length;
		params = new Param[size];
		dimensions = new int[size];
		for(int i=0; i<size; i++){
			params[i] = new DoubleParam().setArray(new double[]{weights[i]}).setName("w"+i);
			dimensions[i] = 1;
		}
	}



}
