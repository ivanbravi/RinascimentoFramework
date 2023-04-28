package utils.filters2D;

import utils.math.Median;
import utils.ops2D.ToString2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mode extends  KernelBasedFilter2D{
	public Mode(int size) {
		super(size);
	}

	@Override
	protected double applySingle(double[][] m, int x, int y) {
		HashMap<Double, Integer> counters = new HashMap<>();
		int maxCount=0;

		// Count all the values in the neighbourhood
		Point pAbs = new Point();
		for(Point p: kernel){
			pAbs.x = x+p.x;
			pAbs.y = y+p.y;
			if(!super.isCoordinateIllegal(pAbs, m.length-1, m[0].length-1)) {
				double value = m[pAbs.x][pAbs.y];
				if(handler.shouldSkip(value))
					continue;
				value = handler.handle(value);
				int count = 1;
				if(counters.containsKey(value)){
					count = counters.get(value)+1;
				}
				counters.put(value,count);
				// Keep track of the max counter
				if(maxCount<count){
					maxCount = count;
				}
			}
		}

		if(counters.isEmpty())
			return handler.ifAllSkippedValue();

		// If multiple modes, select the median of these modes
		ArrayList<Double> modes = new ArrayList<>();
		for(Map.Entry<Double,Integer> e: counters.entrySet()){
			if(e.getValue()==maxCount)
				modes.add(e.getKey());
		}
		return Median.compute(modes.stream().mapToDouble(value -> value).toArray());
	}

	public static void main(String[] args) {
		double[][] r;
		String rString;
		int x = 4;
		int y = 1;
		double[][] m = new double[][]{
				new double[]{1,1,1,1,1,1},
				new double[]{1,1,1,1,1,1},
				new double[]{2,2,3,3,3,3},
				new double[]{1,1,1,3,3,3},
				new double[]{3,3,3,1,4,1},
				new double[]{2,4,2,2,2,2}
		};
		Mode mode = new Mode(1);
		System.out.println("r["+x+"]["+y+"] = "+mode.applySingle(m,x,y));
		r = mode.apply(m);
		rString = ToString2D.matrixToString(r,ToString2D.defaultFormat,false);
		System.out.println(rString);
	}
}
