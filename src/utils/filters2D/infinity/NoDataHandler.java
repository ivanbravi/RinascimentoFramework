package utils.filters2D.infinity;

public class NoDataHandler implements InfinityHandler{
	@Override
	public boolean shouldSkip(double value) {
		return false;
	}

	@Override
	public double handle(double value) {
		if(Double.isInfinite(value))
			return 0;
		return value;
	}

	@Override
	public double ifAllSkippedValue() {
		return 0;
	}
}
