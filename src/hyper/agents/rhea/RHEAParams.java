package hyper.agents.rhea;

import hyper.utilities.CompleteAnnotatedSearchSpace;
import ntbea.params.BooleanParam;
import ntbea.params.DoubleParam;
import ntbea.params.IntegerParam;
import ntbea.params.Param;

public class RHEAParams extends CompleteAnnotatedSearchSpace {

	public RHEAParams(){
		boolean[] shiftBuffer = new boolean[]{false,true};
		int[] 	length = new int[]{1,2,3,5,10,20};
		int[] 	evaluations = new int[]{20,50,100,200};
		boolean[] flipAtLeast = new boolean[]{false,true};
		int[] 	mutationStyles = new int[]{0,1,2};
		int[] 	opponentType = new int[]{0,1,2};
		double[] 	opponentBudget = new double[]{0.005,0.01,0.02,0.05};
		double[] espMutationProb = new double[]{0.5,0.7,0.8,0.9};
		double[] gMean = new double[]{0.0,0.1,0.3,0.5,0.75};
		double[] gStdDev = new double[]{0.5,1,2};


		params = createParams(shiftBuffer,length,evaluations,flipAtLeast,mutationStyles,opponentType,opponentBudget,espMutationProb,gMean,gStdDev);

		dimensions = createDimensions(shiftBuffer,length,evaluations,flipAtLeast,mutationStyles,opponentType,opponentBudget,espMutationProb,gMean,gStdDev);
	}

	private Param[] createParams(boolean[] a, int[] b, int[] c,
								 boolean[] d, int[] e, int[] f, double[] g,
								 double[] h, double[] i, double[] l){
		return new Param[]{
				new BooleanParam().setArray(a).setName("Use Shift Buffer"),
				new IntegerParam().setArray(b).setName("Sequence Length"),
				new IntegerParam().setArray(c).setName("Number of Evaluations"),
				new BooleanParam().setArray(d).setName("Flip at least one"),
				new IntegerParam().setArray(e).setName("Mutation Style"),
				new IntegerParam().setArray(f).setName("Opponent Type"),
				new  DoubleParam().setArray(g).setName("Opponent Budget Share"),
				new  DoubleParam().setArray(h).setName("Exponential Decay"),
				new  DoubleParam().setArray(i).setName("Gaussian Mean"),
				new  DoubleParam().setArray(l).setName("Gaussian Standard Dev")
		};
	}

	private int[] createDimensions(boolean[] a, int[] b, int[] c,
								   boolean[] d, int[] e, int[] f, double[] g,
								   double[] h, double[] i, double[] l){
		return new int[]{
				a.length,
				b.length,
				c.length,
				d.length,
				e.length,
				f.length,
				g.length,
				h.length,
				i.length,
				l.length
		};
	}

	public static void main(String[] args){
		RHEAParams p = new RHEAParams();
		String file = "agents/RHEAParams.json";
		CompleteAnnotatedSearchSpace.save(file,p);
		CompleteAnnotatedSearchSpace l = CompleteAnnotatedSearchSpace.load(file);
		System.out.println(l);
	}

}
