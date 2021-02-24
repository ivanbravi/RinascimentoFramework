package hyper.utilities;

import log.Logger;

public class Distance {

	public int[] dimensionWiseDistance(int[] a, int[] b){
		if(!isSafe(a,b))
			return null;
		int[] d = new int[a.length];

		for(int i=0; i<d.length; i++){
			d[i] = delta(a[i],b[i]);
		}

		return d;
	}

	public double[] dimensionWiseDistance(double[] a, double[] b){
		if(!isSafe(a,b))
			return null;
		double[] d = new double[a.length];

		for(int i=0; i<d.length; i++){
			d[i] = delta(a[i],b[i]);
		}

		return d;
	}

	public static int manhattan(int[] a, int[] b){
		int d = 0;

		if(!isSafe(a,b))
			return d;

		for(int i=0; i<a.length; i++){
			d += delta(a[i],b[i]);
		}

		return d;
	}

	public static double manhattan(double[] a, double[] b){
		double d = 0;

		if(!isSafe(a,b))
			return d;

		for(int i=0; i<a.length; i++){
			d += delta(a[i],b[i]);
		}

		return d;
	}

	public static double eucledean(int[] a, int[] b){
		double d = 0;

		if(!isSafe(a,b))
			return d;

		for (int i=0; i<a.length; i++){
			d += Math.pow( delta(a[i],b[i]) ,2);
		}

		return Math.sqrt(d);
	}

	public static double eucledean(double[] a, double[] b){
		double d = 0;

		if(!isSafe(a,b))
			return d;

		for (int i=0; i<a.length; i++){
			d += Math.pow( delta(a[i],b[i]) ,2);
		}

		return Math.sqrt(d);
	}

	private static boolean isSafe(double[] a, double[] b){
		if(a==null || b==null)
			return false;
		if(a.length!=b.length)
			return false;
		return true;
	}

	private static boolean isSafe(int[] a, int[] b){
		if(a==null || b==null)
			return false;
		if(a.length!=b.length)
			return false;
		return true;
	}


	public static double delta(double a, double b){
		return Math.abs(a-b);
	}

	public static int delta(int a, int b){
		return Math.abs(a-b);
	}

	public static void main(String[] args){
		Logger.print(
		        manhattan(new int[]{1,2},new int[]{2,1}),
				eucledean(new int[]{1,2},new int[]{2,1}),
				manhattan(new double[]{1,2},new double[]{2,1}),
				eucledean(new double[]{1,2},new double[]{2,1}),
				manhattan(new int[]{1,1},new int[]{2,1}),
				eucledean(new int[]{1,1},new int[]{2,1}),
				manhattan(new double[]{1,1},new double[]{2,1}),
				eucledean(new double[]{1,1},new double[]{2,1})
		);
	}

}
