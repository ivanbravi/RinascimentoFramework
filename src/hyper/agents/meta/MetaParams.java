package hyper.agents.meta;

import hyper.utilities.CompleteAnnotatedSearchSpace;
import ntbea.params.DoubleParam;
import ntbea.params.IntegerParam;
import ntbea.params.Param;

public class MetaParams extends CompleteAnnotatedSearchSpace {

	public MetaParams(){
		int[] tuningTurns = new int[]{1,5,10,20,30}; // 10 -> 2
		int[] rollsDepth = new int[]{1,2,3,4,5}; // 2 -> 1
		double[] eps = new double[]{0.01,0.1,0.4,0.7,0.9}; // 0.7 -> 3
		double[] exp = new double[]{0,5,10,20,30}; // 20 -> 3
		double[] ratio = new double[]{0.01,0.05,0.1,0.2};
		// defalut -> (10,2,0.7,20) -> (2,1,3,3,1)

		params = createParams(tuningTurns,rollsDepth,eps,exp,ratio);
		dimensions = createDimensions(tuningTurns,rollsDepth,eps,exp,ratio);
	}

	private Param[] createParams(int[] a, int[] b, double[] c, double[] d, double[] e){
		return new Param[]{
				new IntegerParam().setArray(a).setName("Tuning Turns"),
				new IntegerParam().setArray(b).setName("Rolls Depth"),
				new  DoubleParam().setArray(c).setName("Epsilon"),
				new  DoubleParam().setArray(d).setName("Exploration"),
				new  DoubleParam().setArray(e).setName("Budget Ratio")
		};
	}

	private int[] createDimensions(int[] a, int[] b, double[] c, double[] d, double[] e) {
		return new int[]{
				a.length,
				b.length,
				c.length,
				d.length,
				e.length
		};
	}

	public static void main(String[] args){
		CompleteAnnotatedSearchSpace.save("agents/MetaParams.json",new MetaParams());
	}

}
