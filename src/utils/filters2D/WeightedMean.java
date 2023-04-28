package utils.filters2D;

import java.awt.*;

public class WeightedMean extends KernelBasedFilter2D{
	public WeightedMean(int size) {
		super(size);
	}

	@Override
	protected double applySingle(double[][] m, int x, int y) {
		double acc = 0;
		int count = 0;
		Point pAbs = new Point();

		for(Point p: kernel){
			pAbs.x = x+p.x;
			pAbs.y = y+p.y;
			if(!isCoordinateIllegal(pAbs, m.length-1, m[0].length-1)) {
				double value = m[pAbs.x][pAbs.y];
				if(handler.shouldSkip(value))
					continue;
				int weight = 1+size-Math.max(Math.abs(p.x),Math.abs(p.y));
				count+=weight;
				acc += value*weight;
			}
		}

		if(count==0)
			return handler.ifAllSkippedValue();

		return acc/count;
	}
}
