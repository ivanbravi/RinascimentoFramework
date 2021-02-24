package utils.math.permutation;

import java.util.Arrays;

public class SimpleContainer {

	private P params;

	private int[][] permutations;
	private int size;

	SimpleContainer(P params, Permutations p){
		this.params = params;
		this.size = p.permutations(params.getConfiguration());
		this.permutations = new int[size][];
	}

	private boolean isIndexValid(int index){
		return index>=0 && index<size;
	}

	public boolean containsPermutation(int index){
		if(!isIndexValid(index))
			return false;
		return permutations[index]!=null;
	}

	public int[] getPermutation(int index){
		if(!isIndexValid(index))
			return null;
		return permutations[index];
	}

	public boolean isPermutationValid(int[] permutation){
		int [] counters = Arrays.copyOf(params.getConfiguration(),params.getConfiguration().length);


		for(int i=0; i<permutation.length; i++) {
			int index = Arrays.binarySearch(params.getConfiguration(), permutation[i]);
			if(index<0){
				return false;
			}
			counters[index]--;
		}

		for(int i=0; i<counters.length; i++) {
			if(counters[i]!=0)
				return false;
		}

		return true;
	}
}
