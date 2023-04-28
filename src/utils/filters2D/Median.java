package utils.filters2D;

import java.awt.*;
import java.util.ArrayList;

public class Median extends KernelBasedFilter2D{
	public Median(int size) {
		super(size);
	}

	@Override
	protected double applySingle(double[][] m, int x, int y) {
		ArrayList<Double> values = new ArrayList<>();
		Point pAbs = new Point();

		for(Point p: kernel){
			pAbs.x = x+p.x;
			pAbs.y = y+p.y;
			if(!isCoordinateIllegal(pAbs, m.length-1, m[0].length-1)) {
				double value = m[pAbs.x][pAbs.y];
				if(handler.shouldSkip(value))
					continue;
				values.add(handler.handle(value));
			}
		}

		if(values.isEmpty())
			return handler.ifAllSkippedValue();

		return utils.math.Median.compute(values.stream().mapToDouble(d -> d).toArray());
	}
}
