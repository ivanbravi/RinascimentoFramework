package utils.filters2D.infinity;

public interface InfinityHandler {
	boolean shouldSkip(double value);
	double handle(double value);
	double ifAllSkippedValue();
}
