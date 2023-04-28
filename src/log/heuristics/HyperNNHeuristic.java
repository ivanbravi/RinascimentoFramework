package log.heuristics;

import log.heuristics.nn.encog.creator.SeedlessSparseMultilayerCreator;
import java.util.Arrays;

public class HyperNNHeuristic extends NNHeuristic{

	long seed;
	boolean seedHasChanged = false;

	public HyperNNHeuristic(SeedlessSparseMultilayerCreator creator) {
		super(creator);
	}

	@Override
	public int dimensionality() {
		return super.dimensionality()+1;
	}

	@Override
	public void setWeights(double[] w) {
		if(w.length != dimensionality()){
			System.out.println("Invalid NN weights size! Expected:" + size + ", received:" + w.length);
			return;
		}
		long newSeed = (long) (w[0]*Long.MAX_VALUE);
		seedHasChanged = newSeed != seed;
		this.seed = newSeed;
		this.w = Arrays.copyOfRange(w,1,w.length);
		resetNetwork();
		nn.setWeights(this.w);
	}

	private void resetNetwork(){
		if(seedHasChanged){
			nn = ((SeedlessSparseMultilayerCreator) creator).createWithSeed(seed);
			seedHasChanged = false;
		}
	}

	public WeightedHeuristic clone(){
		HyperNNHeuristic clone = new HyperNNHeuristic((SeedlessSparseMultilayerCreator)creator);
		clone.seed = this.seed;
		if(this.w != null){
			clone.w = this.w;
			clone.setWeights(this.w);
		}
		return clone;
	}

	public static void main(String[] args) {
		long newSeed = (long) (-0.43*Long.MAX_VALUE);
		System.out.println(newSeed);
	}

}
