package utils.filters2D;


import utils.ops2D.Init2D;
import utils.ops2D.Math2D;

public class Normaliser implements Filter2D{
	@Override
	public double[][] apply(double[][] m) {
		double[][] c = Init2D.copyShape(m);

		double max = Math2D.max(m);
		double min = Math2D.min(m);

		for(int x=0; x<m.length; x++)
			for(int y=0; y<m[x].length; y++)
				c[x][y] = normaliseValue(m[x][y],min,max);

		return c;
	}

	public static double normaliseValue(double value, double min, double max){
		return (value-min)/(max-min);
	}
}
