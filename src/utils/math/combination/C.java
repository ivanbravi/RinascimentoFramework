package utils.math.combination;

public class C {
	private int n;
	private int k;

	public C(int n, int k){
		if(n<1 || k<0 || n<k){
			throw new RuntimeException("Illegal arguments");
		}

		this.n = n;
		this.k = k;
	}

	public static C c(int n, int k){
		return new C(n,k);
	}

	public int n(){
		return n;
	}

	public int k(){
		return k;
	}


	public boolean equals(Object o){
		if(o==null)
			return false;

		if (getClass() != o.getClass())
			return false;

		C c = (C) o;
		return this.k == c.k && this.n==c.n;
	}

}
