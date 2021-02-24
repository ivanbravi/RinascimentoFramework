package hyper.agents.seededrhea;

import hyper.utilities.CompleteAnnotatedSearchSpace;
import ntbea.params.BooleanParam;
import ntbea.params.DoubleParam;
import ntbea.params.IntegerParam;
import ntbea.params.Param;

public class SeededRHEAAParams extends CompleteAnnotatedSearchSpace{

	public SeededRHEAAParams(){
		boolean[] flipAtLeastOneValue = new boolean[]{false,true};
		double[] mutationRate = new double[]{0.01,0.05,0.1,0.2,0.3,0.5,0.7,0.8,0.9,1.0};
		int[] sequenceLength = new int[]{1,2,3,5,10,20};
		int[] nEvals = new int[]{0,10,20,50,100,200};
		boolean[] useShiftBuffer = new boolean[]{false,true};
		int[] opponentModelType = new int[]{0,1,2};
		double[] opponentBudgetShare = new double[]{0.001,0.005,0.01,0.02,0.05};

		params = createParams(
				flipAtLeastOneValue,
				mutationRate,
				sequenceLength,
				nEvals,
				useShiftBuffer,
				opponentModelType,
				opponentBudgetShare
		);

		dimensions = createDimensions(
				flipAtLeastOneValue,
				mutationRate,
				sequenceLength,
				nEvals,
				useShiftBuffer,
				opponentModelType,
				opponentBudgetShare
		);

	}

	private int[] createDimensions(boolean[] a, double[]b, int[] c, int[] d, boolean[] e, int[] f, double[] g){
		return new int[]{
				a.length,
				b.length,
				c.length,
				d.length,
				e.length,
				f.length,
				g.length
		};
	}

	private Param[] createParams(boolean[] a, double[]b, int[] c, int[] d, boolean[] e, int[] f, double[] g){
		return new Param[]{
				new BooleanParam().setArray(a).setName("Flip at least one"),
				new  DoubleParam().setArray(b).setName("Mutation rate"),
				new IntegerParam().setArray(c).setName("Sequence Length"),
				new IntegerParam().setArray(d).setName("Number of Evaluations"),
				new BooleanParam().setArray(e).setName("Use Shift Buffer"),
				new IntegerParam().setArray(f).setName("Opponent Model Type"),
				new  DoubleParam().setArray(g).setName("Opponent Budget Share"),
		};
	}

	public static void main(String[] args){
		SeededRHEAAParams p = new SeededRHEAAParams();
		String file = "agents/SeededRHEAParams.json";
		CompleteAnnotatedSearchSpace.save(file,p);
		CompleteAnnotatedSearchSpace l = CompleteAnnotatedSearchSpace.load(file);
		System.out.println(l);
	}

}
