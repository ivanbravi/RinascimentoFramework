package utils.math.permutation;

public abstract class Permutations {

	int eCount;

	public Permutations(int elements){
		this.eCount = elements;
	}

	abstract public int permutations(int[] elements);
	abstract public int[] permutation(int[] elements,int index);

}
