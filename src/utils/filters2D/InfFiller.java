package utils.filters2D;

public class InfFiller extends KernelBasedFilter2D {

	protected KernelBasedFilter2D howToFill;
	protected double decay;

	public InfFiller(KernelBasedFilter2D howToFill, double decay){
		super(howToFill.size);
		this.howToFill = howToFill;
		this.decay = decay;
	}

	@Override
	protected double applySingle(double[][] m, int x, int y) {
		if(m[x][y] != Double.NEGATIVE_INFINITY && m[x][y]!=Double.POSITIVE_INFINITY)
			return m[x][y];
		return decay*howToFill.applySingle(m, x, y);

	}
}
