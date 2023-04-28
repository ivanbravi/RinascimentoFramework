package utils.filters2D.infinity;

public class DefaultHandler implements InfinityHandler{

	@Override
	public boolean shouldSkip(double value){
		return false;
	}

	@Override
	public double handle(double value){
		return value;
	}

	@Override
	public double ifAllSkippedValue(){
		return Double.NEGATIVE_INFINITY;
	}

}
