package utils.ops2D;

import static utils.ops2D.Math2D.sum;

public class Stats2D {

	public static double[][] normalise(double[][] m){
		double[][] r = Init2D.copyShape(m);
		double sum = sum(m);

		for(int x=0; x<m.length; x++)
			for(int y=0; y<m[x].length; y++)
				r[x][y] = m[x][y]/sum;

		return r;
	}

	public static double[][] mean(double[][] a, double[][] b){
		if(!Math2D.areCompatible(a,b))
			return null;

		double[][] r = Init2D.copyShape(a);
		for(int x=0; x<r.length; x++)
			for(int y=0; y<r[x].length; y++)
				r[x][y] = (a[x][y]+b[x][y])/2;

		return r;
	}

	public static double[][] additiveSmoothingNormalisation(double[][]m, double alpha){
		double[][] mPlus = Init2D.copyLegalise(m);
		double sum = sum(mPlus);
		int count = Init2D.count(mPlus);
		double[][] r = Init2D.copyShape(mPlus);
		for(int x=0; x<r.length; x++)
			for(int y=0; y<r[x].length; y++)
				r[x][y] = additiveSmoothingSingle(mPlus[x][y],alpha,count,sum);
		return r;
	}

	private static double additiveSmoothingSingle(double value, double alpha, double count, double total){
		return (value+alpha)/(total+alpha*count);
	}

	public static double jsDivergence(double[][] a, double[][] b){
		double[][] mean = mean(a,b);
		return 0.5 * klDivergence(a,mean) + 0.5 * klDivergence(b,mean);
	}

	public static double klDivergence(double[][] a, double[][] b){
		double sum = 0;

		if(!Math2D.areCompatible(a,b))
			throw new RuntimeException("Can't compute KL divergence over two incompatible matrices.");

		for(int x=0; x<a.length; x++)
			for(int y=0; y<a[x].length; y++)
				sum +=klDivergenceSingle(a[x][y],b[x][y]);

		return sum;
	}

	private static double klDivergenceSingle(double p, double q){
		return p * Math.log(p/q);
	}
}
