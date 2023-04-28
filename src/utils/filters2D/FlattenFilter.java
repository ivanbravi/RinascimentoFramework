package utils.filters2D;

import utils.filters2D.infinity.InfinityHandler;

public class FlattenFilter extends KernelBasedFilter2D{
	InfinityHandler handler;

	public FlattenFilter(InfinityHandler handler) {
		super(0);
		this.handler = handler;
	}

	@Override
	protected double applySingle(double[][] m, int x, int y) {
		return handler.handle(m[x][y]);
	}
}
