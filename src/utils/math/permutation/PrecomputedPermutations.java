package utils.math.permutation;

import java.util.HashMap;

public class PrecomputedPermutations extends Permutations {

	HashMap<P, Integer> counters;
	//HashMap<P, >

	public PrecomputedPermutations(int elements){
		super(elements);
	}

	@Override
	public int permutations(int[] elements) {

		return 0;
	}

	@Override
	public int[] permutation(int[] elements,int index) {
		return new int[0];
	}
}
