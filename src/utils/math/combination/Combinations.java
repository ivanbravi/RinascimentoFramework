package utils.math.combination;

public abstract class Combinations {

	protected int n;
	protected int k;

	public Combinations(int n, int k){
		this.n=n;
		this.k=k;
	}

	abstract public  long combinations(int n, int k);
	abstract public  int[] combination(int n, int k, int index);

}
