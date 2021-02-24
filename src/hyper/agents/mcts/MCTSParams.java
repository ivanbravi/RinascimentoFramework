package hyper.agents.mcts;

import hyper.utilities.CompleteAnnotatedSearchSpace;
import ntbea.params.BooleanParam;
import ntbea.params.DoubleParam;
import ntbea.params.IntegerParam;
import ntbea.params.Param;

public class MCTSParams extends CompleteAnnotatedSearchSpace {

	public MCTSParams(){
		double[] exploration 			= new double[]	{0,1.41,4,9,15,20};
		int[] maxDepth 					= new int[]		{5,10,20,30,40};
		int[] opponentModel 			= new int[]		{0,1,2};
		double[] opponentBudgetRatio 	= new double[]	{0.01,0.05,0.1};
		double[] expansionProbability 	= new double[]	{0.1,0.2,0.3,0.4};
		int[] progressionSize 			= new int[]		{1,3,5,10,15};
		double[] epsilon 				= new double[]	{1e-6};
		int[] recommendationType 		= new int[]		{0,1,2};
		boolean[] rollWithOpponents 	= new boolean[]	{false,true};

		params = createParams(exploration, maxDepth, opponentModel, opponentBudgetRatio, expansionProbability, progressionSize, epsilon, recommendationType, rollWithOpponents);
		dimensions = createDimensions(exploration, maxDepth, opponentModel, opponentBudgetRatio, expansionProbability, progressionSize, epsilon, recommendationType, rollWithOpponents);
	}


	private Param[] createParams(double[] a, int[] b, int[] c, double[] d, double[] e, int[] f, double[] g, int[] h, boolean[] i){
		return new Param[]{
				new  DoubleParam().setArray(a).setName("Exploration Term"),
				new IntegerParam().setArray(b).setName("Tree/Rollout Depth"),
				new IntegerParam().setArray(c).setName("Opponent Model"),
				new  DoubleParam().setArray(d).setName("Opponent Budget Share"),
				new  DoubleParam().setArray(e).setName("Expansion Probability"),
				new IntegerParam().setArray(f).setName("Progression Width"),
				new  DoubleParam().setArray(g).setName("UCB Epsilon"),
				new IntegerParam().setArray(h).setName("Recommendation Type"),
				new BooleanParam().setArray(i).setName("Rollout With Opponents")
		};
	}

	private int[] createDimensions(double[] a, int[] b, int[] c, double[] d, double[] e, int[] f, double[] g, int[] h, boolean[] i){
		return new int[]{
				a.length,
				b.length,
				c.length,
				d.length,
				e.length,
				f.length,
				g.length,
				h.length,
				i.length
		};
	}

	public static void main(String[] args){
		CompleteAnnotatedSearchSpace.save("agents/MCTSParams.json",new MCTSParams());
	}

}
