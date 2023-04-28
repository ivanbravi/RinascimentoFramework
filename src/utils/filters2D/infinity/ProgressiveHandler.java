package utils.filters2D.infinity;

public class ProgressiveHandler implements InfinityHandler {
	@Override
	public boolean shouldSkip(double value) {
		return value == Double.NEGATIVE_INFINITY || value==Double.POSITIVE_INFINITY;
	}

	@Override
	public double handle(double value) {
		return value;
	}

	@Override
	public double ifAllSkippedValue() {
		return Double.NEGATIVE_INFINITY;
	}
}
