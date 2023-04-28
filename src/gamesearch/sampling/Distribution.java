package gamesearch.sampling;

import utils.CopyToClipboard;

import java.util.Arrays;
import java.util.Random;

public class Distribution {

	public static class Gaussian{
		private static int samples = 1000000;

		public static double[] pdf(int center, int size, double sigma){
			Random r = new Random();
			double[] d = new double[size];
			boolean upperHalf = center < d.length/2;
			for(int i=0; i<samples; ){
				double g = Math.abs(r.nextGaussian());
				int x = (int) (g/3.0*sigma*d.length);
				if(upperHalf) {
					x = x+center;
					if (x < d.length && x >= 0) {
						d[x]++;
						i++;
					}
				}else{
					x = center-x;
					if (x < d.length && x >= 0) {
						d[x]++;
						i++;
					}
				}
			}

			if(upperHalf)
				for(int i=1; i<=center; i++)
					d[center-i] = d[center+i];
			else
				for(int i=1; i<d.length-center; i++)
					d[center+i] = d[center-i];

			normalise(d);


			return d;
		}
	}

	public static int nextIndex(double[] cdf, double rnd){
		for(int i=0; i<cdf.length; i++)
			if(rnd <= cdf[i])
				return i;
		return -1;
	}

	public static double[] cdf(double[] pdf){
		double[] r = new double[pdf.length];
		double acc = 0;
		for(int i=0; i<r.length; i++){
			acc += pdf[i];
			r[i] = acc;
		}
		return r;
	}

	private static void normalise(double[] d){
		double sum=0;
		for(int i=0; i<d.length; i++)
			sum+= d[i];
		for(int i=0; i<d.length; i++)
			d[i] = d[i]/sum;
	}

	public static void main(String[] args) {

		StringBuilder b = new StringBuilder();
//		double[] sigmas = new double[]{0.1, 0.6, 1, 1.6, 2, 6, 10};
//		int[] sizes = new int[]{5,8,13,18,25,30};
//		double[] centers = new double[]{0.0, 0.33, 0.5, 0.66, 1};
		double[] sigmas = new double[]{1};
		int[] sizes = new int[]{13};
		double[] centers = new double[]{0.66};

		System.out.println("|pdf|:"+(sigmas.length*sizes.length*centers.length));

		for(double sigma: sigmas) {
			for (int size: sizes) {
				for (double center: centers) {
					double[] pdf = Gaussian.pdf((int) (center*(size-1)), size, sigma);
					double[] cdf = cdf(pdf);
					b.append(Arrays.toString(pdf)).append(",\n");
					b.append(Arrays.toString(cdf)).append(",\n");
				}
			}
		}

		CopyToClipboard.copy(b.toString());
		System.out.println(b);
	}
}
