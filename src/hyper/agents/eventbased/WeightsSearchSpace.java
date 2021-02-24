package hyper.agents.eventbased;

import hyper.utilities.CompleteAnnotatedSearchSpace;
import ntbea.params.DoubleParam;
import ntbea.params.Param;

public class WeightsSearchSpace extends CompleteAnnotatedSearchSpace {

	private int samples;
	public static int min = -1;
	public static int max = 1;

	public WeightsSearchSpace(int size, int samples){
		this.samples = samples;
		params = new Param[size];
		dimensions = new int[size];
		double[] weights = distributeWeights();
		for(int i=0; i<size; i++){
			params[i] = new DoubleParam().setArray(weights).setName("w"+i);
			dimensions[i] = samples;
		}
	}

	private double[] distributeWeights(){
		double[] w = new double[samples];
		for(int i=0; i<samples; i++)
			w[i] = min+(1.0/(samples-1)*i)*(max-min);
		return w;
	}

}
