package utils.filters2D;

import utils.filters2D.infinity.DefaultHandler;
import utils.filters2D.infinity.InfinityHandler;
import utils.filters2D.infinity.ProgressiveHandler;

import java.awt.*;
import java.util.ArrayList;

public abstract class KernelBasedFilter2D implements Filter2D{

	protected Point[] kernel;
	protected int size;
	public static InfinityHandler handler = new ProgressiveHandler();

	public KernelBasedFilter2D(int size){
		kernel = createKernel(size);
		this.size = size;
	}



	protected abstract double applySingle(double[][] m, int x, int y);

	@Override
	public double[][] apply(double[][] m) {
		double[][] s = new double[m.length][];

		for(int i=0; i<s.length; i++)
			s[i] = new double[m[i].length];

		for(int x=0; x<s.length; x++)
			for(int y=0; y<s[x].length; y++)
				s[x][y] = applySingle(m, x, y);

		return s;
	}

	protected static boolean isCoordinateIllegal(Point p, int xMax, int yMax){
		return p.x<0 || p.x>xMax || p.y<0 || p.y>yMax;
	}

	protected static boolean isCoordinateLegal(Point p, int xMax, int yMax){
		return !isCoordinateIllegal(p,xMax,yMax);
	}

	private static Point[] createKernel(int size){
		ArrayList<Point> k = new ArrayList<>();

		for(int x=-size; x<=size; x++)
			for(int y=-size; y<=size; y++)
				k.add(new Point(x,y));

		return k.toArray(new Point[k.size()]);
	}
}
