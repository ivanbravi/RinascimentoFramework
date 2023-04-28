package utils.filters2D;

import java.awt.*;

public class Mean extends KernelBasedFilter2D{

	public Mean(int size) {
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
			if(!super.isCoordinateIllegal(pAbs, m.length-1, m[0].length-1)) {
				double value = m[pAbs.x][pAbs.y];
				if(handler.shouldSkip(value))
					continue;
				count++;
				acc += handler.handle(value);
			}
		}

		if(count==0)
			return handler.ifAllSkippedValue();

		return acc/count;
	}


}
